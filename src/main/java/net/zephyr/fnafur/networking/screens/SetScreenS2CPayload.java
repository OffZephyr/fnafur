package net.zephyr.fnafur.networking.screens;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;

public record SetScreenS2CPayload(String index) implements CustomPayload {

    public static final Id<SetScreenS2CPayload> ID = new Id<>(ScreenPayloads.SetScreen);

    public static final PacketCodec<RegistryByteBuf, SetScreenS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, SetScreenS2CPayload::index,
            SetScreenS2CPayload::new);

    public static void receive(SetScreenS2CPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            GoopyNetworkingUtils.setClientScreen(payload.index(), new NbtCompound(), 0);
        });
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
