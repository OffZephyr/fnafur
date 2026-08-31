package net.zephyr.fnafur.networking.nbt_updates;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record UpdateEntityNbtC2SGetFromServerPayload(int entityID) implements CustomPacketPayload {

    public static final Type<UpdateEntityNbtC2SGetFromServerPayload> ID = new Type<>(NbtPayloads.C2SEntityUpdateServer);

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateEntityNbtC2SGetFromServerPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, UpdateEntityNbtC2SGetFromServerPayload::entityID,
            UpdateEntityNbtC2SGetFromServerPayload::new);

    public static void receive(UpdateEntityNbtC2SGetFromServerPayload payload, ServerPlayNetworking.Context context) {
        Entity entity = context.player().level().getEntity(payload.entityID());
        if(entity != null) {

            for(ServerPlayer p : PlayerLookup.all(context.server())){
                ServerPlayNetworking.send(p, new UpdateEntityNbtS2CPongPayload(payload.entityID(), ((IEntityDataSaver) entity).getPersistentData()));
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }
}
