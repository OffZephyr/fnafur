package net.zephyr.fnafur.networking.payloads;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.zephyr.fnafur.networking.PayloadDef;
import net.zephyr.fnafur.networking.nbt_updates.NbtPayloads;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record MoneySyncDataC2SPayload(int credits, boolean shouldUpdate) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MoneySyncDataC2SPayload> ID = new CustomPacketPayload.Type<>(NbtPayloads.C2SMoneyID);
    public static final StreamCodec<FriendlyByteBuf, MoneySyncDataC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, MoneySyncDataC2SPayload::credits,
            ByteBufCodecs.BOOL, MoneySyncDataC2SPayload::shouldUpdate,
            MoneySyncDataC2SPayload::new);
    public static void receive(MoneySyncDataC2SPayload payload, ServerPlayNetworking.Context context) {
        if(payload.shouldUpdate()) ((IEntityDataSaver)context.player()).getPersistentData().putInt("Credits", payload.credits());
        int money = payload.shouldUpdate() ? payload.credits() : ((IEntityDataSaver)context.player()).getPersistentData().getInt("Credits").get();
        ServerPlayNetworking.send(context.player(), new MoneySyncDataS2CPayload(money));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
