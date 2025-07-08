package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;

public record UpdateCreativeExtraSlotsC2SPayload(NbtCompound ItemData) implements CustomPayload {
    public static final Id<UpdateCreativeExtraSlotsC2SPayload> ID = new Id<>(EntityPayloads.C2SExtraSlotUpdate);
    public static final PacketCodec<RegistryByteBuf, UpdateCreativeExtraSlotsC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.NBT_COMPOUND, UpdateCreativeExtraSlotsC2SPayload::ItemData,
            UpdateCreativeExtraSlotsC2SPayload::new);
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void receive(UpdateCreativeExtraSlotsC2SPayload payload, ServerPlayNetworking.Context context) {
        ItemStack stack1 = ItemStack.EMPTY;
        ItemStack stack2 = ItemStack.EMPTY;

        if(payload.ItemData.getBoolean("hasStack1", false)){
            stack1 = payload.ItemData.get("stack1", ItemStack.CODEC).orElse(ItemStack.EMPTY);
        }
        if(payload.ItemData.getBoolean("hasStack2", false)){
            stack1 = payload.ItemData.get("stack2", ItemStack.CODEC).orElse(ItemStack.EMPTY);
        }

        context.player().getInventory().setStack(FnafInventoryScreen.SLOTS_OFFSET, stack1);
        context.player().getInventory().setStack(FnafInventoryScreen.SLOTS_OFFSET + 1, stack2);
    }
}
