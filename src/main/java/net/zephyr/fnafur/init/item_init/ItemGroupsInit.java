package net.zephyr.fnafur.init.item_init;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.block_init.GeoBlockInit;
import net.zephyr.fnafur.init.block_init.PropInit;

public class ItemGroupsInit {

    Item icon = new Item(new Item.Settings());
    public static final ItemGroup FNAF_PROPS = Registry.register(Registries.ITEM_GROUP, Identifier.of(FnafUniverseRebuilt.MOD_ID, "props"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable(FnafUniverseRebuilt.MOD_ID + ".props"))
                    .noRenderedName()
                    .icon(() -> new ItemStack(PropInit.FLOOR_MONITORS))
                    .entries((displayContext, entries) -> {
                        entries.add(PropInit.FNAF_1_DESK);
                        entries.add(PropInit.WOODEN_CLOCK);
                        entries.add(PropInit.PARTY_TABLE);
                        entries.add(PropInit.PARTY_TABLE_CONFETTI);
                        entries.add(PropInit.PARTY_HAT);
                        entries.add(PropInit.STAR_PLASTIC_CHAIR);
                        entries.add(PropInit.WOODEN_CHAIR);
                        entries.add(PropInit.WALL_PIZZA);
                        entries.add(PropInit.WALL_CLOUDS);
                        entries.add(PropInit.HANGING_STARS);
                        entries.add(PropInit.PRESENT_STACK);
                        entries.add(PropInit.EXIT_SIGN);
                        entries.add(PropInit.LIGHT_SWITCH);
                        entries.add(PropInit.WALL_OUTLET);
                        entries.add(PropInit.PUNCH_IN_CARDS);

                        entries.add(PropInit.FLOOR_MONITORS);
                        entries.add(PropInit.WOODEN_SHELF);
                        entries.add(PropInit.AC_UNIT);
                        entries.add(PropInit.RETRO_TABLE);

                        entries.add(PropInit.AIR_VENT);
                        entries.add(PropInit.CEILING_TILE_VENT);
                        entries.add(PropInit.CEILING_TILE_VENT_BLACK);
                        entries.add(PropInit.RESTROOM_SIGN);
                        entries.add(PropInit.TOILET_PAPER_ROLL);
                        entries.add(PropInit.TOILET);
                        entries.add(PropInit.FLOOR_TRASH);
                        entries.add(PropInit.BROOM);
                        entries.add(PropInit.MOP_BUCKET);
                        entries.add(PropInit.TRASH_BIN);
                        entries.add(PropInit.WET_FLOOR_SIGN);

                        entries.add(PropInit.PIZZA_OVEN);
                        entries.add(PropInit.FRIDGE);
                        entries.add(PropInit.DOUBLE_DOOR_FRIDGE);
                        entries.add(PropInit.KITCHEN_PREP_TABLE);
                        entries.add(PropInit.POTS_AND_PANS_RACK);
                        entries.add(PropInit.WATER_DISPENSER);
                        entries.add(PropInit.ICE_CREAM_DISPENSER);

                        entries.add(PropInit.SKEEBALL_ARCADE);
                        entries.add(GeoBlockInit.PIRATES_COVE_STAGE);
                        entries.add(GeoBlockInit.PIRATES_COVE_CURTAIN);

                        entries.add(BlockInit.WAREHOUSE_SHELF);
                        entries.add(GeoBlockInit.SMALL_GRAY_DOOR);
                        entries.add(GeoBlockInit.TWO_FIVE_RED_DOOR);
                        entries.add(GeoBlockInit.TWO_FIVE_BLACK_DOOR);
                        entries.add(GeoBlockInit.TWO_FIVE_GREEN_DOOR);
                        entries.add(GeoBlockInit.TWO_FIVE_CYAN_DOOR);
                        entries.add(GeoBlockInit.TWO_FIVE_BROWN_DOOR);
                        entries.add(GeoBlockInit.TWO_FIVE_RED_DOOR_WINDOW);
                        entries.add(GeoBlockInit.TWO_FIVE_BLACK_DOOR_WINDOW);
                        entries.add(GeoBlockInit.TWO_FIVE_GREEN_DOOR_WINDOW);
                        entries.add(GeoBlockInit.TWO_FIVE_CYAN_DOOR_WINDOW);
                        entries.add(GeoBlockInit.TWO_FIVE_BROWN_DOOR_WINDOW);
                        entries.add(GeoBlockInit.BIG_GRAY_DOOR);
                        entries.add(GeoBlockInit.BIG_MAGENTA_DOOR);
                        entries.add(GeoBlockInit.BIG_GREEN_DOOR);

                    }).build());
    public static final ItemGroup FNAF_TECHNICAL = Registry.register(Registries.ITEM_GROUP, Identifier.of(FnafUniverseRebuilt.MOD_ID, "technical"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable(FnafUniverseRebuilt.MOD_ID + ".technical"))
                    .noRenderedName()
                    .icon(() -> new ItemStack(ItemInit.PIPE_WRENCH))
                    .entries((displayContext, entries) -> {
                        entries.add(BlockInit.ANIMATRONIC_BLOCK);
                        entries.add(PropInit.COSMO_GIFT);
                        entries.add(ItemInit.DEATHCOIN);
                        entries.add(ItemInit.CPU);
                        entries.add(ItemInit.ILLUSIONDISC);

                        entries.add(ItemInit.PIPE_WRENCH);
                        entries.add(ItemInit.WRENCH);
                        entries.add(ItemInit.PAINTBRUSH);
                        entries.add(ItemInit.SCRAPER);
                        entries.add(ItemInit.TAPEMEASURE);
                        entries.add(ItemInit.JERRYCAN);

                        entries.add(BlockInit.CAMERA);
                        entries.add(ItemInit.TABLET);
                        entries.add(BlockInit.FOG_BLOCK);

                        entries.add(BlockInit.GARAGE_DOOR);
                        entries.add(BlockInit.OFFICE_DOOR);
                        entries.add(PropInit.OFFICE_BUTTONS);

                        entries.add(BlockInit.COMPUTER);
                        entries.add(BlockInit.CPU_CONFIG_PANEL);
                        entries.add(BlockInit.WORKBENCH);

                        entries.add(BlockInit.FUEL_GENERATOR);
                        entries.add(BlockInit.REDSTONE_CONVERTER);
                        entries.add(BlockInit.ELECTRICAL_LOCKER);

                    }).build());
    public static final ItemGroup FNAF_BLOCKS = Registry.register(Registries.ITEM_GROUP, Identifier.of(FnafUniverseRebuilt.MOD_ID, "blocks"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable(FnafUniverseRebuilt.MOD_ID + ".blocks"))
                    .noRenderedName()
                    .icon(() -> new ItemStack(BlockInit.BLACK_WHITE_TILES))
                    .entries((displayContext, entries) -> {

                        // MIMIC FRAMES
                        entries.add(BlockInit.MIMIC_FRAME);
                        entries.add(BlockInit.MIMIC_FRAME_2x2);
                        entries.add(BlockInit.MIMIC_FRAME_4x4);

                        // TILES
                        entries.add(BlockInit.GROUT_TILE);
                        entries.add(BlockInit.DARK_GROUT_TILES);
                        entries.add(BlockInit.BLACK_TILE);
                        entries.add(BlockInit.WHITE_TILE);
                        entries.add(BlockInit.BLACK_WHITE_TILES);
                        entries.add(BlockInit.RED_BLUE_TILES);
                        entries.add(BlockInit.RED_BLACK_TILES);
                        entries.add(BlockInit.BLACK_BLUE_TILES);
                        entries.add(BlockInit.BLACK_PURPLE_TILES);
                        entries.add(BlockInit.EXTRUDED_BLACK_TILES);
                        entries.add(BlockInit.BLUE_TILES);
                        entries.add(BlockInit.BRIGHT_BLUE_TILES);
                        entries.add(BlockInit.BRIGHT_NAVY_BLUE_TILES);
                        entries.add(BlockInit.BRIGHT_GREEN_TILES);
                        entries.add(BlockInit.BRIGHT_WHITE_TILES);
                        entries.add(BlockInit.BRIGHT_BROWN_TILES);
                        entries.add(BlockInit.BRIGHT_MAGENTA_TILES);
                        entries.add(BlockInit.BRIGHT_ORANGE_TILES);
                        entries.add(BlockInit.BRIGHT_PINK_TILES);
                        entries.add(BlockInit.BRIGHT_PURPLE_TILES);
                        entries.add(BlockInit.DARK_BLUE_TILES);
                        entries.add(BlockInit.BLACK_TILES);
                        entries.add(BlockInit.RED_TILES);
                        entries.add(BlockInit.WHITE_TILES);
                        entries.add(BlockInit.BLACK_WHITE_16_TILES);
                        entries.add(BlockInit.BLACK_WHITE_16_TILES_TRIM);
                        entries.add(BlockInit.WHITE_16_CLEAN_TILES);
                        entries.add(BlockInit.WHITE_16_TILES);
                        entries.add(BlockInit.TAN_16_CLEAN_TILES);
                        entries.add(BlockInit.TAN_16_TILES);
                        entries.add(BlockInit.TAN_16_SPACED_TILES);
                        entries.add(BlockInit.TAN_RAINBOW_16_TILES);
                        entries.add(BlockInit.SMALL_BLACK_TILES);
                        entries.add(BlockInit.SMALL_BLACK_RED_TILES);
                        entries.add(BlockInit.SMALL_BLUE_BLACK_TILES);
                        entries.add(BlockInit.SMALL_BLUE_RED_TILES);
                        entries.add(BlockInit.SMALL_CYAN_BLACK_TILES);
                        entries.add(BlockInit.SMALL_DARK_BLUE_TILES);
                        entries.add(BlockInit.SMALL_BIEGE_TILES);
                        entries.add(BlockInit.GREEN_DIRTY_TILES);
                        entries.add(BlockInit.GREEN_DIRTY_TILES_BLACK_LINING);
                        entries.add(BlockInit.GREEN_TILES_BLACK_LINING);
                        entries.add(BlockInit.OFF_WHITE_TILES);
                        entries.add(BlockInit.OFF_WHITE_TILES_DIRTY);
                        entries.add(BlockInit.BLACK_CORNER_BROWN_TILE);
                        entries.add(BlockInit.BLACK_GREEN_DIAGONAL_TILE);
                        entries.add(BlockInit.BRIGHT_YELLOW_TILES);
                        entries.add(BlockInit.POOL_FLOOR_SMALL_TILES);
                        entries.add(BlockInit.BLUE_SMALL_TILES);
                        entries.add(BlockInit.TURQUOISE_SMALL_TILES);
                        entries.add(BlockInit.PURPLE_SMALL_TILES);
                        entries.add(BlockInit.SMALL_STONE_TILES);

                        // GLASS
                        entries.add(BlockInit.TILED_GLASS);
                        entries.add(BlockInit.TILED_GLASS_SLIT);
                        entries.add(BlockInit.TILED_GLASS_COLORED);
                        entries.add(BlockInit.TILED_GLASS_SLIT_COLORED);
                        entries.add(BlockInit.BIG_WINDOW);
                        entries.add(BlockInit.BIG_WINDOW_WHITE);
                        entries.add(BlockInit.BIG_WINDOW_DARK);
                        entries.add(BlockInit.DIRTY_GLASS);

                        // WOOD
                        entries.add(BlockInit.STAGE_PLANKS);
                        entries.add(BlockInit.STAGE_PLANKS_THIN);
                        entries.add(BlockInit.DARK_STAGE_PLANKS);
                        entries.add(BlockInit.DARK_STAGE_PLANKS_THIN);
                        entries.add(BlockInit.LIGHT_STAGE_PLANKS);
                        entries.add(BlockInit.WOODEN_LOWER_WALL);
                        entries.add(BlockInit.WOODEN_LOWER_WALL_TRIMMED);

                        // FLOORS AND CONCRETE
                        entries.add(BlockInit.KITCHEN_FLOOR);
                        entries.add(BlockInit.CONCRETE_FLOOR);
                        entries.add(BlockInit.CONCRETE_FLOOR_DARK);
                        entries.add(BlockInit.CONCRETE_FLOOR_TILE);
                        entries.add(BlockInit.DARK_GRAY_CONCRETE);
                        entries.add(BlockInit.GRAY_CONCRETE_WALL);
                        entries.add(BlockInit.GRAY_CONCRETE_WALL_SPLIT);

                        // CARPETS
                        entries.add(BlockInit.CARPET_STAR_GREEN);
                        entries.add(BlockInit.CARPET_STAR_CYAN);
                        entries.add(BlockInit.CARPET_STAR_BLUE);
                        entries.add(BlockInit.CARPET_STAR_PURPLE);
                        entries.add(BlockInit.CARPET_STAR_PINK);
                        entries.add(BlockInit.CARPET_STAR_RED);
                        entries.add(BlockInit.CARPET_STAR_ORANGE);
                        entries.add(BlockInit.CARPET_STAR_BROWN);
                        entries.add(BlockInit.CARPET_SWIRLY_RED);
                        entries.add(BlockInit.CARPET_CONFETTI);
                        entries.add(BlockInit.CARPET_CONFETTI_FREDBEARS);
                        entries.add(BlockInit.CARPET_SPACE);
                        entries.add(BlockInit.CARPET_TRIANGLE);

                        // WALLS AND BRICKS
                        entries.add(BlockInit.WALL_TILE_FULL);
                        entries.add(BlockInit.GRAY_WALL);
                        entries.add(BlockInit.DARK_GRAY_WALL);
                        entries.add(BlockInit.WHITE_DINER_WALL);
                        entries.add(BlockInit.TAN_DINER_WALL);
                        entries.add(BlockInit.BLACK_BLUE_WALL_TILES);
                        entries.add(BlockInit.BLACK_BLUE_WALL_TILES_TOP);
                        entries.add(BlockInit.BLACK_RED_WALL_TILES);
                        entries.add(BlockInit.BLACK_RED_WALL_TILES_TOP);
                        entries.add(BlockInit.DIRTY_BRICKS);
                        entries.add(BlockInit.BRICK_WALL);
                        entries.add(BlockInit.BRICK_WALL_DARKER);
                        entries.add(BlockInit.RED_BRICK_WALL);
                        entries.add(BlockInit.RED_BRICK_WALL_SMALL);
                        entries.add(BlockInit.RED_BRICK_WALL_MIXED);
                        entries.add(BlockInit.RED_BRICKS_BLACK_GROUT);
                        entries.add(BlockInit.RED_BRICKS_WHITE_GROUT);
                        entries.add(BlockInit.BLACK_BRICKS);
                        entries.add(BlockInit.BLUE_BRICKS);
                        entries.add(BlockInit.BRIGHT_BLUE_BRICKS);
                        entries.add(BlockInit.BRIGHT_NAVY_BLUE_BRICKS);
                        entries.add(BlockInit.BRIGHT_GREEN_BRICKS);
                        entries.add(BlockInit.BRIGHT_WHITE_BRICKS);
                        entries.add(BlockInit.BRIGHT_BROWN_BRICKS);
                        entries.add(BlockInit.BRIGHT_MAGENTA_BRICKS);
                        entries.add(BlockInit.BRIGHT_ORANGE_BRICKS);
                        entries.add(BlockInit.BRIGHT_PINK_BRICKS);
                        entries.add(BlockInit.BRIGHT_PURPLE_BRICKS);
                        entries.add(BlockInit.BLUE_GRAY_BRICKS);
                        entries.add(BlockInit.TEAL_BRICKS);
                        entries.add(BlockInit.GREEN_BRICKS);
                        entries.add(BlockInit.LARGE_BROWN_BRICKS);
                        entries.add(BlockInit.LARGE_LIGHT_GRAY_BRICKS);
                        entries.add(BlockInit.LARGE_WHITE_BRICKS);
                        entries.add(BlockInit.GRAY_BRICKS_WHITE_CEMENT);
                        entries.add(BlockInit.SMALL_GRAY_BRICKS);
                        entries.add(BlockInit.SMALL_LIGHT_GRAY_BRICKS);
                        entries.add(BlockInit.METAL_PLATES);
                        entries.add(BlockInit.ROUGH_METAL_PLATES);
                        entries.add(BlockInit.CEILING_TILE_LIGHT);
                        entries.add(BlockInit.WHITE_CEILING_TILES);
                        entries.add(BlockInit.BLACK_CEILING_TILE);
                        entries.add(BlockInit.CEILING_TILES);
                        entries.add(BlockInit.CEILING_TILES_STAINED);
                        entries.add(BlockInit.DARK_CEILING);
                        entries.add(BlockInit.DARK_CEILING_DETAIL);
                        entries.add(BlockInit.PLAIN_BLACK_BLOCK);
                        entries.add(BlockInit.TAN_THIN_BRICKS);
                        entries.add(BlockInit.VARIED_TAN_THIN_BRICKS);
                        entries.add(BlockInit.BROWN_THIN_BRICKS);
                        entries.add(BlockInit.RED_THIN_BRICKS);
                        entries.add(BlockInit.ORANGE_THIN_BRICKS);
                        entries.add(BlockInit.YELLOW_THIN_BRICKS);
                        entries.add(BlockInit.GREEN_THIN_BRICKS);
                        entries.add(BlockInit.CYAN_THIN_BRICKS);
                        entries.add(BlockInit.PURPLE_THIN_BRICKS);
                        entries.add(BlockInit.MAGENTA_THIN_BRICKS);
                        entries.add(BlockInit.PINK_THIN_BRICKS);
                        entries.add(BlockInit.GRAY_THIN_BRICKS);
                        entries.add(BlockInit.WHITE_THIN_BRICKS);
                        entries.add(BlockInit.MOSAIC_BRICKS);
                        entries.add(BlockInit.GRUNGE_STONE_BRICKS);
                        entries.add(BlockInit.GRUNGE_STONE_BRICKS_DIRTY);
                        entries.add(BlockInit.DARK_TAN_BRICKS);
                        entries.add(BlockInit.LIGHT_TAN_BRICKS);

                        // MISCELLANEOUS
                        entries.add(BlockInit.BALLPIT);

                        entries.add(BlockInit.CHEESE_BLOCK);
                        entries.add(BlockInit.CHEESE_BLOCK_WHITE);

                        // STICKERS
                        entries.add(StickerInit.BLACK_WHITE_RED_WALL_TILES);
                        entries.add(StickerInit.BLACK_WHITE_RED_WALL_BIG_TILES);
                        entries.add(StickerInit.BLACK_WHITE_BLACK_WALL_TILES);
                        entries.add(StickerInit.BLACK_WHITE_BLACK_DIRTY_WALL_TILES);
                        entries.add(StickerInit.COLORED_WHITE_WALL_TILES);
                        entries.add(StickerInit.COLORED_WHITE_LONG_WALL_TILES);
                        entries.add(StickerInit.WALL_GRUNGE);
                        entries.add(StickerInit.WALL_GRUNGE_2);


                    }).build());
    public static final ItemGroup FNAF = Registry.register(Registries.ITEM_GROUP, Identifier.of(FnafUniverseRebuilt.MOD_ID, FnafUniverseRebuilt.MOD_ID),
            FabricItemGroup.builder()
                    .displayName(Text.literal(FnafUniverseRebuilt.MOD_ID))
                    .noRenderedName()
                    .texture(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/tabs_fnaf.png"))
                    .icon(() -> new ItemStack(SpawnItemInit.CL_FRED_SPAWN))
                    .entries((displayContext, entries) -> {
                        entries.add(SpawnItemInit.CL_FRED_SPAWN);
                    }).build());

    public static void registerItemGroups() {
        FnafUniverseRebuilt.LOGGER.info("Registering Item Groups for " + FnafUniverseRebuilt.MOD_ID);
    }
}
