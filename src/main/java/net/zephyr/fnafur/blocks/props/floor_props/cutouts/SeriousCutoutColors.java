package net.zephyr.fnafur.blocks.props.floor_props.cutouts;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;
import net.zephyr.fnafur.init.SoundsInit;

public enum SeriousCutoutColors implements StringIdentifiable, ColorEnumInterface {
    BEAR5("bear5", 0, SoundsInit.BEAR5, SoundsInit.BEAR5),
    HELPY("helpy", 1, SoundsInit.HELPULES, SoundsInit.HELPULES_PLACE),
    MARKIPLIER("markiplier", 2, SoundsInit.MARKIPLIER, SoundsInit.MARKIPLIER_PLACE),
    DAWKO("dawko", 3, SoundsInit.DAWKO, SoundsInit.DAWKO_PLACE),
    MATPAT("matpat", 4, SoundsInit.MATPAT, SoundsInit.MATPAT_PLACE),
    DASEN("dasen", 5, SoundsInit.DASEN, SoundsInit.DASEN_PLACE),
    ZEPHYR("zephyr", 6, null, null)
    ;
    private final String name;
    private final int index;
    private final SoundEvent sound;
    private final SoundEvent place;
    SeriousCutoutColors(String name, int index, SoundEvent sound, SoundEvent place){
        this.name = name;
        this.index = index;
        this.sound = sound;
        this.place = place;
    }
    @Override
    public String asString() {
        return this.name;
    }
    public int getIndex(){
        return this.index;
    }
    public SoundEvent getSound(){
        return this.sound;
    }
    public SoundEvent getPlace(){
        return this.place;
    }
}
