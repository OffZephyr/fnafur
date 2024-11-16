package net.zephyr.fnafur.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.*;
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
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.GRAY_WALL);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.DARK_GRAY_WALL);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.LARGE_BROWN_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_WHITE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.RED_BLUE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_BLUE_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.BLACK_WHITE_16_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.TAN_16_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.TAN_16_SPACED_TILES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.SMALL_GRAY_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.SMALL_LIGHT_GRAY_BRICKS);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.METAL_PLATES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.ROUGH_METAL_PLATES);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CEILING_TILES);

        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CARPET_STAR_GREEN);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CARPET_STAR_CYAN);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CARPET_STAR_BLUE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CARPET_STAR_PURPLE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CARPET_STAR_PINK);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CARPET_STAR_RED);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CARPET_STAR_ORANGE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CARPET_STAR_BROWN);
        blockStateModelGenerator.registerSimpleCubeAll(BlockInit.CARPET_STAR_BROWN);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ItemInit.DEATHCOIN, Models.GENERATED);
        itemModelGenerator.register(ItemInit.ILLUSIONDISC, Models.GENERATED);

        itemModelGenerator.register(SpawnItemInit.CL_FRED_SPAWN, Models.GENERATED);
        itemModelGenerator.register(SpawnItemInit.CL_BON_SPAWN, Models.GENERATED);
    }
}
