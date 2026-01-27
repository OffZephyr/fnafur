package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record SetEntitySpawnDataS2CPayload(int EntityID, double x, double y, double z, float yaw) implements CustomPayload {
    public static final Id<SetEntitySpawnDataS2CPayload> ID = new Id<>(EntityPayloads.S2CSetSpawnData);
    public static final PacketCodec<RegistryByteBuf, SetEntitySpawnDataS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, SetEntitySpawnDataS2CPayload::EntityID,
            PacketCodecs.DOUBLE, SetEntitySpawnDataS2CPayload::x,
            PacketCodecs.DOUBLE, SetEntitySpawnDataS2CPayload::y,
            PacketCodecs.DOUBLE, SetEntitySpawnDataS2CPayload::z,
            PacketCodecs.FLOAT, SetEntitySpawnDataS2CPayload::yaw,
            SetEntitySpawnDataS2CPayload::new);

    public static void receive(SetEntitySpawnDataS2CPayload payload, ClientPlayNetworking.Context context) {
        Entity entity = context.player().getEntityWorld().getEntityById(payload.EntityID);
        if(entity instanceof AnimatronicEntity entity1){
            ((IEntityDataSaver)entity1).getPersistentData().putDouble("spawnX", payload.x);
            ((IEntityDataSaver)entity1).getPersistentData().putDouble("spawnY", payload.y);
            ((IEntityDataSaver)entity1).getPersistentData().putDouble("spawnZ", payload.z);
            ((IEntityDataSaver)entity1).getPersistentData().putFloat("spawnYaw", payload.yaw);
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
