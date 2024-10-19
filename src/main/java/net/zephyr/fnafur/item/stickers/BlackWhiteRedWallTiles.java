package net.zephyr.fnafur.item.stickers;

import net.zephyr.fnafur.item.stickers.base.StickerItem;

public class BlackWhiteRedWallTiles extends StickerItem {
    public BlackWhiteRedWallTiles(Settings settings) {
        super(settings);
    }

    @Override
    public String sticker_name() {
        return "b_w_r_wall_tiles";
    }

    @Override
    public boolean isWallSticker() {
        return true;
    }

    @Override
    public boolean isStackable() {
        return true;
    }

    @Override
    public float mouseOffset() {
        return 0.75f;
    }

}
