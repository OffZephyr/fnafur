package net.zephyr.fnafur.blocks.dynamic.tiling;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;

public class TableBlock extends HorizontalTilingBlock{
    public TableBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.box(0, 0.4375f, 0, 1, 1, 1);

        HorizontalTileStates type = state.getValue(TYPE);
        if((type.CONNECTS_SOUTH || type.CONNECTS_EAST) && !type.CONNECTS_NORTH && !type.CONNECTS_WEST)
            shape = Shapes.or(shape, Shapes.box(0, 0, 0, 0.15f, 0.4375f, 0.15f));
        if((type.CONNECTS_SOUTH || type.CONNECTS_WEST) && !type.CONNECTS_NORTH && !type.CONNECTS_EAST)
            shape = Shapes.or(shape, Shapes.box(0.85f, 0, 0, 1, 0.4375f, 0.15f));
        if((type.CONNECTS_NORTH || type.CONNECTS_EAST) && !type.CONNECTS_SOUTH && !type.CONNECTS_WEST)
            shape = Shapes.or(shape, Shapes.box(0, 0, 0.85f, 0.15f, 0.4375f, 1));
        if((type.CONNECTS_NORTH || type.CONNECTS_WEST) && !type.CONNECTS_SOUTH && !type.CONNECTS_EAST)
            shape = Shapes.or(shape, Shapes.box(0.85f, 0, 0.85f, 1, 0.4375f, 1));

        return shape;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return true;
    }
}
