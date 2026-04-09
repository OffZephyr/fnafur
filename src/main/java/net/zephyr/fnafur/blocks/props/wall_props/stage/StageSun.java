package net.zephyr.fnafur.blocks.props.wall_props.stage;

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

public class StageSun extends WallPropBlock<DefaultPropColorEnum> {
    public StageSun(Properties settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = switch (state.getValue(FACING)){
            default -> Shapes.box(-0.4, -0.5, 0, 1.4, 1.5f, 0.1f);
            case SOUTH -> Shapes.box(-0.4, -0.5, 0.9f, 1.4, 1.5f, 1);
            case WEST -> Shapes.box(0, -0.5, -0.4, 0.1f, 1.5f, 1.4);
            case EAST -> Shapes.box(0.9f, -0.5, -0.4, 1, 1.5f, 1.4);
        };
        return drawingOutline ? shape : Shapes.block();
    }

    @Override
    public Class<DefaultPropColorEnum> COLOR_ENUM() { return null;
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