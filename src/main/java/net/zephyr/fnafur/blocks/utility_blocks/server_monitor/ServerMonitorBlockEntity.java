package net.zephyr.fnafur.blocks.utility_blocks.server_monitor;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;

public class ServerMonitorBlockEntity extends BlockEntity {
    public ServerMonitorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.SERVER_MONITOR, pos, state);
    }

    public void tick(World world, BlockPos blockPos, BlockState state, ServerMonitorBlockEntity entity) {

    }
}
