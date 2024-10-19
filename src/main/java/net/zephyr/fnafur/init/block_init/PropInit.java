package net.zephyr.fnafur.init.block_init;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.props.FloorMonitors.FloorMonitors1;
import net.zephyr.fnafur.blocks.props.FloorMonitors.FloorMonitors2;
import net.zephyr.fnafur.blocks.props.OfficeButtons.OfficeButtons;
import net.zephyr.fnafur.blocks.props.WoodenShelf.WoodenShelf;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.blocks.props.base.PropRenderer;
import net.zephyr.fnafur.blocks.props.plushies.BephPlushieBlock;

import java.util.ArrayList;
import java.util.List;

public class PropInit {
    public static List<Item> propItems = new ArrayList<>();
    public static final Block BEPH_PLUSHIE = registerBlock("beph_plushie",
            new BephPlushieBlock(AbstractBlock.Settings.copy(Blocks.WHITE_WOOL).nonOpaque().allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never).noCollision()));

    public static final Block FLOOR_MONITORS_1 = registerBlock("floor_monitors1",
            new FloorMonitors1(AbstractBlock.Settings.copy(Blocks.STONE).nonOpaque().allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never).noCollision()));

    public static final Block FLOOR_MONITORS_2 = registerBlock("floor_monitors2",
            new FloorMonitors2(AbstractBlock.Settings.copy(Blocks.STONE).nonOpaque().allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never).noCollision()));

    public static final Block WOODEN_SHELF = registerBlock("wooden_shelf",
            new WoodenShelf(AbstractBlock.Settings.copy(Blocks.STONE).nonOpaque().allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never).noCollision()));

    public static final Block OFFICE_BUTTONS = registerBlock("office_buttons",
            new OfficeButtons(AbstractBlock.Settings.copy(Blocks.STONE).nonOpaque().allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never).noCollision()));
    private static Block registerBlock(String name, PropBlock<?> block) {
        propItems.add(registerBlockItem(name, block));
        return Registry.register(Registries.BLOCK, Identifier.of(FnafUniverseResuited.MOD_ID, name), block);
    }
    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, Identifier.of(FnafUniverseResuited.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerPropsOnClient() {
        BlockEntityRendererFactories.register(BlockEntityInit.PROPS, PropRenderer::new);

        for (Item item : propItems) {
            BlockRenderLayerMap.INSTANCE.putBlock(((BlockItem)item).getBlock(), RenderLayer.getCutout());
        }

        FnafUniverseResuited.LOGGER.info("Registering Blocks On CLIENT for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }
}
