package net.zephyr.fnafur.blocks.props.wall_props.wall_papers;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;
import net.zephyr.fnafur.blocks.props.wall_props.poster.PosterTextures;

public class WallPapers extends WallPropBlock<WallPapersTextures> {
    public WallPapers(Properties settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = switch (state.getValue(FACING)){
            default -> Shapes.box(0f, 0, 0, 1f, 1, 0.1f);
        };
        return drawingOutline ? shape : getInteractionShape(state, world, pos);
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
    public boolean goesOnFloor(BlockItemStateProperties state) {
        return false;
    }
}
