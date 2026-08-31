package net.zephyr.fnafur.rendering;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.textures.GpuSampler;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayerGroup;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
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

    public static void render(ChunkSectionsToRender state, ChunkSectionLayerGroup group, GpuSampler terrainSampler) {

//        if(true) return;
        Minecraft mc = Minecraft.getInstance();
        boolean wire = SharedConstants.DEBUG_HOTKEYS && mc.wireframe;

        // Ensure screen-sized textures exist (normal/depth/light)
        LightingPrepassResources.ensureSize(
                mc.getWindow().getWidth(),
                mc.getWindow().getHeight()
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
        if (group == ChunkSectionLayerGroup.OPAQUE) {
            NormalsPrepassRenderer.renderOpaqueNormalsDepth(state, group, terrainSampler);
            PositionPrepassRenderer.renderOpaquePosition(state, group, terrainSampler);
            LightingFullscreenRenderer.renderLightBuffer(visibleLights);
        }

        // Regular terrain pass
        RenderSystem.AutoStorageIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
        GpuBuffer idxBuffer = state.maxIndicesRequired() == 0 ? null : shapeIndexBuffer.getBuffer(state.maxIndicesRequired());
        VertexFormat.IndexType indexType = state.maxIndicesRequired() == 0 ? null : shapeIndexBuffer.type();

        RenderTarget framebuffer = group.outputTarget();

        try (RenderPass pass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(
                        () -> "Cool Terrain " + group.label(),
                        framebuffer.getColorTextureView(),
                        OptionalInt.empty(),
                        framebuffer.getDepthTextureView(),
                        OptionalDouble.empty()
                )) {

            RenderSystem.bindDefaultUniforms(pass);

            // vanilla lightmap
            pass.bindTexture(
                    "Sampler2",
                    mc.gameRenderer.lightTexture().getTextureView(),
                    RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR)
            );

            // block atlas
            pass.bindTexture("Sampler0", state.textureView(), terrainSampler);

            // lighting multiplier buffer
            pass.bindTexture(
                    "LightBuffer",
                    LightingPrepassResources.lightView,
                    RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR)
            );

            // shadows
            var shadowSampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
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

            for (ChunkSectionLayer layer : group.layers()) {
                @SuppressWarnings("unchecked")
                List<RenderPass.Draw<com.mojang.blaze3d.buffers.GpuBufferSlice[]>> list =
                        (List<RenderPass.Draw<com.mojang.blaze3d.buffers.GpuBufferSlice[]>>) state.drawsPerLayer().get(layer);

                if (list.isEmpty()) continue;
                if (layer == ChunkSectionLayer.TRANSLUCENT) list = list.reversed();

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