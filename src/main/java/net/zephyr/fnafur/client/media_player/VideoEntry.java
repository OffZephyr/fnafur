package net.zephyr.fnafur.client.media_player;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class VideoEntry {
    Identifier ID;
    SoundEvent sound;
    public VideoEntry(Identifier id, SoundEvent sound){
        this.ID = id;
        this.sound = sound;
    }

    Identifier getID(){
        return this.ID;
    }

    SoundEvent getSoundEvent(){
        return this.sound;
    }
}
