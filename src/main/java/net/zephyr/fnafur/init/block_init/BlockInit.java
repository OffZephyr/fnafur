package net.zephyr.fnafur.init.block_init;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.basic_blocks.BallpitBlock;
import net.zephyr.fnafur.blocks.basic_blocks.ColoredLight;
import net.zephyr.fnafur.blocks.basic_blocks.Random3Block;
import net.zephyr.fnafur.blocks.basic_blocks.Random4Block;
import net.zephyr.fnafur.blocks.decorations.WarehouseShelfBlock;
import net.zephyr.fnafur.blocks.energy.blocks.generators.FuelGeneratorBlock;
import net.zephyr.fnafur.blocks.energy.blocks.switches.CircuitBreakerBlock;
import net.zephyr.fnafur.blocks.energy.blocks.switches.ElectricalLockerBlock;
import net.zephyr.fnafur.blocks.energy.blocks.switches.RedstoneConverterBlock;
import net.zephyr.fnafur.blocks.illusion_block.MimicFrames;
import net.zephyr.fnafur.blocks.illusion_block.MimicFrames2x2;
import net.zephyr.fnafur.blocks.illusion_block.MimicFrames4x4;
import net.zephyr.fnafur.blocks.camera.CameraBlock;
import net.zephyr.fnafur.blocks.camera.CameraBlockRenderer;
import net.zephyr.fnafur.blocks.camera_desk.CameraDeskBlock;
import net.zephyr.fnafur.blocks.camera_desk.CameraDeskBlockRenderer;
import net.zephyr.fnafur.blocks.tile_doors.TileDoorBlock;
import net.zephyr.fnafur.blocks.tile_doors.TileDoorBlockEntityRenderer;
import net.zephyr.fnafur.blocks.tile_doors.TileDoorDirection;
import net.zephyr.fnafur.blocks.tile_doors.TileDoorItem;
import net.zephyr.fnafur.blocks.utility_blocks.computer.ComputerBlock;
import net.zephyr.fnafur.blocks.fog.FogBlock;
import net.zephyr.fnafur.blocks.fog.FogBlockRenderer;
import net.zephyr.fnafur.blocks.stickers_blocks.BlockWithSticker;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlock;
import net.zephyr.fnafur.blocks.tile_doors.beta.OfficeDoor;
import net.zephyr.fnafur.blocks.utility_blocks.cpu_config_panel.CpuConfigPanelBlock;
import net.zephyr.fnafur.blocks.utility_blocks.workbench.WorkbenchBlock;
import net.zephyr.fnafur.client.JavaModels;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BlockInit {
    public static List<StickerBlock> STICKER_BLOCKS = new ArrayList<>();


    /* CUSTOM MODELS */

    public static final Block COMPUTER = registerBlock(
            "computer",
            ComputerBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .notSolid()
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );

    public static final Block CPU_CONFIG_PANEL = registerBlock(
            "cpu_config_panel",
            CpuConfigPanelBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .notSolid()
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block WORKBENCH = registerBlock(
            "workbench",
            WorkbenchBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .notSolid()
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block STICKER_BLOCK = registerBlock(
            "sticker_block",
            BlockWithSticker::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .mapColor(MapColor.GRAY)
    );
    public static final Block MIMIC_FRAME = registerFrame(
            "mimic_frame_block",
            MimicFrames::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .solidBlock(Blocks::never)
                    .nonOpaque()
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block MIMIC_FRAME_2x2 = registerFrame(
            "mimic_frame_2",
            MimicFrames2x2::new,
            AbstractBlock.Settings.copy(MIMIC_FRAME)
                    .solidBlock(Blocks::never)
                    .nonOpaque()
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .replaceable()
    );
    public static final Block MIMIC_FRAME_4x4 = registerFrame(
            "mimic_frame_4",
            MimicFrames4x4::new,
            AbstractBlock.Settings.copy(MIMIC_FRAME)
                    .solidBlock(Blocks::never)
                    .nonOpaque()
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .replaceable()
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

    /* TILING DOORS */


    public static final Block GARAGE_DOOR = registerTileDoor(
            "garage_door",
            TileDoorBlock::new,
            TileDoorDirection.UP,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .notSolid()
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
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



    /* Ceiling Blocks */

    public static final Block CEILING_TILES = registerBlock(
            "ceiling_tiles",
            Block::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
    );
    public static final Block CEILING_TILE_LIGHT = registerBlock(
            "ceiling_tile_light",
            ColoredLight::new,
            AbstractBlock.Settings.copy(Blocks.REDSTONE_LAMP)
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
    public static final Block CIRCUIT_BREAKER = registerBlock(
            "circuit_breaker",
            CircuitBreakerBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
    );

    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        final Identifier identifier = Identifier.of(FnafUniverseResuited.MOD_ID, name);
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);

        final Block block = Blocks.register(registryKey, factory, settings);
        Items.register(block);
        return block;
    }
    private static Block registerTileDoor(String name, Function<AbstractBlock.Settings, Block> factory, TileDoorDirection direction, AbstractBlock.Settings settings) {
        final Identifier identifier = Identifier.of(FnafUniverseResuited.MOD_ID, name);
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);

        final TileDoorBlock block = (TileDoorBlock) Blocks.register(registryKey, factory, settings);
        Items.register(block, TileDoorItem::new);
        return block;
    }

    private static Block registerFrame(String name, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        Block block = registerBlock(name, factory, settings);
        MimicFrames.IDs.add(Identifier.of(FnafUniverseResuited.MOD_ID, "block/" + name));
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

        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.CAMERA, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.CAMERA_DESK, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.CPU_CONFIG_PANEL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.WORKBENCH, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.FUEL_GENERATOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.ELECTRICAL_LOCKER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.WAREHOUSE_SHELF, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.MIMIC_FRAME, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.MIMIC_FRAME_2x2, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.MIMIC_FRAME_4x4, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.STICKER_BLOCK, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.TILED_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.TILED_GLASS_COLORED, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.BIG_WINDOW, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.BIG_WINDOW_WHITE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.BIG_WINDOW_DARK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.DIRTY_GLASS, RenderLayer.getTranslucent());


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
