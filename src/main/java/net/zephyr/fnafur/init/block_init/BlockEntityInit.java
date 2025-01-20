package net.zephyr.fnafur.init.block_init;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.energy.entity.BaseEnergyBlockEntity;
import net.zephyr.fnafur.blocks.illusion_block.MimicFrameBlockEntity;
import net.zephyr.fnafur.blocks.camera.CameraBlockEntity;
import net.zephyr.fnafur.blocks.camera_desk.CameraDeskBlockEntity;
import net.zephyr.fnafur.blocks.computer.ComputerBlockEntity;
import net.zephyr.fnafur.blocks.fog.FogBlockEntity;
import net.zephyr.fnafur.blocks.basic_blocks.layered_block.LayeredBlockEntity;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.blocks.stickers_blocks.StickerBlockEntity;
import net.zephyr.fnafur.blocks.tile_doors.TileDoorBlockEntity;

public class BlockEntityInit {
    public static BlockEntityType<ComputerBlockEntity> COMPUTER;
    public static BlockEntityType<LayeredBlockEntity> LAYERED_BLOCK;
    public static BlockEntityType<CameraBlockEntity> CAMERA;
    public static BlockEntityType<CameraDeskBlockEntity> CAMERA_DESK;
    public static BlockEntityType<FogBlockEntity> FOG_BLOCK;
    public static BlockEntityType<PropBlockEntity> PROPS;
    public static BlockEntityType<GeoPropBlockEntity> GEO_PROPS;
    public static BlockEntityType<StickerBlockEntity> STICKER_BLOCK;
    public static BlockEntityType<TileDoorBlockEntity> TILE_DOOR;
    public static BlockEntityType<MimicFrameBlockEntity> MIMIC_FRAME;
    public static BlockEntityType<BaseEnergyBlockEntity> ENERGY;

    public static void registerBlockEntities() {
        COMPUTER =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, "computer"),
                        FabricBlockEntityTypeBuilder.create(ComputerBlockEntity::new,
                                BlockInit.COMPUTER).build());
        LAYERED_BLOCK =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, "layered_block"),
                        FabricBlockEntityTypeBuilder.create(LayeredBlockEntity::new,
                                BlockInit.LAYERED_BLOCK_BASE).build());

        CAMERA =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, "camera"),
                        FabricBlockEntityTypeBuilder.create(CameraBlockEntity::new,
                                BlockInit.CAMERA).build());

        CAMERA_DESK =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, "camera_desk"),
                        FabricBlockEntityTypeBuilder.create(CameraDeskBlockEntity::new,
                                BlockInit.CAMERA_DESK).build());
        FOG_BLOCK =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, "fog_block"),
                        FabricBlockEntityTypeBuilder.create(FogBlockEntity::new,
                                BlockInit.FOG_BLOCK).build());

        TILE_DOOR =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, "tile_door"),
                        FabricBlockEntityTypeBuilder.create(TileDoorBlockEntity::new,
                                BlockInit.OFFICE_DOOR
                        ).build());
        PROPS =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, "props"),
                        FabricBlockEntityTypeBuilder.create(PropBlockEntity::new,
                                PropInit.FLOOR_MONITORS_1,
                                PropInit.FLOOR_MONITORS_2,
                                PropInit.WOODEN_SHELF,
                                PropInit.RETRO_TABLE,

                                PropInit.WALL_CLOUDS,
                                PropInit.RESTROOM_SIGN,
                                PropInit.BROOM,
                                PropInit.MOP_BUCKET,
                                PropInit.OFFICE_BUTTONS,

                                PropInit.BEPH_PLUSHIE
                        ).build());
        GEO_PROPS =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, "geo_props"),
                        FabricBlockEntityTypeBuilder.create(GeoPropBlockEntity::new,
                                PropInit.FNAF_1_DESK
                        ).build());

        STICKER_BLOCK =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, "sticker_block"),
                        FabricBlockEntityTypeBuilder.create(StickerBlockEntity::new,
                                BlockInit.GRAY_WALL,
                                BlockInit.DARK_GRAY_WALL,
                                BlockInit.BRICK_WALL,
                                BlockInit.BRICK_WALL_DARKER,
                                BlockInit.RED_BRICK_WALL,
                                BlockInit.RED_BRICK_WALL_SMALL,
                                BlockInit.RED_BRICK_WALL_MIXED,
                                BlockInit.BLUE_BRICKS,
                                BlockInit.LARGE_BROWN_BRICKS,
                                BlockInit.LARGE_LIGHT_GRAY_BRICKS,
                                BlockInit.LARGE_WHITE_BRICKS,
                                BlockInit.SMALL_GRAY_BRICKS,
                                BlockInit.SMALL_LIGHT_GRAY_BRICKS,
                                BlockInit.METAL_PLATES,
                                BlockInit.GRUNGE_STONE_BRICKS,
                                BlockInit.GRUNGE_STONE_BRICKS_DIRTY,
                                BlockInit.ROUGH_METAL_PLATES,
                                BlockInit.TAN_THIN_BRICKS,
                                BlockInit.VARIED_TAN_THIN_BRICKS,
                                BlockInit.GRAY_THIN_BRICKS,
                                BlockInit.BROWN_THIN_BRICKS,
                                BlockInit.CYAN_THIN_BRICKS,
                                BlockInit.GREEN_THIN_BRICKS,
                                BlockInit.MAGENTA_THIN_BRICKS,
                                BlockInit.PINK_THIN_BRICKS,
                                BlockInit.ORANGE_THIN_BRICKS,
                                BlockInit.PURPLE_THIN_BRICKS,
                                BlockInit.RED_THIN_BRICKS,
                                BlockInit.WHITE_THIN_BRICKS,
                                BlockInit.YELLOW_THIN_BRICKS
                        ).build());

        MIMIC_FRAME =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, "mimic_frame"),
                        FabricBlockEntityTypeBuilder.create(MimicFrameBlockEntity::new,
                                BlockInit.MIMIC_FRAME,
                                BlockInit.MIMIC_FRAME_SLAB
                        ).build());

        //battery.blocks
        ENERGY      =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, "energy"),
                        FabricBlockEntityTypeBuilder.create(BaseEnergyBlockEntity::new,
                        BlockInit.FUEL_GENERATOR,
                                BlockInit.REDSTONE_CONVERTER
                        ).build());

        FnafUniverseResuited.LOGGER.info("Registering Block Entities for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }
}
