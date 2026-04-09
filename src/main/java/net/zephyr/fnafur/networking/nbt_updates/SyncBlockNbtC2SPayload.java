package net.zephyr.fnafur.networking.nbt_updates;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.gameevent.GameEvent;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record SyncBlockNbtC2SPayload(long pos) implements CustomPacketPayload {

    public static final Type<SyncBlockNbtC2SPayload> ID = new Type<>(NbtPayloads.C2SBlockSync);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncBlockNbtC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, SyncBlockNbtC2SPayload::pos,
            SyncBlockNbtC2SPayload::new);

    public static void receive(SyncBlockNbtC2SPayload payload, ServerPlayNetworking.Context context) {
       BlockEntity entity = context.player().level().getBlockEntity(BlockPos.of(payload.pos()));

       if(entity == null || ((IEntityDataSaver)entity).getPersistentData().isEmpty()) return;

        BlockState state = context.player().level().getBlockState(BlockPos.of(payload.pos()));
        context.player().level().sendBlockUpdated(BlockPos.of(payload.pos()), state, state, 3);

        context.player().level().setBlockAndUpdate(BlockPos.of(payload.pos()), context.player().level().getBlockState(BlockPos.of(payload.pos())));
        context.player().level().gameEvent(GameEvent.BLOCK_CHANGE, BlockPos.of(payload.pos()), GameEvent.Context.of(context.player(), context.player().level().getBlockState(BlockPos.of(payload.pos()))));
        if(entity != null) {
            for (ServerPlayer p : PlayerLookup.all(context.server())) {
                ServerPlayNetworking.send(p, new UpdateBlockNbtS2CPongPayload(payload.pos(), ((IEntityDataSaver) entity).getPersistentData()));
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }
}
