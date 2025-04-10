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
import net.zephyr.fnafur.blocks.props.floor_props.arcade.SkeeballArcade;
import net.zephyr.fnafur.blocks.props.floor_props.chairs.StarPlasticChair;
import net.zephyr.fnafur.blocks.props.floor_props.chairs.WoodenChair;
import net.zephyr.fnafur.blocks.props.floor_props.floor_monitors.FloorMonitors;
import net.zephyr.fnafur.blocks.props.floor_props.floor_trash.FloorTrash;
import net.zephyr.fnafur.blocks.props.floor_props.kitchen.*;
import net.zephyr.fnafur.blocks.props.floor_props.party_hats.PartyHats;
import net.zephyr.fnafur.blocks.props.floor_props.plushies.BephPlushieBlock;
import net.zephyr.fnafur.blocks.props.floor_props.present_stack.PresentStack;
import net.zephyr.fnafur.blocks.props.floor_props.restroom.Toilet;
import net.zephyr.fnafur.blocks.props.floor_props.supplies.Broom;
import net.zephyr.fnafur.blocks.props.floor_props.supplies.MopBucket;
import net.zephyr.fnafur.blocks.props.floor_props.tables.RetroTableBlock;
import net.zephyr.fnafur.blocks.props.floor_props.tables.fnaf1desk.Fnaf1Desk;
import net.zephyr.fnafur.blocks.props.floor_props.trash_bin.TrashBin;
import net.zephyr.fnafur.blocks.props.floor_props.water_dispenser.WaterDispenser;
import net.zephyr.fnafur.blocks.props.floor_props.wet_floor_sign.WetFloorSign;
import net.zephyr.fnafur.blocks.props.floor_props.wooden_shelf.WoodenShelf;
import net.zephyr.fnafur.blocks.props.other.CeilingTileVent;
import net.zephyr.fnafur.blocks.props.tiling.TableBlock;
import net.zephyr.fnafur.blocks.props.other.CeilingTileVentBlack;
import net.zephyr.fnafur.blocks.props.wall_props.ac_unit.AcUnit;
import net.zephyr.fnafur.blocks.props.wall_props.air_vent.AirVent;
import net.zephyr.fnafur.blocks.props.wall_props.clocks.GeoClockPropRenderer;
import net.zephyr.fnafur.blocks.props.wall_props.clocks.WoodenClock;
import net.zephyr.fnafur.blocks.props.wall_props.kitchen.PotsAndPansRack;
import net.zephyr.fnafur.blocks.props.wall_props.electricity.light_switch.LightSwitch;
import net.zephyr.fnafur.blocks.props.wall_props.office_buttons.OfficeButtons;
import net.zephyr.fnafur.blocks.props.wall_props.punch_in_cards.PunchInCards;
import net.zephyr.fnafur.blocks.props.wall_props.restroom.ToiletPaperRoll;
import net.zephyr.fnafur.blocks.props.wall_props.restroom_sign.RestroomSign;
import net.zephyr.fnafur.blocks.props.wall_props.exit_sign_wall.ExitSign;
import net.zephyr.fnafur.blocks.props.wall_props.stage.WallClouds;
import net.zephyr.fnafur.blocks.props.wall_props.electricity.wall_outlet.WallOutlet;
import net.zephyr.fnafur.blocks.utility_blocks.cosmo_gift.CosmoGift;
import net.zephyr.fnafur.blocks.utility_blocks.cosmo_gift.GalaxyLayerGeoPropRenderer;
import net.zephyr.fnafur.entity.animatronic.block.AnimatronicBlockEntityRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PropInit {
    public static List<Item> PROPS = new ArrayList<>();
    public static List<Item> GEO_PROPS = new ArrayList<>();
    public static List<Item> GEO_PROPS_TRANSLUCENT = new ArrayList<>();

    public static final Block FNAF_1_DESK = registerGeoProp(
            "fnaf1desk",
            Fnaf1Desk::new,
            Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/props/fnaf1desk.png"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/props/fnaf1desk.geo.json"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "animations/block/props/fnaf1desk.animation.json"),
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .noCollision()
    );
    public static final Block STAR_PLASTIC_CHAIR = registerGeoProp(
            "star_plastic_chair",
            StarPlasticChair::new,
            Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/props/fnaf1chair.png"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/props/fnaf1chair.geo.json"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "animations/block/props/fnaf1desk.animation.json"),
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .noCollision()
    );
    public static final Block COSMO_GIFT = registerGeoProp(
            "cosmo_gift",
            CosmoGift::new,
            Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/props/gift_boxes/cosmo_gift.png"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/props/giftbox.geo.json"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "animations/block/props/cosmo_gift.animation.json"),
            AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)
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
    public static final Block PARTY_TABLE = registerBlock(
            "party_table",
            TableBlock::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block PARTY_TABLE_CONFETTI = registerBlock(
            "party_table_confetti",
            TableBlock::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
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
    public static final Block WATER_DISPENSER = registerBlock(
            "water_dispenser",
            WaterDispenser::new,
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
                    .offset(AbstractBlock.OffsetType.NONE)
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
    public static final Block WOODEN_CHAIR = registerBlock(
            "wooden_chair",
            WoodenChair::new,
            AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
    );
    public static final Block EXIT_SIGN = registerBlock(
            "exit_sign",
            ExitSign::new,
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
    public static final Block ICE_CREAM_DISPENSER = registerBlock(
            "ice_cream_dispenser",
            IceCreamDispenser::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
    );
    public static final Block DOUBLE_DOOR_FRIDGE = registerGeoProp(
            "double_door_fridge",
            DoubleDoorFridge::new,
            Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/props/double_fridge.png"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/props/double_door_fridge.geo.json"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "animations/block/props/double_door_fridge.animation.json"),
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
    );
    public static final Block FRIDGE = registerGeoProp(
            "fridge",
            Fridge::new,
            Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/props/fridge.png"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/props/fridge.geo.json"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "animations/block/props/fridge.animation.json"),
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
    );
    public static final Block WOODEN_CLOCK = registerGeoProp(
            "wooden_clock",
            WoodenClock::new,
            Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/props/wooden_clock.png"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/props/wall_clock.geo.json"),
            Identifier.of(FnafUniverseResuited.MOD_ID, "animations/block/props/wall_clock.animation.json"),
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
    );
    public static final Block KITCHEN_PREP_TABLE = registerBlock(
            "kitchen_prep_table",
            KitchenPrepTable::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .offset(AbstractBlock.OffsetType.NONE)
                    .breakInstantly()
                    .noCollision()
    );
    public static final Block SKEEBALL_ARCADE = registerBlock(
            "skeeball_arcade",
            SkeeballArcade::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
    );
    public static final Block POTS_AND_PANS_RACK = registerBlock(
            "pots_and_pans_rack",
            PotsAndPansRack::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
    );
    public static final Block PUNCH_IN_CARDS = registerBlock(
            "punch_in_cards",
            PunchInCards::new,
            AbstractBlock.Settings.copy(Blocks.BAMBOO)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
                    .offset(AbstractBlock.OffsetType.NONE)
    );
    public static final Block PARTY_HAT = registerBlock(
            "party_hats",
            PartyHats::new,
            AbstractBlock.Settings.copy(Blocks.BAMBOO)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
                    .offset(AbstractBlock.OffsetType.NONE)
                    .sounds(BlockSoundGroup.COBWEB)

    );
    public static final Block TOILET_PAPER_ROLL = registerBlock(
            "toilet_paper_roll",
            ToiletPaperRoll::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
                    .sounds(BlockSoundGroup.COBWEB)

    );
    public static final Block TOILET = registerBlock(
            "toilet",
            Toilet::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()

    );
    public static final Block FLOOR_TRASH = registerBlock(
            "floor_trash",
            FloorTrash::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .breakInstantly()
                    .noCollision()
                    .sounds(BlockSoundGroup.COBWEB)

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
        BlockEntityRendererFactories.register(BlockEntityInit.ANIMATRONIC_BLOCK, AnimatronicBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlockEntityInit.GEO_CLOCK_PROP, GeoClockPropRenderer::new);
        BlockEntityRendererFactories.register(BlockEntityInit.GALAXY_GEO_PROPS, GalaxyLayerGeoPropRenderer::new);

        for (Item item : PROPS) {
            BlockRenderLayerMap.INSTANCE.putBlock(((BlockItem)item).getBlock(), RenderLayer.getCutout());
        }
        for (Item item : GEO_PROPS) {
            BlockRenderLayerMap.INSTANCE.putBlock(((BlockItem)item).getBlock(), RenderLayer.getCutout());
        }
        for (Item item : GEO_PROPS_TRANSLUCENT) {
            BlockRenderLayerMap.INSTANCE.putBlock(((BlockItem)item).getBlock(), RenderLayer.getTranslucent());
        }

        FnafUniverseResuited.LOGGER.info("Registering Props On CLIENT for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }

    public static void registerProps() {
        FnafUniverseResuited.LOGGER.info("Registering Props for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }
}
