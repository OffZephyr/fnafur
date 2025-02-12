package net.zephyr.fnafur.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.zephyr.fnafur.client.gui.screens.FnafCreativeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * When drawing a mirror, always use the mirror's framebuffer instead of the normal one.
 */
@Mixin(net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen.class)
public class CreativeInventoryScreenMixin {
    @Unique
    ClientPlayerEntity mixin$player;
    @Unique
    FeatureSet mixin$enabledFeatures;
    @Unique
    boolean mixin$operatorTabEnabled;
    @Inject(method = "<init>", at = @At("TAIL"))
    void fnafCreativeTabInit(ClientPlayerEntity player, FeatureSet enabledFeatures, boolean operatorTabEnabled, CallbackInfo callbackInfo){
        mixin$player = player;
        mixin$enabledFeatures = enabledFeatures;
        mixin$operatorTabEnabled = operatorTabEnabled;
    }
    @Inject(method = "handledScreenTick", at = @At("HEAD"), cancellable = true)
    void fnafCreativeTabTick(CallbackInfo callbackInfo){
        if(!(((CreativeInventoryScreen) (Object) this) instanceof FnafCreativeInventoryScreen)){
            MinecraftClient.getInstance().setScreen(new FnafCreativeInventoryScreen(mixin$player, mixin$enabledFeatures, mixin$operatorTabEnabled));
            callbackInfo.cancel();
        }
    }
}
