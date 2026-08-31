package net.zephyr.fnafur.rendering.lighting;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class AreaLightManager {

    public static final int MAX_DISTANCE = 128;
    public static final int MAX_LIGHTS = 128;

    public static final int STRIDE = 192;
    public static final int UBO_SIZE = 16 + MAX_LIGHTS * STRIDE;

    public static final Map<ILightHolder, AreaLightInstance> WORLD_LIGHT_MAP = new HashMap<>();
    public static final List<AreaLightInstance> WORLD_LIGHTS = new ArrayList<>();

    public static final GpuBuffer lightBuffer = RenderSystem.getDevice().createBuffer(
            () -> "AreaLight UBO",
            GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_COPY_DST,
            UBO_SIZE
    );

    public static final GpuBufferSlice lightSlice = new GpuBufferSlice(lightBuffer, 0, UBO_SIZE);

    private static final float[] MAT_TMP_16 = new float[16];

    public static GpuBufferSlice uploadAndGetLightUbo(List<AreaLightInstance> visibleLights) {
        ByteBuffer buf = buildLightUbo(visibleLights);

        CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
        encoder.writeToBuffer(lightSlice, buf);

        MemoryUtil.memFree(buf);
        return lightSlice;
    }

    public static List<AreaLightInstance> getVisibleLights() {
        List<AreaLightInstance> out = new ArrayList<>();

        Vec3 cam = Minecraft.getInstance().gameRenderer.getMainCamera().position;

        for (AreaLightInstance L : WORLD_LIGHTS) {
            Vec3 rel = L.getPosition().subtract(cam);
            if (rel.length() < MAX_DISTANCE) {
                out.add(L);
                if (out.size() >= MAX_LIGHTS) break;
            }
        }

        out.sort(Comparator.comparingDouble(l -> l.getPosition().distanceToSqr(cam)));
        return out;
    }

    private static ByteBuffer buildLightUbo(List<AreaLightInstance> list) {
        ByteBuffer buffer = MemoryUtil.memAlloc(UBO_SIZE).order(ByteOrder.nativeOrder());

        int count = Math.min(list.size(), MAX_LIGHTS);

        buffer.putInt(count);
        buffer.putInt(0).putInt(0).putInt(0);

        for (int i = 0; i < MAX_LIGHTS; i++) {
            if (i < count) {
                packLight(buffer, list.get(i));
            } else {
                buffer.position(buffer.position() + STRIDE);
            }
        }

        buffer.flip();
        return buffer;
    }

    private static void packLight(ByteBuffer b, AreaLightInstance L) {
        Vec3 cam = Minecraft.getInstance().gameRenderer.getMainCamera().position;
        Vec3 p = L.getPosition().subtract(cam);

        Vector3f dir = new Vector3f(L.getDirection()).normalize();
        Vector3f col = new Vector3f(L.getColor());
        Vector3f nrm = new Vector3f(L.getNormal()).normalize();

        b.putFloat((float) p.x).putFloat((float) p.y).putFloat((float) p.z).putFloat(L.getRadius());
        b.putFloat((float) p.x).putFloat((float) p.y).putFloat((float) p.z).putFloat(L.getRadius());

        putVec3(b, dir);
        b.putFloat(L.getLength());

        putVec3(b, col);
        b.putFloat(L.getIntensity());

        putVec3(b, nrm);
        b.putFloat(L.getNormalInfluence());

        b.putFloat(L.getEdgeSmoothness());
        b.putFloat(L.getDistanceSmoothness());
        b.putFloat(L.getProjectionRadius());
        b.putFloat((float) L.getShadowIndex());

        Vector4f proj = L.getProjectedUVRect();
        b.putFloat(proj.x).putFloat(proj.y).putFloat(proj.z).putFloat(proj.w);

        Vector4f shape = L.getShapeUVRect();
        b.putFloat(shape.x).putFloat(shape.y).putFloat(shape.z).putFloat(shape.w);

        Matrix4f m = L.getLightViewProj();
        m.get(MAT_TMP_16);
        for (int i = 0; i < 16; i++) b.putFloat(MAT_TMP_16[i]);
    }

    static void putVec3(ByteBuffer b, Vector3f v) {
        b.putFloat(v.x).putFloat(v.y).putFloat(v.z);
    }
}