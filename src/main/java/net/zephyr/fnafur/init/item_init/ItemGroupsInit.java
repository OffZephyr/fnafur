package net.zephyr.fnafur.init.item_init;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.block_init.GeoBlockInit;
import net.zephyr.fnafur.init.block_init.PropInit;

public class ItemGroupsInit {

    Item icon = new Item(new Item.Settings());
    public static final ItemGroup FNAF_PROPS = Registry.register(Registries.ITEM_GROUP, Identifier.of(FnafUniverseResuited.MOD_ID, "props"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable(FnafUniverseResuited.MOD_ID + ".props"))
                    .noRenderedName()
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/tabs_fnaf.png"))
                    .icon(() -> new ItemStack(PropInit.FLOOR_MONITORS_1))
                    .entries((displayContext, entries) -> {
                        entries.add(PropInit.FNAF_1_DESK);
                        entries.add(PropInit.WALL_CLOUDS);

                        entries.add(PropInit.FLOOR_MONITORS_1);
                        entries.add(PropInit.FLOOR_MONITORS_2);
                        entries.add(PropInit.WOODEN_SHELF);
                        entries.add(PropInit.RETRO_TABLE);

                        entries.add(PropInit.CEILING_TILE_VENT);
                        entries.add(PropInit.RESTROOM_SIGN);
                        entries.add(PropInit.BROOM);
                        entries.add(PropInit.MOP_BUCKET);

                        entries.add(GeoBlockInit.PIRATES_COVE_STAGE);
                        entries.add(GeoBlockInit.PIRATES_COVE_CURTAIN);

                        entries.add(GeoBlockInit.SMALL_GRAY_DOOR);
                        entries.add(GeoBlockInit.BIG_GRAY_DOOR);
                        entries.add(GeoBlockInit.BIG_MAGENTA_DOOR);
                        entries.add(GeoBlockInit.BIG_GREEN_DOOR);

                    }).build());
    public static final ItemGroup FNAF_TECHNICAL = Registry.register(Registries.ITEM_GROUP, Identifier.of(FnafUniverseResuited.MOD_ID, "technical"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable(FnafUniverseResuited.MOD_ID + ".technical"))
                    .noRenderedName()
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/tabs_fnaf.png"))
                    .icon(() -> new ItemStack(ItemInit.PIPE_WRENCH))
                    .entries((displayContext, entries) -> {

                        entries.add(ItemInit.PIPE_WRENCH);
                        entries.add(ItemInit.WRENCH);
                        entries.add(ItemInit.PAINTBRUSH);
                        entries.add(ItemInit.TAPEMEASURE);
                        entries.add(ItemInit.JERRYCAN);

                        entries.add(BlockInit.CAMERA);
                        entries.add(ItemInit.TABLET);
                        entries.add(BlockInit.FOG_BLOCK);

                        entries.add(BlockInit.OFFICE_DOOR);
                        entries.add(PropInit.OFFICE_BUTTONS);

                        entries.add(BlockInit.COMPUTER);

                    }).build());
    public static final ItemGroup FNAF_BLOCKS = Registry.register(Registries.ITEM_GROUP, Identifier.of(FnafUniverseResuited.MOD_ID, "blocks"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable(FnafUniverseResuited.MOD_ID + ".blocks"))
                    .noRenderedName()
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/tabs_fnaf.png"))
                    .icon(() -> new ItemStack(BlockInit.BLACK_WHITE_TILES))
                    .entries((displayContext, entries) -> {

                        entries.add(BlockInit.MIMIC_FRAME);
                        entries.add(BlockInit.MIMIC_FRAME_2x2);
                        entries.add(BlockInit.MIMIC_FRAME_4x4);
                        entries.add(BlockInit.MIMIC_FRAME_SLAB);

                        entries.add(BlockInit.GROUT_TILE);
                        entries.add(BlockInit.BLACK_TILE);
                        entries.add(BlockInit.WHITE_TILE);
                        entries.add(BlockInit.BLACK_WHITE_TILES);
                        entries.add(BlockInit.RED_BLUE_TILES);
                        entries.add(BlockInit.BLACK_BLUE_TILES);
                        entries.add(BlockInit.EXTRUDED_BLACK_TILES);
                        entries.add(BlockInit.BLUE_TILES);
                        entries.add(BlockInit.BLACK_WHITE_16_TILES);
                        entries.add(BlockInit.BLACK_WHITE_16_TILES_TRIM);
                        entries.add(BlockInit.WHITE_16_CLEAN_TILES);
                        entries.add(BlockInit.WHITE_16_TILES);
                        entries.add(BlockInit.TAN_16_CLEAN_TILES);
                        entries.add(BlockInit.TAN_16_TILES);
                        entries.add(BlockInit.TAN_16_SPACED_TILES);
                        entries.add(BlockInit.TAN_RAINBOW_16_TILES);
                        entries.add(BlockInit.BLACK_CORNER_BROWN_TILE);
                        entries.add(BlockInit.BLACK_GREEN_DIAGONAL_TILE);
                        entries.add(BlockInit.BRIGHT_YELLOW_TILES);

                        entries.add(BlockInit.STAGE_PLANKS);
                        entries.add(BlockInit.STAGE_PLANKS_THIN);
                        entries.add(BlockInit.DARK_STAGE_PLANKS);
                        entries.add(BlockInit.DARK_STAGE_PLANKS_THIN);
                        entries.add(BlockInit.LIGHT_STAGE_PLANKS);

                        entries.add(BlockInit.KITCHEN_FLOOR);
                        entries.add(BlockInit.CONCRETE_FLOOR);

                        entries.add(BlockInit.CARPET_STAR_GREEN);
                        entries.add(BlockInit.CARPET_STAR_CYAN);
                        entries.add(BlockInit.CARPET_STAR_BLUE);
                        entries.add(BlockInit.CARPET_STAR_PURPLE);
                        entries.add(BlockInit.CARPET_STAR_PINK);
                        entries.add(BlockInit.CARPET_STAR_RED);
                        entries.add(BlockInit.CARPET_STAR_ORANGE);
                        entries.add(BlockInit.CARPET_STAR_BROWN);
                        entries.add(BlockInit.CARPET_SWIRLY_RED);
                        entries.add(BlockInit.CARPET_CONFETTI);
                        entries.add(BlockInit.CARPET_CONFETTI_FREDBEARS);
                        entries.add(BlockInit.CARPET_SPACE);
                        entries.add(BlockInit.CARPET_TRIANGLE);

                        entries.add(BlockInit.WALL_TILE_FULL);
                        entries.add(BlockInit.GRAY_WALL);
                        entries.add(BlockInit.DARK_GRAY_WALL);
                        entries.add(BlockInit.BLACK_BLUE_WALL_TILES);
                        entries.add(BlockInit.BLACK_BLUE_WALL_TILES_TOP);
                        entries.add(BlockInit.BLACK_RED_WALL_TILES);
                        entries.add(BlockInit.BLACK_RED_WALL_TILES_TOP);
                        entries.add(BlockInit.BRICK_WALL);
                        entries.add(BlockInit.BRICK_WALL_DARKER);
                        entries.add(BlockInit.RED_BRICK_WALL);
                        entries.add(BlockInit.RED_BRICK_WALL_SMALL);
                        entries.add(BlockInit.RED_BRICK_WALL_MIXED);
                        entries.add(BlockInit.BLACK_BRICKS);
                        entries.add(BlockInit.BLUE_BRICKS);
                        entries.add(BlockInit.LARGE_BROWN_BRICKS);
                        entries.add(BlockInit.LARGE_LIGHT_GRAY_BRICKS);
                        entries.add(BlockInit.LARGE_WHITE_BRICKS);
                        entries.add(BlockInit.SMALL_GRAY_BRICKS);
                        entries.add(BlockInit.SMALL_LIGHT_GRAY_BRICKS);
                        entries.add(BlockInit.METAL_PLATES);
                        entries.add(BlockInit.ROUGH_METAL_PLATES);
                        entries.add(BlockInit.CEILING_TILE_LIGHT);
                        entries.add(BlockInit.BLACK_CEILING_TILE);
                        entries.add(BlockInit.CEILING_TILES);
                        entries.add(BlockInit.CEILING_TILES_STAINED);
                        entries.add(BlockInit.DARK_CEILING);
                        entries.add(BlockInit.DARK_CEILING_DETAIL);
                        entries.add(BlockInit.PLAIN_BLACK_BLOCK);
                        entries.add(BlockInit.TAN_THIN_BRICKS);
                        entries.add(BlockInit.VARIED_TAN_THIN_BRICKS);
                        entries.add(BlockInit.BROWN_THIN_BRICKS);
                        entries.add(BlockInit.RED_THIN_BRICKS);
                        entries.add(BlockInit.ORANGE_THIN_BRICKS);
                        entries.add(BlockInit.YELLOW_THIN_BRICKS);
                        entries.add(BlockInit.GREEN_THIN_BRICKS);
                        entries.add(BlockInit.CYAN_THIN_BRICKS);
                        entries.add(BlockInit.PURPLE_THIN_BRICKS);
                        entries.add(BlockInit.MAGENTA_THIN_BRICKS);
                        entries.add(BlockInit.PINK_THIN_BRICKS);
                        entries.add(BlockInit.GRAY_THIN_BRICKS);
                        entries.add(BlockInit.WHITE_THIN_BRICKS);
                        entries.add(BlockInit.MOSAIC_BRICKS);
                        entries.add(BlockInit.GRUNGE_STONE_BRICKS);
                        entries.add(BlockInit.GRUNGE_STONE_BRICKS_DIRTY);
                        entries.add(BlockInit.SMALL_BLACK_BRICKS);
                        entries.add(BlockInit.SMALL_BLACK_RED_BRICKS);
                        entries.add(BlockInit.SMALL_BLUE_BLACK_BRICKS);
                        entries.add(BlockInit.SMALL_BLUE_RED_BRICKS);
                        entries.add(BlockInit.SMALL_CYAN_BLACK_BRICKS);
                        entries.add(BlockInit.SMALL_DARK_BLUE_BRICKS);

                        entries.add(BlockInit.BALLPIT);

                        entries.add(BlockInit.CHEESE_BLOCK);
                        entries.add(BlockInit.CHEESE_BLOCK_WHITE);

                        entries.add(StickerInit.BLACK_WHITE_RED_WALL_TILES);
                        entries.add(StickerInit.BLACK_WHITE_RED_WALL_BIG_TILES);
                        entries.add(StickerInit.BLACK_WHITE_BLACK_WALL_TILES);
                        entries.add(StickerInit.BLACK_WHITE_BLACK_DIRTY_WALL_TILES);
                        entries.add(StickerInit.COLORED_WHITE_WALL_TILES);
                        entries.add(StickerInit.COLORED_WHITE_LONG_WALL_TILES);
                        entries.add(StickerInit.WALL_GRUNGE);
                        entries.add(StickerInit.WALL_GRUNGE_2);


                    }).build());
    public static final ItemGroup FNAF_ANIMATRONICS = Registry.register(Registries.ITEM_GROUP, Identifier.of(FnafUniverseResuited.MOD_ID, "animatronics"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable(FnafUniverseResuited.MOD_ID + ".animatronics"))
                    .noRenderedName()
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/tabs_fnaf.png"))
                    .icon(() -> new ItemStack(SpawnItemInit.CL_FRED_SPAWN))
                    .entries((displayContext, entries) -> {
                        entries.add(ItemInit.DEATHCOIN);
                        entries.add(ItemInit.CPU);
                        entries.add(ItemInit.ILLUSIONDISC);

                        entries.add(SpawnItemInit.CL_FRED_SPAWN);
                        entries.add(SpawnItemInit.CL_BON_SPAWN);
                        entries.add(SpawnItemInit.CL_CHICA_SPAWN);
                        entries.add(SpawnItemInit.CL_FOXY_SPAWN);

                    }).build());

    public static void registerItemGroups() {
        FnafUniverseResuited.LOGGER.info("Registering Item Groups for " + FnafUniverseResuited.MOD_ID);
    }
}
