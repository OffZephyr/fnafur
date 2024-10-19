package net.zephyr.fnafur.networking.nbt_updates;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record UpdateBlockNbtS2CGetFromClientPayload(long pos) implements CustomPayload {

    public static final Id<UpdateBlockNbtS2CGetFromClientPayload> ID = new Id<>(NbtPayloads.S2CBlockUpdateServer);

    public static final PacketCodec<RegistryByteBuf, UpdateBlockNbtS2CGetFromClientPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_LONG, UpdateBlockNbtS2CGetFromClientPayload::pos,
            UpdateBlockNbtS2CGetFromClientPayload::new);

    public static void receive(UpdateBlockNbtS2CGetFromClientPayload payload, ClientPlayNetworking.Context context) {
        BlockEntity entity = context.player().getWorld().getBlockEntity(BlockPos.fromLong(payload.pos()));
        if(entity == null) return;
        NbtCompound data = ((IEntityDataSaver)entity).getPersistentData().copy();
        GoopyNetworkingUtils.saveBlockNbt(BlockPos.fromLong(payload.pos()), data);
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
