package net.zephyr.fnafur.rendering.lighting;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;

public final class AreaLightShadowResources {

    public static final int SHADOW_RES = 1024;
    public static final int MIP_LEVELS = 1;

    // Hard cap: only this many lights can cast shadows in one terrain pass.
    public static final int MAX_SHADOWED_LIGHTS = 8;

    public static final GpuTexture[] shadowDepth = new GpuTexture[MAX_SHADOWED_LIGHTS];
    public static final GpuTextureView[] shadowDepthView = new GpuTextureView[MAX_SHADOWED_LIGHTS];

    // Dummy color target required by createRenderPass(...)
    public static final GpuTexture shadowColorDummy = RenderSystem.getDevice().createTexture(
            () -> "AreaLight Shadow Dummy Color",
            GpuTexture.USAGE_RENDER_ATTACHMENT,
            TextureFormat.RGBA8,
            SHADOW_RES,
            SHADOW_RES,
            1,
            1
    );

    public static final GpuTextureView shadowColorDummyView =
            RenderSystem.getDevice().createTextureView(shadowColorDummy, 0, 1);

    static {
        for (int i = 0; i < MAX_SHADOWED_LIGHTS; i++) {
            int finalI = i;
            shadowDepth[i] = RenderSystem.getDevice().createTexture(
                    () -> "AreaLight Shadow Depth " + finalI,
                    GpuTexture.USAGE_RENDER_ATTACHMENT | GpuTexture.USAGE_TEXTURE_BINDING,
                    TextureFormat.DEPTH32,
                    SHADOW_RES,
                    SHADOW_RES,
                    1,
                    MIP_LEVELS
            );
            shadowDepthView[i] = RenderSystem.getDevice().createTextureView(shadowDepth[i], 0, 1);
        }
    }

    private AreaLightShadowResources() {}
}