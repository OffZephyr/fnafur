package net.zephyr.fnafur.rendering.lighting;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.textures.GpuSampler;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayerGroup;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
import net.zephyr.fnafur.client.CustomRenderingPipelines;

import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public final class NormalsPrepassRenderer {

    private NormalsPrepassRenderer() {}

    public static void renderOpaqueNormalsDepth(
            ChunkSectionsToRender state,
            ChunkSectionLayerGroup group,
            GpuSampler sampler
    ) {
        // We only want opaque geometry contributing to the gbuffer.
        // Skip translucent.
        RenderSystem.AutoStorageIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
        GpuBuffer idxBuffer = state.maxIndicesRequired() == 0 ? null : shapeIndexBuffer.getBuffer(state.maxIndicesRequired());
        VertexFormat.IndexType indexType = state.maxIndicesRequired() == 0 ? null : shapeIndexBuffer.type();

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

            for (ChunkSectionLayer layer : group.layers()) {
                if (layer == ChunkSectionLayer.TRANSLUCENT) continue;

                @SuppressWarnings("unchecked")
                List<RenderPass.Draw<GpuBufferSlice[]>> list =
                        (List<RenderPass.Draw<GpuBufferSlice[]>>) state.drawsPerLayer().get(layer);

                if (list.isEmpty()) continue;

                pass.drawMultipleIndexed(list, idxBuffer, indexType, List.of("ChunkSection"), state.chunkSectionInfos());
            }
        }
    }
}