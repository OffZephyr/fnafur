package net.zephyr.fnafur.networking.sounds;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.server.level.ServerPlayer;

public record StopSoundPingC2SPayload(int entityID, String name) implements CustomPacketPayload {

    public static final Type<StopSoundPingC2SPayload> ID = new Type<>(SoundPayloads.StopSoundC2S);

    public static final StreamCodec<RegistryFriendlyByteBuf, StopSoundPingC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, StopSoundPingC2SPayload::entityID,
            ByteBufCodecs.STRING_UTF8, StopSoundPingC2SPayload::name,
            StopSoundPingC2SPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }

    public static void receive(StopSoundPingC2SPayload payload, ServerPlayNetworking.Context context) {
        for(ServerPlayer p : PlayerLookup.all(context.server())){
            ServerPlayNetworking.send(p, new StopSoundS2CPayload(payload.entityID(), payload.name()));
        }
    }
}
