package net.zephyr.fnafur.util.mixinAccessing;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.renderer.PostChain;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import net.minecraft.resources.Identifier;

public interface IPostProcessorLoader {

    void loadPostProcessor(Identifier id);
    void setMonitorPostProcessor(Identifier id, RenderTarget framebuffer);
    void render(FrameGraphBuilder builder, int textureWidth, int textureHeight, PostChain.TargetBundle framebufferSet);
    void renderMonitor(float delta, boolean bool, RenderTarget framebuffer);
    void setMonitorUniform(RenderTarget buffer, String uniform, float value1, float value2, float value3);
    void setMonitorUniform(RenderTarget buffer, String uniform, float value1, float value2);
    void setMonitorUniform(RenderTarget buffer, String uniform, float value1);
    void clearPostProcessor();
    void resizePostProcessor(RenderTarget framebuffer, int width, int height);
    PostChain getMonitorPostProcessor(RenderTarget framebuffer);
}
