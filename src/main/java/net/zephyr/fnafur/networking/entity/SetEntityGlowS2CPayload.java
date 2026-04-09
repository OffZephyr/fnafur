package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;

public record SetEntityGlowS2CPayload(int EntityID, boolean glow) implements CustomPacketPayload {
    public static final Type<SetEntityGlowS2CPayload> ID = new Type<>(EntityPayloads.S2CSetEntityGlow);
    public static final StreamCodec<RegistryFriendlyByteBuf, SetEntityGlowS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SetEntityGlowS2CPayload::EntityID,
            ByteBufCodecs.BOOL, SetEntityGlowS2CPayload::glow,
            SetEntityGlowS2CPayload::new);

    public static void receive(SetEntityGlowS2CPayload payload, ClientPlayNetworking.Context context) {
        Entity entity = context.player().level().getEntity(payload.EntityID);
        if(entity instanceof AnimatronicEntity entity1){
            entity1.setCanGlow(payload.glow);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
