package net.zephyr.fnafur.blocks.props.floor_props.floor_monitors;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;

public class FloorMonitors1 extends FloorPropBlock<FloorMonitorColors> {
    public FloorMonitors1(Settings settings) {
        super(settings);
    }

    @Override
    public Class<FloorMonitorColors> COLOR_ENUM() {
        return FloorMonitorColors.class;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(new Box(0, 0, 0, 1, 1.35, 0.8)));
        return drawingOutline ? shape : VoxelShapes.fullCube();
    }

    @Override
    protected VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.fullCube();
    }


    @Override
    public boolean rotates() {
        return false;
    }
}
