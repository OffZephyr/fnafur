package net.zephyr.fnafur.blocks.decorations;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.zephyr.fnafur.init.block_init.BlockInit;
import org.jetbrains.annotations.Nullable;

public class WarehouseShelfBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<WarehouseShelfBlock> CODEC = Block.simpleCodec(WarehouseShelfBlock::new);
    public static final EnumProperty<WarehouseShelf> TYPE = EnumProperty.create("type", WarehouseShelf.class);
    public static final BooleanProperty TOP = BooleanProperty.create("top");

    public WarehouseShelfBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends WarehouseShelfBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE, TOP);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.box(0, 0.9f, 0, 1, 1, 1);

        float y = state.getValue(TOP) ? 1 : 2;

        if(state.getValue(FACING).getAxis() == Direction.Axis.X){
            if((state.getValue(FACING) == Direction.EAST && state.getValue(TYPE) == WarehouseShelf.LEFT) || (state.getValue(FACING) == Direction.WEST && state.getValue(TYPE) == WarehouseShelf.RIGHT) || state.getValue(TYPE) == WarehouseShelf.SINGLE){
                shape = Shapes.or(shape, Shapes.box(0, 0, 0, 1, y, 0.1f));
            }
            if((state.getValue(FACING) == Direction.WEST && state.getValue(TYPE) == WarehouseShelf.LEFT) || (state.getValue(FACING) == Direction.EAST && state.getValue(TYPE) == WarehouseShelf.RIGHT) || state.getValue(TYPE) == WarehouseShelf.SINGLE){
                shape = Shapes.or(shape, Shapes.box(0, 0, 0.9f, 1, y, 1));
            }
        }
        if(state.getValue(FACING).getAxis() == Direction.Axis.Z){
            if((state.getValue(FACING) == Direction.NORTH && state.getValue(TYPE) == WarehouseShelf.LEFT) || (state.getValue(FACING) == Direction.SOUTH && state.getValue(TYPE) == WarehouseShelf.RIGHT) || state.getValue(TYPE) == WarehouseShelf.SINGLE){
                shape = Shapes.or(shape, Shapes.box(0, 0, 0, 0.1f, y, 1));
            }
            if((state.getValue(FACING) == Direction.SOUTH && state.getValue(TYPE) == WarehouseShelf.LEFT) || (state.getValue(FACING) == Direction.NORTH && state.getValue(TYPE) == WarehouseShelf.RIGHT) || state.getValue(TYPE) == WarehouseShelf.SINGLE){
                shape = Shapes.or(shape, Shapes.box(0.9f, 0, 0, 1, y, 1));
            }
        }

        return shape;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {

        BlockState upState = ctx.getLevel().getBlockState(ctx.getClickedPos().above());
        BlockState upTwoState = ctx.getLevel().getBlockState(ctx.getClickedPos().above(2));

        boolean top = !upState.is(BlockInit.WAREHOUSE_SHELF) && !upState.isRedstoneConductor(ctx.getLevel(), ctx.getClickedPos()) && upTwoState.is(BlockInit.WAREHOUSE_SHELF);

        Direction facing = ctx.getHorizontalDirection().getOpposite();

        BlockPos leftPos = ctx.getClickedPos().relative(facing.getCounterClockWise());
        BlockPos rightPos  = ctx.getClickedPos().relative(facing.getClockWise());

        BlockState leftState = ctx.getLevel().getBlockState(leftPos);
        BlockState rightState = ctx.getLevel().getBlockState(rightPos);

        BlockState leftUpState = ctx.getLevel().getBlockState(leftPos.above());
        BlockState rightUpState = ctx.getLevel().getBlockState(rightPos.above());
        BlockState leftUpTwoState = ctx.getLevel().getBlockState(leftPos.above(2));
        BlockState rightUpTwoState = ctx.getLevel().getBlockState(rightPos.above(2));

        boolean topLeft = !leftUpState.is(BlockInit.WAREHOUSE_SHELF) && !leftUpState.isRedstoneConductor(ctx.getLevel(), leftPos.above()) && leftUpTwoState.is(BlockInit.WAREHOUSE_SHELF);
        boolean topRight = !rightUpState.is(BlockInit.WAREHOUSE_SHELF) && !rightUpState.isRedstoneConductor(ctx.getLevel(), rightPos.above()) && rightUpTwoState.is(BlockInit.WAREHOUSE_SHELF);

        WarehouseShelf type =
                leftState.is(BlockInit.WAREHOUSE_SHELF) && leftState.getValue(TYPE) == WarehouseShelf.SINGLE ? WarehouseShelf.RIGHT :
                        rightState.is(BlockInit.WAREHOUSE_SHELF) && rightState.getValue(TYPE) == WarehouseShelf.SINGLE ? WarehouseShelf.LEFT :
                                WarehouseShelf.SINGLE;

        boolean top2 = type == WarehouseShelf.LEFT && topLeft || type == WarehouseShelf.RIGHT && topRight || type == WarehouseShelf.SINGLE && top;

        return defaultBlockState().setValue(FACING, facing).setValue(TYPE, type).setValue(TOP, !top && !top2);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, placer, itemStack);
        world.setBlockAndUpdate(pos.below(2), Block.updateFromNeighbourShapes(world.getBlockState(pos.below(2)), world, pos.below(2)));
        //world.updateNeighbors(pos.down(2), BlockInit.WAREHOUSE_SHELF);
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(world, player, pos, state, blockEntity, tool);
        world.setBlockAndUpdate(pos.below(2), Block.updateFromNeighbourShapes(world.getBlockState(pos.below(2)), world, pos.below(2)));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {

        BlockState upState = world.getBlockState(pos.above());
        BlockState upTwoState = world.getBlockState(pos.above(2));
        BlockState neighborUpState = world.getBlockState(neighborPos.above());
        BlockState neighborUpTwoState = world.getBlockState(neighborPos.above(2));

        boolean top = !upState.is(BlockInit.WAREHOUSE_SHELF) && !upState.isRedstoneConductor(world, pos) && upTwoState.is(BlockInit.WAREHOUSE_SHELF);
        boolean top2 = !neighborUpState.is(BlockInit.WAREHOUSE_SHELF) && !neighborUpState.isRedstoneConductor(world, pos) && neighborUpTwoState.is(BlockInit.WAREHOUSE_SHELF);

        WarehouseShelf type = state.getValue(TYPE);
        Direction facing = state.getValue(FACING);
        boolean isTop = !top && !top2;

        if(state.getValue(TYPE) == WarehouseShelf.SINGLE && neighborState.is(BlockInit.WAREHOUSE_SHELF)){
            if(direction == neighborState.getValue(FACING).getCounterClockWise() && neighborState.getValue(TYPE) == WarehouseShelf.LEFT){
                type = WarehouseShelf.RIGHT;
                facing = neighborState.getValue(FACING);
            }
            else if(direction == neighborState.getValue(FACING).getClockWise() && neighborState.getValue(TYPE) == WarehouseShelf.RIGHT){
                type = WarehouseShelf.LEFT;
                facing = neighborState.getValue(FACING);
            }
        }
        else if(!neighborState.is(BlockInit.WAREHOUSE_SHELF)){
            if(direction == state.getValue(FACING).getCounterClockWise() && state.getValue(TYPE) == WarehouseShelf.RIGHT) {
                type = WarehouseShelf.SINGLE;
                isTop = !top;
            }
            else if(direction == state.getValue(FACING).getClockWise() && state.getValue(TYPE) == WarehouseShelf.LEFT) {
                type = WarehouseShelf.SINGLE;
                isTop = !top;
            }
        }

        return state.setValue(FACING, facing).setValue(TYPE, type).setValue(TOP, isTop);
    }
}