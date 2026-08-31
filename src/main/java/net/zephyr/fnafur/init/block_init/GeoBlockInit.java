package net.zephyr.fnafur.init.block_init;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.geo_doors.GeoDoor;
import net.zephyr.fnafur.blocks.geo_doors.GeoDoorRenderer;
import net.zephyr.fnafur.blocks.geo_doors.doors.Geo1x2Door;
import net.zephyr.fnafur.blocks.geo_doors.doors.Geo2x3Door;
import net.zephyr.fnafur.blocks.props.other.pirates_cove.curtain.PiratesCoveCurtain;
import net.zephyr.fnafur.blocks.props.other.pirates_cove.curtain.PiratesCoveCurtainRenderer;
import net.zephyr.fnafur.blocks.props.other.pirates_cove.stage.PiratesCoveStage;
import net.zephyr.fnafur.blocks.props.other.pirates_cove.stage.PiratesCoveStageRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GeoBlockInit {
    public static List<GeoDoor> DOORS = new ArrayList<>();

    public static final Block PIRATES_COVE_STAGE = registerBlock(
            "pirates_cove_stage",
            PiratesCoveStage::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
    );
    public static final Block PIRATES_COVE_CURTAIN = registerBlock(
            "pirates_cove_curtain",
            PiratesCoveCurtain::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .noCollision()
    );
    public static final Block SMALL_GRAY_DOOR = registerDoor(
            "small_gray_door",
            Geo1x2Door::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/doors/door_gray_small.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/doors/small_geo_door"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
    );
    public static final Block TWO_FIVE_RED_DOOR = registerDoor(
            "two_five_red_door",
            Geo1x2Door::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/doors/red_door.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/doors/two_five_geo_door"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
    );
    public static final Block TWO_FIVE_BLACK_DOOR = registerDoor(
            "two_five_black_door",
            Geo1x2Door::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/doors/black_door.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/doors/two_five_geo_door"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
    );
    public static final Block TWO_FIVE_GREEN_DOOR = registerDoor(
            "two_five_green_door",
            Geo1x2Door::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/doors/green_door.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/doors/two_five_geo_door"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
    );
    public static final Block TWO_FIVE_CYAN_DOOR = registerDoor(
            "two_five_cyan_door",
            Geo1x2Door::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/doors/cyan_door.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/doors/two_five_geo_door"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
    );
    public static final Block TWO_FIVE_BROWN_DOOR = registerDoor(
            "two_five_brown_door",
            Geo1x2Door::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/doors/brown_door.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/doors/two_five_geo_door"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
    );
    public static final Block TWO_FIVE_RED_DOOR_WINDOW = registerDoor(
            "two_five_red_door_window",
            Geo1x2Door::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/doors/red_door_window.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/doors/2_5_door_window.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/doors/two_five_geo_door"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
    );
    public static final Block TWO_FIVE_BLACK_DOOR_WINDOW = registerDoor(
            "two_five_black_door_window",
            Geo1x2Door::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/doors/black_door_window.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/doors/2_5_door_window.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/doors/two_five_geo_door"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
    );
    public static final Block TWO_FIVE_GREEN_DOOR_WINDOW = registerDoor(
            "two_five_green_door_window",
            Geo1x2Door::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/doors/green_door_window.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/doors/2_5_door_window.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/doors/two_five_geo_door"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
    );
    public static final Block TWO_FIVE_CYAN_DOOR_WINDOW = registerDoor(
            "two_five_cyan_door_window",
            Geo1x2Door::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/doors/cyan_door_window.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/doors/2_5_door_window.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/doors/two_five_geo_door"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
    );
    public static final Block TWO_FIVE_BROWN_DOOR_WINDOW = registerDoor(
            "two_five_brown_door_window",
            Geo1x2Door::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/doors/brown_door_window.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/doors/2_5_door_window.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/doors/two_five_geo_door"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
    );
    public static final Block BIG_GRAY_DOOR = registerDoor(
            "big_gray_door",
            Geo2x3Door::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/doors/door_gray_big.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/doors/big_geo_door"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
    );
    public static final Block BIG_MAGENTA_DOOR = registerDoor(
            "big_magenta_door",
            Geo2x3Door::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/doors/door_magenta_big.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/doors/big_geo_door"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
    );
    public static final Block BIG_GREEN_DOOR = registerDoor(
            "big_green_door",
            Geo2x3Door::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/doors/door_green_big.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/doors/big_geo_door"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
    );



    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties settings) {
        final Identifier identifier = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, name);
        final ResourceKey<Block> registryKey = ResourceKey.create(Registries.BLOCK, identifier);

        final Block block = Blocks.register(registryKey, factory, settings);
        Items.registerBlock(block);
        return block;
    }
    private static Block registerDoor(String name, Function<BlockBehaviour.Properties, Block> factory, Identifier texture, Identifier model, BlockBehaviour.Properties settings) {
        return registerDoor(name, factory, texture, texture, model, settings);
    }
    private static Block registerDoor(String name, Function<BlockBehaviour.Properties, Block> factory, Identifier texture, Identifier windowTexture, Identifier model, BlockBehaviour.Properties settings) {

        GeoDoor block = ((GeoDoor)registerBlock(name, factory, settings));
        DOORS.add(block);
        return block.setActualModelTexture(texture, windowTexture, model);
    }

    public static void registerGeoBlocksOnClient() {

        BlockEntityRenderers.register(GeoBlockEntityInit.PIRATES_COVE_STAGE, PiratesCoveStageRenderer::new);
        BlockEntityRenderers.register(GeoBlockEntityInit.PIRATES_COVE_CURTAIN, PiratesCoveCurtainRenderer::new);

        BlockEntityRenderers.register(GeoBlockEntityInit.GEO_DOOR, GeoDoorRenderer::new);
        //BlockRenderLayerMap.INSTANCE.putBlock(PIRATES_COVE_STAGE, RenderLayer.getCutout());

        //BlockRenderLayerMap.putBlock(TWO_FIVE_RED_DOOR_WINDOW, ChunkSectionLayer.TRANSLUCENT);
        //for (GeoDoor block : DOORS) {
        //    BlockRenderLayerMap.putBlock(block, ChunkSectionLayer.TRANSLUCENT);
        //}

        FnafUniverseRebuilt.LOGGER.info("Registering Geo Blocks On CLIENT for " + FnafUniverseRebuilt.MOD_ID.toUpperCase());
    }
}
