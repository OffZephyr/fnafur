package net.zephyr.fnafur.blocks.props.floor_props.floor_monitors;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum FloorMonitorColors implements StringIdentifiable, ColorEnumInterface {
    DEFAULT("black", 0),
    BEIGE("beige", 1);
    private final String name;
    private final int index;
    FloorMonitorColors(String name, int index){
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
