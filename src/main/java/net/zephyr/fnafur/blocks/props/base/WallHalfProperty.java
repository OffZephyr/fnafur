package net.zephyr.fnafur.blocks.props.base;

import net.minecraft.util.StringIdentifiable;

public enum WallHalfProperty implements StringIdentifiable {
    WALL("wall"),
    CEILING("ceiling"),
    FLOOR("floor");
    private final String name;
    WallHalfProperty(String name){
        this.name = name;
    }
    @Override
    public String asString() {
        return this.name;
    }
}
