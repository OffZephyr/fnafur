package net.zephyr.fnafur.blocks.energy.blocks.switches;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import javax.swing.plaf.ComponentUI;

public class CircuitBreakerBlock extends BaseWallSwitchBlock {

    public static String KEY_CIRCUIT_OPEN = "circuit_open";
    public static BooleanProperty CIRCUIT_OPEN = BooleanProperty.create(KEY_CIRCUIT_OPEN);

    public CircuitBreakerBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        world.setBlockAndUpdate(pos, state.cycle(CIRCUIT_OPEN));
        return InteractionResult.PASS;
    }

    @Override
    public boolean isPowered(BlockGetter world, BlockPos pos) {
        return world.getBlockState(pos).getValue(CIRCUIT_OPEN) && super.isPowered(world, pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder builder) {
        builder.add(CIRCUIT_OPEN);
        super.createBlockStateDefinition(builder);
    }

}
