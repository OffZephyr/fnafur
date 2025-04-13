package net.zephyr.fnafur.client.lighting;


import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.debug.LightDebugRenderer;
import net.minecraft.client.render.item.tint.GrassTintSource;
import net.minecraft.client.resource.GrassColormapResourceSupplier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.LightType;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;

public class RGBLightMap {
    private final SimpleFramebuffer lightmapFramebuffer;
    private final SimpleFramebuffer redLightmapFramebuffer;
    private final SimpleFramebuffer greenLightmapFramebuffer;
    private final SimpleFramebuffer blueLightmapFramebuffer;
    public RGBLightMap(){

        this.lightmapFramebuffer = new SimpleFramebuffer(16, 16, false);
        this.lightmapFramebuffer.setTexFilter(9729);
        this.lightmapFramebuffer.setClearColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.lightmapFramebuffer.clear();

        this.redLightmapFramebuffer = new SimpleFramebuffer(16, 16, false);
        this.redLightmapFramebuffer.setTexFilter(9729);
        this.redLightmapFramebuffer.setClearColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.redLightmapFramebuffer.clear();

        this.greenLightmapFramebuffer = new SimpleFramebuffer(16, 16, false);
        this.greenLightmapFramebuffer.setTexFilter(9729);
        this.greenLightmapFramebuffer.setClearColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.greenLightmapFramebuffer.clear();

        this.blueLightmapFramebuffer = new SimpleFramebuffer(16, 16, false);
        this.blueLightmapFramebuffer.setTexFilter(9729);
        this.blueLightmapFramebuffer.setClearColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.blueLightmapFramebuffer.clear();
    }

    public static final VertexFormat POSITION_NORMAL = VertexFormat.builder()
            .add("Position", VertexFormatElement.POSITION)
            .add("Normal", VertexFormatElement.NORMAL)
            .build();
    public static VertexFormatElement register(int id, int uvIndex, VertexFormatElement.ComponentType type, VertexFormatElement.Usage usage, int count) {
        return new VertexFormatElement(id, uvIndex, type, usage, count);
    }
    public ShaderProgram updateShader(ShaderProgram shaderProgram, float ambientLightFactor, float lightingTicksLeft, float flickerIntensity, boolean shouldBrightenLighting, Vector3f skyColor, float nightVisionFactor, float darkness, float skyDarkness, float gamma, float darknessFactor){

        shaderProgram.getUniformOrDefault("AmbientLightFactor").set(ambientLightFactor); // ambientLightFactor
        shaderProgram.getUniformOrDefault("SkyFactor").set(lightingTicksLeft); // lightingTicksLeft
        shaderProgram.getUniformOrDefault("BlockFactor").set(flickerIntensity); // flickerIntensity
        shaderProgram.getUniformOrDefault("UseBrightLightmap").set(shouldBrightenLighting ? 1 : 0); // shouldBrightenLighting ? 1 : 0
        shaderProgram.getUniformOrDefault("SkyLightColor").set(skyColor); // skyColor
        shaderProgram.getUniformOrDefault("NightVisionFactor").set(nightVisionFactor); // nightVisionFactor
        shaderProgram.getUniformOrDefault("DarknessScale").set(darkness); // darkness
        shaderProgram.getUniformOrDefault("DarkenWorldFactor").set(skyDarkness); // skyDarkness
        shaderProgram.getUniformOrDefault("BrightnessFactor").set(Math.max(0.0F, gamma - darknessFactor)); // Math.max(0.0F, gamma - darknessFactor)
        return shaderProgram;
    }

    public void enable(){
        RenderSystem.setShaderTexture(2, this.lightmapFramebuffer.getColorAttachment());
        RenderSystem.setShaderTexture(9, this.redLightmapFramebuffer.getColorAttachment());
        RenderSystem.setShaderTexture(10, this.greenLightmapFramebuffer.getColorAttachment());
        RenderSystem.setShaderTexture(11, this.blueLightmapFramebuffer.getColorAttachment());
    }
    public void disable(){
        RenderSystem.setShaderTexture(2, 0);
        RenderSystem.setShaderTexture(9, 0);
        RenderSystem.setShaderTexture(10, 0);
        RenderSystem.setShaderTexture(11, 0);
    }
    public void close() {
        this.lightmapFramebuffer.delete();
        this.redLightmapFramebuffer.delete();
        this.greenLightmapFramebuffer.delete();
        this.blueLightmapFramebuffer.delete();
    }
    public void renderBuffer(ShaderProgram shaderProgram) {
        renderColoredBuffer(shaderProgram, redLightmapFramebuffer, 0xFFFF0000);
        renderColoredBuffer(shaderProgram, greenLightmapFramebuffer, 0xFF00FF00);
        renderColoredBuffer(shaderProgram, blueLightmapFramebuffer, 0xFF0000FF);
        renderColoredBuffer(shaderProgram, lightmapFramebuffer, 0xFFFFFFFF);
    }
    public static int getLightmapCoordinates(BlockRenderView world, BlockState state, BlockPos pos){

        if (state.hasEmissiveLighting(world, pos)) {
            return 15728880;
        } else {
            int i = world.getLightLevel(LightType.SKY, pos);
            int j = world.getLightLevel(LightType.BLOCK, pos);
            int k = state.getLuminance();
            if (j < k) {
                j = k;
            }

            return i << 20 | j << 4;
        }
    }
    public void renderColoredBuffer(ShaderProgram shaderProgram, SimpleFramebuffer framebuffer, int color) {

        framebuffer.beginWrite(true);

        BufferBuilder bufferBuilder = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

        bufferBuilder.vertex(0.0F, 0.0F, 0.0F)
                .texture(0.0f, 0.0f)
        ;
        bufferBuilder.vertex(1.0F, 0.0F, 0.0F)
                .texture(0.0f, 0.0f)
        ;
        bufferBuilder.vertex(1.0F, 1.0F, 0.0F)
                .texture(0.0f, 0.0f)
        ;
        bufferBuilder.vertex(0.0F, 1.0F, 0.0F)
                .texture(0.0f, 0.0f)
        ;
        bufferBuilder.vertex(0.0F, 0.0F, 1.0F)
                .texture(1.0f, 1.0f)
        ;
        bufferBuilder.vertex(1.0F, 0.0F, 1.0F)
                .texture(1.0f, 1.0f)
        ;
        bufferBuilder.vertex(1.0F, 1.0F, 1.0F)
                .texture(1.0f, 1.0f)
        ;
        bufferBuilder.vertex(0.0F, 1.0F, 1.0F)
                .texture(1.0f, 1.0f)
        ;
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

        framebuffer.endWrite();
    }
}
