package net.zephyr.fnafur.networking.nbt_updates;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.GameEvent;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record SyncBlockNbtC2SPayload(long pos) implements CustomPayload {

    public static final Id<SyncBlockNbtC2SPayload> ID = new Id<>(NbtPayloads.C2SBlockSync);

    public static final PacketCodec<RegistryByteBuf, SyncBlockNbtC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_LONG, SyncBlockNbtC2SPayload::pos,
            SyncBlockNbtC2SPayload::new);

    public static void receive(SyncBlockNbtC2SPayload payload, ServerPlayNetworking.Context context) {
       BlockEntity entity = context.player().getWorld().getBlockEntity(BlockPos.fromLong(payload.pos()));

       if(entity == null || ((IEntityDataSaver)entity).getPersistentData().isEmpty()) return;

        BlockState state = context.player().getWorld().getBlockState(BlockPos.fromLong(payload.pos()));
        context.player().getWorld().updateListeners(BlockPos.fromLong(payload.pos()), state, state, 3);

        context.player().getWorld().setBlockState(BlockPos.fromLong(payload.pos()), context.player().getWorld().getBlockState(BlockPos.fromLong(payload.pos())));
        context.player().getWorld().emitGameEvent(GameEvent.BLOCK_CHANGE, BlockPos.fromLong(payload.pos()), GameEvent.Emitter.of(context.player(), context.player().getWorld().getBlockState(BlockPos.fromLong(payload.pos()))));
        if(entity != null) {
            for (ServerPlayerEntity p : PlayerLookup.all(context.server())) {
                ServerPlayNetworking.send(p, new UpdateBlockNbtS2CPongPayload(payload.pos(), ((IEntityDataSaver) entity).getPersistentData()));
            }
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
