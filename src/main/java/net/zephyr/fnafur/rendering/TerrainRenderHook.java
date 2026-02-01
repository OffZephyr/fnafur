package net.zephyr.fnafur.rendering;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GpuSampler;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.BlockRenderLayerGroup;
import net.minecraft.client.render.SectionRenderState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.client.CustomRenderingPipelines;
import net.zephyr.fnafur.rendering.decals.DecalManager;
import net.zephyr.fnafur.rendering.lighting.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public final class TerrainRenderHook {

    private TerrainRenderHook() {}

    public static void render(SectionRenderState state, BlockRenderLayerGroup group, GpuSampler terrainSampler) {

//        if(true) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        boolean wire = SharedConstants.HOTKEYS && mc.wireFrame;

        // Ensure screen-sized textures exist (normal/depth/light)
        LightingPrepassResources.ensureSize(
                mc.getWindow().getFramebufferWidth(),
                mc.getWindow().getFramebufferHeight()
        );

        for(ILightHolder holder : AreaLightManager.WORLD_LIGHT_MAP.keySet()){
            if(holder instanceof Entity ent){
                if(holder.isLightOn(ent) && holder.isLightAutoUpdate(ent)){
                    holder.updateLightInstance();
                }
            }
        }

        // Gather visible lights
        List<AreaLightInstance> visibleLights = AreaLightManager.getVisibleLights();

        // Shadow maps (your existing pass)
        AreaLightShadowRenderer.renderShadowsForSectionState(state, group, visibleLights);

        // Upload UBOs BEFORE opening any render pass
        DecalManager.getDecalData();
        AreaLightManager.uploadAndGetLightUbo(visibleLights);

        // Only run prepass + fullscreen lighting once (during OPAQUE group)
        if (group == BlockRenderLayerGroup.OPAQUE) {
            NormalsPrepassRenderer.renderOpaqueNormalsDepth(state, group, terrainSampler);
            PositionPrepassRenderer.renderOpaquePosition(state, group, terrainSampler);
            LightingFullscreenRenderer.renderLightBuffer(visibleLights);
        }

        // Regular terrain pass
        RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.DrawMode.QUADS);
        GpuBuffer idxBuffer = state.maxIndicesRequired() == 0 ? null : shapeIndexBuffer.getIndexBuffer(state.maxIndicesRequired());
        VertexFormat.IndexType indexType = state.maxIndicesRequired() == 0 ? null : shapeIndexBuffer.getIndexType();

        Framebuffer framebuffer = group.getFramebuffer();

        try (RenderPass pass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(
                        () -> "Cool Terrain " + group.getName(),
                        framebuffer.getColorAttachmentView(),
                        OptionalInt.empty(),
                        framebuffer.getDepthAttachmentView(),
                        OptionalDouble.empty()
                )) {

            RenderSystem.bindDefaultUniforms(pass);

            // vanilla lightmap
            pass.bindTexture(
                    "Sampler2",
                    mc.gameRenderer.getLightmapTextureManager().getGlTextureView(),
                    RenderSystem.getSamplerCache().get(FilterMode.LINEAR)
            );

            // block atlas
            pass.bindTexture("Sampler0", state.textureView(), terrainSampler);

            // lighting multiplier buffer
            pass.bindTexture(
                    "LightBuffer",
                    LightingPrepassResources.lightView,
                    RenderSystem.getSamplerCache().get(FilterMode.LINEAR)
            );

            // shadows
            var shadowSampler = RenderSystem.getSamplerCache().get(FilterMode.NEAREST);
            pass.bindTexture("ShadowSampler0", AreaLightShadowResources.shadowDepthView[0], shadowSampler);
            pass.bindTexture("ShadowSampler1", AreaLightShadowResources.shadowDepthView[1], shadowSampler);
            pass.bindTexture("ShadowSampler2", AreaLightShadowResources.shadowDepthView[2], shadowSampler);
            pass.bindTexture("ShadowSampler3", AreaLightShadowResources.shadowDepthView[3], shadowSampler);
            pass.bindTexture("ShadowSampler4", AreaLightShadowResources.shadowDepthView[4], shadowSampler);
            pass.bindTexture("ShadowSampler5", AreaLightShadowResources.shadowDepthView[5], shadowSampler);
            pass.bindTexture("ShadowSampler6", AreaLightShadowResources.shadowDepthView[6], shadowSampler);
            pass.bindTexture("ShadowSampler7", AreaLightShadowResources.shadowDepthView[7], shadowSampler);

            // UBOs
            pass.setUniform("DecalInfo", DecalManager.decal_buffer);
            pass.setUniform("LightData", AreaLightManager.lightBuffer);

            for (BlockRenderLayer layer : group.getLayers()) {
                @SuppressWarnings("unchecked")
                List<RenderPass.RenderObject<com.mojang.blaze3d.buffers.GpuBufferSlice[]>> list =
                        (List<RenderPass.RenderObject<com.mojang.blaze3d.buffers.GpuBufferSlice[]>>) state.drawsPerLayer().get(layer);

                if (list.isEmpty()) continue;
                if (layer == BlockRenderLayer.TRANSLUCENT) list = list.reversed();

                RenderPipeline pipeline = switch (layer) {
                    case SOLID -> CustomRenderingPipelines.COOL_SOLID_TERRAIN;
                    case CUTOUT -> CustomRenderingPipelines.COOL_CUTOUT_TERRAIN;
                    case TRANSLUCENT -> CustomRenderingPipelines.COOL_TRANSLUCENT;
                    case TRIPWIRE -> CustomRenderingPipelines.COOL_TRIPWIRE_TERRAIN;
                };

                pass.setPipeline(wire ? CustomRenderingPipelines.COOL_WIREFRAME : pipeline);
                pass.drawMultipleIndexed(list, idxBuffer, indexType, List.of("ChunkSection"), state.chunkSectionInfos());
            }
        }
    }
}