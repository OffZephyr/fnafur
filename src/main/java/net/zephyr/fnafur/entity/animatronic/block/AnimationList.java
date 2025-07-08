package net.zephyr.fnafur.entity.animatronic.block;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum AnimationList implements StringIdentifiable, ColorEnumInterface {
    FLOOR_LAY("floor_lay","floorlay", false, null),
    FLOOR_SIT("floor_sit","floorsit", false, null),
    OFF_SIT("off_sit","situpper", false, "sit"),
    ;

    final String NAME;
    final String MAIN;
    final String LOWER;
    final boolean BLINKS;
    AnimationList(String name, String animation_main, boolean blinks, String animation_lower){
        this.NAME = name;
        this.MAIN = animation_main;
        this.BLINKS = blinks;
        this.LOWER = animation_lower;
    }
    @Override
    public String asString() {
        return NAME;
    }
    public String getMain() {
        return MAIN;
    }
    public String getLower() {
        return LOWER;
    }

    @Override
    public int getIndex() {
        return 0;
    }

    public boolean blinks(){
        return BLINKS;
    }
}
