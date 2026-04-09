package net.zephyr.fnafur.networking.entity.player;


import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zephyr.fnafur.networking.entity.EntityPayloads;

public record UpdateMainHandItemC2SPayload(CompoundTag ItemData) implements CustomPacketPayload {
    public static final Type<UpdateMainHandItemC2SPayload> ID = new Type<>(EntityPayloads.C2SMainHandUpdate);
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateMainHandItemC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, UpdateMainHandItemC2SPayload::ItemData,
            UpdateMainHandItemC2SPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    public static void receive(UpdateMainHandItemC2SPayload payload, ServerPlayNetworking.Context context) {
        ItemStack stack = ItemStack.EMPTY;

        stack = payload.ItemData.read("stack", ItemStack.CODEC).orElse(ItemStack.EMPTY);

        context.player().setItemInHand(context.player().swingingArm, stack);
    }
}
