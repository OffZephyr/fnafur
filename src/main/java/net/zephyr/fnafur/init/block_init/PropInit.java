package net.zephyr.fnafur.init.block_init;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.props.base.PropRenderer;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropRenderer;
import net.zephyr.fnafur.blocks.props.floor_props.floor_monitors.FloorMonitors;
import net.zephyr.fnafur.blocks.props.floor_props.kitchen.PizzaOven;
import net.zephyr.fnafur.blocks.props.floor_props.plushies.BephPlushieBlock;
import net.zephyr.fnafur.blocks.props.floor_props.present_stack.PresentStack;
import net.zephyr.fnafur.blocks.props.floor_props.supplies.Broom;
import net.zephyr.fnafur.blocks.props.floor_props.supplies.MopBucket;
import net.zephyr.fnafur.blocks.props.floor_props.tables.RetroTableBlock;
import net.zephyr.fnafur.blocks.props.floor_props.tables.fnaf1desk.Fnaf1Desk;
import net.zephyr.fnafur.blocks.props.floor_props.trash_bin.TrashBin;
import net.zephyr.fnafur.blocks.props.floor_props.wet_floor_sign.WetFloorSign;
import net.zephyr.fnafur.blocks.props.floor_props.wooden_shelf.WoodenShelf;
import net.zephyr.fnafur.blocks.props.other.CeilingTileVent;
import net.zephyr.fnafur.blocks.props.other.CeilingTileVentBlack;
import net.zephyr.fnafur.blocks.props.wall_props.ac_unit.AcUnit;
import net.zephyr.fnafur.blocks.props.wall_props.air_vent.AirVent;
import net.zephyr.fnafur.blocks.props.wall_props.light_switch.LightSwitch;
import net.zephyr.fnafur.blocks.props.wall_props.office_buttons.OfficeButtons;
import net.zephyr.fnafur.blocks.props.wall_props.restroom_sign.RestroomSign;
import net.zephyr.fnafur.blocks.props.wall_props.exit_sign_wall.ExitSignWall;
import net.zephyr.fnafur.blocks.props.wall_props.stage.WallClouds;
import net.zephyr.fnafur.blocks.props.wall_props.wall_outlet.WallOutlet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PropInit {
    public static List<Item> PROPS = new ArrayList<>();
    public static List<Item> GEO_PROPS = new ArrayList<>();

    public static final Block FNAF_1_DESK = registerGeoProp(
            "fnaf1desk",
            Fnaf1Desk::new,
            Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/props/fnaf1desk.png"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/props/fnaf1desk.geo.json"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/props/fnaf1desk.animation.json"),
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .noCollision()
    );
    public static final Block BEPH_PLUSHIE = registerBlock(
            "beph_plushie",
            BephPlushieBlock::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .noCollision()
    );

    public static final Block WALL_CLOUDS = registerBlock(
            "wall_clouds",
            WallClouds::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
    );
    public static final Block FLOOR_MONITORS = registerBlock(
            "floor_monitors",
            FloorMonitors::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .noCollision()
    );
    public static final Block WOODEN_SHELF = registerBlock(
            "wooden_shelf",
            WoodenShelf::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .noCollision()
    );
    public static final Block AC_UNIT = registerBlock(
            "ac_unit",
           AcUnit::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block RETRO_TABLE = registerBlock(
            "retro_table",
           RetroTableBlock::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block OFFICE_BUTTONS = registerBlock(
            "office_buttons",
            OfficeButtons::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .noCollision()
    );
    public static final Block CEILING_TILE_VENT = registerBlock(
            "ceiling_tile_vent",
            CeilingTileVent::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
    );
    public static final Block CEILING_TILE_VENT_BLACK = registerBlock(
            "ceiling_tile_vent_black",
            CeilingTileVentBlack::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
    );
    public static final Block RESTROOM_SIGN = registerBlock(
            "restroom_sign",
            RestroomSign::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
    );
    public static final Block BROOM = registerBlock(
            "broom",
            Broom::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.LILY_PAD)
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
    );
    public static final Block MOP_BUCKET = registerBlock(
            "mop_bucket",
            MopBucket::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
    );
    public static final Block TRASH_BIN = registerBlock(
            "trash_bin",
            TrashBin::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
    );
    public static final Block WET_FLOOR_SIGN = registerBlock(
            "wet_floor_sign",
            WetFloorSign::new,
            AbstractBlock.Settings.copy(Blocks.BAMBOO)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
    );
    public static final Block PRESENT_STACK = registerBlock(
            "present_stack",
            PresentStack::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
    );
    public static final Block EXIT_SIGN_WALL = registerBlock(
            "exit_sign_wall",
            ExitSignWall::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BARS)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
    );
    public static final Block WALL_OUTLET = registerBlock(
            "wall_outlet",
            WallOutlet::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BARS)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
    );
    public static final Block LIGHT_SWITCH = registerBlock(
            "light_switch",
            LightSwitch::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BARS)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
    );
    public static final Block AIR_VENT = registerBlock(
            "air_vent",
            AirVent::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BARS)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
    );
    public static final Block PIZZA_OVEN = registerBlock(
            "pizza_oven",
            PizzaOven::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
    );

    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        final Identifier identifier = Identifier.of(FnafUniverseResuited.MOD_ID, name);
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);

        final Block block = Blocks.register(registryKey, factory, settings);
        PROPS.add(Items.register(block));
        return block;
    }
    private static Block registerGeoProp(String name, Function<AbstractBlock.Settings, Block> factory, Identifier texture, Identifier model, Identifier animations, AbstractBlock.Settings settings) {
        final Identifier identifier = Identifier.of(FnafUniverseResuited.MOD_ID, name);
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);

        final Block block = Blocks.register(registryKey, factory, settings);
        ((GeoPropBlock)block).setModelInfo(texture, model, animations);
        GEO_PROPS.add(Items.register(block));
        return block;
    }

    public static void registerPropsOnClient() {
        BlockEntityRendererFactories.register(BlockEntityInit.PROPS, PropRenderer::new);
        BlockEntityRendererFactories.register(BlockEntityInit.ENERGY, PropRenderer::new);
        BlockEntityRendererFactories.register(BlockEntityInit.GEO_PROPS, GeoPropRenderer::new);

        for (Item item : PROPS) {
            BlockRenderLayerMap.INSTANCE.putBlock(((BlockItem)item).getBlock(), RenderLayer.getCutout());
        }
        for (Item item : GEO_PROPS) {
            BlockRenderLayerMap.INSTANCE.putBlock(((BlockItem)item).getBlock(), RenderLayer.getCutout());
        }

        FnafUniverseResuited.LOGGER.info("Registering Props On CLIENT for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }

    public static void registerProps() {
        FnafUniverseResuited.LOGGER.info("Registering Props for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }
}
