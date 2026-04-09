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

public class ToiletPaperRoll extends WallPropBlock<DefaultPropColorEnum> {
    public ToiletPaperRoll(Properties settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = switch (state.getValue(FACING)){
            default -> Shapes.box(0.25f, 0.2f, 0, 0.75f, 0.65f, 0.25f);
            case SOUTH -> Shapes.box(0.25f, 0.2f, 0.75f, 0.75f, 0.65f, 1f);
            case WEST -> Shapes.box(0, 0.2f, 0.25f, 0.25f, 0.65f, 0.75f);
            case EAST -> Shapes.box(0.75f, 0.2f, 0.25f, 1, 0.65f, 0.75f);
        };
        return drawingOutline ? shape : getInteractionShape(state, world, pos);
    }

    @Override
    public Class<DefaultPropColorEnum> COLOR_ENUM() {
        return null;
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
