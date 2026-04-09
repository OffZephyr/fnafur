package net.zephyr.fnafur.networking.sounds;


import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.zephyr.fnafur.init.SoundsInit;
import net.zephyr.fnafur.util.SoundUtils;

import java.util.Objects;

public record PlayBlockSoundS2CPayload(long pos, String name, String category, float volume, float pitch) implements CustomPacketPayload {

    public static final Type<PlayBlockSoundS2CPayload> ID = new Type<>(SoundPayloads.PlayBlockSoundS2C);

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayBlockSoundS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.LONG, PlayBlockSoundS2CPayload::pos,
            ByteBufCodecs.STRING_UTF8, PlayBlockSoundS2CPayload::name,
            ByteBufCodecs.STRING_UTF8, PlayBlockSoundS2CPayload::category,
            ByteBufCodecs.FLOAT, PlayBlockSoundS2CPayload::volume,
            ByteBufCodecs.FLOAT, PlayBlockSoundS2CPayload::pitch,
            PlayBlockSoundS2CPayload::new);

    public static void receive(PlayBlockSoundS2CPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
                SoundEvent soundEvent = SoundsInit.getSound(payload.name());
                SoundSource category = SoundSource.MASTER;
                for(SoundSource c : SoundSource.values()){
                    if(Objects.equals(c.getName(), payload.category())){
                        category = c;
                    }
                }

                context.player().level().playLocalSound(BlockPos.of(payload.pos()), soundEvent, category, payload.volume(), payload.pitch(), true);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }
}
