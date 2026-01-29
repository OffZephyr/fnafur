package net.zephyr.fnafur.rendering;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;

import java.lang.reflect.Method;
import java.util.Arrays;

public final class GpuTextureViews {

    private static Method cachedLayerViewMethod = null;
    private static Method cachedMipViewMethod = null;
    private static boolean searched = false;

    private GpuTextureViews() {}

    /**
     * Creates a view of the whole texture (mip range).
     */
    public static GpuTextureView mipView(GpuTexture texture, int baseMip, int mipLevels) {
        ensureMethodsSearched();

        if (cachedMipViewMethod == null) {
            throw new IllegalStateException("Could not find a device method to create a GpuTextureView (mip-only).");
        }

        try {
            Object device = RenderSystem.getDevice();
            return (GpuTextureView) cachedMipViewMethod.invoke(device, texture, baseMip, mipLevels);
        } catch (Throwable t) {
            throw new RuntimeException("Failed to create mip GpuTextureView via reflection.", t);
        }
    }

    /**
     * Creates a view selecting a specific layer range (for array textures).
     * This is required to render depth into layer i.
     */
    public static GpuTextureView layerView(GpuTexture texture, int baseMip, int mipLevels, int baseLayer, int layerCount) {
        ensureMethodsSearched();

        if (cachedLayerViewMethod == null) {
            throw new IllegalStateException(
                    "Could not find a device method to create a layered GpuTextureView. " +
                            "Your Blaze3D build may not expose array-layer views; shadow map arrays won't work."
            );
        }

        try {
            Object device = RenderSystem.getDevice();
            return (GpuTextureView) cachedLayerViewMethod.invoke(device, texture, baseMip, mipLevels, baseLayer, layerCount);
        } catch (Throwable t) {
            throw new RuntimeException("Failed to create layered GpuTextureView via reflection.", t);
        }
    }

    private static void ensureMethodsSearched() {
        if (searched) return;
        searched = true;

        Object device = RenderSystem.getDevice();
        Class<?> dc = device.getClass();

        for (Method m : dc.getMethods()) {
            if (!GpuTextureView.class.isAssignableFrom(m.getReturnType())) continue;

            Class<?>[] p = m.getParameterTypes();
            if (p.length == 3 &&
                    p[0] == GpuTexture.class &&
                    p[1] == int.class &&
                    p[2] == int.class) {
                cachedMipViewMethod = m;
            }

            if (p.length == 5 &&
                    p[0] == GpuTexture.class &&
                    p[1] == int.class &&
                    p[2] == int.class &&
                    p[3] == int.class &&
                    p[4] == int.class) {
                cachedLayerViewMethod = m;
            }
        }

        if (cachedMipViewMethod == null || cachedLayerViewMethod == null) {
            String candidates = Arrays.stream(dc.getMethods())
                    .filter(m -> GpuTextureView.class.isAssignableFrom(m.getReturnType()))
                    .map(m -> m.getName() + Arrays.toString(m.getParameterTypes()))
                    .reduce("", (a, b) -> a + "\n  " + b);

        }
    }
}