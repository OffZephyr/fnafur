package net.zephyr.fnafur.blocks.props.wall_props.stage;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;

public class StageSun extends WallPropBlock<DefaultPropColorEnum> {
    public StageSun(Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = switch (state.get(FACING)){
            default -> VoxelShapes.cuboid(-0.4, -0.5, 0, 1.4, 1.5f, 0.1f);
            case SOUTH -> VoxelShapes.cuboid(-0.4, -0.5, 0.9f, 1.4, 1.5f, 1);
            case WEST -> VoxelShapes.cuboid(0, -0.5, -0.4, 0.1f, 1.5f, 1.4);
            case EAST -> VoxelShapes.cuboid(0.9f, -0.5, -0.4, 1, 1.5f, 1.4);
        };
        return drawingOutline ? shape : VoxelShapes.fullCube();
    }

    @Override
    public Class<DefaultPropColorEnum> COLOR_ENUM() { return null;
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