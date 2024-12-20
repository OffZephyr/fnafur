package net.zephyr.fnafur.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.block_init.PropInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.init.item_init.SpawnItemInit;

public class ModelProvider extends FabricModelProvider {
    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.FOG_BLOCK);

        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.WALL_TILE_FULL);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.GRAY_WALL);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.DARK_GRAY_WALL);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.RED_BRICK_WALL);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.RED_BRICK_WALL_SMALL);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.RED_BRICK_WALL_MIXED);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLUE_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.LARGE_BROWN_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.LARGE_LIGHT_GRAY_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.LARGE_WHITE_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.SMALL_GRAY_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.SMALL_LIGHT_GRAY_BRICKS);
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


        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.STAGE_PLANKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.STAGE_PLANKS_THIN);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.DARK_STAGE_PLANKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.DARK_STAGE_PLANKS_THIN);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.LIGHT_STAGE_PLANKS);

        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_WHITE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_TILE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.WHITE_TILE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.RED_BLUE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_BLUE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLUE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_WHITE_16_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.WHITE_16_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.WHITE_16_CLEAN_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.TAN_16_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.TAN_16_CLEAN_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.TAN_16_SPACED_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_CORNER_BROWN_TILE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_GREEN_DIAGONAL_TILE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.GROUT_TILE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CEILING_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.DARK_CEILING);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.DARK_CEILING_DETAIL);

        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.KITCHEN_FLOOR);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CONCRETE_FLOOR);

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
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ItemInit.DEATHCOIN, Models.GENERATED);
        itemModelGenerator.register(ItemInit.ILLUSIONDISC, Models.GENERATED);
        itemModelGenerator.register(Item.fromBlock(BlockInit.CAMERA), Models.GENERATED);

        itemModelGenerator.register(SpawnItemInit.CL_FRED_SPAWN, Models.GENERATED);
        itemModelGenerator.register(SpawnItemInit.CL_BON_SPAWN, Models.GENERATED);
        itemModelGenerator.register(SpawnItemInit.CL_CHICA_SPAWN, Models.GENERATED);
        itemModelGenerator.register(SpawnItemInit.CL_FOXY_SPAWN, Models.GENERATED);
    }
}
