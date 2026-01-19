package net.zephyr.fnafur.init.block_init.Palettes.Blocks;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.init.block_init.Palettes.PaletteEnum;

import java.util.Locale;

public enum WallpaperPalettes implements PaletteEnum {
    LIGHT_GRAY(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/wallpaper/light_gray.png")),
    DARK_GRAY(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/wallpaper/dark_gray.png"))
    ;

    public final Identifier PALETTE;

    WallpaperPalettes(Identifier palette){
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
