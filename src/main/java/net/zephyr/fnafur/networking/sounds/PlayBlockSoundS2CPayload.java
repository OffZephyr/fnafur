package net.zephyr.fnafur.networking.sounds;


import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.init.SoundsInit;
import net.zephyr.fnafur.util.SoundUtils;

import java.util.Objects;

public record PlayBlockSoundS2CPayload(long pos, String name, String category, float volume, float pitch) implements CustomPayload {

    public static final Id<PlayBlockSoundS2CPayload> ID = new Id<>(SoundPayloads.PlayBlockSoundS2C);

    public static final PacketCodec<RegistryByteBuf, PlayBlockSoundS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.LONG, PlayBlockSoundS2CPayload::pos,
            PacketCodecs.STRING, PlayBlockSoundS2CPayload::name,
            PacketCodecs.STRING, PlayBlockSoundS2CPayload::category,
            PacketCodecs.FLOAT, PlayBlockSoundS2CPayload::volume,
            PacketCodecs.FLOAT, PlayBlockSoundS2CPayload::pitch,
            PlayBlockSoundS2CPayload::new);

    public static void receive(PlayBlockSoundS2CPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
                SoundEvent soundEvent = SoundsInit.getSound(payload.name());
                SoundCategory category = SoundCategory.MASTER;
                for(SoundCategory c : SoundCategory.values()){
                    if(Objects.equals(c.getName(), payload.category())){
                        category = c;
                    }
                }

                context.player().getWorld().playSoundAtBlockCenter(BlockPos.fromLong(payload.pos()), soundEvent, category, payload.volume(), payload.pitch(), true);
        });
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
