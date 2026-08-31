package net.zephyr.fnafur.networking.block;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;

public class BlockPayloads {
    public static final Identifier S2CTileDoorOpenUpdate = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "s2c_tiledoor_update");
    public static final Identifier C2SLinkVisualUpdate = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "c2s_link_visual_update");
    public static final Identifier S2CLinkVisualUpdate = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "s2c_link_visual_update");
    public static final Identifier C2SUpdatePropAlt = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "c2s_update_prop_alt");
    public static final Identifier S2CUpdatePropAlt = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "s2c_update_prop_alt");
    public static final Identifier C2SDropItemFromWorkbench = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "c2s_drop_item_from_workbench");

    public static final Identifier C2SAddDecal = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "c2s_add_decal");
    public static final Identifier S2CAddDecal = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "s2c_add_decal");
    public static final Identifier C2SRemoveDecal = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "c2s_remove_decal");
    public static final Identifier S2CRemoveDecal = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "s2c_remove_decal");
    public static final Identifier C2SFetchDecals = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "c2s_fetch_decals");
    public static final Identifier S2CFetchDecals = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "s2c_fetch_decals");

    public static void registerPayloads() {
        PayloadTypeRegistry.playS2C().register(TileDoorUpdateS2CPayload.ID, TileDoorUpdateS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(LinkVisualUpdateS2CPayload.ID, LinkVisualUpdateS2CPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(LinkVisualStuffC2SPayload.ID, LinkVisualStuffC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(DropItemFromWorkbenchC2SPayload.ID, DropItemFromWorkbenchC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(UpdatePropAltC2SPayload.ID, UpdatePropAltC2SPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(AddDecalC2SPayload.ID, AddDecalC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(RemoveDecalC2SPayload.ID, RemoveDecalC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(FetchAllDecalsC2SPayload.ID, FetchAllDecalsC2SPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(FetchAllDecalsS2CPayload.ID, FetchAllDecalsS2CPayload.CODEC);
    }

    public static void registerClientReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(TileDoorUpdateS2CPayload.ID, TileDoorUpdateS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(LinkVisualUpdateS2CPayload.ID, LinkVisualUpdateS2CPayload::receive);

        ClientPlayNetworking.registerGlobalReceiver(FetchAllDecalsS2CPayload.ID, FetchAllDecalsS2CPayload::receive);
    }
    public static void registerServerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(UpdatePropAltC2SPayload.ID, UpdatePropAltC2SPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(LinkVisualStuffC2SPayload.ID, LinkVisualStuffC2SPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(DropItemFromWorkbenchC2SPayload.ID, DropItemFromWorkbenchC2SPayload::receive);

        ServerPlayNetworking.registerGlobalReceiver(AddDecalC2SPayload.ID, AddDecalC2SPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(RemoveDecalC2SPayload.ID, RemoveDecalC2SPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(FetchAllDecalsC2SPayload.ID, FetchAllDecalsC2SPayload::receive);
    }
}
