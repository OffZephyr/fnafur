package net.zephyr.fnafur.blocks.props.floor_props.present_stack;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum PresentStackColors implements StringIdentifiable, ColorEnumInterface {
    ZERO("0", 0),
    ONE("1", 1),
    TWO("2", 2),

    ;
    private final String name;
    private final int index;
    PresentStackColors(String name, int index){
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
