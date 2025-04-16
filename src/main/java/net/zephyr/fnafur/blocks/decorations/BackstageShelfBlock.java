package net.zephyr.fnafur.blocks.decorations;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import net.zephyr.fnafur.init.block_init.BlockInit;

public class BackstageShelfBlock extends HorizontalFacingBlock {
    public static final MapCodec<BackstageShelfBlock> CODEC = Block.createCodec(BackstageShelfBlock::new);

    public static final EnumProperty<BackstageShelf> TYPE = EnumProperty.of("type", BackstageShelf.class);

    public BackstageShelfBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BackstageShelfBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING, TYPE);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        Direction dir = state.get(Properties.HORIZONTAL_FACING);
        return switch (dir) {
            case SOUTH -> VoxelShapes.cuboid(0.0f, 0.9f, 0.0f, 1.0f, 1.0f, 0.5f);
            case NORTH -> VoxelShapes.cuboid(0.0f, 0.9f, 0.5f, 1.0f, 1.0f, 1.0f);
            case WEST -> VoxelShapes.cuboid(0.5f, 0.9f, 0.0f, 1.0f, 1.0f, 1.0f);
            case EAST -> VoxelShapes.cuboid(0.0f, 0.9f, 0.0f, 0.5f, 1.0f, 1.0f);
            default -> VoxelShapes.fullCube();
        };
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {

        BlockState frontState = world.getBlockState(pos.offset(state.get(FACING)));
        boolean bl = frontState.isOf(BlockInit.BACKSTAGE_SHELF);
        boolean bl2 = bl && frontState.get(FACING) == state.get(FACING).rotateYCounterclockwise();
        return state.with(FACING, bl && !bl2 ? frontState.get(FACING) : state.get(FACING)).with(TYPE, bl ? BackstageShelf.CORNER : BackstageShelf.SINGLE);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getHorizontalPlayerFacing().getOpposite();
        BlockState frontState = ctx.getWorld().getBlockState(ctx.getBlockPos().offset(direction));
        boolean bl = frontState.isOf(BlockInit.BACKSTAGE_SHELF);
        boolean bl2 = bl && frontState.get(FACING) == direction.rotateYCounterclockwise();
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, bl && !bl2 ? frontState.get(FACING) : direction).with(TYPE, bl ? BackstageShelf.CORNER : BackstageShelf.SINGLE);
    }

}