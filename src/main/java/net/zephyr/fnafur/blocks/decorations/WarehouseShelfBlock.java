package net.zephyr.fnafur.blocks.decorations;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import net.zephyr.fnafur.init.block_init.BlockInit;
import org.jetbrains.annotations.Nullable;

public class WarehouseShelfBlock extends HorizontalFacingBlock {
    public static final MapCodec<WarehouseShelfBlock> CODEC = Block.createCodec(WarehouseShelfBlock::new);
    public static final EnumProperty<WarehouseShelf> TYPE = EnumProperty.of("type", WarehouseShelf.class);
    public static final BooleanProperty TOP = BooleanProperty.of("top");

    public WarehouseShelfBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends WarehouseShelfBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE, TOP);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = VoxelShapes.cuboid(0, 0.9f, 0, 1, 1, 1);

        float y = state.get(TOP) ? 1 : 2;

        if(state.get(FACING).getAxis() == Direction.Axis.X){
            if((state.get(FACING) == Direction.EAST && state.get(TYPE) == WarehouseShelf.LEFT) || (state.get(FACING) == Direction.WEST && state.get(TYPE) == WarehouseShelf.RIGHT) || state.get(TYPE) == WarehouseShelf.SINGLE){
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0, 0, 1, y, 0.1f));
            }
            if((state.get(FACING) == Direction.WEST && state.get(TYPE) == WarehouseShelf.LEFT) || (state.get(FACING) == Direction.EAST && state.get(TYPE) == WarehouseShelf.RIGHT) || state.get(TYPE) == WarehouseShelf.SINGLE){
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0, 0.9f, 1, y, 1));
            }
        }
        if(state.get(FACING).getAxis() == Direction.Axis.Z){
            if((state.get(FACING) == Direction.NORTH && state.get(TYPE) == WarehouseShelf.LEFT) || (state.get(FACING) == Direction.SOUTH && state.get(TYPE) == WarehouseShelf.RIGHT) || state.get(TYPE) == WarehouseShelf.SINGLE){
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0, 0, 0.1f, y, 1));
            }
            if((state.get(FACING) == Direction.SOUTH && state.get(TYPE) == WarehouseShelf.LEFT) || (state.get(FACING) == Direction.NORTH && state.get(TYPE) == WarehouseShelf.RIGHT) || state.get(TYPE) == WarehouseShelf.SINGLE){
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.9f, 0, 0, 1, y, 1));
            }
        }

        return shape;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {

        BlockState upState = ctx.getWorld().getBlockState(ctx.getBlockPos().up());
        BlockState upTwoState = ctx.getWorld().getBlockState(ctx.getBlockPos().up(2));

        boolean top = !upState.isOf(BlockInit.WAREHOUSE_SHELF) && !upState.isSolidBlock(ctx.getWorld(), ctx.getBlockPos()) && upTwoState.isOf(BlockInit.WAREHOUSE_SHELF);

        Direction facing = ctx.getHorizontalPlayerFacing().getOpposite();

        BlockPos leftPos = ctx.getBlockPos().offset(facing.rotateYCounterclockwise());
        BlockPos rightPos  = ctx.getBlockPos().offset(facing.rotateYClockwise());

        BlockState leftState = ctx.getWorld().getBlockState(leftPos);
        BlockState rightState = ctx.getWorld().getBlockState(rightPos);

        BlockState leftUpState = ctx.getWorld().getBlockState(leftPos.up());
        BlockState rightUpState = ctx.getWorld().getBlockState(rightPos.up());
        BlockState leftUpTwoState = ctx.getWorld().getBlockState(leftPos.up(2));
        BlockState rightUpTwoState = ctx.getWorld().getBlockState(rightPos.up(2));

        boolean topLeft = !leftUpState.isOf(BlockInit.WAREHOUSE_SHELF) && !leftUpState.isSolidBlock(ctx.getWorld(), leftPos.up()) && leftUpTwoState.isOf(BlockInit.WAREHOUSE_SHELF);
        boolean topRight = !rightUpState.isOf(BlockInit.WAREHOUSE_SHELF) && !rightUpState.isSolidBlock(ctx.getWorld(), rightPos.up()) && rightUpTwoState.isOf(BlockInit.WAREHOUSE_SHELF);

        WarehouseShelf type =
                leftState.isOf(BlockInit.WAREHOUSE_SHELF) && leftState.get(TYPE) == WarehouseShelf.SINGLE ? WarehouseShelf.RIGHT :
                        rightState.isOf(BlockInit.WAREHOUSE_SHELF) && rightState.get(TYPE) == WarehouseShelf.SINGLE ? WarehouseShelf.LEFT :
                                WarehouseShelf.SINGLE;

        boolean top2 = type == WarehouseShelf.LEFT && topLeft || type == WarehouseShelf.RIGHT && topRight || type == WarehouseShelf.SINGLE && top;

        return getDefaultState().with(FACING, facing).with(TYPE, type).with(TOP, !top && !top2);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        world.setBlockState(pos.down(2), Block.postProcessState(world.getBlockState(pos.down(2)), world, pos.down(2)));
        //world.updateNeighbors(pos.down(2), BlockInit.WAREHOUSE_SHELF);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.afterBreak(world, player, pos, state, blockEntity, tool);
        world.setBlockState(pos.down(2), Block.postProcessState(world.getBlockState(pos.down(2)), world, pos.down(2)));
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {

        BlockState upState = world.getBlockState(pos.up());
        BlockState upTwoState = world.getBlockState(pos.up(2));
        BlockState neighborUpState = world.getBlockState(neighborPos.up());
        BlockState neighborUpTwoState = world.getBlockState(neighborPos.up(2));

        boolean top = !upState.isOf(BlockInit.WAREHOUSE_SHELF) && !upState.isSolidBlock(world, pos) && upTwoState.isOf(BlockInit.WAREHOUSE_SHELF);
        boolean top2 = !neighborUpState.isOf(BlockInit.WAREHOUSE_SHELF) && !neighborUpState.isSolidBlock(world, pos) && neighborUpTwoState.isOf(BlockInit.WAREHOUSE_SHELF);

        WarehouseShelf type = state.get(TYPE);
        Direction facing = state.get(FACING);
        boolean isTop = !top && !top2;

        if(state.get(TYPE) == WarehouseShelf.SINGLE && neighborState.isOf(BlockInit.WAREHOUSE_SHELF)){
            if(direction == neighborState.get(FACING).rotateYCounterclockwise() && neighborState.get(TYPE) == WarehouseShelf.LEFT){
                type = WarehouseShelf.RIGHT;
                facing = neighborState.get(FACING);
            }
            else if(direction == neighborState.get(FACING).rotateYClockwise() && neighborState.get(TYPE) == WarehouseShelf.RIGHT){
                type = WarehouseShelf.LEFT;
                facing = neighborState.get(FACING);
            }
        }
        else if(!neighborState.isOf(BlockInit.WAREHOUSE_SHELF)){
            if(direction == state.get(FACING).rotateYCounterclockwise() && state.get(TYPE) == WarehouseShelf.RIGHT) {
                type = WarehouseShelf.SINGLE;
                isTop = !top;
            }
            else if(direction == state.get(FACING).rotateYClockwise() && state.get(TYPE) == WarehouseShelf.LEFT) {
                type = WarehouseShelf.SINGLE;
                isTop = !top;
            }
        }

        return state.with(FACING, facing).with(TYPE, type).with(TOP, isTop);
    }
}