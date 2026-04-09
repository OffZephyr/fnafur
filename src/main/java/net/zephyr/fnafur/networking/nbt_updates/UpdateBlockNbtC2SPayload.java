package net.zephyr.fnafur.networking.nbt_updates;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record UpdateBlockNbtC2SPayload(long pos, CompoundTag data) implements CustomPacketPayload {

    public static final Type<UpdateBlockNbtC2SPayload> ID = new Type<>(NbtPayloads.C2SBlockUpdate);

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateBlockNbtC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, UpdateBlockNbtC2SPayload::pos,
            ByteBufCodecs.COMPOUND_TAG, UpdateBlockNbtC2SPayload::data,
            UpdateBlockNbtC2SPayload::new);

    public static void receive(UpdateBlockNbtC2SPayload payload, ServerPlayNetworking.Context context) {
        BlockEntity entity = context.player().level().getBlockEntity(BlockPos.of(payload.pos()));
        context.player().level().setBlockAndUpdate(BlockPos.of(payload.pos), context.player().level().getBlockState(BlockPos.of(payload.pos())));
        if (entity == null) return;
        ((IEntityDataSaver) entity).setPersistentData(payload.data());
        entity.setChanged();
        for (ServerPlayer p : PlayerLookup.all(context.server())) {
            //p.sendMessage(Text.literal("§6" + "SYNC SERVER"), false);
            ServerPlayNetworking.send(p, new UpdateBlockNbtS2CPongPayload(payload.pos(), payload.data()));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }
}
