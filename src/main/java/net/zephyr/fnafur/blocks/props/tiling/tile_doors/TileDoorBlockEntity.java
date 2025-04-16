package net.zephyr.fnafur.blocks.props.tiling.tile_doors;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class TileDoorBlockEntity extends BlockEntity {
    BlockState state;
    public double delta = 0;
    public TileDoorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.TILE_DOOR, pos, state);
        this.state = state;
    }

    public BlockState getDefaultState(){
        return this.state.getBlock().getDefaultState();
    }


    public void tick(World world, BlockPos pos, BlockState state, net.zephyr.fnafur.blocks.props.tiling.tile_doors.TileDoorBlockEntity blockEntity) {

    }

    public void setStatus(boolean open) {
        BlockPos mainPos = BlockPos.fromLong(((IEntityDataSaver) this).getPersistentData().getLong("main"));
        if (getWorld().getBlockEntity(mainPos) instanceof TileDoorBlockEntity entity) {
            BlockState state = getWorld().getBlockState(getPos());
            int width = ((IEntityDataSaver) entity).getPersistentData().getInt("width");
            int height = ((IEntityDataSaver) entity).getPersistentData().getInt("height");

            BlockPos testPos = mainPos.offset(state.get(TileDoorBlock.FACING).rotateYCounterclockwise());
            Direction direction = getWorld().getBlockState(testPos).getBlock() instanceof TileDoorBlock ? state.get(TileDoorBlock.FACING).rotateYCounterclockwise() : state.get(TileDoorBlock.FACING).rotateYClockwise();

            for (int x = 0; x <= width; x++) {
                for (int y = 0; y <= height; y++) {
                    BlockPos updatePos = mainPos.up(y).offset(direction, x);

                    if (getWorld().getBlockEntity(updatePos) instanceof TileDoorBlockEntity entity2) {
                        ((IEntityDataSaver) entity2).getPersistentData().putBoolean("open", open);
                    }
                }
            }
        }
    }
    public void setSpeed(float speed) {
        BlockPos mainPos = BlockPos.fromLong(((IEntityDataSaver) this).getPersistentData().getLong("main"));
        if (getWorld().getBlockEntity(mainPos) instanceof TileDoorBlockEntity entity) {
            BlockState state = getWorld().getBlockState(getPos());
            int width = ((IEntityDataSaver) entity).getPersistentData().getInt("width");
            int height = ((IEntityDataSaver) entity).getPersistentData().getInt("height");

            BlockPos testPos = mainPos.offset(state.get(TileDoorBlock.FACING).rotateYCounterclockwise());
            Direction direction = getWorld().getBlockState(testPos).getBlock() instanceof TileDoorBlock ? state.get(TileDoorBlock.FACING).rotateYCounterclockwise() : state.get(TileDoorBlock.FACING).rotateYClockwise();

            for (int x = 0; x <= width; x++) {
                for (int y = 0; y <= height; y++) {
                    BlockPos updatePos = mainPos.up(y).offset(direction, x);

                    if (getWorld().getBlockEntity(updatePos) instanceof TileDoorBlockEntity entity2) {
                        ((IEntityDataSaver) entity2).getPersistentData().putFloat("speed", speed);
                    }
                }
            }
        }
    }
}
