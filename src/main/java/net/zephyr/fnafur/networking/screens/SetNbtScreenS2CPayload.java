package net.zephyr.fnafur.networking.screens;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;

public record SetNbtScreenS2CPayload(String index, NbtCompound data) implements CustomPayload {

    public static final Id<SetNbtScreenS2CPayload> ID = new Id<>(ScreenPayloads.SetNbtScreen);

    public static final PacketCodec<RegistryByteBuf, SetNbtScreenS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, SetNbtScreenS2CPayload::index,
            PacketCodecs.NBT_COMPOUND, SetNbtScreenS2CPayload::data,
            SetNbtScreenS2CPayload::new);

    public static void receive(SetNbtScreenS2CPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            GoopyNetworkingUtils.setClientScreen(payload.index(), payload.data(), 0);
        });
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
