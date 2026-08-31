package net.zephyr.fnafur.blocks.utility_blocks.animatronics.chip_reader;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.linking.links.LinkSourceBlockEntity;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.server_monitor.ServerMonitorBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class ChipReaderBlockEntity extends LinkSourceBlockEntity {
    public ChipReaderBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.CHIP_READER, pos, state);
    }

    @Override
    public void tick(Level world, BlockPos blockPos, BlockState state, LinkSourceBlockEntity entity) {
        super.tick(world, blockPos, state, entity);
    }

    @Override
    public boolean canLink(IEntityDataSaver link) {
        return link instanceof ServerMonitorBlockEntity;
    }
}
