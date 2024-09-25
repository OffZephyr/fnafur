package net.zephyr.fnafur.networking.nbt_updates;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.networking.nbt_updates.goopy_entity.UpdateEntityNbtS2CPongPayload;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record SyncEntityNbtC2SPayload(int entityid) implements CustomPayload {

    public static final Id<SyncEntityNbtC2SPayload> ID = new Id<>(NbtPayloads.C2SEntitySync);

    public static final PacketCodec<RegistryByteBuf, SyncEntityNbtC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, SyncEntityNbtC2SPayload::entityid,
            SyncEntityNbtC2SPayload::new);

    public static void receive(SyncEntityNbtC2SPayload payload, ServerPlayNetworking.Context context) {
        Entity entity = context.player().getWorld().getEntityById(payload.entityid());

        for(ServerPlayerEntity p : PlayerLookup.all(context.server())){
            ServerPlayNetworking.send(p, new UpdateEntityNbtS2CPongPayload(payload.entityid(), ((IEntityDataSaver)entity).getPersistentData()));
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
