package net.zephyr.fnafur.rendering.lighting;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.BlockRenderLayerGroup;
import net.minecraft.client.render.SectionRenderState;
import net.zephyr.fnafur.client.CustomRenderingPipelines;

import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public final class AreaLightShadowRenderer {

    private AreaLightShadowRenderer() {}

    /**
     * Renders shadows for up to MAX_SHADOWED_LIGHTS lights.
     * Lights beyond that get shadowIndex = -1 (no shadow).
     *
     * IMPORTANT: visibleLights should be sorted closest-first so the nearest lights get shadows.
     * (AreaLightManager.getVisibleLights() should do this.)
     */
    public static void renderShadowsForSectionState(
            SectionRenderState state,
            BlockRenderLayerGroup group,
            List<AreaLightInstance> visibleLights
    ) {
        // Reset all to "no shadow" so indices don't go stale
        for (AreaLightInstance L : visibleLights) {
            L.setShadowIndex(-1);
        }

        int shadowCount = Math.min(visibleLights.size(), AreaLightShadowResources.MAX_SHADOWED_LIGHTS);
        if (shadowCount == 0) return;

        RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.DrawMode.QUADS);
        GpuBuffer gpuBuffer = state.maxIndicesRequired() == 0 ? null : shapeIndexBuffer.getIndexBuffer(state.maxIndicesRequired());
        VertexFormat.IndexType indexType = state.maxIndicesRequired() == 0 ? null : shapeIndexBuffer.getIndexType();

        for (int si = 0; si < shadowCount; si++) {
            AreaLightInstance L = visibleLights.get(si);
            L.setShadowIndex(si);

            // Upload per-light matrix UBO
            LightShadowUbo.upload(L.getLightViewProj());

            final int layerIndex = si;
            try (RenderPass pass = RenderSystem.getDevice()
                    .createCommandEncoder()
                    .createRenderPass(
                            () -> "AreaLight Shadow Depth " + layerIndex,
                            AreaLightShadowResources.shadowColorDummyView,
                            OptionalInt.of(0), // clear dummy color
                            AreaLightShadowResources.shadowDepthView[si],
                            OptionalDouble.of(1.0) // clear depth
                    )) {

                RenderSystem.bindDefaultUniforms(pass);

                pass.setPipeline(CustomRenderingPipelines.AREA_LIGHT_SHADOW);
                pass.setUniform("LightShadow", LightShadowUbo.buffer);

                // Render chunk geometry to depth; skip translucent for shadows by default
                for (BlockRenderLayer layer : group.getLayers()) {
                    if (layer == BlockRenderLayer.TRANSLUCENT) continue;

                    @SuppressWarnings("unchecked")
                    List<RenderPass.RenderObject<GpuBufferSlice[]>> list =
                            (List<RenderPass.RenderObject<GpuBufferSlice[]>>) state.drawsPerLayer().get(layer);

                    if (list.isEmpty()) continue;

                    pass.drawMultipleIndexed(
                            list,
                            gpuBuffer,
                            indexType,
                            List.of("ChunkSection"),
                            state.chunkSectionInfos()
                    );
                }
            }
        }
    }
}