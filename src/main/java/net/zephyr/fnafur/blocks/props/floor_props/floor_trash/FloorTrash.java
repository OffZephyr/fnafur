package net.zephyr.fnafur.blocks.props.floor_props.floor_trash;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.floor_props.floor_monitors.FloorMonitorColors;

public class FloorTrash extends FloorPropBlock<FloorTrashSkins> {
    public FloorTrash(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(COLOR_PROPERTY(), FloorTrashSkins.MODEL_1));
    }

    @Override
    public Class<FloorTrashSkins> COLOR_ENUM() {
        return FloorTrashSkins.class;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.create(new AABB(0, 0, 0, 1, 0.3, 1)));
        return drawingOutline ? shape : Shapes.block();
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return Shapes.block();
    }


    @Override
    public boolean rotates() {
        return false;
    }
}
