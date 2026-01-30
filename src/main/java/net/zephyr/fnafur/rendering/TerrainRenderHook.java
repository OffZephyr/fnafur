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

    public static void render(SectionRenderState state, BlockRenderLayerGroup group, GpuSampler sampler) {

        Sprite sprite = MinecraftClient.getInstance().getBlockRenderManager().spriteHolder.getSprite(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier.ofVanilla("block/white_concrete")));

        float u0 = sprite.getMinU();
        float v0 = sprite.getMinV();
        float u1 = sprite.getMaxU();
        float v1 = sprite.getMaxV();

        for(ILightHolder holder : AreaLightManager.WORLD_LIGHT_MAP.keySet()){
            if(holder instanceof Entity ent){
                if(holder.isLightOn(ent) && holder.isLightAutoUpdate(ent)){
                    holder.updateLightInstance();
                }
            }
        }
        if(AreaLightManager.WORLD_LIGHTS.isEmpty() && false) {
            AreaLightManager.WORLD_LIGHTS.clear();
            AreaLightInstance lightInstance = new AreaLightInstance(
                    MinecraftClient.getInstance().player.getEyePos(),
                    MinecraftClient.getInstance().player.getHeadRotationVector().toVector3f(),
                    MinecraftClient.getInstance().player.getHeadRotationVector().multiply(-1).toVector3f(),
                    5,
                    15,
                    new Vector3f(1, 1, 1),
                    1,
                    0,
                    0,
                    1,
                    0.25f,
                    new Vector4f(u0, v0, u1, v1),
                    new Vector4f(u0, v0, u1, v1)

            );
            AreaLightManager.WORLD_LIGHTS.add(lightInstance);
        }

        List<AreaLightInstance> visibleLights = AreaLightManager.getVisibleLights();

        updateLightViewProjMatrices(visibleLights);

        AreaLightShadowRenderer.renderShadowsForSectionState(state, group, visibleLights);

        DecalManager.getDecalData();
        AreaLightManager.uploadAndGetLightUbo(visibleLights);

        RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.DrawMode.QUADS);
        GpuBuffer gpuBuffer = state.maxIndicesRequired() == 0 ? null : shapeIndexBuffer.getIndexBuffer(state.maxIndicesRequired());
        VertexFormat.IndexType indexType = state.maxIndicesRequired() == 0 ? null : shapeIndexBuffer.getIndexType();

        BlockRenderLayer[] layers = group.getLayers();
        MinecraftClient mc = MinecraftClient.getInstance();
        boolean wire = SharedConstants.HOTKEYS && mc.wireFrame;
        Framebuffer framebuffer = group.getFramebuffer();

        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(
                        () -> "Cool Terrain for " + group.getName(),
                        framebuffer.getColorAttachmentView(),
                        OptionalInt.empty(),
                        framebuffer.getDepthAttachmentView(),
                        OptionalDouble.empty()
                )) {

            RenderSystem.bindDefaultUniforms(renderPass);

            renderPass.bindTexture(
                    "Sampler2",
                    mc.gameRenderer.getLightmapTextureManager().getGlTextureView(),
                    RenderSystem.getSamplerCache().get(FilterMode.LINEAR)
            );

            var shadowSampler = RenderSystem.getSamplerCache().get(FilterMode.NEAREST);
            renderPass.bindTexture("ShadowSampler0", AreaLightShadowResources.shadowDepthView[0], shadowSampler);
            renderPass.bindTexture("ShadowSampler1", AreaLightShadowResources.shadowDepthView[1], shadowSampler);
            renderPass.bindTexture("ShadowSampler2", AreaLightShadowResources.shadowDepthView[2], shadowSampler);
            renderPass.bindTexture("ShadowSampler3", AreaLightShadowResources.shadowDepthView[3], shadowSampler);
            renderPass.bindTexture("ShadowSampler4", AreaLightShadowResources.shadowDepthView[4], shadowSampler);
            renderPass.bindTexture("ShadowSampler5", AreaLightShadowResources.shadowDepthView[5], shadowSampler);
            renderPass.bindTexture("ShadowSampler6", AreaLightShadowResources.shadowDepthView[6], shadowSampler);
            renderPass.bindTexture("ShadowSampler7", AreaLightShadowResources.shadowDepthView[7], shadowSampler);

            for (BlockRenderLayer layer : layers) {
                @SuppressWarnings("unchecked")
                List<RenderPass.RenderObject<GpuBufferSlice[]>> list =
                        (List<RenderPass.RenderObject<GpuBufferSlice[]>>) state.drawsPerLayer().get(layer);

                if (list.isEmpty()) continue;
                if (layer == BlockRenderLayer.TRANSLUCENT) list = list.reversed();

                RenderPipeline pipeline = switch (layer) {
                    case SOLID -> CustomRenderingPipelines.COOL_SOLID_TERRAIN;
                    case CUTOUT -> CustomRenderingPipelines.COOL_CUTOUT_TERRAIN;
                    case TRANSLUCENT -> CustomRenderingPipelines.COOL_TRANSLUCENT;
                    case TRIPWIRE -> CustomRenderingPipelines.COOL_TRIPWIRE_TERRAIN;
                };

                renderPass.setPipeline(wire ? CustomRenderingPipelines.COOL_WIREFRAME : pipeline);

                renderPass.bindTexture("Sampler0", state.textureView(), sampler);

                renderPass.setUniform("DecalInfo", DecalManager.decal_buffer);
                renderPass.setUniform("LightData", AreaLightManager.lightBuffer);

                renderPass.drawMultipleIndexed(list, gpuBuffer, indexType, List.of("ChunkSection"), state.chunkSectionInfos());
            }
        }
    }

    private static void updateLightViewProjMatrices(List<AreaLightInstance> lights) {
        Vec3d cam = MinecraftClient.getInstance().gameRenderer.getCamera().pos;

        for (AreaLightInstance L : lights) {

            Vec3d pWorld = L.getPosition();
            Vector3f dir = L.getDirection();

            Vector3f eye = new Vector3f(
                    (float) (pWorld.x - cam.x),
                    (float) (pWorld.y - cam.y),
                    (float) (pWorld.z - cam.z)
            );
            Vector3f center = new Vector3f(eye).add(new Vector3f(dir).mul(Math.max(L.getLength(), 1.0f)));

            Vector3f up = (Math.abs(dir.y) > 0.99f) ? new Vector3f(1, 0, 0) : new Vector3f(0, 1, 0);
            up = new Vector3f(0, 1, 0);

            Matrix4f view = new Matrix4f().lookAt(eye, center, up);

            float r = Math.max(L.getRadius(), 0.5f);
            float zNear = 0.1f;
            float zFar = Math.max(4.0f, L.getLength() + r * 4.0f);

            Matrix4f proj = new Matrix4f().ortho(-r, r, -r, r, zNear, zFar);

            Matrix4f vp = new Matrix4f(proj).mul(view);
            L.setLightViewProj(vp);
        }
    }
}