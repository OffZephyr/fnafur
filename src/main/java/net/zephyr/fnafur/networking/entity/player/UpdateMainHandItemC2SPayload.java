package net.zephyr.fnafur.networking.entity.player;


import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.fnafur.networking.entity.EntityPayloads;

public record UpdateMainHandItemC2SPayload(NbtCompound ItemData) implements CustomPayload {
    public static final Id<UpdateMainHandItemC2SPayload> ID = new Id<>(EntityPayloads.C2SMainHandUpdate);
    public static final PacketCodec<RegistryByteBuf, UpdateMainHandItemC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.NBT_COMPOUND, UpdateMainHandItemC2SPayload::ItemData,
            UpdateMainHandItemC2SPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void receive(UpdateMainHandItemC2SPayload payload, ServerPlayNetworking.Context context) {
        ItemStack stack = ItemStack.EMPTY;

        stack = payload.ItemData.get("stack", ItemStack.CODEC).orElse(ItemStack.EMPTY);

        context.player().setStackInHand(context.player().preferredHand, stack);
    }
}
