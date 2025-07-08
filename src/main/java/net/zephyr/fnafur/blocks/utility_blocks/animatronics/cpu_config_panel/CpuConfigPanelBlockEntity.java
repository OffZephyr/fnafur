package net.zephyr.fnafur.blocks.utility_blocks.animatronics.cpu_config_panel;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;

public class CpuConfigPanelBlockEntity extends BlockEntity {
    public CpuConfigPanelBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.CPU_CONFIG_PANEL, pos, state);
    }

    public void tick(World world, BlockPos blockPos, BlockState state, CpuConfigPanelBlockEntity entity) {

    }
}
