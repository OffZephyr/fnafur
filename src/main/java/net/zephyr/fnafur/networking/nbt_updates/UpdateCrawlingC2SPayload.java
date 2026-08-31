package net.zephyr.fnafur.networking.nbt_updates;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.zephyr.fnafur.util.mixinAccessing.IUniversePlayer;

public record UpdateCrawlingC2SPayload(boolean crawl) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UpdateCrawlingC2SPayload> ID = new CustomPacketPayload.Type<>(NbtPayloads.C2SCrawlUpdate);
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateCrawlingC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, UpdateCrawlingC2SPayload::crawl,
            UpdateCrawlingC2SPayload::new);

    public static void receive(UpdateCrawlingC2SPayload payload, ServerPlayNetworking.Context context){
        ((IUniversePlayer)context.player()).setCrawling(payload.crawl());
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
