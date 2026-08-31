package net.zephyr.fnafur.blocks.props.wall_props.clocks;

import net.minecraft.util.StringRepresentable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum WoodenClockColorEnum implements StringRepresentable, ColorEnumInterface {
    DEFAULT("default", 0),
    SECONDS("seconds", 1);
    private final String name;
    private final int index;
    WoodenClockColorEnum(String name, int index){
        this.name = name;
        this.index = index;
    }
    @Override
    public String getSerializedName() {
        return this.name;
    }
    public int getIndex(){
        return this.index;
    }
}
