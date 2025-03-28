package net.zephyr.fnafur.blocks.props.tiling;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class TableBlock extends HorizontalTilingBlock{
    public TableBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = VoxelShapes.cuboid(0, 0.4375f, 0, 1, 1, 1);

        HorizontalTileStates type = state.get(TYPE);
        if((type.CONNECTS_SOUTH || type.CONNECTS_EAST) && !type.CONNECTS_NORTH && !type.CONNECTS_WEST)
            shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0, 0, 0.15f, 0.4375f, 0.15f));
        if((type.CONNECTS_SOUTH || type.CONNECTS_WEST) && !type.CONNECTS_NORTH && !type.CONNECTS_EAST)
            shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.85f, 0, 0, 1, 0.4375f, 0.15f));
        if((type.CONNECTS_NORTH || type.CONNECTS_EAST) && !type.CONNECTS_SOUTH && !type.CONNECTS_WEST)
            shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0, 0.85f, 0.15f, 0.4375f, 1));
        if((type.CONNECTS_NORTH || type.CONNECTS_WEST) && !type.CONNECTS_SOUTH && !type.CONNECTS_EAST)
            shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.85f, 0, 0.85f, 1, 0.4375f, 1));

        return shape;
    }
}
