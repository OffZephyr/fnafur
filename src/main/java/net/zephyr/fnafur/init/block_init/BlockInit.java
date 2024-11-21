package net.zephyr.fnafur.init.block_init;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.basic_blocks.BallpitBlock;
import net.zephyr.fnafur.blocks.basic_blocks.Random3Block;
import net.zephyr.fnafur.blocks.basic_blocks.Random4Block;
import net.zephyr.fnafur.blocks.basic_blocks.illusion_block.MimicFrames;
import net.zephyr.fnafur.blocks.basic_blocks.illusion_block.MimicFramesSlab;
import net.zephyr.fnafur.blocks.battery.blocks.generators.FuelGeneratorBlock;
import net.zephyr.fnafur.blocks.battery.blocks.switches.RedstoneSwitchBlock;
import net.zephyr.fnafur.blocks.camera.CameraBlock;
import net.zephyr.fnafur.blocks.camera.CameraBlockRenderer;
import net.zephyr.fnafur.blocks.camera_desk.CameraDeskBlock;
import net.zephyr.fnafur.blocks.camera_desk.CameraDeskBlockRenderer;
import net.zephyr.fnafur.blocks.computer.ComputerBlock;
import net.zephyr.fnafur.blocks.fog.FogBlock;
import net.zephyr.fnafur.blocks.fog.FogBlockRenderer;
import net.zephyr.fnafur.blocks.basic_blocks.layered_block.LayeredBlock;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlock;
import net.zephyr.fnafur.blocks.tile_doors.OfficeDoor;
import net.zephyr.fnafur.blocks.tile_doors.TileDoorBlockEntityRenderer;
import net.zephyr.fnafur.client.JavaModels;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BlockInit {
    public static List<StickerBlock> STICKER_BLOCKS = new ArrayList<>();

    public static final Block COMPUTER = registerBlock(
            "computer",
            ComputerBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .notSolid()
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block LAYERED_BLOCK_BASE = registerBlock(
            "layered_block",
            LayeredBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .mapColor(MapColor.DIRT_BROWN)
    );
    public static final Block MIMIC_FRAME = registerBlock(
            "mimic_frame_block",
            MimicFrames::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .solidBlock(Blocks::never)
                    .nonOpaque()
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block MIMIC_FRAME_SLAB = registerBlock(
            "mimic_frame_slab",
            MimicFramesSlab::new,
                    AbstractBlock.Settings.copy(Blocks.STONE)
                    .solidBlock(Blocks::never)
                    .nonOpaque()
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );

    public static final Block CAMERA = registerBlock(
            "camera",
            CameraBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .noCollision()
    );
    public static final Block CAMERA_DESK = registerBlock(
            "camera_desk",
            CameraDeskBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block FOG_BLOCK = registerBlock(
            "fog_block",
            FogBlock::new,
            AbstractBlock.Settings.copy(Blocks.SNOW_BLOCK)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
    );

    public static final Block OFFICE_DOOR = registerBlock(
            "office_door",
            OfficeDoor::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .luminance(state -> 0)
    );

    public static final Block GRAY_WALL = registerStickerBlock(
            "gray_wall",
            StickerBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE),
            Identifier.of(FnafUniverseResuited.MOD_ID, "block/gray_wall")
    );
    public static final Block DARK_GRAY_WALL = registerStickerBlock(
            "dark_gray_wall",
            StickerBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE),
            Identifier.of(FnafUniverseResuited.MOD_ID, "block/dark_gray_wall")
    );
    public static final Block LARGE_BROWN_BRICKS = registerStickerBlock(
            "large_brown_bricks",
            StickerBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE),
            Identifier.of(FnafUniverseResuited.MOD_ID, "block/large_brown_bricks")
    );
    public static final Block SMALL_GRAY_BRICKS = registerStickerBlock(
            "small_gray_bricks",
            StickerBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE),
            Identifier.of(FnafUniverseResuited.MOD_ID, "block/small_gray_bricks")
    );
    public static final Block SMALL_LIGHT_GRAY_BRICKS = registerStickerBlock(
            "small_light_gray_bricks",
            StickerBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE),
            Identifier.of(FnafUniverseResuited.MOD_ID, "block/small_light_gray_bricks")
    );
    public static final Block METAL_PLATES = registerStickerBlock(
            "metal_plates",
            StickerBlock::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK),
            Identifier.of(FnafUniverseResuited.MOD_ID, "block/metal_plates")
    );
    public static final Block ROUGH_METAL_PLATES = registerStickerBlock(
            "rough_metal_plates",
            StickerBlock::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK),
            Identifier.of(FnafUniverseResuited.MOD_ID, "block/rough_metal_plates")
    );
    public static final Block BLACK_WHITE_TILES = registerBlock(
            "black_white_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BLACK_TILE = registerBlock(
            "black_tile",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block WHITE_TILE = registerBlock(
            "white_tile",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block RED_BLUE_TILES = registerBlock(
            "red_blue_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BLACK_BLUE_TILES = registerBlock(
            "black_blue_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BLACK_WHITE_16_TILES = registerBlock(
            "black_white_16_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BLACK_WHITE_16_TILES_TRIM = registerBlock(
            "black_white_16_tiles_trim",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block TAN_16_TILES = registerBlock(
            "tan_16_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block TAN_16_SPACED_TILES = registerBlock(
            "tan_16_spaced_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block TAN_RAINBOW_16_TILES = registerBlock(
            "tan_rainbow_16_tiles",
            Random3Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block CEILING_TILES = registerBlock(
            "ceiling_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block CEILING_TILE_LIGHT = registerBlock(
            "ceiling_tile_light",
            RedstoneLampBlock::new,
            AbstractBlock.Settings.copy(Blocks.REDSTONE_LAMP)
    );
    public static final Block CEILING_TILES_STAINED = registerBlock(
            "ceiling_tiles_stained",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block CARPET_STAR_GREEN = registerBlock(
            "carpet_star_green",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)
    );
    public static final Block CARPET_STAR_CYAN = registerBlock(
            "carpet_star_cyan",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)
    );
    public static final Block CARPET_STAR_BLUE = registerBlock(
            "carpet_star_blue",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)
    );
    public static final Block CARPET_STAR_PURPLE = registerBlock(
            "carpet_star_purple",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)
    );
    public static final Block CARPET_STAR_PINK = registerBlock(
            "carpet_star_pink",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)
    );
    public static final Block CARPET_STAR_RED = registerBlock(
            "carpet_star_red",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)
    );
    public static final Block CARPET_STAR_ORANGE = registerBlock(
            "carpet_star_orange",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)
    );
    public static final Block CARPET_STAR_BROWN = registerBlock(
            "carpet_star_brown",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)
    );
    public static final Block CARPET_CONFETTI = registerBlock(
            "carpet_confetti",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)
    );
    public static final Block BALLPIT = registerBlock(
            "ballpit",
            BallpitBlock::new,
            AbstractBlock.Settings.copy(Blocks.SNOW_BLOCK)
                    .strength(0.25F)
                    .dynamicBounds()
                    .solidBlock(Blocks::never)
                    .blockVision(Blocks::always)
    );
    public static final Block FUEL_GENERATOR = registerBlock(
            "fuel_generator",
            FuelGeneratorBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE));
    public static final Block REDSTONE_SWITCH = registerBlock(
            "redstone_switch",
            RedstoneSwitchBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE));

    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        final Identifier identifier = Identifier.of(FnafUniverseResuited.MOD_ID, name);
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);

        final Block block = Blocks.register(registryKey, factory, settings);
        Items.register(block);
        return block;
    }
    private static Block registerStickerBlock(String name, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings, Identifier texture) {
        return registerStickerBlock(name, factory, settings, texture, texture, texture, texture, texture, texture);
    }
    private static Block registerStickerBlock(String name, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings, Identifier sides, Identifier top, Identifier bottom) {
        return registerStickerBlock(name, factory, settings, sides, sides, sides, sides, top, bottom);
    }
    private static Block registerStickerBlock(String name, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings, Identifier north,Identifier east,Identifier south,Identifier west,Identifier top,Identifier bottom) {
        final Identifier identifier = Identifier.of(FnafUniverseResuited.MOD_ID, name);
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);

        final StickerBlock block = ((StickerBlock)Blocks.register(registryKey, factory, settings)).textures(north, east, south, west, top, bottom);
        Items.register(block);

        block.setName(name);
        STICKER_BLOCKS.add(block);

        return block;
    }

    public static void registerBlocks(){
        PropInit.registerProps();
        GeoBlockInit.registerGeoBlocks();
        FnafUniverseResuited.LOGGER.info("Registering Blocks for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }
    public static void registerBlocksOnClient() {
        EntityModelLayerRegistry.registerModelLayer(JavaModels.CAMERA_HEAD, CameraBlockRenderer::getTexturedModelData);
        BlockEntityRendererFactories.register(BlockEntityInit.CAMERA, CameraBlockRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(JavaModels.CAMERA_SCREEN, CameraDeskBlockRenderer::getTexturedModelData);


        BlockEntityRendererFactories.register(BlockEntityInit.CAMERA_DESK, CameraDeskBlockRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.LAYERED_BLOCK_BASE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.CAMERA, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.CAMERA_DESK, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.MIMIC_FRAME, RenderLayer.getTripwire());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.MIMIC_FRAME_SLAB, RenderLayer.getTripwire());

        BlockEntityRendererFactories.register(BlockEntityInit.FOG_BLOCK, FogBlockRenderer::new);

        BlockEntityRendererFactories.register(BlockEntityInit.TILE_DOOR, TileDoorBlockEntityRenderer::new);

        for (StickerBlock block: STICKER_BLOCKS) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getTripwire());
        }

        PropInit.registerPropsOnClient();
        GeoBlockInit.registerGeoBlocksOnClient();
        FnafUniverseResuited.LOGGER.info("Registering Blocks On CLIENT for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }
}
