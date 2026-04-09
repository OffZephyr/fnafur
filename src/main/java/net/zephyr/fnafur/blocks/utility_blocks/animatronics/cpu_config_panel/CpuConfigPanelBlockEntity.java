package net.zephyr.fnafur.blocks.utility_blocks.animatronics.cpu_config_panel;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;

public class CpuConfigPanelBlockEntity extends BlockEntity {
    public CpuConfigPanelBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.CPU_CONFIG_PANEL, pos, state);
    }

    public void tick(Level world, BlockPos blockPos, BlockState state, CpuConfigPanelBlockEntity entity) {

    }
}
