package net.zephyr.fnafur.networking.block;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;
import net.zephyr.fnafur.blocks.props.base.PropBlock;

public record UpdatePropAltC2SPayload(long pos, int alt) implements CustomPayload {
    public static final Id<UpdatePropAltC2SPayload> ID = new Id<>(BlockPayloads.C2SUpdatePropAlt);
    public static final PacketCodec<RegistryByteBuf, UpdatePropAltC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.LONG, UpdatePropAltC2SPayload::pos,
            PacketCodecs.INTEGER, UpdatePropAltC2SPayload::alt,
            UpdatePropAltC2SPayload::new);

    public static<T extends Enum<T> & ColorEnumInterface & StringIdentifiable> void receive(UpdatePropAltC2SPayload payload, ServerPlayNetworking.Context context) {

        BlockState state = context.player().getEntityWorld().getBlockState(BlockPos.fromLong(payload.pos()));
        if(state.getBlock() instanceof PropBlock<?>) {
            PropBlock<T> block = (PropBlock<T>) state.getBlock();
            if(state.contains(block.COLOR_PROPERTY())){
                context.player().getEntityWorld().setBlockState(BlockPos.fromLong(payload.pos()), state.with(block.COLOR_PROPERTY(), block.COLOR_PROPERTY().getValues().get(payload.alt())));
                context.player().getEntityWorld().getBlockState(BlockPos.fromLong(payload.pos())).onStateReplaced(context.player().getEntityWorld(), BlockPos.fromLong(payload.pos()), false);
            }
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
