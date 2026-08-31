package net.zephyr.fnafur.blocks.decorations;

import net.minecraft.util.StringRepresentable;

public enum BackstageShelf implements StringRepresentable {
    SINGLE("single"),
    CORNER("corner"),
    ;
    public final String NAME;
    BackstageShelf(String name){
        this.NAME = name;
    }
    @Override
    public String getSerializedName() {
        return NAME;
    }
}
