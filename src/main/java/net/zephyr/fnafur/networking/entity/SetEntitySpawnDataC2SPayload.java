package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record SetEntitySpawnDataC2SPayload(int EntityID, double x, double y, double z, float yaw) implements CustomPacketPayload {
    public static final Type<SetEntitySpawnDataC2SPayload> ID = new Type<>(EntityPayloads.C2SSetSpawnData);
    public static final StreamCodec<RegistryFriendlyByteBuf, SetEntitySpawnDataC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SetEntitySpawnDataC2SPayload::EntityID,
            ByteBufCodecs.DOUBLE, SetEntitySpawnDataC2SPayload::x,
            ByteBufCodecs.DOUBLE, SetEntitySpawnDataC2SPayload::y,
            ByteBufCodecs.DOUBLE, SetEntitySpawnDataC2SPayload::z,
            ByteBufCodecs.FLOAT, SetEntitySpawnDataC2SPayload::yaw,
            SetEntitySpawnDataC2SPayload::new);

    public static void receive(SetEntitySpawnDataC2SPayload payload, ServerPlayNetworking.Context context) {
        Entity entity = context.player().level().getEntity(payload.EntityID);
        System.out.println("RECEIVED");
        if(entity instanceof AnimatronicEntity entity1){
            if(!((IEntityDataSaver)entity1).getPersistentData().contains("spawnX")) {
                ((IEntityDataSaver) entity1).getPersistentData().putDouble("spawnX", payload.x);
                ((IEntityDataSaver) entity1).getPersistentData().putDouble("spawnY", payload.y);
                ((IEntityDataSaver) entity1).getPersistentData().putDouble("spawnZ", payload.z);
                ((IEntityDataSaver) entity1).getPersistentData().putFloat("spawnYaw", payload.yaw);
                System.out.println("SERVER DATA");

                for (ServerPlayer p : PlayerLookup.all(context.server())) {
                    ServerPlayNetworking.send(p, new SetEntitySpawnDataS2CPayload(entity1.getId(), payload.x, payload.y, payload.z, payload.yaw));
                }
            }
            else{
                System.out.println("ALREADY HAS DATA");
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
