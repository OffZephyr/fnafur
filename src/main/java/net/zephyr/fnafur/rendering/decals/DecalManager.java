package net.zephyr.fnafur.rendering.decals;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.SharedConstants;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.textures.GpuSampler;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayerGroup;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.RandomSource;
import net.zephyr.fnafur.client.CustomRenderingPipelines;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class DecalManager {

    public static final int MAX_DISTANCE = 128;
    public static final int MAX_DECALS = 512;
    static final int STRIDE = 112;
    static int uboSize = 16 + MAX_DECALS * STRIDE;

    public static DecalInstance PREVIEW_DECAL = null;
    public static List<DecalInstance> WORLD_DECALS = new ArrayList<>();
    public static Map<BlockPos, Map<Direction, List<DecalInstance>>> WORLD_DECALS_MAP = new HashMap<>();


    public static GpuBuffer decal_buffer = RenderSystem.getDevice().createBuffer(
            () -> "DecalInfo UBO",
            GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_COPY_DST,
            uboSize
    );

    public static GpuBufferSlice decal_slice = new GpuBufferSlice(decal_buffer, 0, uboSize);

    public static void DecalRenderHook(ChunkSectionsToRender state, ChunkSectionLayerGroup group, GpuSampler sampler) {

        RenderSystem.AutoStorageIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
        GpuBuffer gpuBuffer = state.maxIndicesRequired() == 0 ? null : shapeIndexBuffer.getBuffer(state.maxIndicesRequired());
        VertexFormat.IndexType indexType = state.maxIndicesRequired() == 0 ? null : shapeIndexBuffer.type();
        ChunkSectionLayer[] blockRenderLayers = group.layers();
        Minecraft client = Minecraft.getInstance();
        boolean bl = SharedConstants.DEBUG_HOTKEYS && client.wireframe;
        RenderTarget framebuffer = group.outputTarget();

        GpuBufferSlice decalBuffer = getDecalData();

        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(
                        () -> "Section layers for " + group.label(),
                        framebuffer.getColorTextureView(),
                        OptionalInt.empty(),
                        framebuffer.getDepthTextureView(),
                        OptionalDouble.empty()
                )) {
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.bindTexture(
                    "Sampler2", client.gameRenderer.lightTexture().getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR)
            );

            for (ChunkSectionLayer blockRenderLayer : blockRenderLayers) {
                List<RenderPass.Draw<GpuBufferSlice[]>> list = (List<RenderPass.Draw<GpuBufferSlice[]>>) state.drawsPerLayer().get(blockRenderLayer);
                if (!list.isEmpty()) {
                    if (blockRenderLayer == ChunkSectionLayer.TRANSLUCENT) {
                        list = list.reversed();
                    }

                    TextureAtlasSprite sprite = client.getInstance().getBlockRenderer()
                            .getBlockModelShaper()
                            .getBlockModel(Blocks.STONE.defaultBlockState()).collectParts(RandomSource.create()).getFirst().getQuads(Direction.NORTH).getFirst().sprite();

                    RenderPipeline pipeline = switch (blockRenderLayer){
                        case SOLID -> CustomRenderingPipelines.COOL_SOLID_TERRAIN;
                        case CUTOUT -> CustomRenderingPipelines.COOL_CUTOUT_TERRAIN;
                        case TRANSLUCENT -> CustomRenderingPipelines.COOL_TRANSLUCENT;
                        case TRIPWIRE -> CustomRenderingPipelines.COOL_TRIPWIRE_TERRAIN;
                    };
                    renderPass.setPipeline(bl ? CustomRenderingPipelines.COOL_WIREFRAME : pipeline);
                    renderPass.bindTexture("Sampler0", state.textureView(), sampler);
                    renderPass.setUniform("DecalInfo", decal_buffer);
                    renderPass.drawMultipleIndexed(list, gpuBuffer, indexType, List.of("ChunkSection"), state.chunkSectionInfos());
                }
            }
        }
    }

    public static GpuBufferSlice getDecalData() {
        GpuBuffer ubo = decal_buffer;

        List<DecalInstance> list = getDecalList();
        ByteBuffer buf = buildDecalUbo(list);

        CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
        encoder.writeToBuffer(decal_slice, buf);

        MemoryUtil.memFree(buf);

        return decal_slice;
    }

    static ByteBuffer buildDecalUbo(List<DecalInstance> list){

        ByteBuffer buffer = MemoryUtil.memAlloc(uboSize);
        buffer.order(ByteOrder.nativeOrder());

        buffer.putInt(list.size());
        buffer.putInt(0).putInt(0).putInt(0);

        for (int i = 0; i < MAX_DECALS; i++) {

            if (i < list.size()) {
                buffer = packDecal(buffer, list.get(i));
            } else {
                buffer.position(buffer.position() + STRIDE);
            }

        }
        buffer.flip();
        return buffer;
    }

    static ByteBuffer packDecal(ByteBuffer b, DecalInstance d) {

        b.putFloat(0);b.putFloat(0);b.putFloat(0);
        b.putFloat(0);

        putVec3(b, d.getStartPos().toVector3f());
        b.putFloat(0);

        putVec3(b, d.getEndPos().toVector3f());
        b.putFloat(0);

        putVec3(b, d.getForward().toVector3f());
        b.putFloat(0);

        putVec3(b, d.getRight().toVector3f());
        b.putFloat(0);

        b.putFloat(d.getUV().x);
        b.putFloat(d.getUV().y);
        b.putFloat(d.getUV().z);
        b.putFloat(d.getUV().w);

        b.putInt(d.getBlendMode().ordinal());
        b.putInt(0).putInt(0).putInt(0);
        return b;
    }

    static List<DecalInstance> getDecalList(){

        List<DecalInstance> list = new ArrayList<>();

        for(DecalInstance instance : WORLD_DECALS){
            Vec3 vec1 = instance.getStartPos().add(Minecraft.getInstance().gameRenderer.getMainCamera().position.scale(-1));
            Vec3 vec2 = instance.getEndPos().add(Minecraft.getInstance().gameRenderer.getMainCamera().position.scale(-1));
            if(vec1.length() < MAX_DISTANCE && vec2.length() < MAX_DISTANCE){
                list.add(instance);
            }
        }
//        WORLD_DECALS_MAP.forEach((pos, directions) -> {
//            directions.forEach((direction, instances) -> {
//                instances.forEach((decalInstance -> {
//                    if(!list.contains(decalInstance)) list.add(decalInstance);
//                }));
//            });
//        });

        DecalInstance instance = PREVIEW_DECAL;
        if(instance != null){
            list.add(instance);
        }

        return list;
    }

    public static DecalInstance getClickedDecal(Vec3 pos, Direction direction){
        List<DecalInstance> checkedInstances = new ArrayList<>();
        for(DecalInstance instance : WORLD_DECALS){
            if(isVecInside(instance.getStartPos(), instance.getEndPos(), pos)){
                if(instance.direction() == direction){
                    checkedInstances.add(instance);
                    System.out.println("ADDED");
                }
            }
        }

        checkedInstances = checkedInstances.reversed();

        for(DecalInstance instance : checkedInstances){
            System.out.println("TEST");
            if(instance.getHitbox().inflate(0.1f).contains(pos)){
                System.out.println("GAVE INSTANCE");
                return instance;
            }
        }


        return null;
    }

    public static boolean isVecInside(Vec3 start, Vec3 end, Vec3 point) {
        AABB box = new AABB(start.x(), start.y(), start.z(), end.x(), end.y(), end.z()).inflate(0.55f);

        return box.contains(point);
    }

    static void putVec3(ByteBuffer b, Vector3f v){ b.putFloat(v.x).putFloat(v.y).putFloat(v.z); }
}
