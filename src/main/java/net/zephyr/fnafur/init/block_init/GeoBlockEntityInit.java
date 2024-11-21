package net.zephyr.fnafur.init.block_init;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.props.pirates_cove.curtain.PiratesCoveCurtainBlockEntity;
import net.zephyr.fnafur.blocks.props.pirates_cove.stage.PiratesCoveStageBlockEntity;

public class GeoBlockEntityInit {
    public static BlockEntityType<PiratesCoveStageBlockEntity> PIRATES_COVE_STAGE;
    public static BlockEntityType<PiratesCoveCurtainBlockEntity> PIRATES_COVE_CURTAIN;
    public static void registerBlockEntities() {

        PIRATES_COVE_STAGE =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, "pirates_cove_stage"),
                        FabricBlockEntityTypeBuilder.create(PiratesCoveStageBlockEntity::new,
                                GeoBlockInit.PIRATES_COVE_STAGE
                        ).build());

        PIRATES_COVE_CURTAIN =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FnafUniverseResuited.MOD_ID, "pirates_cove_curtain"),
                        FabricBlockEntityTypeBuilder.create(PiratesCoveCurtainBlockEntity::new,
                                GeoBlockInit.PIRATES_COVE_CURTAIN
                        ).build());

        FnafUniverseResuited.LOGGER.info("Registering Geo Block Entities for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }
}
