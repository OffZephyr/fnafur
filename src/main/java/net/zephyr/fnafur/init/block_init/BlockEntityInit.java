package net.zephyr.fnafur.init.block_init;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.arcademachine.ArcademachineBlockEntity;
import net.zephyr.fnafur.blocks.basic_blocks.illusion_block.MimicFrameBlockEntity;
import net.zephyr.fnafur.blocks.camera.CameraBlockEntity;
import net.zephyr.fnafur.blocks.camera_desk.CameraDeskBlockEntity;
import net.zephyr.fnafur.blocks.computer.ComputerBlockEntity;
import net.zephyr.fnafur.blocks.fog.FogBlockEntity;
import net.zephyr.fnafur.blocks.basic_blocks.layered_block.LayeredBlockEntity;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.blocks.stickers.base.StickerBlockEntity;
import net.zephyr.fnafur.blocks.tile_doors.TileDoorBlockEntity;

public class BlockEntityInit {
    public static BlockEntityType<ComputerBlockEntity> COMPUTER;
    public static BlockEntityType<LayeredBlockEntity> LAYERED_BLOCK;
    public static BlockEntityType<CameraBlockEntity> CAMERA;
    public static BlockEntityType<CameraDeskBlockEntity> CAMERA_DESK;
    public static BlockEntityType<ArcademachineBlockEntity> ARCADE_MACHINE;
    public static BlockEntityType<FogBlockEntity> FOG_BLOCK;
    public static BlockEntityType<PropBlockEntity> PROPS;
    public static BlockEntityType<StickerBlockEntity> STICKER_BLOCK;
    public static BlockEntityType<TileDoorBlockEntity> TILE_DOOR;
    public static BlockEntityType<MimicFrameBlockEntity> MIMIC_FRAME;

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

        ARCADE_MACHINE =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, "arcade_machine"),
                        FabricBlockEntityTypeBuilder.create(ArcademachineBlockEntity::new,
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

                                PropInit.OFFICE_BUTTONS,

                                PropInit.BEPH_PLUSHIE
                        ).build());

        STICKER_BLOCK =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, "sticker_block"),
                        FabricBlockEntityTypeBuilder.create(StickerBlockEntity::new,
                                BlockInit.GRAY_WALL,
                                BlockInit.DARK_GRAY_WALL,
                                BlockInit.LARGE_BROWN_BRICKS,
                                BlockInit.SMALL_GRAY_BRICKS,
                                BlockInit.SMALL_LIGHT_GRAY_BRICKS,
                                BlockInit.METAL_PLATES,
                                BlockInit.ROUGH_METAL_PLATES
                        ).build());

        MIMIC_FRAME =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, "mimic_frame"),
                        FabricBlockEntityTypeBuilder.create(MimicFrameBlockEntity::new,
                                BlockInit.MIMIC_FRAME,
                                BlockInit.MIMIC_FRAME_SLAB
                        ).build());



        FnafUniverseResuited.LOGGER.info("Registering Block Entities for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }
}
