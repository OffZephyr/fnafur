package net.zephyr.fnafur.blocks.energy.enums;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum GenericEnum implements StringIdentifiable, ColorEnumInterface {
    DEFAULT("default", 0),
    OLD("old", 1);

    private final String name;
    private final int index;

    GenericEnum(String name, int index){
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
