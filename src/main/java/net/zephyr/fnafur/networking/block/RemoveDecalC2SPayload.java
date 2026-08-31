package net.zephyr.fnafur.networking.block;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.server.level.ServerPlayer;
import net.zephyr.fnafur.rendering.decals.DecalInstance;
import net.zephyr.fnafur.rendering.decals.DecalWorldState;

public record RemoveDecalC2SPayload(DecalInstance decalInstance) implements CustomPacketPayload {
    public static final Type<RemoveDecalC2SPayload> ID = new Type<>(BlockPayloads.C2SRemoveDecal);
    public static final StreamCodec<RegistryFriendlyByteBuf, RemoveDecalC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(DecalInstance.CODEC), RemoveDecalC2SPayload::decalInstance,
            RemoveDecalC2SPayload::new);

    public static void receive(RemoveDecalC2SPayload payload, ServerPlayNetworking.Context context) {

        DecalWorldState state = DecalWorldState.get(context.player().level());
        state.removeDecal(payload.decalInstance());

        for(ServerPlayer p : PlayerLookup.all(context.server())){
            ServerPlayNetworking.send(p, new FetchAllDecalsS2CPayload(state.getDecals()));
        }

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
