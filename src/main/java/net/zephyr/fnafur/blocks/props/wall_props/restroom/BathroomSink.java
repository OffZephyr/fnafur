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

public class BathroomSink extends WallPropBlock<BathroomSinkSkins> {
    public BathroomSink(Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = switch (state.get(FACING)){
            default -> VoxelShapes.cuboid(0.05f, 0.3f, 0, 0.94f, 1.35f, 0.76f);
            case SOUTH -> VoxelShapes.cuboid(0.06f, 0.3f, 0.24f, 0.94f, 1.35f, 1f);
            case WEST -> VoxelShapes.cuboid(0, 0.3f, 0.06f, 0.75f, 1.35f, 0.94f);
            case EAST -> VoxelShapes.cuboid(0.25f, 0.3f, 0.065f, 1, 1.35f, 0.935f);
        };
        return drawingOutline ? shape : getRaycastShape(state, world, pos);
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
    public boolean goesOnFloor(BlockStateComponent state) {
        return false;
    }
}
