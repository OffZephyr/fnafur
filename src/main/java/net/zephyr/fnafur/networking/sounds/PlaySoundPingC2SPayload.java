package net.zephyr.fnafur.networking.sounds;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.server.level.ServerPlayer;

public record PlaySoundPingC2SPayload(int entityID, String name, float volume, float pitch) implements CustomPacketPayload {

    public static final Type<PlaySoundPingC2SPayload> ID = new Type<>(SoundPayloads.PlaySoundC2S);

    public static final StreamCodec<RegistryFriendlyByteBuf, PlaySoundPingC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PlaySoundPingC2SPayload::entityID,
            ByteBufCodecs.STRING_UTF8, PlaySoundPingC2SPayload::name,
            ByteBufCodecs.FLOAT, PlaySoundPingC2SPayload::volume,
            ByteBufCodecs.FLOAT, PlaySoundPingC2SPayload::pitch,
            PlaySoundPingC2SPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }

    public static void receive(PlaySoundPingC2SPayload payload, ServerPlayNetworking.Context context) {
        for(ServerPlayer p : PlayerLookup.all(context.server())){
            ServerPlayNetworking.send(p, new PlaySoundS2CPayload(payload.entityID(), payload.name(), payload.volume(), payload.pitch()));
        }
    }
}
