package net.zephyr.fnafur.init.decal_init.Palettes;

import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.init.block_init.Palettes.PaletteEnum;

import java.util.Locale;

public enum WallTilesPalettes implements PaletteEnum {
    RED(Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/decals/tiles/red_trim.png"))
    ;

    public final Identifier PALETTE;

    WallTilesPalettes(Identifier palette){
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
