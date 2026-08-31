package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;

public record SetEntityRunS2CPayload(int EntityID, boolean run) implements CustomPacketPayload {
    public static final Type<SetEntityRunS2CPayload> ID = new Type<>(EntityPayloads.S2CSetEntityRun);
    public static final StreamCodec<RegistryFriendlyByteBuf, SetEntityRunS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SetEntityRunS2CPayload::EntityID,
            ByteBufCodecs.BOOL, SetEntityRunS2CPayload::run,
            SetEntityRunS2CPayload::new);

    public static void receive(SetEntityRunS2CPayload payload, ClientPlayNetworking.Context context) {
        Entity entity = context.player().level().getEntity(payload.EntityID);
        if(entity instanceof AnimatronicEntity entity1){
            entity1.setCanRun(payload.run);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
