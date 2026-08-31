package net.zephyr.fnafur.blocks.decorations;

import net.minecraft.util.StringRepresentable;

public enum WarehouseShelf implements StringRepresentable {
    SINGLE("single"),
    LEFT("left"),
    RIGHT("right")
    ;

    public final String NAME;
    WarehouseShelf(String name){
        this.NAME = name;
    }
    @Override
    public String getSerializedName() {
        return NAME;
    }
}
