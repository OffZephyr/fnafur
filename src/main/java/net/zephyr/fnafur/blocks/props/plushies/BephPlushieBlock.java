package net.zephyr.fnafur.blocks.props.plushies;

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

public class BephPlushieBlock extends FloorPropBlock<BephColor> {
    public static final EnumProperty<BephColor> COLOR = EnumProperty.of("color", BephColor.class);

    public BephPlushieBlock(Settings settings) {
        super(settings);
    }

    @Override
    public EnumProperty<BephColor> COLOR_PROPERTY() {
        return COLOR;
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
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) { return true;}


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
