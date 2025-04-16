package net.zephyr.fnafur.blocks.decorations;

import net.minecraft.util.StringIdentifiable;

public enum BackstageShelf implements StringIdentifiable {
    SINGLE("single"),
    CORNER("corner"),
    ;
    public final String NAME;
    BackstageShelf(String name){
        this.NAME = name;
    }
    @Override
    public String asString() {
        return NAME;
    }
}
