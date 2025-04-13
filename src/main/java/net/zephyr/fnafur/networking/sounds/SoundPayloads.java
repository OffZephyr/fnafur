package net.zephyr.fnafur.networking.sounds;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;

public class SoundPayloads {
    public static final Identifier PlaySoundS2C = Identifier.of(FnafUniverseRebuilt.MOD_ID, "s2c_play_sound");
    public static final Identifier PlaySoundC2S = Identifier.of(FnafUniverseRebuilt.MOD_ID, "c2s_play_sound");
    public static final Identifier StopSoundS2C = Identifier.of(FnafUniverseRebuilt.MOD_ID, "s2c_stop_sound");
    public static final Identifier StopSoundC2S = Identifier.of(FnafUniverseRebuilt.MOD_ID, "c2s_stop_sound");

    public static void registerPayloads() {
        PayloadTypeRegistry.playS2C().register(PlaySoundS2CPayload.ID, PlaySoundS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(StopSoundS2CPayload.ID, StopSoundS2CPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(PlaySoundPingC2SPayload.ID, PlaySoundPingC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(StopSoundPingC2SPayload.ID, StopSoundPingC2SPayload.CODEC);
    }

    public static void registerServerReceivers(){
        ServerPlayNetworking.registerGlobalReceiver(PlaySoundPingC2SPayload.ID, PlaySoundPingC2SPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(StopSoundPingC2SPayload.ID, StopSoundPingC2SPayload::receive);

    }

    public static void registerClientReceivers(){
        ClientPlayNetworking.registerGlobalReceiver(PlaySoundS2CPayload.ID, PlaySoundS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(StopSoundS2CPayload.ID, StopSoundS2CPayload::receive);

    }
}
