package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;

public record SetEntityGlowS2CPayload(int EntityID, boolean glow) implements CustomPayload {
    public static final Id<SetEntityGlowS2CPayload> ID = new Id<>(EntityPayloads.S2CSetEntityRun);
    public static final PacketCodec<RegistryByteBuf, SetEntityGlowS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, SetEntityGlowS2CPayload::EntityID,
            PacketCodecs.BOOLEAN, SetEntityGlowS2CPayload::glow,
            SetEntityGlowS2CPayload::new);

    public static void receive(SetEntityGlowS2CPayload payload, ClientPlayNetworking.Context context) {
        Entity entity = context.player().getEntityWorld().getEntityById(payload.EntityID);
        if(entity instanceof AnimatronicEntity entity1){
            entity1.setCanGlow(payload.glow);
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
