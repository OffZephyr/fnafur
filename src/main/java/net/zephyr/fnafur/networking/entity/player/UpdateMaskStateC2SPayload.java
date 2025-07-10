package net.zephyr.fnafur.networking.entity.player;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import net.zephyr.fnafur.item.masks.VanniMaskItem;
import net.zephyr.fnafur.networking.entity.EntityPayloads;
import net.zephyr.fnafur.util.ItemNbtUtil;

public record UpdateMaskStateC2SPayload(boolean state) implements CustomPayload {
    public static final Id<UpdateMaskStateC2SPayload> ID = new Id<>(EntityPayloads.C2SMaskStateUpdate);
    public static final PacketCodec<RegistryByteBuf, UpdateMaskStateC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOLEAN, UpdateMaskStateC2SPayload::state,
            UpdateMaskStateC2SPayload::new);
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void receive(UpdateMaskStateC2SPayload payload, ServerPlayNetworking.Context context) {
        ItemStack stack = context.player().getInventory().getStack(FnafInventoryScreen.SLOTS_OFFSET);
        if(stack.getItem() instanceof VanniMaskItem){
            NbtCompound nbt = ItemNbtUtil.getNbt(stack);
            nbt.putBoolean("inVanniMask", payload.state());
            ItemNbtUtil.setNbt(stack, nbt);
        }

        for(ServerPlayerEntity p : PlayerLookup.all(context.server())){
            ServerPlayNetworking.send(p, new UpdateMaskStateS2CPayload(payload.state(), context.player().getId()));
        }
    }
}
