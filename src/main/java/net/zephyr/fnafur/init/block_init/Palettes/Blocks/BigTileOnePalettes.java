package net.zephyr.fnafur.init.block_init.Palettes.Blocks;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.init.block_init.Palettes.PaletteEnum;

import java.util.Locale;

public enum BigTileOnePalettes implements PaletteEnum {
    BLACK_WHITE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_white_palette.png")),
    BLACK_RED(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_red_palette.png")),
    BLACK_RED_LIGHT(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_red_light_palette.png")),
    BLACK_BLUE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_blue_palette.png")),

    BLACK_ROSEBUD_MAROON(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_rosebud_maroon_palette.png")),
    BLACK_ROYAL_MAROON(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_royal_maroon_palette.png")),
    BLACK_CHOCOLATE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_chocolate_palette.png")),
    BLACK_SCARLET_RED(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_scarlet_red_palette.png")),
    BLACK_WARM_ORANGE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_warm_orange_palette.png")),
    BLACK_BRONZE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_bronze_palette.png")),
    BLACK_GOLDEN_YELLOW(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_golden_yellow_palette.png")),
    BLACK_CHIRSTI_GREEN(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_christi_green_palette.png")),
    BLACK_FOREST_GREEN(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_forest_green_palette.png")),
    BLACK_GREEN_BLUE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_green_blue_palette.png")),
    BLACK_TURQUOISE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_turquoise_palette.png")),
    BLACK_ARABIC_BLUE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_arabic_blue_palette.png")),
    BLACK_DARK_WATER_BLUE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_dark_water_blue_palette.png")),
    BLACK_CARNIVAL_PURPLE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_carnival_purple_palette.png")),
    BLACK_VALHALLA_PURPLE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_valhalla_purple_palette.png")),
    BLACK_VIOLET(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_violet_palette.png")),
    BLACK_FROZEN_ROSE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_frozen_rose_palette.png")),
    BLACK_MATTE_MAGENTA(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_matte_magenta_palette.png")),
    BLACK_SWEET_PURPLE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_sweet_purple_palette.png")),
    BLACK_CREAM_BEIGE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_cream_beige_palette.png")),
    BLACK_NEUTRAL_BEIGE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_neutral_beige_palette.png")),
    BLACK_DULL_BROWN(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_dull_brown_palette.png")),
    BLACK_QUINCY_BROWN(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_color/black_quincy_brown_palette.png"))
    ;

    public final Identifier PALETTE;

    BigTileOnePalettes(Identifier palette){
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
