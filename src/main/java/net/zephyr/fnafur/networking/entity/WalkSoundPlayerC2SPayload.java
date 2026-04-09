package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;

public record WalkSoundPlayerC2SPayload(int EntityID) implements CustomPacketPayload {
    public static final Type<WalkSoundPlayerC2SPayload> ID = new Type<>(EntityPayloads.C2SWalkSoundPlayer);
    public static final StreamCodec<RegistryFriendlyByteBuf, WalkSoundPlayerC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, WalkSoundPlayerC2SPayload::EntityID,
            WalkSoundPlayerC2SPayload::new);

    public static void receive(WalkSoundPlayerC2SPayload payload, ServerPlayNetworking.Context context) {
        Entity entity = context.player().level().getEntity(payload.EntityID);
        if(entity instanceof AnimatronicEntity entity1){
            entity1.playWalkSound(context.player().level());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
