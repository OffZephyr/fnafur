package net.zephyr.fnafur.networking.block;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.core.BlockPos;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.linking.LinkTarget;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record LinkVisualUpdateS2CPayload(long pos, long[] blocks, long[] ids, int sync) implements CustomPacketPayload {
    public static final Type<LinkVisualUpdateS2CPayload> ID = new Type<>(BlockPayloads.S2CLinkVisualUpdate);

    public static final StreamCodec<RegistryFriendlyByteBuf, LinkVisualUpdateS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.LONG, LinkVisualUpdateS2CPayload::pos,
            ByteBufCodecs.LONG_ARRAY, LinkVisualUpdateS2CPayload::blocks,
            ByteBufCodecs.LONG_ARRAY, LinkVisualUpdateS2CPayload::ids,
            ByteBufCodecs.INT, LinkVisualUpdateS2CPayload::sync,
            LinkVisualUpdateS2CPayload::new);

    public static void receive(LinkVisualUpdateS2CPayload payload, ClientPlayNetworking.Context context) {

        if(context.player().level().getBlockEntity(BlockPos.of(payload.pos())) instanceof LinkSource s){
            s.getTargets().clear();
            for(long l : payload.blocks){
                BlockPos pos = BlockPos.of(l);
                if(context.player().level().getBlockEntity(pos) instanceof LinkTarget t){
                    t.getSources().add(((IEntityDataSaver)s));
                    s.getTargets().add(((IEntityDataSaver)t));
                }
            }
            for(long l : payload.ids){
                Entity ent = context.player().level().getEntity((int)l);
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
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
