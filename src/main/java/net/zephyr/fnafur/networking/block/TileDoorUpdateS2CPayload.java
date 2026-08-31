package net.zephyr.fnafur.networking.block;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.core.BlockPos;
import net.zephyr.fnafur.blocks.dynamic.tiling.VerticalTileStates;
import net.zephyr.fnafur.blocks.dynamic.tiling.tile_doors.TileDoorBlock;

public record TileDoorUpdateS2CPayload(long pos, boolean open) implements CustomPacketPayload {
    public static final Type<TileDoorUpdateS2CPayload> ID = new Type<>(BlockPayloads.S2CTileDoorOpenUpdate);

    public static final StreamCodec<RegistryFriendlyByteBuf, TileDoorUpdateS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.LONG, TileDoorUpdateS2CPayload::pos,
            ByteBufCodecs.BOOL, TileDoorUpdateS2CPayload::open,
            TileDoorUpdateS2CPayload::new);

    public static void receive(TileDoorUpdateS2CPayload payload, ClientPlayNetworking.Context context) {

        BlockPos pos = BlockPos.of(payload.pos);
        BlockState state = context.client().level.getBlockState(pos);

        boolean connectUp = context.client().level.getBlockState(pos.above()).is(state.getBlock());
        boolean connectRight = context.client().level.getBlockState(pos.relative(state.getValue(TileDoorBlock.FACING).getClockWise())).is(state.getBlock());
        boolean connectDown = context.client().level.getBlockState(pos.below()).is(state.getBlock());
        boolean connectLeft = context.client().level.getBlockState(pos.relative(state.getValue(TileDoorBlock.FACING).getCounterClockWise())).is(state.getBlock());

        context.client().level.setBlock(pos, state.setValue(TileDoorBlock.OPEN, payload.open()).setValue(TileDoorBlock.TYPE, VerticalTileStates.get(connectUp, connectRight, connectDown, connectLeft)), 0);
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
