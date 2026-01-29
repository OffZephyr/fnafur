package net.zephyr.fnafur.rendering.lighting;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderSystem;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class LightShadowUbo {

    // std140 mat4 = 64 bytes
    private static final int SIZE = 64;

    public static final GpuBuffer buffer = RenderSystem.getDevice().createBuffer(
            () -> "LightShadow UBO",
            GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_COPY_DST,
            SIZE
    );

    public static final GpuBufferSlice slice = new GpuBufferSlice(buffer, 0, SIZE);

    private LightShadowUbo() {}

    public static void upload(Matrix4f lightViewProj) {
        ByteBuffer bb = MemoryUtil.memAlloc(SIZE).order(ByteOrder.nativeOrder());

        float[] tmp = new float[16];
        lightViewProj.get(tmp);
        for (int i = 0; i < 16; i++) bb.putFloat(tmp[i]);

        bb.flip();

        CommandEncoder enc = RenderSystem.getDevice().createCommandEncoder();
        enc.writeToBuffer(slice, bb);

        MemoryUtil.memFree(bb);
    }
}