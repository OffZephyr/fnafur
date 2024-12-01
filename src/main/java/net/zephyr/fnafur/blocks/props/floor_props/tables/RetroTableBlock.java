package net.zephyr.fnafur.blocks.props.floor_props.tables;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class RetroTableBlock extends FloorPropBlock<DefaultPropColorEnum> {

    public RetroTableBlock(Settings settings) {
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
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(new Box(0, 0, 0, 1, 1, 1)));
        return drawingOutline ? shape : VoxelShapes.fullCube();
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = VoxelShapes.empty();
        if(world.getBlockEntity(pos) instanceof PropBlockEntity entity) {
            double offsetX = ((IEntityDataSaver) entity).getPersistentData().getDouble("xOffset");
            double offsetZ = ((IEntityDataSaver) entity).getPersistentData().getDouble("zOffset");
            shape = VoxelShapes.union(shape, VoxelShapes.cuboid(new Box(Math.max(-0.5f + offsetX, 0), 0.9f, Math.max(-0.5f + offsetZ, 0), Math.min(0.5f + offsetX, 1), 1, Math.min(0.5f + offsetZ, 1))));
            return shape;
        }
        return VoxelShapes.empty();
    }

    @Override
    protected VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.fullCube();
    }
}
