package net.zephyr.fnafur.init.block_init;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.geo_doors.GeoDoorEntity;
import net.zephyr.fnafur.blocks.props.other.pirates_cove.curtain.PiratesCoveCurtainBlockEntity;
import net.zephyr.fnafur.blocks.props.other.pirates_cove.stage.PiratesCoveStageBlockEntity;

public class GeoBlockEntityInit {
    public static BlockEntityType<PiratesCoveStageBlockEntity> PIRATES_COVE_STAGE;
    public static BlockEntityType<PiratesCoveCurtainBlockEntity> PIRATES_COVE_CURTAIN;
    public static BlockEntityType<GeoDoorEntity> GEO_DOOR;
    public static void registerBlockEntities() {

        PIRATES_COVE_STAGE =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "pirates_cove_stage"),
                        FabricBlockEntityTypeBuilder.create(PiratesCoveStageBlockEntity::new,
                                GeoBlockInit.PIRATES_COVE_STAGE
                        ).build());

        PIRATES_COVE_CURTAIN =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "pirates_cove_curtain"),
                        FabricBlockEntityTypeBuilder.create(PiratesCoveCurtainBlockEntity::new,
                                GeoBlockInit.PIRATES_COVE_CURTAIN
                        ).build());

        GEO_DOOR =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "geo_door"),
                        FabricBlockEntityTypeBuilder.create(GeoDoorEntity::new,
                                GeoBlockInit.SMALL_GRAY_DOOR,
                                GeoBlockInit.TWO_FIVE_RED_DOOR,
                                GeoBlockInit.TWO_FIVE_BLACK_DOOR,
                                GeoBlockInit.TWO_FIVE_GREEN_DOOR,
                                GeoBlockInit.TWO_FIVE_CYAN_DOOR,
                                GeoBlockInit.TWO_FIVE_BROWN_DOOR,
                                GeoBlockInit.TWO_FIVE_RED_DOOR_WINDOW,
                                GeoBlockInit.TWO_FIVE_BLACK_DOOR_WINDOW,
                                GeoBlockInit.TWO_FIVE_GREEN_DOOR_WINDOW,
                                GeoBlockInit.TWO_FIVE_CYAN_DOOR_WINDOW,
                                GeoBlockInit.TWO_FIVE_BROWN_DOOR_WINDOW,
                                GeoBlockInit.BIG_GRAY_DOOR,
                                GeoBlockInit.BIG_MAGENTA_DOOR,
                                GeoBlockInit.BIG_GREEN_DOOR
                        ).build());

        FnafUniverseRebuilt.LOGGER.info("Registering Geo Block Entities for " + FnafUniverseRebuilt.MOD_ID.toUpperCase());
    }
}
