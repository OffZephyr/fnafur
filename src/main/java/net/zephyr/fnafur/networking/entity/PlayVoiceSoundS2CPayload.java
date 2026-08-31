package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;

public record PlayVoiceSoundS2CPayload(int EntityID, String name, SoundEvent sound, float volume) implements CustomPacketPayload {
    public static final Type<PlayVoiceSoundS2CPayload> ID = new Type<>(EntityPayloads.S2CPlayEntityVoiceSound);
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayVoiceSoundS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PlayVoiceSoundS2CPayload::EntityID,
            ByteBufCodecs.STRING_UTF8, PlayVoiceSoundS2CPayload::name,
            ByteBufCodecs.fromCodec(SoundEvent.DIRECT_CODEC), PlayVoiceSoundS2CPayload::sound,
            ByteBufCodecs.FLOAT, PlayVoiceSoundS2CPayload::volume,
            PlayVoiceSoundS2CPayload::new);

    public static void receive(PlayVoiceSoundS2CPayload payload, ClientPlayNetworking.Context context) {
        Entity entity = context.player().level().getEntity(payload.EntityID);
        if(entity instanceof AnimatronicEntity entity1){
            entity1.playVoiceSound(payload.name, payload.sound, payload.volume);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
