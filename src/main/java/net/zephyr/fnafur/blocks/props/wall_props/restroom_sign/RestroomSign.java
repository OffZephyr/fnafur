package net.zephyr.fnafur.blocks.props.wall_props.restroom_sign;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;

public class RestroomSign extends WallPropBlock<RestroomSignSkins> {
    public RestroomSign(Properties settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = switch (state.getValue(FACING)){
            default -> Shapes.box(0.2f, 0.1f, 0, 0.8f, 0.9f, 0.1f);
            case SOUTH -> Shapes.box(0.2f, 0.1f, 0.9f, 0.8f, 0.9f, 1);
            case WEST -> Shapes.box(0, 0.1f, 0.2f, 0.1f, 0.9f, 0.8f);
            case EAST -> Shapes.box(0.9f, 0.1f, 0.2f, 1, 0.9f, 0.8f);
        };
        return drawingOutline ? shape : getInteractionShape(state, world, pos);
    }

    @Override
    public Class<RestroomSignSkins> COLOR_ENUM() {
        return RestroomSignSkins.class;
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
