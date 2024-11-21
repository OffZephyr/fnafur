package net.zephyr.fnafur.init.block_init;

import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.props.pirates_cove.curtain.PiratesCoveCurtain;
import net.zephyr.fnafur.blocks.props.pirates_cove.curtain.PiratesCoveCurtainRenderer;
import net.zephyr.fnafur.blocks.props.pirates_cove.stage.PiratesCoveStage;
import net.zephyr.fnafur.blocks.props.pirates_cove.stage.PiratesCoveStageRenderer;

import java.util.function.Function;

public class GeoBlockInit {
    public static final Block PIRATES_COVE_STAGE = registerBlock(
            "pirates_cove_stage",
            PiratesCoveStage::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
    );
    public static final Block PIRATES_COVE_CURTAIN = registerBlock(
            "pirates_cove_curtain",
            PiratesCoveCurtain::new,
            AbstractBlock.Settings.copy(Blocks.STONE)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .noCollision()
    );

    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        final Identifier identifier = Identifier.of(FnafUniverseResuited.MOD_ID, name);
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);

        final Block block = Blocks.register(registryKey, factory, settings);
        Items.register(block);
        return block;
    }

    public static void registerGeoBlocksOnClient() {

        BlockEntityRendererRegistry.register(GeoBlockEntityInit.PIRATES_COVE_STAGE, PiratesCoveStageRenderer::new);
        BlockEntityRendererRegistry.register(GeoBlockEntityInit.PIRATES_COVE_CURTAIN, PiratesCoveCurtainRenderer::new);

        //BlockRenderLayerMap.INSTANCE.putBlock(PIRATES_COVE_STAGE, RenderLayer.getCutout());

        FnafUniverseResuited.LOGGER.info("Registering Geo Blocks On CLIENT for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }

    public static void registerGeoBlocks() {
        FnafUniverseResuited.LOGGER.info("Registering Geo Blocks for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }
}
