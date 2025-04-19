package net.zephyr.fnafur.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import net.minecraft.item.Item;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.item_init.ItemInit;

public class ModelProvider extends FabricModelProvider {
    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.FOG_BLOCK);

        // GLASS BLOCKS

        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.TILED_GLASS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.TILED_GLASS_COLORED);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BIG_WINDOW);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BIG_WINDOW_WHITE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BIG_WINDOW_DARK);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.TILED_GLASS_SLIT);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.TILED_GLASS_SLIT_COLORED);


        // WALL AND FLOOR BLOCKS

        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.WALL_TILE_FULL);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.GRAY_WALL);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.DARK_GRAY_WALL);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_BLUE_WALL_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_BLUE_WALL_TILES_TOP);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_RED_WALL_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_RED_WALL_TILES_TOP);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.EXTRUDED_BLACK_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_WHITE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.RED_BLACK_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_TILE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.WHITE_TILE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.RED_BLUE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_BLUE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_PURPLE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLUE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_WHITE_16_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BRIGHT_YELLOW_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.WHITE_16_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.WHITE_16_CLEAN_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.TAN_16_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.TAN_16_CLEAN_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.TAN_16_SPACED_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_CORNER_BROWN_TILE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_GREEN_DIAGONAL_TILE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.GROUT_TILE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.DARK_GROUT_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.KITCHEN_FLOOR);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLUE_SMALL_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.TURQUOISE_SMALL_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.POOL_FLOOR_SMALL_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.PURPLE_SMALL_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.SMALL_STONE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.SMALL_BLACK_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.SMALL_BLACK_RED_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.SMALL_BLUE_BLACK_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.SMALL_BLUE_RED_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.SMALL_CYAN_BLACK_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.SMALL_DARK_BLUE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.SMALL_BIEGE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.GREEN_DIRTY_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.GREEN_DIRTY_TILES_BLACK_LINING);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.GREEN_TILES_BLACK_LINING);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.OFF_WHITE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.OFF_WHITE_TILES_DIRTY);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BRIGHT_BLUE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BRIGHT_NAVY_BLUE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BRIGHT_GREEN_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BRIGHT_WHITE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BRIGHT_BROWN_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BRIGHT_MAGENTA_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BRIGHT_ORANGE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BRIGHT_PINK_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BRIGHT_PURPLE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.DARK_BLUE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.WHITE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.RED_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.WHITE_DINER_WALL);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.TAN_DINER_WALL);

        // CONCRETE
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CONCRETE_FLOOR);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CONCRETE_FLOOR_DARK);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CONCRETE_FLOOR_TILE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.DARK_GRAY_CONCRETE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.GRAY_CONCRETE_WALL);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.GRAY_CONCRETE_WALL_SPLIT);


        // BRICKS
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BRICK_WALL);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BRICK_WALL_DARKER);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.RED_BRICK_WALL);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.RED_BRICK_WALL_SMALL);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.RED_BRICK_WALL_MIXED);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLUE_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.LARGE_BROWN_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.LARGE_LIGHT_GRAY_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.LARGE_WHITE_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.SMALL_GRAY_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.SMALL_LIGHT_GRAY_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.GRUNGE_STONE_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.GRUNGE_STONE_BRICKS_DIRTY);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.METAL_PLATES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.ROUGH_METAL_PLATES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.TAN_THIN_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.VARIED_TAN_THIN_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.GRAY_THIN_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BROWN_THIN_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CYAN_THIN_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.GREEN_THIN_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.MAGENTA_THIN_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.ORANGE_THIN_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.PINK_THIN_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.PURPLE_THIN_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.RED_THIN_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.WHITE_THIN_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.YELLOW_THIN_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.MOSAIC_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.DARK_TAN_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.LIGHT_TAN_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLUE_GRAY_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.GRAY_BRICKS_WHITE_CEMENT);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BRIGHT_BLUE_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BRIGHT_WHITE_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BRIGHT_GREEN_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BRIGHT_NAVY_BLUE_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BRIGHT_BROWN_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BRIGHT_MAGENTA_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BRIGHT_ORANGE_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BRIGHT_PINK_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BRIGHT_PURPLE_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.GREEN_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.TEAL_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.RED_BRICKS_WHITE_GROUT);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.RED_BRICKS_BLACK_GROUT);



        // WOODEN BLOCKS
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.STAGE_PLANKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.STAGE_PLANKS_THIN);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.DARK_STAGE_PLANKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.DARK_STAGE_PLANKS_THIN);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.LIGHT_STAGE_PLANKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.WOODEN_LOWER_WALL);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.WOODEN_LOWER_WALL_TRIMMED);


        // CEILING BLOCKS
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CEILING_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.DARK_CEILING);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.DARK_CEILING_DETAIL);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_CEILING_TILE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.WHITE_CEILING_TILES);

        // SINGLE COLORED BLOCKS
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.PLAIN_BLACK_BLOCK);

        // CARPETS
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CARPET_STAR_GREEN);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CARPET_STAR_CYAN);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CARPET_STAR_BLUE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CARPET_STAR_PURPLE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CARPET_STAR_PINK);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CARPET_STAR_RED);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CARPET_STAR_ORANGE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CARPET_STAR_BROWN);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CARPET_SWIRLY_RED);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CARPET_CONFETTI);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CARPET_CONFETTI_FREDBEARS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CARPET_SPACE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CARPET_TRIANGLE);

        // CHEESE BLOCKS
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CHEESE_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CHEESE_BLOCK_WHITE);

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ItemInit.DEATHCOIN, Models.GENERATED);
        itemModelGenerator.register(ItemInit.ILLUSIONDISC, Models.GENERATED);
        itemModelGenerator.register(Item.fromBlock(BlockInit.CAMERA), Models.GENERATED);
    }
}
