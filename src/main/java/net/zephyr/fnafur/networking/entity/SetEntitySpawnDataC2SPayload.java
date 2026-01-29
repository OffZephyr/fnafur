package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record SetEntitySpawnDataC2SPayload(int EntityID, double x, double y, double z, float yaw) implements CustomPayload {
    public static final Id<SetEntitySpawnDataC2SPayload> ID = new Id<>(EntityPayloads.C2SSetSpawnData);
    public static final PacketCodec<RegistryByteBuf, SetEntitySpawnDataC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, SetEntitySpawnDataC2SPayload::EntityID,
            PacketCodecs.DOUBLE, SetEntitySpawnDataC2SPayload::x,
            PacketCodecs.DOUBLE, SetEntitySpawnDataC2SPayload::y,
            PacketCodecs.DOUBLE, SetEntitySpawnDataC2SPayload::z,
            PacketCodecs.FLOAT, SetEntitySpawnDataC2SPayload::yaw,
            SetEntitySpawnDataC2SPayload::new);

    public static void receive(SetEntitySpawnDataC2SPayload payload, ServerPlayNetworking.Context context) {
        Entity entity = context.player().getEntityWorld().getEntityById(payload.EntityID);
        System.out.println("RECEIVED");
        if(entity instanceof AnimatronicEntity entity1){
            if(!((IEntityDataSaver)entity1).getPersistentData().contains("spawnX")) {
                ((IEntityDataSaver) entity1).getPersistentData().putDouble("spawnX", payload.x);
                ((IEntityDataSaver) entity1).getPersistentData().putDouble("spawnY", payload.y);
                ((IEntityDataSaver) entity1).getPersistentData().putDouble("spawnZ", payload.z);
                ((IEntityDataSaver) entity1).getPersistentData().putFloat("spawnYaw", payload.yaw);
                System.out.println("SERVER DATA");

                for (ServerPlayerEntity p : PlayerLookup.all(context.server())) {
                    ServerPlayNetworking.send(p, new SetEntitySpawnDataS2CPayload(entity1.getId(), payload.x, payload.y, payload.z, payload.yaw));
                }
            }
            else{
                System.out.println("ALREADY HAS DATA");
            }
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
