package net.zephyr.fnafur.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.block_init.PropInit;

import java.util.concurrent.CompletableFuture;

public class LootTableProvider extends FabricBlockLootTableProvider {
    public LootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(BlockInit.CPU_CONFIG_PANEL);
        dropSelf(BlockInit.TRIGGER_BLOCK);
        dropSelf(BlockInit.MIMIC_FRAME_DIAGONAL);
        dropSelf(BlockInit.CHIP_READER);
        dropSelf(BlockInit.SERVER_MONITOR);
        dropSelf(BlockInit.WORKBENCH);
        dropSelf(PropInit.PARTY_TABLE);
        dropSelf(PropInit.PARTY_TABLE_CONFETTI);
        dropSelf(BlockInit.GARAGE_DOOR);
        dropSelf(BlockInit.HEAVY_DOOR);
        dropSelf(BlockInit.WARNING_HEAVY_DOOR);
        dropSelf(PropInit.FNAF_1_DESK);
        dropSelf(PropInit.FLYING_V_GUITAR);
        dropSelf(PropInit.PLUSHIE);
        dropSelf(PropInit.STAR_PLASTIC_CHAIR);
        dropSelf(PropInit.WALL_CLOUDS);
        dropSelf(PropInit.STAGE_SUN);
        dropSelf(PropInit.WALL_PIZZA);
        dropSelf(PropInit.HANGING_STARS);
        dropSelf(PropInit.FLOOR_MONITORS);
        dropSelf(PropInit.OFFICE_BUTTONS);
        dropSelf(PropInit.WOODEN_SHELF);
        dropSelf(PropInit.AC_UNIT);
        dropSelf(PropInit.RETRO_TABLE);
        dropSelf(PropInit.RETRO_STOOL);
        dropSelf(PropInit.CEILING_TILE_VENT);
        dropSelf(PropInit.CEILING_TILE_VENT_BLACK);
        dropSelf(PropInit.RESTROOM_SIGN);
        dropSelf(PropInit.POSTER);
        dropSelf(PropInit.BROOM);
        dropSelf(PropInit.MOP_BUCKET);
        dropSelf(PropInit.TRASH_BIN);
        dropSelf(PropInit.WET_FLOOR_SIGN);
        dropSelf(PropInit.STANDING_MENU);
        dropSelf(PropInit.PRESENT_STACK);
        dropSelf(PropInit.EXIT_SIGN);
        dropSelf(PropInit.WALL_OUTLET);
        dropSelf(PropInit.LIGHT_SWITCH);
        dropSelf(PropInit.AIR_VENT);
        dropSelf(PropInit.PIZZA_OVEN);
        dropSelf(PropInit.KITCHEN_PREP_TABLE);
        dropSelf(PropInit.POTS_AND_PANS_RACK);
        dropSelf(PropInit.PUNCH_IN_CARDS);
        dropSelf(PropInit.BULLETIN_BOARD);
        dropSelf(PropInit.SKEEBALL_ARCADE);
        dropSelf(PropInit.WOODEN_CHAIR);
        dropSelf(PropInit.SCONCE);
        dropSelf(PropInit.HANGING_LIGHT);
        dropSelf(PropInit.SPOT_LIGHT);
        dropSelf(PropInit.HANGING_LIGHTBULB);
        dropSelf(PropInit.WOODEN_STOOL);
        dropSelf(PropInit.PARTY_HAT);
        dropSelf(PropInit.DOUBLE_DOOR_FRIDGE);
        dropSelf(PropInit.FRIDGE);
        dropSelf(PropInit.WOODEN_CLOCK);
        dropSelf(PropInit.TOILET_PAPER_ROLL);
        dropSelf(PropInit.TOILET);
        dropSelf(PropInit.URINAL);
        dropSelf(PropInit.BATHROOM_SINK);
        dropSelf(PropInit.FLOOR_TRASH);
        dropSelf(PropInit.WATER_DISPENSER);
        dropSelf(PropInit.ICE_CREAM_DISPENSER);
        dropSelf(PropInit.SERIOUS_CUTOUT);
        dropSelf(PropInit.SPEAKER);
        dropSelf(PropInit.STANDING_MICROPHONE);
        dropSelf(PropInit.STANDING_SPEAKER);
        dropSelf(PropInit.STANDING_PIANO);
        dropSelf(PropInit.ARCADE_CABINET);
        dropSelf(PropInit.FOOD_DISPLAY_CASE);
        dropSelf(PropInit.RECEPTION_COUNTER);
        dropSelf(PropInit.CONDIMENT_COUNTER);
        dropSelf(PropInit.UTENSILS_BOX);
        dropSelf(PropInit.TOOL_WALL_MOUNT);
        dropSelf(PropInit.ATM);
        dropSelf(PropInit.WOODEN_CRATE);
        dropSelf(PropInit.FNAF1_RULES);
        dropSelf(PropInit.WALL_MENU);
        dropSelf(PropInit.FILING_CABINET);
        dropSelf(PropInit.EXIT_ARROW);
        dropSelf(PropInit.FLOOR_MAT);
        dropSelf(PropInit.WALL_PAPERS);

        dropSelf(BlockInit.FOG_BLOCK);

        //GLASS BLOCKS
        dropSelf(BlockInit.TILED_GLASS);
        dropSelf(BlockInit.TILED_GLASS_COLORED);
        dropSelf(BlockInit.BIG_WINDOW);
        dropSelf(BlockInit.BIG_WINDOW_WHITE);
        dropSelf(BlockInit.BIG_WINDOW_DARK);
        dropSelf(BlockInit.TILED_GLASS_SLIT);
        dropSelf(BlockInit.TILED_GLASS_SLIT_COLORED);


        // WALL TILES AND FLOOR BLOCKS
        dropSelf(BlockInit.WALL_TILE_FULL);
        dropSelf(BlockInit.GRAY_WALL);
        dropSelf(BlockInit.DARK_GRAY_WALL);
        dropSelf(BlockInit.BLACK_BLUE_WALL_TILES);
        dropSelf(BlockInit.BLACK_BLUE_WALL_TILES_TOP);
        dropSelf(BlockInit.BLACK_RED_WALL_TILES);
        dropSelf(BlockInit.BLACK_RED_WALL_TILES_TOP);
        dropSelf(BlockInit.EXTRUDED_BLACK_TILES);
        dropSelf(BlockInit.BLACK_WHITE_TILES);
        dropSelf(BlockInit.RED_BLACK_TILES);
        dropSelf(BlockInit.BLACK_TILE);
        dropSelf(BlockInit.WHITE_TILE);
        dropSelf(BlockInit.RED_BLUE_TILES);
        dropSelf(BlockInit.BLACK_BLUE_TILES);
        dropSelf(BlockInit.BLACK_PURPLE_TILES);
        dropSelf(BlockInit.BLUE_TILES);
        dropSelf(BlockInit.BLACK_WHITE_16_TILES);
        dropSelf(BlockInit.BLACK_WHITE_16_TILES_TRIM);
        dropSelf(BlockInit.WHITE_16_TILES);
        dropSelf(BlockInit.WHITE_16_CLEAN_TILES);
        dropSelf(BlockInit.BRIGHT_YELLOW_TILES);
        dropSelf(BlockInit.TAN_16_TILES);
        dropSelf(BlockInit.TAN_16_CLEAN_TILES);
        dropSelf(BlockInit.TAN_16_SPACED_TILES);
        dropSelf(BlockInit.TAN_RAINBOW_16_TILES);
        dropSelf(BlockInit.BLACK_CORNER_BROWN_TILE);
        dropSelf(BlockInit.BLACK_GREEN_DIAGONAL_TILE);
        dropSelf(BlockInit.GROUT_TILE);
        dropSelf(BlockInit.KITCHEN_FLOOR);
        dropSelf(BlockInit.BLUE_SMALL_TILES);
        dropSelf(BlockInit.POOL_FLOOR_SMALL_TILES);
        dropSelf(BlockInit.PURPLE_SMALL_TILES);
        dropSelf(BlockInit.TURQUOISE_SMALL_TILES);
        dropSelf(BlockInit.SMALL_STONE_TILES);
        dropSelf(BlockInit.SMALL_BLACK_TILES);
        dropSelf(BlockInit.SMALL_BLACK_RED_TILES);
        dropSelf(BlockInit.SMALL_BLUE_BLACK_TILES);
        dropSelf(BlockInit.SMALL_BLUE_RED_TILES);
        dropSelf(BlockInit.SMALL_CYAN_BLACK_TILES);
        dropSelf(BlockInit.SMALL_DARK_BLUE_TILES);
        dropSelf(BlockInit.SMALL_BIEGE_TILES);
        dropSelf(BlockInit.GREEN_DIRTY_TILES);
        dropSelf(BlockInit.GREEN_DIRTY_TILES_BLACK_LINING);
        dropSelf(BlockInit.GREEN_TILES_BLACK_LINING);
        dropSelf(BlockInit.OFF_WHITE_TILES);
        dropSelf(BlockInit.OFF_WHITE_TILES_DIRTY);
        dropSelf(BlockInit.BRIGHT_BLUE_TILES);
        dropSelf(BlockInit.BRIGHT_NAVY_BLUE_TILES);
        dropSelf(BlockInit.BRIGHT_GREEN_TILES);
        dropSelf(BlockInit.BRIGHT_WHITE_TILES);
        dropSelf(BlockInit.BRIGHT_BROWN_TILES);
        dropSelf(BlockInit.BRIGHT_MAGENTA_TILES);
        dropSelf(BlockInit.BRIGHT_ORANGE_TILES);
        dropSelf(BlockInit.BRIGHT_PINK_TILES);
        dropSelf(BlockInit.BRIGHT_PURPLE_TILES);
        dropSelf(BlockInit.BLACK_TILES);
        dropSelf(BlockInit.WHITE_TILES);
        dropSelf(BlockInit.RED_TILES);
        dropSelf(BlockInit.DARK_BLUE_TILES);
        dropSelf(BlockInit.WHITE_DINER_WALL);
        dropSelf(BlockInit.TAN_DINER_WALL);
        dropSelf(BlockInit.DARK_GROUT_TILES);

        // CONCRETE
        dropSelf(BlockInit.CONCRETE_FLOOR);
        dropSelf(BlockInit.CONCRETE_FLOOR_DARK);
        dropSelf(BlockInit.CONCRETE_FLOOR_TILE);
        dropSelf(BlockInit.DARK_GRAY_CONCRETE);
        dropSelf(BlockInit.GRAY_CONCRETE_WALL);
        dropSelf(BlockInit.GRAY_CONCRETE_WALL_SPLIT);

        // BRICKS
        dropSelf(BlockInit.BRICK_WALL);
        dropSelf(BlockInit.BRICK_WALL_DARKER);
        dropSelf(BlockInit.RED_BRICK_WALL);
        dropSelf(BlockInit.RED_BRICK_WALL_SMALL);
        dropSelf(BlockInit.RED_BRICK_WALL_MIXED);
        dropSelf(BlockInit.BLACK_BRICKS);
        dropSelf(BlockInit.BLUE_BRICKS);
        dropSelf(BlockInit.LARGE_BROWN_BRICKS);
        dropSelf(BlockInit.LARGE_LIGHT_GRAY_BRICKS);
        dropSelf(BlockInit.LARGE_WHITE_BRICKS);
        dropSelf(BlockInit.TAN_THIN_BRICKS);
        dropSelf(BlockInit.VARIED_TAN_THIN_BRICKS);
        dropSelf(BlockInit.GRAY_THIN_BRICKS);
        dropSelf(BlockInit.BROWN_THIN_BRICKS);
        dropSelf(BlockInit.CYAN_THIN_BRICKS);
        dropSelf(BlockInit.RED_THIN_BRICKS);
        dropSelf(BlockInit.YELLOW_THIN_BRICKS);
        dropSelf(BlockInit.GREEN_THIN_BRICKS);
        dropSelf(BlockInit.MAGENTA_THIN_BRICKS);
        dropSelf(BlockInit.ORANGE_THIN_BRICKS);
        dropSelf(BlockInit.PINK_THIN_BRICKS);
        dropSelf(BlockInit.PURPLE_THIN_BRICKS);
        dropSelf(BlockInit.WHITE_THIN_BRICKS);
        dropSelf(BlockInit.MOSAIC_BRICKS);
        dropSelf(BlockInit.GRUNGE_STONE_BRICKS);
        dropSelf(BlockInit.GRUNGE_STONE_BRICKS_DIRTY);
        dropSelf(BlockInit.DARK_TAN_BRICKS);
        dropSelf(BlockInit.LIGHT_TAN_BRICKS);
        dropSelf(BlockInit.BLUE_GRAY_BRICKS);
        dropSelf(BlockInit.GRAY_BRICKS_WHITE_CEMENT);
        dropSelf(BlockInit.BRIGHT_BLUE_BRICKS);
        dropSelf(BlockInit.BRIGHT_GREEN_BRICKS);
        dropSelf(BlockInit.BRIGHT_WHITE_BRICKS);
        dropSelf(BlockInit.BRIGHT_NAVY_BLUE_BRICKS);
        dropSelf(BlockInit.BRIGHT_BROWN_BRICKS);
        dropSelf(BlockInit.BRIGHT_MAGENTA_BRICKS);
        dropSelf(BlockInit.BRIGHT_ORANGE_BRICKS);
        dropSelf(BlockInit.BRIGHT_PINK_BRICKS);
        dropSelf(BlockInit.BRIGHT_PURPLE_BRICKS);
        dropSelf(BlockInit.GREEN_BRICKS);
        dropSelf(BlockInit.TEAL_BRICKS);
        dropSelf(BlockInit.RED_BRICKS_BLACK_GROUT);
        dropSelf(BlockInit.RED_BRICKS_WHITE_GROUT);

        // WOODEN BLOCKS
        dropSelf(BlockInit.STAGE_PLANKS);
        dropSelf(BlockInit.STAGE_PLANKS_THIN);
        dropSelf(BlockInit.DARK_STAGE_PLANKS);
        dropSelf(BlockInit.DARK_STAGE_PLANKS_THIN);
        dropSelf(BlockInit.LIGHT_STAGE_PLANKS);
        dropSelf(BlockInit.WOODEN_LOWER_WALL);
        dropSelf(BlockInit.WOODEN_LOWER_WALL_TRIMMED);


        // CEILING BLOCKS
        dropSelf(BlockInit.CEILING_TILES);
        dropSelf(BlockInit.CEILING_TILES_STAINED);
        dropSelf(BlockInit.DARK_CEILING);
        dropSelf(BlockInit.DARK_CEILING_DETAIL);
        dropSelf(BlockInit.BLACK_CEILING_TILE);
        dropSelf(BlockInit.PLAIN_BLACK_BLOCK);
        dropSelf(BlockInit.WHITE_CEILING_TILES);


        // CARPETS
        dropSelf(BlockInit.CARPET_STAR_GREEN);
        dropSelf(BlockInit.CARPET_STAR_CYAN);
        dropSelf(BlockInit.CARPET_STAR_BLUE);
        dropSelf(BlockInit.CARPET_STAR_PURPLE);
        dropSelf(BlockInit.CARPET_STAR_PINK);
        dropSelf(BlockInit.CARPET_STAR_RED);
        dropSelf(BlockInit.CARPET_STAR_ORANGE);
        dropSelf(BlockInit.CARPET_STAR_BROWN);
        dropSelf(BlockInit.CARPET_SWIRLY_RED);
        dropSelf(BlockInit.CARPET_CONFETTI);
        dropSelf(BlockInit.CARPET_CONFETTI_FREDBEARS);
        dropSelf(BlockInit.CARPET_SPACE);
        dropSelf(BlockInit.CARPET_TRIANGLE);

        // CHEESE BLOCKS
        dropSelf(BlockInit.CHEESE_BLOCK);
        dropSelf(BlockInit.CHEESE_BLOCK_WHITE);

    }
}
