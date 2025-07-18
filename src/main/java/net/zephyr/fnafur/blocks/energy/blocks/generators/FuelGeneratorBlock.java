package net.zephyr.fnafur.blocks.energy.blocks.generators;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtInt;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.energy.entity.BaseEnergyBlockEntity;
import org.jetbrains.annotations.Nullable;

public class FuelGeneratorBlock extends BaseGeneratorBlock {
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

    public FuelGeneratorBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isPowered(BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(ACTIVE));
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        ActionResult result = super.onUse(state, world, pos, player, hit);
        if(result != ActionResult.PASS) return result;
        world.setBlockState(pos, state.cycle(ACTIVE));
        return ActionResult.PASS;
    }
}

// TODO: find model problem