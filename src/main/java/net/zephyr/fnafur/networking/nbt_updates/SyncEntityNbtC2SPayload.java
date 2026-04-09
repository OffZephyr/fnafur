package net.zephyr.fnafur.networking.nbt_updates;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record SyncEntityNbtC2SPayload(int entityid) implements CustomPacketPayload {

    public static final Type<SyncEntityNbtC2SPayload> ID = new Type<>(NbtPayloads.C2SEntitySync);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncEntityNbtC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SyncEntityNbtC2SPayload::entityid,
            SyncEntityNbtC2SPayload::new);

    public static void receive(SyncEntityNbtC2SPayload payload, ServerPlayNetworking.Context context) {
        Entity entity = context.player().level().getEntity(payload.entityid());

        for(ServerPlayer p : PlayerLookup.all(context.server())){
            ServerPlayNetworking.send(p, new UpdateEntityNbtS2CPongPayload(payload.entityid(), ((IEntityDataSaver)entity).getPersistentData()));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }
}
