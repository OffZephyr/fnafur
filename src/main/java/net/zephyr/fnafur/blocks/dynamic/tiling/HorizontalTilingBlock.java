package net.zephyr.fnafur.blocks.dynamic.tiling;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.ScheduledTickAccess;
import org.jetbrains.annotations.Nullable;

public class HorizontalTilingBlock extends Block {
    public static final EnumProperty<HorizontalTileStates> TYPE = EnumProperty.create("type", HorizontalTileStates.class);
    public HorizontalTilingBlock(Properties settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {

        boolean connectNorth = ctx.getLevel().getBlockState(ctx.getClickedPos().north()).is(defaultBlockState().getBlock());
        boolean connectEast = ctx.getLevel().getBlockState(ctx.getClickedPos().east()).is(defaultBlockState().getBlock());
        boolean connectSouth = ctx.getLevel().getBlockState(ctx.getClickedPos().south()).is(defaultBlockState().getBlock());
        boolean connectWest = ctx.getLevel().getBlockState(ctx.getClickedPos().west()).is(defaultBlockState().getBlock());

        return defaultBlockState().setValue(TYPE, HorizontalTileStates.get(connectNorth, connectEast, connectSouth, connectWest));
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {

        boolean connectNorth = world.getBlockState(pos.north()).is(state.getBlock());
        boolean connectEast = world.getBlockState(pos.east()).is(state.getBlock());
        boolean connectSouth = world.getBlockState(pos.south()).is(state.getBlock());
        boolean connectWest = world.getBlockState(pos.west()).is(state.getBlock());

        world.setBlockAndUpdate(pos, defaultBlockState().setValue(TYPE, HorizontalTileStates.get(connectNorth, connectEast, connectSouth, connectWest)));
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify) {
        boolean connectNorth = world.getBlockState(pos.north()).is(state.getBlock());
        boolean connectEast = world.getBlockState(pos.east()).is(state.getBlock());
        boolean connectSouth = world.getBlockState(pos.south()).is(state.getBlock());
        boolean connectWest = world.getBlockState(pos.west()).is(state.getBlock());

        world.setBlockAndUpdate(pos, defaultBlockState().setValue(TYPE, HorizontalTileStates.get(connectNorth, connectEast, connectSouth, connectWest)));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {

        /*boolean connectNorth = neighborPos.equals(pos.north()) && neighborState.isOf(state.getBlock());
        boolean connectEast = neighborPos.equals(pos.north()) && neighborState.isOf(state.getBlock());
        boolean connectSouth = neighborPos.equals(pos.north()) && neighborState.isOf(state.getBlock());
        boolean connectWest = neighborPos.equals(pos.north()) && neighborState.isOf(state.getBlock());

        return state.with(TYPE, HorizontalTileStates.get(connectNorth, connectEast, connectSouth, connectWest));*/

		return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(TYPE));
    }
}
