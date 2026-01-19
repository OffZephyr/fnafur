package net.zephyr.fnafur.networking.block;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.workbench.WorkbenchBlock;
import net.zephyr.fnafur.decals.DecalInstance;
import net.zephyr.fnafur.decals.DecalWorldState;

public record AddDecalC2SPayload(DecalInstance decalInstance) implements CustomPayload {
    public static final Id<AddDecalC2SPayload> ID = new Id<>(BlockPayloads.C2SAddDecal);
    public static final PacketCodec<RegistryByteBuf, AddDecalC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.codec(DecalInstance.CODEC), AddDecalC2SPayload::decalInstance,
            AddDecalC2SPayload::new);

    public static void receive(AddDecalC2SPayload payload, ServerPlayNetworking.Context context) {
        DecalWorldState state = DecalWorldState.get(context.player().getEntityWorld());
        state.addDecal(payload.decalInstance());

        for(ServerPlayerEntity p : PlayerLookup.all(context.server())){
            ServerPlayNetworking.send(p, new FetchAllDecalsS2CPayload(state.getDecals()));
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
