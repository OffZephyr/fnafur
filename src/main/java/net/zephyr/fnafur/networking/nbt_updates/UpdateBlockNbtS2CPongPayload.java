package net.zephyr.fnafur.networking.nbt_updates;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record UpdateBlockNbtS2CPongPayload(long pos, NbtCompound data) implements CustomPayload {

    public static final Id<UpdateBlockNbtS2CPongPayload> ID = new Id<>(NbtPayloads.S2CBlockUpdatePong);

    public static final PacketCodec<RegistryByteBuf, UpdateBlockNbtS2CPongPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_LONG, UpdateBlockNbtS2CPongPayload::pos,
            PacketCodecs.NBT_COMPOUND, UpdateBlockNbtS2CPongPayload::data,
            UpdateBlockNbtS2CPongPayload::new);

    public static void receive(UpdateBlockNbtS2CPongPayload payload, ClientPlayNetworking.Context context) {
        BlockEntity entity = context.player().getWorld().getBlockEntity(BlockPos.fromLong(payload.pos()));
        context.player().getWorld().setBlockState(BlockPos.fromLong(payload.pos), context.player().getWorld().getBlockState(BlockPos.fromLong(payload.pos())));
        if (entity == null) return;
        ((IEntityDataSaver) entity).setServerUpdateStatus(false);
        ((IEntityDataSaver) entity).getPersistentData().copyFrom(payload.data());
        entity.markDirty();
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
