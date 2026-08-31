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

public final class PositionPrepassRenderer {
    private PositionPrepassRenderer() {}

    public static void renderOpaquePosition(ChunkSectionsToRender state, ChunkSectionLayerGroup group, GpuSampler terrainSampler) {
        // Only for opaque group (same as normals prepass)
        if (group != ChunkSectionLayerGroup.OPAQUE) return;

        RenderSystem.AutoStorageIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
        GpuBuffer idxBuffer = state.maxIndicesRequired() == 0 ? null : shapeIndexBuffer.getBuffer(state.maxIndicesRequired());
        VertexFormat.IndexType indexType = state.maxIndicesRequired() == 0 ? null : shapeIndexBuffer.type();

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
            for (ChunkSectionLayer layer : group.layers()) {
                if (layer == ChunkSectionLayer.TRANSLUCENT) continue;

                @SuppressWarnings("unchecked")
                List<RenderPass.Draw<GpuBufferSlice[]>> list =
                        (List<RenderPass.Draw<GpuBufferSlice[]>>) state.drawsPerLayer().get(layer);

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