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
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GpuSampler;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.BlockRenderLayerGroup;
import net.minecraft.client.render.SectionRenderState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
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

    public static void DecalRenderHook(SectionRenderState state, BlockRenderLayerGroup group, GpuSampler sampler) {

        RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.DrawMode.QUADS);
        GpuBuffer gpuBuffer = state.maxIndicesRequired() == 0 ? null : shapeIndexBuffer.getIndexBuffer(state.maxIndicesRequired());
        VertexFormat.IndexType indexType = state.maxIndicesRequired() == 0 ? null : shapeIndexBuffer.getIndexType();
        BlockRenderLayer[] blockRenderLayers = group.getLayers();
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        boolean bl = SharedConstants.HOTKEYS && minecraftClient.wireFrame;
        Framebuffer framebuffer = group.getFramebuffer();

        GpuBufferSlice decalBuffer = getDecalData();

        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(
                        () -> "Section layers for " + group.getName(),
                        framebuffer.getColorAttachmentView(),
                        OptionalInt.empty(),
                        framebuffer.getDepthAttachmentView(),
                        OptionalDouble.empty()
                )) {
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.bindTexture(
                    "Sampler2", minecraftClient.gameRenderer.getLightmapTextureManager().getGlTextureView(), RenderSystem.getSamplerCache().get(FilterMode.LINEAR)
            );

            for (BlockRenderLayer blockRenderLayer : blockRenderLayers) {
                List<RenderPass.RenderObject<GpuBufferSlice[]>> list = (List<RenderPass.RenderObject<GpuBufferSlice[]>>) state.drawsPerLayer().get(blockRenderLayer);
                if (!list.isEmpty()) {
                    if (blockRenderLayer == BlockRenderLayer.TRANSLUCENT) {
                        list = list.reversed();
                    }

                    Sprite sprite = MinecraftClient.getInstance().getBlockRenderManager()
                            .getModels()
                            .getModel(Blocks.STONE.getDefaultState()).getParts(Random.create()).getFirst().getQuads(Direction.NORTH).getFirst().sprite();

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
            Vec3d vec1 = instance.getStartPos().add(MinecraftClient.getInstance().gameRenderer.getCamera().pos.multiply(-1));
            Vec3d vec2 = instance.getEndPos().add(MinecraftClient.getInstance().gameRenderer.getCamera().pos.multiply(-1));
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

    public static DecalInstance getClickedDecal(Vec3d pos, Direction direction){
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
            if(instance.getHitbox().expand(0.1f).contains(pos)){
                System.out.println("GAVE INSTANCE");
                return instance;
            }
        }


        return null;
    }

    public static boolean isVecInside(Vec3d start, Vec3d end, Vec3d point) {
        Box box = new Box(start.getX(), start.getY(), start.getZ(), end.getX(), end.getY(), end.getZ()).expand(0.55f);

        return box.contains(point);
    }

    static void putVec3(ByteBuffer b, Vector3f v){ b.putFloat(v.x).putFloat(v.y).putFloat(v.z); }
}
