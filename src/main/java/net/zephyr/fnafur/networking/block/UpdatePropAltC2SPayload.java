package net.zephyr.fnafur.networking.block;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.StringRepresentable;
import net.minecraft.core.BlockPos;
import net.zephyr.fnafur.blocks.light.Sconce;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;
import net.zephyr.fnafur.blocks.props.base.PropBlock;

public record UpdatePropAltC2SPayload(long pos, int alt) implements CustomPacketPayload {
    public static final Type<UpdatePropAltC2SPayload> ID = new Type<>(BlockPayloads.C2SUpdatePropAlt);
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdatePropAltC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.LONG, UpdatePropAltC2SPayload::pos,
            ByteBufCodecs.INT, UpdatePropAltC2SPayload::alt,
            UpdatePropAltC2SPayload::new);

    public static<T extends Enum<T> & ColorEnumInterface & StringRepresentable> void receive(UpdatePropAltC2SPayload payload, ServerPlayNetworking.Context context) {

        BlockState state = context.player().level().getBlockState(BlockPos.of(payload.pos()));
        if(state.getBlock() instanceof PropBlock<?>) {
            PropBlock<T> block = (PropBlock<T>) state.getBlock();
            if(state.hasProperty(block.COLOR_PROPERTY())){
                context.player().level().setBlockAndUpdate(BlockPos.of(payload.pos()), state.setValue(block.COLOR_PROPERTY(), block.COLOR_PROPERTY().getPossibleValues().get(payload.alt())));
                context.player().level().getBlockState(BlockPos.of(payload.pos())).affectNeighborsAfterRemoval(context.player().level(), BlockPos.of(payload.pos()), false);
            }
        }
        if(state.getBlock() instanceof Sconce) {
            if(state.hasProperty(Sconce.COLOR)){
                context.player().level().setBlockAndUpdate(BlockPos.of(payload.pos()), state.setValue(Sconce.COLOR, Sconce.COLOR.getPossibleValues().get(payload.alt())));
                context.player().level().getBlockState(BlockPos.of(payload.pos())).affectNeighborsAfterRemoval(context.player().level(), BlockPos.of(payload.pos()), false);
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
