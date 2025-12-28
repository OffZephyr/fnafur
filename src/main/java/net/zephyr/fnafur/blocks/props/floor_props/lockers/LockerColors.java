package net.zephyr.fnafur.blocks.props.floor_props.lockers;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum LockerColors implements StringIdentifiable, ColorEnumInterface {
    DEFAULT("default", 0),
    YELLOW("yellow", 1),
    ;

    private final String name;
    private final int index;
    LockerColors(String name, int index){
        this.name = name;
        this.index = index;
    }
    @Override
    public String asString() {
        return this.name;
    }
    public int getIndex(){
        return this.index;
    }
}
