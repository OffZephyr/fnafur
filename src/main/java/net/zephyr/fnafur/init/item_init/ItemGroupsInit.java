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
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.init.item_init.SpawnItemInit;
import net.zephyr.fnafur.init.item_init.StickerInit;

public class ItemGroupsInit {

    Item icon = new Item(new Item.Settings());
    public static final ItemGroup FNAF_UR = Registry.register(Registries.ITEM_GROUP, Identifier.of(FnafUniverseResuited.MOD_ID, "creative"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable(FnafUniverseResuited.MOD_ID + ".creative"))
                    .noRenderedName()
                    //.texture("blocks.png")
                    .icon(() -> new ItemStack(ItemInit.MOD_LOGO))
                    .entries((displayContext, entries) -> {

                        entries.add(BlockInit.COMPUTER);
                        entries.add(ItemInit.FLOPPYDISK);
                        entries.add(SpawnItemInit.ZEPHYRSPAWN);
                        entries.add(PropInit.BEPH_PLUSHIE);
                        entries.add(ItemInit.DEATHCOIN);
                        entries.add(ItemInit.ILLUSIONDISC);
                        entries.add(BlockInit.LAYERED_BLOCK_BASE);
                        entries.add(BlockInit.CAMERA);
                        entries.add(ItemInit.TABLET);
                        entries.add(ItemInit.WRENCH);
                        entries.add(ItemInit.PAINTBRUSH);
                        entries.add(ItemInit.TAPEMEASURE);

                    }).build());
    public static final ItemGroup FNAF_PROPS = Registry.register(Registries.ITEM_GROUP, Identifier.of(FnafUniverseResuited.MOD_ID, "props"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable(FnafUniverseResuited.MOD_ID + ".props"))
                    .noRenderedName()
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/tabs_fnaf.png"))
                    .icon(() -> new ItemStack(PropInit.FLOOR_MONITORS_1))
                    .entries((displayContext, entries) -> {

                        entries.add(PropInit.FLOOR_MONITORS_1);
                        entries.add(PropInit.FLOOR_MONITORS_2);

                    }).build());
    public static final ItemGroup FNAF_BLOCKS = Registry.register(Registries.ITEM_GROUP, Identifier.of(FnafUniverseResuited.MOD_ID, "blocks"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable(FnafUniverseResuited.MOD_ID + ".blocks"))
                    .noRenderedName()
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/tabs_fnaf.png"))
                    .icon(() -> new ItemStack(BlockInit.BLACK_WHITE_TILES))
                    .entries((displayContext, entries) -> {

                        entries.add(BlockInit.BLACK_WHITE_TILES);
                        entries.add(BlockInit.RED_BLUE_TILES);
                        entries.add(BlockInit.GRAY_WALL);
                        entries.add(BlockInit.DARK_GRAY_WALL);

                        entries.add(StickerInit.BLACK_WHITE_RED_WALL_TILES);
                        entries.add(StickerInit.WALL_GRUNGE);

                    }).build());
    public static final ItemGroup FNAF_ANIMATRONICS = Registry.register(Registries.ITEM_GROUP, Identifier.of(FnafUniverseResuited.MOD_ID, "animatronics"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable(FnafUniverseResuited.MOD_ID + ".animatronics"))
                    .noRenderedName()
                    .texture(Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/tabs_fnaf.png"))
                    .icon(() -> new ItemStack(SpawnItemInit.CL_FRED_SPAWN))
                    .entries((displayContext, entries) -> {

                        entries.add(SpawnItemInit.CL_FRED_SPAWN);

                    }).build());

    public static void registerItemGroups() {
        FnafUniverseResuited.LOGGER.info("Registering Item Groups for " + FnafUniverseResuited.MOD_ID);
    }
}
