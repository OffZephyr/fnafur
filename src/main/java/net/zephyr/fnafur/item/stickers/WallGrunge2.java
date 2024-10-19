package net.zephyr.fnafur.item.stickers;

import net.zephyr.fnafur.item.stickers.base.StickerItem;

public class WallGrunge2 extends StickerItem {
    public WallGrunge2(Settings settings) {
        super(settings);
    }

    @Override
    public String sticker_name() {
        return "wall_grunge_2";
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
        return 0;
    }
}
