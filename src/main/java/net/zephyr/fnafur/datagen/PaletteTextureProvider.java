package net.zephyr.fnafur.datagen;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.block_init.Palettes.PaletteManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class PaletteTextureProvider implements DataProvider {

    private final DataOutput.PathResolver texturePathResolver;
    private final FabricDataOutput output;

    public PaletteTextureProvider(FabricDataOutput output) {
        this.texturePathResolver = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "textures/block");
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
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

    private NativeImage generateRecoloredImage(BlockInit.PaletteBlock palette, Identifier template) throws IOException {

        Path baseTexture = output.getModContainer()
                .findPath("assets/fnafur/" + template.getPath())
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
        return "Palette Textures";
    }

    private static HashCode writeNativeImageAndHash(NativeImage image, Path outPath) throws IOException {
        Files.createDirectories(outPath.getParent());
        image.writeTo(outPath);

        return Hashing.sha256().hashBytes(Files.readAllBytes(outPath));
    }

    public void generate(DataWriter writer, List<CompletableFuture<?>> futures) throws IOException {
        for (BlockInit.PaletteBlock palette : BlockInit.PALETTES) {

            for(int i = 0; i < palette.templateTextures().length; i++) {
                NativeImage recolored = generateRecoloredImage(palette, palette.templateTextures()[i]);

                String suffix = "";
                if(palette.templateTextures().length > 1 && i != 0) suffix = "_" + (i + 1);

                Path outPath = output.resolvePath(DataOutput.OutputType.RESOURCE_PACK)
                        .resolve("fnafur/textures/block/" + palette.name() + "_" + palette.paletteEnum().getName() + suffix + ".png");

                HashCode pngHash = writeNativeImageAndHash(recolored, outPath);

                writer.write(outPath, Files.readAllBytes(outPath), pngHash);

                recolored.close();

            }
        }

    }
}
