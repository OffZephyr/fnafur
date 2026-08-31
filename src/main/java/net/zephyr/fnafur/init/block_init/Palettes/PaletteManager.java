package net.zephyr.fnafur.init.block_init.Palettes;

import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.zephyr.fnafur.FnafUniverseRebuilt;

import java.util.HashMap;
import java.util.Map;

public class PaletteManager {
    /** Convert ABGR (Minecraft format) to RGB int */
    private static int abgrToRgb(int abgr) {
        int r = abgr & 0xFF;
        int g = (abgr >> 8) & 0xFF;
        int b = (abgr >> 16) & 0xFF;
        return (r << 16) | (g << 8) | b;
    }

    /** Convert RGB int back to ABGR, preserving alpha */
    private static int rgbToAbgr(int rgb, int alpha) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return (alpha << 24) | (b << 16) | (g << 8) | r;
    }

    /** Build a palette map from a palette image (reference row → replacement row) */
    public static Map<Integer, Integer> buildPaletteMap(NativeImage palette) {
        Map<Integer, Integer> map = new HashMap<>();

        int width = palette.getWidth();
        int height = palette.getHeight();

        for (int y = 0; y < height - 1; y += 2) {
            for (int x = 0; x < width; x++) {
                int refAbgr = palette.getPixel(x, y);
                int repAbgr = palette.getPixel(x, y + 1);

                int refAlpha = (refAbgr >>> 24) & 0xFF;
                if (refAlpha == 0) continue; // skip transparent

                // Store ABGR directly — no conversion
                map.put(refAbgr, repAbgr);
            }
        }

        return map;
    }

    /** Apply a palette map to a base image */
    public static NativeImage applyPalette(NativeImage base, Map<Integer, Integer> paletteMap) {
        NativeImage result = new NativeImage(base.getWidth(), base.getHeight(), true);

        for (int y = 0; y < base.getHeight(); y++) {
            for (int x = 0; x < base.getWidth(); x++) {
                int srcAbgr = base.getPixel(x, y);

                int replacement = paletteMap.getOrDefault(srcAbgr, srcAbgr);

                result.setPixel(x, y, replacement);
            }
        }

        return result;
    }

    public static void registerTexture(
            Identifier id,
            NativeImage image
    ) {
        DynamicTexture texture =
                new DynamicTexture(() -> "Generated palette texture: " + id, image);

        Minecraft.getInstance()
                .getTextureManager()
                .register(id, texture);
    }

    public static Identifier getRecoloredIdentifier(String block, PaletteEnum paletteEnum) {
        return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/" + block + "_" + paletteEnum.getName());
    }
}
