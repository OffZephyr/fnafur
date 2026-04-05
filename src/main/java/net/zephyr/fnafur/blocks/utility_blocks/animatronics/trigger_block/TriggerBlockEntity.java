package net.zephyr.fnafur.blocks.utility_blocks.animatronics.trigger_block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;

public class TriggerBlockEntity extends BlockEntity {
    public TriggerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.TRIGGER_BLOCK, pos, state);
    }

    public void tick(World world, BlockPos blockPos, BlockState state, TriggerBlockEntity entity) {

    }
}
