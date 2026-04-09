package net.zephyr.fnafur.blocks.props.floor_props.tables;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class RetroTableBlock extends FloorPropBlock<DefaultPropColorEnum> {

    public RetroTableBlock(Properties settings) {
        super(settings);
    }
    @Override
    public Class<DefaultPropColorEnum> COLOR_ENUM() {
        return null;
    }

    @Override
    public boolean rotates() {
        return true;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.create(new AABB(0, 0, 0, 1, 1, 1)));
        return drawingOutline ? shape : Shapes.block();
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        if(world.getBlockEntity(pos) instanceof PropBlockEntity entity) {
            double offsetX = ((IEntityDataSaver) entity).getPersistentData().getDouble("xOffset").get();
            double offsetZ = ((IEntityDataSaver) entity).getPersistentData().getDouble("zOffset").get();
            shape = Shapes.or(shape, Shapes.create(new AABB(Math.max(-0.5f + offsetX, 0), 0.9f, Math.max(-0.5f + offsetZ, 0), Math.min(0.5f + offsetX, 1), 1, Math.min(0.5f + offsetZ, 1))));
            return shape;
        }
        return Shapes.empty();
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return Shapes.block();
    }
}
