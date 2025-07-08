package net.zephyr.fnafur.blocks.utility_blocks.animatronics.chip_reader;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;

public class ChipReaderBlockEntity extends BlockEntity {
    public ChipReaderBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.CHIP_READER, pos, state);
    }
}
