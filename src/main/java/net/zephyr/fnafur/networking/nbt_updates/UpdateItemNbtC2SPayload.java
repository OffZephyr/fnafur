package net.zephyr.fnafur.networking.nbt_updates;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zephyr.fnafur.util.ItemUtil;

public record UpdateItemNbtC2SPayload(String slot, CompoundTag data) implements CustomPacketPayload {

    public static final Type<UpdateItemNbtC2SPayload> ID = new Type<>(NbtPayloads.C2SItemUpdate);

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateItemNbtC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, UpdateItemNbtC2SPayload::slot,
            ByteBufCodecs.COMPOUND_TAG, UpdateItemNbtC2SPayload::data,
            UpdateItemNbtC2SPayload::new);

    public static void receive(UpdateItemNbtC2SPayload payload, ServerPlayNetworking.Context context) {
        ItemStack stack = context.player().getItemBySlot(EquipmentSlot.byName(payload.slot()));
        ItemUtil.setNbt(stack, payload.data());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }
}
