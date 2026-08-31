package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;

public record SetEntityRunC2SPayload(int EntityID) implements CustomPacketPayload {
    public static final Type<SetEntityRunC2SPayload> ID = new Type<>(EntityPayloads.C2SSetEntityRun);
    public static final StreamCodec<RegistryFriendlyByteBuf, SetEntityRunC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SetEntityRunC2SPayload::EntityID,
            SetEntityRunC2SPayload::new);

    public static void receive(SetEntityRunC2SPayload payload, ServerPlayNetworking.Context context) {
        Entity entity = context.player().level().getEntity(payload.EntityID);
        if(entity instanceof AnimatronicEntity entity1){
            ServerPlayNetworking.send(context.player(), new SetEntityRunS2CPayload(entity1.getId(), entity1.isRunning()));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
