package net.zephyr.fnafur.blocks.stickers.base;

import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.zephyr.fnafur.FnafUniverseResuited;

public enum Sticker implements StringIdentifiable {
    BLACK_WHITE_RED_WALL_TILES(
            "b_w_r_wall_tiles",
            Movable.VERTICAL,
            22,
            64,
            Identifier.of(FnafUniverseResuited.MOD_ID,"block/stickers/black_white_red_wall_tiles")
    ),
    WALL_GRUNGE(
            "wall_grunge",
    Movable.NONE,
            0,
            64,
            Identifier.of(FnafUniverseResuited.MOD_ID,"block/stickers/wall_grunge_1"),
            Identifier.of(FnafUniverseResuited.MOD_ID,"block/stickers/wall_grunge_2"),
            Identifier.of(FnafUniverseResuited.MOD_ID,"block/stickers/wall_grunge_3"),
            Identifier.of(FnafUniverseResuited.MOD_ID,"block/stickers/wall_grunge_4")
            );

    final String name;
    final Identifier[] ids;
    Movable move;
    final int size;
    final int pixelDensity;
    public static Sticker getSticker(String name) {
        for(Sticker sticker : Sticker.values()){
            if(sticker.name.equals(name)) return sticker;
        }
        return null;
    }
    Sticker(String name, Movable move, int size, int pixelDensity, Identifier... textures){
        this.name = name;
        this.move = move;
        this.size = size;
        this.pixelDensity = pixelDensity;
        this.ids = textures;
    }
    public int getSize() {
        return size;
    }
    public int getPixelDensity() {
        return pixelDensity;
    }
    public Identifier[] getTextures() {
        return ids;
    }
    public Movable getDirection() {
        return move;
    }

    @Override
    public String asString() {
        return name;
    }

    public enum Movable {
        NONE,
        VERTICAL,
        HORIZONTAL;
    }
}
