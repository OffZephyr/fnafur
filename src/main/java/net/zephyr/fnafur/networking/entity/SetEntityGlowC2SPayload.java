package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;

public record SetEntityGlowC2SPayload(int EntityID) implements CustomPayload {
    public static final Id<SetEntityGlowC2SPayload> ID = new Id<>(EntityPayloads.C2SSetEntityRun);
    public static final PacketCodec<RegistryByteBuf, SetEntityGlowC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, SetEntityGlowC2SPayload::EntityID,
            SetEntityGlowC2SPayload::new);

    public static void receive(SetEntityGlowC2SPayload payload, ServerPlayNetworking.Context context) {
        Entity entity = context.player().getEntityWorld().getEntityById(payload.EntityID);
        if(entity instanceof AnimatronicEntity entity1){
            ServerPlayNetworking.send(context.player(), new SetEntityGlowS2CPayload(entity1.getId(), entity1.shouldEyesGlow()));
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
