package net.zephyr.fnafur.networking.screens;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;

public record SetItemScreenS2CPayload(String index, CompoundTag data, String slot) implements CustomPacketPayload {

    public static final Type<SetItemScreenS2CPayload> ID = new Type<>(ScreenPayloads.SetItemScreen);

    public static final StreamCodec<RegistryFriendlyByteBuf, SetItemScreenS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SetItemScreenS2CPayload::index,
            ByteBufCodecs.COMPOUND_TAG, SetItemScreenS2CPayload::data,
            ByteBufCodecs.STRING_UTF8, SetItemScreenS2CPayload::slot,
            SetItemScreenS2CPayload::new);

    public static void receive(SetItemScreenS2CPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            GoopyNetworkingUtils.setClientScreen(payload.index(), payload.data(), payload.slot());
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }
}
