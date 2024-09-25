package net.zephyr.fnafur.init.item_init;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.item.stickers.BlackWhiteRedWallTiles;
import net.zephyr.fnafur.item.stickers.WallGrunge;

public class StickerInit {
    public static final Item BLACK_WHITE_RED_WALL_TILES = registerItem("sticker_black_white_red_wall_tiles",
            new BlackWhiteRedWallTiles(new Item.Settings().maxCount(1).rarity(Rarity.COMMON)));
    public static final Item WALL_GRUNGE = registerItem("sticker_wall_grunge",
            new WallGrunge(new Item.Settings().maxCount(1).rarity(Rarity.COMMON)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(FnafUniverseResuited.MOD_ID, name), item);
    }
    public static void registerStickers() {
        FnafUniverseResuited.LOGGER.info("Registering Sticker Items for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }
}
