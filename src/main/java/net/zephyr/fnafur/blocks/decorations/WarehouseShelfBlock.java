package net.zephyr.fnafur.blocks.decorations;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import javax.swing.text.html.BlockView;

public class WarehouseShelfBlock extends HorizontalFacingBlock {
    public static final MapCodec<WarehouseShelfBlock> CODEC = Block.createCodec(WarehouseShelfBlock::new);

    public WarehouseShelfBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends WarehouseShelfBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        Direction dir = state.get(Properties.HORIZONTAL_FACING);
        return switch (dir) {
            case NORTH -> VoxelShapes.cuboid(0.0f, 0.0f, 0.0f, 2.0f, 2.0f, 0.5f);
            case SOUTH -> VoxelShapes.cuboid(0.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f);
            case EAST -> VoxelShapes.cuboid(0.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f);
            case WEST -> VoxelShapes.cuboid(0.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f);
            default -> VoxelShapes.fullCube();
        };
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

}