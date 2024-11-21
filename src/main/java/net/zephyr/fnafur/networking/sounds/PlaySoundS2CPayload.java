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

public record PlaySoundS2CPayload(int entityID, String name, float volume, float pitch) implements CustomPayload {

    public static final Id<PlaySoundS2CPayload> ID = new Id<>(SoundPayloads.PlaySoundS2C);

    public static final PacketCodec<RegistryByteBuf, PlaySoundS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, PlaySoundS2CPayload::entityID,
            PacketCodecs.STRING, PlaySoundS2CPayload::name,
            PacketCodecs.FLOAT, PlaySoundS2CPayload::volume,
            PacketCodecs.FLOAT, PlaySoundS2CPayload::pitch,
            PlaySoundS2CPayload::new);

    public static void receive(PlaySoundS2CPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            Entity entity = context.player().getWorld().getEntityById(payload.entityID());
            if(entity != null) {
                SoundEvent soundEvent = SoundsInit.getSound(payload.name());

                SoundUtils.playMutableSound(entity, soundEvent, payload.volume(), payload.pitch());}
        });
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
