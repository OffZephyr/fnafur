package net.zephyr.fnafur.init.item_init;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.item.StickerItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class StickerInit {

    public static Map<String, Sticker> STICKERS = new HashMap<>();
    public static final Item BLACK_WHITE_RED_WALL_TILES = registerSticker(
            "sticker_black_white_red_wall_tiles",
            StickerItem::new,
            new Item.Settings().maxCount(1).rarity(Rarity.COMMON),
            "b_w_r_wall_tiles",
            5.5f,
            16,
            0.75f,
            Movable.VERTICAL,
            Identifier.of(FnafUniverseResuited.MOD_ID, "block/stickers/black_white_red_wall_tiles")
    );
    public static final Item BLACK_WHITE_RED_WALL_BIG_TILES = registerSticker(
            "sticker_black_white_red_wall_big_tiles",
            StickerItem::new,
            new Item.Settings().maxCount(1).rarity(Rarity.COMMON),
            "b_w_r_wall_big_tiles",
            1.75f,
            16,
            0.55f,
            Movable.VERTICAL,
            Identifier.of(FnafUniverseResuited.MOD_ID, "block/stickers/black_white_red_wall_big_tiles")
    );
    public static final Item WALL_GRUNGE = registerSticker(
            "sticker_wall_grunge",
            StickerItem::new,
            new Item.Settings().maxCount(1).rarity(Rarity.COMMON),
            "wall_grunge",
            0,
            16,
            Identifier.of(FnafUniverseResuited.MOD_ID, "block/stickers/wall_grunge_1"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "block/stickers/wall_grunge_2"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "block/stickers/wall_grunge_3"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "block/stickers/wall_grunge_4")
    );
    public static final Item WALL_GRUNGE_2 = registerSticker(
            "sticker_wall_grunge_2",
            StickerItem::new,
            new Item.Settings().maxCount(1).rarity(Rarity.COMMON),
            "wall_grunge_2",
            0,
            16,
            Identifier.of(FnafUniverseResuited.MOD_ID, "block/stickers/wall_grunge_flat")
    );
    public static final Item COLORED_WHITE_WALL_TILES = registerSticker(
            "sticker_colored_white_wall_tiles",
            StickerItem::new,
            new Item.Settings().maxCount(1).rarity(Rarity.COMMON),
            "c_w_wall_tiles",
            9,
            16,
            0.75f,
            Movable.VERTICAL,
            Identifier.of(FnafUniverseResuited.MOD_ID, "block/stickers/colored_white_wall_tiles")
    );
    public static final Item COLORED_WHITE_LONG_WALL_TILES = registerSticker(
            "sticker_colored_white_long_wall_tiles",
            StickerItem::new,
            new Item.Settings().maxCount(1).rarity(Rarity.COMMON),
            "c_w_long_wall_tiles",
            9,
            16,
            0.75f,
            Movable.VERTICAL,
            Identifier.of(FnafUniverseResuited.MOD_ID, "block/stickers/colored_white_long_wall_tiles")
    );

    private static Item registerSticker(String itemName, Function<Item.Settings, Item> factory, Item.Settings settings, String stickerName, float size, int pixelDensity, Identifier... stickerTexture) {
        return registerSticker(itemName, factory, settings, stickerName, size, pixelDensity, 0, Movable.NONE, true, true, stickerTexture);
    }

    private static Item registerSticker(String itemName, Function<Item.Settings, Item> factory, Item.Settings settings, String stickerName, float size, int pixelDensity, float mouseOffset, Identifier... stickerTexture) {
        return registerSticker(itemName, factory, settings, stickerName, size, pixelDensity, mouseOffset, Movable.NONE, true, true, stickerTexture);
    }

    private static Item registerSticker(String itemName, Function<Item.Settings, Item> factory, Item.Settings settings, String stickerName, float size, int pixelDensity, Movable direction, Identifier... stickerTexture) {
        return registerSticker(itemName, factory, settings, stickerName, size, pixelDensity, 0, direction, true, true, stickerTexture);
    }

    private static Item registerSticker(String itemName, Function<Item.Settings, Item> factory, Item.Settings settings, String stickerName, float size, int pixelDensity, float mouseOffset, Movable direction, Identifier... stickerTexture) {
        return registerSticker(itemName, factory, settings, stickerName, size, pixelDensity, mouseOffset, direction, true, true, stickerTexture);
    }

    private static Item registerSticker(String itemName, Function<Item.Settings, Item> factory, Item.Settings settings, String stickerName, float size, int pixelDensity, float mouseOffset, Movable direction, boolean isWallSticker, Identifier... stickerTexture) {
        return registerSticker(itemName, factory, settings, stickerName, size, pixelDensity, mouseOffset, direction, isWallSticker, true, stickerTexture);
    }

    private static Item registerSticker(String itemName, Function<Item.Settings, Item> factory, Item.Settings settings, String stickerName, float size, int pixelDensity, float mouseOffset, Movable direction, boolean isWallSticker, boolean isStackable, Identifier... stickerTexture) {
        STICKERS.put(stickerName, new Sticker(stickerName, direction, size, pixelDensity, stickerTexture));

        final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(FnafUniverseResuited.MOD_ID, itemName));
        return ((StickerItem) Items.register(registryKey, factory, settings)).info(stickerName, mouseOffset, isWallSticker, isStackable);
    }

    public static Sticker getSticker(String name){
        return STICKERS.get(name);
    }

    public static void registerStickers() {
        FnafUniverseResuited.LOGGER.info("Registering Sticker Items for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }

    public record Sticker(String name, Movable move, float size, int pixelDensity, Identifier... ids) {
        public float getSize() {
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
    }

    public enum Movable {
        NONE,
        VERTICAL,
        HORIZONTAL;
    }
}
