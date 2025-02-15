package net.zephyr.fnafur.mixin;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.zephyr.fnafur.client.rendering.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin {
    CameraMapRenderer mapRenderer = new CameraMapRenderer();
    FloorPropPlacingRenderer floorPropPlacingRenderer = new FloorPropPlacingRenderer();
    WallPropPlacingRenderer wallPropPlacingRenderer = new WallPropPlacingRenderer();
    StickerPlacingRenderer stickerPlacingRenderer = new StickerPlacingRenderer();
    EnergyInteractionRenderer energyInteractionRenderer = new EnergyInteractionRenderer();

    @Inject(method = "render", at = @At("HEAD"))
    public void render(MatrixStack matrices, Frustum frustum, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci){
        mapRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        floorPropPlacingRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        wallPropPlacingRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        stickerPlacingRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        energyInteractionRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
    }
}
