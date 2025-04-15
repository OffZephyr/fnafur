package net.zephyr.fnafur.blocks.props.floor_props.chairs;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum RetroStoolColors implements StringIdentifiable, ColorEnumInterface {
    COLOR_0("color_0", 0),
    COLOR_1("color_1", 1),
    COLOR_2("color_2", 2),
    COLOR_3("color_3", 3),
    COLOR_4("color_4", 4),
    COLOR_5("color_5", 5),
    COLOR_6("color_6", 6),
    COLOR_7("color_7", 7),
    COLOR_8("color_8", 8),
    COLOR_9("color_9", 9),
    COLOR_10("color_10", 10),
    COLOR_11("color_11", 11),
    COLOR_12("color_12", 12),
    COLOR_13("color_13", 13),
    COLOR_14("color_14", 14),
    ;
    private final String name;
    private final int index;
    RetroStoolColors(String name, int index){
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
