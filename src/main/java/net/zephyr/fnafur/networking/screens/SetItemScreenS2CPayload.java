package net.zephyr.fnafur.networking.screens;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;

public record SetItemScreenS2CPayload(String index, NbtCompound data, String slot) implements CustomPayload {

    public static final Id<SetItemScreenS2CPayload> ID = new Id<>(ScreenPayloads.SetItemScreen);

    public static final PacketCodec<RegistryByteBuf, SetItemScreenS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, SetItemScreenS2CPayload::index,
            PacketCodecs.NBT_COMPOUND, SetItemScreenS2CPayload::data,
            PacketCodecs.STRING, SetItemScreenS2CPayload::slot,
            SetItemScreenS2CPayload::new);

    public static void receive(SetItemScreenS2CPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            GoopyNetworkingUtils.setClientScreen(payload.index(), payload.data(), payload.slot());
        });
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
