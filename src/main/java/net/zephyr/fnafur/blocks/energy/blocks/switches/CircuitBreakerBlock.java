package net.zephyr.fnafur.blocks.energy.blocks.switches;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import javax.swing.plaf.ComponentUI;

public class CircuitBreakerBlock extends BaseWallSwitchBlock {

    public static String KEY_CIRCUIT_OPEN = "circuit_open";
    public static BooleanProperty CIRCUIT_OPEN = BooleanProperty.of(KEY_CIRCUIT_OPEN);

    public CircuitBreakerBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        world.setBlockState(pos, state.cycle(CIRCUIT_OPEN));
        return ActionResult.PASS;
    }

    @Override
    public boolean isPowered(BlockView world, BlockPos pos) {
        return world.getBlockState(pos).get(CIRCUIT_OPEN) && super.isPowered(world, pos);
    }

    @Override
    protected void appendProperties(StateManager.Builder builder) {
        builder.add(CIRCUIT_OPEN);
        super.appendProperties(builder);
    }

}
