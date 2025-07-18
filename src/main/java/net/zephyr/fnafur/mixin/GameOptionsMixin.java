package net.zephyr.fnafur.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import net.zephyr.fnafur.util.mixinAccessing.IUniversePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public class GameOptionsMixin {

    @Inject(method = "setPerspective", at = @At("HEAD"), cancellable = true)
    void setPerspective(Perspective perspective, CallbackInfo ci){
        if(((IUniversePlayer)MinecraftClient.getInstance().player).getMaskDelta() != 0 && ((IUniversePlayer)MinecraftClient.getInstance().player).getMaskDelta() != 1.5){
            ci.cancel();
        }
    }
}
