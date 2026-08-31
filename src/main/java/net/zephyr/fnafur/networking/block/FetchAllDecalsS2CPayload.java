package net.zephyr.fnafur.networking.block;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.zephyr.fnafur.rendering.decals.DecalInstance;
import net.zephyr.fnafur.rendering.decals.DecalManager;

import java.util.List;

public record FetchAllDecalsS2CPayload(List<DecalInstance> decals) implements CustomPacketPayload {
    public static final Type<FetchAllDecalsS2CPayload> ID = new Type<>(BlockPayloads.S2CFetchDecals);
    public static final StreamCodec<RegistryFriendlyByteBuf, FetchAllDecalsS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(DecalInstance.CODEC).apply(ByteBufCodecs.list()), FetchAllDecalsS2CPayload::decals,
            FetchAllDecalsS2CPayload::new);

    public static void receive(FetchAllDecalsS2CPayload payload, ClientPlayNetworking.Context context) {


        DecalManager.WORLD_DECALS.clear();
        DecalManager.WORLD_DECALS.addAll(payload.decals());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
