package net.zephyr.fnafur.networking.nbt_updates;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record UpdateEntityNbtC2SGetFromServerPayload(int entityID) implements CustomPayload {

    public static final Id<UpdateEntityNbtC2SGetFromServerPayload> ID = new Id<>(NbtPayloads.C2SEntityUpdateServer);

    public static final PacketCodec<RegistryByteBuf, UpdateEntityNbtC2SGetFromServerPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, UpdateEntityNbtC2SGetFromServerPayload::entityID,
            UpdateEntityNbtC2SGetFromServerPayload::new);

    public static void receive(UpdateEntityNbtC2SGetFromServerPayload payload, ServerPlayNetworking.Context context) {
        Entity entity = context.player().getEntityWorld().getEntityById(payload.entityID());
        if(entity != null) {

            for(ServerPlayerEntity p : PlayerLookup.all(context.server())){
                ServerPlayNetworking.send(p, new UpdateEntityNbtS2CPongPayload(payload.entityID(), ((IEntityDataSaver) entity).getPersistentData()));
            }
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
