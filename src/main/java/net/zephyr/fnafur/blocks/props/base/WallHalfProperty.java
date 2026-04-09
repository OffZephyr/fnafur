package net.zephyr.fnafur.blocks.props.base;

import net.minecraft.util.StringRepresentable;

public enum WallHalfProperty implements StringRepresentable {
    WALL("wall"),
    CEILING("ceiling"),
    FLOOR("floor");
    private final String name;
    WallHalfProperty(String name){
        this.name = name;
    }
    @Override
    public String getSerializedName() {
        return this.name;
    }
}
