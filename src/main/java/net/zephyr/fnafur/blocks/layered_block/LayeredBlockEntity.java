package net.zephyr.fnafur.blocks.layered_block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.blocks.GoopyBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;

public class LayeredBlockEntity extends GoopyBlockEntity {
    public LayeredBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.LAYERED_BLOCK, pos, state);
    }
}
