package net.zephyr.fnafur.networking.nbt_updates;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record UpdateBlockNbtC2SPayload(long pos, NbtCompound data) implements CustomPayload {

    public static final Id<UpdateBlockNbtC2SPayload> ID = new Id<>(NbtPayloads.C2SBlockUpdate);

    public static final PacketCodec<RegistryByteBuf, UpdateBlockNbtC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_LONG, UpdateBlockNbtC2SPayload::pos,
            PacketCodecs.NBT_COMPOUND, UpdateBlockNbtC2SPayload::data,
            UpdateBlockNbtC2SPayload::new);

    public static void receive(UpdateBlockNbtC2SPayload payload, ServerPlayNetworking.Context context) {
       BlockEntity entity = context.player().getWorld().getBlockEntity(BlockPos.fromLong(payload.pos()));
       context.player().getWorld().setBlockState(BlockPos.fromLong(payload.pos), context.player().getWorld().getBlockState(BlockPos.fromLong(payload.pos())));
        ((IEntityDataSaver)entity).getPersistentData().copyFrom(payload.data());
        entity.markDirty();
        for(ServerPlayerEntity p : PlayerLookup.all(context.server())){
            ServerPlayNetworking.send(p, new UpdateBlockNbtS2CPongPayload(payload.pos(), payload.data()));
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
