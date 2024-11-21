package net.zephyr.fnafur.util.mixinAccessing;

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.util.Identifier;

public interface IPostProcessorLoader {

    void loadPostProcessor(Identifier id);
    void setMonitorPostProcessor(Identifier id, Framebuffer framebuffer);
    void render(FrameGraphBuilder builder, int textureWidth, int textureHeight, PostEffectProcessor.FramebufferSet framebufferSet);
    void renderMonitor(float delta, boolean bool,Framebuffer framebuffer);
    void setMonitorUniform(Framebuffer buffer, String uniform, float value1, float value2, float value3);
    void setMonitorUniform(Framebuffer buffer, String uniform, float value1, float value2);
    void setMonitorUniform(Framebuffer buffer, String uniform, float value1);
    void clearPostProcessor();
    void resizePostProcessor(Framebuffer framebuffer, int width, int height);
    PostEffectProcessor getMonitorPostProcessor(Framebuffer framebuffer);
}
