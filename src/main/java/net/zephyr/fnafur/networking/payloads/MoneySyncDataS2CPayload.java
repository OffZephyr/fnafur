package net.zephyr.fnafur.networking.payloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.zephyr.fnafur.networking.PayloadDef;
import net.zephyr.fnafur.networking.nbt_updates.NbtPayloads;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public record MoneySyncDataS2CPayload(int credits) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MoneySyncDataS2CPayload> ID = new CustomPacketPayload.Type<>(NbtPayloads.S2CMoneyID);
    public static final StreamCodec<FriendlyByteBuf, MoneySyncDataS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, MoneySyncDataS2CPayload::credits,
            MoneySyncDataS2CPayload::new);


    public static void receive(MoneySyncDataS2CPayload payload, ClientPlayNetworking.Context context){
        ((IEntityDataSaver)context.client().player).getPersistentData().putInt("Credits", payload.credits());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
