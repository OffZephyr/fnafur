package net.zephyr.fnafur.blocks.props.wall_props.ac_unit;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;
import net.zephyr.fnafur.blocks.props.wall_props.restroom_sign.RestroomSignSkins;

public class AcUnit extends WallPropBlock<DefaultPropColorEnum> {
    public AcUnit(Properties settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = switch (state.getValue(FACING)){
            default -> Shapes.box(0, 0, 0, 1, 0.75f, 0.5f);
            case SOUTH -> Shapes.box(0, 0, 0.5f, 1, 0.75f, 1);
            case WEST -> Shapes.box(0, 0, 0, 0.5f, 0.75f, 1);
            case EAST -> Shapes.box(0.5f, 0, 0, 1, 0.75f, 1);
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
        return true;
    }
}
