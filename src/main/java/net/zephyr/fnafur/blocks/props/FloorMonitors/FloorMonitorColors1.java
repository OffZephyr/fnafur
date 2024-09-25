package net.zephyr.fnafur.blocks.props.FloorMonitors;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.ColorEnumInterface;

public enum FloorMonitorColors1 implements StringIdentifiable, ColorEnumInterface {
    DEFAULT("black", 0),
    BEIGE("beige", 1);
    private final String name;
    private final int index;
    FloorMonitorColors1(String name, int index){
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
