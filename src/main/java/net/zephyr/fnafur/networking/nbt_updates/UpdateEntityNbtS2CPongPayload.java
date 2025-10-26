package net.zephyr.fnafur.networking.nbt_updates;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record UpdateEntityNbtS2CPongPayload(int entityID, NbtCompound data) implements CustomPayload {

    public static final Id<UpdateEntityNbtS2CPongPayload> ID = new Id<>(NbtPayloads.S2CEntityUpdatePong);

    public static final PacketCodec<RegistryByteBuf, UpdateEntityNbtS2CPongPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, UpdateEntityNbtS2CPongPayload::entityID,
            PacketCodecs.NBT_COMPOUND, UpdateEntityNbtS2CPongPayload::data,
            UpdateEntityNbtS2CPongPayload::new);

    public static void receive(UpdateEntityNbtS2CPongPayload payload, ClientPlayNetworking.Context context) {
        Entity entity = context.player().getEntityWorld().getEntityById(payload.entityID());
        if(entity != null) {
            ((IEntityDataSaver) entity).getPersistentData().copyFrom(payload.data());
            ((IEntityDataSaver) entity).getPersistentData().putBoolean("synced", true);
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
