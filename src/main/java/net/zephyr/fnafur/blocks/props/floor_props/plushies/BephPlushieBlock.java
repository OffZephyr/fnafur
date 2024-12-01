package net.zephyr.fnafur.blocks.props.floor_props.plushies;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;

public class BephPlushieBlock extends FloorPropBlock<DefaultPropColorEnum> {

    public BephPlushieBlock(Settings settings) {
        super(settings);
    }

    @Override
    public Class<DefaultPropColorEnum> COLOR_ENUM() {
        return null;
    }

    @Override
    public boolean rotates() {
        return false;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(new Box(0.25, 0.0, 0.35, 0.75, 0.5, 0.70)));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(new Box(0.25, 0.5, 0.25, 0.75, 1, 0.75)));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(new Box(0.4, 1, 0.4, 0.6, 1.25, 0.6)));
        return drawingOutline ? shape : VoxelShapes.fullCube();
    }

    @Override
    protected VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.fullCube();
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }
}
