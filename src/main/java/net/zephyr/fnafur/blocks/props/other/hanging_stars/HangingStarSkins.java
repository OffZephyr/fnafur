package net.zephyr.fnafur.blocks.props.other.hanging_stars;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum HangingStarSkins implements StringIdentifiable, ColorEnumInterface {
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
    public String asString() {
        return NAME;
    }

    @Override
    public int getIndex() {
        return 0;
    }
}
