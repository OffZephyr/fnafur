package net.zephyr.fnafur.networking.sounds;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record PlaySoundPingC2SPayload(int entityID, String name, float volume, float pitch) implements CustomPayload {

    public static final Id<PlaySoundPingC2SPayload> ID = new Id<>(SoundPayloads.PlaySoundC2S);

    public static final PacketCodec<RegistryByteBuf, PlaySoundPingC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, PlaySoundPingC2SPayload::entityID,
            PacketCodecs.STRING, PlaySoundPingC2SPayload::name,
            PacketCodecs.FLOAT, PlaySoundPingC2SPayload::volume,
            PacketCodecs.FLOAT, PlaySoundPingC2SPayload::pitch,
            PlaySoundPingC2SPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }

    public static void receive(PlaySoundPingC2SPayload payload, ServerPlayNetworking.Context context) {
        for(ServerPlayerEntity p : PlayerLookup.all(context.server())){
            ServerPlayNetworking.send(p, new PlaySoundS2CPayload(payload.entityID(), payload.name(), payload.volume(), payload.pitch()));
        }
    }
}
