package net.zephyr.fnafur.util.mixinAccessing;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gl.RenderPipelines;
import net.zephyr.fnafur.client.CustomRenderingPipelines;

public interface IUniverseRenderPipelines {
    RenderPipelines pipelines = new RenderPipelines();
    RenderPipeline cosmoSpace();
    static RenderPipeline getCosmoSpace(){
        return CustomRenderingPipelines.cosmoSpace();
    }
}
