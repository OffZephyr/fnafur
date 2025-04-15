package net.zephyr.fnafur.client.gui.screens.main_menu;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderLoader;
import net.minecraft.client.gl.ShaderProgramDefinition;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.resource.metadata.TextureResourceMetadata;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ReloadableTexture;
import net.minecraft.client.texture.TextureContents;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.util.mixinAccessing.IShaderLoaderAccessor;
import net.zephyr.fnafur.util.mixinAccessing.IUniverseRenderLayers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class FnafSplashOverlay extends SplashOverlay {
    private static final Identifier BG = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/bg.png");
    public static final Identifier LOADING = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/loading.png");
    private static final Identifier STARS = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/stars.png");
    private static final Identifier PIXELS = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/pixels.png");

    private final MinecraftClient client;
    private final ResourceReload reload;
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
    public FnafSplashOverlay(MinecraftClient client, ResourceReload monitor, Consumer<Optional<Throwable>> exceptionHandler, boolean reloading) {
        super(client, monitor, exceptionHandler, reloading);
        this.client = client;
        this.reload = monitor;
        this.exceptionHandler = exceptionHandler;
        this.reloading = reloading;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();

        long l = Util.getMeasuringTimeMs();
        if (this.reloading && this.reloadStartTime == -1L) {
            this.reloadStartTime = l;
        }

        float f = this.reloadCompleteTime > -1L ? (float)(l - this.reloadCompleteTime) / 1000.0F : -1.0F;
        float g = this.reloadStartTime > -1L ? (float)(l - this.reloadStartTime) / 500.0F : -1.0F;
        float h;

        if (f >= 1.0F) {
            if (this.client.currentScreen != null) {
                this.client.currentScreen.render(context, mouseX, mouseY, delta);
            }

            h = 1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F);
        } else if (this.reloading) {
            if (this.client.currentScreen != null && g < 1.0F) {
                this.client.currentScreen.render(context, mouseX, mouseY, delta);
            }

            h = MathHelper.clamp(g, 0.0F, 1.0F);
        } else {
            float m = 0;
            float n = 0;
            float o = 0;
            GlStateManager._clearColor(m, n, o, 1.0F);
            GlStateManager._clear(16384);
            h = 1.0F;
        }

        float u = this.reload.getProgress();
        this.progress = MathHelper.clamp(this.progress * 0.95F + u * 0.050000012F, 0.0F, 1.0F);

        context.fill(RenderLayer.getGuiOverlay(),0, 0, width, height, ColorHelper.getArgb((int) (h * 255), 0, 0, 0));
        context.drawTexture(identifier -> RenderLayer.getGuiTextured(BG), BG, 0, 0, 0, 0, width, height, 8, 8, 8, 8, ColorHelper.getWhite(h));

        float loadingWidth = (512f / 1080f) * height;
        float loadingHeight = (128f / 1080f) * height;

        float borderWidth = (4f / 1080f) * height;
        float progressHeight = (34f / 1080f) * height;

        context.drawTexture(identifier -> RenderLayer.getGuiTextured(LOADING), LOADING, width - (int) loadingWidth - (int) borderWidth, height - (int) loadingHeight - (int) borderWidth, 0, 0, (int) loadingWidth, (int) loadingHeight, 512, 128, 512, 512, ColorHelper.getWhite(h));

        float x2 = MathHelper.lerp(this.progress, width - (int) loadingWidth + (int) borderWidth, (int) width  - (int) borderWidth - (int) borderWidth);
        context.fill(width - (int) loadingWidth + (int) borderWidth, height - (int) progressHeight - (int) borderWidth - (int) borderWidth - (int) borderWidth - (int) borderWidth, (int) x2, height - (int) borderWidth - (int) borderWidth - (int) borderWidth - (int) borderWidth, ColorHelper.getWhite(h));

        float gearWidth = (82f / 1080f) * height;

        this.gearRotation += (delta/20f) * 45f;

        context.getMatrices().push();
        context.getMatrices().translate(borderWidth + borderWidth + borderWidth + borderWidth + borderWidth, height - gearWidth - borderWidth - borderWidth - borderWidth, 0);
        context.getMatrices().translate(gearWidth/2f, gearWidth/2f, 0);
        context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(gearRotation));
        context.getMatrices().translate(-gearWidth/2f, -gearWidth/2f, 0);
        context.drawTexture(identifier -> RenderLayer.getGuiTextured(LOADING), LOADING, 0, 0, 512-82, 512-82, (int) gearWidth, (int) gearWidth, 82, 82, 512, 512, ColorHelper.getWhite(h));
        context.getMatrices().pop();

        float hatHeight = (52f / 1080f) * height;
        float hatWidth = (120f / 1080f) * height;
        context.drawTexture(identifier -> RenderLayer.getGuiTextured(LOADING), LOADING, (int) borderWidth, height - (int) gearWidth - (int) hatHeight - (int) borderWidth, 0, 512-52, (int) hatWidth, (int) hatHeight, 120, 52, 512, 512, ColorHelper.getWhite(h));

        context.drawTexture(identifier -> RenderLayer.getGuiTextured(STARS), STARS, 0, 0, 0, 0, width, height, 8, 8, 8, 8, ColorHelper.getArgb((int) (h * ColorHelper.getAlpha(0x8dFFFFFF)), 255, 255, 255));
        context.drawTexture(identifier -> RenderLayer.getGuiTextured(PIXELS), PIXELS, 0, 0, 0, 0, width, height, 8, 8, 8, 8, ColorHelper.getArgb((int) (h * ColorHelper.getAlpha(0x0dFFFFFF)), 255, 255, 255));


        this.fadeTimer = Math.clamp(this.fadeTimer + delta/20f, 0, fadeTimerGoal);
        context.fill(0, 0, width, height, ColorHelper.getArgb((int) MathHelper.lerp(fadeTimer / fadeTimerGoal, fadeStart, fadeGoal), 0, 0, 0));


        if (f >= 2.0F) {
            this.client.setOverlay(null);
        }


        if (this.reloadCompleteTime == -1L && this.reload.isComplete() && (!this.reloading || g >= 2.0F)) {
            try {
                this.reload.throwException();
                this.exceptionHandler.accept(Optional.empty());
            } catch (Throwable var24) {
                this.exceptionHandler.accept(Optional.of(var24));
            }

            this.reloadCompleteTime = Util.getMeasuringTimeMs();
            if (this.client.currentScreen != null) {
                this.client.currentScreen.init(this.client, context.getScaledWindowWidth(), context.getScaledWindowHeight());
            }
        }
    }
    public static void init(TextureManager textureManager) {
        //textureManager.registerTexture(STARS, new LoadingTexture(STARS));
        //textureManager.registerTexture(PIXELS, new LoadingTexture(PIXELS));
        //textureManager.registerTexture(LOADING, new LoadingTexture(LOADING));
        //textureManager.registerTexture(BG, new LoadingTexture(BG));
    }

    @Environment(EnvType.CLIENT)
    static class LoadingTexture extends ReloadableTexture {
        private final Identifier TEXTURE;
        public LoadingTexture(Identifier texture) {
            super(texture);
            this.TEXTURE = texture;
        }

        @Override
        public TextureContents loadContents(ResourceManager resourceManager) throws IOException {
            List<Resource> list = resourceManager.getAllResources(TEXTURE);
            InputStream inputStream = list.getFirst().getInputStream();

            TextureContents var4;
            try {
                var4 = new TextureContents(NativeImage.read(inputStream), new TextureResourceMetadata(true, true));
            } catch (Throwable var7) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }

                throw var7;
            }

            if (inputStream != null) {
                inputStream.close();
            }

            return var4;
        }
    }
}
