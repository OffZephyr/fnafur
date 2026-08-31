package net.zephyr.fnafur.datagen;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.data.PackOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.CachedOutput;
import net.zephyr.fnafur.init.decal_init.DecalInit;
import net.zephyr.fnafur.init.block_init.Palettes.PaletteManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DecalPaletteTextureProvider implements DataProvider {

    private final PackOutput.PathProvider texturePathResolver;
    private final FabricPackOutput output;

    public DecalPaletteTextureProvider(FabricPackOutput output) {
        this.texturePathResolver = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "textures/block/decals/");
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        return CompletableFuture.runAsync(() -> {
            try {
                generate(writer, futures);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    private NativeImage loadImage(Path path) throws IOException {
        try (InputStream in = Files.newInputStream(path)) {
            return NativeImage.read(in);
        }
    }

    private NativeImage generateRecoloredImage(DecalInit.PaletteDecal palette) throws IOException {
        Path baseTexture = output.getModContainer()
                .findPath("assets/fnafur/" + palette.templateTexture().getPath())
                .orElseThrow();

        Path paletteTexture = output.getModContainer()
                .findPath("assets/fnafur/" + palette.paletteEnum().getPalette().getPath())
                .orElseThrow();

        NativeImage base = loadImage(baseTexture);
        NativeImage paletteImg = loadImage(paletteTexture);

        Map<Integer, Integer> paletteMap = PaletteManager.buildPaletteMap(paletteImg);
        NativeImage recolored = PaletteManager.applyPalette(base, paletteMap);

        base.close();
        paletteImg.close();

        return recolored;
    }


    @Override
    public String getName() {
        return "DECAL Palette Textures";
    }

    private static HashCode writeNativeImageAndHash(NativeImage image, Path outPath) throws IOException {
        Files.createDirectories(outPath.getParent());
        image.writeToFile(outPath);

        return Hashing.sha256().hashBytes(Files.readAllBytes(outPath));
    }

    public void generate(CachedOutput writer, List<CompletableFuture<?>> futures) throws IOException {
        for (DecalInit.PaletteDecal palette : DecalInit.PALETTES) {
            NativeImage recolored = generateRecoloredImage(palette);

            Path outPath = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                    .resolve("fnafur/textures/block/decals/" + palette.name() + "_" + palette.paletteEnum().getName() + ".png");

            HashCode pngHash = writeNativeImageAndHash(recolored, outPath);

            writer.writeIfNeeded(outPath, Files.readAllBytes(outPath), pngHash);

            recolored.close();
        }

    }
}
