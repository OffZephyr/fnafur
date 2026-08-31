package net.zephyr.fnafur.networking.nbt_updates;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record UpdateBlockNbtS2CPongPayload(long pos, CompoundTag data) implements CustomPacketPayload {

    public static final Type<UpdateBlockNbtS2CPongPayload> ID = new Type<>(NbtPayloads.S2CBlockUpdatePong);

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateBlockNbtS2CPongPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, UpdateBlockNbtS2CPongPayload::pos,
            ByteBufCodecs.COMPOUND_TAG, UpdateBlockNbtS2CPongPayload::data,
            UpdateBlockNbtS2CPongPayload::new);

    public static void receive(UpdateBlockNbtS2CPongPayload payload, ClientPlayNetworking.Context context) {
        BlockEntity entity = context.player().level().getBlockEntity(BlockPos.of(payload.pos()));
        context.player().level().setBlockAndUpdate(BlockPos.of(payload.pos), context.player().level().getBlockState(BlockPos.of(payload.pos())));
        if (entity == null) return;
        ((IEntityDataSaver) entity).setServerUpdateStatus(false);
        CompoundTag newNbt = payload.data().copy();
        newNbt.putBoolean("synced", true);
        ((IEntityDataSaver) entity).setPersistentData(newNbt);
        entity.setChanged();

        BlockState state = context.player().level().getBlockState(BlockPos.of(payload.pos));
        context.player().level().setBlock(BlockPos.of(payload.pos), state, Block.UPDATE_ALL_IMMEDIATE);
        context.player().level().sendBlockUpdated(BlockPos.of(payload.pos), state, state, Block.UPDATE_ALL_IMMEDIATE);
        //context.player().sendMessage(Text.literal("§9" +"SYNC CLIENT"), false);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }
}
