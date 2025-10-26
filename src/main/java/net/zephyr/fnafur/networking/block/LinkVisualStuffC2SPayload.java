package net.zephyr.fnafur.networking.block;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import net.zephyr.fnafur.networking.entity.EntityPayloads;

public record LinkVisualStuffC2SPayload(long pos, int sync) implements CustomPayload {
    public static final Id<LinkVisualStuffC2SPayload> ID = new Id<>(BlockPayloads.C2SLinkVisualUpdate);
    public static final PacketCodec<RegistryByteBuf, LinkVisualStuffC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.LONG, LinkVisualStuffC2SPayload::pos,
            PacketCodecs.INTEGER, LinkVisualStuffC2SPayload::sync,
            LinkVisualStuffC2SPayload::new);

    public static void receive(LinkVisualStuffC2SPayload payload, ServerPlayNetworking.Context context) {

        if(context.player().getEntityWorld().getBlockEntity(BlockPos.fromLong(payload.pos())) instanceof LinkSource s){
            s.setSourceAmountSync(payload.sync);
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
