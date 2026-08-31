package net.zephyr.fnafur.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderPipelines;
import com.mojang.blaze3d.shaders.UniformType;
import net.zephyr.fnafur.rendering.decals.DecalManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderPipelines.class)
public class RenderPipelinesMixin {

    @Shadow @Final @Mutable
    public static final RenderPipeline.Snippet TERRAIN_SNIPPET = RenderPipeline.builder(RenderPipelines.GENERIC_BLOCKS_SNIPPET)
            .withUniform("Projection", UniformType.UNIFORM_BUFFER)
            .withUniform("ChunkSection", UniformType.UNIFORM_BUFFER)
            .withUniform("DecalInfo", UniformType.UNIFORM_BUFFER)
            .withShaderDefine("MAX_DECAL_DISTANCE", DecalManager.MAX_DISTANCE)
            .withVertexShader("core/terrain2")
            .withFragmentShader("core/terrain2")
            .buildSnippet();


}
