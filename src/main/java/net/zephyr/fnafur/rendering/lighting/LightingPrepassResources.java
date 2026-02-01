package net.zephyr.fnafur.rendering.lighting;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;

public final class LightingPrepassResources {

    public static final float POS_ENCODE_RANGE = 256.0f;

    private LightingPrepassResources() {}

    public static int width = -1;
    public static int height = -1;

    public static GpuTexture normalTex;
    public static GpuTexture positionTex;
    public static GpuTexture depthTex;
    public static GpuTexture lightTex;

    public static GpuTextureView normalView;
    public static GpuTextureView positionView;
    public static GpuTextureView depthView;
    public static GpuTextureView lightView;

    public static void ensureSize(int w, int h) {
        if (w <= 0 || h <= 0) return;
        if (w == width && h == height && normalTex != null && !normalTex.isClosed() && positionTex != null && !positionTex.isClosed()) return;

        destroy();

        width = w;
        height = h;

        int usageColor = GpuTexture.USAGE_RENDER_ATTACHMENT | GpuTexture.USAGE_TEXTURE_BINDING;
        int usageDepth = GpuTexture.USAGE_RENDER_ATTACHMENT | GpuTexture.USAGE_TEXTURE_BINDING;

        normalTex = RenderSystem.getDevice().createTexture(
                () -> "LightPrepass Normal",
                usageColor,
                TextureFormat.RGBA8,
                w, h, 1, 1
        );

        positionTex = RenderSystem.getDevice().createTexture(
                () -> "LightPrepass Position",
                usageColor,
                TextureFormat.RGBA8,
                w, h, 1, 1
        );

        depthTex = RenderSystem.getDevice().createTexture(
                () -> "LightPrepass Depth",
                usageDepth,
                TextureFormat.DEPTH32,
                w, h, 1, 1
        );

        lightTex = RenderSystem.getDevice().createTexture(
                () -> "LightBuffer",
                usageColor,
                TextureFormat.RGBA8,
                w, h, 1, 1
        );

        normalView = RenderSystem.getDevice().createTextureView(normalTex);
        positionView = RenderSystem.getDevice().createTextureView(positionTex);
        depthView = RenderSystem.getDevice().createTextureView(depthTex);
        lightView = RenderSystem.getDevice().createTextureView(lightTex);
    }

    public static void destroy() {
        if (normalView != null) normalView.close();
        if (positionView != null) positionView.close();
        if (depthView != null) depthView.close();
        if (lightView != null) lightView.close();

        if (normalTex != null) normalTex.close();
        if (positionTex != null) positionTex.close();
        if (depthTex != null) depthTex.close();
        if (lightTex != null) lightTex.close();

        normalView = null;
        positionView = null;
        depthView = null;
        lightView = null;

        normalTex = null;
        positionTex = null;
        depthTex = null;
        lightTex = null;
    }
}