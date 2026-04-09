package net.zephyr.fnafur.blocks.decorations;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.zephyr.fnafur.init.block_init.BlockInit;

public class BackstageShelfBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<BackstageShelfBlock> CODEC = Block.simpleCodec(BackstageShelfBlock::new);

    public static final EnumProperty<BackstageShelf> TYPE = EnumProperty.create("type", BackstageShelf.class);

    public BackstageShelfBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BackstageShelfBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING, TYPE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        return switch (dir) {
            case SOUTH -> Shapes.box(0.0f, 0.9f, 0.0f, 1.0f, 1.0f, 0.5f);
            case NORTH -> Shapes.box(0.0f, 0.9f, 0.5f, 1.0f, 1.0f, 1.0f);
            case WEST -> Shapes.box(0.5f, 0.9f, 0.0f, 1.0f, 1.0f, 1.0f);
            case EAST -> Shapes.box(0.0f, 0.9f, 0.0f, 0.5f, 1.0f, 1.0f);
            default -> Shapes.block();
        };
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {

        BlockState frontState = world.getBlockState(pos.relative(state.getValue(FACING)));
        boolean bl = frontState.is(BlockInit.BACKSTAGE_SHELF);
        boolean bl2 = bl && frontState.getValue(FACING) == state.getValue(FACING).getCounterClockWise();
        return state.setValue(FACING, bl && !bl2 ? frontState.getValue(FACING) : state.getValue(FACING)).setValue(TYPE, bl ? BackstageShelf.CORNER : BackstageShelf.SINGLE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction direction = ctx.getHorizontalDirection().getOpposite();
        BlockState frontState = ctx.getLevel().getBlockState(ctx.getClickedPos().relative(direction));
        boolean bl = frontState.is(BlockInit.BACKSTAGE_SHELF);
        boolean bl2 = bl && frontState.getValue(FACING) == direction.getCounterClockWise();
        return super.getStateForPlacement(ctx).setValue(BlockStateProperties.HORIZONTAL_FACING, bl && !bl2 ? frontState.getValue(FACING) : direction).setValue(TYPE, bl ? BackstageShelf.CORNER : BackstageShelf.SINGLE);
    }

}