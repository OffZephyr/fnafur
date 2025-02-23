package net.zephyr.fnafur.blocks.camera_desk;

import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.*;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.render.*;
import net.minecraft.client.util.Pool;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.util.mixinAccessing.IPostProcessorLoader;
import net.zephyr.fnafur.util.compat.Iris;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.Map;

public class CameraRenderer {

    PostEffectProcessor monitorPostProcessor;
    private boolean monitorPostProcessorEnabled;
    private static final Identifier normalShader = Identifier.of(FnafUniverseResuited.MOD_ID, "shaders/post/camera_desk.json");
    private static final Identifier nvShader = Identifier.of(FnafUniverseResuited.MOD_ID, "shaders/post/camera_desk_nightvision.json");
    private static final Framebuffer[] framebuffers = new Framebuffer[3];
    private static int Deep;
    public static boolean nightVision;
    public static boolean illuminateScreen;
    public static Map<BlockPos, Boolean> dirty = new HashMap<>();
    public static Map<Framebuffer, Float> noiseIntensity = new HashMap<>();

    private static final Pool pool = new Pool(3);

    public static boolean isDrawing() {
        return Deep > 0;
    }

    public static boolean canDraw() {
        boolean fabulous = MinecraftClient.getInstance().options.getGraphicsMode().getValue() == GraphicsMode.FABULOUS;
        return !fabulous && !Iris.isInstalled() && Deep < 3;
    }

    @Nullable
    public static Framebuffer getFramebuffer() {
        if (!canDraw() || !isDrawing()) return null;
        return framebuffers[Deep - 1];
    }

    public static void onResize(int width, int height) {
        for (int i = 0; i < framebuffers.length; i++) {
            framebuffers[i] = new SimpleFramebuffer(width, height, true);
            dirty.replaceAll((p, v) -> true);
            ((IPostProcessorLoader) MinecraftClient.getInstance().gameRenderer).resizePostProcessor(framebuffers[i], width, height);
        }
    }

    public static void onRenderWorld(WorldRenderContext ctx) {
        for (BlockPos pos : CameraDeskBlockEntity.posList) {
            var blockentity = ctx.world().getBlockEntity(pos);
            if (blockentity instanceof CameraDeskBlockEntity mirror) {
                var cameraPos = ctx.camera().getPos();
                var entityPos = mirror.getPos();
                float cameraYaw = ctx.camera().getYaw();
                float entityYaw = mirror.getYaw();
                float epsilon = 180;
                boolean withinDistance = entityPos.toCenterPos().distanceTo(cameraPos) < 128.0;
                boolean withinYawRange = Math.abs(entityYaw - cameraYaw) % 360 < 180 + epsilon && Math.abs(entityYaw - cameraYaw) % 360 > 180 - epsilon;


                if (withinDistance && withinYawRange) {
                    renderMirror(mirror, ctx.matrixStack(), ctx.tickCounter().getTickDelta(false));
                }
            }
        }
    }


    private static void renderMirror(CameraDeskBlockEntity entity, MatrixStack matrices, float tickDelta) {
        int tex = renderWorld(entity, tickDelta);
        int tex2 = MinecraftClient.getInstance().getTextureManager().getTexture(entity.staticTexture(tickDelta)).getGlId();

        if (tex == -1) {
            tex = tex2;
        }

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        var camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        var entityPos = entity.getPos().toCenterPos();
        var cameraPos = camera.getPos();
        var translation = entityPos.subtract(cameraPos);

        matrices.push();
        matrices.translate(0, 1, 0);
        matrices.translate(translation.x, translation.y, translation.z);
        matrices.multiply(entity.getWorld().getBlockState(entity.getPos()).get(CameraDeskBlock.FACING).getRotationQuaternion());

        matrices.translate(0, 0.5, 0);

        float tWidth = 0.4f;
        float tHeight = 0.4f;
        float vWidth = 0.5f;
        float vHeight = 0.5f;

        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX);

        var buffer = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

        RenderSystem.setShaderTexture(0, tex);

        buffer.vertex(matrices.peek().getPositionMatrix(), -vWidth, 0.0f, -vHeight).texture(0.5f - tWidth, 0.5f + tHeight);
        buffer.vertex(matrices.peek().getPositionMatrix(), -vWidth, 0.0f, vHeight).texture(0.5f - tWidth, 0.5f - tHeight);
        buffer.vertex(matrices.peek().getPositionMatrix(), vWidth, 0.0f, vHeight).texture(0.5f + tWidth, 0.5f - tHeight);
        buffer.vertex(matrices.peek().getPositionMatrix(), vWidth, 0.0f, -vHeight).texture(0.5f + tWidth, 0.5f + tHeight);

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        matrices.pop();

        RenderSystem.disablePolygonOffset();
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.depthMask(true);
    }
    private static int renderWorld(CameraDeskBlockEntity entity, float tickDelta) {
        if (!canDraw() || !entity.hasFootage()) return -1;

        nightVision = entity.isNightVision();

        var client = MinecraftClient.getInstance();
        var camera = client.gameRenderer.getCamera();
        var position = entity.getFeedPos();

        try {
            var oldModelViewStack = RenderSystem.getModelViewStack();
            var oldModelViewMat = new Matrix4f(RenderSystem.getModelViewMatrix());
            var prevProjMat = new Matrix4f(RenderSystem.getProjectionMatrix());
            var oldFrustum = client.worldRenderer.frustum;
            var oldPos = camera.pos;
            float oldYaw = camera.getYaw();
            float oldPitch = camera.getPitch();
            int oldFboWidth = client.getWindow().getFramebufferWidth();
            int oldFboHeight = client.getWindow().getFramebufferHeight();

            camera.pos = new Vec3d(position.toVector3f());

            camera.setRotation(entity.getFeedYaw(), entity.getFeedPitch());

            var cameraRotation = camera.getRotation();



            // Set up frustum
            var rotMat = new Matrix4f().rotate(cameraRotation.conjugate(new Quaternionf()));
            var projMat = client.gameRenderer.getBasicProjectionMatrix(70f);
            client.worldRenderer.setupFrustum(camera.getPos(), rotMat, projMat);
            RenderSystem.viewport(0, 0, MinecraftClient.getInstance().getFramebuffer().textureWidth, MinecraftClient.getInstance().getFramebuffer().textureHeight);

            Deep++;

            var framebuffer = getFramebuffer();
            if (framebuffer == null) {
                Deep--;
                return -1;
            }
            if(MinecraftClient.IS_SYSTEM_MAC) {
                framebuffer.clear();
            }

            if (isDirty(entity.getPos())) {
                ((IPostProcessorLoader) client.gameRenderer).setMonitorPostProcessor(normalShader, framebuffer);
            }
            Vec3d red = entity.getScreenColor()[0];
            Vec3d green = entity.getScreenColor()[1];
            Vec3d blue = entity.getScreenColor()[2];
            float sat = entity.getScreenSaturation();
            ((IPostProcessorLoader) client.gameRenderer).setMonitorUniform(framebuffer, "RedMatrix", (float)red.getX(), (float)red.getY(), (float)red.getX());
            ((IPostProcessorLoader) client.gameRenderer).setMonitorUniform(framebuffer, "GreenMatrix", (float)green.getX(), (float)green.getY(), (float)green.getX());
            ((IPostProcessorLoader) client.gameRenderer).setMonitorUniform(framebuffer, "BlueMatrix", (float)blue.getX(), (float)blue.getY(), (float)blue.getX());
            ((IPostProcessorLoader) client.gameRenderer).setMonitorUniform(framebuffer, "Saturation", sat);
            ((IPostProcessorLoader) client.gameRenderer).setMonitorUniform(framebuffer, "Time", tickDelta);
            ((IPostProcessorLoader) client.gameRenderer).setMonitorUniform(framebuffer, "NoiseIntensity", tickDelta);


            framebuffer.beginWrite(true);
            client.getWindow().setFramebufferWidth(framebuffer.textureWidth);
            client.getWindow().setFramebufferHeight(framebuffer.textureHeight);
            RenderSystem.setProjectionMatrix(projMat, ProjectionType.PERSPECTIVE);
            RenderSystem.modelViewStack = new Matrix4fStack(16);
            illuminateScreen = nightVision;
            client.gameRenderer.getLightmapTextureManager().tick();
            client.gameRenderer.getLightmapTextureManager().update(tickDelta);
            client.worldRenderer.render(pool, client.getRenderTickCounter(), false, camera, client.gameRenderer, rotMat, projMat);
            illuminateScreen = false;
            client.gameRenderer.getLightmapTextureManager().tick();
            client.gameRenderer.getLightmapTextureManager().update(tickDelta);

            ((IPostProcessorLoader)client.gameRenderer).renderMonitor(tickDelta, CameraRenderer.isDrawing(), framebuffer);
            Deep--;

            // Restore original values
            client.getFramebuffer().beginWrite(false);
            camera.pos = oldPos;
            camera.setRotation(oldYaw, oldPitch);
            client.worldRenderer.frustum = oldFrustum;
            RenderSystem.setProjectionMatrix(prevProjMat, ProjectionType.PERSPECTIVE);
            RenderSystem.modelViewStack = oldModelViewStack;
            client.getWindow().setFramebufferWidth(oldFboWidth);
            client.getWindow().setFramebufferHeight(oldFboHeight);
            RenderSystem.viewport(0, 0, client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());

            return framebuffer.getColorAttachment();
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public static boolean isDirty(BlockPos pos) {
        boolean val = dirty.get(pos);
        setDirty(pos, false);
        return val;
    }

    public static void setDirty(BlockPos pos, boolean value){
        dirty.put(pos, value);
    }
}

