package net.zephyr.fnafur.init.block_init.Palettes;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.init.block_init.BlockInit;

import java.util.Map;

public class PaletteTextureReloader implements SimpleSynchronousResourceReloadListener {

    @Override
    public Identifier getFabricId() {
        return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "palette_textures");
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        BlockInit.PALETTES.forEach(
                (PaletteBlock) -> {
                    try {
                        for(Identifier template : PaletteBlock.templateTextures()) {
                            NativeImage base = NativeImage.read(
                                    manager.getResource(
                                            template
                                    ).get().open()
                            );

                            NativeImage palette = NativeImage.read(
                                    manager.getResource(
                                            PaletteBlock.paletteEnum().getPalette()
                                    ).get().open()
                            );

                            Map<Integer, Integer> map = PaletteManager.buildPaletteMap(palette);
                            NativeImage recolored = PaletteManager.applyPalette(base, map);

                            PaletteManager.registerTexture(
                                    PaletteManager.getRecoloredIdentifier(PaletteBlock.name(), PaletteBlock.paletteEnum()),
                                    recolored
                            );
                        }
                        FnafUniverseRebuilt.LOGGER.info("Built palette {} texture for {} ({})", PaletteBlock.paletteEnum().getName(), PaletteBlock.block().getName(), PaletteManager.getRecoloredIdentifier(PaletteBlock.name(), PaletteBlock.paletteEnum()));

                    } catch (Exception e) {
                        throw new RuntimeException("Failed to build palette " + PaletteBlock.paletteEnum().getName() + " texture for " + PaletteBlock.block().getName(), e);
                    }
                }
        );

    }
}
