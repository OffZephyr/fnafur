package net.zephyr.fnafur.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.debug.LightSectionDebugRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.zephyr.fnafur.client.rendering.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin {
    @Unique
    CameraMapRenderer mapRenderer = new CameraMapRenderer();
    @Unique
    FloorPropPlacingRenderer floorPropPlacingRenderer = new FloorPropPlacingRenderer();
    @Unique
    StickerPlacingRenderer stickerPlacingRenderer = new StickerPlacingRenderer();
    @Unique
    TileDoorPlacingRenderer tileDoorPlacingRenderer = new TileDoorPlacingRenderer();
    @Unique
    SpecialBlockPlacingRenderer specialBlockPlacingRenderer = new SpecialBlockPlacingRenderer();
    @Unique
    LinkRenderer linkRenderer = new LinkRenderer();

    @Inject(method = "emitGizmos", at = @At("HEAD"))
    public void render(Frustum frustum, double cameraX, double cameraY, double cameraZ, float tickProgress, CallbackInfo ci){
        PoseStack matrices = new PoseStack();
        matrices.pushPose();
        MultiBufferSource.BufferSource vertexConsumers = Minecraft.getInstance().renderBuffers().bufferSource();
        mapRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        specialBlockPlacingRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        floorPropPlacingRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        stickerPlacingRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        tileDoorPlacingRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);

        //linkRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        matrices.popPose();
    }
}
