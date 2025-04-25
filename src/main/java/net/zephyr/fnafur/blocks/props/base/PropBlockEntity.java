package net.zephyr.fnafur.blocks.props.base;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtC2SGetFromServerPayload;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtC2SPayload;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class PropBlockEntity extends BlockEntity {
    public PropBlockEntity(BlockPos pos, BlockState state) {
        this(BlockEntityInit.PROPS, pos, state);
    }
    public PropBlockEntity(BlockEntityType<? extends BlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void tick(World world, BlockPos blockPos, BlockState state, PropBlockEntity entity) {
        if (world.isClient()) {
            if (!((IEntityDataSaver) entity).getPersistentData().getBoolean("synced")) {
                ClientPlayNetworking.send(new UpdateBlockNbtC2SGetFromServerPayload(getPos().asLong()));
                world.setBlockState(blockPos, world.getBlockState(blockPos), Block.NOTIFY_ALL_AND_REDRAW);
            }

            if (((IEntityDataSaver) this).getServerUpdateStatus()) {
                MinecraftClient.getInstance().player.sendMessage(Text.literal("SYNCING PROP"), false);
                ClientPlayNetworking.send(new UpdateBlockNbtC2SPayload(getPos().asLong(), ((IEntityDataSaver) this).getPersistentData()));
            }
        }
    }
}
