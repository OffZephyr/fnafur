package net.zephyr.fnafur.blocks.props.wall_props.wall_papers;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;
import net.zephyr.fnafur.blocks.props.wall_props.poster.PosterTextures;

public class WallPapers extends WallPropBlock<WallPapersTextures> {
    public WallPapers(Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = switch (state.get(FACING)){
            default -> VoxelShapes.cuboid(0f, 0, 0, 1f, 1, 0.1f);
        };
        return drawingOutline ? shape : getRaycastShape(state, world, pos);
    }

    @Override
    public Class<WallPapersTextures> COLOR_ENUM() {
        return WallPapersTextures.class;
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
