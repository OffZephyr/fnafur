package net.zephyr.fnafur.init.block_init;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.geo_doors.doors.Geo2x3Door;
import net.zephyr.fnafur.blocks.geo_doors.GeoDoor;
import net.zephyr.fnafur.blocks.geo_doors.doors.Geo1x2Door;
import net.zephyr.fnafur.blocks.geo_doors.GeoDoorRenderer;
import net.zephyr.fnafur.blocks.props.other.pirates_cove.curtain.PiratesCoveCurtain;
import net.zephyr.fnafur.blocks.props.other.pirates_cove.curtain.PiratesCoveCurtainRenderer;
import net.zephyr.fnafur.blocks.props.other.pirates_cove.stage.PiratesCoveStage;
import net.zephyr.fnafur.blocks.props.other.pirates_cove.stage.PiratesCoveStageRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GeoBlockInit {
    public static List<Block> DOORS = new ArrayList<>();
    public static final Block PIRATES_COVE_STAGE = registerBlock(
            "pirates_cove_stage",
            PiratesCoveStage::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block PIRATES_COVE_CURTAIN = registerBlock(
            "pirates_cove_curtain",
            PiratesCoveCurtain::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .noCollision()
    );
    public static final Block SMALL_GRAY_DOOR = registerDoor(
            "small_gray_door",
            Geo1x2Door::new,
            Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/doors/door_gray_small.png"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/doors/small_geo_door.geo.json"),
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block TWO_FIVE_RED_DOOR = registerDoor(
            "two_five_red_door",
            Geo1x2Door::new,
            Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/doors/red_door.png"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/doors/two_five_geo_door.geo.json"),
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block TWO_FIVE_BLACK_DOOR = registerDoor(
            "two_five_black_door",
            Geo1x2Door::new,
            Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/doors/black_door.png"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/doors/two_five_geo_door.geo.json"),
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block TWO_FIVE_GREEN_DOOR = registerDoor(
            "two_five_green_door",
            Geo1x2Door::new,
            Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/doors/green_door.png"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/doors/two_five_geo_door.geo.json"),
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block TWO_FIVE_CYAN_DOOR = registerDoor(
            "two_five_cyan_door",
            Geo1x2Door::new,
            Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/doors/cyan_door.png"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/doors/two_five_geo_door.geo.json"),
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block TWO_FIVE_BROWN_DOOR = registerDoor(
            "two_five_brown_door",
            Geo1x2Door::new,
            Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/doors/brown_door.png"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/doors/two_five_geo_door.geo.json"),
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block TWO_FIVE_RED_DOOR_WINDOW = registerDoor(
            "two_five_red_door_window",
            Geo1x2Door::new,
            Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/doors/red_door_window.png"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/doors/two_five_geo_door.geo.json"),
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block TWO_FIVE_BLACK_DOOR_WINDOW = registerDoor(
            "two_five_black_door_window",
            Geo1x2Door::new,
            Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/doors/black_door_window.png"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/doors/two_five_geo_door.geo.json"),
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block TWO_FIVE_GREEN_DOOR_WINDOW = registerDoor(
            "two_five_green_door_window",
            Geo1x2Door::new,
            Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/doors/green_door_window.png"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/doors/two_five_geo_door.geo.json"),
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block TWO_FIVE_CYAN_DOOR_WINDOW = registerDoor(
            "two_five_cyan_door_window",
            Geo1x2Door::new,
            Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/doors/cyan_door_window.png"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/doors/two_five_geo_door.geo.json"),
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block TWO_FIVE_BROWN_DOOR_WINDOW = registerDoor(
            "two_five_brown_door_window",
            Geo1x2Door::new,
            Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/doors/brown_door_window.png"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/doors/two_five_geo_door.geo.json"),
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block BIG_GRAY_DOOR = registerDoor(
            "big_gray_door",
            Geo2x3Door::new,
            Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/doors/door_gray_big.png"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/doors/big_geo_door.geo.json"),
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block BIG_MAGENTA_DOOR = registerDoor(
            "big_magenta_door",
            Geo2x3Door::new,
            Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/doors/door_magenta_big.png"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/doors/big_geo_door.geo.json"),
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block BIG_GREEN_DOOR = registerDoor(
            "big_green_door",
            Geo2x3Door::new,
            Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/doors/door_green_big.png"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/doors/big_geo_door.geo.json"),
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );

    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        final Identifier identifier = Identifier.of(FnafUniverseResuited.MOD_ID, name);
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);

        final Block block = Blocks.register(registryKey, factory, settings);
        Items.register(block);
        return block;
    }
    private static Block registerDoor(String name, Function<AbstractBlock.Settings, Block> factory, Identifier texture, Identifier model, AbstractBlock.Settings settings) {

        GeoDoor block = ((GeoDoor)registerBlock(name, factory, settings));
        DOORS.add(block);
        return block.setActualModelTexture(texture, model);
    }

    public static void registerGeoBlocksOnClient() {

        BlockEntityRendererFactories.register(GeoBlockEntityInit.PIRATES_COVE_STAGE, PiratesCoveStageRenderer::new);
        BlockEntityRendererFactories.register(GeoBlockEntityInit.PIRATES_COVE_CURTAIN, PiratesCoveCurtainRenderer::new);

        BlockEntityRendererFactories.register(GeoBlockEntityInit.GEO_DOOR, GeoDoorRenderer::new);
        //BlockRenderLayerMap.INSTANCE.putBlock(PIRATES_COVE_STAGE, RenderLayer.getCutout());

        for (Block block : DOORS) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getTranslucent());
        }

        FnafUniverseResuited.LOGGER.info("Registering Geo Blocks On CLIENT for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }

    public static void registerGeoBlocks() {
        FnafUniverseResuited.LOGGER.info("Registering Geo Blocks for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }
}
