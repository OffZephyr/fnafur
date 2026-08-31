package net.zephyr.fnafur.networking.nbt_updates;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.core.BlockPos;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record UpdateBlockNbtS2CGetFromClientPayload(long pos) implements CustomPacketPayload {

    public static final Type<UpdateBlockNbtS2CGetFromClientPayload> ID = new Type<>(NbtPayloads.S2CBlockUpdateServer);

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateBlockNbtS2CGetFromClientPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, UpdateBlockNbtS2CGetFromClientPayload::pos,
            UpdateBlockNbtS2CGetFromClientPayload::new);

    public static void receive(UpdateBlockNbtS2CGetFromClientPayload payload, ClientPlayNetworking.Context context) {
        BlockEntity entity = context.player().level().getBlockEntity(BlockPos.of(payload.pos()));
        if(entity == null) return;
        CompoundTag data = ((IEntityDataSaver)entity).getPersistentData().copy();
        GoopyNetworkingUtils.saveBlockNbt(BlockPos.of(payload.pos()), data);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }
}
