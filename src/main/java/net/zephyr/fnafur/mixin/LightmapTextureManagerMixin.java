package net.zephyr.fnafur.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.zephyr.fnafur.blocks.camera_desk.CameraRenderer;
import net.zephyr.fnafur.client.gui.screens.CameraTabletScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @ModifyVariable(method = "update", at = @At("STORE"), ordinal = 6)
    float brightnessStrength(float x){
        if (CameraRenderer.isDrawing() && CameraRenderer.illuminateScreen) {
            return 2f;
        }
        if(MinecraftClient.getInstance().currentScreen instanceof CameraTabletScreen screen) {
            if (screen.nightVision()) {
                return 2f;
            }
        }
        return x;
    }
}
