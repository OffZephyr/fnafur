package net.zephyr.fnafur.blocks.linking;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.item.tools.WrenchItem;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

import java.util.ArrayList;
import java.util.List;

public interface LinkTarget {
    List<IEntityDataSaver> getSources();
    List<IEntityDataSaver> allTargets = new ArrayList<>();
    default<T extends IEntityDataSaver> void addSource(T source) {
        getSources().add(source);
    }
    default int getButtonId(IEntityDataSaver source){
        NbtCompound nbtButtonIndexHolder = ((IEntityDataSaver)this).getPersistentData().getCompound("buttonIndexData").orElse(new NbtCompound());
        if (!nbtButtonIndexHolder.isEmpty()){
            if(source instanceof BlockEntity ent){
                String name = "" + ent.getPos().asLong();
                if(nbtButtonIndexHolder.contains(name)) {
                    return nbtButtonIndexHolder.getInt(name, -1);
                }
            }
        }
        return -1;
    }

    default<T extends IEntityDataSaver> void removeSource(T source) {
        while(getSources().contains(source)){
            getSources().remove(source);
        }
    }
    default<T extends IEntityDataSaver> boolean isTargetOf(T source) {
        return getSources().contains(source);
    }

    int getUpdateDepth();
    void setUpdateDepth(int depth);
    default void updateStatus(World world, BlockPos sourcePos, IEntityDataSaver source){
        if(this instanceof BlockEntity ent){
            world.updateNeighbors(ent.getPos(), world.getBlockState(ent.getPos()).getBlock());
        }

        setUpdateDepth(getUpdateDepth() + 1);
        if(getUpdateDepth() >= 2) return;
        if(this instanceof LinkSource s){
            for(IEntityDataSaver ent : s.getTargets()){
                if(ent instanceof LinkTarget t){
                    t.updateStatus(world, sourcePos, source);
                }
            }
        }
        setUpdateDepth(0);
    }

    default void writeData(WriteView view, World world){
        WriteView.ListAppender<BlockPos> pos = view.getListAppender("Sources", BlockPos.CODEC);

        for(IEntityDataSaver link : getSources()){
            if(link instanceof BlockEntity ent){
                pos.add(ent.getPos());
            }
        }
    }
    default void readData(ReadView view, World world){
        if(!allTargets.contains((IEntityDataSaver)this)){
            allTargets.add((IEntityDataSaver)this);
        }

        ReadView.TypedListReadView<BlockPos> list = view.getTypedListView("Sources", BlockPos.CODEC);

        if(world != null){
            for(BlockPos pos : list){
                if(world.getBlockEntity(pos) instanceof LinkSource s){
                    getSources().add((IEntityDataSaver)s);
                    if(!s.getTargets().contains((IEntityDataSaver)this)){
                        s.getTargets().add((IEntityDataSaver)this);
                    }
                }
            }
        }
    }
    default void cleanSources(){

        if(!getSources().isEmpty()){
            for(int i = 0; i < getSources().size(); i++){
                if(getSources().get(i) instanceof IEntityDataSaver ent && ent instanceof LinkSource s){
                    s.getTargets().remove(((IEntityDataSaver) this));
                    getSources().remove(ent);
                }
            }
        }
        LinkTarget.allTargets.remove((IEntityDataSaver) this);
    }

    default ActionResult tryEndLink(PlayerEntity player, World world, BlockPos pos){

        ItemStack stack = player.getMainHandStack();
        if(player.getMainHandStack().getItem() instanceof WrenchItem){
            NbtCompound nbt = ItemUtil.getNbt(stack);

            BlockPos startPos = nbt.get("startLink", BlockPos.CODEC).orElse(BlockPos.ORIGIN);

            if(!nbt.contains("startLink")){
                return null;
            }
            else if(!startPos.equals(pos)){
                if(world.getBlockEntity(startPos) instanceof BlockEntity source){
                    if(((LinkSource)source).getTargets().contains((IEntityDataSaver)this)){

                        NbtCompound nbtButtonIndexHolder = ((IEntityDataSaver)this).getPersistentData().getCompoundOrEmpty("buttonIndexData");
                        if(nbtButtonIndexHolder.contains("" + source.getPos().asLong())) nbtButtonIndexHolder.remove("" + source.getPos().asLong());
                        ((IEntityDataSaver) this).getPersistentData().put("buttonIndexData", nbtButtonIndexHolder);

                        ((LinkSource)source).unlink((IEntityDataSaver) this);
                    }
                    else{
                        ((LinkSource)source).makeLink((IEntityDataSaver) this);
                    }
                    if(this instanceof BlockEntity ent){
                        if(nbt.contains("buttonIndex")) {
                            int buttonIndex = nbt.getInt("buttonIndex", 0);

                            NbtCompound nbtButtonIndexHolder = ((IEntityDataSaver)this).getPersistentData().getCompoundOrEmpty("buttonIndexData");
                            nbtButtonIndexHolder.putInt("" + source.getPos().asLong(), buttonIndex);
                            ((IEntityDataSaver) this).getPersistentData().put("buttonIndexData", nbtButtonIndexHolder);
                        }
                        ent.markDirty();
                    }

                    source.markDirty();
                }
            }

            nbt.remove("startLink");
            nbt.remove("buttonIndex");
            ItemUtil.setNbt(stack, nbt);
            return ActionResult.SUCCESS;
        }
        return null;
    }
}
