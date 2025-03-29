package net.zephyr.fnafur.blocks.props.wall_props.punch_in_cards;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;

public class PunchInCards extends WallPropBlock<DefaultPropColorEnum> {
    public PunchInCards(Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = switch (state.get(FACING)){
            default -> VoxelShapes.cuboid(0.2f, 0.1f, 0, 0.8f, 0.9f, 0.1f);
            case SOUTH -> VoxelShapes.cuboid(0.2f, 0.1f, 0.9f, 0.8f, 0.9f, 1);
            case WEST -> VoxelShapes.cuboid(0, 0.1f, 0.2f, 0.1f, 0.9f, 0.8f);
            case EAST -> VoxelShapes.cuboid(0.9f, 0.1f, 0.2f, 1, 0.9f, 0.8f);
        };
        return drawingOutline ? shape : VoxelShapes.fullCube();
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
    public boolean goesOnFloor(BlockStateComponent state) {
        return false;
    }
}
