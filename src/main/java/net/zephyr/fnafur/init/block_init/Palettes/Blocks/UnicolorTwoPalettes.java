package net.zephyr.fnafur.init.block_init.Palettes.Blocks;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.init.block_init.Palettes.PaletteEnum;

import java.util.Locale;

public enum UnicolorTwoPalettes implements PaletteEnum {
    BLACK_WHITE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_white_palette.png")),
    RED_BLUE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/red_blue_palette.png")),
    RED_BLUE_LIGHT(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/red_blue_light_palette.png")),

    WHITE_ROSEBUD_MAROON(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_rosebud_maroon_palette.png")),
    WHITE_ROYAL_MAROON(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_royal_maroon_palette.png")),
    WHITE_CHOCOLATE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_chocolate_palette.png")),
    WHITE_SCARLET_RED(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_scarlet_red_palette.png")),
    WHITE_WARM_ORANGE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_warm_orange_palette.png")),
    WHITE_BRONZE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_bronze_palette.png")),
    WHITE_GOLDEN_YELLOW(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_golden_yellow_palette.png")),
    WHITE_CHIRSTI_GREEN(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_christi_green_palette.png")),
    WHITE_FOREST_GREEN(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_forest_green_palette.png")),
    WHITE_GREEN_BLUE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_green_blue_palette.png")),
    WHITE_TURQUOISE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_turquoise_palette.png")),
    WHITE_ARABIC_BLUE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_arabic_blue_palette.png")),
    WHITE_DARK_WATER_BLUE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_dark_water_blue_palette.png")),
    WHITE_CARNIVAL_PURPLE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_carnival_purple_palette.png")),
    WHITE_VALHALLA_PURPLE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_valhalla_purple_palette.png")),
    WHITE_VIOLET(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_violet_palette.png")),
    WHITE_FROZEN_ROSE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_frozen_rose_palette.png")),
    WHITE_MATTE_MAGENTA(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_matte_magenta_palette.png")),
    WHITE_SWEET_PURPLE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_sweet_purple_palette.png")),
    WHITE_CREAM_BEIGE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_cream_beige_palette.png")),
    WHITE_NEUTRAL_BEIGE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_neutral_beige_palette.png")),
    WHITE_DULL_BROWN(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_dull_brown_palette.png")),
    WHITE_QUINCY_BROWN(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/white_color/white_quincy_brown_palette.png"))
    ;

    public final Identifier PALETTE;

    UnicolorTwoPalettes(Identifier palette){
        this.PALETTE = palette;
    }

    @Override
    public String getName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    @Override
    public Identifier getPalette() {
        return PALETTE;
    }
}
