package net.zephyr.fnafur.blocks.props.other.hanging_stars;

import net.minecraft.util.StringRepresentable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum HangingStarSkins implements StringRepresentable, ColorEnumInterface {
    WHITE("white"),
    BLACK("black"),
    WHITE_COLORED("white_colored"),
    BLACK_COLORED("black_colored")
    ;

    final String NAME;
    HangingStarSkins(String name){
        this.NAME = name;
    }
    @Override
    public String getSerializedName() {
        return NAME;
    }

    @Override
    public int getIndex() {
        return 0;
    }
}
