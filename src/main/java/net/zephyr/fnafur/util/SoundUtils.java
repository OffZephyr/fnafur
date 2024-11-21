package net.zephyr.fnafur.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.zephyr.fnafur.init.SoundsInit;
import net.zephyr.fnafur.networking.sounds.PlaySoundPingC2SPayload;
import net.zephyr.fnafur.networking.sounds.PlaySoundS2CPayload;
import net.zephyr.fnafur.networking.sounds.StopSoundPingC2SPayload;
import net.zephyr.fnafur.networking.sounds.StopSoundS2CPayload;

import java.util.HashMap;
import java.util.Map;

public class SoundUtils {
    public static Map<String, SoundInstance> sounds = new HashMap<>();
    public static void playMutableSound(World world, Entity entity, String sound, float volume, float pitch){
        if(world instanceof ServerWorld world2) {
            for (ServerPlayerEntity entity2 : PlayerLookup.all(world2.getServer())) {
                ServerPlayNetworking.send(entity2, new PlaySoundS2CPayload(entity.getId(), sound, volume, pitch));
            }
        }
        else {
            ClientPlayNetworking.send(new PlaySoundPingC2SPayload(entity.getId(), sound, volume, pitch));
        }
    }
    @Environment(EnvType.CLIENT)
    public static void playMutableSound(Entity entity, SoundEvent sound, float volume, float pitch) {
        SoundInstance si = new CustomSoundInstance(sound, SoundCategory.PLAYERS, volume, pitch, entity, Random.createThreadSafe().nextLong(), SoundsInit.soundRepeats(sound));
        MinecraftClient.getInstance().getSoundManager().play(si);
        sounds.put(getKey(entity, sound), si);
    }
    @Environment(EnvType.CLIENT)
    public static void stopSound(World world, Entity entity, String sound){
        if(world instanceof ServerWorld world2) {
            for (ServerPlayerEntity entity2 : PlayerLookup.all(world2.getServer())) {
                ServerPlayNetworking.send(entity2, new StopSoundS2CPayload(entity.getId(), sound));
            }
        }
        else {
            ClientPlayNetworking.send(new StopSoundPingC2SPayload(entity.getId(), sound));
        }
    }

    @Environment(EnvType.CLIENT)
    public static void stopSound(Entity entity, SoundEvent sound){
        String key = getKey(entity, sound);
        SoundInstance si = sounds.get(key);
        MinecraftClient.getInstance().getSoundManager().stop(si);
        sounds.remove(key);
    }
    @Environment(EnvType.CLIENT)
    public static boolean playingSound(Entity entity, SoundEvent sound){
        return sounds.containsKey(getKey(entity, sound));
    }

    public static String getKey(Entity entity, SoundEvent sound){
        return sound.id().getPath() + entity.getId();
    }
}
