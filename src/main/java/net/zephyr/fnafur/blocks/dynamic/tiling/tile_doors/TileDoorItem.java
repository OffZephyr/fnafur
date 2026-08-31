package net.zephyr.fnafur.blocks.dynamic.tiling.tile_doors;

import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.zephyr.fnafur.util.ItemUtil;

public class TileDoorItem extends BlockItem {
    public TileDoorItem(Block block, Properties settings) {
        super(block, settings);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        CompoundTag nbt = ItemUtil.getNbt(context.getItemInHand());

        if(!nbt.contains("pos1")){
            nbt.putLong("pos1", context.getClickedPos().relative(context.getClickedFace()).asLong());
            ItemUtil.setNbt(context.getItemInHand(), nbt.copy());
            return InteractionResult.SUCCESS;
        }
        else{
            nbt.putLong("pos2", context.getClickedPos().relative(context.getClickedFace()).asLong());
            ItemUtil.setNbt(context.getItemInHand(), nbt.copy());
            return super.useOn(context);
        }
    }


    public static Vec3i getMin(Vec3i pos1, Vec3i pos2){
        return new Vec3i(
                Math.min(pos1.getX(), pos2.getX()),
                Math.min(pos1.getY(), pos2.getY()),
                Math.min(pos1.getZ(), pos2.getZ())
        );
    }

    public static Vec3i getMax(Vec3i pos1, Vec3i pos2){
        return new Vec3i(
                Math.max(pos1.getX(), pos2.getX()),
                Math.max(pos1.getY(), pos2.getY()),
                Math.max(pos1.getZ(), pos2.getZ())
        );
    }

    public static Vec3i getDistance(Vec3i pos1, Vec3i pos2, Direction facing){
        Vec3i direction = facing.getUnitVec3i();

        Vec3i minPos = getMin(pos1, pos2);
        Vec3i maxPos = getMax(pos1, pos2);

        Vec3i distance =
                new Vec3i(
                        maxPos.getX() - minPos.getX(),
                        maxPos.getY() - minPos.getY(),
                        maxPos.getZ() - minPos.getZ()
                );

        return new Vec3i(
                distance.getX() * Math.abs(direction.getZ()),
                distance.getY(),
                distance.getZ() * Math.abs(direction.getX())
        );
    }
}