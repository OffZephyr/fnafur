package net.zephyr.fnafur.util.mixinAccessing;

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.util.Identifier;

public interface IPostProcessorLoader {

    void setPostProcessor(Identifier id);
    void setMonitorPostProcessor(Identifier id, Framebuffer framebuffer);
    void render(float delta);
    void renderMonitor(float delta, boolean bool,Framebuffer framebuffer);
    void setMonitorUniform(Framebuffer buffer, String uniform, float value1, float value2, float value3);
    void setMonitorUniform(Framebuffer buffer, String uniform, float value1, float value2);
    void setMonitorUniform(Framebuffer buffer, String uniform, float value1);
    void clearPostProcessor();
    void resizePostProcessor(Framebuffer framebuffer, int width, int height);
    PostEffectProcessor getMonitorPostProcessor(Framebuffer framebuffer);
}
