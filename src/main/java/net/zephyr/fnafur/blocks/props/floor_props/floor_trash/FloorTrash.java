package net.zephyr.fnafur.blocks.props.floor_props.floor_trash;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.floor_props.floor_monitors.FloorMonitorColors;

public class FloorTrash extends FloorPropBlock<FloorTrashSkins> {
    public FloorTrash(Settings settings) {
        super(settings);
    }

    @Override
    public Class<FloorTrashSkins> COLOR_ENUM() {
        return FloorTrashSkins.class;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(new Box(0, 0, 0, 1, 0.3, 1)));
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
