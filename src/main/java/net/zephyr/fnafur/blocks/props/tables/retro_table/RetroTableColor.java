package net.zephyr.fnafur.blocks.props.tables.retro_table;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.ColorEnumInterface;

public enum RetroTableColor implements StringIdentifiable, ColorEnumInterface {
    DEFAULT("default", 0),
    DEFAULT_2("default2", 0);
    private final String name;
    private final int index;
    RetroTableColor(String name, int index){
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