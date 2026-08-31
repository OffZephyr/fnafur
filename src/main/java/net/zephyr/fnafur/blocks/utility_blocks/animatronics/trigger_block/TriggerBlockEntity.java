package net.zephyr.fnafur.blocks.utility_blocks.animatronics.trigger_block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;

public class TriggerBlockEntity extends BlockEntity {
    public TriggerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.TRIGGER_BLOCK, pos, state);
    }

    public void tick(Level world, BlockPos blockPos, BlockState state, TriggerBlockEntity entity) {

    }
}
