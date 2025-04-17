package net.zephyr.fnafur.blocks.light;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;

public class ChainLight extends HorizontalFacingBlock {

    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
    public static final BooleanProperty CHAIN = BooleanProperty.of("chain");
    public ChainLight(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(LIT, false).with(CHAIN, false));
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return null;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState downState = ctx.getWorld().getBlockState(ctx.getBlockPos().down());
        boolean chain = downState.getBlock() instanceof ChainLight;
        Direction direction = chain ? downState.get(FACING) : ctx.getHorizontalPlayerFacing().getOpposite();

        return this.getDefaultState().with(FACING, direction).with(CHAIN, chain).with(LIT, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
    }
    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        BlockState upState = world.getBlockState(pos.up());

        BlockState downState = world.getBlockState(pos.down());
        boolean chain = downState.getBlock() instanceof ChainLight;
        Direction direction = chain ? downState.get(FACING): state.get(FACING);

        boolean bl = world.isReceivingRedstonePower(pos) || (upState.getBlock() instanceof ChainLight && upState.get(LIT));

        world.setBlockState(pos, state.with(LIT, bl).with(CHAIN, world.getBlockState(pos.down()).getBlock() instanceof ChainLight).with(FACING, direction), Block.NOTIFY_ALL);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if ((Boolean)state.get(LIT) && !world.isReceivingRedstonePower(pos)) {
            world.setBlockState(pos, state.cycle(LIT), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT, CHAIN, FACING);
    }
}
