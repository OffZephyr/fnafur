package net.zephyr.fnafur.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.zephyr.fnafur.client.gui.screens.FnafCreativeInventoryScreen;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * When drawing a mirror, always use the mirror's framebuffer instead of the normal one.
 */
@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {
    @Unique
    PlayerEntity mixin$player;
    @Inject(method = "<init>", at = @At("TAIL"))
    void fnafCreativeTabInit(PlayerEntity player, CallbackInfo callbackInfo){
        mixin$player = player;
    }
    @Inject(method = "handledScreenTick", at = @At("HEAD"), cancellable = true)
    void fnafCreativeTabTick(CallbackInfo callbackInfo){
        if(!(((InventoryScreen) (Object) this) instanceof FnafInventoryScreen)){
            MinecraftClient.getInstance().setScreen(new FnafInventoryScreen(mixin$player));
            callbackInfo.cancel();
        }
    }
}
