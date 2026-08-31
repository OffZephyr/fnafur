package net.zephyr.fnafur.blocks.props.wall_props.kitchen;

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
import net.zephyr.fnafur.blocks.props.wall_props.restroom_sign.RestroomSignSkins;

public class PotsAndPansRack extends WallPropBlock<DefaultPropColorEnum> {
    public PotsAndPansRack(Properties settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = switch (state.getValue(FACING)){
            default -> Shapes.box(-0.4, -0.2, 0, 1.4, 0.8f, 0.4f);
            case SOUTH -> Shapes.box(-0.4, -0.2, 0.6f, 1.4, 0.8f, 1f);
            case WEST -> Shapes.box(0, -0.2, -0.4, 0.4f, 0.8f, 1.4f);
            case EAST -> Shapes.box(0.6, -0.2, -0.4, 1.01f, 0.8f, 1.4f);
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