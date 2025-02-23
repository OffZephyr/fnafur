package net.zephyr.fnafur.networking.payloads;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.zephyr.fnafur.networking.PayloadDef;
import net.zephyr.fnafur.networking.nbt_updates.NbtPayloads;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record MoneySyncDataC2SPayload(int credits, boolean shouldUpdate) implements CustomPayload {
    public static final CustomPayload.Id<MoneySyncDataC2SPayload> ID = new CustomPayload.Id<>(NbtPayloads.C2SMoneyID);
    public static final PacketCodec<PacketByteBuf, MoneySyncDataC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, MoneySyncDataC2SPayload::credits,
            PacketCodecs.BOOLEAN, MoneySyncDataC2SPayload::shouldUpdate,
            MoneySyncDataC2SPayload::new);
    public static void receive(MoneySyncDataC2SPayload payload, ServerPlayNetworking.Context context) {
        if(payload.shouldUpdate()) ((IEntityDataSaver)context.player()).getPersistentData().putInt("Credits", payload.credits());
        int money = payload.shouldUpdate() ? payload.credits() : ((IEntityDataSaver)context.player()).getPersistentData().getInt("Credits");
        ServerPlayNetworking.send(context.player(), new MoneySyncDataS2CPayload(money));
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
