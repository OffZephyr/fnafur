package net.zephyr.fnafur.blocks.props.wall_props.restroom;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum BathroomSinkSkins implements ColorEnumInterface, StringIdentifiable {
    DEFAULT("default", 0),
    STAINS_1("stains_1", 1);
    private final String name;
    private final int index;
    BathroomSinkSkins(final String name, final int index){
        this.name = name;
        this.index = index;
    }

    @Override
    public String asString() {
        return name;
    }

    @Override
    public int getIndex() {
        return index;
    }
}
