package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;

public record SetEntityGlowC2SPayload(int EntityID) implements CustomPacketPayload {
    public static final Type<SetEntityGlowC2SPayload> ID = new Type<>(EntityPayloads.C2SSetEntityGlow);
    public static final StreamCodec<RegistryFriendlyByteBuf, SetEntityGlowC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SetEntityGlowC2SPayload::EntityID,
            SetEntityGlowC2SPayload::new);

    public static void receive(SetEntityGlowC2SPayload payload, ServerPlayNetworking.Context context) {
        Entity entity = context.player().level().getEntity(payload.EntityID);
        if(entity instanceof AnimatronicEntity entity1){
            ServerPlayNetworking.send(context.player(), new SetEntityGlowS2CPayload(entity1.getId(), entity1.shouldEyesGlow()));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
