package net.zephyr.fnafur.blocks.linking;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
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
        CompoundTag nbtButtonIndexHolder = ((IEntityDataSaver)this).getPersistentData().getCompound("buttonIndexData").orElse(new CompoundTag());
        if (!nbtButtonIndexHolder.isEmpty()){
            if(source instanceof BlockEntity ent){
                String name = "" + ent.getBlockPos().asLong();
                if(nbtButtonIndexHolder.contains(name)) {
                    return nbtButtonIndexHolder.getIntOr(name, -1);
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
    default void updateStatus(Level world, BlockPos sourcePos, IEntityDataSaver source){
        if(this instanceof BlockEntity ent){
            world.updateNeighborsAt(ent.getBlockPos(), world.getBlockState(ent.getBlockPos()).getBlock());
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

    default void writeData(ValueOutput view, Level world){
        ValueOutput.TypedOutputList<BlockPos> pos = view.list("Sources", BlockPos.CODEC);

        for(IEntityDataSaver link : getSources()){
            if(link instanceof BlockEntity ent){
                pos.add(ent.getBlockPos());
            }
        }
    }
    default void readData(ValueInput view, Level world){
        if(!allTargets.contains((IEntityDataSaver)this)){
            allTargets.add((IEntityDataSaver)this);
        }

        ValueInput.TypedInputList<BlockPos> list = view.listOrEmpty("Sources", BlockPos.CODEC);

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

    default InteractionResult tryEndLink(Player player, Level world, BlockPos pos){

        ItemStack stack = player.getMainHandItem();
        if(player.getMainHandItem().getItem() instanceof WrenchItem){
            CompoundTag nbt = ItemUtil.getNbt(stack);

            BlockPos startPos = nbt.read("startLink", BlockPos.CODEC).orElse(BlockPos.ZERO);

            if(!nbt.contains("startLink")){
                return null;
            }
            else if(!startPos.equals(pos)){
                if(world.getBlockEntity(startPos) instanceof BlockEntity source){
                    if(((LinkSource)source).getTargets().contains((IEntityDataSaver)this)){

                        CompoundTag nbtButtonIndexHolder = ((IEntityDataSaver)this).getPersistentData().getCompoundOrEmpty("buttonIndexData");
                        if(nbtButtonIndexHolder.contains("" + source.getBlockPos().asLong())) nbtButtonIndexHolder.remove("" + source.getBlockPos().asLong());
                        ((IEntityDataSaver) this).getPersistentData().put("buttonIndexData", nbtButtonIndexHolder);

                        ((LinkSource)source).unlink((IEntityDataSaver) this);
                    }
                    else{
                        ((LinkSource)source).makeLink((IEntityDataSaver) this);
                    }
                    if(this instanceof BlockEntity ent){
                        if(nbt.contains("buttonIndex")) {
                            int buttonIndex = nbt.getIntOr("buttonIndex", 0);

                            CompoundTag nbtButtonIndexHolder = ((IEntityDataSaver)this).getPersistentData().getCompoundOrEmpty("buttonIndexData");
                            nbtButtonIndexHolder.putInt("" + source.getBlockPos().asLong(), buttonIndex);
                            ((IEntityDataSaver) this).getPersistentData().put("buttonIndexData", nbtButtonIndexHolder);
                        }
                        ent.setChanged();
                    }

                    source.setChanged();
                }
            }

            nbt.remove("startLink");
            nbt.remove("buttonIndex");
            ItemUtil.setNbt(stack, nbt);
            return InteractionResult.SUCCESS;
        }
        return null;
    }
}
