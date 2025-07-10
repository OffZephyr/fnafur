package net.zephyr.fnafur.blocks.linking;

import com.mojang.serialization.Codec;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

import java.util.ArrayList;
import java.util.List;

public interface LinkSource {
    List<IEntityDataSaver> allSources = new ArrayList<>();
    List<IEntityDataSaver> getTargets();
    List<BlockPos> blockTargetQueue();
    List<Integer> entityTargetQueue();

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

    default void writeData(WriteView view, World world){
        updateData(world);

        for(IEntityDataSaver link : getTargets()){
            if(link instanceof BlockEntity ent){
                view.getListAppender("BlockEntity", BlockPos.CODEC).add(ent.getPos());
            }
            else if(link instanceof Entity ent){
                view.getListAppender("Entity", Codec.INT).add(ent.getId());
            }
        }
    }
    default void readData(ReadView view, World world){
        ReadView.TypedListReadView<BlockPos> list = view.getTypedListView("BlockEntity", BlockPos.CODEC);
        ReadView.TypedListReadView<Integer> list2 = view.getTypedListView("Entity", Codec.INT);

        for(BlockPos pos : list){
            blockTargetQueue().add(pos);
        }
        for(int id : list2){
            entityTargetQueue().add(id);
        }

        updateData(world);
        updateLinks();

        if(!allSources.contains((IEntityDataSaver)this)){
            allSources.add((IEntityDataSaver)this);
        }
    }

    default void updateData(World world){
        if(world != null){
            for(BlockPos pos : blockTargetQueue()){
                if(world.getBlockEntity(pos) instanceof BlockEntity ent) {
                    getTargets().add((IEntityDataSaver) ent);
                    if (ent instanceof LinkTarget t) {
                        t.addSource((IEntityDataSaver)this);
                    }
                }
            }
            blockTargetQueue().clear();
            for(int id : entityTargetQueue()){
                if(world.getEntityById(id) instanceof Entity ent){
                    getTargets().add((IEntityDataSaver) ent);
                    if(ent instanceof LinkTarget t){
                        t.addSource((IEntityDataSaver)this);
                    }
                }
            }
            entityTargetQueue().clear();
        }
    }
}
