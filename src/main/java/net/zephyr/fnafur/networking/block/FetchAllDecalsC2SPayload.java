package net.zephyr.fnafur.networking.block;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.fnafur.rendering.decals.DecalWorldState;

public record FetchAllDecalsC2SPayload(int guh) implements CustomPayload {
    public static final Id<FetchAllDecalsC2SPayload> ID = new Id<>(BlockPayloads.C2SFetchDecals);
    public static final PacketCodec<RegistryByteBuf, FetchAllDecalsC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, FetchAllDecalsC2SPayload::guh,
            FetchAllDecalsC2SPayload::new);

    public static void receive(FetchAllDecalsC2SPayload payload, ServerPlayNetworking.Context context) {
        DecalWorldState state = DecalWorldState.get(context.player().getEntityWorld());
        ServerPlayNetworking.send(context.player(), new FetchAllDecalsS2CPayload(state.getDecals()));
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
