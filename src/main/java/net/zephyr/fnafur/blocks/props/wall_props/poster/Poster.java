package net.zephyr.fnafur.blocks.props.wall_props.poster;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;

public class Poster extends WallPropBlock<PosterTextures> {
    public Poster(Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = switch (state.get(FACING)){
            default -> VoxelShapes.cuboid(0f, 0, 0, 1f, 1, 0.1f);
            case SOUTH -> VoxelShapes.cuboid(0f, 0, 0.9f, 1f, 1, 1);
            case WEST -> VoxelShapes.cuboid(0, 0, 0f, 0.1f, 1, 1f);
            case EAST -> VoxelShapes.cuboid(0.9f, 0, 0f, 1, 1, 1f);
        };
        return drawingOutline ? shape : getRaycastShape(state, world, pos);
    }

    @Override
    public Class<PosterTextures> COLOR_ENUM() {
        return PosterTextures.class;
    }

    @Override
    public boolean lockY(BlockState state) {
        return false;
    }

    @Override
    public boolean goesOnFloor(BlockStateComponent state) {
        return false;
    }
}
