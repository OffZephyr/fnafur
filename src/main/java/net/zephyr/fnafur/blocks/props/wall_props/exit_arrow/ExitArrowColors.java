package net.zephyr.fnafur.blocks.props.wall_props.exit_arrow;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum ExitArrowColors implements ColorEnumInterface, StringIdentifiable {
    LEFT("left", 0),
    RIGHT("right", 1),
    ;
    private final String name;
    private final int index;
    ExitArrowColors(final String name, final int index){
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
