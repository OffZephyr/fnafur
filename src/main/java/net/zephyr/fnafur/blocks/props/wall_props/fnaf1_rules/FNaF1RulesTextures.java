package net.zephyr.fnafur.blocks.props.wall_props.fnaf1_rules;

import net.minecraft.util.StringRepresentable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum FNaF1RulesTextures implements ColorEnumInterface, StringRepresentable {
    TORN("torn", 0),
    CLEAN("clean", 1)
    ;
    private final String name;
    private final int index;
    FNaF1RulesTextures(final String name, final int index){
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
