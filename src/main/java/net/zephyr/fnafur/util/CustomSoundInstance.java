package net.zephyr.fnafur.util;

import net.minecraft.client.sound.EntityTrackingSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

public class CustomSoundInstance extends EntityTrackingSoundInstance {
    public CustomSoundInstance(SoundEvent sound, SoundCategory category, float volume, float pitch, Entity entity, long seed, boolean repeat) {
        super(sound, category, volume, pitch, entity, seed);
        this.repeat = repeat;
    }

    @Override
    public boolean shouldAlwaysPlay() {
        return true;
    }
}
