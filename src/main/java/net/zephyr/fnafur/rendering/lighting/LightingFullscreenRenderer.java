package net.zephyr.fnafur.rendering.lighting;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.zephyr.fnafur.client.CustomRenderingPipelines;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public final class LightingFullscreenRenderer {

    private LightingFullscreenRenderer() {}

    // Fullscreen triangle positions (clip space)
    private static GpuBuffer fsTri;

    private static void ensureFsTri() {
        if (fsTri != null && !fsTri.isClosed()) return;

        // 3 vertices: (-1,-1,0), (3,-1,0), (-1,3,0)
        ByteBuffer buf = ByteBuffer.allocateDirect(3 * 3 * 4).order(ByteOrder.nativeOrder());
        buf.putFloat(-1f).putFloat(-1f).putFloat(0f);
        buf.putFloat( 3f).putFloat(-1f).putFloat(0f);
        buf.putFloat(-1f).putFloat( 3f).putFloat(0f);
        buf.flip();

        fsTri = RenderSystem.getDevice().createBuffer(
                () -> "FullscreenTri",
                GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_COPY_DST,
                buf
        );
    }

    public static void renderLightBuffer(List<AreaLightInstance> visibleLights) {
        ensureFsTri();

        // IMPORTANT: clear to WHITE (multiplier default = 1)
        final int CLEAR_WHITE = 0xFFFFFFFF;

        try (RenderPass pass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(
                        () -> "Lighting Fullscreen",
                        LightingPrepassResources.lightView,
                        OptionalInt.of(CLEAR_WHITE),
                        null,
                        OptionalDouble.empty()
                )) {

            RenderSystem.bindDefaultUniforms(pass);

            pass.setPipeline(CustomRenderingPipelines.LIGHTING_FULLSCREEN);

            var linear  = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
            var nearest = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);

            // Inputs
            pass.bindTexture("NormalBuffer", LightingPrepassResources.normalView, linear);
            pass.bindTexture("PositionBuffer", LightingPrepassResources.positionView, linear);
            pass.bindTexture("DepthBuffer",  LightingPrepassResources.depthView,  nearest);

            // Block atlas (projected textures + masks)
            pass.bindTexture(
                    "Sampler0",
                    Minecraft.getInstance()
                            .getTextureManager()
                            .getTexture(TextureAtlas.LOCATION_BLOCKS)
                            .getTextureView(),
                    linear
            );

            // Shadows (separate samplers)
            pass.bindTexture("ShadowSampler0", AreaLightShadowResources.shadowDepthView[0], nearest);
            pass.bindTexture("ShadowSampler1", AreaLightShadowResources.shadowDepthView[1], nearest);
            pass.bindTexture("ShadowSampler2", AreaLightShadowResources.shadowDepthView[2], nearest);
            pass.bindTexture("ShadowSampler3", AreaLightShadowResources.shadowDepthView[3], nearest);
            pass.bindTexture("ShadowSampler4", AreaLightShadowResources.shadowDepthView[4], nearest);
            pass.bindTexture("ShadowSampler5", AreaLightShadowResources.shadowDepthView[5], nearest);
            pass.bindTexture("ShadowSampler6", AreaLightShadowResources.shadowDepthView[6], nearest);
            pass.bindTexture("ShadowSampler7", AreaLightShadowResources.shadowDepthView[7], nearest);

            // Light UBO
            pass.setUniform("LightData", AreaLightManager.lightBuffer);

            // Draw fullscreen tri
            pass.setVertexBuffer(0, fsTri);
            pass.draw(0, 3);
        }
    }
}