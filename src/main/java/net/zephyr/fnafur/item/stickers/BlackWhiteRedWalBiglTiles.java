package net.zephyr.fnafur.item.stickers;

import net.zephyr.fnafur.item.stickers.base.StickerItem;

public class BlackWhiteRedWalBiglTiles extends StickerItem {
    public BlackWhiteRedWalBiglTiles(Settings settings) {
        super(settings);
    }

    @Override
    public String sticker_name() {
        return "b_w_r_wall_big_tiles";
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
        return 0.55f;
    }

}
