package net.zephyr.fnafur.init.block_init;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.curtain.CurtainBlockEntity;
import net.zephyr.fnafur.blocks.energy.blocks.generators.GeneratorBlockEntity;
import net.zephyr.fnafur.blocks.energy.blocks.switches.office_buttons.OfficeButtonsBlockEntity;
import net.zephyr.fnafur.blocks.energy.entity.BaseEnergyBlockEntity;
import net.zephyr.fnafur.blocks.fog.FogBlockEntity;
import net.zephyr.fnafur.blocks.linking.presets.SimpleEnergySwitchPropBlockEntity;
import net.zephyr.fnafur.blocks.linking.presets.SimpleEnergyTargetPropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.blocks.dynamic.tiling.tile_doors.TileDoorBlockEntity;
import net.zephyr.fnafur.blocks.props.wall_props.clocks.GeoClockPropBlockEntity;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlockEntity;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.chip_reader.ChipReaderBlockEntity;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.cosmo_gift.GalaxyLayerGeoPropEntity;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.cpu_config_panel.CpuConfigPanelBlockEntity;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.server_monitor.ServerMonitorBlockEntity;

public class BlockEntityInit {
    public static BlockEntityType<CpuConfigPanelBlockEntity> CPU_CONFIG_PANEL;
    public static BlockEntityType<ChipReaderBlockEntity> CHIP_READER;
    public static BlockEntityType<ServerMonitorBlockEntity> SERVER_MONITOR;
    public static BlockEntityType<FogBlockEntity> FOG_BLOCK;
    public static BlockEntityType<PropBlockEntity> PROPS;
    public static BlockEntityType<OfficeButtonsBlockEntity> OFFICE_BUTTONS;
    public static BlockEntityType<SimpleEnergySwitchPropBlockEntity> SIMPLE_PROP_ENERGY_SWITCH;
    public static BlockEntityType<SimpleEnergyTargetPropBlockEntity> SIMPLE_PROP_ENERGY_TARGET;
    public static BlockEntityType<GeoPropBlockEntity> GEO_PROPS;
    public static BlockEntityType<GeoClockPropBlockEntity> GEO_CLOCK_PROP;
    public static BlockEntityType<GalaxyLayerGeoPropEntity> GALAXY_GEO_PROPS;
    public static BlockEntityType<StickerBlockEntity> STICKER_BLOCK;
    public static BlockEntityType<TileDoorBlockEntity> TILE_DOOR;
    public static BlockEntityType<BaseEnergyBlockEntity> ENERGY;
    public static BlockEntityType<GeneratorBlockEntity> GENERATOR;
    public static BlockEntityType<CurtainBlockEntity> CURTAIN;

    public static void registerBlockEntities() {
        CPU_CONFIG_PANEL =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "cpu_config_panel"),
                        FabricBlockEntityTypeBuilder.create(CpuConfigPanelBlockEntity::new,
                                BlockInit.CPU_CONFIG_PANEL).build());
        CHIP_READER =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "chip_reader"),
                        FabricBlockEntityTypeBuilder.create(ChipReaderBlockEntity::new,
                                BlockInit.CHIP_READER).build());
        SERVER_MONITOR =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "server_monitor"),
                        FabricBlockEntityTypeBuilder.create(ServerMonitorBlockEntity::new,
                                BlockInit.SERVER_MONITOR).build());
        FOG_BLOCK =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "fog_block"),
                        FabricBlockEntityTypeBuilder.create(FogBlockEntity::new,
                                BlockInit.FOG_BLOCK).build());

        TILE_DOOR =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "tile_door"),
                        FabricBlockEntityTypeBuilder.create(TileDoorBlockEntity::new,
                                BlockInit.GARAGE_DOOR,
                                BlockInit.HEAVY_DOOR,
                                BlockInit.WARNING_HEAVY_DOOR
                        ).build());
        PROPS =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "props"),
                        FabricBlockEntityTypeBuilder.create(PropBlockEntity::new,
                                BlockInit.WORKBENCH,

                                PropInit.FLOOR_MONITORS,
                                PropInit.WOODEN_SHELF,
                                PropInit.AC_UNIT,
                                PropInit.RETRO_TABLE,
                                PropInit.RETRO_STOOL,

                                PropInit.WALL_CLOUDS,
                                PropInit.STAGE_SUN,
                                PropInit.WALL_PIZZA,
                                PropInit.RESTROOM_SIGN,
                                PropInit.POSTER,
                                PropInit.BROOM,
                                PropInit.MOP_BUCKET,
                                PropInit.TRASH_BIN,
                                PropInit.WET_FLOOR_SIGN,
                                PropInit.PRESENT_STACK,
                                PropInit.EXIT_SIGN,
                                PropInit.WALL_OUTLET,
                                PropInit.AIR_VENT,
                                PropInit.PIZZA_OVEN,
                                PropInit.KITCHEN_PREP_TABLE,
                                PropInit.PUNCH_IN_CARDS,
                                PropInit.BULLETIN_BOARD,
                                PropInit.TOOL_WALL_MOUNT,
                                PropInit.POTS_AND_PANS_RACK,
                                PropInit.SKEEBALL_ARCADE,
                                PropInit.WOODEN_CHAIR,
                                PropInit.WOODEN_STOOL,
                                PropInit.PARTY_HAT,
                                PropInit.TOILET_PAPER_ROLL,
                                PropInit.TOILET,
                                PropInit.URINAL,
                                PropInit.BATHROOM_SINK,
                                PropInit.FLOOR_TRASH,
                                PropInit.WATER_DISPENSER,
                                PropInit.ICE_CREAM_DISPENSER,
                                PropInit.SERIOUS_CUTOUT,
                                PropInit.STANDING_MENU,
                                PropInit.SPEAKER,
                                PropInit.STANDING_MICROPHONE,
                                PropInit.STANDING_SPEAKER,
                                PropInit.STANDING_PIANO,
                                PropInit.ARCADE_CABINET,
                                PropInit.FOOD_DISPLAY_CASE,
                                PropInit.RECEPTION_COUNTER,
                                PropInit.CONDIMENT_COUNTER,
                                PropInit.UTENSILS_BOX,
                                PropInit.WALL_MENU,
                                PropInit.ATM,
                                PropInit.WOODEN_CRATE,
                                PropInit.FNAF1_RULES,

                                PropInit.BEPH_PLUSHIE
                        ).build());
        OFFICE_BUTTONS =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "office_buttons"),
                        FabricBlockEntityTypeBuilder.create(OfficeButtonsBlockEntity::new,
                                PropInit.OFFICE_BUTTONS
                        ).build());
        SIMPLE_PROP_ENERGY_SWITCH =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "energy_switch_prop"),
                        FabricBlockEntityTypeBuilder.create(SimpleEnergySwitchPropBlockEntity::new,
                                PropInit.LIGHT_SWITCH
                        ).build());
        SIMPLE_PROP_ENERGY_TARGET =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "energy_target_prop"),
                        FabricBlockEntityTypeBuilder.create(SimpleEnergyTargetPropBlockEntity::new,
                                BlockInit.REDSTONE_CONVERTER
                        ).build());
        GEO_PROPS =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "geo_props"),
                        FabricBlockEntityTypeBuilder.create(GeoPropBlockEntity::new,
                                PropInit.FNAF_1_DESK,
                                PropInit.FLYING_V_GUITAR,
                                PropInit.STAR_PLASTIC_CHAIR,
                                PropInit.DOUBLE_DOOR_FRIDGE,
                                PropInit.FRIDGE,
                                PropInit.PLUSHIE
                        ).build());
        GEO_CLOCK_PROP =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "clock_geo_props"),
                        FabricBlockEntityTypeBuilder.create(GeoClockPropBlockEntity::new,
                                PropInit.WOODEN_CLOCK
                        ).build());

        GALAXY_GEO_PROPS =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "galaxy_geo_props"),
                        FabricBlockEntityTypeBuilder.create(GalaxyLayerGeoPropEntity::new,
                                PropInit.COSMO_GIFT
                        ).build());

        STICKER_BLOCK =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "sticker_block"),
                        FabricBlockEntityTypeBuilder.create(StickerBlockEntity::new,
                                BlockInit.STICKER_BLOCK,
                                BlockInit.MIMIC_FRAME_DIAGONAL,
                                BlockInit.MIMIC_FRAME,
                                BlockInit.MIMIC_FRAME_2x2,
                                BlockInit.MIMIC_FRAME_4x4
                        ).build());

        GENERATOR      =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "generator"),
                        FabricBlockEntityTypeBuilder.create(GeneratorBlockEntity::new,
                                BlockInit.FUEL_GENERATOR
                        ).build());
        //battery.blocks
        ENERGY      =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "energy"),
                        FabricBlockEntityTypeBuilder.create(BaseEnergyBlockEntity::new,
                                BlockInit.ELECTRICAL_LOCKER,
                                BlockInit.CIRCUIT_BREAKER
                        ).build());
        CURTAIN      =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "curtain"),
                        FabricBlockEntityTypeBuilder.create(CurtainBlockEntity::new,
                                BlockInit.CURTAIN_TEST
                        ).build());

        FnafUniverseRebuilt.LOGGER.info("Registering Block Entities for " + FnafUniverseRebuilt.MOD_ID.toUpperCase());
    }
}
