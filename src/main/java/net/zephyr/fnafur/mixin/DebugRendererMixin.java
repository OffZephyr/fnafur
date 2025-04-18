package net.zephyr.fnafur.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.render.debug.LightDebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
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
    EnergyInteractionRenderer energyInteractionRenderer = new EnergyInteractionRenderer();
    @Unique
    TileDoorPlacingRenderer tileDoorPlacingRenderer = new TileDoorPlacingRenderer();
    @Unique
    SpecialBlockPlacingRenderer specialBlockPlacingRenderer = new SpecialBlockPlacingRenderer();

    @Inject(method = "render", at = @At("HEAD"))
    public void render(MatrixStack matrices, Frustum frustum, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci){
        mapRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        specialBlockPlacingRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        floorPropPlacingRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        stickerPlacingRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        energyInteractionRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        tileDoorPlacingRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
    }
}
