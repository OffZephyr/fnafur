package net.zephyr.fnafur.entity.animatronic.voice;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;

public class EntityVoiceSoundInstance extends AbstractTickableSoundInstance {

    private final AnimatronicEntity entity;
    public String name = "";

    public EntityVoiceSoundInstance(AnimatronicEntity entity, String name, SoundEvent sound, float volume, float pitch) {
        super(sound, entity.getSoundSource(), SoundInstance.createUnseededRandom());
        this.entity = entity;
        this.volume = volume;
        this.pitch = pitch;
        this.looping = false;
        this.name = name;
    }

    @Override
    public void tick() {
        if (entity.isRemoved()) {
            this.stop();
            return;
        }

        this.x = entity.getX();
        this.y = entity.getY();
        this.z = entity.getZ();
    }


}