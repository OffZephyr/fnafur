package net.zephyr.fnafur.networking.block;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;

public class BlockPayloads {
    public static final Identifier S2CTileDoorOpenUpdate = Identifier.of(FnafUniverseRebuilt.MOD_ID, "s2c_tiledoor_update");
    public static final Identifier C2SLinkVisualUpdate = Identifier.of(FnafUniverseRebuilt.MOD_ID, "c2s_link_visual_update");
    public static final Identifier S2CLinkVisualUpdate = Identifier.of(FnafUniverseRebuilt.MOD_ID, "s2c_link_visual_update");
    public static void registerPayloads() {
        PayloadTypeRegistry.playS2C().register(TileDoorUpdateS2CPayload.ID, TileDoorUpdateS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(LinkVisualUpdateS2CPayload.ID, LinkVisualUpdateS2CPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(LinkVisualStuffC2SPayload.ID, LinkVisualStuffC2SPayload.CODEC);
    }

    public static void registerClientReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(TileDoorUpdateS2CPayload.ID, TileDoorUpdateS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(LinkVisualUpdateS2CPayload.ID, LinkVisualUpdateS2CPayload::receive);
    }
    public static void registerServerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(LinkVisualStuffC2SPayload.ID, LinkVisualStuffC2SPayload::receive);
    }
}
