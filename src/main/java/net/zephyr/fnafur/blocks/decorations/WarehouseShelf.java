package net.zephyr.fnafur.blocks.decorations;

import net.minecraft.util.StringIdentifiable;

public enum WarehouseShelf implements StringIdentifiable {
    SINGLE("single"),
    LEFT("left"),
    RIGHT("right")
    ;

    public final String NAME;
    WarehouseShelf(String name){
        this.NAME = name;
    }
    @Override
    public String asString() {
        return NAME;
    }
}
