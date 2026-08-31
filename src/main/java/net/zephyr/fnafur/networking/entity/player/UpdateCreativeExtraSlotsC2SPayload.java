package net.zephyr.fnafur.networking.entity.player;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import net.zephyr.fnafur.networking.entity.EntityPayloads;

public record UpdateCreativeExtraSlotsC2SPayload(CompoundTag ItemData) implements CustomPacketPayload {
    public static final Type<UpdateCreativeExtraSlotsC2SPayload> ID = new Type<>(EntityPayloads.C2SExtraSlotUpdate);
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateCreativeExtraSlotsC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, UpdateCreativeExtraSlotsC2SPayload::ItemData,
            UpdateCreativeExtraSlotsC2SPayload::new);
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    public static void receive(UpdateCreativeExtraSlotsC2SPayload payload, ServerPlayNetworking.Context context) {
        ItemStack stack1 = ItemStack.EMPTY;
        ItemStack stack2 = ItemStack.EMPTY;

        if(payload.ItemData.getBooleanOr("hasStack1", false)){
            stack1 = payload.ItemData.read("stack1", ItemStack.CODEC).orElse(ItemStack.EMPTY);
        }
        if(payload.ItemData.getBooleanOr("hasStack2", false)){
            stack1 = payload.ItemData.read("stack2", ItemStack.CODEC).orElse(ItemStack.EMPTY);
        }

        context.player().getInventory().setItem(FnafInventoryScreen.SLOTS_OFFSET, stack1);
        context.player().getInventory().setItem(FnafInventoryScreen.SLOTS_OFFSET + 1, stack2);
    }
}
