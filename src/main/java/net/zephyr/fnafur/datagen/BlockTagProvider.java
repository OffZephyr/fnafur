package net.zephyr.fnafur.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.block_init.PropInit;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public BlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.PLANKS)
                .add(PropInit.WOODEN_SHELF)
                .add(PropInit.RETRO_TABLE)
                .add(PropInit.RESTROOM_SIGN)
                .add(PropInit.WALL_CLOUDS)
        ;

        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(BlockInit.COMPUTER)
                .add(BlockInit.CAMERA)
                .add(BlockInit.FOG_BLOCK)
                .add(PropInit.FNAF_1_DESK)
                .add(PropInit.CEILING_TILE_VENT)
                .add(PropInit.FLOOR_MONITORS_1)
                .add(PropInit.FLOOR_MONITORS_2)
                .add(PropInit.OFFICE_BUTTONS)

                // WALL BLOCKS AND FLOOR BLOCKS
                .add(BlockInit.WALL_TILE_FULL)
                .add(BlockInit.BLACK_BLUE_WALL_TILES)
                .add(BlockInit.BLACK_BLUE_WALL_TILES_TOP)
                .add(BlockInit.BLACK_RED_WALL_TILES)
                .add(BlockInit.BLACK_RED_WALL_TILES_TOP)
                .add(BlockInit.GRAY_WALL)
                .add(BlockInit.DARK_GRAY_WALL)
                .add(BlockInit.EXTRUDED_BLACK_TILES)
                .add(BlockInit.BLACK_WHITE_TILES)
                .add(BlockInit.BLACK_TILE)
                .add(BlockInit.WHITE_TILE)
                .add(BlockInit.RED_BLUE_TILES)
                .add(BlockInit.BLACK_BLUE_TILES)
                .add(BlockInit.BLUE_TILES)
                .add(BlockInit.BLACK_WHITE_16_TILES)
                .add(BlockInit.BLACK_WHITE_16_TILES_TRIM)
                .add(BlockInit.WHITE_16_TILES)
                .add(BlockInit.WHITE_16_CLEAN_TILES)
                .add(BlockInit.BRIGHT_YELLOW_TILES)
                .add(BlockInit.TAN_16_TILES)
                .add(BlockInit.TAN_16_CLEAN_TILES)
                .add(BlockInit.TAN_16_SPACED_TILES)
                .add(BlockInit.TAN_RAINBOW_16_TILES)
                .add(BlockInit.BLACK_CORNER_BROWN_TILE)
                .add(BlockInit.BLACK_GREEN_DIAGONAL_TILE)
                .add(BlockInit.GROUT_TILE)
                .add(BlockInit.KITCHEN_FLOOR)
                .add(BlockInit.CONCRETE_FLOOR)
                .add(BlockInit.BLUE_SMALL_TILES)
                .add(BlockInit.POOL_FLOOR_SMALL_TILES)
                .add(BlockInit.PURPLE_SMALL_TILES)
                .add(BlockInit.TURQUOISE_SMALL_TILES)
                .add(BlockInit.SMALL_STONE_TILES)



                // BRICKS
                .add(BlockInit.BRICK_WALL)
                .add(BlockInit.BRICK_WALL_DARKER)
                .add(BlockInit.RED_BRICK_WALL)
                .add(BlockInit.RED_BRICK_WALL_SMALL)
                .add(BlockInit.RED_BRICK_WALL_MIXED)
                .add(BlockInit.BLACK_BRICKS)
                .add(BlockInit.BLUE_BRICKS)
                .add(BlockInit.LARGE_BROWN_BRICKS)
                .add(BlockInit.LARGE_LIGHT_GRAY_BRICKS)
                .add(BlockInit.LARGE_WHITE_BRICKS)
                .add(BlockInit.LARGE_WHITE_BRICKS)
                .add(BlockInit.SMALL_GRAY_BRICKS)
                .add(BlockInit.SMALL_LIGHT_GRAY_BRICKS)
                .add(BlockInit.GRUNGE_STONE_BRICKS)
                .add(BlockInit.GRUNGE_STONE_BRICKS_DIRTY)
                .add(BlockInit.TAN_THIN_BRICKS)
                .add(BlockInit.VARIED_TAN_THIN_BRICKS)
                .add(BlockInit.GRAY_THIN_BRICKS)
                .add(BlockInit.BROWN_THIN_BRICKS)
                .add(BlockInit.CYAN_THIN_BRICKS)
                .add(BlockInit.GREEN_THIN_BRICKS)
                .add(BlockInit.MAGENTA_THIN_BRICKS)
                .add(BlockInit.ORANGE_THIN_BRICKS)
                .add(BlockInit.PINK_THIN_BRICKS)
                .add(BlockInit.PURPLE_THIN_BRICKS)
                .add(BlockInit.RED_THIN_BRICKS)
                .add(BlockInit.WHITE_THIN_BRICKS)
                .add(BlockInit.YELLOW_THIN_BRICKS)
                .add(BlockInit.MOSAIC_BRICKS)
                .add(BlockInit.SMALL_BLACK_BRICKS)
                .add(BlockInit.SMALL_BLACK_RED_BRICKS)
                .add(BlockInit.SMALL_BLUE_BLACK_BRICKS)
                .add(BlockInit.SMALL_BLUE_RED_BRICKS)
                .add(BlockInit.SMALL_CYAN_BLACK_BRICKS)
                .add(BlockInit.SMALL_DARK_BLUE_BRICKS)
                .add(BlockInit.DARK_TAN_BRICKS)
                .add(BlockInit.LIGHT_TAN_BRICKS)
                .add(BlockInit.SMALL_BIEGE_BRICKS)

                // METALLIC BLOCKS
                .add(BlockInit.METAL_PLATES)
                .add(BlockInit.ROUGH_METAL_PLATES)

                // WOODEN BLOCKS
                .add(BlockInit.STAGE_PLANKS)
                .add(BlockInit.STAGE_PLANKS_THIN)
                .add(BlockInit.DARK_STAGE_PLANKS)
                .add(BlockInit.DARK_STAGE_PLANKS_THIN)
                .add(BlockInit.LIGHT_STAGE_PLANKS)

                // CEILING BLOCKS
                .add(BlockInit.CEILING_TILES)
                .add(BlockInit.CEILING_TILES_STAINED)
                .add(BlockInit.DARK_CEILING)
                .add(BlockInit.DARK_CEILING_DETAIL)
                .add(BlockInit.BLACK_CEILING_TILE)

                // SINGLE COLORED BLOCKS
                .add(BlockInit.PLAIN_BLACK_BLOCK)

                // GLASS BLOCKS
                .add(BlockInit.TILED_GLASS)
                .add(BlockInit.TILED_GLASS_COLORED)
               ;

                // CARPETS
        getOrCreateTagBuilder(BlockTags.WOOL)
                .add(BlockInit.CARPET_STAR_GREEN)
                .add(BlockInit.CARPET_STAR_CYAN)
                .add(BlockInit.CARPET_STAR_BLUE)
                .add(BlockInit.CARPET_STAR_PURPLE)
                .add(BlockInit.CARPET_STAR_PINK)
                .add(BlockInit.CARPET_STAR_RED)
                .add(BlockInit.CARPET_STAR_ORANGE)
                .add(BlockInit.CARPET_STAR_BROWN)
                .add(BlockInit.CARPET_SWIRLY_RED)
                .add(BlockInit.CARPET_CONFETTI)
                .add(BlockInit.CARPET_CONFETTI_FREDBEARS)
                .add(BlockInit.CARPET_SPACE)
                .add(BlockInit.CARPET_TRIANGLE)
        ;

        // CHEESE BLOCKS

        getOrCreateTagBuilder(BlockTags.SWORD_EFFICIENT)
                .add(BlockInit.CHEESE_BLOCK)
                .add(BlockInit.CHEESE_BLOCK_WHITE)
        ;
        // BALLPIT

        getOrCreateTagBuilder(BlockTags.CLIMBABLE)
                .add(BlockInit.BALLPIT)
                ;
    }
}
