package net.zephyr.fnafur.networking.block;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.linking.LinkTarget;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record LinkVisualUpdateS2CPayload(long pos, long[] blocks, long[] ids, int sync) implements CustomPayload {
    public static final Id<LinkVisualUpdateS2CPayload> ID = new Id<>(BlockPayloads.S2CLinkVisualUpdate);

    public static final PacketCodec<RegistryByteBuf, LinkVisualUpdateS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.LONG, LinkVisualUpdateS2CPayload::pos,
            PacketCodecs.LONG_ARRAY, LinkVisualUpdateS2CPayload::blocks,
            PacketCodecs.LONG_ARRAY, LinkVisualUpdateS2CPayload::ids,
            PacketCodecs.INTEGER, LinkVisualUpdateS2CPayload::sync,
            LinkVisualUpdateS2CPayload::new);

    public static void receive(LinkVisualUpdateS2CPayload payload, ClientPlayNetworking.Context context) {

        if(context.player().getWorld().getBlockEntity(BlockPos.fromLong(payload.pos())) instanceof LinkSource s){
            s.getTargets().clear();
            for(long l : payload.blocks){
                BlockPos pos = BlockPos.fromLong(l);
                if(context.player().getWorld().getBlockEntity(pos) instanceof LinkTarget t){
                    t.getSources().add(((IEntityDataSaver)s));
                    s.getTargets().add(((IEntityDataSaver)t));
                }
            }
            for(long l : payload.ids){
                Entity ent = context.player().getWorld().getEntityById((int)l);
                if(ent instanceof LinkTarget t){
                    t.getSources().add(((IEntityDataSaver)s));
                    s.getTargets().add(((IEntityDataSaver)t));
                }
            }

            s.setSourceAmountSync(payload.sync());
            ClientPlayNetworking.send(new LinkVisualStuffC2SPayload(payload.pos(), s.getTargets().size()));
        }
    }
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
