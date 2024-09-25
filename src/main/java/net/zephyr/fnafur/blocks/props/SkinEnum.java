package net.zephyr.fnafur.blocks.props;

import net.minecraft.util.StringIdentifiable;

public enum SkinEnum implements StringIdentifiable {
    DEFAULT("default");
    private final String name;
    SkinEnum(String name){
        this.name = name;
    }
    @Override
    public String asString() {
        return this.name;
    }
}
