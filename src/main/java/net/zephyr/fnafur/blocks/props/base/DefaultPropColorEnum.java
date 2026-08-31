package net.zephyr.fnafur.blocks.props.base;

import net.minecraft.util.StringRepresentable;

public enum DefaultPropColorEnum implements StringRepresentable, ColorEnumInterface {
    DEFAULT("default", 0),
    DEFAULT_2("default2", 0);
    private final String name;
    private final int index;
    DefaultPropColorEnum(String name, int index){
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
