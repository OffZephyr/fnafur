package net.zephyr.fnafur.networking.nbt_updates;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.zephyr.fnafur.networking.nbt_updates.goopy_entity.UpdateEntityNbtS2CPongPayload;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record UpdateEntityNbtC2SPayload(int entityID, NbtCompound data) implements CustomPayload {

    public static final Id<UpdateEntityNbtC2SPayload> ID = new Id<>(NbtPayloads.C2SEntityUpdate);

    public static final PacketCodec<RegistryByteBuf, UpdateEntityNbtC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, UpdateEntityNbtC2SPayload::entityID,
            PacketCodecs.NBT_COMPOUND, UpdateEntityNbtC2SPayload::data,
            UpdateEntityNbtC2SPayload::new);

    public static void receive(UpdateEntityNbtC2SPayload payload, ServerPlayNetworking.Context context) {
        Entity entity = context.player().getWorld().getEntityById(payload.entityID());
        if(entity != null) {
            ((IEntityDataSaver) entity).getPersistentData().copyFrom(payload.data());

            for(ServerPlayerEntity p : PlayerLookup.all(context.server())){
                ServerPlayNetworking.send(p, new UpdateEntityNbtS2CPongPayload(payload.entityID(), payload.data()));
            }
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
