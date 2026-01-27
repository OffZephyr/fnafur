package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.sound.SoundEvent;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;

public record PlayVoiceSoundS2CPayload(int EntityID, String name, SoundEvent sound, float volume) implements CustomPayload {
    public static final Id<PlayVoiceSoundS2CPayload> ID = new Id<>(EntityPayloads.S2CPlayEntityVoiceSound);
    public static final PacketCodec<RegistryByteBuf, PlayVoiceSoundS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, PlayVoiceSoundS2CPayload::EntityID,
            PacketCodecs.STRING, PlayVoiceSoundS2CPayload::name,
            PacketCodecs.codec(SoundEvent.CODEC), PlayVoiceSoundS2CPayload::sound,
            PacketCodecs.FLOAT, PlayVoiceSoundS2CPayload::volume,
            PlayVoiceSoundS2CPayload::new);

    public static void receive(PlayVoiceSoundS2CPayload payload, ClientPlayNetworking.Context context) {
        Entity entity = context.player().getEntityWorld().getEntityById(payload.EntityID);
        if(entity instanceof AnimatronicEntity entity1){
            entity1.playVoiceSound(payload.name, payload.sound, payload.volume);
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
