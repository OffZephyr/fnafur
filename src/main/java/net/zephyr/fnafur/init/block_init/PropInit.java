package net.zephyr.fnafur.init.block_init;

import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.registry.LandPathNodeTypesRegistry;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.energy.blocks.switches.office_buttons.OfficeButtons;
import net.zephyr.fnafur.blocks.energy.blocks.switches.office_buttons.light_switch.LightSwitch;
import net.zephyr.fnafur.blocks.light.ChainLight;
import net.zephyr.fnafur.blocks.light.HorizontalFacingLight;
import net.zephyr.fnafur.blocks.light.Sconce;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropRenderer;
import net.zephyr.fnafur.blocks.props.floor_props.arcade.ArcadeCabinet;
import net.zephyr.fnafur.blocks.props.floor_props.arcade.SkeeballArcade;
import net.zephyr.fnafur.blocks.props.floor_props.atm.Atm;
import net.zephyr.fnafur.blocks.props.floor_props.chairs.RetroStool;
import net.zephyr.fnafur.blocks.props.floor_props.chairs.StarPlasticChair;
import net.zephyr.fnafur.blocks.props.floor_props.chairs.WoodenChair;
import net.zephyr.fnafur.blocks.props.floor_props.chairs.WoodenStool;
import net.zephyr.fnafur.blocks.props.floor_props.condiment_counter.CondimentCounter;
import net.zephyr.fnafur.blocks.props.floor_props.cutouts.SeriousCutout;
import net.zephyr.fnafur.blocks.props.floor_props.filing_cabinet.FilingCabinet;
import net.zephyr.fnafur.blocks.props.floor_props.floor_mat.FloorMat;
import net.zephyr.fnafur.blocks.props.floor_props.floor_monitors.FloorMonitors;
import net.zephyr.fnafur.blocks.props.floor_props.floor_trash.FloorTrash;
import net.zephyr.fnafur.blocks.props.floor_props.fnaf1desk.Fnaf1Desk;
import net.zephyr.fnafur.blocks.props.floor_props.instruments.flying_v_guitar.FlyingVGuitar;
import net.zephyr.fnafur.blocks.props.floor_props.instruments.flying_v_guitar.FlyingVGuitarItem;
import net.zephyr.fnafur.blocks.props.floor_props.instruments.standing_microphone.StandingMicrophone;
import net.zephyr.fnafur.blocks.props.floor_props.instruments.standing_piano.StandingPiano;
import net.zephyr.fnafur.blocks.props.floor_props.instruments.standing_speaker.StandingSpeaker;
import net.zephyr.fnafur.blocks.props.floor_props.kitchen.*;
import net.zephyr.fnafur.blocks.props.floor_props.kitchen.food_display_case.FoodDisplayCase;
import net.zephyr.fnafur.blocks.props.floor_props.party_hats.PartyHats;
import net.zephyr.fnafur.blocks.props.floor_props.plushies.BephPlushieBlock;
import net.zephyr.fnafur.blocks.props.floor_props.plushies.PlushieBlock;
import net.zephyr.fnafur.blocks.props.floor_props.present_stack.PresentStack;
import net.zephyr.fnafur.blocks.props.floor_props.reception_counter.ReceptionCounter;
import net.zephyr.fnafur.blocks.props.floor_props.restroom.Toilet;
import net.zephyr.fnafur.blocks.props.floor_props.speaker.Speaker;
import net.zephyr.fnafur.blocks.props.floor_props.standing_menu.StandingMenu;
import net.zephyr.fnafur.blocks.props.floor_props.supplies.Broom;
import net.zephyr.fnafur.blocks.props.floor_props.supplies.MopBucket;
import net.zephyr.fnafur.blocks.props.floor_props.tables.RetroTableBlock;
import net.zephyr.fnafur.blocks.props.floor_props.trash_bin.TrashBin;
import net.zephyr.fnafur.blocks.props.floor_props.utensils_box.UtensilsBox;
import net.zephyr.fnafur.blocks.props.floor_props.water_dispenser.WaterDispenser;
import net.zephyr.fnafur.blocks.props.floor_props.wet_floor_sign.WetFloorSign;
import net.zephyr.fnafur.blocks.props.floor_props.wooden_crate.WoodenCrate;
import net.zephyr.fnafur.blocks.props.floor_props.wooden_shelf.WoodenShelf;
import net.zephyr.fnafur.blocks.props.other.CeilingTileVent;
import net.zephyr.fnafur.blocks.props.other.CeilingTileVentBlack;
import net.zephyr.fnafur.blocks.props.other.hanging_stars.HangingStarsBlock;
import net.zephyr.fnafur.blocks.dynamic.tiling.TableBlock;
import net.zephyr.fnafur.blocks.props.wall_props.ac_unit.AcUnit;
import net.zephyr.fnafur.blocks.props.wall_props.air_vent.AirVent;
import net.zephyr.fnafur.blocks.props.wall_props.bulletin_board.BulletinBoard;
import net.zephyr.fnafur.blocks.props.wall_props.clocks.GeoClockPropRenderer;
import net.zephyr.fnafur.blocks.props.wall_props.clocks.WoodenClock;
import net.zephyr.fnafur.blocks.props.wall_props.electricity.wall_outlet.WallOutlet;
import net.zephyr.fnafur.blocks.props.wall_props.exit_arrow.ExitArrow;
import net.zephyr.fnafur.blocks.props.wall_props.exit_sign_wall.ExitSign;
import net.zephyr.fnafur.blocks.props.wall_props.fnaf1_rules.FNaF1Rules;
import net.zephyr.fnafur.blocks.props.wall_props.kitchen.PotsAndPansRack;
import net.zephyr.fnafur.blocks.props.wall_props.poster.Poster;
import net.zephyr.fnafur.blocks.props.wall_props.punch_in_cards.PunchInCards;
import net.zephyr.fnafur.blocks.props.wall_props.restroom.BathroomSink;
import net.zephyr.fnafur.blocks.props.wall_props.restroom.ToiletPaperRoll;
import net.zephyr.fnafur.blocks.props.wall_props.restroom.Urinal;
import net.zephyr.fnafur.blocks.props.wall_props.restroom_sign.RestroomSign;
import net.zephyr.fnafur.blocks.props.wall_props.stage.StageSun;
import net.zephyr.fnafur.blocks.props.wall_props.stage.WallClouds;
import net.zephyr.fnafur.blocks.props.wall_props.tool_wall_mount.ToolWallMount;
import net.zephyr.fnafur.blocks.props.wall_props.wall_menu.WallMenu;
import net.zephyr.fnafur.blocks.props.wall_props.wall_papers.WallPapers;
import net.zephyr.fnafur.blocks.props.wall_props.wall_pizza.WallPizza;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.cosmo_gift.CosmoGift;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.cosmo_gift.GalaxyLayerGeoPropRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PropInit {
    public static List<Block> SOFT_AVOIDED_PROPS = new ArrayList<>();
    public static List<Block> AVOIDED_PROPS = new ArrayList<>();

    public static List<Item> PROPS = new ArrayList<>();
    public static List<Item> GEO_PROPS = new ArrayList<>();
    public static List<Item> GEO_PROPS_TRANSLUCENT = new ArrayList<>();

    public static final Block FNAF_1_DESK = registerGeoProp(
            "fnaf1desk",
            Fnaf1Desk::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/props/fnaf1desk.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/fnaf1desk"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/fnaf1desk"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .noCollision()
    );

    public static final Block FILING_CABINET = registerGeoProp(
            "filing_cabinet",
            FilingCabinet::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/props/filing_cabinet.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/filing_cabinet"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/fnaf1desk"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .noCollision()
    );
    public static final Block FLYING_V_GUITAR = registerGeoProp(
            "flying_v_guitar",
            FlyingVGuitar::new,
            FlyingVGuitarItem::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/props/flying_v_guitar.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/flying_v_guitar"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/fnaf1desk"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .noCollision()
    );
    public static final Block STAR_PLASTIC_CHAIR = registerGeoProp(
            "star_plastic_chair",
            StarPlasticChair::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/props/fnaf1chair.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/fnaf1chair"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/fnaf1desk"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .noCollision()
    );
    public static final Block COSMO_GIFT = registerGeoProp(
            "cosmo_gift",
            CosmoGift::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/props/gift_boxes/cosmo_gift.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/giftbox"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/cosmo_gift"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .noCollision()

    );

    public static final Block PLUSHIE = registerGeoProp(
            "freddy_plush",
            PlushieBlock::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/props/plushies/freddy_plush.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/freddy_plush"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/fnaf1desk"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_WOOL)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .noCollision(),
            List.of(
                    Component.translatable("fnafur.symbol.paintbrush")
            )
    );

    public static final Block BEPH_PLUSHIE = registerBlock(
            "beph_plushie",
            BephPlushieBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .noCollision()
    );
    public static final Block STANDING_MICROPHONE = registerBlock(
            "standing_microphone",
            StandingMicrophone::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block STANDING_SPEAKER = registerBlock(
            "standing_speaker",
            StandingSpeaker::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block STANDING_PIANO = registerBlock(
            "standing_piano",
            StandingPiano::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );

    public static final Block PARTY_TABLE = registerBlock(
            "party_table",
            TableBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
    );
    public static final Block PARTY_TABLE_CONFETTI = registerBlock(
            "party_table_confetti",
            TableBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
    );

    public static final Block WALL_CLOUDS = registerBlock(
            "wall_clouds",
            WallClouds::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block STAGE_SUN = registerBlock(
            "stage_sun",
            StageSun::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );

    public static final Block WALL_PIZZA = registerBlock(
            "wall_pizza",
            WallPizza::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision(),
            List.of(
                    Component.translatable("fnafur.symbol.paintbrush")
            )
    );

    public static final Block HANGING_STARS = registerBlock(
            "hanging_stars",
            HangingStarsBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.COBWEB)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision(),
            List.of(
                    Component.translatable("fnafur.symbol.paintbrush")
            )
    );
    public static final Block FLOOR_MONITORS = registerBlock(
            "floor_monitors",
            FloorMonitors::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .noCollision(),
            List.of(
                    Component.translatable("fnafur.symbol.paintbrush")
            )
    );
    public static final Block WOODEN_SHELF = registerBlock(
            "wooden_shelf",
            WoodenShelf::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .noCollision()
    );
    public static final Block AC_UNIT = registerBlock(
            "ac_unit",
           AcUnit::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .noCollision()
    );
    public static final Block RETRO_TABLE = registerBlock(
            "retro_table",
           RetroTableBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
    );
    public static final Block RETRO_STOOL = registerBlock(
            "retro_stool",
           RetroStool::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .noCollision(),
            List.of(
                    Component.translatable("fnafur.symbol.paintbrush")
            )
    );
    public static final Block OFFICE_BUTTONS = registerBlock(
            "office_buttons",
            OfficeButtons::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .noCollision(),
            List.of(
                    Component.translatable("fnafur.symbol.paintbrush")
            )
    );
    public static final Block CEILING_TILE_VENT = registerBlock(
            "ceiling_tile_vent",
            CeilingTileVent::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block CEILING_TILE_VENT_BLACK = registerBlock(
            "ceiling_tile_vent_black",
            CeilingTileVentBlack::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block RESTROOM_SIGN = registerBlock(
            "restroom_sign",
            RestroomSign::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision(),
            List.of(
                    Component.translatable("fnafur.symbol.paintbrush")
            )
    );
    public static final Block POSTER = registerBlock(
            "poster",
            Poster::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
                    .sound(SoundType.COBWEB),
            List.of(
                    Component.translatable("fnafur.symbol.paintbrush")
            )
    );
    public static final Block WALL_PAPERS = registerBlock(
            "wall_papers",
            WallPapers::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
                    .sound(SoundType.COBWEB),
            List.of(
                    Component.translatable("fnafur.symbol.paintbrush")
            )
    );
    public static final Block FNAF1_RULES = registerBlock(
            "fnaf1_rules",
            FNaF1Rules::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
                    .sound(SoundType.COBWEB),
            List.of(
                    Component.translatable("fnafur.symbol.paintbrush")
            )
    );
    public static final Block BROOM = registerBlock(
            "broom",
            Broom::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .sound(SoundType.LILY_PAD)
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block MOP_BUCKET = registerBlock(
            "mop_bucket",
            MopBucket::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block TRASH_BIN = registerBlock(
            "trash_bin",
            TrashBin::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block WATER_DISPENSER = registerBlock(
            "water_dispenser",
            WaterDispenser::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block WET_FLOOR_SIGN = registerBlock(
            "wet_floor_sign",
            WetFloorSign::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .offsetType(BlockBehaviour.OffsetType.NONE)
                    .noCollision()
    );
    public static final Block STANDING_MENU = registerBlock(
            "standing_menu",
            StandingMenu::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .offsetType(BlockBehaviour.OffsetType.NONE)
                    .noCollision()
    );
    public static final Block WALL_MENU = registerBlock(
            "wall_menu",
            WallMenu::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .offsetType(BlockBehaviour.OffsetType.NONE)
                    .noCollision()
    );
    public static final Block PRESENT_STACK = registerBlock(
            "present_stack",
            PresentStack::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision(),
            List.of(
                    Component.translatable("fnafur.symbol.paintbrush")
            )
    );
    public static final Block WOODEN_CHAIR = registerBlock(
            "wooden_chair",
            WoodenChair::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block WOODEN_STOOL = registerBlock(
            "wooden_stool",
            WoodenStool::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block WOODEN_CRATE = registerBlock(
            "wooden_crate",
            WoodenCrate::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );

    public static final Block SCONCE = registerBlock(
            "sconce",
            Sconce::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_CHAIN)
                    .noOcclusion()
                    .lightLevel(Blocks.litBlockEmission(10))
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision(),
            List.of(
                    Component.translatable("fnafur.symbol.paintbrush")
            )
    );
    public static final Block HANGING_LIGHT = registerBlock(
            "hanging_light",
            ChainLight::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_CHAIN)
                    .noOcclusion()
                    .lightLevel(state -> (state.getValue(BlockStateProperties.LIT) && !state.getValue(ChainLight.CHAIN)) ? 12 : 0 )
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block SPOT_LIGHT = registerBlock(
            "spot_light",
            HorizontalFacingLight::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_CHAIN)
                    .noOcclusion()
                    .lightLevel(Blocks.litBlockEmission(12))
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block HANGING_LIGHTBULB = registerBlock(
            "hanging_lightbulb",
            ChainLight::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_CHAIN)
                    .noOcclusion()
                    .lightLevel(state -> (state.getValue(BlockStateProperties.LIT) && !state.getValue(ChainLight.CHAIN)) ? 10 : 0 )
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block EXIT_SIGN = registerBlock(
            "exit_sign",
            ExitSign::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block EXIT_ARROW = registerBlock(
            "exit_arrow",
            ExitArrow::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
                    .offsetType(BlockBehaviour.OffsetType.NONE),
            List.of(
                    Component.translatable("fnafur.symbol.paintbrush")
            )
    );
    public static final Block WALL_OUTLET = registerBlock(
            "wall_outlet",
            WallOutlet::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block LIGHT_SWITCH = registerBlock(
            "light_switch",
            LightSwitch::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block AIR_VENT = registerBlock(
            "air_vent",
            AirVent::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block FLOOR_MAT = registerBlock(
            "floor_mat",
            FloorMat::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_CARPET)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block PIZZA_OVEN = registerBlock(
            "pizza_oven",
            PizzaOven::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block ICE_CREAM_DISPENSER = registerBlock(
            "ice_cream_dispenser",
            IceCreamDispenser::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block DOUBLE_DOOR_FRIDGE = registerGeoProp(
            "double_door_fridge",
            DoubleDoorFridge::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/props/double_fridge.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/double_door_fridge"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/double_door_fridge"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block FRIDGE = registerGeoProp(
            "fridge",
            Fridge::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/props/fridge.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/fridge"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/fridge"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block WOODEN_CLOCK = registerGeoProp(
            "wooden_clock",
            WoodenClock::new,
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/props/wooden_clock.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/wall_clock"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/wall_clock"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision(),
            List.of(
                    Component.translatable("fnafur.symbol.paintbrush")
            )
    );
    public static final Block KITCHEN_PREP_TABLE = registerBlock(
            "kitchen_prep_table",
            KitchenPrepTable::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .offsetType(BlockBehaviour.OffsetType.NONE)
                    .instabreak()
                    .noCollision()
    );
    public static final Block CONDIMENT_COUNTER = registerBlock(
            "condiment_counter",
            CondimentCounter::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .offsetType(BlockBehaviour.OffsetType.NONE)
                    .instabreak()
                    .noCollision()
    );
    public static final Block UTENSILS_BOX = registerBlock(
            "utensils_box",
            UtensilsBox::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .offsetType(BlockBehaviour.OffsetType.NONE)
                    .instabreak()
                    .noCollision()
    );
    public static final Block FOOD_DISPLAY_CASE = registerBlock(
            "food_display_case",
            FoodDisplayCase::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .offsetType(BlockBehaviour.OffsetType.NONE)
                    .instabreak()
                    .noCollision(),
            List.of(
                    Component.translatable("fnafur.symbol.paintbrush")
            )
    );
    public static final Block RECEPTION_COUNTER = registerBlock(
            "reception_counter",
            ReceptionCounter::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .offsetType(BlockBehaviour.OffsetType.NONE)
                    .instabreak()
                    .noCollision()
    );
    public static final Block SKEEBALL_ARCADE = registerBlock(
            "skeeball_arcade",
            SkeeballArcade::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block ARCADE_CABINET = registerBlock(
            "arcade_cabinet",
            ArcadeCabinet::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision(),
            List.of(
                    Component.translatable("fnafur.symbol.paintbrush")
            )
    );
    public static final Block ATM = registerBlock(
            "atm",
            Atm::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block POTS_AND_PANS_RACK = registerBlock(
            "pots_and_pans_rack",
            PotsAndPansRack::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
    );
    public static final Block PUNCH_IN_CARDS = registerBlock(
            "punch_in_cards",
            PunchInCards::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
                    .offsetType(BlockBehaviour.OffsetType.NONE)
    );
    public static final Block BULLETIN_BOARD = registerBlock(
            "bulletin_board",
            BulletinBoard::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
                    .offsetType(BlockBehaviour.OffsetType.NONE)
    );
    public static final Block TOOL_WALL_MOUNT = registerBlock(
            "tool_wall_mount",
            ToolWallMount::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
                    .offsetType(BlockBehaviour.OffsetType.NONE)
    );
    public static final Block PARTY_HAT = registerBlock(
            "party_hats",
            PartyHats::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
                    .offsetType(BlockBehaviour.OffsetType.NONE)
                    .sound(SoundType.COBWEB),
            List.of(
                    Component.translatable("fnafur.symbol.paintbrush")
            )

    );
    public static final Block TOILET_PAPER_ROLL = registerBlock(
            "toilet_paper_roll",
            ToiletPaperRoll::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
                    .sound(SoundType.COBWEB)

    );
    public static final Block TOILET = registerBlock(
            "toilet",
            Toilet::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()

    );
    public static final Block URINAL = registerBlock(
            "urinal",
            Urinal::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()

    );
    public static final Block BATHROOM_SINK = registerBlock(
            "bathroom_sink",
            BathroomSink::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()

    );
    public static final Block SPEAKER = registerBlock(
            "speaker",
            Speaker::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()

    );
    public static final Block FLOOR_TRASH = registerBlock(
            "floor_trash",
            FloorTrash::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
                    .sound(SoundType.COBWEB),
            List.of(
                    Component.translatable("fnafur.symbol.paintbrush")
            )

    );
    public static final Block SERIOUS_CUTOUT = registerBlock(
            "serious_cutout",
            SeriousCutout::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never)
                    .instabreak()
                    .noCollision()
                    .sound(SoundType.BAMBOO),
            List.of(
                    Component.translatable("fnafur.symbol.paintbrush")
            )
    );

    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties settings) {
        return registerBlock(name, factory, settings, List.of());
    }
    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties settings, List<Component> description) {
        final Identifier identifier = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, name);
        final ResourceKey<Block> registryKey = ResourceKey.create(Registries.BLOCK, identifier);

        final Block block = Blocks.register(registryKey, factory, settings);
        if(description.isEmpty()){
            PROPS.add(Items.registerBlock(block));
        }
        else{
            PROPS.add(Items.registerBlock(block, BlockItem::new, new net.minecraft.world.item.Item.Properties().component(DataComponents.LORE, new ItemLore(description))));
        }
        return block;
    }
    private static Block registerGeoProp(String name, Function<BlockBehaviour.Properties, Block> factory, Identifier texture, Identifier model, Identifier animations, BlockBehaviour.Properties settings) {
        return registerGeoProp(name, factory, texture, model, animations, settings, List.of());
    }
    private static Block registerGeoProp(String name, Function<BlockBehaviour.Properties, Block> factory, Identifier texture, Identifier model, Identifier animations, BlockBehaviour.Properties settings, List<Component> description) {
        return registerGeoProp(name, factory, BlockItem::new, texture, model, animations, settings, description);
    }
    private static Block registerGeoProp(String name, Function<BlockBehaviour.Properties, Block> factory, BiFunction<Block, net.minecraft.world.item.Item.Properties, Item> factory2, Identifier texture, Identifier model, Identifier animations, BlockBehaviour.Properties settings) {
        return registerGeoProp(name, factory, factory2, texture, model, animations, settings, List.of());
    }
    private static Block registerGeoProp(String name, Function<BlockBehaviour.Properties, Block> factory, BiFunction<Block, net.minecraft.world.item.Item.Properties, Item> factory2, Identifier texture, Identifier model, Identifier animations, BlockBehaviour.Properties settings, List<Component> description) {
        final Identifier identifier = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, name);
        final ResourceKey<Block> registryKey = ResourceKey.create(Registries.BLOCK, identifier);

        final Block block = Blocks.register(registryKey, factory, settings);
        ((GeoPropBlock)block).setModelInfo(texture, model, animations);
        if(description.isEmpty()){
            GEO_PROPS.add(Items.registerBlock(block, factory2));
        }
        else{
            GEO_PROPS.add(Items.registerBlock(block, factory2, new net.minecraft.world.item.Item.Properties().component(DataComponents.LORE, new ItemLore(description))));
        }
        return block;
    }

    public static void registerPropsOnClient() {
        BlockColors colors = Minecraft.getInstance().getBlockColors();

        BlockEntityRenderers.register(BlockEntityInit.GEO_PROPS, GeoPropRenderer::new);
        BlockEntityRenderers.register(BlockEntityInit.GEO_CLOCK_PROP, GeoClockPropRenderer::new);
        BlockEntityRenderers.register(BlockEntityInit.GALAXY_GEO_PROPS, GalaxyLayerGeoPropRenderer::new);

        for (Item item : PROPS) {
            SOFT_AVOIDED_PROPS.add(((BlockItem)item).getBlock());
            BlockRenderLayerMap.putBlock(((BlockItem)item).getBlock(), ChunkSectionLayer.CUTOUT);
        }
        for (Item item : GEO_PROPS) {
            SOFT_AVOIDED_PROPS.add(((BlockItem)item).getBlock());
            BlockRenderLayerMap.putBlock(((BlockItem)item).getBlock(), ChunkSectionLayer.CUTOUT);
        }
        for (Item item : GEO_PROPS_TRANSLUCENT) {
            SOFT_AVOIDED_PROPS.add(((BlockItem)item).getBlock());
            BlockRenderLayerMap.putBlock(((BlockItem)item).getBlock(), ChunkSectionLayer.TRANSLUCENT);
        }

        AVOIDED_PROPS.add(PARTY_TABLE);
        AVOIDED_PROPS.add(PARTY_TABLE_CONFETTI);

        BlockRenderLayerMap.putBlock(FOOD_DISPLAY_CASE, ChunkSectionLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(CONDIMENT_COUNTER, ChunkSectionLayer.TRANSLUCENT);

        FnafUniverseRebuilt.LOGGER.info("Registering Props On CLIENT for " + FnafUniverseRebuilt.MOD_ID.toUpperCase());
    }
}
