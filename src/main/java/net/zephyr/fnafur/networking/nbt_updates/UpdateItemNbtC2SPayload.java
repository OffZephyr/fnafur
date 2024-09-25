package net.zephyr.fnafur.networking.nbt_updates;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record UpdateItemNbtC2SPayload(String slot, NbtCompound data) implements CustomPayload {

    public static final Id<UpdateItemNbtC2SPayload> ID = new Id<>(NbtPayloads.C2SItemUpdate);

    public static final PacketCodec<RegistryByteBuf, UpdateItemNbtC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, UpdateItemNbtC2SPayload::slot,
            PacketCodecs.NBT_COMPOUND, UpdateItemNbtC2SPayload::data,
            UpdateItemNbtC2SPayload::new);

    public static void receive(UpdateItemNbtC2SPayload payload, ServerPlayNetworking.Context context) {
        ItemStack stack = context.player().getEquippedStack(EquipmentSlot.byName(payload.slot()));
        ItemNbtUtil.setNbt(stack, payload.data());
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
