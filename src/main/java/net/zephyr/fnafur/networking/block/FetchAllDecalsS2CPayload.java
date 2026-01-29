package net.zephyr.fnafur.networking.block;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.fnafur.rendering.decals.DecalInstance;
import net.zephyr.fnafur.rendering.decals.DecalManager;

import java.util.List;

public record FetchAllDecalsS2CPayload(List<DecalInstance> decals) implements CustomPayload {
    public static final Id<FetchAllDecalsS2CPayload> ID = new Id<>(BlockPayloads.S2CFetchDecals);
    public static final PacketCodec<RegistryByteBuf, FetchAllDecalsS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.codec(DecalInstance.CODEC).collect(PacketCodecs.toList()), FetchAllDecalsS2CPayload::decals,
            FetchAllDecalsS2CPayload::new);

    public static void receive(FetchAllDecalsS2CPayload payload, ClientPlayNetworking.Context context) {


        DecalManager.WORLD_DECALS.clear();
        DecalManager.WORLD_DECALS.addAll(payload.decals());
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
