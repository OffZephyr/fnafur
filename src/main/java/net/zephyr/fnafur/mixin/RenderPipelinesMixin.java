package net.zephyr.fnafur.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gl.UniformType;
import net.zephyr.fnafur.decals.DecalManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderPipelines.class)
public class RenderPipelinesMixin {

    @Shadow @Final @Mutable
    public static final RenderPipeline.Snippet TERRAIN_SNIPPET = RenderPipeline.builder(RenderPipelines.FOG_AND_SAMPLERS_SNIPPET)
            .withUniform("Projection", UniformType.UNIFORM_BUFFER)
            .withUniform("ChunkSection", UniformType.UNIFORM_BUFFER)
            .withUniform("DecalInfo", UniformType.UNIFORM_BUFFER)
            .withShaderDefine("MAX_DECAL_DISTANCE", DecalManager.MAX_DISTANCE)
            .withVertexShader("core/terrain2")
            .withFragmentShader("core/terrain2")
            .buildSnippet();


}
