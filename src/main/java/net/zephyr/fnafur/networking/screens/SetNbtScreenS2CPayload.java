package net.zephyr.fnafur.networking.screens;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;

public record SetNbtScreenS2CPayload(String index, CompoundTag data) implements CustomPacketPayload {

    public static final Type<SetNbtScreenS2CPayload> ID = new Type<>(ScreenPayloads.SetNbtScreen);

    public static final StreamCodec<RegistryFriendlyByteBuf, SetNbtScreenS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SetNbtScreenS2CPayload::index,
            ByteBufCodecs.COMPOUND_TAG, SetNbtScreenS2CPayload::data,
            SetNbtScreenS2CPayload::new);

    public static void receive(SetNbtScreenS2CPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            GoopyNetworkingUtils.setClientScreen(payload.index(), payload.data(), 0);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }
}
