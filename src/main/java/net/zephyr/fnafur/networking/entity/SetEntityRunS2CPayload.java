package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;

public record SetEntityRunS2CPayload(int EntityID, boolean run) implements CustomPayload {
    public static final Id<SetEntityRunS2CPayload> ID = new Id<>(EntityPayloads.S2CSetEntityRun);
    public static final PacketCodec<RegistryByteBuf, SetEntityRunS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, SetEntityRunS2CPayload::EntityID,
            PacketCodecs.BOOLEAN, SetEntityRunS2CPayload::run,
            SetEntityRunS2CPayload::new);

    public static void receive(SetEntityRunS2CPayload payload, ClientPlayNetworking.Context context) {
        Entity entity = context.player().getEntityWorld().getEntityById(payload.EntityID);
        if(entity instanceof AnimatronicEntity entity1){
            entity1.setCanRun(payload.run);
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
