package net.zephyr.fnafur.init.block_init.Palettes;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;

import java.util.Locale;

public enum TilesPalettes implements PaletteEnum {
    BLACK_WHITE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_white_palette.png")),
    BLACK_BLUE(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/black_blue_palette.png"))
    ;

    public final Identifier PALETTE;

    TilesPalettes(Identifier palette){
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
