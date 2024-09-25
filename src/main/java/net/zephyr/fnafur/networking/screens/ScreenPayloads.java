package net.zephyr.fnafur.networking.screens;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;

public class ScreenPayloads {
    public static final Identifier SetBlockScreen = Identifier.of(FnafUniverseResuited.MOD_ID, "s2c_block_screen_open");
    public static final Identifier SetEntityScreen = Identifier.of(FnafUniverseResuited.MOD_ID, "s2c_entity_screen_open");
    public static final Identifier SetItemScreen = Identifier.of(FnafUniverseResuited.MOD_ID, "s2c_item_screen_open");
    public static final Identifier SetNbtScreen = Identifier.of(FnafUniverseResuited.MOD_ID, "s2c_nbt_screen_open");
    public static final Identifier SetScreen = Identifier.of(FnafUniverseResuited.MOD_ID, "s2c_screen_open");


    public static void registerPayloads() {
        PayloadTypeRegistry.playS2C().register(SetBlockScreenS2CPayload.ID, SetBlockScreenS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SetEntityScreenS2CPayload.ID, SetEntityScreenS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SetItemScreenS2CPayload.ID, SetItemScreenS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SetNbtScreenS2CPayload.ID, SetNbtScreenS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SetScreenS2CPayload.ID, SetScreenS2CPayload.CODEC);
    }
    public static void registerClientReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(SetBlockScreenS2CPayload.ID, SetBlockScreenS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(SetEntityScreenS2CPayload.ID, SetEntityScreenS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(SetItemScreenS2CPayload.ID, SetItemScreenS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(SetNbtScreenS2CPayload.ID, SetNbtScreenS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(SetScreenS2CPayload.ID, SetScreenS2CPayload::receive);
    }

    public static void registerServerReceivers() {
    }
}
