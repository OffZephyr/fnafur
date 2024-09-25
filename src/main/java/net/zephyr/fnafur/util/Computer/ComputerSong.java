package net.zephyr.fnafur.util.Computer;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ComputerSong {
    String name;
    String author;
    Identifier iconTexture;
    SoundEvent sound;
    int lengthInTicks;
    public ComputerSong(SoundEvent sound, String name, String author, Identifier iconTexture, int lengthInSeconds){
        this.sound = sound;
        this.name = name;
        this.author = author;
        this.iconTexture = iconTexture;
        this.lengthInTicks = lengthInSeconds * 20;
    }
    public String getName(){
        return this.name;
    }
    public String getAuthor(){
        return this.author;
    }

    public SoundEvent getSound(){
        return this.sound;
    }
    public int getLengthInTicks(){
        return this.lengthInTicks;
    }
    public float getLengthInSeconds(){
        return this.lengthInTicks * 20f;
    }
    public Identifier getIconTexture(){
        return this.iconTexture;
    }
}
