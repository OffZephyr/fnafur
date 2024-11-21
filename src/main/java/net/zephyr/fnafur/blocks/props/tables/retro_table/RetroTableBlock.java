package net.zephyr.fnafur.blocks.props.tables.retro_table;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class RetroTableBlock extends FloorPropBlock<RetroTableColor> {
    public static final EnumProperty<RetroTableColor> COLOR = EnumProperty.of("color", RetroTableColor.class);

    public RetroTableBlock(Settings settings) {
        super(settings);
    }

    @Override
    public EnumProperty<RetroTableColor> COLOR_PROPERTY() {
        return COLOR;
    }

    @Override
    public boolean rotates() {
        return false;
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

    @Override
    public boolean isTransparent(BlockState state) { return true;}


    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return true;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}
