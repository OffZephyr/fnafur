package net.zephyr.fnafur.blocks.props.wall_props.clocks;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum WoodenClockColorEnum implements StringIdentifiable, ColorEnumInterface {
    DEFAULT("default", 0),
    SECONDS("seconds", 1);
    private final String name;
    private final int index;
    WoodenClockColorEnum(String name, int index){
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
