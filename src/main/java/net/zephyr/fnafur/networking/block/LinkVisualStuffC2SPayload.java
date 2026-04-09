package net.zephyr.fnafur.networking.block;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.core.BlockPos;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import net.zephyr.fnafur.networking.entity.EntityPayloads;

public record LinkVisualStuffC2SPayload(long pos, int sync) implements CustomPacketPayload {
    public static final Type<LinkVisualStuffC2SPayload> ID = new Type<>(BlockPayloads.C2SLinkVisualUpdate);
    public static final StreamCodec<RegistryFriendlyByteBuf, LinkVisualStuffC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.LONG, LinkVisualStuffC2SPayload::pos,
            ByteBufCodecs.INT, LinkVisualStuffC2SPayload::sync,
            LinkVisualStuffC2SPayload::new);

    public static void receive(LinkVisualStuffC2SPayload payload, ServerPlayNetworking.Context context) {

        if(context.player().level().getBlockEntity(BlockPos.of(payload.pos())) instanceof LinkSource s){
            s.setSourceAmountSync(payload.sync);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
