package net.zephyr.fnafur.item.stickers;

import net.zephyr.fnafur.item.stickers.base.StickerItem;

public class WallGrunge extends StickerItem {
    public WallGrunge(Settings settings) {
        super(settings);
    }

    @Override
    public String sticker_name() {
        return "wall_grunge";
    }

    @Override
    public boolean isWallSticker() {
        return true;
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
