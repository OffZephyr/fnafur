package net.zephyr.fnafur.blocks.props.wall_props.wall_pizza;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum WallPizzaSkins implements ColorEnumInterface, StringIdentifiable {
    DEFAULT("default", 0),
    PEPPERONI("pepperoni", 1);
    private final String name;
    private final int index;
    WallPizzaSkins(final String name, final int index){
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
