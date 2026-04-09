package net.zephyr.fnafur.blocks.energy.blocks.generators;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.energy.entity.BaseEnergyBlockEntity;
import org.jetbrains.annotations.Nullable;

public class FuelGeneratorBlock extends BaseGeneratorBlock {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public FuelGeneratorBlock(Properties settings) {
        super(settings);
    }

    @Override
    public boolean isPowered(BlockGetter world, BlockPos pos) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(ACTIVE));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        InteractionResult result = super.useWithoutItem(state, world, pos, player, hit);
        if(result != InteractionResult.PASS) return result;
        world.setBlockAndUpdate(pos, state.cycle(ACTIVE));
        return InteractionResult.PASS;
    }
}

// TODO: find model problem