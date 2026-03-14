package net.zephyr.fnafur.init.block_init.Palettes.Blocks;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.init.block_init.Palettes.PaletteEnum;

import java.util.Locale;

public enum WallPalettes implements PaletteEnum {
    GRAY_1(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/gray_1.png")),
    GRAY_2(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/gray_2.png")),
    GRAY_3(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/gray_3.png")),
    GRAY_4(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/gray_4.png")),
    GRAY_5(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/gray_5.png")),
    BLUE_1(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/blue_1.png")),
    BLUE_2(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/blue_2.png")),
    BLUE_3(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/blue_3.png")),
    BLUE_4(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/blue_4.png")),
    BLUE_5(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/blue_5.png")),
    WARM_1(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/warm_1.png")),
    WARM_2(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/warm_2.png")),
    WARM_3(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/warm_3.png")),
    WARM_4(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/warm_4.png")),
    WARM_5(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/warm_5.png")),
    YELLOW_1(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/yellow_1.png")),
    YELLOW_2(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/yellow_2.png")),
    YELLOW_3(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/yellow_3.png")),
    YELLOW_4(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/yellow_4.png")),
    YELLOW_5(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/yellow_5.png"))
    ;

    public final Identifier PALETTE;

    WallPalettes(Identifier palette){
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
