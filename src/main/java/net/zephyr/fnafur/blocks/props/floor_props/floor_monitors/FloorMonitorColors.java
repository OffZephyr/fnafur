package net.zephyr.fnafur.blocks.props.floor_props.floor_monitors;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum FloorMonitorColors implements StringIdentifiable, ColorEnumInterface {
    BLACK_0("black_0", 0),
    BLACK_1("black_1", 1),
    BLACK_2("black_2", 2),
    BLACK_3("black_3", 3),
    BLACK_4("black_4", 4),
    BLACK_5("black_5", 5),
    BLACK_6("black_6", 6),
    BLACK_7("black_7", 7),
    BEIGE_0("beige_0", 0),
    BEIGE_1("beige_1", 1),
    BEIGE_2("beige_2", 2),
    BEIGE_3("beige_3", 3),
    BEIGE_4("beige_4", 4),
    BEIGE_5("beige_5", 5),
    BEIGE_6("beige_6", 6),
    BEIGE_7("beige_7", 7)
    ;
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
