package net.zephyr.fnafur.networking.screens;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;

public record SetScreenS2CPayload(String index) implements CustomPacketPayload {

    public static final Type<SetScreenS2CPayload> ID = new Type<>(ScreenPayloads.SetScreen);

    public static final StreamCodec<RegistryFriendlyByteBuf, SetScreenS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SetScreenS2CPayload::index,
            SetScreenS2CPayload::new);

    public static void receive(SetScreenS2CPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            GoopyNetworkingUtils.setClientScreen(payload.index(), new CompoundTag(), 0);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }
}
