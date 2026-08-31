package net.zephyr.fnafur.blocks.light;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.Nullable;

public class ChainLight extends HorizontalDirectionalBlock {

    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
    public static final BooleanProperty CHAIN = BooleanProperty.create("chain");
    public ChainLight(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(LIT, false).setValue(CHAIN, false));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return null;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState downState = ctx.getLevel().getBlockState(ctx.getClickedPos().below());
        boolean chain = downState.getBlock() instanceof ChainLight;
        Direction direction = chain ? downState.getValue(FACING) : ctx.getHorizontalDirection().getOpposite();

        return this.defaultBlockState().setValue(FACING, direction).setValue(CHAIN, chain).setValue(LIT, ctx.getLevel().hasNeighborSignal(ctx.getClickedPos()));
    }
    @Override
    protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify) {
        BlockState upState = world.getBlockState(pos.above());

        BlockState downState = world.getBlockState(pos.below());
        boolean chain = downState.getBlock() instanceof ChainLight;
        Direction direction = chain ? downState.getValue(FACING): state.getValue(FACING);

        boolean bl = world.hasNeighborSignal(pos) || (upState.getBlock() instanceof ChainLight && upState.getValue(LIT));

        world.setBlock(pos, state.setValue(LIT, bl).setValue(CHAIN, world.getBlockState(pos.below()).getBlock() instanceof ChainLight).setValue(FACING, direction), Block.UPDATE_ALL);
    }

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if ((Boolean)state.getValue(LIT) && !world.hasNeighborSignal(pos)) {
            world.setBlock(pos, state.cycle(LIT), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, CHAIN, FACING);
    }
}
