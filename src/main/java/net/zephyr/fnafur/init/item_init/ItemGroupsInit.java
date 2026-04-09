package net.zephyr.fnafur.init.item_init;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.block_init.GeoBlockInit;
import net.zephyr.fnafur.init.block_init.PropInit;

public class ItemGroupsInit {

    Item icon = new Item(new Item.Properties());
    public static final CreativeModeTab FNAF_PROPS = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "props"),
            FabricItemGroup.builder()
                    .title(Component.translatable(FnafUniverseRebuilt.MOD_ID + ".props"))
                    .hideTitle()
                    .icon(() -> new ItemStack(PropInit.PRESENT_STACK))
                    .displayItems((displayContext, entries) -> {
                        entries.accept(PropInit.FNAF_1_DESK);
                        entries.accept(BlockInit.GARAGE_DOOR);
                        entries.accept(BlockInit.HEAVY_DOOR);
                        entries.accept(BlockInit.WARNING_HEAVY_DOOR);
                        entries.accept(PropInit.OFFICE_BUTTONS);
                        entries.accept(PropInit.WOODEN_CLOCK);
                        entries.accept(BlockInit.BACKSTAGE_SHELF);
                        entries.accept(PropInit.FLYING_V_GUITAR);
                        entries.accept(PropInit.STANDING_MICROPHONE);
                        entries.accept(PropInit.STANDING_SPEAKER);
                        entries.accept(PropInit.STANDING_PIANO);
                        entries.accept(PropInit.SPEAKER);
                        entries.accept(PropInit.PLUSHIE);
                        entries.accept(PropInit.RECEPTION_COUNTER);
                        entries.accept(PropInit.PARTY_TABLE);
                        entries.accept(PropInit.PARTY_TABLE_CONFETTI);
                        entries.accept(PropInit.PARTY_HAT);
                        entries.accept(PropInit.STAR_PLASTIC_CHAIR);
                        entries.accept(PropInit.WOODEN_CHAIR);
                        entries.accept(PropInit.WOODEN_STOOL);
                        entries.accept(PropInit.SPOT_LIGHT);
                        entries.accept(PropInit.SCONCE);
                        entries.accept(PropInit.HANGING_LIGHT);
                        entries.accept(PropInit.HANGING_LIGHTBULB);
                        entries.accept(PropInit.WALL_PIZZA);
                        entries.accept(PropInit.WALL_CLOUDS);
                        entries.accept(PropInit.STAGE_SUN);
                        entries.accept(PropInit.HANGING_STARS);
                        entries.accept(PropInit.PRESENT_STACK);
                        entries.accept(PropInit.EXIT_SIGN);
                        entries.accept(PropInit.EXIT_ARROW);
                        entries.accept(PropInit.LIGHT_SWITCH);
                        entries.accept(PropInit.WALL_OUTLET);
                        entries.accept(PropInit.PUNCH_IN_CARDS);
                        entries.accept(PropInit.BULLETIN_BOARD);

                        entries.accept(PropInit.FLOOR_MONITORS);
                        entries.accept(PropInit.WOODEN_SHELF);
                        entries.accept(PropInit.AC_UNIT);
                        entries.accept(PropInit.RETRO_TABLE);
                        entries.accept(PropInit.RETRO_STOOL);

                        entries.accept(PropInit.AIR_VENT);
                        entries.accept(PropInit.CEILING_TILE_VENT);
                        entries.accept(PropInit.CEILING_TILE_VENT_BLACK);
                        entries.accept(PropInit.RESTROOM_SIGN);
                        entries.accept(PropInit.TOILET_PAPER_ROLL);
                        entries.accept(PropInit.TOILET);
                        entries.accept(PropInit.URINAL);
                        entries.accept(PropInit.BATHROOM_SINK);
                        entries.accept(PropInit.FLOOR_TRASH);
                        entries.accept(PropInit.BROOM);
                        entries.accept(PropInit.MOP_BUCKET);
                        entries.accept(PropInit.TRASH_BIN);
                        entries.accept(PropInit.WET_FLOOR_SIGN);
                        entries.accept(PropInit.FLOOR_MAT);

                        entries.accept(PropInit.FOOD_DISPLAY_CASE);
                        entries.accept(PropInit.CONDIMENT_COUNTER);
                        entries.accept(PropInit.UTENSILS_BOX);
                        entries.accept(PropInit.STANDING_MENU);
                        entries.accept(PropInit.WALL_MENU);
                        entries.accept(PropInit.PIZZA_OVEN);
                        entries.accept(PropInit.FRIDGE);
                        entries.accept(PropInit.DOUBLE_DOOR_FRIDGE);
                        entries.accept(PropInit.KITCHEN_PREP_TABLE);
                        entries.accept(PropInit.POTS_AND_PANS_RACK);
                        entries.accept(PropInit.WATER_DISPENSER);
                        entries.accept(PropInit.ICE_CREAM_DISPENSER);

                        entries.accept(PropInit.ATM);
                        entries.accept(PropInit.ARCADE_CABINET);
                        entries.accept(PropInit.SKEEBALL_ARCADE);
                        entries.accept(PropInit.POSTER);
                        entries.accept(PropInit.FNAF1_RULES);
                        entries.accept(PropInit.WALL_PAPERS);
                        entries.accept(GeoBlockInit.PIRATES_COVE_STAGE);
                        entries.accept(GeoBlockInit.PIRATES_COVE_CURTAIN);

                        entries.accept(PropInit.FILING_CABINET);
                        entries.accept(PropInit.WOODEN_CRATE);
                        entries.accept(PropInit.TOOL_WALL_MOUNT);
                        entries.accept(BlockInit.WAREHOUSE_SHELF);
                        entries.accept(GeoBlockInit.SMALL_GRAY_DOOR);
                        entries.accept(GeoBlockInit.TWO_FIVE_RED_DOOR);
                        entries.accept(GeoBlockInit.TWO_FIVE_BLACK_DOOR);
                        entries.accept(GeoBlockInit.TWO_FIVE_GREEN_DOOR);
                        entries.accept(GeoBlockInit.TWO_FIVE_CYAN_DOOR);
                        entries.accept(GeoBlockInit.TWO_FIVE_BROWN_DOOR);
                        entries.accept(GeoBlockInit.TWO_FIVE_RED_DOOR_WINDOW);
                        entries.accept(GeoBlockInit.TWO_FIVE_BLACK_DOOR_WINDOW);
                        entries.accept(GeoBlockInit.TWO_FIVE_GREEN_DOOR_WINDOW);
                        entries.accept(GeoBlockInit.TWO_FIVE_CYAN_DOOR_WINDOW);
                        entries.accept(GeoBlockInit.TWO_FIVE_BROWN_DOOR_WINDOW);
                        entries.accept(GeoBlockInit.BIG_GRAY_DOOR);
                        entries.accept(GeoBlockInit.BIG_MAGENTA_DOOR);
                        entries.accept(GeoBlockInit.BIG_GREEN_DOOR);
                        entries.accept(PropInit.SERIOUS_CUTOUT);

                    }).build());
    public static final CreativeModeTab FNAF_TECHNICAL = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "technical"),
            FabricItemGroup.builder()
                    .title(Component.translatable(FnafUniverseRebuilt.MOD_ID + ".technical"))
                    .hideTitle()
                    .icon(() -> new ItemStack(ItemInit.PIPE_WRENCH))
                    .displayItems((displayContext, entries) -> {

                        entries.accept(ItemInit.VANNI_MASK);
                        entries.accept(ItemInit.PIPE_WRENCH);
                        entries.accept(ItemInit.PAINTBRUSH);
                        entries.accept(ItemInit.SCRAPER);
                        entries.accept(ItemInit.DECAL_BOOK);
                        entries.accept(ItemInit.FLASHLIGHT);
                        //entries.add(ItemInit.TAPEMEASURE);

                        entries.accept(ItemInit.ANIMATRONIC_SUIT);
                        entries.accept(ItemInit.CPU);
                        entries.accept(PropInit.COSMO_GIFT);
                        entries.accept(ItemInit.DEATHCOIN);
                        entries.accept(BlockInit.WORKBENCH);
                        entries.accept(BlockInit.CPU_CONFIG_PANEL);
                        entries.accept(BlockInit.TRIGGER_BLOCK);
                        entries.accept(BlockInit.CHIP_READER);
                        entries.accept(BlockInit.SERVER_MONITOR);
                        entries.accept(BlockInit.VENT);
                        //entries.add(ItemInit.ILLUSIONDISC);

                        // MIMIC FRAMES
                        entries.accept(BlockInit.MIMIC_FRAME);
                        entries.accept(BlockInit.MIMIC_FRAME_2x2);
                        entries.accept(BlockInit.MIMIC_FRAME_4x4);
                        entries.accept(BlockInit.MIMIC_FRAME_DIAGONAL);
                        entries.accept(BlockInit.CURTAIN_TEST);

                        //entries.add(BlockInit.CAMERA);
                        //entries.add(ItemInit.TABLET);
                        //entries.add(BlockInit.FOG_BLOCK);

                        //entries.add(BlockInit.COMPUTER);

                        entries.accept(ItemInit.JERRYCAN);
                        entries.accept(BlockInit.FUEL_GENERATOR);
                        entries.accept(BlockInit.REDSTONE_CONVERTER);
                        entries.accept(BlockInit.ELECTRICAL_LOCKER);
                    }).build());
//    public static final ItemGroup FNAF_BLOCKS = Registry.register(Registries.ITEM_GROUP, Identifier.of(FnafUniverseRebuilt.MOD_ID, "blocks"),
//            FabricItemGroup.builder()
//                    .displayName(Text.translatable(FnafUniverseRebuilt.MOD_ID + ".blocks"))
//                    .noRenderedName()
//                    .icon(() -> new ItemStack(BlockInit.BLACK_WHITE_TILES))
//                    .entries((displayContext, entries) -> {
//
//                        // TILES
//                        entries.add(BlockInit.GROUT_TILE);
//                        entries.add(BlockInit.DARK_GROUT_TILES);
//                        entries.add(BlockInit.BLACK_TILE);
//                        entries.add(BlockInit.WHITE_TILE);
//                        entries.add(BlockInit.BLACK_WHITE_TILES);
//                        entries.add(BlockInit.RED_BLUE_TILES);
//                        entries.add(BlockInit.RED_BLACK_TILES);
//                        entries.add(BlockInit.BLACK_BLUE_TILES);
//                        entries.add(BlockInit.BLACK_PURPLE_TILES);
//                        entries.add(BlockInit.EXTRUDED_BLACK_TILES);
//                        entries.add(BlockInit.BLUE_TILES);
//                        entries.add(BlockInit.BRIGHT_BLUE_TILES);
//                        entries.add(BlockInit.BRIGHT_NAVY_BLUE_TILES);
//                        entries.add(BlockInit.BRIGHT_GREEN_TILES);
//                        entries.add(BlockInit.BRIGHT_WHITE_TILES);
//                        entries.add(BlockInit.BRIGHT_BROWN_TILES);
//                        entries.add(BlockInit.BRIGHT_MAGENTA_TILES);
//                        entries.add(BlockInit.BRIGHT_ORANGE_TILES);
//                        entries.add(BlockInit.BRIGHT_PINK_TILES);
//                        entries.add(BlockInit.BRIGHT_PURPLE_TILES);
//                        entries.add(BlockInit.DARK_BLUE_TILES);
//                        entries.add(BlockInit.BLACK_TILES);
//                        entries.add(BlockInit.RED_TILES);
//                        entries.add(BlockInit.WHITE_TILES);
//                        entries.add(BlockInit.BLACK_WHITE_16_TILES);
//                        entries.add(BlockInit.BLACK_WHITE_16_TILES_TRIM);
//                        entries.add(BlockInit.WHITE_16_CLEAN_TILES);
//                        entries.add(BlockInit.WHITE_16_TILES);
//                        entries.add(BlockInit.TAN_16_CLEAN_TILES);
//                        entries.add(BlockInit.TAN_16_TILES);
//                        entries.add(BlockInit.TAN_16_SPACED_TILES);
//                        entries.add(BlockInit.TAN_RAINBOW_16_TILES);
//                        entries.add(BlockInit.SMALL_BLACK_TILES);
//                        entries.add(BlockInit.SMALL_BLACK_RED_TILES);
//                        entries.add(BlockInit.SMALL_BLUE_BLACK_TILES);
//                        entries.add(BlockInit.SMALL_BLUE_RED_TILES);
//                        entries.add(BlockInit.SMALL_CYAN_BLACK_TILES);
//                        entries.add(BlockInit.SMALL_DARK_BLUE_TILES);
//                        entries.add(BlockInit.SMALL_BIEGE_TILES);
//                        entries.add(BlockInit.GREEN_DIRTY_TILES);
//                        entries.add(BlockInit.GREEN_DIRTY_TILES_BLACK_LINING);
//                        entries.add(BlockInit.GREEN_TILES_BLACK_LINING);
//                        entries.add(BlockInit.OFF_WHITE_TILES);
//                        entries.add(BlockInit.OFF_WHITE_TILES_DIRTY);
//                        entries.add(BlockInit.BLACK_CORNER_BROWN_TILE);
//                        entries.add(BlockInit.BLACK_GREEN_DIAGONAL_TILE);
//                        entries.add(BlockInit.BRIGHT_YELLOW_TILES);
//                        entries.add(BlockInit.POOL_FLOOR_SMALL_TILES);
//                        entries.add(BlockInit.BLUE_SMALL_TILES);
//                        entries.add(BlockInit.TURQUOISE_SMALL_TILES);
//                        entries.add(BlockInit.PURPLE_SMALL_TILES);
//                        entries.add(BlockInit.SMALL_STONE_TILES);
//
//                        // GLASS
//                        entries.add(BlockInit.TILED_GLASS);
//                        entries.add(BlockInit.TILED_GLASS_SLIT);
//                        entries.add(BlockInit.TILED_GLASS_COLORED);
//                        entries.add(BlockInit.TILED_GLASS_SLIT_COLORED);
//                        entries.add(BlockInit.BIG_WINDOW);
//                        entries.add(BlockInit.BIG_WINDOW_WHITE);
//                        entries.add(BlockInit.BIG_WINDOW_DARK);
//                        entries.add(BlockInit.DIRTY_GLASS);
//
//                        // WOOD
//                        entries.add(BlockInit.STAGE_PLANKS);
//                        entries.add(BlockInit.STAGE_PLANKS_THIN);
//                        entries.add(BlockInit.DARK_STAGE_PLANKS);
//                        entries.add(BlockInit.DARK_STAGE_PLANKS_THIN);
//                        entries.add(BlockInit.LIGHT_STAGE_PLANKS);
//                        entries.add(BlockInit.WOODEN_LOWER_WALL);
//                        entries.add(BlockInit.WOODEN_LOWER_WALL_TRIMMED);
//
//                        // FLOORS AND CONCRETE
//                        entries.add(BlockInit.KITCHEN_FLOOR);
//                        entries.add(BlockInit.CONCRETE_FLOOR);
//                        entries.add(BlockInit.CONCRETE_FLOOR_DARK);
//                        entries.add(BlockInit.CONCRETE_FLOOR_TILE);
//                        entries.add(BlockInit.DARK_GRAY_CONCRETE);
//                        entries.add(BlockInit.GRAY_CONCRETE_WALL);
//                        entries.add(BlockInit.GRAY_CONCRETE_WALL_SPLIT);
//
//                        // CARPETS
//                        entries.add(BlockInit.CARPET_STAR_GREEN);
//                        entries.add(BlockInit.CARPET_STAR_CYAN);
//                        entries.add(BlockInit.CARPET_STAR_BLUE);
//                        entries.add(BlockInit.CARPET_STAR_PURPLE);
//                        entries.add(BlockInit.CARPET_STAR_PINK);
//                        entries.add(BlockInit.CARPET_STAR_RED);
//                        entries.add(BlockInit.CARPET_STAR_ORANGE);
//                        entries.add(BlockInit.CARPET_STAR_BROWN);
//                        entries.add(BlockInit.CARPET_SWIRLY_RED);
//                        entries.add(BlockInit.CARPET_CONFETTI);
//                        entries.add(BlockInit.CARPET_CONFETTI_FREDBEARS);
//                        entries.add(BlockInit.CARPET_SPACE);
//                        entries.add(BlockInit.CARPET_TRIANGLE);
//
//                        // WALLS AND BRICKS
//                        entries.add(BlockInit.WALL_TILE_FULL);
//                        entries.add(BlockInit.GRAY_WALL);
//                        entries.add(BlockInit.DARK_GRAY_WALL);
//                        entries.add(BlockInit.WHITE_DINER_WALL);
//                        entries.add(BlockInit.TAN_DINER_WALL);
//                        entries.add(BlockInit.BLACK_BLUE_WALL_TILES);
//                        entries.add(BlockInit.BLACK_BLUE_WALL_TILES_TOP);
//                        entries.add(BlockInit.BLACK_RED_WALL_TILES);
//                        entries.add(BlockInit.BLACK_RED_WALL_TILES_TOP);
//                        entries.add(BlockInit.DIRTY_BRICKS);
//                        entries.add(BlockInit.BRICK_WALL);
//                        entries.add(BlockInit.BRICK_WALL_DARKER);
//                        entries.add(BlockInit.RED_BRICK_WALL);
//                        entries.add(BlockInit.RED_BRICK_WALL_SMALL);
//                        entries.add(BlockInit.RED_BRICK_WALL_MIXED);
//                        entries.add(BlockInit.RED_BRICKS_BLACK_GROUT);
//                        entries.add(BlockInit.RED_BRICKS_WHITE_GROUT);
//                        entries.add(BlockInit.BLACK_BRICKS);
//                        entries.add(BlockInit.BLUE_BRICKS);
//                        entries.add(BlockInit.BRIGHT_BLUE_BRICKS);
//                        entries.add(BlockInit.BRIGHT_NAVY_BLUE_BRICKS);
//                        entries.add(BlockInit.BRIGHT_GREEN_BRICKS);
//                        entries.add(BlockInit.BRIGHT_WHITE_BRICKS);
//                        entries.add(BlockInit.BRIGHT_BROWN_BRICKS);
//                        entries.add(BlockInit.BRIGHT_MAGENTA_BRICKS);
//                        entries.add(BlockInit.BRIGHT_ORANGE_BRICKS);
//                        entries.add(BlockInit.BRIGHT_PINK_BRICKS);
//                        entries.add(BlockInit.BRIGHT_PURPLE_BRICKS);
//                        entries.add(BlockInit.BLUE_GRAY_BRICKS);
//                        entries.add(BlockInit.TEAL_BRICKS);
//                        entries.add(BlockInit.GREEN_BRICKS);
//                        entries.add(BlockInit.LARGE_BROWN_BRICKS);
//                        entries.add(BlockInit.LARGE_LIGHT_GRAY_BRICKS);
//                        entries.add(BlockInit.LARGE_WHITE_BRICKS);
//                        entries.add(BlockInit.GRAY_BRICKS_WHITE_CEMENT);
//                        entries.add(BlockInit.SMALL_GRAY_BRICKS);
//                        entries.add(BlockInit.SMALL_LIGHT_GRAY_BRICKS);
//                        entries.add(BlockInit.METAL_PLATES);
//                        entries.add(BlockInit.ROUGH_METAL_PLATES);
//                        entries.add(BlockInit.CEILING_TILE_LIGHT);
//                        entries.add(BlockInit.WHITE_CEILING_TILES);
//                        entries.add(BlockInit.BLACK_CEILING_TILE);
//                        entries.add(BlockInit.CEILING_TILES);
//                        entries.add(BlockInit.CEILING_TILES_STAINED);
//                        entries.add(BlockInit.DARK_CEILING);
//                        entries.add(BlockInit.DARK_CEILING_DETAIL);
//                        entries.add(BlockInit.PLAIN_BLACK_BLOCK);
//                        entries.add(BlockInit.TAN_THIN_BRICKS);
//                        entries.add(BlockInit.VARIED_TAN_THIN_BRICKS);
//                        entries.add(BlockInit.BROWN_THIN_BRICKS);
//                        entries.add(BlockInit.RED_THIN_BRICKS);
//                        entries.add(BlockInit.ORANGE_THIN_BRICKS);
//                        entries.add(BlockInit.YELLOW_THIN_BRICKS);
//                        entries.add(BlockInit.GREEN_THIN_BRICKS);
//                        entries.add(BlockInit.CYAN_THIN_BRICKS);
//                        entries.add(BlockInit.PURPLE_THIN_BRICKS);
//                        entries.add(BlockInit.MAGENTA_THIN_BRICKS);
//                        entries.add(BlockInit.PINK_THIN_BRICKS);
//                        entries.add(BlockInit.GRAY_THIN_BRICKS);
//                        entries.add(BlockInit.WHITE_THIN_BRICKS);
//                        entries.add(BlockInit.MOSAIC_BRICKS);
//                        entries.add(BlockInit.GRUNGE_STONE_BRICKS);
//                        entries.add(BlockInit.GRUNGE_STONE_BRICKS_DIRTY);
//                        entries.add(BlockInit.DARK_TAN_BRICKS);
//                        entries.add(BlockInit.LIGHT_TAN_BRICKS);
//
//                        // MISCELLANEOUS
//                        entries.add(BlockInit.BALLPIT);
//
//                        entries.add(BlockInit.CHEESE_BLOCK);
//                        entries.add(BlockInit.CHEESE_BLOCK_WHITE);
//                    }).build());
    public static final CreativeModeTab FNAF = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, FnafUniverseRebuilt.MOD_ID),
            FabricItemGroup.builder()
                    .title(Component.literal(FnafUniverseRebuilt.MOD_ID))
                    .hideTitle()
                    .backgroundTexture(Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/tabs_fnaf.png"))
                    .icon(() -> new ItemStack(Blocks.STONE))
                    .displayItems((displayContext, entries) -> {
                        entries.accept(Blocks.STONE);
                    }).build());

    public static void registerItemGroups() {
        FnafUniverseRebuilt.LOGGER.info("Registering Item Groups for " + FnafUniverseRebuilt.MOD_ID);
    }
}
