package net.zephyr.fnafur.blocks.props.wall_props.restroom_sign;

import net.minecraft.util.StringRepresentable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum RestroomSignSkins implements ColorEnumInterface, StringRepresentable {
    MEN("men", 0),
    WOMEN("women", 1),
    NEUTRAL("neutral", 2);
    private final String name;
    private final int index;
    RestroomSignSkins(final String name, final int index){
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
