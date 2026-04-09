package net.zephyr.fnafur.blocks.props.wall_props.wall_pizza;

import net.minecraft.util.StringRepresentable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum WallPizzaSkins implements ColorEnumInterface, StringRepresentable {
    DEFAULT("default", 0),
    PEPPERONI("pepperoni", 1),
    SLICE("slice",2),
    CUT("cut", 3);

    private final String name;
    private final int index;
    WallPizzaSkins(final String name, final int index){
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
