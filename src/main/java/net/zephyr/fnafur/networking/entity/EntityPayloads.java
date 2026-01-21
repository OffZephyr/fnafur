package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.networking.entity.player.UpdateCreativeExtraSlotsC2SPayload;
import net.zephyr.fnafur.networking.entity.player.UpdateMainHandItemC2SPayload;
import net.zephyr.fnafur.networking.entity.player.UpdateMaskStateC2SPayload;
import net.zephyr.fnafur.networking.entity.player.UpdateMaskStateS2CPayload;

public class EntityPayloads {
    public static final Identifier C2SMaskStateUpdate = Identifier.of(FnafUniverseRebuilt.MOD_ID, "c2s_mask_state_update");
    public static final Identifier S2CMaskStateUpdate = Identifier.of(FnafUniverseRebuilt.MOD_ID, "s2c_mask_state_update");
    public static final Identifier C2SExtraSlotUpdate = Identifier.of(FnafUniverseRebuilt.MOD_ID, "c2s_extra_slot_update");
    public static final Identifier C2SMainHandUpdate = Identifier.of(FnafUniverseRebuilt.MOD_ID, "c2s_main_hand_update");
    public static final Identifier C2SWalkSoundPlayer = Identifier.of(FnafUniverseRebuilt.MOD_ID, "c2s_walk_sound_player");
    public static final Identifier C2SWorkbenchSave = Identifier.of(FnafUniverseRebuilt.MOD_ID, "c2s_workbench_save");
    public static final Identifier S2CWorkbenchSave = Identifier.of(FnafUniverseRebuilt.MOD_ID, "s2c_workbench_save");
    public static final Identifier S2CSetEntityRun = Identifier.of(FnafUniverseRebuilt.MOD_ID, "s2c_entity_run");
    public static final Identifier C2SSetEntityRun = Identifier.of(FnafUniverseRebuilt.MOD_ID, "c2s_entity_run");
    public static void registerPayloads() {
        PayloadTypeRegistry.playC2S().register(UpdateMaskStateC2SPayload.ID, UpdateMaskStateC2SPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(UpdateMaskStateS2CPayload.ID, UpdateMaskStateS2CPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(UpdateCreativeExtraSlotsC2SPayload.ID, UpdateCreativeExtraSlotsC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(UpdateMainHandItemC2SPayload.ID, UpdateMainHandItemC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(WalkSoundPlayerC2SPayload.ID, WalkSoundPlayerC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(WorkbenchSaveC2SPayload.ID, WorkbenchSaveC2SPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(WorkbenchSaveS2CPayload.ID, WorkbenchSaveS2CPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(SetEntityRunS2CPayload.ID, SetEntityRunS2CPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SetEntityRunC2SPayload.ID, SetEntityRunC2SPayload.CODEC);
    }

    public static void registerClientReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(WorkbenchSaveS2CPayload.ID, WorkbenchSaveS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(UpdateMaskStateS2CPayload.ID, UpdateMaskStateS2CPayload::receive);

        ClientPlayNetworking.registerGlobalReceiver(SetEntityRunS2CPayload.ID, SetEntityRunS2CPayload::receive);
    }
    public static void registerServerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(UpdateMaskStateC2SPayload.ID, UpdateMaskStateC2SPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(UpdateCreativeExtraSlotsC2SPayload.ID, UpdateCreativeExtraSlotsC2SPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(UpdateMainHandItemC2SPayload.ID, UpdateMainHandItemC2SPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(WalkSoundPlayerC2SPayload.ID, WalkSoundPlayerC2SPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(WorkbenchSaveC2SPayload.ID, WorkbenchSaveC2SPayload::receive);

        ServerPlayNetworking.registerGlobalReceiver(SetEntityRunC2SPayload.ID, SetEntityRunC2SPayload::receive);
    }
}
