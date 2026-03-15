package net.zephyr.fnafur.init.block_init;

import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.block.*;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.basic_blocks.BallpitBlock;
import net.zephyr.fnafur.blocks.basic_blocks.Random3Block;
import net.zephyr.fnafur.blocks.basic_blocks.Random4Block;
import net.zephyr.fnafur.blocks.curtain.CurtainBlock;
import net.zephyr.fnafur.blocks.curtain.CurtainBlockEntityRenderer;
import net.zephyr.fnafur.blocks.curtain.CurtainBlockItem;
import net.zephyr.fnafur.blocks.decorations.BackstageShelfBlock;
import net.zephyr.fnafur.blocks.decorations.WarehouseShelfBlock;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.diagonal.DiagonalMimicFrame;
import net.zephyr.fnafur.blocks.dynamic.tiling.vent.VentBlock;
import net.zephyr.fnafur.blocks.energy.blocks.generators.FuelGeneratorBlock;
import net.zephyr.fnafur.blocks.energy.blocks.switches.CircuitBreakerBlock;
import net.zephyr.fnafur.blocks.energy.blocks.switches.ElectricalLockerBlock;
import net.zephyr.fnafur.blocks.energy.blocks.receivers.RedstoneConverterBlock;
import net.zephyr.fnafur.blocks.fog.FogBlock;
import net.zephyr.fnafur.blocks.fog.FogBlockRenderer;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.MimicFrames;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.MimicFrames2x2;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.MimicFrames4x4;
import net.zephyr.fnafur.blocks.dynamic.tiling.tile_doors.TileDoorBlock;
import net.zephyr.fnafur.blocks.dynamic.tiling.tile_doors.TileDoorBlockEntityRenderer;
import net.zephyr.fnafur.blocks.dynamic.tiling.tile_doors.TileDoorDirection;
import net.zephyr.fnafur.blocks.dynamic.tiling.tile_doors.TileDoorItem;
import net.zephyr.fnafur.blocks.stickers_blocks.BlockWithSticker;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.chip_reader.ChipReaderBlock;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.cpu_config_panel.CpuConfigPanelBlock;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.server_monitor.ServerMonitorBlock;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.trigger_block.TriggerBlock;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.workbench.WorkbenchBlock;
import net.zephyr.fnafur.init.SoundsInit;
import net.zephyr.fnafur.init.block_init.Palettes.Blocks.*;
import net.zephyr.fnafur.init.block_init.Palettes.PaletteEnum;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class BlockInit {

    public record PaletteBlock(Block block, String name, PaletteEnum paletteEnum, boolean rotates, Identifier... templateTextures) {
    }
    public static List<PaletteBlock> PALETTES = new ArrayList<>();

    public static Boolean randomize = true;

    /* CUSTOM MODELS */

    public static final Block CPU_CONFIG_PANEL = registerBlock(
            "cpu_config_panel",
            CpuConfigPanelBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );

    public static final Block TRIGGER_BLOCK = registerBlock(
            "trigger_block",
            TriggerBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block CHIP_READER = registerBlock(
            "chip_reader",
            ChipReaderBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block SERVER_MONITOR = registerBlock(
            "server_monitor",
            ServerMonitorBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block WORKBENCH = registerBlock(
            "workbench",
            WorkbenchBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block STICKER_BLOCK = registerBlock(
            "sticker_block",
            BlockWithSticker::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .mapColor(MapColor.GRAY),
            List.of(
                    Text.translatable("fnafur.symbol.scraper")
            )
    );
    public static final Block MIMIC_FRAME = registerFrame(
            "mimic_frame_block",
            MimicFrames::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .solidBlock(MimicFrames::isFullSolidBlock)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never),
            List.of(
                    Text.translatable("fnafur.symbol.scraper")
            )
    );
    public static final Block MIMIC_FRAME_2x2 = registerFrame(
            "mimic_frame_2",
            MimicFrames2x2::new,
            AbstractBlock.Settings.copy(MIMIC_FRAME)
                    .nonOpaque()
                    .solidBlock(MimicFrames::isFullSolidBlock)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never),
            List.of(
                    Text.translatable("fnafur.symbol.scraper")
            )
    );
    public static final Block MIMIC_FRAME_4x4 = registerFrame(
            "mimic_frame_4",
            MimicFrames4x4::new,
            AbstractBlock.Settings.copy(MIMIC_FRAME)
                    .nonOpaque()
                    .solidBlock(MimicFrames::isFullSolidBlock)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never),
            List.of(
                    Text.translatable("fnafur.symbol.scraper")
            )
    );
    public static final Block MIMIC_FRAME_DIAGONAL = registerBlock(
            "mimic_frame_diagonal",
            DiagonalMimicFrame::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
                    .nonOpaque()
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

    public static final Block FUEL_GENERATOR = registerBlock(
            "fuel_generator",
            FuelGeneratorBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
    );
    public static final Block REDSTONE_CONVERTER = registerBlock(
            "redstone_converter",
            RedstoneConverterBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
    );
    public static final Block ELECTRICAL_LOCKER = registerBlock(
            "electrical_locker",
            ElectricalLockerBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
    );

    public static final Block WAREHOUSE_SHELF = registerBlock(
            "warehouse_shelf",
            WarehouseShelfBlock::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BARS)
                    .nonOpaque()
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block BACKSTAGE_SHELF = registerBlock(
            "backstage_shelf",
            BackstageShelfBlock::new,
            AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)
                    .nonOpaque()
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)

    );

    /* TILING DOORS */


    public static final Block GARAGE_DOOR = registerTileDoor(
            "garage_door",
            TileDoorBlock::new,
            TileDoorDirection.UP,
            false,
            SoundsInit.OFFICE_DOOR_ACTIVATE,
            SoundsInit.OFFICE_DOOR_ACTIVATE,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never),
            List.of(
                    Text.translatable("fnafur.symbol.wrench")
            )
    );
    public static final Block HEAVY_DOOR = registerTileDoor(
            "heavy_door",
            TileDoorBlock::new,
            TileDoorDirection.UP,
            true,
            SoundsInit.OFFICE_DOOR_ACTIVATE,
            SoundsInit.OFFICE_DOOR_ACTIVATE,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never),
            List.of(
                    Text.translatable("fnafur.symbol.wrench")
            )
    );
    public static final Block WARNING_HEAVY_DOOR = registerTileDoor(
            "warning_heavy_door",
            TileDoorBlock::new,
            TileDoorDirection.UP,
            true,
            SoundsInit.OFFICE_DOOR_ACTIVATE,
            SoundsInit.OFFICE_DOOR_ACTIVATE,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never),
            List.of(
                    Text.translatable("fnafur.symbol.wrench")
            )
    );

    //BASIC CUBE BLOCKS START HERE!!!

    /* GLASS BLOCKS */

    public static final Block TILED_GLASS = registerBlock(
            "tiled_glass",
            TransparentBlock::new,
            AbstractBlock.Settings.copy(Blocks.GLASS)
    );
    public static final Block TILED_GLASS_COLORED = registerBlock(
            "tiled_glass_colored",
            TransparentBlock::new,
            AbstractBlock.Settings.copy(Blocks.GLASS)
    );
    public static final Block TILED_GLASS_SLIT = registerBlock(
            "tiled_glass_slit",
            TransparentBlock::new,
            AbstractBlock.Settings.copy(Blocks.GLASS)
    );
    public static final Block TILED_GLASS_SLIT_COLORED = registerBlock(
            "tiled_glass_slit_colored",
            TransparentBlock::new,
            AbstractBlock.Settings.copy(Blocks.GLASS)
    );
    public static final Block BIG_WINDOW = registerBlock(
            "big_window",
            TransparentBlock::new,
            AbstractBlock.Settings.copy(Blocks.GLASS)
    );
    public static final Block BIG_WINDOW_WHITE = registerBlock(
            "big_window_white",
            TransparentBlock::new,
            AbstractBlock.Settings.copy(Blocks.GLASS)
    );
    public static final Block BIG_WINDOW_DARK = registerBlock(
            "big_window_dark",
            TransparentBlock::new,
            AbstractBlock.Settings.copy(Blocks.GLASS)
    );
    public static final Block DIRTY_GLASS = registerBlock(
            "dirty_glass",
            Random4Block::new,
            AbstractBlock.Settings.copy(Blocks.GLASS)
    );

    /*Other Wall Blocks */


    public static final Block[] TRIANGLES_WALLPAPER = registerBlockPalette(
            "triangles_wallpaper",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/wallpaper/triangle_wallpaper_template.png"),
            WallpaperPalettes.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] TRIANGLES_WALLPAPER2 = registerBlockPalette(
            "triangles_wallpaper",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/triangle_wallpaper_template.png"),
            WallPalettes.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] CONCRETE = registerRotatingBlockPalette(
            "concrete",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/concrete_template.png"),
            WallPalettes.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] PAINTED_CONCRETE = registerBlockPaletteRotatingRandom(
            "painted_concrete",
            new Identifier[]{
                    Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/painted_concrete_template.png"),
                    Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/dirty_painted_concrete_template.png"),
            },
            WallPalettes.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] SPECKLED_STONE = registerBlockPalette(
            "speckled_stone",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/walls/speckled_stone_template.png"),
            WallPalettes.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );

    public static final Block FIVE_WALL = registerBlock(
            "not_suspicious_wall",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block FIVE_TILES = registerBlock(
            "not_suspicious_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );

    public static final Block GRAY_WALL = registerBlock(
            "gray_wall",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block DARK_GRAY_WALL = registerBlock(
            "dark_gray_wall",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block WHITE_DINER_WALL = registerBlock(
            "white_diner_wall",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block TAN_DINER_WALL = registerBlock(
            "tan_diner_wall",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );


    /* Decorative Bricks */

    public static final Block BRICK_WALL = registerBlock(
            "brick_wall",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BRICK_WALL_DARKER = registerBlock(
            "brick_wall_darker",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block RED_BRICK_WALL = registerBlock(
            "red_brick_wall",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block RED_BRICK_WALL_MIXED = registerBlock(
            "red_brick_wall_mixed",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block RED_BRICK_WALL_SMALL = registerBlock(
            "red_brick_wall_small",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BLACK_BRICKS = registerBlock(
            "black_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BLUE_BRICKS = registerBlock(
            "blue_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block LARGE_BROWN_BRICKS = registerBlock(
            "large_brown_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block LARGE_LIGHT_GRAY_BRICKS = registerBlock(
            "large_light_gray_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block TAN_THIN_BRICKS = registerBlock(
            "tan_thin_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block VARIED_TAN_THIN_BRICKS = registerBlock(
            "varied_tan_thin_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block GRAY_THIN_BRICKS = registerBlock(
            "gray_thin_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BROWN_THIN_BRICKS = registerBlock(
            "brown_thin_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block CYAN_THIN_BRICKS = registerBlock(
            "cyan_thin_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block GREEN_THIN_BRICKS = registerBlock(
            "green_thin_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block MAGENTA_THIN_BRICKS = registerBlock(
            "magenta_thin_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block ORANGE_THIN_BRICKS = registerBlock(
            "orange_thin_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block PINK_THIN_BRICKS = registerBlock(
            "pink_thin_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block PURPLE_THIN_BRICKS = registerBlock(
            "purple_thin_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block RED_THIN_BRICKS = registerBlock(
            "red_thin_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block WHITE_THIN_BRICKS = registerBlock(
            "white_thin_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block YELLOW_THIN_BRICKS = registerBlock(
            "yellow_thin_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block LARGE_WHITE_BRICKS = registerBlock(
            "large_white_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block SMALL_GRAY_BRICKS = registerBlock(
            "small_gray_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block SMALL_LIGHT_GRAY_BRICKS = registerBlock(
            "small_light_gray_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block GRUNGE_STONE_BRICKS = registerBlock(
            "grunge_stone_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block GRUNGE_STONE_BRICKS_DIRTY = registerBlock(
            "grunge_stone_bricks_dirty",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block MOSAIC_BRICKS = registerBlock(
            "mosaic_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block DARK_TAN_BRICKS = registerBlock(
            "dark_tan_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block LIGHT_TAN_BRICKS = registerBlock(
            "light_tan_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );

    public static final Block BLUE_GRAY_BRICKS = registerBlock(
            "blue_gray_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block GRAY_BRICKS_WHITE_CEMENT = registerBlock(
            "gray_bricks_white_cement",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BRIGHT_BLUE_BRICKS = registerBlock(
            "bright_blue_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block DIRTY_BRICKS = registerBlock(
            "dirty_bricks",
            Random4Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BRIGHT_GREEN_BRICKS = registerBlock(
            "bright_green_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BRIGHT_NAVY_BLUE_BRICKS = registerBlock(
            "bright_navy_blue_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BRIGHT_WHITE_BRICKS = registerBlock(
            "bright_white_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BRIGHT_BROWN_BRICKS = registerBlock(
            "bright_brown_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BRIGHT_MAGENTA_BRICKS = registerBlock(
            "bright_magenta_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BRIGHT_ORANGE_BRICKS = registerBlock(
            "bright_orange_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BRIGHT_PINK_BRICKS = registerBlock(
            "bright_pink_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BRIGHT_PURPLE_BRICKS = registerBlock(
            "bright_purple_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block GREEN_BRICKS = registerBlock(
            "green_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block TEAL_BRICKS = registerBlock(
            "teal_bricks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block RED_BRICKS_BLACK_GROUT = registerBlock(
            "red_bricks_black_grout",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block RED_BRICKS_WHITE_GROUT = registerBlock(
            "red_bricks_white_grout",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );

    /* Metallic Blocks */


    public static final Block METAL_PLATES = registerBlock(
            "metal_plates",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
    );
    public static final Block ROUGH_METAL_PLATES = registerBlock(
            "rough_metal_plates",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
    );

    /* Custom Wall Tiles and Floors */

    public static final Block BLACK_BLUE_WALL_TILES = registerBlock(
            "black_blue_wall_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BLACK_BLUE_WALL_TILES_TOP = registerBlock(
            "black_blue_wall_tiles_top",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BLACK_RED_WALL_TILES = registerBlock(
            "black_red_wall_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BLACK_RED_WALL_TILES_TOP = registerBlock(
            "black_red_wall_tiles_top",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block WALL_TILE_FULL = registerBlock(
            "wall_tile_full",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block EXTRUDED_BLACK_TILES = registerBlock(
            "extruded_black_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block SMALL_BLACK_TILES = registerBlock(
            "small_black_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block SMALL_BLACK_RED_TILES = registerBlock(
            "small_black_red_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block SMALL_BLUE_BLACK_TILES = registerBlock(
            "small_blue_black_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block SMALL_BLUE_RED_TILES = registerBlock(
            "small_blue_red_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block SMALL_CYAN_BLACK_TILES = registerBlock(
            "small_cyan_black_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block SMALL_DARK_BLUE_TILES = registerBlock(
            "small_dark_blue_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block SMALL_BIEGE_TILES = registerBlock(
            "small_beige_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block TURQUOISE_SMALL_TILES = registerBlock(
            "turquoise_small_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block GREEN_DIRTY_TILES = registerBlock(
            "green_dirty_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block GREEN_DIRTY_TILES_BLACK_LINING = registerBlock(
            "green_dirty_tiles_black_lining",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block GREEN_TILES_BLACK_LINING = registerBlock(
            "green_tiles_black_lining",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block OFF_WHITE_TILES = registerBlock(
            "off_white_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block OFF_WHITE_TILES_DIRTY = registerBlock(
            "off_white_tiles_dirty",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BRIGHT_BLUE_TILES = registerBlock(
            "bright_blue_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BLACK_TILES = registerBlock(
            "black_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block DARK_BLUE_TILES = registerBlock(
            "dark_blue_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block RED_TILES = registerBlock(
            "red_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block WHITE_TILES = registerBlock(
            "white_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BRIGHT_GREEN_TILES = registerBlock(
            "bright_green_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BRIGHT_NAVY_BLUE_TILES = registerBlock(
            "bright_navy_blue_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BRIGHT_WHITE_TILES = registerBlock(
            "bright_white_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BRIGHT_BROWN_TILES = registerBlock(
            "bright_brown_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BRIGHT_MAGENTA_TILES = registerBlock(
            "bright_magenta_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BRIGHT_ORANGE_TILES = registerBlock(
            "bright_orange_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BRIGHT_PINK_TILES = registerBlock(
            "bright_pink_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BRIGHT_PURPLE_TILES = registerBlock(
            "bright_purple_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );

    /* Wooden Blocks */

    public static final Block STAGE_PLANKS = registerBlock(
            "stage_planks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)
    );
    public static final Block STAGE_PLANKS_THIN = registerBlock(
            "stage_planks_thin",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)
    );
    public static final Block DARK_STAGE_PLANKS = registerBlock(
            "dark_stage_planks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.DARK_OAK_PLANKS)
    );
    public static final Block DARK_STAGE_PLANKS_THIN = registerBlock(
            "dark_stage_planks_thin",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.DARK_OAK_PLANKS)
    );
    public static final Block LIGHT_STAGE_PLANKS = registerBlock(
            "light_stage_planks",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)
    );
    public static final Block WOODEN_LOWER_WALL = registerBlock(
            "wooden_lower_wall",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)
    );
    public static final Block WOODEN_LOWER_WALL_TRIMMED = registerBlock(
            "wooden_lower_wall_trimmed",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)
    );

   /*Floor Blocks & Floor Tiles*/

    public static final Block[] TWO_BY_TWO_TILES = registerBlockPalette(
            "two_tile",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/two_tile_template.png"),
            TilesPalettes.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] TWO_BY_TWO_TILES_WHITE = registerBlockPalette(
            "two_tile_white",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/two_tile_template.png"),
            TilesPalettesWhite.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] TWO_BY_TWO_TILES_BLACK = registerBlockPalette(
            "two_tile_black",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/two_tile_template.png"),
            TilesPalettesBlack.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] TWO_BY_TWO_TILES_UNICOLOR = registerBlockPalette(
            "two_tile_unicolor",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/unicolor_two_tile_template.png"),
            UnicolorOnePalettes.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] TWO_BY_TWO_TILES_DIRTY = registerBlockPalette(
            "two_tile_dirty",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/two_tile_template_dirty.png"),
            TilesPalettes.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] TWO_BY_TWO_TILES_DIRTY_WHITE = registerBlockPalette(
            "two_tile_dirty_white",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/two_tile_template_dirty.png"),
            TilesPalettesWhite.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] TWO_BY_TWO_TILES_DIRTY_BLACK = registerBlockPalette(
            "two_tile_dirty_black",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/two_tile_template_dirty.png"),
            TilesPalettesBlack.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] TWO_BY_TWO_TILES_DIRTY_UNICOLOR = registerBlockPalette(
            "two_tile_dirty_unicolor",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/unicolor_two_tile_template_dirty.png"),
            UnicolorOnePalettes.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] ONE_BY_ONE_TILE_FIRST = registerBlockPalette(
            "one_tile_one",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/one_tile_template_one.png"),
            UnicolorOnePalettes.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] ONE_BY_ONE_TILE_SECOND = registerBlockPalette(
            "one_tile_two",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/one_tile_template_two.png"),
            UnicolorTwoPalettes.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    //public static final Block[] ONE_BY_ONE_TILE_FIRST_DIRTY2 = registerBlockPalette(
    //        "one_tile_one_dirty2",
    //        Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/one_tile_template_dirty2.png"),
    //        UnicolorOnePalettes.values(),
    //        Block::new,
    //        AbstractBlock.Settings.copy(Blocks.STONE)
    //);
    //public static final Block[] ONE_BY_ONE_TILE_SECOND_DIRTY2 = registerBlockPalette(
    //        "one_tile_two_dirty2",
    //        Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/one_tile_template_two_dirty2.png"),
    //        UnicolorTwoPalettes.values(),
    //        Block::new,
    //        AbstractBlock.Settings.copy(Blocks.STONE)
    //);
    public static final Block[] ONE_BY_ONE_TILE_FIRST_DIRTY1 = registerBlockPaletteRandom(
            "one_tile_one_dirty",
            new Identifier[]{
                    Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/one_tile_template_dirty1.png"),
                    Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/one_tile_template_dirty2.png")
            },
            UnicolorOnePalettes.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] ONE_BY_ONE_TILE_SECOND_DIRTY1 = registerBlockPaletteRandom(
            "one_tile_two_dirty",
            new Identifier[]{
                    Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/one_tile_template_two_dirty1.png"),
                    Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/one_tile_template_two_dirty2.png")
            },
            UnicolorTwoPalettes.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] FOUR_BY_FOUR_TILES = registerBlockPalette(
            "four_tile",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/four_tile_template.png"),
            TilesPalettes.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] FOUR_BY_FOUR_TILES_WHITE = registerBlockPalette(
            "four_tile_white",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/four_tile_template.png"),
            TilesPalettesWhite.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] FOUR_BY_FOUR_TILES_BLACK = registerBlockPalette(
            "four_tile_black",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/four_tile_template.png"),
            TilesPalettesBlack.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] FOUR_BY_FOUR_TILES_UNICOLOR = registerBlockPalette(
            "four_tile_unicolor",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/unicolor_four_tile_template.png"),
            UnicolorOnePalettes.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] FOUR_BY_FOUR_TILES_DIRTY = registerBlockPalette(
            "four_tile_dirty",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/four_tile_template_dirty.png"),
            TilesPalettes.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] FOUR_BY_FOUR_TILES_DIRTY_WHITE = registerBlockPalette(
            "four_tile_dirty_white",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/four_tile_template_dirty.png"),
            TilesPalettesWhite.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] FOUR_BY_FOUR_TILES_DIRTY_BLACK = registerBlockPalette(
            "four_tile_dirty_black",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/four_tile_template_dirty.png"),
            TilesPalettesBlack.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] FOUR_BY_FOUR_TILES_DIRTY_UNICOLOR = registerBlockPalette(
            "four_tile_dirty_unicolor",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/unicolor_four_tile_template_dirty.png"),
            UnicolorOnePalettes.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] EIGHT_BY_EIGHT_TILES = registerBlockPalette(
            "eight_tile",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/eight_tile_template.png"),
            TilesPalettes.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] EIGHT_BY_EIGHT_TILES_WHITE = registerBlockPalette(
            "eight_tile_white",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/eight_tile_template.png"),
            TilesPalettesWhite.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] EIGHT_BY_EIGHT_TILES_BLACK = registerBlockPalette(
            "eight_tile_black",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/eight_tile_template.png"),
            TilesPalettesBlack.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] EIGHT_BY_EIGHT_TILES_UNICOLOR = registerBlockPalette(
            "eight_tile_unicolor",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/unicolor_eight_tile_template.png"),
            UnicolorOnePalettes.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] EIGHT_BY_EIGHT_TILES_DIRTY = registerBlockPalette(
            "eight_tile_dirty",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/eight_tile_template_dirty.png"),
            TilesPalettes.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] EIGHT_BY_EIGHT_TILES_DIRTY_WHITE = registerBlockPalette(
            "eight_tile_dirty_white",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/eight_tile_template_dirty.png"),
            TilesPalettesWhite.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] EIGHT_BY_EIGHT_TILES_DIRTY_BLACK = registerBlockPalette(
            "eight_tile_dirty_black",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/eight_tile_template_dirty.png"),
            TilesPalettesBlack.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block[] EIGHT_BY_EIGHT_TILES_DIRTY_UNICOLOR = registerBlockPalette(
            "eight_tile_dirty_unicolor",
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/tiles/unicolor_eight_tile_template_dirty.png"),
            UnicolorOnePalettes.values(),
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );

    public static final Block BLACK_WHITE_TILES = registerBlock(
            "black_white_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block RED_BLACK_TILES = registerBlock(
            "red_black_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block GROUT_TILE = registerBlock(
            "grout_tile",
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
    public static final Block BLACK_PURPLE_TILES = registerBlock(
            "black_purple_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BLUE_TILES = registerBlock(
            "blue_tiles",
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
    public static final Block WHITE_16_TILES = registerBlock(
            "white_16_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block WHITE_16_CLEAN_TILES = registerBlock(
            "white_16_clean_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BRIGHT_YELLOW_TILES = registerBlock(
            "bright_yellow_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block TAN_16_TILES = registerBlock(
            "tan_16_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block TAN_16_CLEAN_TILES = registerBlock(
            "tan_16_clean_tiles",
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
    public static final Block BLACK_CORNER_BROWN_TILE = registerBlock(
            "black_corner_brown_tile",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BLACK_GREEN_DIAGONAL_TILE = registerBlock(
            "black_green_diagonal_tile",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block KITCHEN_FLOOR = registerBlock(
            "kitchen_floor",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BLUE_SMALL_TILES = registerBlock(
            "blue_small_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block POOL_FLOOR_SMALL_TILES = registerBlock(
            "pool_floor_small_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block PURPLE_SMALL_TILES = registerBlock(
            "purple_small_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block SMALL_STONE_TILES = registerBlock(
            "small_stone_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block DARK_GROUT_TILES = registerBlock(
            "dark_grout_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );



    /* Ceiling Blocks */

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
    public static final Block VENT = registerBlock(
            "vent",
            VentBlock::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
    );
    public static final Block CEILING_TILES_STAINED = registerBlock(
            "ceiling_tiles_stained",
            Random4Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block DARK_CEILING = registerBlock(
            "dark_ceiling",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block DARK_CEILING_DETAIL = registerBlock(
            "dark_ceiling_detail",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block BLACK_CEILING_TILE = registerBlock(
            "black_ceiling_tile",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block WHITE_CEILING_TILES = registerBlock(
            "white_ceiling_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );

    /* Carpet Blocks */

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

    public static final Block CARPET_SWIRLY_RED = registerBlock(
            "carpet_swirly_red",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)
    );
    public static final Block CARPET_CONFETTI = registerBlock(
            "carpet_confetti",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)
    );
    public static final Block CARPET_CONFETTI_FREDBEARS = registerBlock(
            "carpet_confetti_fredbears",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)
    );
    public static final Block CARPET_SPACE = registerBlock(
            "carpet_space",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)
    );
    public static final Block CARPET_TRIANGLE = registerBlock(
            "carpet_triangle",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)
    );

    /* Concrete Variations */

    public static final Block CONCRETE_FLOOR = registerBlock(
            "concrete_floor",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)

    );
    public static final Block CONCRETE_FLOOR_DARK = registerBlock(
            "concrete_floor_dark",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)

    );
    public static final Block CONCRETE_FLOOR_TILE = registerBlock(
            "concrete_floor_tile",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)

    );
    public static final Block DARK_GRAY_CONCRETE = registerBlock(
            "dark_gray_concrete",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)

    );
    public static final Block GRAY_CONCRETE_WALL = registerBlock(
            "gray_concrete_wall",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)

    );
    public static final Block GRAY_CONCRETE_WALL_SPLIT = registerBlock(
            "gray_concrete_wall_split",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)

    );

   /* Miscellaneous Blocks */

    public static final Block PLAIN_BLACK_BLOCK = registerBlock(
            "plain_black_block",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block CHEESE_BLOCK = registerBlock(
            "cheese_block",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.HONEY_BLOCK)
    );
    public static final Block CHEESE_BLOCK_WHITE = registerBlock(
            "cheese_block_white",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.HONEY_BLOCK)
    );

    /* Ballpit */

    public static final Block BALLPIT = registerBlock(
            "ballpit",
            BallpitBlock::new,
            AbstractBlock.Settings.copy(Blocks.SNOW_BLOCK)
                    .strength(0.25F)
                    .dynamicBounds()
                    .solidBlock(Blocks::never)
                    .blockVision(Blocks::always)
    );

   /*Circuit Breaker */

    public static final Block CIRCUIT_BREAKER = registerBlock(
            "circuit_breaker",
            CircuitBreakerBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
    );
    // CURTAINS

    public static final Block CURTAIN_TEST = registerBlock(
            "curtain_test",
            CurtainBlock::new,
            CurtainBlockItem::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .blockVision(Blocks::never)
                    .suffocates(Blocks::never)
                    .solidBlock(Blocks::never)
    );



    private static<T extends Enum<T> & PaletteEnum> Block[] registerBlockPalette(String name, Identifier template, T[] palettes, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        return registerBlockPalette(name, new Identifier[]{template}, palettes, factory, BlockItem::new, false, settings);
    }
    private static<T extends Enum<T> & PaletteEnum> Block[] registerRotatingBlockPalette(String name, Identifier template, T[] palettes, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        return registerBlockPalette(name, new Identifier[]{template}, palettes, factory, BlockItem::new, true, settings);
    }

    private static<T extends Enum<T> & PaletteEnum> Block[] registerBlockPaletteRandom(String name, Identifier[] templates, T[] palettes, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        return registerBlockPaletteRandom(name, templates, palettes, factory, false, settings);
    }
    private static<T extends Enum<T> & PaletteEnum> Block[] registerBlockPaletteRandom(String name, Identifier[] templates, T[] palettes, Function<AbstractBlock.Settings, Block> factory, boolean rotates, AbstractBlock.Settings settings) {
        return registerBlockPalette(name, templates, palettes, factory, BlockItem::new, rotates, settings);
    }
    private static<T extends Enum<T> & PaletteEnum> Block[] registerBlockPaletteRotatingRandom(String name, Identifier[] templates, T[] palettes, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        return registerBlockPaletteRandom(name, templates, palettes, factory, true, settings);
    }

    private static<T extends Enum<T> & PaletteEnum> Block[] registerBlockPalette(String name, Identifier[] templates, T[] palettes, Function<AbstractBlock.Settings, Block> factory, BiFunction<Block, Item.Settings, Item> itemFactory, boolean rotate, AbstractBlock.Settings settings) {
        Block[] array = new Block[palettes.length];
        for(int i = 0; i < palettes.length; i++) {
            T palette = palettes[i];
            Block block = registerBlock(
                    name + "_" + palette.name().toLowerCase(Locale.ROOT),
                    factory,
                    itemFactory,
                    settings
            );
            array[i] = block;
            PALETTES.add(new PaletteBlock(block, name, palette, rotate, templates));
        }
        return array;
    }

    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        return registerBlock(name, factory, BlockItem::new, settings, List.of());
    }
    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> factory, BiFunction<Block, Item.Settings, Item> itemFactory, AbstractBlock.Settings settings) {
        return registerBlock(name, factory, itemFactory, settings, List.of());
    }
    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings, List<Text> description) {
        return registerBlock(name, factory, BlockItem::new, settings, description);
    }
    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> factory, BiFunction<Block, Item.Settings, Item> itemFactory, AbstractBlock.Settings settings, List<Text> description) {
        final Identifier identifier = Identifier.of(FnafUniverseRebuilt.MOD_ID, name);
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);

        final Block block = Blocks.register(registryKey, factory, settings);
        if(description.isEmpty()){
            Items.register(block, itemFactory);
        }
        else{
            Items.register(block, itemFactory, new Item.Settings().component(DataComponentTypes.LORE, new LoreComponent(description)));
        }
        return block;
    }
    private static Block registerTileDoor(String name, Function<AbstractBlock.Settings, Block> factory, TileDoorDirection direction, boolean isInverted, SoundEvent openSound, SoundEvent closeSound, AbstractBlock.Settings settings, List<Text> description) {
        final Identifier identifier = Identifier.of(FnafUniverseRebuilt.MOD_ID, name);
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);

        final TileDoorBlock block = ((TileDoorBlock) Blocks.register(registryKey, factory, settings)).setDirection(direction).setInverted(isInverted).setOpenCloseSounds(openSound, closeSound);
        Items.register(block, TileDoorItem::new, new Item.Settings().component(DataComponentTypes.LORE, new LoreComponent(description)));
        return block;
    }

    private static Block registerFrame(String name, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings, List<Text> description) {
        Block block = registerBlock(name, factory, BlockItem::new, settings, description);
        MimicFrames.IDs.add(Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/" + name));
        return block;
    }

    public static void registerBlocksOnClient() {

        BlockRenderLayerMap.putBlock(BlockInit.MIMIC_FRAME_DIAGONAL, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(BlockInit.CPU_CONFIG_PANEL, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(BlockInit.TRIGGER_BLOCK, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(BlockInit.CHIP_READER, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(BlockInit.SERVER_MONITOR, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(BlockInit.WORKBENCH, BlockRenderLayer.CUTOUT);

        PropInit.PROPS.add(BlockInit.FUEL_GENERATOR.asItem());
        PropInit.PROPS.add(BlockInit.REDSTONE_CONVERTER.asItem());
        PropInit.PROPS.add(BlockInit.WORKBENCH.asItem());

        BlockRenderLayerMap.putBlock(BlockInit.FUEL_GENERATOR, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(BlockInit.ELECTRICAL_LOCKER, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(BlockInit.WAREHOUSE_SHELF, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(BlockInit.BACKSTAGE_SHELF, BlockRenderLayer.CUTOUT);

        BlockRenderLayerMap.putBlock(BlockInit.MIMIC_FRAME, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(BlockInit.MIMIC_FRAME_2x2, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(BlockInit.MIMIC_FRAME_4x4, BlockRenderLayer.CUTOUT);

        BlockRenderLayerMap.putBlock(BlockInit.STICKER_BLOCK, BlockRenderLayer.CUTOUT);

        BlockRenderLayerMap.putBlock(BlockInit.TILED_GLASS, BlockRenderLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(BlockInit.TILED_GLASS_SLIT, BlockRenderLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(BlockInit.TILED_GLASS_COLORED, BlockRenderLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(BlockInit.TILED_GLASS_SLIT_COLORED, BlockRenderLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(BlockInit.BIG_WINDOW, BlockRenderLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(BlockInit.BIG_WINDOW_WHITE, BlockRenderLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(BlockInit.BIG_WINDOW_DARK, BlockRenderLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(BlockInit.DIRTY_GLASS, BlockRenderLayer.TRANSLUCENT);
        
        BlockEntityRendererFactories.register(BlockEntityInit.FOG_BLOCK, FogBlockRenderer::new);
        BlockEntityRendererFactories.register(BlockEntityInit.CURTAIN, CurtainBlockEntityRenderer::new);

        BlockEntityRendererFactories.register(BlockEntityInit.TILE_DOOR, TileDoorBlockEntityRenderer::new);

        PropInit.registerPropsOnClient();
        GeoBlockInit.registerGeoBlocksOnClient();
        FnafUniverseRebuilt.LOGGER.info("Registering Blocks On CLIENT for " + FnafUniverseRebuilt.MOD_ID.toUpperCase());
    }

}
