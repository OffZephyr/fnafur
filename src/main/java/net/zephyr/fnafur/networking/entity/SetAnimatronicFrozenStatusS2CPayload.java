package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;

public record SetAnimatronicFrozenStatusS2CPayload(int EntityID, boolean frozen, float headYaw, float bodyYaw, float pitch) implements CustomPacketPayload {

    public static final Type<SetAnimatronicFrozenStatusS2CPayload> ID = new Type<>(EntityPayloads.S2CSetEntityFrozen);

    public static final StreamCodec<RegistryFriendlyByteBuf, SetAnimatronicFrozenStatusS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SetAnimatronicFrozenStatusS2CPayload::EntityID,
            ByteBufCodecs.BOOL, SetAnimatronicFrozenStatusS2CPayload::frozen,
            ByteBufCodecs.FLOAT, SetAnimatronicFrozenStatusS2CPayload::headYaw,
            ByteBufCodecs.FLOAT, SetAnimatronicFrozenStatusS2CPayload::bodyYaw,
            ByteBufCodecs.FLOAT, SetAnimatronicFrozenStatusS2CPayload::pitch,
            SetAnimatronicFrozenStatusS2CPayload::new);

    public static void receive(SetAnimatronicFrozenStatusS2CPayload payload, ClientPlayNetworking.Context context) {
        Entity entity = context.player().level().getEntity(payload.EntityID);
        if(entity instanceof AnimatronicEntity entity1){
            entity1.isFrozen = payload.frozen;
            entity1.frozenPitch = entity1.getXRot();
            entity1.frozenBodyYaw = entity1.getVisualRotationYInDegrees();
            entity1.frozenHeadYaw = entity1.getYHeadRot();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
