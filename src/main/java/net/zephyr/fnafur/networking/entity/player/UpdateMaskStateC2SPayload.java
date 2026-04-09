package net.zephyr.fnafur.networking.entity.player;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import net.zephyr.fnafur.item.masks.VanniMaskItem;
import net.zephyr.fnafur.networking.entity.EntityPayloads;
import net.zephyr.fnafur.util.ItemUtil;

public record UpdateMaskStateC2SPayload(boolean state) implements CustomPacketPayload {
    public static final Type<UpdateMaskStateC2SPayload> ID = new Type<>(EntityPayloads.C2SMaskStateUpdate);
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateMaskStateC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, UpdateMaskStateC2SPayload::state,
            UpdateMaskStateC2SPayload::new);
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    public static void receive(UpdateMaskStateC2SPayload payload, ServerPlayNetworking.Context context) {
        ItemStack stack = context.player().getInventory().getItem(FnafInventoryScreen.SLOTS_OFFSET);
        if(stack.getItem() instanceof VanniMaskItem){
            CompoundTag nbt = ItemUtil.getNbt(stack);
            nbt.putBoolean("inVanniMask", payload.state());
            ItemUtil.setNbt(stack, nbt);
        }

        for(ServerPlayer p : PlayerLookup.all(context.server())){
            ServerPlayNetworking.send(p, new UpdateMaskStateS2CPayload(payload.state(), context.player().getId()));
        }
    }
}
