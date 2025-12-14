package net.zephyr.fnafur.init.item_init;

import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.client.gui.screens.creative_menu.ItemCategoriesManager;
import net.zephyr.fnafur.client.gui.screens.creative_menu.ItemCategory;
import net.zephyr.fnafur.init.block_init.BlockInit;

public class ItemCategoriesInit {

    public static final ItemCategory TILES = ItemCategory.create(ItemCategory.builder()
            .setID("tiles")
            .addItems(
                    ItemCategory.Entry.create(BlockInit.BLACK_WHITE_TILES,
                            BlockInit.BLACK_WHITE_TILES,
                            BlockInit.RED_BLUE_TILES,
                            BlockInit.RED_BLACK_TILES,
                            BlockInit.BLACK_BLUE_TILES,
                            BlockInit.DARK_BLUE_TILES,
                            BlockInit.BLACK_TILES,
                            BlockInit.RED_TILES,
                            BlockInit.WHITE_TILES,
                            BlockInit.GROUT_TILE,
                            BlockInit.DARK_GROUT_TILES
                    ),
                    ItemCategory.Entry.create(BlockInit.BLACK_TILE,
                            BlockInit.BLACK_TILE,
                            BlockInit.WHITE_TILE,
                            BlockInit.BLACK_CORNER_BROWN_TILE,
                            BlockInit.BLACK_GREEN_DIAGONAL_TILE,
                            BlockInit.CONCRETE_FLOOR_TILE
                    ),
                    ItemCategory.Entry.create(BlockInit.BLACK_WHITE_16_TILES,
                            BlockInit.BLACK_WHITE_16_TILES,
                            BlockInit.BLACK_WHITE_16_TILES_TRIM,
                            BlockInit.BLACK_BLUE_WALL_TILES,
                            BlockInit.BLACK_BLUE_WALL_TILES_TOP,
                            BlockInit.BLACK_RED_WALL_TILES,
                            BlockInit.BLACK_RED_WALL_TILES_TOP
                    ),
                    ItemCategory.Entry.create(BlockInit.TAN_16_CLEAN_TILES,
                            BlockInit.TAN_16_CLEAN_TILES,
                            BlockInit.TAN_16_SPACED_TILES,
                            BlockInit.TAN_RAINBOW_16_TILES,
                            BlockInit.TAN_16_TILES,
                            BlockInit.BLUE_TILES,
                            BlockInit.GREEN_TILES_BLACK_LINING,
                            BlockInit.EXTRUDED_BLACK_TILES,
                            BlockInit.KITCHEN_FLOOR
                    ),
                    ItemCategory.Entry.create(BlockInit.BRIGHT_YELLOW_TILES,
                            BlockInit.BRIGHT_PURPLE_TILES,
                            BlockInit.BRIGHT_NAVY_BLUE_TILES,
                            BlockInit.BRIGHT_BLUE_TILES,
                            BlockInit.BRIGHT_GREEN_TILES,
                            BlockInit.OFF_WHITE_TILES,
                            BlockInit.BRIGHT_YELLOW_TILES,
                            BlockInit.BRIGHT_WHITE_TILES,
                            BlockInit.BRIGHT_PINK_TILES,
                            BlockInit.BRIGHT_MAGENTA_TILES,
                            BlockInit.BRIGHT_ORANGE_TILES,
                            BlockInit.BRIGHT_BROWN_TILES,
                            BlockInit.WHITE_16_CLEAN_TILES,
                            BlockInit.WHITE_16_TILES
                    ),
                    ItemCategory.Entry.create(BlockInit.SMALL_BLACK_TILES,
                            BlockInit.SMALL_BLACK_TILES,
                            BlockInit.SMALL_BLACK_RED_TILES,
                            BlockInit.SMALL_BLUE_BLACK_TILES,
                            BlockInit.SMALL_BLUE_RED_TILES,
                            BlockInit.SMALL_CYAN_BLACK_TILES,
                            BlockInit.SMALL_DARK_BLUE_TILES,
                            BlockInit.SMALL_BIEGE_TILES,
                            BlockInit.POOL_FLOOR_SMALL_TILES,
                            BlockInit.BLUE_SMALL_TILES,
                            BlockInit.TURQUOISE_SMALL_TILES,
                            BlockInit.PURPLE_SMALL_TILES,
                            BlockInit.DARK_TAN_BRICKS,
                            BlockInit.SMALL_STONE_TILES
                    ),
                    ItemCategory.Entry.create(BlockInit.CEILING_TILES,
                            BlockInit.CEILING_TILES,
                            BlockInit.CEILING_TILES_STAINED,
                            BlockInit.BLACK_CEILING_TILE,
                            BlockInit.WHITE_CEILING_TILES,
                            BlockInit.CEILING_TILE_LIGHT
                    ),
                    ItemCategory.Entry.create(BlockInit.MOSAIC_BRICKS,
                            BlockInit.MOSAIC_BRICKS
                    )
            )
            .build()
    );

    public static void registerItemCategories() {
        FnafUniverseRebuilt.LOGGER.info("Registering Item Categories for " + FnafUniverseRebuilt.MOD_ID);
    }
}
