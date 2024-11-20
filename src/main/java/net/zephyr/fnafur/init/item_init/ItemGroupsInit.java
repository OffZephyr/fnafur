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
                        entries.add(PropInit.FLOOR_MONITORS_1);
                        entries.add(PropInit.FLOOR_MONITORS_2);
                        entries.add(PropInit.WOODEN_SHELF);

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
                        entries.add(BlockInit.MIMIC_FRAME_SLAB);

                        entries.add(BlockInit.BLACK_WHITE_TILES);
                        entries.add(BlockInit.RED_BLUE_TILES);
                        entries.add(BlockInit.BLACK_BLUE_TILES);
                        entries.add(BlockInit.BLACK_WHITE_16_TILES);
                        entries.add(BlockInit.BLACK_WHITE_16_TILES_TRIM);
                        entries.add(BlockInit.TAN_16_TILES);
                        entries.add(BlockInit.TAN_16_SPACED_TILES);
                        entries.add(BlockInit.TAN_RAINBOW_16_TILES);

                        entries.add(BlockInit.CARPET_STAR_GREEN);
                        entries.add(BlockInit.CARPET_STAR_CYAN);
                        entries.add(BlockInit.CARPET_STAR_BLUE);
                        entries.add(BlockInit.CARPET_STAR_PURPLE);
                        entries.add(BlockInit.CARPET_STAR_PINK);
                        entries.add(BlockInit.CARPET_STAR_RED);
                        entries.add(BlockInit.CARPET_STAR_ORANGE);
                        entries.add(BlockInit.CARPET_STAR_BROWN);

                        entries.add(BlockInit.GRAY_WALL);
                        entries.add(BlockInit.DARK_GRAY_WALL);
                        entries.add(BlockInit.LARGE_BROWN_BRICKS);
                        entries.add(BlockInit.SMALL_GRAY_BRICKS);
                        entries.add(BlockInit.SMALL_LIGHT_GRAY_BRICKS);
                        entries.add(BlockInit.METAL_PLATES);
                        entries.add(BlockInit.ROUGH_METAL_PLATES);
                        entries.add(BlockInit.CEILING_TILES);
                        entries.add(BlockInit.CEILING_TILES_STAINED);

                        entries.add(BlockInit.BALLPIT);

                        entries.add(StickerInit.BLACK_WHITE_RED_WALL_TILES);
                        entries.add(StickerInit.BLACK_WHITE_RED_WALL_BIG_TILES);
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

                    }).build());

    public static void registerItemGroups() {
        FnafUniverseResuited.LOGGER.info("Registering Item Groups for " + FnafUniverseResuited.MOD_ID);
    }
}
