package net.zephyr.fnafur.blocks.linking;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
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

    default void updateSources(World world, BlockPos pos){
        if(world instanceof ServerWorld sw){
            List<BlockPos> blockList = new ArrayList<>();
            List<Integer> idList = new ArrayList<>();
            for(IEntityDataSaver ent : getTargets()){
                if(ent instanceof BlockEntity bEnt){
                    blockList.add(bEnt.getPos());
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

            for(ServerPlayerEntity p : PlayerLookup.tracking(sw, pos)){
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
    default ActionResult makeLink(IEntityDataSaver link){
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

                return ActionResult.SUCCESS;
            }
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }
    default ActionResult unlink(IEntityDataSaver link){
        return unlink(link, true);
    }

    default ActionResult unlink(IEntityDataSaver link, boolean update){
        if(update){
            updateLinks();
        }

        if(link instanceof LinkTarget ent && canUnlink(link)){
            boolean bl = getTargets().remove(link);
            ent.removeSource((IEntityDataSaver)this);
            return bl ? ActionResult.SUCCESS : ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }

    default Vec3d getLinkPos(IEntityDataSaver link){
        if(link instanceof BlockEntity ent){
            return ent.getPos().toCenterPos();
        }
        else if(link instanceof Entity ent){
            return new Vec3d(ent.getX(), ent.getEyeY(), ent.getZ());
        }
        return Vec3d.ZERO;
    }
    default void updateLinks(){
        for(IEntityDataSaver link : getTargets()){
            if(link instanceof BlockEntity ent && ent.getWorld().getBlockEntity(ent.getPos()) == null){
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

    default void writeSourceData(WriteView view, World world){
        updateData(world);
        WriteView.ListAppender<BlockPos> pos = view.getListAppender("BlockEntity", BlockPos.CODEC);
        WriteView.ListAppender<Integer> id = view.getListAppender("Entity", Codec.INT);

        for(BlockPos p : blockTargetQueue()){
            pos.add(p);
        }
        for(int i : entityTargetQueue()){
            id.add(i);
        }

        for(IEntityDataSaver link : getTargets()){
            if(link instanceof BlockEntity ent){
                pos.add(ent.getPos());
            }
            else if(link instanceof Entity ent){
                id.add(ent.getId());
            }
        }
    }
    default void readSourceData(ReadView view, World world){
        ReadView.TypedListReadView<BlockPos> list = view.getTypedListView("BlockEntity", BlockPos.CODEC);
        ReadView.TypedListReadView<Integer> list2 = view.getTypedListView("Entity", Codec.INT);

        for(BlockPos pos : list){
            blockTargetQueue().add(pos);
        }
        for(int id : list2){
            entityTargetQueue().add(id);
        }

        updateData(world);

        allSources.add((IEntityDataSaver)this);
    }

    default void updateData(World world){
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
                if(world.getEntityById(id) instanceof Entity ent){
                    getTargets().add((IEntityDataSaver) ent);
                    if(ent instanceof LinkTarget t){
                        t.addSource((IEntityDataSaver)this);
                    }
                }
            }
        }
    }
    default ActionResult tryStartLink(PlayerEntity player, BlockPos pos){

        ItemStack stack = player.getMainHandStack();
        if(stack.getItem() instanceof WrenchItem && ((IUniversePlayer)player).isUsingVanniMask()){
            NbtCompound nbt = ItemUtil.getNbt(stack);

            BlockPos startPos = nbt.get("startLink", BlockPos.CODEC).orElse(BlockPos.ORIGIN);
            if(player.isSneaking()){
                nbt.remove("startLink");
                for(IEntityDataSaver ent : getTargets()){
                    unlink(ent);
                }
            }
            else if(startPos.equals(pos) || startPos != BlockPos.ORIGIN){
                nbt.remove("startLink");
            }
            else{
                nbt.put("startLink", BlockPos.CODEC, pos);
            }
            ItemUtil.setNbt(stack, nbt);
            return ActionResult.SUCCESS;
        }

        return null;
    }
}
