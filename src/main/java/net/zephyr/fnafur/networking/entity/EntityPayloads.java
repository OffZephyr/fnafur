package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.networking.nbt_updates.*;
import net.zephyr.fnafur.networking.nbt_updates.computer.ComputerEjectPayload;
import net.zephyr.fnafur.networking.nbt_updates.goopy_entity.*;

public class EntityPayloads {
    public static final Identifier C2SWalkSoundPlayer = Identifier.of(FnafUniverseRebuilt.MOD_ID, "c2s_walk_sound_player");
    public static final Identifier C2SWorkbenchSave = Identifier.of(FnafUniverseRebuilt.MOD_ID, "c2s_workbench_save");
    public static final Identifier S2CWorkbenchSave = Identifier.of(FnafUniverseRebuilt.MOD_ID, "s2c_workbench_save");
    public static void registerPayloads() {
        PayloadTypeRegistry.playC2S().register(WalkSoundPlayerC2SPayload.ID, WalkSoundPlayerC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(WorkbenchSaveC2SPayload.ID, WorkbenchSaveC2SPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(WorkbenchSaveS2CPayload.ID, WorkbenchSaveS2CPayload.CODEC);
    }

    public static void registerClientReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(WorkbenchSaveS2CPayload.ID, WorkbenchSaveS2CPayload::receive);
    }
    public static void registerServerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(WalkSoundPlayerC2SPayload.ID, WalkSoundPlayerC2SPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(WorkbenchSaveC2SPayload.ID, WorkbenchSaveC2SPayload::receive);
    }
}
