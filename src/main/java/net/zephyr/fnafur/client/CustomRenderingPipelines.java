package net.zephyr.fnafur.client;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.function.BiFunction;

public class CustomRenderingPipelines {

    private static final RenderPipeline.Snippet RENDERTYPE_COSMO_SPACE_SNIPPET = RenderPipeline.builder(
                    RenderPipelines.TRANSFORMS_AND_PROJECTION_SNIPPET, RenderPipelines.FOG_SNIPPET, RenderPipelines.GLOBALS_SNIPPET
            )
            .withVertexShader("core/rendertype_cosmo_space")
            .withFragmentShader("core/rendertype_cosmo_space")
            .withSampler("Sampler0")
            .withSampler("Sampler1")
            .withVertexFormat(VertexFormats.POSITION, VertexFormat.DrawMode.QUADS)
            .buildSnippet();


    private static final RenderPipeline COSMO_SPACE = RenderPipelines.register(
            RenderPipeline.builder(RENDERTYPE_COSMO_SPACE_SNIPPET)
                    .withLocation("pipeline/cosmo_space")
                    .withShaderDefine("PORTAL_LAYERS", 16)
                    .build()
    );

    public static final RenderPipeline.Snippet ANIMATRONIC_SNIPPET = RenderPipeline.builder(RenderPipelines.TRANSFORMS_PROJECTION_FOG_LIGHTING_SNIPPET)
            .withVertexShader("core/animatronic")
            .withFragmentShader("core/animatronic")
            .withSampler("Sampler0")
            .withSampler("Sampler2")
            .withSampler("Sampler3")
            .withVertexFormat(VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS)
            .buildSnippet();

    public static final RenderPipeline ANIMATRONIC_TRANSLUCENT = RenderPipelines.register(
            RenderPipeline.builder(ANIMATRONIC_SNIPPET)
                    .withLocation("pipeline/animatronic_translucent")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withShaderDefine("PER_FACE_LIGHTING")
                    .withSampler("Sampler1")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build()
    );

    private static final BiFunction<Identifier[], Boolean, RenderLayer> ENTITY_ANIMATRONIC = Util.memoize(
            ((textures, affectsOutline) -> {

                RenderPhase.Textures.Builder textureBuilder = RenderPhase.Textures.create();
                for (Identifier texture : textures) {
                    textureBuilder.add(texture, false);
                }

                RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
                        .texture(textureBuilder.build())
                        .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                        .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
                        .build(affectsOutline);
                return RenderLayer.of("animatronic_translucent", 1536, true, true, ANIMATRONIC_TRANSLUCENT, multiPhaseParameters);
            })
    );

    public static RenderPipeline cosmoSpace() {
        return COSMO_SPACE;
    }


    public static RenderLayer getAnimatronic(Identifier main, Identifier map) {
        Identifier[] textures = new Identifier[2];
        textures[0] = main;
        textures[1] = map;
        return ENTITY_ANIMATRONIC.apply(textures, true);
    }
}
