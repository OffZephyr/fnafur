package net.zephyr.fnafur.networking.screens;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;

public record SetBlockScreenS2CPayload(String index, NbtCompound data, long pos) implements CustomPayload {

    public static final Id<SetBlockScreenS2CPayload> ID = new Id<>(ScreenPayloads.SetBlockScreen);

    public static final PacketCodec<RegistryByteBuf, SetBlockScreenS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, SetBlockScreenS2CPayload::index,
            PacketCodecs.NBT_COMPOUND, SetBlockScreenS2CPayload::data,
            PacketCodecs.VAR_LONG, SetBlockScreenS2CPayload::pos,
            SetBlockScreenS2CPayload::new);

    public static void receive(SetBlockScreenS2CPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            GoopyNetworkingUtils.setClientScreen(payload.index(), payload.data(), BlockPos.fromLong(payload.pos()));
        });
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
