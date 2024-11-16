package net.zephyr.fnafur.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
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
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(BlockInit.COMPUTER)
                .add(BlockInit.CAMERA)
                .add(BlockInit.FOG_BLOCK)
                .add(BlockInit.GRAY_WALL)
                .add(BlockInit.DARK_GRAY_WALL)
                .add(BlockInit.LARGE_BROWN_BRICKS)
                .add(BlockInit.BLACK_WHITE_TILES)
                .add(BlockInit.RED_BLUE_TILES)
                .add(BlockInit.BLACK_BLUE_TILES)
                .add(BlockInit.BLACK_WHITE_16_TILES)
                .add(BlockInit.BLACK_WHITE_16_TILES_TRIM)
                .add(BlockInit.TAN_16_TILES)
                .add(BlockInit.TAN_16_SPACED_TILES)
                .add(BlockInit.TAN_RAINBOW_16_TILES)
                .add(BlockInit.SMALL_GRAY_BRICKS)
                .add(BlockInit.SMALL_LIGHT_GRAY_BRICKS)
                .add(BlockInit.METAL_PLATES)
                .add(BlockInit.ROUGH_METAL_PLATES)
                .add(BlockInit.CEILING_TILES)
                .add(BlockInit.CEILING_TILES_STAINED)

                .add(PropInit.FLOOR_MONITORS_1)
                .add(PropInit.FLOOR_MONITORS_2)
                .add(PropInit.OFFICE_BUTTONS)
        ;

        getOrCreateTagBuilder(BlockTags.WOOL)
                .add(BlockInit.CARPET_STAR_GREEN)
                .add(BlockInit.CARPET_STAR_CYAN)
                .add(BlockInit.CARPET_STAR_BLUE)
                .add(BlockInit.CARPET_STAR_PURPLE)
                .add(BlockInit.CARPET_STAR_PINK)
                .add(BlockInit.CARPET_STAR_RED)
                .add(BlockInit.CARPET_STAR_ORANGE)
                .add(BlockInit.CARPET_STAR_BROWN)
                ;

        getOrCreateTagBuilder(BlockTags.CLIMBABLE)
                .add(BlockInit.BALLPIT)
                ;
    }
}
