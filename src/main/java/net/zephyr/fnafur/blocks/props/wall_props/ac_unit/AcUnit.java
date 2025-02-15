package net.zephyr.fnafur.blocks.props.wall_props.ac_unit;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;
import net.zephyr.fnafur.blocks.props.wall_props.restroom_sign.RestroomSignSkins;

public class AcUnit extends WallPropBlock<DefaultPropColorEnum> {
    public AcUnit(Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = switch (state.get(FACING)){
            default -> VoxelShapes.cuboid(0, 0, 0, 1, 0.75f, 0.5f);
            case SOUTH -> VoxelShapes.cuboid(0, 0, 0.5f, 1, 0.75f, 1);
            case WEST -> VoxelShapes.cuboid(0, 0, 0, 0.5f, 0.75f, 1);
            case EAST -> VoxelShapes.cuboid(0.5f, 0, 0, 1, 0.75f, 1);
        };
        return drawingOutline ? shape : VoxelShapes.fullCube();
    }

    @Override
    public Class<DefaultPropColorEnum> COLOR_ENUM() {
        return DefaultPropColorEnum.class;
    }

    @Override
    public boolean lockY(BlockState state) {
        return false;
    }

    @Override
    public boolean goesOnFloor(BlockStateComponent state) {
        return true;
    }
}
