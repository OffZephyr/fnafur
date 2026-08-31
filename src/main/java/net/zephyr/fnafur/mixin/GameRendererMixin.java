package net.zephyr.fnafur.mixin;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.renderer.PostChain;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.client.gui.screens.CameraTabletScreen;
import net.zephyr.fnafur.client.media_player.MediaPlayerUtil;
import net.zephyr.fnafur.client.media_player.VideoInstance;
import net.zephyr.fnafur.util.mixinAccessing.IPostProcessorLoader;
import net.zephyr.fnafur.util.mixinAccessing.IPostProcessorUniform;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(GameRenderer.class)
public class GameRendererMixin implements IPostProcessorLoader {

    @Shadow
    Minecraft minecraft;
    @Shadow
    private Identifier postEffectId;
    private Map<RenderTarget, PostChain> monitorPostProcessors = new HashMap<>();

    @Shadow
    private void setPostEffect(Identifier id) {

    }
    /*@Inject(method = "render", at = @At(value = "HEAD"))
    public void renderMonitorPostProcessor(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        if(!client.skipGameRender) {
            renderMonitor(tickCounter.getLastDuration(), CameraRenderer.isDrawing(), CameraRenderer.getFramebuffer());
        }
    }*/

    @Inject(method = "getFov(Lnet/minecraft/client/Camera;FZ)F", at = @At("RETURN"), cancellable = true)
    public void getZoomLevel(CallbackInfoReturnable<Float> callbackInfo) {
        if(Minecraft.getInstance().screen instanceof CameraTabletScreen) {
            float fov = 45;
            callbackInfo.setReturnValue(fov);
        }
    }
    @Inject(method = "resize", at = @At(value = "HEAD"))
    private void illusions$onResized$HEAD(int width, int height, CallbackInfo ci) {
        //CameraRenderer.onResize(width, height);
        //TODO CamRenderer
    }
    @Inject(method = "render", at = @At(value = "TAIL"))
    private void illusions$onResized$HEAD(DeltaTracker tickCounter, boolean tick, CallbackInfo ci) {
        //MediaPlayerUtil.renderVideo(((GameRenderer) (Object) this));
    }

    public void resizePostProcessor(RenderTarget framebuffer, int width, int height){
        PostChain monitorPostProcessor = monitorPostProcessors.get(framebuffer);
        if(monitorPostProcessor != null) {
           // monitorPostProcessor.setupDimensions(width, height);
        }
    }

    @Override
    public void loadPostProcessor(Identifier id) {
        setPostEffect(id);
    }

    @Override
    public void render(FrameGraphBuilder builder, int textureWidth, int textureHeight, PostChain.TargetBundle framebufferSet) {

        //PostEffectProcessor postProcessor = this.client.getShaderLoader().loadPostEffect(this.postProcessorId, DefaultFramebufferSet.MAIN_ONLY);
        //if(postProcessor != null){
        //    postProcessor.render(builder, textureWidth, textureHeight, framebufferSet);
        //}
    }
    @Override
    public void setMonitorPostProcessor(Identifier id, RenderTarget framebuffer) {
        /*if(monitorPostProcessors.get(framebuffer) == null || !monitorPostProcessors.get(framebuffer).getName().equals(id.toString())) {
            System.out.println(id);
            loadMonitorPostProcessor(id, framebuffer);
        }*/
    }
    @Override
    public void renderMonitor(float delta, boolean bool, RenderTarget framebuffer) {
        //PostEffectProcessor monitorPostProcessor = monitorPostProcessors.get(framebuffer);
        //if(bool && monitorPostProcessor != null){
        //    RenderSystem.disableBlend();
        //    RenderSystem.disableDepthTest();
        //    RenderSystem.resetTextureMatrix();
        //    //monitorPostProcessor.render(delta);
        //}
    }

    @Override
    public void setMonitorUniform(RenderTarget buffer, String uniform, float value1, float value2, float value3) {
        PostChain monitorPostProcessor = monitorPostProcessors.get(buffer);
        if(monitorPostProcessor != null) {
            ((IPostProcessorUniform) monitorPostProcessor).set3FloatUniforms(uniform, value1, value2, value3);
        }
    }
    @Override
    public void setMonitorUniform(RenderTarget buffer, String uniform, float value1, float value2) {
        PostChain monitorPostProcessor = monitorPostProcessors.get(buffer);
        if(monitorPostProcessor != null) {
            ((IPostProcessorUniform) monitorPostProcessor).set2FloatUniforms(uniform, value1, value2);
        }
    }
    @Override
    public void setMonitorUniform(RenderTarget buffer, String uniform, float value1) {
        //PostEffectProcessor monitorPostProcessor = monitorPostProcessors.get(buffer);
        //if(monitorPostProcessor != null) {
        //    monitorPostProcessor.setUniforms(uniform, value1);
        //}
    }

    @Override
    public void clearPostProcessor() {
        //PostEffectProcessor postProcessor = this.client.getShaderLoader().loadPostEffect(this.postProcessorId, DefaultFramebufferSet.MAIN_ONLY);
        //if (postProcessor != null) {
            //this.postProcessor.close();
        //}
    }
    @Override
    public PostChain getMonitorPostProcessor(RenderTarget framebuffer) {
        return this.monitorPostProcessors.get(framebuffer);
    }


    @Inject(method = "close", at = @At("HEAD"))
    public void close(CallbackInfo ci){
        //VideoInstance.closeAllInstances();
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
