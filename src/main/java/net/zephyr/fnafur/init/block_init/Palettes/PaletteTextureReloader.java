package net.zephyr.fnafur.init.block_init.Palettes;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.init.block_init.BlockInit;

import java.util.Map;

public class PaletteTextureReloader implements SimpleSynchronousResourceReloadListener {

    @Override
    public Identifier getFabricId() {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "palette_textures");
    }

    @Override
    public void reload(ResourceManager manager) {
        BlockInit.PALETTES.forEach(
                (PaletteBlock) -> {
                    try {
                        NativeImage base = NativeImage.read(
                                manager.getResource(
                                        PaletteBlock.templateTexture()
                                ).get().getInputStream()
                        );

                        NativeImage palette = NativeImage.read(
                                manager.getResource(
                                        PaletteBlock.paletteEnum().getPalette()
                                ).get().getInputStream()
                        );

                        Map<Integer, Integer> map = PaletteManager.buildPaletteMap(palette);
                        NativeImage recolored = PaletteManager.applyPalette(base, map);

                        PaletteManager.registerTexture(
                                PaletteManager.getRecoloredIdentifier(PaletteBlock.name(), PaletteBlock.paletteEnum()),
                                recolored
                        );
                        FnafUniverseRebuilt.LOGGER.info("Built palette {} texture for {} ({})", PaletteBlock.paletteEnum().getName(), PaletteBlock.block().getName(), PaletteManager.getRecoloredIdentifier(PaletteBlock.name(), PaletteBlock.paletteEnum()));
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to build palette " + PaletteBlock.paletteEnum().getName() + " texture for " + PaletteBlock.block().getName(), e);
                    }
                }
        );

    }
}
