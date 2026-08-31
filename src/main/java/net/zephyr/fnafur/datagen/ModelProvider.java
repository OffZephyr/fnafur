package net.zephyr.fnafur.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.data.*;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.item_init.ItemInit;

import java.util.ArrayList;
import java.util.List;

public class ModelProvider extends FabricModelProvider {
    public ModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        blockStateModelGenerator.createTrivialCube(BlockInit.FOG_BLOCK);

        // GLASS BLOCKS

        blockStateModelGenerator.createTrivialCube(BlockInit.TILED_GLASS);
        blockStateModelGenerator.createTrivialCube(BlockInit.TILED_GLASS_COLORED);
        blockStateModelGenerator.createTrivialCube(BlockInit.BIG_WINDOW);
        blockStateModelGenerator.createTrivialCube(BlockInit.BIG_WINDOW_WHITE);
        blockStateModelGenerator.createTrivialCube(BlockInit.BIG_WINDOW_DARK);
        blockStateModelGenerator.createTrivialCube(BlockInit.TILED_GLASS_SLIT);
        blockStateModelGenerator.createTrivialCube(BlockInit.TILED_GLASS_SLIT_COLORED);


        // WALL AND FLOOR BLOCKS

        blockStateModelGenerator.createTrivialCube(BlockInit.WALL_TILE_FULL);
        blockStateModelGenerator.createTrivialCube(BlockInit.GRAY_WALL);
        blockStateModelGenerator.createTrivialCube(BlockInit.DARK_GRAY_WALL);
        blockStateModelGenerator.createTrivialCube(BlockInit.BLACK_BLUE_WALL_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.BLACK_BLUE_WALL_TILES_TOP);
        blockStateModelGenerator.createTrivialCube(BlockInit.BLACK_RED_WALL_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.BLACK_RED_WALL_TILES_TOP);
        blockStateModelGenerator.createTrivialCube(BlockInit.EXTRUDED_BLACK_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.BLACK_WHITE_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.RED_BLACK_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.BLACK_TILE);
        blockStateModelGenerator.createTrivialCube(BlockInit.WHITE_TILE);
        blockStateModelGenerator.createTrivialCube(BlockInit.RED_BLUE_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.BLACK_BLUE_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.BLACK_PURPLE_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.BLUE_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.BLACK_WHITE_16_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.BRIGHT_YELLOW_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.WHITE_16_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.WHITE_16_CLEAN_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.TAN_16_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.TAN_16_CLEAN_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.TAN_16_SPACED_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.BLACK_CORNER_BROWN_TILE);
        blockStateModelGenerator.createTrivialCube(BlockInit.BLACK_GREEN_DIAGONAL_TILE);
        blockStateModelGenerator.createTrivialCube(BlockInit.GROUT_TILE);
        blockStateModelGenerator.createTrivialCube(BlockInit.DARK_GROUT_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.KITCHEN_FLOOR);
        blockStateModelGenerator.createTrivialCube(BlockInit.BLUE_SMALL_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.TURQUOISE_SMALL_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.POOL_FLOOR_SMALL_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.PURPLE_SMALL_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.SMALL_STONE_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.SMALL_BLACK_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.SMALL_BLACK_RED_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.SMALL_BLUE_BLACK_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.SMALL_BLUE_RED_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.SMALL_CYAN_BLACK_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.SMALL_DARK_BLUE_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.SMALL_BIEGE_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.GREEN_DIRTY_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.GREEN_DIRTY_TILES_BLACK_LINING);
        blockStateModelGenerator.createTrivialCube(BlockInit.GREEN_TILES_BLACK_LINING);
        blockStateModelGenerator.createTrivialCube(BlockInit.OFF_WHITE_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.OFF_WHITE_TILES_DIRTY);
        blockStateModelGenerator.createTrivialCube(BlockInit.BRIGHT_BLUE_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.BRIGHT_NAVY_BLUE_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.BRIGHT_GREEN_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.BRIGHT_WHITE_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.BRIGHT_BROWN_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.BRIGHT_MAGENTA_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.BRIGHT_ORANGE_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.BRIGHT_PINK_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.BRIGHT_PURPLE_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.BLACK_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.DARK_BLUE_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.WHITE_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.RED_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.WHITE_DINER_WALL);
        blockStateModelGenerator.createTrivialCube(BlockInit.TAN_DINER_WALL);

        // CONCRETE
        blockStateModelGenerator.createTrivialCube(BlockInit.CONCRETE_FLOOR);
        blockStateModelGenerator.createTrivialCube(BlockInit.CONCRETE_FLOOR_DARK);
        blockStateModelGenerator.createTrivialCube(BlockInit.CONCRETE_FLOOR_TILE);
        blockStateModelGenerator.createTrivialCube(BlockInit.DARK_GRAY_CONCRETE);
        blockStateModelGenerator.createTrivialCube(BlockInit.GRAY_CONCRETE_WALL);
        blockStateModelGenerator.createTrivialCube(BlockInit.GRAY_CONCRETE_WALL_SPLIT);


        // BRICKS
        blockStateModelGenerator.createTrivialCube(BlockInit.BRICK_WALL);
        blockStateModelGenerator.createTrivialCube(BlockInit.BRICK_WALL_DARKER);
        blockStateModelGenerator.createTrivialCube(BlockInit.RED_BRICK_WALL);
        blockStateModelGenerator.createTrivialCube(BlockInit.RED_BRICK_WALL_SMALL);
        blockStateModelGenerator.createTrivialCube(BlockInit.RED_BRICK_WALL_MIXED);
        blockStateModelGenerator.createTrivialCube(BlockInit.BLACK_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.BLUE_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.LARGE_BROWN_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.LARGE_LIGHT_GRAY_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.LARGE_WHITE_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.SMALL_GRAY_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.SMALL_LIGHT_GRAY_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.GRUNGE_STONE_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.GRUNGE_STONE_BRICKS_DIRTY);
        blockStateModelGenerator.createTrivialCube(BlockInit.METAL_PLATES);
        blockStateModelGenerator.createTrivialCube(BlockInit.ROUGH_METAL_PLATES);
        blockStateModelGenerator.createTrivialCube(BlockInit.TAN_THIN_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.VARIED_TAN_THIN_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.GRAY_THIN_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.BROWN_THIN_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.CYAN_THIN_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.GREEN_THIN_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.MAGENTA_THIN_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.ORANGE_THIN_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.PINK_THIN_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.PURPLE_THIN_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.RED_THIN_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.WHITE_THIN_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.YELLOW_THIN_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.MOSAIC_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.DARK_TAN_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.LIGHT_TAN_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.BLUE_GRAY_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.GRAY_BRICKS_WHITE_CEMENT);
        blockStateModelGenerator.createTrivialCube(BlockInit.BRIGHT_BLUE_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.BRIGHT_WHITE_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.BRIGHT_GREEN_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.BRIGHT_NAVY_BLUE_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.BRIGHT_BROWN_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.BRIGHT_MAGENTA_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.BRIGHT_ORANGE_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.BRIGHT_PINK_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.BRIGHT_PURPLE_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.GREEN_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.TEAL_BRICKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.RED_BRICKS_WHITE_GROUT);
        blockStateModelGenerator.createTrivialCube(BlockInit.RED_BRICKS_BLACK_GROUT);


        // WOODEN BLOCKS
        blockStateModelGenerator.createTrivialCube(BlockInit.STAGE_PLANKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.STAGE_PLANKS_THIN);
        blockStateModelGenerator.createTrivialCube(BlockInit.DARK_STAGE_PLANKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.DARK_STAGE_PLANKS_THIN);
        blockStateModelGenerator.createTrivialCube(BlockInit.LIGHT_STAGE_PLANKS);
        blockStateModelGenerator.createTrivialCube(BlockInit.WOODEN_LOWER_WALL);
        blockStateModelGenerator.createTrivialCube(BlockInit.WOODEN_LOWER_WALL_TRIMMED);


        // CEILING BLOCKS
        blockStateModelGenerator.createTrivialCube(BlockInit.CEILING_TILES);
        blockStateModelGenerator.createTrivialCube(BlockInit.DARK_CEILING);
        blockStateModelGenerator.createTrivialCube(BlockInit.DARK_CEILING_DETAIL);
        blockStateModelGenerator.createTrivialCube(BlockInit.BLACK_CEILING_TILE);
        blockStateModelGenerator.createTrivialCube(BlockInit.WHITE_CEILING_TILES);

        // SINGLE COLORED BLOCKS
        blockStateModelGenerator.createTrivialCube(BlockInit.PLAIN_BLACK_BLOCK);

        // CARPETS
        blockStateModelGenerator.createTrivialCube(BlockInit.CARPET_STAR_GREEN);
        blockStateModelGenerator.createTrivialCube(BlockInit.CARPET_STAR_CYAN);
        blockStateModelGenerator.createTrivialCube(BlockInit.CARPET_STAR_BLUE);
        blockStateModelGenerator.createTrivialCube(BlockInit.CARPET_STAR_PURPLE);
        blockStateModelGenerator.createTrivialCube(BlockInit.CARPET_STAR_PINK);
        blockStateModelGenerator.createTrivialCube(BlockInit.CARPET_STAR_RED);
        blockStateModelGenerator.createTrivialCube(BlockInit.CARPET_STAR_ORANGE);
        blockStateModelGenerator.createTrivialCube(BlockInit.CARPET_STAR_BROWN);
        blockStateModelGenerator.createTrivialCube(BlockInit.CARPET_SWIRLY_RED);
        blockStateModelGenerator.createTrivialCube(BlockInit.CARPET_CONFETTI);
        blockStateModelGenerator.createTrivialCube(BlockInit.CARPET_CONFETTI_FREDBEARS);
        blockStateModelGenerator.createTrivialCube(BlockInit.CARPET_SPACE);
        blockStateModelGenerator.createTrivialCube(BlockInit.CARPET_TRIANGLE);

        // CHEESE BLOCKS
        blockStateModelGenerator.createTrivialCube(BlockInit.CHEESE_BLOCK);
        blockStateModelGenerator.createTrivialCube(BlockInit.CHEESE_BLOCK_WHITE);

        for (BlockInit.PaletteBlock paletteBlock : BlockInit.PALETTES) {

            if (paletteBlock.rotates()) {
                //blockStateModelGenerator.registerMirrorable(paletteBlock.block());

                if(paletteBlock.templateTextures().length > 1){
                    generateRotatingRandomModel(blockStateModelGenerator, paletteBlock.block(), paletteBlock.templateTextures().length);
                }
                else{
                    generateRotatingModel(blockStateModelGenerator, paletteBlock.block());
                }

            } else {

                if(paletteBlock.templateTextures().length > 1){
                    generateRandomModel(blockStateModelGenerator, paletteBlock.block(), paletteBlock.templateTextures().length);
                }
                else{
                    blockStateModelGenerator.createTrivialCube(paletteBlock.block());
                }
            }

//            Identifier texture = PaletteManager.getRecoloredIdentifier(paletteBlock.name(), paletteBlock.paletteEnum());
//
//            blockStateModelGenerator.blockStateCollector.accept(
//                    BlockStateModelGenerator.createSingletonBlockState(
//                            paletteBlock.block(),
//                            BlockStateModelGenerator.createWeightedVariant(
//                                    Models.CUBE_ALL.upload(
//                                            texture,
//                                            TextureMap.all(texture),
//                                            blockStateModelGenerator.modelCollector
//                                    )
//                            )
//                    )
//            );
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(ItemInit.CPU, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemInit.DEATHCOIN, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemInit.ILLUSIONDISC, ModelTemplates.FLAT_ITEM);
        //itemModelGenerator.register(Item.fromBlock(BlockInit.CAMERA), Models.GENERATED);
    }

    public void generateRandomModel(BlockModelGenerators blockStateModelGenerator, Block block, int variantLength) {

        List<Variant> modelVariants = new ArrayList<>();
        for (int i = 1; i <= variantLength; i++) {
            String suffix = "";
            if(i != 1) suffix = "_" + i;
            Variant modelVariant = BlockModelGenerators.plainModel(ModelTemplates.CUBE_ALL.createWithSuffix(block, suffix, new TextureMapping().put(TextureSlot.ALL, TextureMapping.getBlockTexture(block, suffix)), blockStateModelGenerator.modelOutput));
            modelVariants.add(modelVariant);
        }

        Variant[] variantsArray = modelVariants.toArray(new Variant[0]);

        blockStateModelGenerator.blockStateOutput
                .accept(
                        MultiVariantGenerator.dispatch(
                                block,
                                BlockModelGenerators.variants(
                                        variantsArray
                                )
                        )
                );
    }
    public void generateRotatingRandomModel(BlockModelGenerators blockStateModelGenerator, Block block, int variantLength) {

        List<Variant> modelVariants = new ArrayList<>();
        for (int i = 1; i <= variantLength; i++) {
            String suffix = "";
            if(i != 1) suffix = "_" + i;
            Variant modelVariant = BlockModelGenerators.plainModel(TexturedModel.CUBE.createWithSuffix(block, suffix, blockStateModelGenerator.modelOutput));

            List<Variant> list = List.of(
                    modelVariant,
                    modelVariant.with(BlockModelGenerators.X_ROT_90),
                    modelVariant.with(BlockModelGenerators.X_ROT_180),
                    modelVariant.with(BlockModelGenerators.X_ROT_270),
                    modelVariant.with(BlockModelGenerators.Y_ROT_90),
                    modelVariant.with(BlockModelGenerators.Y_ROT_90.then(BlockModelGenerators.X_ROT_90)),
                    modelVariant.with(BlockModelGenerators.Y_ROT_90.then(BlockModelGenerators.X_ROT_180)),
                    modelVariant.with(BlockModelGenerators.Y_ROT_90.then(BlockModelGenerators.X_ROT_270)),
                    modelVariant.with(BlockModelGenerators.Y_ROT_180),
                    modelVariant.with(BlockModelGenerators.Y_ROT_180.then(BlockModelGenerators.X_ROT_90)),
                    modelVariant.with(BlockModelGenerators.Y_ROT_180.then(BlockModelGenerators.X_ROT_180)),
                    modelVariant.with(BlockModelGenerators.Y_ROT_180.then(BlockModelGenerators.X_ROT_270)),
                    modelVariant.with(BlockModelGenerators.Y_ROT_270),
                    modelVariant.with(BlockModelGenerators.Y_ROT_270.then(BlockModelGenerators.X_ROT_90)),
                    modelVariant.with(BlockModelGenerators.Y_ROT_270.then(BlockModelGenerators.X_ROT_180)),
                    modelVariant.with(BlockModelGenerators.Y_ROT_270.then(BlockModelGenerators.X_ROT_270))
            );

            modelVariants.addAll(list);
        }

        Variant[] variantsArray = modelVariants.toArray(new Variant[0]);

        blockStateModelGenerator.blockStateOutput
                .accept(
                        MultiVariantGenerator.dispatch(
                                block,
                                BlockModelGenerators.variants(
                                        variantsArray
                                )
                        )
                );
    }

    public void generateRotatingModel(BlockModelGenerators blockStateModelGenerator, Block block) {

        Variant modelVariant = BlockModelGenerators.plainModel(TexturedModel.CUBE.create(block, blockStateModelGenerator.modelOutput));

        blockStateModelGenerator.blockStateOutput
                .accept(
                        MultiVariantGenerator.dispatch(
                                block,
                                BlockModelGenerators.variants(
                                        modelVariant,
                                        modelVariant.with(BlockModelGenerators.X_ROT_90),
                                        modelVariant.with(BlockModelGenerators.X_ROT_180),
                                        modelVariant.with(BlockModelGenerators.X_ROT_270),
                                        modelVariant.with(BlockModelGenerators.Y_ROT_90),
                                        modelVariant.with(BlockModelGenerators.Y_ROT_90.then(BlockModelGenerators.X_ROT_90)),
                                        modelVariant.with(BlockModelGenerators.Y_ROT_90.then(BlockModelGenerators.X_ROT_180)),
                                        modelVariant.with(BlockModelGenerators.Y_ROT_90.then(BlockModelGenerators.X_ROT_270)),
                                        modelVariant.with(BlockModelGenerators.Y_ROT_180),
                                        modelVariant.with(BlockModelGenerators.Y_ROT_180.then(BlockModelGenerators.X_ROT_90)),
                                        modelVariant.with(BlockModelGenerators.Y_ROT_180.then(BlockModelGenerators.X_ROT_180)),
                                        modelVariant.with(BlockModelGenerators.Y_ROT_180.then(BlockModelGenerators.X_ROT_270)),
                                        modelVariant.with(BlockModelGenerators.Y_ROT_270),
                                        modelVariant.with(BlockModelGenerators.Y_ROT_270.then(BlockModelGenerators.X_ROT_90)),
                                        modelVariant.with(BlockModelGenerators.Y_ROT_270.then(BlockModelGenerators.X_ROT_180)),
                                        modelVariant.with(BlockModelGenerators.Y_ROT_270.then(BlockModelGenerators.X_ROT_270))
                                )
                        )
                );
    }
}
