package net.zephyr.fnafur.util.mixinAccessing;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gl.RenderPipelines;

public interface IUniverseRenderPipelines {
    RenderPipelines pipelines = new RenderPipelines();
    RenderPipeline cosmoSpace();
    static RenderPipeline getCosmoSpace(){
        return ((IUniverseRenderPipelines)pipelines).cosmoSpace();
    }
}
