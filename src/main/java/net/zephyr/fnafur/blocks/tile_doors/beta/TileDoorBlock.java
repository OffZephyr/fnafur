package net.zephyr.fnafur.blocks.tile_doors.beta;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.tick.ScheduledTickView;
import net.zephyr.fnafur.blocks.tile_doors.TileDoorBlockEntity;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

public abstract class TileDoorBlock extends BlockWithEntity {
    public static EnumProperty<DoorTileProperty> TILE = EnumProperty.of("tile", DoorTileProperty.class);
    public static EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    public static BooleanProperty POWERED = Properties.POWERED;
    protected TileDoorBlock(Settings settings) {
        super(settings);
        this.setDefaultState(
                this.stateManager.getDefaultState().with(TILE, DoorTileProperty.SINGLE).with(FACING, Direction.SOUTH).with(POWERED, Boolean.valueOf(false))
        );
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TileDoorBlockEntity(pos, state);
    }

    public abstract Identifier blockTexture();

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, TILE, POWERED);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if(state.get(POWERED))
            return VoxelShapes.empty();
        if(state.get(FACING) == Direction.NORTH || state.get(FACING) == Direction.SOUTH){
            return VoxelShapes.cuboid(0, 0, 0.3125f, 1, 1, 0.6875);
        } else {
            return VoxelShapes.cuboid(0.3125f, 0, 0, 0.6875, 1, 1);
        }
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return state.get(POWERED);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(POWERED) ? VoxelShapes.empty() : super.getCollisionShape(state, world, pos, context);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {

        Direction facing = state.get(FACING);

        BlockState topState = world.getBlockState(pos.up());
        BlockState leftState = world.getBlockState(pos.offset(facing.rotateYClockwise()));
        BlockState bottomState = world.getBlockState(pos.down());
        BlockState rightState = world.getBlockState(pos.offset(facing.rotateYCounterclockwise()));

        DoorTileProperty tile = getTile(topState, leftState, bottomState, rightState, facing);

        boolean bl = state.get(POWERED);
        if(neighborState.getBlock() instanceof TileDoorBlock) {
            if(bl != neighborState.get(POWERED)) bl = neighborState.get(POWERED);
        }
        updateHeight(world, pos);

        return state.with(TILE, tile).with(POWERED, bl);
    }

    public void updateHeight(BlockView world, BlockPos pos){


        BlockPos checkPos = pos;
        while(world.getBlockState(checkPos.up()).getBlock() instanceof TileDoorBlock){
            checkPos = checkPos.up();
        }
        if(world.getBlockEntity(checkPos) instanceof TileDoorBlockEntity) {
            ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().putLong("highest", checkPos.asLong());
        }

        checkPos = pos;
        while(world.getBlockState(checkPos.down()).getBlock() instanceof TileDoorBlock){
            checkPos = checkPos.down();
        }
        if(world.getBlockEntity(checkPos) instanceof TileDoorBlockEntity) {
            ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().putLong("lowest", checkPos.asLong());
        }

        if(world.getBlockEntity(pos) instanceof TileDoorBlockEntity) {
            GoopyNetworkingUtils.saveBlockNbt(pos, ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData());
        }
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        boolean bl = world.isReceivingRedstonePower(pos);
        boolean power = state.get(POWERED);

        updateHeight(world, pos);

        if(power != bl) {
            power = bl;
            world.setBlockState(pos, state.with(POWERED, power), Block.NOTIFY_ALL);
        }
    }

    DoorTileProperty getTile(BlockState topState, BlockState leftState, BlockState bottomState, BlockState rightState, Direction facing){
        boolean top = topState.getBlock() instanceof TileDoorBlock && topState.get(FACING) == facing;
        boolean left = leftState.getBlock() instanceof TileDoorBlock && leftState.get(FACING) == facing;
        boolean bottom = bottomState.getBlock() instanceof TileDoorBlock && bottomState.get(FACING) == facing;
        boolean right = rightState.getBlock() instanceof TileDoorBlock && rightState.get(FACING) == facing;

        if(!top && !left && bottom && right) return DoorTileProperty.CORNER_TOP_LEFT;
        else if(!top && left && bottom && right) return DoorTileProperty.TOP;
        else if(!top && left && bottom && !right) return DoorTileProperty.CORNER_TOP_RIGHT;
        else if(top && !left && bottom && right) return DoorTileProperty.SIDE_LEFT;
        else if(top && left && bottom && right) return DoorTileProperty.CENTER;
        else if(top && left && bottom && !right) return DoorTileProperty.SIDE_RIGHT;
        else if(top && !left && !bottom && right) return DoorTileProperty.CORNER_BOTTOM_LEFT;
        else if(top && left && !bottom && right) return DoorTileProperty.BOTTOM;
        else if(top && left && !bottom && !right) return DoorTileProperty.CORNER_BOTTOM_RIGHT;
        else if(top && !left && !bottom && !right) return DoorTileProperty.SINGLE_BOTTOM;
        else if(top && !left && bottom && !right) return DoorTileProperty.SINGLE_CENTER;
        else if(!top && !left && bottom && !right) return DoorTileProperty.SINGLE_TOP;

        return DoorTileProperty.SINGLE;
    }
}
