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

public record StopSoundS2CPayload(int entityID, String name) implements CustomPacketPayload {

    public static final Type<StopSoundS2CPayload> ID = new Type<>(SoundPayloads.StopSoundS2C);

    public static final StreamCodec<RegistryFriendlyByteBuf, StopSoundS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, StopSoundS2CPayload::entityID,
            ByteBufCodecs.STRING_UTF8, StopSoundS2CPayload::name,
            StopSoundS2CPayload::new);

    public static void receive(StopSoundS2CPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            Entity entity = context.player().level().getEntity(payload.entityID());
            SoundEvent soundEvent = SoundsInit.getSound(payload.name());
            if(entity != null && SoundUtils.playingSound(context.player(), soundEvent)) {
                SoundUtils.stopSound(entity, soundEvent);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }
}
