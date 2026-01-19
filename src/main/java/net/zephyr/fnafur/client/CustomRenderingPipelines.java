package net.zephyr.fnafur.client;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.PolygonMode;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.zephyr.fnafur.decals.DecalManager;

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

    public static final RenderPipeline.Snippet COOL_TERRAIN_SNIPPET = RenderPipeline.builder(RenderPipelines.FOG_AND_SAMPLERS_SNIPPET)
            .withUniform("Projection", UniformType.UNIFORM_BUFFER)
            .withUniform("ChunkSection", UniformType.UNIFORM_BUFFER)
            .withUniform("DecalInfo", UniformType.UNIFORM_BUFFER)
            .withShaderDefine("MAX_DECAL_DISTANCE", DecalManager.MAX_DISTANCE)
            .withVertexShader("core/terrain")
            .withFragmentShader("core/terrain")
            .buildSnippet();

    public static final RenderPipeline COOL_SOLID_TERRAIN = RenderPipelines.register(RenderPipeline.builder(COOL_TERRAIN_SNIPPET).withLocation("pipeline/solid_terrain").build());
    public static final RenderPipeline COOL_WIREFRAME = RenderPipelines.register(
            RenderPipeline.builder(COOL_TERRAIN_SNIPPET).withLocation("pipeline/wireframe").withPolygonMode(PolygonMode.WIREFRAME).build()
    );
    public static final RenderPipeline COOL_CUTOUT_TERRAIN = RenderPipelines.register(
            RenderPipeline.builder(COOL_TERRAIN_SNIPPET).withLocation("pipeline/cutout_terrain").withShaderDefine("ALPHA_CUTOUT", 0.5F).build()
    );
    public static final RenderPipeline COOL_TRANSLUCENT = RenderPipelines.register(
            RenderPipeline.builder(COOL_TERRAIN_SNIPPET)
                    .withLocation("pipeline/translucent_terrain")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withShaderDefine("ALPHA_CUTOUT", 0.01F)
                    .build()
    );
    public static final RenderPipeline COOL_TRIPWIRE_TERRAIN = RenderPipelines.register(
            RenderPipeline.builder(COOL_TERRAIN_SNIPPET)
                    .withLocation("pipeline/tripwire_terrain")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .build()
    );

    public static final RenderPipeline.Snippet ANIMATRONIC_SNIPPET = RenderPipeline.builder(RenderPipelines.TRANSFORMS_PROJECTION_FOG_LIGHTING_SNIPPET)
            .withVertexShader("core/animatronic")
            .withFragmentShader("core/animatronic")
            .withSampler("Sampler0")
            .withSampler("Sampler2")
            .withVertexFormat(VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS)
            .buildSnippet();

    public static final RenderPipeline ANIMATRONIC_TRANSLUCENT = RenderPipelines.register(
            RenderPipeline.builder(ANIMATRONIC_SNIPPET)
                    .withLocation("pipeline/animatronic_translucent")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withShaderDefine("PER_FACE_LIGHTING")
                    .withSampler("Sampler1")
                    .withSampler("Sampler3")
                    .withSampler("Sampler4")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build()
    );
    public static final RenderPipeline ANIMATRONIC_NO_EYES_TRANSLUCENT = RenderPipelines.register(
            RenderPipeline.builder(ANIMATRONIC_SNIPPET)
                    .withLocation("pipeline/animatronic_translucent")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withShaderDefine("PER_FACE_LIGHTING")
                    .withShaderDefine("NO_EYES")
                    .withSampler("Sampler1")
                    .withSampler("Sampler3")
                    .withSampler("Sampler4")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build()
    );
    public static final RenderPipeline.Snippet ANIMATRONIC_SUIT_SNIPPET = RenderPipeline.builder(RenderPipelines.TRANSFORMS_PROJECTION_FOG_LIGHTING_SNIPPET)
            .withVertexShader("core/animatronic_suit")
            .withFragmentShader("core/animatronic_suit")
            .withSampler("Sampler0")
            .withSampler("Sampler2")
            .withVertexFormat(VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS)
            .buildSnippet();

    public static final RenderPipeline ANIMATRONIC_SUIT_TRANSLUCENT = RenderPipelines.register(
            RenderPipeline.builder(ANIMATRONIC_SUIT_SNIPPET)
                    .withLocation("pipeline/animatronic_translucent")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withShaderDefine("PER_FACE_LIGHTING")
                    .withSampler("Sampler1")
                    .withSampler("Sampler3")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build()
    );

    private static final BiFunction<Identifier[], Boolean, RenderLayer> ENTITY_ANIMATRONIC_SUIT = Util.memoize(
            ((textures, affectsOutline) -> {

                RenderSetup renderSetup = RenderSetup.builder(ANIMATRONIC_SUIT_TRANSLUCENT)
                        .texture("Sampler0", textures[0])
                        .texture("Sampler3", textures[1])
                        .useLightmap()
                        .useOverlay()
                        .crumbling()
                        .outlineMode(affectsOutline ? RenderSetup.OutlineMode.AFFECTS_OUTLINE : RenderSetup.OutlineMode.NONE)
                        .build();
                return RenderLayer.of("animatronic_translucent_suit", renderSetup);
            })
    );
    public static final BiFunction<Identifier[], Boolean, RenderLayer> ENTITY_ANIMATRONIC = Util.memoize(
            ((textures, affectsOutline) -> {

                RenderSetup renderSetup = RenderSetup.builder(ANIMATRONIC_TRANSLUCENT)
                        .texture("Sampler0", textures[0])
                        .texture("Sampler3", textures[1])
                        .texture("Sampler4", textures[2])
                        .useLightmap()
                        .useOverlay()
                        .crumbling()
                        .outlineMode(affectsOutline ? RenderSetup.OutlineMode.AFFECTS_OUTLINE : RenderSetup.OutlineMode.NONE)
                        .build();
                return RenderLayer.of("animatronic_translucent", renderSetup);
            })
    );
    public static final BiFunction<Identifier[], Boolean, RenderLayer> ENTITY_ANIMATRONIC_NO_EYES = Util.memoize(
            ((textures, affectsOutline) -> {

                RenderSetup renderSetup = RenderSetup.builder(ANIMATRONIC_NO_EYES_TRANSLUCENT)
                        .texture("Sampler0", textures[0])
                        .texture("Sampler3", textures[1])
                        .texture("Sampler4", textures[2])
                        .useLightmap()
                        .useOverlay()
                        .crumbling()
                        .outlineMode(affectsOutline ? RenderSetup.OutlineMode.AFFECTS_OUTLINE : RenderSetup.OutlineMode.NONE)
                        .build();
                return RenderLayer.of("animatronic_no_eyes_translucent", renderSetup);
            })
    );

    public static RenderPipeline cosmoSpace() {
        return COSMO_SPACE;
    }


    public static RenderLayer getAnimatronicSuit(Identifier main, Identifier map) {
        Identifier[] textures = new Identifier[2];
        textures[0] = main;
        textures[1] = map;
        return ENTITY_ANIMATRONIC_SUIT.apply(textures, true);
    }
    public static  RenderLayer getAnimatronic(Identifier main, Identifier eyes, Identifier eyes_map) {
        Identifier[] textures = new Identifier[3];
        textures[0] = main;
        textures[1] = eyes;
        textures[2] = eyes_map;
        return ENTITY_ANIMATRONIC.apply(textures, true);
    }
    public static  RenderLayer getAnimatronicNoEyes(Identifier main, Identifier eyes, Identifier eyes_map) {
        Identifier[] textures = new Identifier[3];
        textures[0] = main;
        textures[1] = eyes;
        textures[2] = eyes_map;
        return ENTITY_ANIMATRONIC_NO_EYES.apply(textures, true);
    }
    public static  RenderLayer getItemWithArms(Identifier main, Identifier eyes, Identifier eyes_map) {
        Identifier[] textures = new Identifier[3];
        textures[0] = main;
        textures[1] = eyes;
        textures[2] = eyes_map;
        return ENTITY_ANIMATRONIC.apply(textures, true);
    }
}
