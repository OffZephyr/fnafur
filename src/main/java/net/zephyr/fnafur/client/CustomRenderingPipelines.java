package net.zephyr.fnafur.client;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.platform.PolygonMode;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.zephyr.fnafur.rendering.decals.DecalManager;
import net.zephyr.fnafur.rendering.lighting.AreaLightManager;
import net.zephyr.fnafur.rendering.lighting.AreaLightShadowResources;

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

    public static final RenderPipeline.Snippet NORMALS_PREPASS_SNIPPET =
            RenderPipeline.builder(RenderPipelines.FOG_AND_SAMPLERS_SNIPPET)
                    .withUniform("Projection", UniformType.UNIFORM_BUFFER)
                    .withUniform("ChunkSection", UniformType.UNIFORM_BUFFER)
                    .withVertexShader("core/terrain")
                    .withFragmentShader("core/normals_prepass")
                    .buildSnippet();

    public static final RenderPipeline NORMALS_PREPASS =
            RenderPipelines.register(
                    RenderPipeline.builder(NORMALS_PREPASS_SNIPPET)
                            .withLocation("pipeline/normals_prepass")
                            .build()
            );
    public static final RenderPipeline.Snippet POSITION_PREPASS_SNIPPET =
            RenderPipeline.builder(RenderPipelines.FOG_AND_SAMPLERS_SNIPPET)
                    .withUniform("Projection", UniformType.UNIFORM_BUFFER)
                    .withUniform("ChunkSection", UniformType.UNIFORM_BUFFER)
                    .withVertexShader("core/terrain")
                    .withFragmentShader("core/position_prepass")
                    .buildSnippet();

    public static final RenderPipeline POSITION_PREPASS =
            RenderPipelines.register(
                    RenderPipeline.builder(POSITION_PREPASS_SNIPPET)
                            .withLocation("pipeline/position_prepass")
                            // Only write where depth already matches
                            .withDepthTestFunction(DepthTestFunction.EQUAL_DEPTH_TEST)
                            .withDepthWrite(false)
                            .withColorWrite(true)
                            .build()
            );

    public static final RenderPipeline.Snippet LIGHTING_FULLSCREEN_SNIPPET =
            RenderPipeline.builder(
                            RenderPipelines.TRANSFORMS_AND_PROJECTION_SNIPPET,
                            RenderPipelines.GLOBALS_SNIPPET
                    )
                    .withSampler("NormalBuffer")
                    .withSampler("DepthBuffer")
                    .withSampler("Sampler0")
                    .withSampler("PositionBuffer")
                    .withUniform("LightData", UniformType.UNIFORM_BUFFER)

                    .withSampler("ShadowSampler0")
                    .withSampler("ShadowSampler1")
                    .withSampler("ShadowSampler2")
                    .withSampler("ShadowSampler3")
                    .withSampler("ShadowSampler4")
                    .withSampler("ShadowSampler5")
                    .withSampler("ShadowSampler6")
                    .withSampler("ShadowSampler7")

                    .withShaderDefine("MAX_LIGHTS", AreaLightManager.MAX_LIGHTS)
                    .withShaderDefine("MAX_SHADOWED_LIGHTS", AreaLightShadowResources.MAX_SHADOWED_LIGHTS)
                    .withShaderDefine("SHADOW_RES", AreaLightShadowResources.SHADOW_RES)

                    .withVertexShader("core/lighting_fullscreen")
                    .withFragmentShader("core/lighting_fullscreen")

                    // simple position-only buffer (we created fsTri with 2 floats per vert)
                    .withVertexFormat(VertexFormats.POSITION, VertexFormat.DrawMode.TRIANGLES)
                    .buildSnippet();

    public static final RenderPipeline LIGHTING_FULLSCREEN =
            RenderPipelines.register(
                    RenderPipeline.builder(LIGHTING_FULLSCREEN_SNIPPET)
                            .withLocation("pipeline/lighting_fullscreen")
                            .build()
            );



    public static final RenderPipeline.Snippet COOL_TERRAIN_SNIPPET = RenderPipeline.builder(RenderPipelines.FOG_AND_SAMPLERS_SNIPPET)
            .withUniform("Projection", UniformType.UNIFORM_BUFFER)
            .withUniform("ChunkSection", UniformType.UNIFORM_BUFFER)
            .withUniform("DecalInfo", UniformType.UNIFORM_BUFFER)

            .withUniform("LightData", UniformType.UNIFORM_BUFFER)

            .withSampler("ShadowSampler0")
            .withSampler("ShadowSampler1")
            .withSampler("ShadowSampler2")
            .withSampler("ShadowSampler3")
            .withSampler("ShadowSampler4")
            .withSampler("ShadowSampler5")
            .withSampler("ShadowSampler6")
            .withSampler("ShadowSampler7")

            .withSampler("LightBuffer")

            .withShaderDefine("MAX_DECAL_DISTANCE", DecalManager.MAX_DISTANCE)
            .withShaderDefine("MAX_LIGHTS", AreaLightManager.MAX_LIGHTS)
            .withShaderDefine("MAX_SHADOWED_LIGHTS", AreaLightShadowResources.MAX_SHADOWED_LIGHTS)
            .withShaderDefine("SHADOW_RES", AreaLightShadowResources.SHADOW_RES)

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

    public static final RenderPipeline.Snippet AREA_LIGHT_SHADOW_SNIPPET =
            RenderPipeline.builder(RenderPipelines.TRANSFORMS_AND_PROJECTION_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
                    .withUniform("ChunkSection", UniformType.UNIFORM_BUFFER)

                    // NEW: Shadow matrix UBO (std140 mat4)
                    .withUniform("LightShadow", UniformType.UNIFORM_BUFFER)

                    .withVertexShader("core/area_light_shadow")
                    .withFragmentShader("core/area_light_shadow")

                    // Must match the chunk section vertex data format that drawMultipleIndexed supplies:
                    .withVertexFormat(VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS)
                    .buildSnippet();

    public static final RenderPipeline AREA_LIGHT_SHADOW =
            RenderPipelines.register(
                    RenderPipeline.builder(AREA_LIGHT_SHADOW_SNIPPET)
                            .withLocation("pipeline/area_light_shadow")
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
                    .withSampler("Sampler5")
                    .withSampler("SamplerEyeColor")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build()
    );
    public static final RenderPipeline ANIMATRONIC_NO_EYES_TRANSLUCENT = RenderPipelines.register(
            RenderPipeline.builder(ANIMATRONIC_SNIPPET)
                    .withLocation("pipeline/animatronic_no_eyes_translucent")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withShaderDefine("PER_FACE_LIGHTING")
                    .withShaderDefine("NO_EYES")
                    .withSampler("Sampler1")
                    .withSampler("Sampler3")
                    .withSampler("Sampler4")
                    .withSampler("Sampler5")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build()
    );
    public static final RenderPipeline ANIMATRONIC_GLOWING_EYES_DOTS_TRANSLUCENT = RenderPipelines.register(
            RenderPipeline.builder(ANIMATRONIC_SNIPPET)
                    .withLocation("pipeline/animatronic_glowing_eyes_translucent")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withShaderDefine("PER_FACE_LIGHTING")
                    .withShaderDefine("DOTS")
                    .withSampler("Sampler1")
                    .withSampler("Sampler3")
                    .withSampler("Sampler4")
                    .withSampler("Sampler5")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build()
    );
    public static final RenderPipeline ANIMATRONIC_GLOWING_EYES_IRISES_TRANSLUCENT = RenderPipelines.register(
            RenderPipeline.builder(ANIMATRONIC_SNIPPET)
                    .withLocation("pipeline/animatronic_glowing_eyes_translucent")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withShaderDefine("PER_FACE_LIGHTING")
                    .withShaderDefine("GLOWING_IRISES")
                    .withSampler("Sampler1")
                    .withSampler("Sampler3")
                    .withSampler("Sampler4")
                    .withSampler("Sampler5")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build()
    );
    public static final RenderPipeline ANIMATRONIC_GLOWING_EYES_IRISES_DOTS_TRANSLUCENT = RenderPipelines.register(
            RenderPipeline.builder(ANIMATRONIC_SNIPPET)
                    .withLocation("pipeline/animatronic_glowing_eyes_translucent")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withShaderDefine("PER_FACE_LIGHTING")
                    .withShaderDefine("GLOWING_IRISES")
                    .withShaderDefine("DOTS")
                    .withSampler("Sampler1")
                    .withSampler("Sampler3")
                    .withSampler("Sampler4")
                    .withSampler("Sampler5")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build()
    );
    public static final RenderPipeline ANIMATRONIC_GLOWING_EYES_FULL_EYES_TRANSLUCENT = RenderPipelines.register(
            RenderPipeline.builder(ANIMATRONIC_SNIPPET)
                    .withLocation("pipeline/animatronic_glowing_eyes_translucent")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withShaderDefine("PER_FACE_LIGHTING")
                    .withShaderDefine("GLOWING_EYES")
                    .withSampler("Sampler1")
                    .withSampler("Sampler3")
                    .withSampler("Sampler4")
                    .withSampler("Sampler5")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build()
    );
    public static final RenderPipeline ANIMATRONIC_GLOWING_EYES_FULL_EYES_DOTS_TRANSLUCENT = RenderPipelines.register(
            RenderPipeline.builder(ANIMATRONIC_SNIPPET)
                    .withLocation("pipeline/animatronic_glowing_eyes_translucent")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withShaderDefine("PER_FACE_LIGHTING")
                    .withShaderDefine("GLOWING_EYES")
                    .withShaderDefine("DOTS")
                    .withSampler("Sampler1")
                    .withSampler("Sampler3")
                    .withSampler("Sampler4")
                    .withSampler("Sampler5")
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
                    .withShaderDefine("NO_EYES")
                    .withSampler("Sampler1")
                    .withSampler("Sampler3")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build()
    );

    private static final BiFunction<AnimatronicTexture, Boolean, RenderLayer> ENTITY_ANIMATRONIC_SUIT = Util.memoize(
            ((texture, affectsOutline) -> {

                RenderSetup renderSetup = RenderSetup.builder(ANIMATRONIC_SUIT_TRANSLUCENT)
                        .texture("Sampler0", texture.main)
                        .texture("Sampler3", texture.eyes)
                        .useLightmap()
                        .useOverlay()
                        .crumbling()
                        .outlineMode(affectsOutline ? RenderSetup.OutlineMode.AFFECTS_OUTLINE : RenderSetup.OutlineMode.NONE)
                        .build();
                return RenderLayer.of("animatronic_translucent_suit", renderSetup);
            })
    );
    public static final BiFunction<AnimatronicTexture, Boolean, RenderLayer> ENTITY_ANIMATRONIC = Util.memoize(
            ((texture, affectsOutline) -> {

                RenderSetup renderSetup = RenderSetup.builder(ANIMATRONIC_TRANSLUCENT)
                        .texture("Sampler0", texture.main)
                        .texture("Sampler3", texture.eyes)
                        .texture("Sampler4", texture.eyes_map)
                        .texture("Sampler5", texture.glow_color)
                        .useLightmap()
                        .useOverlay()
                        .crumbling()
                        .outlineMode(affectsOutline ? RenderSetup.OutlineMode.AFFECTS_OUTLINE : RenderSetup.OutlineMode.NONE)
                        .build();
                return RenderLayer.of("animatronic_translucent", renderSetup);
            })
    );
    public static final BiFunction<AnimatronicTexture, Boolean, RenderLayer> ENTITY_ANIMATRONIC_NO_EYES = Util.memoize(
            ((texture, affectsOutline) -> {

                RenderSetup renderSetup = RenderSetup.builder(ANIMATRONIC_NO_EYES_TRANSLUCENT)
                        .texture("Sampler0", texture.main)
                        .texture("Sampler3", texture.eyes)
                        .texture("Sampler4", texture.eyes_map)
                        .texture("Sampler5", texture.glow_color)
                        .useLightmap()
                        .useOverlay()
                        .crumbling()
                        .outlineMode(affectsOutline ? RenderSetup.OutlineMode.AFFECTS_OUTLINE : RenderSetup.OutlineMode.NONE)
                        .build();
                return RenderLayer.of("animatronic_no_eyes_translucent", renderSetup);
            })
    );
    public static final BiFunction<AnimatronicTexture, Boolean, RenderLayer> ENTITY_ANIMATRONIC_GLOWING_EYES_DOTS = Util.memoize(
            ((texture, affectsOutline) -> {

                RenderSetup renderSetup = RenderSetup.builder(ANIMATRONIC_GLOWING_EYES_DOTS_TRANSLUCENT)
                        .texture("Sampler0", texture.main)
                        .texture("Sampler3", texture.eyes)
                        .texture("Sampler4", texture.eyes_map)
                        .texture("Sampler5", texture.glow_color)
                        .useLightmap()
                        .useOverlay()
                        .crumbling()
                        .outlineMode(affectsOutline ? RenderSetup.OutlineMode.AFFECTS_OUTLINE : RenderSetup.OutlineMode.NONE)
                        .build();
                return RenderLayer.of("animatronic_glowing_eyes_translucent", renderSetup);
            })
    );
    public static final BiFunction<AnimatronicTexture, Boolean, RenderLayer> ENTITY_ANIMATRONIC_GLOWING_EYES_IRISES = Util.memoize(
            ((texture, affectsOutline) -> {

                RenderSetup renderSetup = RenderSetup.builder(ANIMATRONIC_GLOWING_EYES_IRISES_TRANSLUCENT)
                        .texture("Sampler0", texture.main)
                        .texture("Sampler3", texture.eyes)
                        .texture("Sampler4", texture.eyes_map)
                        .texture("Sampler5", texture.glow_color)
                        .useLightmap()
                        .useOverlay()
                        .crumbling()
                        .outlineMode(affectsOutline ? RenderSetup.OutlineMode.AFFECTS_OUTLINE : RenderSetup.OutlineMode.NONE)
                        .build();
                return RenderLayer.of("animatronic_glowing_eyes_translucent", renderSetup);
            })
    );
    public static final BiFunction<AnimatronicTexture, Boolean, RenderLayer> ENTITY_ANIMATRONIC_GLOWING_EYES_FULL_EYES = Util.memoize(
            ((texture, affectsOutline) -> {

                RenderSetup renderSetup = RenderSetup.builder(ANIMATRONIC_GLOWING_EYES_FULL_EYES_TRANSLUCENT)
                        .texture("Sampler0", texture.main)
                        .texture("Sampler3", texture.eyes)
                        .texture("Sampler4", texture.eyes_map)
                        .texture("Sampler5", texture.glow_color)
                        .useLightmap()
                        .useOverlay()
                        .crumbling()
                        .outlineMode(affectsOutline ? RenderSetup.OutlineMode.AFFECTS_OUTLINE : RenderSetup.OutlineMode.NONE)
                        .build();
                return RenderLayer.of("animatronic_glowing_eyes_translucent", renderSetup);
            })
    );
    public static final BiFunction<AnimatronicTexture, Boolean, RenderLayer> ENTITY_ANIMATRONIC_GLOWING_EYES_IRISES_DOTS = Util.memoize(
            ((texture, affectsOutline) -> {

                RenderSetup renderSetup = RenderSetup.builder(ANIMATRONIC_GLOWING_EYES_IRISES_DOTS_TRANSLUCENT)
                        .texture("Sampler0", texture.main)
                        .texture("Sampler3", texture.eyes)
                        .texture("Sampler4", texture.eyes_map)
                        .texture("Sampler5", texture.glow_color)
                        .useLightmap()
                        .useOverlay()
                        .crumbling()
                        .outlineMode(affectsOutline ? RenderSetup.OutlineMode.AFFECTS_OUTLINE : RenderSetup.OutlineMode.NONE)
                        .build();
                return RenderLayer.of("animatronic_glowing_eyes_translucent", renderSetup);
            })
    );
    public static final BiFunction<AnimatronicTexture, Boolean, RenderLayer> ENTITY_ANIMATRONIC_GLOWING_EYES_FULL_EYES_DOTS = Util.memoize(
            ((texture, affectsOutline) -> {

                RenderSetup renderSetup = RenderSetup.builder(ANIMATRONIC_GLOWING_EYES_FULL_EYES_DOTS_TRANSLUCENT)
                        .texture("Sampler0", texture.main)
                        .texture("Sampler3", texture.eyes)
                        .texture("Sampler4", texture.eyes_map)
                        .texture("Sampler5", texture.glow_color)
                        .useLightmap()
                        .useOverlay()
                        .crumbling()
                        .outlineMode(affectsOutline ? RenderSetup.OutlineMode.AFFECTS_OUTLINE : RenderSetup.OutlineMode.NONE)
                        .build();
                return RenderLayer.of("animatronic_glowing_eyes_translucent", renderSetup);
            })
    );

    public static RenderPipeline cosmoSpace() {
        return COSMO_SPACE;
    }


    public static RenderLayer getAnimatronicSuit(Identifier main, Identifier map) {
        AnimatronicTexture texture = new AnimatronicTexture(main, map, map, map);
        return ENTITY_ANIMATRONIC_SUIT.apply(texture, true);
    }
    public static  RenderLayer getAnimatronic(Identifier main, Identifier eyes, Identifier eyes_map, Identifier glow_color) {
        AnimatronicTexture texture = new AnimatronicTexture(main, eyes, eyes_map, glow_color);
        return ENTITY_ANIMATRONIC.apply(texture, true);
    }

    public static  RenderLayer getAnimatronicGlowingEyesDots(Identifier main, Identifier eyes, Identifier eyes_map, Identifier glow_color) {
        AnimatronicTexture texture = new AnimatronicTexture(main, eyes, eyes_map, glow_color);
        return ENTITY_ANIMATRONIC_GLOWING_EYES_DOTS.apply(texture, true);
    }
    public static  RenderLayer getAnimatronicGlowingEyesIrises(Identifier main, Identifier eyes, Identifier eyes_map, Identifier glow_color) {
        AnimatronicTexture texture = new AnimatronicTexture(main, eyes, eyes_map, glow_color);
        return ENTITY_ANIMATRONIC_GLOWING_EYES_IRISES.apply(texture, true);
    }
    public static  RenderLayer getAnimatronicGlowingEyesIrisesDots(Identifier main, Identifier eyes, Identifier eyes_map, Identifier glow_color) {
        AnimatronicTexture texture = new AnimatronicTexture(main, eyes, eyes_map, glow_color);
        return ENTITY_ANIMATRONIC_GLOWING_EYES_IRISES_DOTS.apply(texture, true);
    }
    public static  RenderLayer getAnimatronicGlowingEyesFullEyes(Identifier main, Identifier eyes, Identifier eyes_map, Identifier glow_color) {
        AnimatronicTexture texture = new AnimatronicTexture(main, eyes, eyes_map, glow_color);
        return ENTITY_ANIMATRONIC_GLOWING_EYES_FULL_EYES.apply(texture, true);
    }
    public static  RenderLayer getAnimatronicGlowingEyesFullEyesDots(Identifier main, Identifier eyes, Identifier eyes_map, Identifier glow_color) {
        AnimatronicTexture texture = new AnimatronicTexture(main, eyes, eyes_map, glow_color);
        return ENTITY_ANIMATRONIC_GLOWING_EYES_FULL_EYES_DOTS.apply(texture, true);
    }

    public static  RenderLayer getAnimatronicNoEyes(Identifier main, Identifier eyes, Identifier eyes_map, Identifier glow_color) {
        AnimatronicTexture texture = new AnimatronicTexture(main, eyes, eyes_map, glow_color);
        return ENTITY_ANIMATRONIC_NO_EYES.apply(texture, true);
    }
    public static  RenderLayer getItemWithArms(Identifier main, Identifier eyes, Identifier eyes_map, Identifier glow_color) {
        AnimatronicTexture texture = new AnimatronicTexture(main, eyes, eyes_map, glow_color);
        return ENTITY_ANIMATRONIC.apply(texture, true);
    }

    record AnimatronicTexture(Identifier main, Identifier eyes, Identifier eyes_map, Identifier glow_color){

    }
}