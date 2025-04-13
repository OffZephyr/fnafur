package net.zephyr.fnafur.mixin;

import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.DefaultFramebufferSet;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.camera_desk.CameraRenderer;
import net.zephyr.fnafur.client.gui.screens.CameraTabletScreen;
import net.zephyr.fnafur.util.mixinAccessing.IPostProcessorLoader;
import net.zephyr.fnafur.util.mixinAccessing.IPostProcessorUniform;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Mixin(GameRenderer.class)
public class GameRendererMixin implements IPostProcessorLoader {

    @Shadow
    MinecraftClient client;
    @Shadow
    private Identifier postProcessorId;
    @Shadow
    ResourceManager resourceManager;
    private Map<Framebuffer, PostEffectProcessor> monitorPostProcessors = new HashMap<>();

    @Shadow
    private void setPostProcessor(Identifier id) {

    }
    /*@Inject(method = "render", at = @At(value = "HEAD"))
    public void renderMonitorPostProcessor(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        if(!client.skipGameRender) {
            renderMonitor(tickCounter.getLastDuration(), CameraRenderer.isDrawing(), CameraRenderer.getFramebuffer());
        }
    }*/

    @Inject(method = "getFov(Lnet/minecraft/client/render/Camera;FZ)F", at = @At("RETURN"), cancellable = true)
    public void getZoomLevel(CallbackInfoReturnable<Float> callbackInfo) {
        if(MinecraftClient.getInstance().currentScreen instanceof CameraTabletScreen) {
            float fov = 45;
            callbackInfo.setReturnValue(fov);
        }
    }
    @Inject(method = "onResized", at = @At(value = "HEAD"))
    private void illusions$onResized$HEAD(int width, int height, CallbackInfo ci) {
        CameraRenderer.onResize(width, height);
    }

    public void resizePostProcessor(Framebuffer framebuffer, int width, int height){
        PostEffectProcessor monitorPostProcessor = monitorPostProcessors.get(framebuffer);
        if(monitorPostProcessor != null) {
           // monitorPostProcessor.setupDimensions(width, height);
        }
    }

    @Override
    public void loadPostProcessor(Identifier id) {
        setPostProcessor(id);
    }

    @Override
    public void render(FrameGraphBuilder builder, int textureWidth, int textureHeight, PostEffectProcessor.FramebufferSet framebufferSet) {

        PostEffectProcessor postProcessor = this.client.getShaderLoader().loadPostEffect(this.postProcessorId, DefaultFramebufferSet.MAIN_ONLY);
        if(postProcessor != null){
            postProcessor.render(builder, textureWidth, textureHeight, framebufferSet);
        }
    }
    @Override
    public void setMonitorPostProcessor(Identifier id, Framebuffer framebuffer) {
        /*if(monitorPostProcessors.get(framebuffer) == null || !monitorPostProcessors.get(framebuffer).getName().equals(id.toString())) {
            System.out.println(id);
            loadMonitorPostProcessor(id, framebuffer);
        }*/
    }
    @Override
    public void renderMonitor(float delta, boolean bool, Framebuffer framebuffer) {
        PostEffectProcessor monitorPostProcessor = monitorPostProcessors.get(framebuffer);
        if(bool && monitorPostProcessor != null){
            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.resetTextureMatrix();
            //monitorPostProcessor.render(delta);
        }
    }

    @Override
    public void setMonitorUniform(Framebuffer buffer, String uniform, float value1, float value2, float value3) {
        PostEffectProcessor monitorPostProcessor = monitorPostProcessors.get(buffer);
        if(monitorPostProcessor != null) {
            ((IPostProcessorUniform) monitorPostProcessor).set3FloatUniforms(uniform, value1, value2, value3);
        }
    }
    @Override
    public void setMonitorUniform(Framebuffer buffer, String uniform, float value1, float value2) {
        PostEffectProcessor monitorPostProcessor = monitorPostProcessors.get(buffer);
        if(monitorPostProcessor != null) {
            ((IPostProcessorUniform) monitorPostProcessor).set2FloatUniforms(uniform, value1, value2);
        }
    }
    @Override
    public void setMonitorUniform(Framebuffer buffer, String uniform, float value1) {
        PostEffectProcessor monitorPostProcessor = monitorPostProcessors.get(buffer);
        if(monitorPostProcessor != null) {
            monitorPostProcessor.setUniforms(uniform, value1);
        }
    }

    @Override
    public void clearPostProcessor() {
        PostEffectProcessor postProcessor = this.client.getShaderLoader().loadPostEffect(this.postProcessorId, DefaultFramebufferSet.MAIN_ONLY);
        if (postProcessor != null) {
            //this.postProcessor.close();
        }
    }
    @Override
    public PostEffectProcessor getMonitorPostProcessor(Framebuffer framebuffer) {
        return this.monitorPostProcessors.get(framebuffer);
    }

    /*void loadMonitorPostProcessor(Identifier id, Framebuffer framebuffer){
        PostEffectProcessor monitorPostProcessor = monitorPostProcessors.get(framebuffer);
        if (monitorPostProcessor != null) {
           // monitorPostProcessor.close();
        }
        try {
            monitorPostProcessor = new PostEffectProcessor(this.client.getTextureManager(), this.resourceManager, framebuffer, id);
            //monitorPostProcessor.setupDimensions(this.client.getWindow().getFramebufferWidth(), this.client.getWindow().getFramebufferHeight());
            monitorPostProcessors.put(framebuffer, monitorPostProcessor);
        } catch (IOException iOException) {
            FnafUniverseRebuilt.LOGGER.warn("Failed to load shader: {}", (Object)id, (Object)iOException);
        } catch (JsonSyntaxException jsonSyntaxException) {
            FnafUniverseRebuilt.LOGGER.warn("Failed to parse shader: {}", (Object)id, (Object)jsonSyntaxException);
        }
    }*/
}
