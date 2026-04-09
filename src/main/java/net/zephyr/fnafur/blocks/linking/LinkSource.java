package net.zephyr.fnafur.blocks.linking;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.item.tools.WrenchItem;
import net.zephyr.fnafur.networking.block.LinkVisualUpdateS2CPayload;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.fnafur.util.mixinAccessing.IUniversePlayer;

import java.util.ArrayList;
import java.util.List;

public interface LinkSource {
    List<IEntityDataSaver> allSources = new ArrayList<>();
    List<IEntityDataSaver> getTargets();
    List<BlockPos> blockTargetQueue();
    List<Integer> entityTargetQueue();

    int getSourceAmountSync();
    void setSourceAmountSync(int num);

    default void updateSources(Level world, BlockPos pos){
        if(world instanceof ServerLevel sw){
            List<BlockPos> blockList = new ArrayList<>();
            List<Integer> idList = new ArrayList<>();
            for(IEntityDataSaver ent : getTargets()){
                if(ent instanceof BlockEntity bEnt){
                    blockList.add(bEnt.getBlockPos());
                }
                if(ent instanceof Entity eEnt){
                    idList.add(eEnt.getId());
                }
            }
            long[] blocks = new long[blockList.size()];
            long[] ids = new long[idList.size()];

            for(int i = 0; i < blocks.length; i++){
                blocks[i] = blockList.get(i).asLong();
            }
            for(int i = 0; i < ids.length; i++){
                ids[i] = idList.get(i);
            }

            for(ServerPlayer p : PlayerLookup.tracking(sw, pos)){
                ServerPlayNetworking.send(p, new LinkVisualUpdateS2CPayload(pos.asLong(), blocks, ids, getTargets().size()));
            }
        }
    }
    default boolean canLink(IEntityDataSaver link){
        return true;
    }
    default boolean canUnlink(IEntityDataSaver link){
        return true;
    }
    default InteractionResult makeLink(IEntityDataSaver link){
        updateLinks();

        if(link instanceof LinkTarget ent && canLink(link)){
            if(!getTargets().contains(link)){
                getTargets().add(link);
                ent.addSource((IEntityDataSaver)this);

                if(((IEntityDataSaver)this).getPersistentData().contains("synced")){
                    ((IEntityDataSaver)this).getPersistentData().remove("synced");
                }
                if(link.getPersistentData().contains("synced")){
                    link.getPersistentData().remove("synced");
                }

                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }
    default InteractionResult unlink(IEntityDataSaver link){
        return unlink(link, true);
    }

    default InteractionResult unlink(IEntityDataSaver link, boolean update){
        if(update){
            updateLinks();
        }

        if(link instanceof LinkTarget ent && canUnlink(link)){
            boolean bl = getTargets().remove(link);
            ent.removeSource((IEntityDataSaver)this);
            return bl ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }

    default Vec3 getLinkPos(IEntityDataSaver link){
        if(link instanceof BlockEntity ent){
            return ent.getBlockPos().getCenter();
        }
        else if(link instanceof Entity ent){
            return new Vec3(ent.getX(), ent.getEyeY(), ent.getZ());
        }
        return Vec3.ZERO;
    }
    default void updateLinks(){
        for(IEntityDataSaver link : getTargets()){
            if(link instanceof BlockEntity ent && ent.getLevel().getBlockEntity(ent.getBlockPos()) == null){
                unlink(link, false);
                continue;
            }
            if(link instanceof LinkTarget target){
                if(!target.isTargetOf((IEntityDataSaver)this)){
                    unlink(link);
                }
            }
        }
    }

    default void writeSourceData(ValueOutput view, Level world){
        updateData(world);
        ValueOutput.TypedOutputList<BlockPos> pos = view.list("BlockEntity", BlockPos.CODEC);
        ValueOutput.TypedOutputList<Integer> id = view.list("Entity", Codec.INT);

        for(BlockPos p : blockTargetQueue()){
            pos.add(p);
        }
        for(int i : entityTargetQueue()){
            id.add(i);
        }

        for(IEntityDataSaver link : getTargets()){
            if(link instanceof BlockEntity ent){
                pos.add(ent.getBlockPos());
            }
            else if(link instanceof Entity ent){
                id.add(ent.getId());
            }
        }
    }
    default void readSourceData(ValueInput view, Level world){
        ValueInput.TypedInputList<BlockPos> list = view.listOrEmpty("BlockEntity", BlockPos.CODEC);
        ValueInput.TypedInputList<Integer> list2 = view.listOrEmpty("Entity", Codec.INT);

        for(BlockPos pos : list){
            blockTargetQueue().add(pos);
        }
        for(int id : list2){
            entityTargetQueue().add(id);
        }

        updateData(world);

        allSources.add((IEntityDataSaver)this);
    }

    default void updateData(Level world){
        if(world != null){
            List<BlockPos> remBlockList = new ArrayList<>();
            for(BlockPos pos : blockTargetQueue()){
                if(world.getBlockEntity(pos) instanceof BlockEntity ent) {
                    getTargets().add((IEntityDataSaver) ent);
                    if (ent instanceof LinkTarget t) {
                        t.addSource((IEntityDataSaver)this);
                        remBlockList.add(pos);
                    }
                }
            }
            for(BlockPos pos : remBlockList){
                blockTargetQueue().remove(pos);
            }
            remBlockList.clear();

            List<Integer> remIdList = new ArrayList<>();
            for(int id : entityTargetQueue()){
                if(world.getEntity(id) instanceof Entity ent){
                    getTargets().add((IEntityDataSaver) ent);
                    if(ent instanceof LinkTarget t){
                        t.addSource((IEntityDataSaver)this);
                    }
                }
            }
        }
    }
    default InteractionResult tryStartLink(Player player, BlockPos pos){

        ItemStack stack = player.getMainHandItem();
        if(stack.getItem() instanceof WrenchItem && ((IUniversePlayer)player).isUsingVanniMask()){
            CompoundTag nbt = ItemUtil.getNbt(stack);

            BlockPos startPos = nbt.read("startLink", BlockPos.CODEC).orElse(BlockPos.ZERO);
            if(player.isShiftKeyDown()){
                nbt.remove("startLink");
                for(IEntityDataSaver ent : getTargets()){
                    unlink(ent);
                }
            }
            else if(startPos.equals(pos) || startPos != BlockPos.ZERO){
                nbt.remove("startLink");
            }
            else{
                nbt.store("startLink", BlockPos.CODEC, pos);
            }
            ItemUtil.setNbt(stack, nbt);
            return InteractionResult.SUCCESS;
        }

        return null;
    }
}
