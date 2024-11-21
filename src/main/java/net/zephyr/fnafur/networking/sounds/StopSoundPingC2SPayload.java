package net.zephyr.fnafur.networking.sounds;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record StopSoundPingC2SPayload(int entityID, String name) implements CustomPayload {

    public static final Id<StopSoundPingC2SPayload> ID = new Id<>(SoundPayloads.StopSoundC2S);

    public static final PacketCodec<RegistryByteBuf, StopSoundPingC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, StopSoundPingC2SPayload::entityID,
            PacketCodecs.STRING, StopSoundPingC2SPayload::name,
            StopSoundPingC2SPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }

    public static void receive(StopSoundPingC2SPayload payload, ServerPlayNetworking.Context context) {
        for(ServerPlayerEntity p : PlayerLookup.all(context.server())){
            ServerPlayNetworking.send(p, new StopSoundS2CPayload(payload.entityID(), payload.name()));
        }
    }
}
