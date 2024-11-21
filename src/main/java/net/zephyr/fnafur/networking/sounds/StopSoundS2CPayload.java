package net.zephyr.fnafur.networking.sounds;


import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.sound.SoundEvent;
import net.zephyr.fnafur.init.SoundsInit;
import net.zephyr.fnafur.util.SoundUtils;

public record StopSoundS2CPayload(int entityID, String name) implements CustomPayload {

    public static final Id<StopSoundS2CPayload> ID = new Id<>(SoundPayloads.StopSoundS2C);

    public static final PacketCodec<RegistryByteBuf, StopSoundS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, StopSoundS2CPayload::entityID,
            PacketCodecs.STRING, StopSoundS2CPayload::name,
            StopSoundS2CPayload::new);

    public static void receive(StopSoundS2CPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            Entity entity = context.player().getWorld().getEntityById(payload.entityID());
            SoundEvent soundEvent = SoundsInit.getSound(payload.name());
            if(entity != null && SoundUtils.playingSound(context.player(), soundEvent)) {
                SoundUtils.stopSound(entity, soundEvent);
            }
        });
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
