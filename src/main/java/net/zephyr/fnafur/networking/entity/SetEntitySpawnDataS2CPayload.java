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
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record SetEntitySpawnDataS2CPayload(int EntityID, double x, double y, double z, float yaw) implements CustomPacketPayload {
    public static final Type<SetEntitySpawnDataS2CPayload> ID = new Type<>(EntityPayloads.S2CSetSpawnData);
    public static final StreamCodec<RegistryFriendlyByteBuf, SetEntitySpawnDataS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SetEntitySpawnDataS2CPayload::EntityID,
            ByteBufCodecs.DOUBLE, SetEntitySpawnDataS2CPayload::x,
            ByteBufCodecs.DOUBLE, SetEntitySpawnDataS2CPayload::y,
            ByteBufCodecs.DOUBLE, SetEntitySpawnDataS2CPayload::z,
            ByteBufCodecs.FLOAT, SetEntitySpawnDataS2CPayload::yaw,
            SetEntitySpawnDataS2CPayload::new);

    public static void receive(SetEntitySpawnDataS2CPayload payload, ClientPlayNetworking.Context context) {
        Entity entity = context.player().level().getEntity(payload.EntityID);
        if(entity instanceof AnimatronicEntity entity1){
            ((IEntityDataSaver)entity1).getPersistentData().putDouble("spawnX", payload.x);
            ((IEntityDataSaver)entity1).getPersistentData().putDouble("spawnY", payload.y);
            ((IEntityDataSaver)entity1).getPersistentData().putDouble("spawnZ", payload.z);
            ((IEntityDataSaver)entity1).getPersistentData().putFloat("spawnYaw", payload.yaw);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
