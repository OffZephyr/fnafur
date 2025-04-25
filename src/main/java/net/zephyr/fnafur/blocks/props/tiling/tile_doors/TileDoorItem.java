package net.zephyr.fnafur.blocks.props.tiling.tile_doors;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.zephyr.fnafur.util.ItemNbtUtil;

public class TileDoorItem extends BlockItem {
    public TileDoorItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        NbtCompound nbt = ItemNbtUtil.getNbt(context.getStack());

        if(!nbt.contains("pos1")){
            nbt.putLong("pos1", context.getBlockPos().offset(context.getSide()).asLong());
            ItemNbtUtil.setNbt(context.getStack(), nbt.copy());
            return ActionResult.SUCCESS;
        }
        else{
            nbt.putLong("pos2", context.getBlockPos().offset(context.getSide()).asLong());
            ItemNbtUtil.setNbt(context.getStack(), nbt.copy());
            return super.useOnBlock(context);
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
        Vec3i direction = facing.getVector();

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