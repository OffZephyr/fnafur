package net.zephyr.fnafur.networking.block;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;

public class BlockPayloads {
    public static final Identifier S2CTileDoorOpenUpdate = Identifier.of(FnafUniverseResuited.MOD_ID, "s2c_tiledoor_update");
    public static void registerPayloads() {
        PayloadTypeRegistry.playS2C().register(TileDoorUpdateS2CPayload.ID, TileDoorUpdateS2CPayload.CODEC);
    }

    public static void registerClientReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(TileDoorUpdateS2CPayload.ID, TileDoorUpdateS2CPayload::receive);
    }
    public static void registerServerReceivers() {
    }
}
