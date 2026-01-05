package net.zephyr.fnafur.blocks.curtain;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.RailShape;
import net.minecraft.block.enums.StairShape;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.AxisRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.DirectionTransformation;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import net.zephyr.fnafur.blocks.linking.presets.SimpleEnergyTargetPropBlockEntity;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.chip_reader.ChipReaderBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CurtainBlock extends BlockWithEntity {
    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    public static final EnumProperty<StairShape> SHAPE = Properties.STAIR_SHAPE;

    private static final VoxelShape OUTER_SHAPE = VoxelShapes.union(
            Block.createColumnShape(16.0, 0.0, 8.0), Block.createCuboidShape(0.0, 8.0, 0.0, 8.0, 16.0, 8.0)
    );
    private static final VoxelShape STRAIGHT_SHAPE = VoxelShapes.union(OUTER_SHAPE, VoxelShapes.transform(OUTER_SHAPE, DirectionTransformation.field_64511));
    private static final VoxelShape INNER_SHAPE = VoxelShapes.union(STRAIGHT_SHAPE, VoxelShapes.transform(STRAIGHT_SHAPE, DirectionTransformation.field_64511));
    private static final Map<Direction, VoxelShape> OUTER_BOTTOM_SHAPES = VoxelShapes.createHorizontalFacingShapeMap(OUTER_SHAPE);
    private static final Map<Direction, VoxelShape> STRAIGHT_BOTTOM_SHAPES = VoxelShapes.createHorizontalFacingShapeMap(STRAIGHT_SHAPE);
    private static final Map<Direction, VoxelShape> INNER_BOTTOM_SHAPES = VoxelShapes.createHorizontalFacingShapeMap(INNER_SHAPE);

    public CurtainBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CurtainBlockEntity(pos, state);
    }
    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);

        Map var10000 = switch ((StairShape)state.get(SHAPE)) {
            case STRAIGHT -> STRAIGHT_BOTTOM_SHAPES;
            case OUTER_LEFT, OUTER_RIGHT -> OUTER_BOTTOM_SHAPES;
            case INNER_RIGHT, INNER_LEFT -> INNER_BOTTOM_SHAPES;
        };

        return (VoxelShape)var10000.get(switch ((StairShape)state.get(SHAPE)) {
            case STRAIGHT, OUTER_LEFT, INNER_RIGHT -> direction;
            case INNER_LEFT -> direction.rotateYCounterclockwise();
            case OUTER_RIGHT -> direction.rotateYClockwise();
        });
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof CurtainBlockEntity ent) {
            if(ent.isFirst()){
                ActionResult result2 = ent.tryEndLink(player, world, pos);
                if (result2 != null) return result2;
            }
        }
        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        BlockState blockState = this.getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing());
        return blockState.with(SHAPE, getStairShape(blockState, ctx.getWorld(), blockPos));
    }

    @Override
    protected BlockState getStateForNeighborUpdate(
            BlockState state,
            WorldView world,
            ScheduledTickView tickView,
            BlockPos pos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighborState,
            Random random
    ) {

        return direction.getAxis().isHorizontal()
                ? state.with(SHAPE, getStairShape(state, world, pos))
                : super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    private static boolean isDifferentOrientation(BlockState state, BlockView world, BlockPos pos, Direction dir) {
        BlockState blockState = world.getBlockState(pos.offset(dir));
        return !isStairs(blockState) || blockState.get(FACING) != state.get(FACING);
    }

    public static boolean isStairs(BlockState state) {
        return state.getBlock() instanceof CurtainBlock;
    }
    private static StairShape getStairShape(BlockState state, BlockView world, BlockPos pos) {
        Direction direction = state.get(FACING);
        BlockState blockState = world.getBlockState(pos.offset(direction));
        if (isStairs(blockState)) {
            Direction direction2 = blockState.get(FACING);
            if (direction2.getAxis() != ((Direction)state.get(FACING)).getAxis() && isDifferentOrientation(state, world, pos, direction2.getOpposite())) {
                if (direction2 == direction.rotateYCounterclockwise()) {
                    return StairShape.OUTER_LEFT;
                }

                return StairShape.OUTER_RIGHT;
            }
        }

        BlockState blockState2 = world.getBlockState(pos.offset(direction.getOpposite()));
        if (isStairs(blockState2)) {
            Direction direction3 = blockState2.get(FACING);
            if (direction3.getAxis() != ((Direction)state.get(FACING)).getAxis() && isDifferentOrientation(state, world, pos, direction3)) {
                if (direction3 == direction.rotateYCounterclockwise()) {
                    return StairShape.INNER_LEFT;
                }

                return StairShape.INNER_RIGHT;
            }
        }

        return StairShape.STRAIGHT;
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        Direction direction = state.get(FACING);
        StairShape stairShape = state.get(SHAPE);
        switch (mirror) {
            case LEFT_RIGHT:
                if (direction.getAxis() == Direction.Axis.Z) {
                    switch (stairShape) {
                        case OUTER_LEFT:
                            return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.OUTER_RIGHT);
                        case INNER_RIGHT:
                            return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.INNER_LEFT);
                        case INNER_LEFT:
                            return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.INNER_RIGHT);
                        case OUTER_RIGHT:
                            return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.OUTER_LEFT);
                        default:
                            return state.rotate(BlockRotation.CLOCKWISE_180);
                    }
                }
                break;
            case FRONT_BACK:
                if (direction.getAxis() == Direction.Axis.X) {
                    switch (stairShape) {
                        case STRAIGHT:
                            return state.rotate(BlockRotation.CLOCKWISE_180);
                        case OUTER_LEFT:
                            return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.OUTER_RIGHT);
                        case INNER_RIGHT:
                            return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.INNER_RIGHT);
                        case INNER_LEFT:
                            return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.INNER_LEFT);
                        case OUTER_RIGHT:
                            return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.OUTER_LEFT);
                    }
                }
        }

        return super.mirror(state, mirror);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, SHAPE);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return true;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, BlockEntityInit.CURTAIN,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));
    }
}
