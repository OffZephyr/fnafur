package net.zephyr.fnafur.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.zephyr.fnafur.client.gui.screens.main_menu.FnafSplashOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(LoadingOverlay.class)
public class LoadingOverlayMixin {
    @Unique
    private Minecraft client = null;
    @Unique
    private ReloadInstance reload = null;
    @Unique
    private Consumer<Optional<Throwable>> exceptionHandler = null;
    @Unique
    private boolean reloading = false;
    @Inject(method = "<init>", at = @At("TAIL"))
    public void getInfo(Minecraft client, ReloadInstance monitor, Consumer<Optional<Throwable>> exceptionHandler, boolean reloading, CallbackInfo ci) {
        this.client = client;
        this.reload = monitor;
        this.exceptionHandler = exceptionHandler;
        this.reloading = reloading;
    }
    @Inject(method = "render", at = @At("HEAD"))
    public void newOverlay(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!(Minecraft.getInstance().getOverlay() instanceof FnafSplashOverlay)) {
            Minecraft.getInstance().setOverlay(new FnafSplashOverlay(client, reload, exceptionHandler, reloading));
        }
    }
    @Inject(method = "registerTextures", at = @At("HEAD"))
    private static void textureSetup(TextureManager textureManager, CallbackInfo ci) {
        FnafSplashOverlay.init(textureManager);
    }
}
