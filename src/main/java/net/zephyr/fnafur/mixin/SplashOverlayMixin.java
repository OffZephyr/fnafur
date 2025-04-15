package net.zephyr.fnafur.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ResourceReload;
import net.zephyr.fnafur.client.gui.screens.main_menu.FnafSplashOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(SplashOverlay.class)
public class SplashOverlayMixin {
    @Unique
    private MinecraftClient client = null;
    @Unique
    private ResourceReload reload = null;
    @Unique
    private Consumer<Optional<Throwable>> exceptionHandler = null;
    @Unique
    private boolean reloading = false;
    @Inject(method = "<init>", at = @At("TAIL"))
    public void getInfo(MinecraftClient client, ResourceReload monitor, Consumer<Optional<Throwable>> exceptionHandler, boolean reloading, CallbackInfo ci) {
        this.client = client;
        this.reload = monitor;
        this.exceptionHandler = exceptionHandler;
        this.reloading = reloading;
    }
    @Inject(method = "render", at = @At("HEAD"))
    public void newOverlay(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!(MinecraftClient.getInstance().getOverlay() instanceof FnafSplashOverlay)) {
            MinecraftClient.getInstance().setOverlay(new FnafSplashOverlay(client, reload, exceptionHandler, reloading));
        }
    }
    @Inject(method = "init", at = @At("HEAD"))
    private static void textureSetup(TextureManager textureManager, CallbackInfo ci) {
        FnafSplashOverlay.init(textureManager);
    }
}
