package net.zephyr.fnafur.blocks.props.base;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtC2SGetFromServerPayload;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtC2SPayload;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class PropBlockEntity extends BlockEntity {
    public PropBlockEntity(BlockPos pos, BlockState state) {
        this(BlockEntityInit.PROPS, pos, state);
    }
    public PropBlockEntity(BlockEntityType<? extends BlockEntity> type, BlockPos pos, BlockState state) {
        this(type, pos, state, false);
    }
    public PropBlockEntity(BlockEntityType<? extends BlockEntity> type, BlockPos pos, BlockState state, boolean createLink) {
        super(type, pos, state);
    }

    public void tick(Level world, BlockPos blockPos, BlockState state, PropBlockEntity entity) {

        if (world.isClientSide()) {
            if (!((IEntityDataSaver) entity).getPersistentData().contains("synced")) {
                ClientPlayNetworking.send(new UpdateBlockNbtC2SGetFromServerPayload(getBlockPos().asLong()));
                world.setBlock(blockPos, world.getBlockState(blockPos), Block.UPDATE_ALL_IMMEDIATE);
            }

            if (((IEntityDataSaver) this).getServerUpdateStatus()) {
                //Minecraft.getInstance().player.sendMessage(Text.literal("SYNCING PROP"), false);
                ClientPlayNetworking.send(new UpdateBlockNbtC2SPayload(getBlockPos().asLong(), ((IEntityDataSaver) this).getPersistentData()));
            }
        }
    }
}
