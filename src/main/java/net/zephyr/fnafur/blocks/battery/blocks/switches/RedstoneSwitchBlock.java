package net.zephyr.fnafur.blocks.battery.blocks.switches;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import org.jetbrains.annotations.Nullable;

public class RedstoneSwitchBlock extends Block implements BlockEntityProvider {
    // Variables
    BlockPos blockPos;
    BlockState blockState;
    RedstoneSwitchBlockEntity entity;

    public RedstoneSwitchBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        blockState  = state;
        blockPos    = pos;
        entity      = BlockEntityInit.REDSTONE_SWITCH.instantiate(pos, state);
        return entity;
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return 16 * (entity.isPowered() ? 1 : 0);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if(world.isClient) return super.onUse(state, world, pos, player, hit);
        entity.cycle();
        world.updateNeighbors(pos, state.getBlock());
        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, blockEntity) -> {
            if(entity == null) return;
            world1.updateNeighbors(pos, state1.getBlock());
            world1.setBlockState(pos, state1);
        };
    }
}
