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
import net.zephyr.fnafur.blocks.props.wall_props.restroom_sign.RestroomSignSkins;

public class WallClouds extends WallPropBlock<DefaultPropColorEnum> {
    public WallClouds(Properties settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = switch (state.getValue(FACING)){
            default -> Shapes.box(-1, 0, 0, 2, 1.5f, 0.3f);
            case SOUTH -> Shapes.box(-1, 0, 0.7f, 2, 1.5f, 1);
            case WEST -> Shapes.box(0, 0, -1, 0.3f, 1.5f, 2);
            case EAST -> Shapes.box(0.7f, 0, -1, 1, 1.5f, 2);
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