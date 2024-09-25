package net.zephyr.fnafur.networking.screens;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;

public record SetEntityScreenS2CPayload(String index, NbtCompound data, int entityID) implements CustomPayload {

    public static final Id<SetEntityScreenS2CPayload> ID = new Id<>(ScreenPayloads.SetEntityScreen);

    public static final PacketCodec<RegistryByteBuf, SetEntityScreenS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, SetEntityScreenS2CPayload::index,
            PacketCodecs.NBT_COMPOUND, SetEntityScreenS2CPayload::data,
            PacketCodecs.INTEGER, SetEntityScreenS2CPayload::entityID,
            SetEntityScreenS2CPayload::new);

    public static void receive(SetEntityScreenS2CPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            GoopyNetworkingUtils.setClientScreen(payload.index(), payload.data(), payload.entityID());
        });
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
