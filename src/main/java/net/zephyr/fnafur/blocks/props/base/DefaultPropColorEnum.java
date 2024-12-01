package net.zephyr.fnafur.blocks.props.base;

import net.minecraft.util.StringIdentifiable;

public enum DefaultPropColorEnum implements StringIdentifiable, ColorEnumInterface {
    DEFAULT("default", 0),
    DEFAULT_2("default2", 0);
    private final String name;
    private final int index;
    DefaultPropColorEnum(String name, int index){
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
