package net.zephyr.fnafur.util;

import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;

public class CustomSoundInstance extends EntityBoundSoundInstance {
    public CustomSoundInstance(SoundEvent sound, SoundSource category, float volume, float pitch, Entity entity, long seed, boolean repeat) {
        super(sound, category, volume, pitch, entity, seed);
        this.looping = repeat;
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }
}
