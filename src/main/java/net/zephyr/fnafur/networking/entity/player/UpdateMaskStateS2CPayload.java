package net.zephyr.fnafur.networking.entity.player;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import net.zephyr.fnafur.item.masks.VanniMaskItem;
import net.zephyr.fnafur.networking.entity.EntityPayloads;
import net.zephyr.fnafur.util.ItemUtil;

public record UpdateMaskStateS2CPayload(boolean state, int entityID) implements CustomPacketPayload {
    public static final Type<UpdateMaskStateS2CPayload> ID = new Type<>(EntityPayloads.S2CMaskStateUpdate);

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateMaskStateS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, UpdateMaskStateS2CPayload::state,
            ByteBufCodecs.INT, UpdateMaskStateS2CPayload::entityID,
            UpdateMaskStateS2CPayload::new);

    public static void receive(UpdateMaskStateS2CPayload payload, ClientPlayNetworking.Context context) {
        if(context.player().level().getEntity(payload.entityID()) instanceof Player p) {
            ItemStack stack = p.getInventory().getItem(FnafInventoryScreen.SLOTS_OFFSET);
            if (stack.getItem() instanceof VanniMaskItem) {
                CompoundTag nbt = ItemUtil.getNbt(stack);
                nbt.putBoolean("inVanniMask", payload.state());
                ItemUtil.setNbt(stack, nbt);
            }
        }
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
