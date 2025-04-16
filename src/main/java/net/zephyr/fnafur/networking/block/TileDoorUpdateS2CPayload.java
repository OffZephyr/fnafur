package net.zephyr.fnafur.networking.block;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.blocks.props.tiling.VerticalTileStates;
import net.zephyr.fnafur.blocks.props.tiling.tile_doors.TileDoorBlock;

public record TileDoorUpdateS2CPayload(long pos, boolean open) implements CustomPayload {
    public static final Id<TileDoorUpdateS2CPayload> ID = new Id<>(BlockPayloads.S2CTileDoorOpenUpdate);

    public static final PacketCodec<RegistryByteBuf, TileDoorUpdateS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.LONG, TileDoorUpdateS2CPayload::pos,
            PacketCodecs.BOOLEAN, TileDoorUpdateS2CPayload::open,
            TileDoorUpdateS2CPayload::new);

    public static void receive(TileDoorUpdateS2CPayload payload, ClientPlayNetworking.Context context) {

        BlockPos pos = BlockPos.fromLong(payload.pos);
        BlockState state = context.client().world.getBlockState(pos);

        boolean connectUp = context.client().world.getBlockState(pos.up()).isOf(state.getBlock());
        boolean connectRight = context.client().world.getBlockState(pos.offset(state.get(TileDoorBlock.FACING).rotateYClockwise())).isOf(state.getBlock());
        boolean connectDown = context.client().world.getBlockState(pos.down()).isOf(state.getBlock());
        boolean connectLeft = context.client().world.getBlockState(pos.offset(state.get(TileDoorBlock.FACING).rotateYCounterclockwise())).isOf(state.getBlock());

        context.client().world.setBlockState(pos, state.with(TileDoorBlock.OPEN, payload.open()).with(TileDoorBlock.TYPE, VerticalTileStates.get(connectUp, connectRight, connectDown, connectLeft)), 0);
    }
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
