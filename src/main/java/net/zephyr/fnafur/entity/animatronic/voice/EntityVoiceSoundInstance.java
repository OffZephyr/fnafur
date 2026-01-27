package net.zephyr.fnafur.entity.animatronic.voice;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;

public class EntityVoiceSoundInstance extends MovingSoundInstance {

    private final AnimatronicEntity entity;
    public String name = "";

    public EntityVoiceSoundInstance(AnimatronicEntity entity, String name, SoundEvent sound, float volume, float pitch) {
        super(sound, entity.getSoundCategory(), SoundInstance.createRandom());
        this.entity = entity;
        this.volume = volume;
        this.pitch = pitch;
        this.repeat = false;
        this.name = name;
    }

    @Override
    public void tick() {
        if (entity.isRemoved()) {
            this.setDone();
            return;
        }

        this.x = entity.getX();
        this.y = entity.getY();
        this.z = entity.getZ();
    }


}