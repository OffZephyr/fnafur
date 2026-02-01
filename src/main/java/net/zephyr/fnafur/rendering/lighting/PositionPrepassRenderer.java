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

public final class PositionPrepassRenderer {
    private PositionPrepassRenderer() {}

    public static void renderOpaquePosition(SectionRenderState state, BlockRenderLayerGroup group, GpuSampler terrainSampler) {
        // Only for opaque group (same as normals prepass)
        if (group != BlockRenderLayerGroup.OPAQUE) return;

        RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.DrawMode.QUADS);
        GpuBuffer idxBuffer = state.maxIndicesRequired() == 0 ? null : shapeIndexBuffer.getIndexBuffer(state.maxIndicesRequired());
        VertexFormat.IndexType indexType = state.maxIndicesRequired() == 0 ? null : shapeIndexBuffer.getIndexType();

        try (RenderPass pass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(
                        () -> "Position Prepass",
                        LightingPrepassResources.positionView,   // color
                        OptionalInt.of(0),                       // clear color OK
                        LightingPrepassResources.depthView,      // depth = SAME texture from normals pass
                        OptionalDouble.empty()                   // DO NOT clear depth
                )) {

            RenderSystem.bindDefaultUniforms(pass);

            pass.setPipeline(CustomRenderingPipelines.POSITION_PREPASS);

            // Bind atlas (terrain.vsh likely needs it for texcoord logic; safe to bind anyway)
            pass.bindTexture("Sampler0", state.textureView(), terrainSampler);

            // Draw SOLID/CUTOUT only (same rule as your shadow pass)
            for (BlockRenderLayer layer : group.getLayers()) {
                if (layer == BlockRenderLayer.TRANSLUCENT) continue;

                @SuppressWarnings("unchecked")
                List<RenderPass.RenderObject<GpuBufferSlice[]>> list =
                        (List<RenderPass.RenderObject<GpuBufferSlice[]>>) state.drawsPerLayer().get(layer);

                if (list.isEmpty()) continue;

                pass.drawMultipleIndexed(
                        list,
                        idxBuffer,
                        indexType,
                        List.of("ChunkSection"),
                        state.chunkSectionInfos()
                );
            }
        }
    }
}