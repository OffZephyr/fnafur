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
        addDrop(BlockInit.COMPUTER);
        addDrop(BlockInit.CAMERA);
        addDrop(BlockInit.FOG_BLOCK);
        addDrop(BlockInit.GRAY_WALL);
        addDrop(BlockInit.DARK_GRAY_WALL);
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

        addDrop(BlockInit.BLACK_WHITE_TILES);
        addDrop(BlockInit.BLACK_TILE);
        addDrop(BlockInit.WHITE_TILE);
        addDrop(BlockInit.RED_BLUE_TILES);
        addDrop(BlockInit.BLACK_BLUE_TILES);
        addDrop(BlockInit.BLACK_WHITE_16_TILES);
        addDrop(BlockInit.BLACK_WHITE_16_TILES_TRIM);
        addDrop(BlockInit.WHITE_16_TILES);
        addDrop(BlockInit.WHITE_16_CLEAN_TILES);
        addDrop(BlockInit.TAN_16_TILES);
        addDrop(BlockInit.TAN_16_CLEAN_TILES);
        addDrop(BlockInit.TAN_16_SPACED_TILES);
        addDrop(BlockInit.TAN_RAINBOW_16_TILES);
        addDrop(BlockInit.BLACK_CORNER_BROWN_TILE);
        addDrop(BlockInit.BLACK_GREEN_DIAGONAL_TILE);
        addDrop(BlockInit.CEILING_TILES);
        addDrop(BlockInit.CEILING_TILES_STAINED);

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
        addDrop(BlockInit.CARPET_SPACE);

        addDrop(PropInit.FLOOR_MONITORS_1);
        addDrop(PropInit.FLOOR_MONITORS_2);
        addDrop(PropInit.OFFICE_BUTTONS);
    }
}
