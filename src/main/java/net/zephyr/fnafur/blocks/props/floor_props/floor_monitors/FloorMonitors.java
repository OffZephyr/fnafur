package net.zephyr.fnafur.blocks.props.floor_props.floor_monitors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;

public class FloorMonitors extends FloorPropBlock<FloorMonitorColors> {
    public FloorMonitors(Properties settings) {
        super(settings);
    }

    @Override
    public Class<FloorMonitorColors> COLOR_ENUM() {
        return FloorMonitorColors.class;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.create(new AABB(0, 0, 0, 1, 1.35, 0.8)));
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
