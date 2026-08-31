package net.zephyr.fnafur.client.gui.screens.main_menu;

import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.ReloadableTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import com.mojang.math.Axis;
import net.zephyr.fnafur.FnafUniverseRebuilt;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class FnafSplashOverlay extends LoadingOverlay {
    private static final Identifier BG = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/bg.png");
    public static final Identifier LOADING = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/loading.png");
    private static final Identifier STARS = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/stars.png");
    private static final Identifier PIXELS = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/pixels.png");

    private final Minecraft client;
    private final ReloadInstance reload;
    private final Consumer<Optional<Throwable>> exceptionHandler;
    private final boolean reloading;
    private long reloadCompleteTime = -1L;
    private long reloadStartTime = -1L;
    float gearRotation = 0;
    float fadeStart = 255;
    float fadeGoal = 0;
    float fadeTimer = 0;
    float fadeTimerGoal = 1;
    private float progress;
    public FnafSplashOverlay(Minecraft client, ReloadInstance monitor, Consumer<Optional<Throwable>> exceptionHandler, boolean reloading) {
        super(client, monitor, exceptionHandler, reloading);
        this.client = client;
        this.reload = monitor;
        this.exceptionHandler = exceptionHandler;
        this.reloading = reloading;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        int width = context.guiWidth();
        int height = context.guiHeight();

        long l = Util.getMillis();
        if (this.reloading && this.reloadStartTime == -1L) {
            this.reloadStartTime = l;
        }

        float f = this.reloadCompleteTime > -1L ? (float)(l - this.reloadCompleteTime) / 1000.0F : -1.0F;
        float g = this.reloadStartTime > -1L ? (float)(l - this.reloadStartTime) / 500.0F : -1.0F;
        float h;

        if (f >= 1.0F) {
            if (this.client.screen != null) {
                if(this.client.screen instanceof FnafTitleScreen title){
                    title.tab = FnafUniverseRebuilt.DISABLE_DISCLAIMER ? 0 : -1;
                }
                this.client.screen.extractRenderState(context, mouseX, mouseY, delta);
            }

            h = 1.0F - Mth.clamp(f - 1.0F, 0.0F, 1.0F);
        } else if (this.reloading) {
            if (this.client.screen != null && g < 1.0F) {
                this.client.screen.extractRenderState(context, mouseX, mouseY, delta);
            }

            h = Mth.clamp(g, 0.0F, 1.0F);
        } else {
            int m = 0;
            int n = 0;
            int o = 0;
            RenderSystem.getDevice().createCommandEncoder().clearColorTexture(this.client.getMainRenderTarget().getColorTexture(), ARGB.color(255, m, n, o));
            GlStateManager._clear(16384);
            h = 1.0F;
        }

        float u = this.reload.getActualProgress();
        this.progress = Mth.clamp(this.progress * 0.95F + u * 0.050000012F, 0.0F, 1.0F);

        context.fill(RenderPipelines.GUI,0, 0, width, height, ARGB.color((int) (h * 255), 0, 0, 0));
        context.blit(RenderPipelines.GUI_TEXTURED, BG, 0, 0, 0, 0, width, height, 8, 8, 8, 8, ARGB.white(h));

        float loadingWidth = (512f / 1080f) * height;
        float loadingHeight = (128f / 1080f) * height;

        float borderWidth = (4f / 1080f) * height;
        float progressHeight = (34f / 1080f) * height;

        context.blit(RenderPipelines.GUI_TEXTURED, LOADING, width - (int) loadingWidth - (int) borderWidth, height - (int) loadingHeight - (int) borderWidth, 0, 0, (int) loadingWidth, (int) loadingHeight, 512, 128, 512, 512, ARGB.white(h));

        float x2 = Mth.lerpInt(this.progress, width - (int) loadingWidth + (int) borderWidth, (int) width  - (int) borderWidth - (int) borderWidth);
        context.fill(width - (int) loadingWidth + (int) borderWidth, height - (int) progressHeight - (int) borderWidth - (int) borderWidth - (int) borderWidth - (int) borderWidth, (int) x2, height - (int) borderWidth - (int) borderWidth - (int) borderWidth - (int) borderWidth, ARGB.white(h));

        float gearWidth = (82f / 1080f) * height;

        this.gearRotation += (delta/20f) * 45f;

        context.pose().pushMatrix();
        context.pose().translate(borderWidth + borderWidth + borderWidth + borderWidth + borderWidth, height - gearWidth - borderWidth - borderWidth - borderWidth);
        context.pose().translate(gearWidth/2f, gearWidth/2f);
        context.pose().rotate(gearRotation * Mth.DEG_TO_RAD);
        context.pose().translate(-gearWidth/2f, -gearWidth/2f);
        context.blit(RenderPipelines.GUI_TEXTURED, LOADING, 0, 0, 512-82, 512-82, (int) gearWidth, (int) gearWidth, 82, 82, 512, 512, ARGB.white(h));
        context.pose().popMatrix();

        float hatHeight = (52f / 1080f) * height;
        float hatWidth = (120f / 1080f) * height;
        context.blit(RenderPipelines.GUI_TEXTURED, LOADING, (int) borderWidth, height - (int) gearWidth - (int) hatHeight - (int) borderWidth, 0, 512-52, (int) hatWidth, (int) hatHeight, 120, 52, 512, 512, ARGB.white(h));

        context.blit(RenderPipelines.GUI_TEXTURED, STARS, 0, 0, 0, 0, width, height, 8, 8, 8, 8, ARGB.color((int) (h * ARGB.alpha(0x8dFFFFFF)), 255, 255, 255));
        context.blit(RenderPipelines.GUI_TEXTURED, PIXELS, 0, 0, 0, 0, width, height, 8, 8, 8, 8, ARGB.color((int) (h * ARGB.alpha(0x0dFFFFFF)), 255, 255, 255));


        this.fadeTimer = Math.clamp(this.fadeTimer + delta/20f, 0, fadeTimerGoal);
        context.fill(0, 0, width, height, ARGB.color((int) Mth.lerp(fadeTimer / fadeTimerGoal, fadeStart, fadeGoal), 0, 0, 0));


        if (f >= 2.0F) {
            this.client.setOverlay(null);
        }


        if (this.reloadCompleteTime == -1L && this.reload.isDone() && (!this.reloading || g >= 2.0F)) {
            try {
                this.reload.checkExceptions();
                this.exceptionHandler.accept(Optional.empty());
            } catch (Throwable var24) {
                this.exceptionHandler.accept(Optional.of(var24));
            }

            this.reloadCompleteTime = Util.getMillis();
            if (this.client.screen != null) {
                this.client.screen.init(context.guiWidth(), context.guiHeight());
            }
        }
    }
    public static void init(TextureManager textureManager) {
        //textureManager.registerTexture(STARS, new LoadingTexture(STARS));
        //textureManager.registerTexture(PIXELS, new LoadingTexture(PIXELS));
        //textureManager.registerTexture(LOADING, new LoadingTexture(LOADING));
        //textureManager.registerTexture(BG, new LoadingTexture(BG));
    }
}
