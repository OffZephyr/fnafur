package net.zephyr.fnafur.networking.screens;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;

public record SetBlockScreenS2CPayload(String index, CompoundTag data, long pos) implements CustomPacketPayload {

    public static final Type<SetBlockScreenS2CPayload> ID = new Type<>(ScreenPayloads.SetBlockScreen);

    public static final StreamCodec<RegistryFriendlyByteBuf, SetBlockScreenS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SetBlockScreenS2CPayload::index,
            ByteBufCodecs.COMPOUND_TAG, SetBlockScreenS2CPayload::data,
            ByteBufCodecs.VAR_LONG, SetBlockScreenS2CPayload::pos,
            SetBlockScreenS2CPayload::new);

    public static void receive(SetBlockScreenS2CPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            GoopyNetworkingUtils.setClientScreen(payload.index(), payload.data(), BlockPos.of(payload.pos()));
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }
}
