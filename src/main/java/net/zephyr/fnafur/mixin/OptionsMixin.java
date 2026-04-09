package net.zephyr.fnafur.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.CameraType;
import net.zephyr.fnafur.util.mixinAccessing.IUniversePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public class OptionsMixin {

    @Inject(method = "setCameraType", at = @At("HEAD"), cancellable = true)
    void setPerspective(CameraType perspective, CallbackInfo ci){
        if(((IUniversePlayer) Minecraft.getInstance().player).getMaskDelta() != 0 && ((IUniversePlayer) Minecraft.getInstance().player).getMaskDelta() != 1.5){
            ci.cancel();
        }
    }
}
