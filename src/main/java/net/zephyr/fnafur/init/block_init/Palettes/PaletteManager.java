package net.zephyr.fnafur.init.block_init.Palettes;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
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
                int refAbgr = palette.getColorArgb(x, y);
                int repAbgr = palette.getColorArgb(x, y + 1);

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
                int srcAbgr = base.getColorArgb(x, y);

                int replacement = paletteMap.getOrDefault(srcAbgr, srcAbgr);

                result.setColorArgb(x, y, replacement);
            }
        }

        return result;
    }

    public static void registerTexture(
            Identifier id,
            NativeImage image
    ) {
        NativeImageBackedTexture texture =
                new NativeImageBackedTexture(() -> "Generated palette texture: " + id, image);

        MinecraftClient.getInstance()
                .getTextureManager()
                .registerTexture(id, texture);
    }

    public static Identifier getRecoloredIdentifier(String block, PaletteEnum paletteEnum) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/" + block + "_" + paletteEnum.getName());
    }
}
