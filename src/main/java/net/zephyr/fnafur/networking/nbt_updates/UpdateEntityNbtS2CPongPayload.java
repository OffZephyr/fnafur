package net.zephyr.fnafur.networking.nbt_updates;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record UpdateEntityNbtS2CPongPayload(int entityID, CompoundTag data) implements CustomPacketPayload {

    public static final Type<UpdateEntityNbtS2CPongPayload> ID = new Type<>(NbtPayloads.S2CEntityUpdatePong);

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateEntityNbtS2CPongPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, UpdateEntityNbtS2CPongPayload::entityID,
            ByteBufCodecs.COMPOUND_TAG, UpdateEntityNbtS2CPongPayload::data,
            UpdateEntityNbtS2CPongPayload::new);

    public static void receive(UpdateEntityNbtS2CPongPayload payload, ClientPlayNetworking.Context context) {
        Entity entity = context.player().level().getEntity(payload.entityID());
        if(entity != null) {
            ((IEntityDataSaver) entity).getPersistentData().merge(payload.data());
            ((IEntityDataSaver) entity).getPersistentData().putBoolean("synced", true);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }
}
