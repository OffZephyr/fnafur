package net.zephyr.fnafur.entity.animatronic.voice;

import org.jetbrains.annotations.Nullable;

public interface VoiceSource {
    @Nullable EntityVoiceSoundInstance getVoiceSound();

    void setVoiceSound(EntityVoiceSoundInstance sound);
}
