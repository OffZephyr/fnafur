package net.zephyr.fnafur.rendering.lighting;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AreaLightManager {

    public static final int MAX_DISTANCE = 128;
    public static final int MAX_LIGHTS = 128;

    /**
     * std140 sizing:
     * Header: 16 bytes (int + ivec3)
     *
     * Per-light:
     *   vec4 pos_radius              16
     *   vec4 dir_length              16
     *   vec4 color_intensity         16
     *   vec4 normal_influence        16
     *   vec4 smooth_shadowLayer      16
     *   vec4 proj_uv                 16
     *   vec4 shape_uv                16
     *   mat4 lightViewProj           64
     * = 192 bytes per light
     */
    public static final int STRIDE = 192;
    public static final int UBO_SIZE = 16 + MAX_LIGHTS * STRIDE;

    public static final List<AreaLightInstance> WORLD_LIGHTS = new ArrayList<>();

    public static final GpuBuffer lightBuffer = RenderSystem.getDevice().createBuffer(
            () -> "AreaLight UBO",
            GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_COPY_DST,
            UBO_SIZE
    );

    public static final GpuBufferSlice lightSlice = new GpuBufferSlice(lightBuffer, 0, UBO_SIZE);

    // Reuse to avoid per-light allocations
    private static final float[] MAT_TMP_16 = new float[16];

    /**
     * Uploads a std140 UBO for the provided visible lights.
     * IMPORTANT: This expects each AreaLightInstance already has shadowIndex set
     * (0..MAX_SHADOWED_LIGHTS-1 or -1) by the shadow renderer.
     */
    public static GpuBufferSlice uploadAndGetLightUbo(List<AreaLightInstance> visibleLights) {
        ByteBuffer buf = buildLightUbo(visibleLights);

        CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
        encoder.writeToBuffer(lightSlice, buf);

        MemoryUtil.memFree(buf);
        return lightSlice;
    }

    /**
     * Returns lights near the camera, sorted by distance ascending (closest first).
     * This is important because the first MAX_SHADOWED_LIGHTS are typically assigned shadows.
     */
    public static List<AreaLightInstance> getVisibleLights() {
        List<AreaLightInstance> out = new ArrayList<>();

        Vec3d cam = MinecraftClient.getInstance().gameRenderer.getCamera().pos;

        for (AreaLightInstance L : WORLD_LIGHTS) {
            Vec3d rel = L.getPosition().subtract(cam);
            if (rel.length() < MAX_DISTANCE) {
                out.add(L);
                if (out.size() >= MAX_LIGHTS) break;
            }
        }

        // Closest first (stable shadow assignment)
        out.sort(Comparator.comparingDouble(l -> l.getPosition().squaredDistanceTo(cam)));
        return out;
    }

    private static ByteBuffer buildLightUbo(List<AreaLightInstance> list) {
        ByteBuffer buffer = MemoryUtil.memAlloc(UBO_SIZE).order(ByteOrder.nativeOrder());

        int count = Math.min(list.size(), MAX_LIGHTS);

        // Header
        buffer.putInt(count);
        buffer.putInt(0).putInt(0).putInt(0);

        // Body
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
        Vec3d p = L.getPosition();

        // Normalize defensively to avoid weird lighting/shadow bias if callers forget.
        Vector3f dir = new Vector3f(L.getDirection()).normalize();
        Vector3f col = new Vector3f(L.getColor());
        Vector3f nrm = new Vector3f(L.getNormal()).normalize();

        // vec4 pos_radius
        b.putFloat((float) p.x).putFloat((float) p.y).putFloat((float) p.z).putFloat(L.getRadius());
        b.putFloat((float) p.x).putFloat((float) p.y).putFloat((float) p.z).putFloat(L.getRadius());

        // vec4 dir_length
        putVec3(b, dir);
        b.putFloat(L.getLength());

        // vec4 color_intensity
        putVec3(b, col);
        b.putFloat(L.getIntensity());

        // vec4 normal_influence
        putVec3(b, nrm);
        b.putFloat(L.getNormalInfluence());

        // vec4 smooth_shadowLayer
        // x = edge smoothness (0..1)
        // y = distance falloff factor (>=0)
        // z = projection radius
        // w = shadowIndex (-1 or 0..MAX_SHADOWED_LIGHTS-1)
        b.putFloat(L.getEdgeSmoothness());
        b.putFloat(L.getDistanceSmoothness());
        b.putFloat(L.getProjectionRadius());
        b.putFloat((float) L.getShadowIndex());

        // vec4 proj_uv
        Vector4f proj = L.getProjectedUVRect();
        b.putFloat(proj.x).putFloat(proj.y).putFloat(proj.z).putFloat(proj.w);

        // vec4 shape_uv
        Vector4f shape = L.getShapeUVRect();
        b.putFloat(shape.x).putFloat(shape.y).putFloat(shape.z).putFloat(shape.w);

        // mat4 lightViewProj (std140 mat4 = 16 floats)
        Matrix4f m = L.getLightViewProj();
        m.get(MAT_TMP_16);
        for (int i = 0; i < 16; i++) b.putFloat(MAT_TMP_16[i]);
    }

    static void putVec3(ByteBuffer b, Vector3f v) {
        b.putFloat(v.x).putFloat(v.y).putFloat(v.z);
    }
}