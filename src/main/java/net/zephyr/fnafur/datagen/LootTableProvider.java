package net.zephyr.fnafur.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.block_init.PropInit;

import java.util.concurrent.CompletableFuture;

public class LootTableProvider extends FabricBlockLootTableProvider {
    public LootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(BlockInit.ANIMATRONIC_BLOCK);
        addDrop(BlockInit.COMPUTER);
        addDrop(BlockInit.CPU_CONFIG_PANEL);
        addDrop(BlockInit.WORKBENCH);
        addDrop(BlockInit.CAMERA);
        addDrop(PropInit.PARTY_TABLE);
        addDrop(PropInit.PARTY_TABLE_CONFETTI);
        addDrop(BlockInit.GARAGE_DOOR);
        addDrop(BlockInit.HEAVY_DOOR);
        addDrop(BlockInit.WARNING_HEAVY_DOOR);
        addDrop(PropInit.FNAF_1_DESK);
        addDrop(PropInit.STAR_PLASTIC_CHAIR);
        addDrop(PropInit.WALL_CLOUDS);
        addDrop(PropInit.WALL_PIZZA);
        addDrop(PropInit.HANGING_STARS);
        addDrop(PropInit.FLOOR_MONITORS);
        addDrop(PropInit.OFFICE_BUTTONS);
        addDrop(PropInit.WOODEN_SHELF);
        addDrop(PropInit.AC_UNIT);
        addDrop(PropInit.RETRO_TABLE);
        addDrop(PropInit.RETRO_STOOL);
        addDrop(PropInit.CEILING_TILE_VENT);
        addDrop(PropInit.CEILING_TILE_VENT_BLACK);
        addDrop(PropInit.RESTROOM_SIGN);
        addDrop(PropInit.POSTER);
        addDrop(PropInit.BROOM);
        addDrop(PropInit.MOP_BUCKET);
        addDrop(PropInit.TRASH_BIN);
        addDrop(PropInit.WET_FLOOR_SIGN);
        addDrop(PropInit.PRESENT_STACK);
        addDrop(PropInit.EXIT_SIGN);
        addDrop(PropInit.WALL_OUTLET);
        addDrop(PropInit.LIGHT_SWITCH);
        addDrop(PropInit.AIR_VENT);
        addDrop(PropInit.PIZZA_OVEN);
        addDrop(PropInit.KITCHEN_PREP_TABLE);
        addDrop(PropInit.POTS_AND_PANS_RACK);
        addDrop(PropInit.PUNCH_IN_CARDS);
        addDrop(PropInit.SKEEBALL_ARCADE);
        addDrop(PropInit.WOODEN_CHAIR);
        addDrop(PropInit.SCONCE);
        addDrop(PropInit.HANGING_LIGHT);
        addDrop(PropInit.SPOT_LIGHT);
        addDrop(PropInit.HANGING_LIGHTBULB);
        addDrop(PropInit.WOODEN_STOOL);
        addDrop(PropInit.PARTY_HAT);
        addDrop(PropInit.DOUBLE_DOOR_FRIDGE);
        addDrop(PropInit.FRIDGE);
        addDrop(PropInit.WOODEN_CLOCK);
        addDrop(PropInit.TOILET_PAPER_ROLL);
        addDrop(PropInit.TOILET);
        addDrop(PropInit.FLOOR_TRASH);
        addDrop(PropInit.WATER_DISPENSER);
        addDrop(PropInit.ICE_CREAM_DISPENSER);
        addDrop(PropInit.SERIOUS_CUTOUT);

        addDrop(BlockInit.FOG_BLOCK);

        //GLASS BLOCKS
        addDrop(BlockInit.TILED_GLASS);
        addDrop(BlockInit.TILED_GLASS_COLORED);
        addDrop(BlockInit.BIG_WINDOW);
        addDrop(BlockInit.BIG_WINDOW_WHITE);
        addDrop(BlockInit.BIG_WINDOW_DARK);
        addDrop(BlockInit.TILED_GLASS_SLIT);
        addDrop(BlockInit.TILED_GLASS_SLIT_COLORED);


        // WALL TILES AND FLOOR BLOCKS
        addDrop(BlockInit.WALL_TILE_FULL);
        addDrop(BlockInit.GRAY_WALL);
        addDrop(BlockInit.DARK_GRAY_WALL);
        addDrop(BlockInit.BLACK_BLUE_WALL_TILES);
        addDrop(BlockInit.BLACK_BLUE_WALL_TILES_TOP);
        addDrop(BlockInit.BLACK_RED_WALL_TILES);
        addDrop(BlockInit.BLACK_RED_WALL_TILES_TOP);
        addDrop(BlockInit.EXTRUDED_BLACK_TILES);
        addDrop(BlockInit.BLACK_WHITE_TILES);
        addDrop(BlockInit.RED_BLACK_TILES);
        addDrop(BlockInit.BLACK_TILE);
        addDrop(BlockInit.WHITE_TILE);
        addDrop(BlockInit.RED_BLUE_TILES);
        addDrop(BlockInit.BLACK_BLUE_TILES);
        addDrop(BlockInit.BLACK_PURPLE_TILES);
        addDrop(BlockInit.BLUE_TILES);
        addDrop(BlockInit.BLACK_WHITE_16_TILES);
        addDrop(BlockInit.BLACK_WHITE_16_TILES_TRIM);
        addDrop(BlockInit.WHITE_16_TILES);
        addDrop(BlockInit.WHITE_16_CLEAN_TILES);
        addDrop(BlockInit.BRIGHT_YELLOW_TILES);
        addDrop(BlockInit.TAN_16_TILES);
        addDrop(BlockInit.TAN_16_CLEAN_TILES);
        addDrop(BlockInit.TAN_16_SPACED_TILES);
        addDrop(BlockInit.TAN_RAINBOW_16_TILES);
        addDrop(BlockInit.BLACK_CORNER_BROWN_TILE);
        addDrop(BlockInit.BLACK_GREEN_DIAGONAL_TILE);
        addDrop(BlockInit.GROUT_TILE);
        addDrop(BlockInit.KITCHEN_FLOOR);
        addDrop(BlockInit.BLUE_SMALL_TILES);
        addDrop(BlockInit.POOL_FLOOR_SMALL_TILES);
        addDrop(BlockInit.PURPLE_SMALL_TILES);
        addDrop(BlockInit.TURQUOISE_SMALL_TILES);
        addDrop(BlockInit.SMALL_STONE_TILES);
        addDrop(BlockInit.SMALL_BLACK_TILES);
        addDrop(BlockInit.SMALL_BLACK_RED_TILES);
        addDrop(BlockInit.SMALL_BLUE_BLACK_TILES);
        addDrop(BlockInit.SMALL_BLUE_RED_TILES);
        addDrop(BlockInit.SMALL_CYAN_BLACK_TILES);
        addDrop(BlockInit.SMALL_DARK_BLUE_TILES);
        addDrop(BlockInit.SMALL_BIEGE_TILES);
        addDrop(BlockInit.GREEN_DIRTY_TILES);
        addDrop(BlockInit.GREEN_DIRTY_TILES_BLACK_LINING);
        addDrop(BlockInit.GREEN_TILES_BLACK_LINING);
        addDrop(BlockInit.OFF_WHITE_TILES);
        addDrop(BlockInit.OFF_WHITE_TILES_DIRTY);
        addDrop(BlockInit.BRIGHT_BLUE_TILES);
        addDrop(BlockInit.BRIGHT_NAVY_BLUE_TILES);
        addDrop(BlockInit.BRIGHT_GREEN_TILES);
        addDrop(BlockInit.BRIGHT_WHITE_TILES);
        addDrop(BlockInit.BRIGHT_BROWN_TILES);
        addDrop(BlockInit.BRIGHT_MAGENTA_TILES);
        addDrop(BlockInit.BRIGHT_ORANGE_TILES);
        addDrop(BlockInit.BRIGHT_PINK_TILES);
        addDrop(BlockInit.BRIGHT_PURPLE_TILES);
        addDrop(BlockInit.BLACK_TILES);
        addDrop(BlockInit.WHITE_TILES);
        addDrop(BlockInit.RED_TILES);
        addDrop(BlockInit.DARK_BLUE_TILES);
        addDrop(BlockInit.WHITE_DINER_WALL);
        addDrop(BlockInit.TAN_DINER_WALL);
        addDrop(BlockInit.DARK_GROUT_TILES);

        // CONCRETE
        addDrop(BlockInit.CONCRETE_FLOOR);
        addDrop(BlockInit.CONCRETE_FLOOR_DARK);
        addDrop(BlockInit.CONCRETE_FLOOR_TILE);
        addDrop(BlockInit.DARK_GRAY_CONCRETE);
        addDrop(BlockInit.GRAY_CONCRETE_WALL);
        addDrop(BlockInit.GRAY_CONCRETE_WALL_SPLIT);

        // BRICKS
        addDrop(BlockInit.BRICK_WALL);
        addDrop(BlockInit.BRICK_WALL_DARKER);
        addDrop(BlockInit.RED_BRICK_WALL);
        addDrop(BlockInit.RED_BRICK_WALL_SMALL);
        addDrop(BlockInit.RED_BRICK_WALL_MIXED);
        addDrop(BlockInit.BLACK_BRICKS);
        addDrop(BlockInit.BLUE_BRICKS);
        addDrop(BlockInit.LARGE_BROWN_BRICKS);
        addDrop(BlockInit.LARGE_LIGHT_GRAY_BRICKS);
        addDrop(BlockInit.LARGE_WHITE_BRICKS);
        addDrop(BlockInit.TAN_THIN_BRICKS);
        addDrop(BlockInit.VARIED_TAN_THIN_BRICKS);
        addDrop(BlockInit.GRAY_THIN_BRICKS);
        addDrop(BlockInit.BROWN_THIN_BRICKS);
        addDrop(BlockInit.CYAN_THIN_BRICKS);
        addDrop(BlockInit.RED_THIN_BRICKS);
        addDrop(BlockInit.YELLOW_THIN_BRICKS);
        addDrop(BlockInit.GREEN_THIN_BRICKS);
        addDrop(BlockInit.MAGENTA_THIN_BRICKS);
        addDrop(BlockInit.ORANGE_THIN_BRICKS);
        addDrop(BlockInit.PINK_THIN_BRICKS);
        addDrop(BlockInit.PURPLE_THIN_BRICKS);
        addDrop(BlockInit.WHITE_THIN_BRICKS);
        addDrop(BlockInit.MOSAIC_BRICKS);
        addDrop(BlockInit.GRUNGE_STONE_BRICKS);
        addDrop(BlockInit.GRUNGE_STONE_BRICKS_DIRTY);
        addDrop(BlockInit.DARK_TAN_BRICKS);
        addDrop(BlockInit.LIGHT_TAN_BRICKS);
        addDrop(BlockInit.BLUE_GRAY_BRICKS);
        addDrop(BlockInit.GRAY_BRICKS_WHITE_CEMENT);
        addDrop(BlockInit.BRIGHT_BLUE_BRICKS);
        addDrop(BlockInit.BRIGHT_GREEN_BRICKS);
        addDrop(BlockInit.BRIGHT_WHITE_BRICKS);
        addDrop(BlockInit.BRIGHT_NAVY_BLUE_BRICKS);
        addDrop(BlockInit.BRIGHT_BROWN_BRICKS);
        addDrop(BlockInit.BRIGHT_MAGENTA_BRICKS);
        addDrop(BlockInit.BRIGHT_ORANGE_BRICKS);
        addDrop(BlockInit.BRIGHT_PINK_BRICKS);
        addDrop(BlockInit.BRIGHT_PURPLE_BRICKS);
        addDrop(BlockInit.GREEN_BRICKS);
        addDrop(BlockInit.TEAL_BRICKS);
        addDrop(BlockInit.RED_BRICKS_BLACK_GROUT);
        addDrop(BlockInit.RED_BRICKS_WHITE_GROUT);

        // WOODEN BLOCKS
        addDrop(BlockInit.STAGE_PLANKS);
        addDrop(BlockInit.STAGE_PLANKS_THIN);
        addDrop(BlockInit.DARK_STAGE_PLANKS);
        addDrop(BlockInit.DARK_STAGE_PLANKS_THIN);
        addDrop(BlockInit.LIGHT_STAGE_PLANKS);
        addDrop(BlockInit.WOODEN_LOWER_WALL);
        addDrop(BlockInit.WOODEN_LOWER_WALL_TRIMMED);


        // CEILING BLOCKS
        addDrop(BlockInit.CEILING_TILES);
        addDrop(BlockInit.CEILING_TILES_STAINED);
        addDrop(BlockInit.DARK_CEILING);
        addDrop(BlockInit.DARK_CEILING_DETAIL);
        addDrop(BlockInit.BLACK_CEILING_TILE);
        addDrop(BlockInit.PLAIN_BLACK_BLOCK);
        addDrop(BlockInit.WHITE_CEILING_TILES);


        // CARPETS
        addDrop(BlockInit.CARPET_STAR_GREEN);
        addDrop(BlockInit.CARPET_STAR_CYAN);
        addDrop(BlockInit.CARPET_STAR_BLUE);
        addDrop(BlockInit.CARPET_STAR_PURPLE);
        addDrop(BlockInit.CARPET_STAR_PINK);
        addDrop(BlockInit.CARPET_STAR_RED);
        addDrop(BlockInit.CARPET_STAR_ORANGE);
        addDrop(BlockInit.CARPET_STAR_BROWN);
        addDrop(BlockInit.CARPET_SWIRLY_RED);
        addDrop(BlockInit.CARPET_CONFETTI);
        addDrop(BlockInit.CARPET_CONFETTI_FREDBEARS);
        addDrop(BlockInit.CARPET_SPACE);
        addDrop(BlockInit.CARPET_TRIANGLE);

        // CHEESE BLOCKS
        addDrop(BlockInit.CHEESE_BLOCK);
        addDrop(BlockInit.CHEESE_BLOCK_WHITE);

    }
}
