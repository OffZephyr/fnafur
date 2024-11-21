package net.zephyr.fnafur.blocks.props.wooden_shelf;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.ColorEnumInterface;

public enum WoodenShelfColors1 implements StringIdentifiable, ColorEnumInterface {
    DEFAULT("gray", 0),
    GRAY("gray_2", 1);
    private final String name;
    private final int index;
    WoodenShelfColors1(String name, int index){
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
