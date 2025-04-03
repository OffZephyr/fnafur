package net.zephyr.fnafur.blocks.props.wall_props.restroom;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;

public class ToiletPaperRoll extends WallPropBlock<DefaultPropColorEnum> {
    public ToiletPaperRoll(Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = switch (state.get(FACING)){
            default -> VoxelShapes.cuboid(0.25f, 0.2f, 0, 0.75f, 0.65f, 0.25f);
            case SOUTH -> VoxelShapes.cuboid(0.25f, 0.2f, 0.75f, 0.75f, 0.65f, 1f);
            case WEST -> VoxelShapes.cuboid(0, 0.2f, 0.25f, 0.25f, 0.65f, 0.75f);
            case EAST -> VoxelShapes.cuboid(0.75f, 0.2f, 0.25f, 1, 0.65f, 0.75f);
        };
        return drawingOutline ? shape : getRaycastShape(state, world, pos);
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
