package net.zephyr.fnafur.blocks.light;

import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.blocks.props.base.ColorEnumInterface;

public enum SconceColors implements StringIdentifiable, ColorEnumInterface {
    BLACK_PURPLE("black_purple"),
    BROWN_RED("brown_red"),
    BLACK("black"),
    GRAY("gray"),
    LIGHT_GRAY("light_gray"),
    WHITE("white"),
    BROWN("brown"),
    RED("red"),
    ORANGE("orange"),
    YELLOW("yellow"),
    LIME("lime"),
    GREEN("green"),
    CYAN("cyan"),
    LIGHT_BLUE("light_blue"),
    BLUE("blue"),
    PURPLE("purple"),
    MAGENTA("magenta"),
    PINK("pink")
    ;
    final String NAME;

    SconceColors(String name) {
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
