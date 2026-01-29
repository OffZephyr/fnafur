package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;

public record SetAnimatronicFrozenStatusS2CPayload(int EntityID, boolean frozen, float headYaw, float bodyYaw, float pitch) implements CustomPayload {

    public static final Id<SetAnimatronicFrozenStatusS2CPayload> ID = new Id<>(EntityPayloads.S2CSetEntityFrozen);

    public static final PacketCodec<RegistryByteBuf, SetAnimatronicFrozenStatusS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, SetAnimatronicFrozenStatusS2CPayload::EntityID,
            PacketCodecs.BOOLEAN, SetAnimatronicFrozenStatusS2CPayload::frozen,
            PacketCodecs.FLOAT, SetAnimatronicFrozenStatusS2CPayload::headYaw,
            PacketCodecs.FLOAT, SetAnimatronicFrozenStatusS2CPayload::bodyYaw,
            PacketCodecs.FLOAT, SetAnimatronicFrozenStatusS2CPayload::pitch,
            SetAnimatronicFrozenStatusS2CPayload::new);

    public static void receive(SetAnimatronicFrozenStatusS2CPayload payload, ClientPlayNetworking.Context context) {
        Entity entity = context.player().getEntityWorld().getEntityById(payload.EntityID);
        if(entity instanceof AnimatronicEntity entity1){
            entity1.isFrozen = payload.frozen;
            entity1.frozenPitch = entity1.getPitch();
            entity1.frozenBodyYaw = entity1.getBodyYaw();
            entity1.frozenHeadYaw = entity1.getHeadYaw();
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
