package net.zephyr.fnafur.networking.entity.player;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.cosmo_gift.GalaxyLayerGeoPropEntity;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import net.zephyr.fnafur.item.masks.VanniMaskItem;
import net.zephyr.fnafur.networking.entity.EntityPayloads;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record UpdateMaskStateS2CPayload(boolean state, int entityID) implements CustomPayload {
    public static final Id<UpdateMaskStateS2CPayload> ID = new Id<>(EntityPayloads.S2CMaskStateUpdate);

    public static final PacketCodec<RegistryByteBuf, UpdateMaskStateS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOLEAN, UpdateMaskStateS2CPayload::state,
            PacketCodecs.INTEGER, UpdateMaskStateS2CPayload::entityID,
            UpdateMaskStateS2CPayload::new);

    public static void receive(UpdateMaskStateS2CPayload payload, ClientPlayNetworking.Context context) {
        if(context.player().getEntityWorld().getEntityById(payload.entityID()) instanceof PlayerEntity p) {
            ItemStack stack = p.getInventory().getStack(FnafInventoryScreen.SLOTS_OFFSET);
            if (stack.getItem() instanceof VanniMaskItem) {
                NbtCompound nbt = ItemNbtUtil.getNbt(stack);
                nbt.putBoolean("inVanniMask", payload.state());
                ItemNbtUtil.setNbt(stack, nbt);
            }
        }
    }
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
