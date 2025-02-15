package net.zephyr.fnafur.blocks.utility_blocks.cpu_config_panel;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;

public class CpuConfigPanelBlockEntity extends BlockEntity {
    public CpuConfigPanelBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.CPU_CONFIG_PANEL, pos, state);
    }
}
