package net.zephyr.fnafur.init.block_init;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.arcademachine.ArcademachineBlock;
import net.zephyr.fnafur.blocks.basic_blocks.Random3Block;
import net.zephyr.fnafur.blocks.camera.CameraBlock;
import net.zephyr.fnafur.blocks.camera.CameraBlockRenderer;
import net.zephyr.fnafur.blocks.camera_desk.CameraDeskBlock;
import net.zephyr.fnafur.blocks.camera_desk.CameraDeskBlockRenderer;
import net.zephyr.fnafur.blocks.computer.ComputerBlock;
import net.zephyr.fnafur.blocks.fog.FogBlock;
import net.zephyr.fnafur.blocks.fog.FogBlockRenderer;
import net.zephyr.fnafur.blocks.layered_block.LayeredBlock;
import net.zephyr.fnafur.blocks.stickers.DarkGrayWall;
import net.zephyr.fnafur.blocks.stickers.GrayWall;
import net.zephyr.fnafur.blocks.stickers.LargeBrownBricks;
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
            ItemWithDescription.PAINT_BRUSH, ItemWithDescription.TAPE_MEASURE
    );
    public static final Block CAMERA = registerBlock("camera",
            new CameraBlock(AbstractBlock.Settings.copy(Blocks.STONE).nonOpaque().allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never).noCollision()),
            ItemWithDescription.WRENCH);
    public static final Block CAMERA_DESK = registerBlock("camera_desk",
            new CameraDeskBlock(AbstractBlock.Settings.copy(Blocks.STONE).nonOpaque().allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never)));
    public static final Block ARCADE_MACHINE = registerBlock("arcade_machine",
            new ArcademachineBlock(AbstractBlock.Settings.copy(Blocks.STONE).nonOpaque().allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never)));

    public static final Block FOG_BLOCK = registerBlock("fog_block",
            new FogBlock(AbstractBlock.Settings.copy(Blocks.SNOW_BLOCK).nonOpaque().allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never)));

    public static final Block OFFICE_DOOR = registerBlock("office_door",
            new OfficeDoor(AbstractBlock.Settings.copy(Blocks.STONE).nonOpaque().allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never).breakInstantly().luminance(state -> 0)));

    public static final Block GRAY_WALL = registerBlock("gray_wall",
            new GrayWall(AbstractBlock.Settings.copy(Blocks.STONE)));
    public static final Block DARK_GRAY_WALL = registerBlock("dark_gray_wall",
            new DarkGrayWall(AbstractBlock.Settings.copy(Blocks.STONE)));
    public static final Block LARGE_BROWN_BRICKS = registerBlock("large_brown_bricks",
            new LargeBrownBricks(AbstractBlock.Settings.copy(Blocks.STONE)));
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


    private static Block registerBlock(String name, StickerBlock block) {
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

        BlockEntityRendererFactories.register(BlockEntityInit.FOG_BLOCK, FogBlockRenderer::new);

        BlockEntityRendererFactories.register(BlockEntityInit.TILE_DOOR, TileDoorBlockEntityRenderer::new);

        for (StickerBlock block: STICKER_BLOCKS) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getTripwire());
        }

        PropInit.registerPropsOnClient();
        FnafUniverseResuited.LOGGER.info("Registering Blocks On CLIENT for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }
}
