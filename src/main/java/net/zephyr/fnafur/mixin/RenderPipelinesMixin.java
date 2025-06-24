package net.zephyr.fnafur.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.VertexFormats;
import net.zephyr.fnafur.util.mixinAccessing.IUniverseRenderPipelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(RenderPipelines.class)
public class RenderPipelinesMixin implements IUniverseRenderPipelines {
    @Shadow
    public static RenderPipeline.Snippet TRANSFORMS_AND_PROJECTION_SNIPPET;
    @Shadow
    public static RenderPipeline.Snippet FOG_SNIPPET;
    @Shadow
    public static RenderPipeline.Snippet GLOBALS_SNIPPET;

    @Shadow
    public static RenderPipeline register(RenderPipeline pipeline) {
        return null;
    }

    @Unique
    private static final RenderPipeline.Snippet RENDERTYPE_COSMO_SPACE_SNIPPET = RenderPipeline.builder(
                    TRANSFORMS_AND_PROJECTION_SNIPPET, FOG_SNIPPET, GLOBALS_SNIPPET
            )
            .withVertexShader("core/rendertype_cosmo_space")
            .withFragmentShader("core/rendertype_cosmo_space")
            .withSampler("Sampler0")
            .withSampler("Sampler1")
            .withVertexFormat(VertexFormats.POSITION, VertexFormat.DrawMode.QUADS)
            .buildSnippet();


    @Unique
    private static final RenderPipeline COSMO_SPACE = register(
            RenderPipeline.builder(RENDERTYPE_COSMO_SPACE_SNIPPET)
                    .withLocation("pipeline/cosmo_space")
                    .withShaderDefine("PORTAL_LAYERS", 16)
                    .build()
    );

    @Override
    public RenderPipeline cosmoSpace() {
        return COSMO_SPACE;
    }
}
