package net.zephyr.fnafur.networking.sounds;


import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.zephyr.fnafur.init.SoundsInit;
import net.zephyr.fnafur.util.SoundUtils;

public record PlaySoundS2CPayload(int entityID, String name, float volume, float pitch) implements CustomPacketPayload {

    public static final Type<PlaySoundS2CPayload> ID = new Type<>(SoundPayloads.PlaySoundS2C);

    public static final StreamCodec<RegistryFriendlyByteBuf, PlaySoundS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PlaySoundS2CPayload::entityID,
            ByteBufCodecs.STRING_UTF8, PlaySoundS2CPayload::name,
            ByteBufCodecs.FLOAT, PlaySoundS2CPayload::volume,
            ByteBufCodecs.FLOAT, PlaySoundS2CPayload::pitch,
            PlaySoundS2CPayload::new);

    public static void receive(PlaySoundS2CPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            Entity entity = context.player().level().getEntity(payload.entityID());
            if(entity != null) {
                SoundEvent soundEvent = SoundsInit.getSound(payload.name());

                SoundUtils.playMutableSound(entity, soundEvent, payload.volume(), payload.pitch());}
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }
}
