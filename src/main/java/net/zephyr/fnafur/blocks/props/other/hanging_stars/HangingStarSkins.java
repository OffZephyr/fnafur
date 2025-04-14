package net.zephyr.fnafur.blocks.props.other.hanging_stars;

import net.minecraft.util.StringIdentifiable;

public enum HangingStarSkins implements StringIdentifiable {
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
}
