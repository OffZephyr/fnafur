package net.zephyr.fnafur.blocks.dynamic.tiling.vent;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.ScheduledTickAccess;
import org.jspecify.annotations.Nullable;

public class VentBlock extends Block {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<VentPosition> VENT_POSITION = EnumProperty.create("position", VentPosition.class);
    public static final EnumProperty<VentShape> VENT_SHAPE = EnumProperty.create("shape", VentShape.class);
    public static final EnumProperty<VentOffset> VENT_OFFSET = EnumProperty.create("offset", VentOffset.class);
    public static final BooleanProperty IS_FLOOR = BooleanProperty.create("floor");

    public VentBlock(Properties settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction d = ctx.getClickedFace().getAxis() == Direction.Axis.Y ? ctx.getHorizontalDirection() : ctx.getClickedFace();
        return getWorldVentBlockstate(defaultBlockState().setValue(FACING, d), ctx.getClickedPos(), ctx.getLevel());
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {

        if(!world.isClientSide()) {
            if (placer != null) {
                Direction facing = state.getValue(FACING);
                BlockPos prev = pos.relative(facing.getOpposite());
                BlockPos next = pos.relative(facing);
                BlockPos leftPos = pos.relative(facing.getCounterClockWise(), 2);
                BlockPos rightPos = pos.relative(facing.getClockWise(), 2);
                BlockPos usedPos = world.getBlockState(prev).getBlock() instanceof VentBlock ? prev : next;
                if (world.getBlockState(usedPos).getBlock() instanceof VentBlock) {
                    BlockState checkedState = world.getBlockState(usedPos);
                    state = state.setValue(IS_FLOOR, checkedState.getValue(IS_FLOOR));
                    if(world.getBlockState(leftPos).getBlock() instanceof VentBlock || world.getBlockState(rightPos).getBlock() instanceof VentBlock){

                    }
                    placeVentAtPos(state, VentPosition.getCenter(checkedState.getValue(VENT_POSITION), pos, checkedState.getValue(FACING)), world);
                }
                else{
                    boolean isFloor = world.getBlockState(pos.below().relative(facing.getOpposite())).isCollisionShapeFullBlock(world, pos.below().relative(facing.getOpposite()));

                    state = state.setValue(IS_FLOOR, isFloor);
                    placeVentAtPos(state, pos, world);
                }
            }
        }
        super.setPlacedBy(world, pos, state, placer, itemStack);
    }

    @Override
    protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify) {
        super.neighborChanged(state, world, pos, sourceBlock, wireOrientation, notify);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
//        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
        return getWorldVentBlockstate(state, pos, world);
    }

    private BlockState getWorldVentBlockstate(BlockState state, BlockPos blockPos, LevelReader world){
        Direction facing = state.getValue(FACING);
        VentPosition pos = state.getValue(VENT_POSITION);
        VentOffset offset = state.getValue(VENT_OFFSET);
        VentShape shape = state.getValue(VENT_SHAPE);

        BlockPos prev = blockPos.relative(facing.getOpposite());
        BlockPos next = blockPos.relative(facing);
        BlockPos leftPos = blockPos.relative(facing.getCounterClockWise(), 2);
        BlockPos rightPos = blockPos.relative(facing.getClockWise(), 2);
        BlockState prevPosState = world.getBlockState(prev);
        BlockState nextPosState = world.getBlockState(next);
        BlockState leftPosState = world.getBlockState(leftPos);
        BlockState rightPosState = world.getBlockState(rightPos);


        boolean left = leftPosState.getBlock() instanceof VentBlock;
        boolean right = rightPosState.getBlock() instanceof VentBlock;
        boolean front = nextPosState.getBlock() instanceof VentBlock;
        boolean back = prevPosState.getBlock() instanceof VentBlock;


        if(back){
            if (state.getValue(FACING).getAxis() == prevPosState.getValue(FACING).getAxis()){
                state = state.setValue(FACING, prevPosState.getValue(FACING));
            }

            if(prevPosState.getValue(VENT_OFFSET) == VentOffset.EVEN || prevPosState.getValue(VENT_OFFSET) == VentOffset.END_EVEN || prevPosState.getValue(VENT_OFFSET) == VentOffset.SINGLE){
                state = state.setValue(VENT_OFFSET, VentOffset.END_ODD);
            }
            else if(prevPosState.getValue(VENT_OFFSET) == VentOffset.ODD || prevPosState.getValue(VENT_OFFSET) == VentOffset.END_ODD){
                state = state.setValue(VENT_OFFSET, VentOffset.END_EVEN);
            }
            if(left || right){
                if(!front){
                    state = state.setValue(VENT_SHAPE, VentShape.CORNER);
                }
                else{

                }
            }
            else{
                if(state.getValue(VENT_OFFSET) != VentOffset.SINGLE){
                    if(front){
                        if(state.getValue(VENT_OFFSET) == VentOffset.END_EVEN){
                            state = state.setValue(VENT_OFFSET, VentOffset.EVEN);
                        }
                        else if(state.getValue(VENT_OFFSET) == VentOffset.END_ODD){
                            state = state.setValue(VENT_OFFSET, VentOffset.ODD);
                        }
                    }
                }
            }
        }

        return state;
    }

    void placeVentAtPos(BlockState state, BlockPos centerPos, Level world){
        Direction facing = state.getValue(FACING);
        for(int x = -1; x <= 1; x++){
            for(int y = -1; y <= 1; y++){
                BlockPos placePos = centerPos.relative(Direction.UP, y).relative(facing.getCounterClockWise(), x);

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

                state = state.setValue(VENT_POSITION, position);

                world.setBlock(placePos, state, 3);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING, IS_FLOOR, VENT_SHAPE, VENT_OFFSET, VENT_POSITION));
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        return super.playerWillDestroy(world, pos, state, player);
    }
}
