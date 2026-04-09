package net.zephyr.fnafur.networking.block;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.zephyr.fnafur.rendering.decals.DecalWorldState;

public record FetchAllDecalsC2SPayload(int guh) implements CustomPacketPayload {
    public static final Type<FetchAllDecalsC2SPayload> ID = new Type<>(BlockPayloads.C2SFetchDecals);
    public static final StreamCodec<RegistryFriendlyByteBuf, FetchAllDecalsC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, FetchAllDecalsC2SPayload::guh,
            FetchAllDecalsC2SPayload::new);

    public static void receive(FetchAllDecalsC2SPayload payload, ServerPlayNetworking.Context context) {
        DecalWorldState state = DecalWorldState.get(context.player().level());
        ServerPlayNetworking.send(context.player(), new FetchAllDecalsS2CPayload(state.getDecals()));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
