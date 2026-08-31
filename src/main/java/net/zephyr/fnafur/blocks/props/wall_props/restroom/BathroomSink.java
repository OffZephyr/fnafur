package net.zephyr.fnafur.blocks.props.wall_props.restroom;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;

public class BathroomSink extends WallPropBlock<BathroomSinkSkins> {
    public BathroomSink(Properties settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = switch (state.getValue(FACING)){
            default -> Shapes.box(0.05f, 0.3f, 0, 0.94f, 1.35f, 0.76f);
            case SOUTH -> Shapes.box(0.06f, 0.3f, 0.24f, 0.94f, 1.35f, 1f);
            case WEST -> Shapes.box(0, 0.3f, 0.06f, 0.75f, 1.35f, 0.94f);
            case EAST -> Shapes.box(0.25f, 0.3f, 0.065f, 1, 1.35f, 0.935f);
        };
        return drawingOutline ? shape : getInteractionShape(state, world, pos);
    }

    @Override
    public Class<BathroomSinkSkins> COLOR_ENUM() {
        return BathroomSinkSkins.class;
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
