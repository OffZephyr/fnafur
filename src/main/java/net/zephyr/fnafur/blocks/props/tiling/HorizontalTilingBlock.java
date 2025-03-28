package net.zephyr.fnafur.blocks.props.tiling;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

public class HorizontalTilingBlock extends Block {
    public static final EnumProperty<HorizontalTileStates> TYPE = EnumProperty.of("type", HorizontalTileStates.class);
    public HorizontalTilingBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {

        boolean connectNorth = ctx.getWorld().getBlockState(ctx.getBlockPos().north()).isOf(getDefaultState().getBlock());
        boolean connectEast = ctx.getWorld().getBlockState(ctx.getBlockPos().east()).isOf(getDefaultState().getBlock());
        boolean connectSouth = ctx.getWorld().getBlockState(ctx.getBlockPos().south()).isOf(getDefaultState().getBlock());
        boolean connectWest = ctx.getWorld().getBlockState(ctx.getBlockPos().west()).isOf(getDefaultState().getBlock());

        return getDefaultState().with(TYPE, HorizontalTileStates.get(connectNorth, connectEast, connectSouth, connectWest));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {


        boolean connectNorth = world.getBlockState(pos.north()).isOf(state.getBlock());
        boolean connectEast = world.getBlockState(pos.east()).isOf(state.getBlock());
        boolean connectSouth = world.getBlockState(pos.south()).isOf(state.getBlock());
        boolean connectWest = world.getBlockState(pos.west()).isOf(state.getBlock());

        world.setBlockState(pos, getDefaultState().with(TYPE, HorizontalTileStates.get(connectNorth, connectEast, connectSouth, connectWest)));
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        boolean connectNorth = world.getBlockState(pos.north()).isOf(state.getBlock());
        boolean connectEast = world.getBlockState(pos.east()).isOf(state.getBlock());
        boolean connectSouth = world.getBlockState(pos.south()).isOf(state.getBlock());
        boolean connectWest = world.getBlockState(pos.west()).isOf(state.getBlock());

        world.setBlockState(pos, getDefaultState().with(TYPE, HorizontalTileStates.get(connectNorth, connectEast, connectSouth, connectWest)));
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {

        /*boolean connectNorth = neighborPos.equals(pos.north()) && neighborState.isOf(state.getBlock());
        boolean connectEast = neighborPos.equals(pos.north()) && neighborState.isOf(state.getBlock());
        boolean connectSouth = neighborPos.equals(pos.north()) && neighborState.isOf(state.getBlock());
        boolean connectWest = neighborPos.equals(pos.north()) && neighborState.isOf(state.getBlock());

        return state.with(TYPE, HorizontalTileStates.get(connectNorth, connectEast, connectSouth, connectWest));*/

		return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(TYPE));
    }
}
