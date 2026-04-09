package net.zephyr.fnafur.networking.screens;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;

public record SetEntityScreenS2CPayload(String index, CompoundTag data, int entityID) implements CustomPacketPayload {

    public static final Type<SetEntityScreenS2CPayload> ID = new Type<>(ScreenPayloads.SetEntityScreen);

    public static final StreamCodec<RegistryFriendlyByteBuf, SetEntityScreenS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SetEntityScreenS2CPayload::index,
            ByteBufCodecs.COMPOUND_TAG, SetEntityScreenS2CPayload::data,
            ByteBufCodecs.INT, SetEntityScreenS2CPayload::entityID,
            SetEntityScreenS2CPayload::new);

    public static void receive(SetEntityScreenS2CPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            GoopyNetworkingUtils.setClientScreen(payload.index(), payload.data(), payload.entityID());
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }
}
