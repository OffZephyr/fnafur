package net.zephyr.fnafur.networking.entity;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.networking.nbt_updates.*;
import net.zephyr.fnafur.networking.nbt_updates.computer.ComputerEjectPayload;
import net.zephyr.fnafur.networking.nbt_updates.goopy_entity.*;

public class EntityPayloads {
    public static final Identifier C2SWalkSoundPlayer = Identifier.of(FnafUniverseResuited.MOD_ID, "c2s_walk_sound_player");
    public static void registerPayloads() {
        PayloadTypeRegistry.playC2S().register(WalkSoundPlayerC2SPayload.ID, WalkSoundPlayerC2SPayload.CODEC);
    }

    public static void registerClientReceivers() {
    }
    public static void registerServerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(WalkSoundPlayerC2SPayload.ID, WalkSoundPlayerC2SPayload::receive);
    }
}
