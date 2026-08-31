package net.zephyr.fnafur.blocks.curtain;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.BlockHitResult;
import com.mojang.math.Quadrant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import com.mojang.math.OctahedralGroup;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.zephyr.fnafur.blocks.linking.presets.SimpleEnergyTargetPropBlockEntity;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.chip_reader.ChipReaderBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CurtainBlock extends BaseEntityBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;

    private static final VoxelShape OUTER_SHAPE = Shapes.or(
            Block.column(16.0, 0.0, 8.0), Block.box(0.0, 8.0, 0.0, 8.0, 16.0, 8.0)
    );
    private static final VoxelShape STRAIGHT_SHAPE = Shapes.or(OUTER_SHAPE, Shapes.rotate(OUTER_SHAPE, OctahedralGroup.BLOCK_ROT_Y_90));
    private static final VoxelShape INNER_SHAPE = Shapes.or(STRAIGHT_SHAPE, Shapes.rotate(STRAIGHT_SHAPE, OctahedralGroup.BLOCK_ROT_Y_90));
    private static final Map<Direction, VoxelShape> OUTER_BOTTOM_SHAPES = Shapes.rotateHorizontal(OUTER_SHAPE);
    private static final Map<Direction, VoxelShape> STRAIGHT_BOTTOM_SHAPES = Shapes.rotateHorizontal(STRAIGHT_SHAPE);
    private static final Map<Direction, VoxelShape> INNER_BOTTOM_SHAPES = Shapes.rotateHorizontal(INNER_SHAPE);

    public CurtainBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CurtainBlockEntity(pos, state);
    }
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);

        Map var10000 = switch ((StairsShape)state.getValue(SHAPE)) {
            case STRAIGHT -> STRAIGHT_BOTTOM_SHAPES;
            case OUTER_LEFT, OUTER_RIGHT -> OUTER_BOTTOM_SHAPES;
            case INNER_RIGHT, INNER_LEFT -> INNER_BOTTOM_SHAPES;
        };

        return (VoxelShape)var10000.get(switch ((StairsShape)state.getValue(SHAPE)) {
            case STRAIGHT, OUTER_LEFT, INNER_RIGHT -> direction;
            case INNER_LEFT -> direction.getCounterClockWise();
            case OUTER_RIGHT -> direction.getClockWise();
        });
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof CurtainBlockEntity ent) {
            if(ent.isFirst()){
                InteractionResult result2 = ent.tryEndLink(player, world, pos);
                if (result2 != null) return result2;
            }
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos blockPos = ctx.getClickedPos();
        BlockState blockState = this.defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection());
        return blockState.setValue(SHAPE, getStairShape(blockState, ctx.getLevel(), blockPos));
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            LevelReader world,
            ScheduledTickAccess tickView,
            BlockPos pos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighborState,
            RandomSource random
    ) {

        return direction.getAxis().isHorizontal()
                ? state.setValue(SHAPE, getStairShape(state, world, pos))
                : super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    private static boolean isDifferentOrientation(BlockState state, BlockGetter world, BlockPos pos, Direction dir) {
        BlockState blockState = world.getBlockState(pos.relative(dir));
        return !isStairs(blockState) || blockState.getValue(FACING) != state.getValue(FACING);
    }

    public static boolean isStairs(BlockState state) {
        return state.getBlock() instanceof CurtainBlock;
    }
    private static StairsShape getStairShape(BlockState state, BlockGetter world, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockState blockState = world.getBlockState(pos.relative(direction));
        if (isStairs(blockState)) {
            Direction direction2 = blockState.getValue(FACING);
            if (direction2.getAxis() != ((Direction)state.getValue(FACING)).getAxis() && isDifferentOrientation(state, world, pos, direction2.getOpposite())) {
                if (direction2 == direction.getCounterClockWise()) {
                    return StairsShape.OUTER_LEFT;
                }

                return StairsShape.OUTER_RIGHT;
            }
        }

        BlockState blockState2 = world.getBlockState(pos.relative(direction.getOpposite()));
        if (isStairs(blockState2)) {
            Direction direction3 = blockState2.getValue(FACING);
            if (direction3.getAxis() != ((Direction)state.getValue(FACING)).getAxis() && isDifferentOrientation(state, world, pos, direction3)) {
                if (direction3 == direction.getCounterClockWise()) {
                    return StairsShape.INNER_LEFT;
                }

                return StairsShape.INNER_RIGHT;
            }
        }

        return StairsShape.STRAIGHT;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        Direction direction = state.getValue(FACING);
        StairsShape stairShape = state.getValue(SHAPE);
        switch (mirror) {
            case LEFT_RIGHT:
                if (direction.getAxis() == Direction.Axis.Z) {
                    switch (stairShape) {
                        case OUTER_LEFT:
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                        case INNER_RIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                        case INNER_LEFT:
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                        case OUTER_RIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                        default:
                            return state.rotate(Rotation.CLOCKWISE_180);
                    }
                }
                break;
            case FRONT_BACK:
                if (direction.getAxis() == Direction.Axis.X) {
                    switch (stairShape) {
                        case STRAIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180);
                        case OUTER_LEFT:
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                        case INNER_RIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                        case INNER_LEFT:
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                        case OUTER_RIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                    }
                }
        }

        return super.mirror(state, mirror);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SHAPE);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return true;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityInit.CURTAIN,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));
    }
}
