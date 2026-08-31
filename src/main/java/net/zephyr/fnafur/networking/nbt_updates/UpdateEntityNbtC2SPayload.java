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

public record UpdateEntityNbtC2SPayload(int entityID, CompoundTag data) implements CustomPacketPayload {

    public static final Type<UpdateEntityNbtC2SPayload> ID = new Type<>(NbtPayloads.C2SEntityUpdate);

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateEntityNbtC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, UpdateEntityNbtC2SPayload::entityID,
            ByteBufCodecs.COMPOUND_TAG, UpdateEntityNbtC2SPayload::data,
            UpdateEntityNbtC2SPayload::new);

    public static void receive(UpdateEntityNbtC2SPayload payload, ServerPlayNetworking.Context context) {
        Entity entity = context.player().level().getEntity(payload.entityID());
        if(entity != null) {
            ((IEntityDataSaver) entity).getPersistentData().merge(payload.data());

            for(ServerPlayer p : PlayerLookup.all(context.server())){
                ServerPlayNetworking.send(p, new UpdateEntityNbtS2CPongPayload(payload.entityID(), payload.data()));
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }
}
