package net.zephyr.fnafur.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Player;
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
    Player mixin$player;
    @Inject(method = "<init>", at = @At("TAIL"))
    void fnafCreativeTabInit(Player player, CallbackInfo callbackInfo){
        mixin$player = player;
    }
    @Inject(method = "containerTick", at = @At("HEAD"), cancellable = true)
    void fnafCreativeTabTick(CallbackInfo callbackInfo){
        if(!(((InventoryScreen) (Object) this) instanceof FnafInventoryScreen)){
            Minecraft.getInstance().setScreen(new FnafInventoryScreen(mixin$player));
            callbackInfo.cancel();
        }
    }
}
