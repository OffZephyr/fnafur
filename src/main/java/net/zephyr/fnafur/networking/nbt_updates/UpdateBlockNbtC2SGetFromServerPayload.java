package net.zephyr.fnafur.networking.nbt_updates;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record UpdateBlockNbtC2SGetFromServerPayload(long pos) implements CustomPacketPayload {

    public static final Type<UpdateBlockNbtC2SGetFromServerPayload> ID = new Type<>(NbtPayloads.S2CBlockUpdateClient);

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateBlockNbtC2SGetFromServerPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, UpdateBlockNbtC2SGetFromServerPayload::pos,
            UpdateBlockNbtC2SGetFromServerPayload::new);

    public static void receive(UpdateBlockNbtC2SGetFromServerPayload payload, ServerPlayNetworking.Context context) {
        BlockEntity entity = context.player().level().getBlockEntity(BlockPos.of(payload.pos()));
        if (entity == null) return;
        for (ServerPlayer p : PlayerLookup.all(context.server())) {
            //p.sendMessage(Text.literal("UPDATING FROM SERVER"), false);
            ServerPlayNetworking.send(p, new UpdateBlockNbtS2CPongPayload(payload.pos(), ((IEntityDataSaver) entity).getPersistentData()));
        }

    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }
}
