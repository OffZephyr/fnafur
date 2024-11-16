package net.zephyr.fnafur.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.networking.nbt_updates.computer.ComputerEjectPayload;
import net.zephyr.fnafur.networking.nbt_updates.UpdateCrawlingC2SPayload;
import net.zephyr.fnafur.networking.payloads.*;

public class PayloadDef {

    public static void registerC2SPackets() {

        PayloadTypeRegistry.playC2S().register(MoneySyncDataC2SPayload.ID, MoneySyncDataC2SPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(MoneySyncDataS2CPayload.ID, MoneySyncDataS2CPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(MoneySyncDataC2SPayload.ID, MoneySyncDataC2SPayload::receive);
    }
    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(MoneySyncDataS2CPayload.ID, MoneySyncDataS2CPayload::receive);
    }
}
