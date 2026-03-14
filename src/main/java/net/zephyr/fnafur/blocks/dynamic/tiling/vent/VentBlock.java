package net.zephyr.fnafur.blocks.dynamic.tiling.vent;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.tick.ScheduledTickView;
import org.jspecify.annotations.Nullable;

public class VentBlock extends Block {
    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    public static final EnumProperty<VentPosition> VENT_POSITION = EnumProperty.of("position", VentPosition.class);
    public static final EnumProperty<VentShape> VENT_SHAPE = EnumProperty.of("shape", VentShape.class);
    public static final EnumProperty<VentOffset> VENT_OFFSET = EnumProperty.of("offset", VentOffset.class);
    public static final BooleanProperty IS_FLOOR = BooleanProperty.of("floor");

    public VentBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction d = ctx.getSide().getAxis() == Direction.Axis.Y ? ctx.getHorizontalPlayerFacing() : ctx.getSide();
        return getWorldVentBlockstate(getDefaultState().with(FACING, d), ctx.getBlockPos(), ctx.getWorld());
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {

        if(!world.isClient()) {
            if (placer != null) {
                Direction facing = state.get(FACING);
                BlockPos prev = pos.offset(facing.getOpposite());
                BlockPos next = pos.offset(facing);
                BlockPos leftPos = pos.offset(facing.rotateYCounterclockwise(), 2);
                BlockPos rightPos = pos.offset(facing.rotateYClockwise(), 2);
                BlockPos usedPos = world.getBlockState(prev).getBlock() instanceof VentBlock ? prev : next;
                if (world.getBlockState(usedPos).getBlock() instanceof VentBlock) {
                    BlockState checkedState = world.getBlockState(usedPos);
                    state = state.with(IS_FLOOR, checkedState.get(IS_FLOOR));
                    if(world.getBlockState(leftPos).getBlock() instanceof VentBlock || world.getBlockState(rightPos).getBlock() instanceof VentBlock){

                    }
                    placeVentAtPos(state, VentPosition.getCenter(checkedState.get(VENT_POSITION), pos, checkedState.get(FACING)), world);
                }
                else{
                    boolean isFloor = world.getBlockState(pos.down().offset(facing.getOpposite())).isFullCube(world, pos.down().offset(facing.getOpposite()));

                    state = state.with(IS_FLOOR, isFloor);
                    placeVentAtPos(state, pos, world);
                }
            }
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
//        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
        return getWorldVentBlockstate(state, pos, world);
    }

    private BlockState getWorldVentBlockstate(BlockState state, BlockPos blockPos, WorldView world){
        Direction facing = state.get(FACING);
        VentPosition pos = state.get(VENT_POSITION);
        VentOffset offset = state.get(VENT_OFFSET);
        VentShape shape = state.get(VENT_SHAPE);

        BlockPos prev = blockPos.offset(facing.getOpposite());
        BlockPos next = blockPos.offset(facing);
        BlockPos leftPos = blockPos.offset(facing.rotateYCounterclockwise(), 2);
        BlockPos rightPos = blockPos.offset(facing.rotateYClockwise(), 2);
        BlockState prevPosState = world.getBlockState(prev);
        BlockState nextPosState = world.getBlockState(next);
        BlockState leftPosState = world.getBlockState(leftPos);
        BlockState rightPosState = world.getBlockState(rightPos);


        boolean left = leftPosState.getBlock() instanceof VentBlock;
        boolean right = rightPosState.getBlock() instanceof VentBlock;
        boolean front = nextPosState.getBlock() instanceof VentBlock;
        boolean back = prevPosState.getBlock() instanceof VentBlock;


        if(back){
            if (state.get(FACING).getAxis() == prevPosState.get(FACING).getAxis()){
                state = state.with(FACING, prevPosState.get(FACING));
            }

            if(prevPosState.get(VENT_OFFSET) == VentOffset.EVEN || prevPosState.get(VENT_OFFSET) == VentOffset.END_EVEN || prevPosState.get(VENT_OFFSET) == VentOffset.SINGLE){
                state = state.with(VENT_OFFSET, VentOffset.END_ODD);
            }
            else if(prevPosState.get(VENT_OFFSET) == VentOffset.ODD || prevPosState.get(VENT_OFFSET) == VentOffset.END_ODD){
                state = state.with(VENT_OFFSET, VentOffset.END_EVEN);
            }
            if(left || right){
                if(!front){
                    state = state.with(VENT_SHAPE, VentShape.CORNER);
                }
                else{

                }
            }
            else{
                if(state.get(VENT_OFFSET) != VentOffset.SINGLE){
                    if(front){
                        if(state.get(VENT_OFFSET) == VentOffset.END_EVEN){
                            state = state.with(VENT_OFFSET, VentOffset.EVEN);
                        }
                        else if(state.get(VENT_OFFSET) == VentOffset.END_ODD){
                            state = state.with(VENT_OFFSET, VentOffset.ODD);
                        }
                    }
                }
            }
        }

        return state;
    }

    void placeVentAtPos(BlockState state, BlockPos centerPos, World world){
        Direction facing = state.get(FACING);
        for(int x = -1; x <= 1; x++){
            for(int y = -1; y <= 1; y++){
                BlockPos placePos = centerPos.offset(Direction.UP, y).offset(facing.rotateYCounterclockwise(), x);

                boolean floor = false;
                VentPosition position = VentPosition.C;
                if(y == -1){
                    if(x == -1){
                        position = VentPosition.BL;
                    }
                    else if(x == 0){
                        position = VentPosition.BM;
                    }
                    else{
                        position = VentPosition.BR;
                    }
                }
                else if(y == 0){
                    if(x == -1){
                        position = VentPosition.CL;
                    }
                    else if(x == 0){
                        position = VentPosition.C;
                    }
                    else{
                        position = VentPosition.CR;
                    }
                }
                else{
                    if(x == -1){
                        position = VentPosition.UL;
                    }
                    else if(x == 0){
                        position = VentPosition.UM;
                    }
                    else{
                        position = VentPosition.UR;
                    }
                }

                state = state.with(VENT_POSITION, position);

                world.setBlockState(placePos, state, 3);
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(FACING, IS_FLOOR, VENT_SHAPE, VENT_OFFSET, VENT_POSITION));
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        return super.onBreak(world, pos, state, player);
    }
}
