package net.zephyr.fnafur.blocks.props.wall_props.restroom;

import net.minecraft.util.StringRepresentable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum BathroomSinkSkins implements ColorEnumInterface, StringRepresentable {
    DEFAULT("default", 0),
    STAINS_1("stains_1", 1);
    private final String name;
    private final int index;
    BathroomSinkSkins(final String name, final int index){
        this.name = name;
        this.index = index;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    @Override
    public int getIndex() {
        return index;
    }
}
