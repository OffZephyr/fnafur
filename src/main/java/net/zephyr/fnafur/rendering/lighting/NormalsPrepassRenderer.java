package net.zephyr.fnafur.rendering.lighting;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.GpuSampler;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.BlockRenderLayerGroup;
import net.minecraft.client.render.SectionRenderState;
import net.zephyr.fnafur.client.CustomRenderingPipelines;

import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public final class NormalsPrepassRenderer {

    private NormalsPrepassRenderer() {}

    public static void renderOpaqueNormalsDepth(
            SectionRenderState state,
            BlockRenderLayerGroup group,
            GpuSampler sampler
    ) {
        // We only want opaque geometry contributing to the gbuffer.
        // Skip translucent.
        RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.DrawMode.QUADS);
        GpuBuffer idxBuffer = state.maxIndicesRequired() == 0 ? null : shapeIndexBuffer.getIndexBuffer(state.maxIndicesRequired());
        VertexFormat.IndexType indexType = state.maxIndicesRequired() == 0 ? null : shapeIndexBuffer.getIndexType();

        try (RenderPass pass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(
                        () -> "Normals+Depth Prepass",
                        LightingPrepassResources.normalView,
                        OptionalInt.of(0), // clear normals to 0
                        LightingPrepassResources.depthView,
                        OptionalDouble.of(1.0) // clear depth
                )) {

            RenderSystem.bindDefaultUniforms(pass);

            // Atlas needed because chunk vertex shader expects Sampler2/lightmap in your terrain.vsh,
            // but for normals we don't sample textures. Still, terrain.vsh uses Sampler2 for lightmap,
            // so bind it to avoid undefined access if you reuse terrain.vsh.
            pass.bindTexture("Sampler0", state.textureView(), sampler);

            pass.setPipeline(CustomRenderingPipelines.NORMALS_PREPASS);

            for (BlockRenderLayer layer : group.getLayers()) {
                if (layer == BlockRenderLayer.TRANSLUCENT) continue;

                @SuppressWarnings("unchecked")
                List<RenderPass.RenderObject<GpuBufferSlice[]>> list =
                        (List<RenderPass.RenderObject<GpuBufferSlice[]>>) state.drawsPerLayer().get(layer);

                if (list.isEmpty()) continue;

                pass.drawMultipleIndexed(list, idxBuffer, indexType, List.of("ChunkSection"), state.chunkSectionInfos());
            }
        }
    }
}