package net.zephyr.fnafur.init.block_init;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.arcademachine.ArcademachineBlock;
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
import net.zephyr.fnafur.blocks.stickers.base.StickerBlock;
import net.zephyr.fnafur.blocks.tile_doors.OfficeDoor;
import net.zephyr.fnafur.blocks.tile_doors.TileDoorBlockEntityRenderer;
import net.zephyr.fnafur.client.JavaModels;
import net.zephyr.fnafur.item.BlockItemWithDescription;
import net.zephyr.fnafur.item.ItemWithDescription;

import java.util.ArrayList;
import java.util.List;

public class BlockInit {
    public static List<StickerBlock> STICKER_BLOCKS = new ArrayList<>();

    public static final Block COMPUTER = registerBlock("computer",
            new ComputerBlock(AbstractBlock.Settings.copy(Blocks.STONE).nonOpaque().notSolid().suffocates(Blocks::never).blockVision(Blocks::never)));
    public static final Block LAYERED_BLOCK_BASE = registerBlock("layered_block",
            new LayeredBlock(AbstractBlock.Settings.copy(Blocks.STONE).mapColor(MapColor.DIRT_BROWN)),
            ItemWithDescription.PAINT_BRUSH, ItemWithDescription.TAPE_MEASURE);
    public static final Block MIMIC_FRAME = registerBlock("mimic_frame_block",
            new MimicFrames(AbstractBlock.Settings.copy(Blocks.STONE)
                    .solidBlock(Blocks::never)
                    .nonOpaque()
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)));
    public static final Block MIMIC_FRAME_SLAB = registerBlock("mimic_frame_slab",
            new MimicFramesSlab(AbstractBlock.Settings.copy(Blocks.STONE)
                    .solidBlock(Blocks::never)
                    .nonOpaque()
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)));

    public static final Block CAMERA = registerBlock("camera",
            new CameraBlock(AbstractBlock.Settings.copy(Blocks.STONE).nonOpaque().allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never).noCollision()),
            ItemWithDescription.PIPE_WRENCH);
    public static final Block CAMERA_DESK = registerBlock("camera_desk",
            new CameraDeskBlock(AbstractBlock.Settings.copy(Blocks.STONE).nonOpaque().allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never)));
    public static final Block ARCADE_MACHINE = registerBlock("arcade_machine",
            new ArcademachineBlock(AbstractBlock.Settings.copy(Blocks.STONE).nonOpaque().allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never)));

    public static final Block FOG_BLOCK = registerBlock("fog_block",
            new FogBlock(AbstractBlock.Settings.copy(Blocks.SNOW_BLOCK).nonOpaque().allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never).breakInstantly()));

    public static final Block OFFICE_DOOR = registerBlock("office_door",
            new OfficeDoor(AbstractBlock.Settings.copy(Blocks.STONE).nonOpaque().allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never).luminance(state -> 0)));

    public static final Block GRAY_WALL = registerStickerBlock("gray_wall",
            new StickerBlock(AbstractBlock.Settings.copy(Blocks.STONE), Identifier.of(FnafUniverseResuited.MOD_ID, "block/gray_wall")));
    public static final Block DARK_GRAY_WALL = registerStickerBlock("dark_gray_wall",
            new StickerBlock(AbstractBlock.Settings.copy(Blocks.STONE), Identifier.of(FnafUniverseResuited.MOD_ID, "block/dark_gray_wall")));
    public static final Block LARGE_BROWN_BRICKS = registerStickerBlock("large_brown_bricks",
            new StickerBlock(AbstractBlock.Settings.copy(Blocks.STONE), Identifier.of(FnafUniverseResuited.MOD_ID, "block/large_brown_bricks")));
    public static final Block SMALL_GRAY_BRICKS = registerStickerBlock("small_gray_bricks",
            new StickerBlock(AbstractBlock.Settings.copy(Blocks.STONE), Identifier.of(FnafUniverseResuited.MOD_ID, "block/small_gray_bricks")));
    public static final Block SMALL_LIGHT_GRAY_BRICKS = registerStickerBlock("small_light_gray_bricks",
            new StickerBlock(AbstractBlock.Settings.copy(Blocks.STONE), Identifier.of(FnafUniverseResuited.MOD_ID, "block/small_light_gray_bricks")));
    public static final Block METAL_PLATES = registerStickerBlock("metal_plates",
            new StickerBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK), Identifier.of(FnafUniverseResuited.MOD_ID, "block/metal_plates")));
    public static final Block ROUGH_METAL_PLATES = registerStickerBlock("rough_metal_plates",
            new StickerBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK), Identifier.of(FnafUniverseResuited.MOD_ID, "block/rough_metal_plates")));
    public static final Block BLACK_WHITE_TILES = registerBlock("black_white_tiles",
            new Block(AbstractBlock.Settings.copy(Blocks.STONE)));
    public static final Block RED_BLUE_TILES = registerBlock("red_blue_tiles",
            new Block(AbstractBlock.Settings.copy(Blocks.STONE)));
    public static final Block BLACK_BLUE_TILES = registerBlock("black_blue_tiles",
            new Block(AbstractBlock.Settings.copy(Blocks.STONE)));
    public static final Block BLACK_WHITE_16_TILES = registerBlock("black_white_16_tiles",
            new Block(AbstractBlock.Settings.copy(Blocks.STONE)));
    public static final Block BLACK_WHITE_16_TILES_TRIM = registerBlock("black_white_16_tiles_trim",
            new Block(AbstractBlock.Settings.copy(Blocks.STONE)));
    public static final Block TAN_16_TILES = registerBlock("tan_16_tiles",
            new Block(AbstractBlock.Settings.copy(Blocks.STONE)));
    public static final Block TAN_16_SPACED_TILES = registerBlock("tan_16_spaced_tiles",
            new Block(AbstractBlock.Settings.copy(Blocks.STONE)));
    public static final Block TAN_RAINBOW_16_TILES = registerBlock("tan_rainbow_16_tiles",
            new Random3Block(AbstractBlock.Settings.copy(Blocks.STONE)));
    public static final Block CEILING_TILES = registerBlock("ceiling_tiles",
            new Block(AbstractBlock.Settings.copy(Blocks.STONE)));
    public static final Block CEILING_TILE_LIGHT = registerBlock("ceiling_tile_light",
            new RedstoneLampBlock(AbstractBlock.Settings.copy(Blocks.REDSTONE_LAMP)));
    public static final Block CEILING_TILES_STAINED = registerBlock("ceiling_tiles_stained",
            new Random4Block(AbstractBlock.Settings.copy(Blocks.STONE)));
    public static final Block CARPET_STAR_GREEN = registerBlock("carpet_star_green",
            new Random4Block(AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)));
    public static final Block CARPET_STAR_CYAN = registerBlock("carpet_star_cyan",
            new Random4Block(AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)));
    public static final Block CARPET_STAR_BLUE = registerBlock("carpet_star_blue",
            new Random4Block(AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)));
    public static final Block CARPET_STAR_PURPLE = registerBlock("carpet_star_purple",
            new Random4Block(AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)));
    public static final Block CARPET_STAR_PINK = registerBlock("carpet_star_pink",
            new Random4Block(AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)));
    public static final Block CARPET_STAR_RED = registerBlock("carpet_star_red",
            new Random4Block(AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)));
    public static final Block CARPET_STAR_ORANGE = registerBlock("carpet_star_orange",
            new Random4Block(AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)));
    public static final Block CARPET_STAR_BROWN = registerBlock("carpet_star_brown",
            new Random4Block(AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)));
    public static final Block BALLPIT = registerBlock("ballpit",
            new BallpitBlock(AbstractBlock.Settings.copy(Blocks.SNOW_BLOCK).strength(0.25F).dynamicBounds().solidBlock(Blocks::never).blockVision(Blocks::always)));

    public static final Block FUEL_GENERATOR = registerBlock("fuel_generator",
            new FuelGeneratorBlock(AbstractBlock.Settings.copy(Blocks.STONE)));
    public static final Block REDSTONE_SWITCH = registerBlock("redstone_switch",
            new RedstoneSwitchBlock(AbstractBlock.Settings.copy(Blocks.STONE)));

    private static Block registerStickerBlock(String name, StickerBlock block) {
        block.setName(name);
        STICKER_BLOCKS.add(block);
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(FnafUniverseResuited.MOD_ID, name), block);
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(FnafUniverseResuited.MOD_ID, name), block);
    }
    private static Block registerBlock(String name, Block block, int... tools) {
        registerBlockItem(name, block, tools);
        return Registry.register(Registries.BLOCK, Identifier.of(FnafUniverseResuited.MOD_ID, name), block);
    }
    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, Identifier.of(FnafUniverseResuited.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }
    private static Item registerBlockItem(String name, Block block, int... tools) {
        return Registry.register(Registries.ITEM, Identifier.of(FnafUniverseResuited.MOD_ID, name),
                new BlockItemWithDescription(block, new Item.Settings(), tools));
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
        FnafUniverseResuited.LOGGER.info("Registering Blocks On CLIENT for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }
}
