package net.zephyr.fnafur.client.gui.screens.main_menu;

import com.mojang.authlib.minecraft.BanDetails;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LogoDrawer;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerWarningScreen;
import net.minecraft.client.gui.screen.option.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screen.option.CreditsAndAttributionScreen;
import net.minecraft.client.gui.screen.option.LanguageOptionsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.client.realms.gui.screen.RealmsNotificationsScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.WorldPresets;
import net.minecraft.world.level.storage.LevelStorage;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.init.SoundsInit;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class FnafTitleScreen extends Screen {
    SoundInstance music;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Text NARRATOR_SCREEN_TITLE = Text.translatable("narrator.screen.title");
    private static final Text COPYRIGHT = Text.translatable("title.credits");
    private static final String DEMO_WORLD_NAME = "Demo_World";
    private static final Identifier PIXELS = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/pixels.png");
    private static final Identifier STARS = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/stars.png");
    private static final Identifier SCROLLING_TEXTURE = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/scrolling_texture.png");
    private static final Identifier BG = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/bg.png");
    private static final Identifier OUTLINE = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/outline.png");
    private static final Identifier BUTTONS = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/buttons.png");
    private static final Identifier DISCLAIMER = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/demo_disclaimer.png");
    float bgFadeTimer = 0;
    float bgFadeTimerGoal = 0;
    float bgFadeStart = 0;
    float bgFadeGoal = 0;
    int tab = 0;
    int bgScroll = 0;
    float staticIndex = 0;
    float renderGlitchTimer = 0;
    float  renderGlitchTimerGoal = 0;
    int renderGlitchIndex = 0;
    int renderIndex = 0;

    private record triggerSoundZone(SoundEvent sound, int x, int y, int width, int height){

    }

    //context.fill(renderX + (int)(onHeight(1550)), renderY + (int)(onHeight(567)), renderX + (int)(onHeight(1550)) + (int)(onHeight(250)), renderY + (int)(onHeight(567) )+ (int)(onHeight(136)), 0xFFFFFFFF);

    int offsetX = 0, offsetY = 0;
    private static final Identifier[][] RENDERS = new Identifier[][]{
        {
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/renders/fnafone/main.png"),
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/renders/fnafone/freddyglitch1.png"),
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/renders/fnafone/freddyglitch2.png"),
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/renders/fnafone/bonnieglitch1.png"),
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/renders/fnafone/bonnieglitch2.png"),
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/renders/fnafone/chicaglitch1.png"),
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/renders/fnafone/chicaglitch2.png"),
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/renders/fnafone/foxyglitch1.png"),
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/renders/fnafone/foxyglitch2.png"),
        }
    };
    private static final Identifier[] STATIC = new Identifier[]{
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/static/0.png"),
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/static/1.png"),
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/static/2.png"),
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/static/3.png"),
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/static/4.png"),
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/static/5.png")
    };
    private Map<Identifier, triggerSoundZone> easterEggMap = Map.of(
            RENDERS[0][0], new triggerSoundZone(SoundsInit.HONK, 1550, 560, 187, 90)
    );
    private static final float field_49900 = 2000.0F;
    @Nullable
    private SplashTextRenderer splashText;
    private ButtonWidget buttonResetDemo;
    @Nullable
    private RealmsNotificationsScreen realmsNotificationGui;
    private float backgroundAlpha = 1.0F;
    private boolean doBackgroundFade;
    private long backgroundFadeStart;
    private final LogoDrawer logoDrawer;

    public FnafTitleScreen() {
        this(false);
    }

    public FnafTitleScreen(boolean doBackgroundFade) {
        this(doBackgroundFade, null);
    }

    public FnafTitleScreen(boolean doBackgroundFade, @Nullable LogoDrawer logoDrawer) {
        super(NARRATOR_SCREEN_TITLE);
        this.doBackgroundFade = doBackgroundFade;
        this.logoDrawer = (LogoDrawer) Objects.requireNonNullElseGet(logoDrawer, () -> new LogoDrawer(false));
    }

    private boolean isRealmsNotificationsGuiDisplayed() {
        return this.realmsNotificationGui != null;
    }

    @Override
    public void tick() {
        if(MinecraftClient.getInstance().getMusicInstance() != null){
            MinecraftClient.getInstance().getMusicTracker().stop();
        }
        MinecraftClient.getInstance().getMusicTracker().stop();
        if (this.isRealmsNotificationsGuiDisplayed()) {
            this.realmsNotificationGui.tick();
        }
    }

    public static void registerTextures(TextureManager textureManager) {
        textureManager.registerTexture(LogoDrawer.LOGO_TEXTURE);
        textureManager.registerTexture(LogoDrawer.EDITION_TEXTURE);
        textureManager.registerTexture(RotatingCubeMapRenderer.OVERLAY_TEXTURE);
        PANORAMA_RENDERER.registerTextures(textureManager);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void init() {
        if (this.splashText == null) {
            this.splashText = this.client.getSplashTextLoader().get();
        }

        int i = this.textRenderer.getWidth(COPYRIGHT);
        int j = this.width - i - 2;
        int k = 24;
        int l = this.height / 4 + 48;
        if (this.client.isDemo()) {
            l = this.addDemoWidgets(l, 24);
        } else {
            l = this.addNormalWidgets(l, 24);
        }

        l = this.addDevelopmentWidgets(l, 24);
        TextIconButtonWidget textIconButtonWidget = this.addDrawableChild(
                AccessibilityOnboardingButtons.createLanguageButton(
                        20, button -> this.client.setScreen(new LanguageOptionsScreen(this, this.client.options, this.client.getLanguageManager())), true
                )
        );
        int var10001 = this.width / 2 - 124;
        l += 36;
        textIconButtonWidget.setPosition(var10001, l);
        this.addDrawableChild(
                ButtonWidget.builder(Text.translatable("menu.options"), button -> this.client.setScreen(new OptionsScreen(this, this.client.options)))
                        .dimensions(this.width / 2 - 100, l, 98, 20)
                        .build()
        );
        this.addDrawableChild(
                ButtonWidget.builder(Text.translatable("menu.quit"), button -> this.client.scheduleStop()).dimensions(this.width / 2 + 2, l, 98, 20).build()
        );
        TextIconButtonWidget textIconButtonWidget2 = this.addDrawableChild(
                AccessibilityOnboardingButtons.createAccessibilityButton(
                        20, button -> this.client.setScreen(new AccessibilityOptionsScreen(this, this.client.options)), true
                )
        );
        textIconButtonWidget2.setPosition(this.width / 2 + 104, l);
        this.addDrawableChild(
                new PressableTextWidget(j, this.height - 10, i, 10, COPYRIGHT, button -> this.client.setScreen(new CreditsAndAttributionScreen(this)), this.textRenderer)
        );
        if (this.realmsNotificationGui == null) {
            this.realmsNotificationGui = new RealmsNotificationsScreen();
        }

        if (this.isRealmsNotificationsGuiDisplayed()) {
            this.realmsNotificationGui.init(this.client, this.width, this.height);
        }
        renderIndex = Random.create().nextBetween(0, RENDERS.length - 1);
        if(music == null) {
            music = new PositionedSoundInstance(SoundsInit.MAIN_MENU.id(), SoundCategory.MASTER, 0.35f, 1, Random.create(), true, 0, SoundInstance.AttenuationType.NONE, 0, 0, 0, false);
            MinecraftClient.getInstance().getSoundManager().play(music);
        }
        fadeBackground(6, 0.35f);
    }

    private int addDevelopmentWidgets(int y, int spacingY) {
        if (SharedConstants.isDevelopment) {
            this.addDrawableChild(
                    ButtonWidget.builder(Text.literal("Create Test World"), button -> CreateWorldScreen.showTestWorld(this.client, this))
                            .dimensions(this.width / 2 - 100, y += spacingY, 200, 20)
                            .build()
            );
        }

        return y;
    }

    private int addNormalWidgets(int y, int spacingY) {
        this.addDrawableChild(
                ButtonWidget.builder(Text.translatable("menu.singleplayer"), button -> this.client.setScreen(new SelectWorldScreen(this)))
                        .dimensions(this.width / 2 - 100, y, 200, 20)
                        .build()
        );
        Text text = this.getMultiplayerDisabledText();
        boolean bl = text == null;
        Tooltip tooltip = text != null ? Tooltip.of(text) : null;
        int var6;
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("menu.multiplayer"), button -> {
            Screen screen = (Screen)(this.client.options.skipMultiplayerWarning ? new MultiplayerScreen(this) : new MultiplayerWarningScreen(this));
            this.client.setScreen(screen);
        }).dimensions(this.width / 2 - 100, var6 = y + spacingY, 200, 20).tooltip(tooltip).build()).active = bl;
        this.addDrawableChild(
                ButtonWidget.builder(Text.translatable("menu.online"), button -> this.client.setScreen(new RealmsMainScreen(this)))
                        .dimensions(this.width / 2 - 100, y = var6 + spacingY, 200, 20)
                        .tooltip(tooltip)
                        .build()
        )
                .active = bl;
        return y;
    }

    @Nullable
    private Text getMultiplayerDisabledText() {
        if (this.client.isMultiplayerEnabled()) {
            return null;
        } else if (this.client.isUsernameBanned()) {
            return Text.translatable("title.multiplayer.disabled.banned.name");
        } else {
            BanDetails banDetails = this.client.getMultiplayerBanDetails();
            if (banDetails != null) {
                return banDetails.expires() != null
                        ? Text.translatable("title.multiplayer.disabled.banned.temporary")
                        : Text.translatable("title.multiplayer.disabled.banned.permanent");
            } else {
                return Text.translatable("title.multiplayer.disabled");
            }
        }
    }

    private int addDemoWidgets(int y, int spacingY) {
        boolean bl = this.canReadDemoWorldData();
        this.addDrawableChild(
                ButtonWidget.builder(
                                Text.translatable("menu.playdemo"),
                                button -> {
                                    if (bl) {
                                        this.client.createIntegratedServerLoader().start("Demo_World", () -> this.client.setScreen(this));
                                    } else {
                                        this.client
                                                .createIntegratedServerLoader()
                                                .createAndStart("Demo_World", MinecraftServer.DEMO_LEVEL_INFO, GeneratorOptions.DEMO_OPTIONS, WorldPresets::createDemoOptions, this);
                                    }
                                }
                        )
                        .dimensions(this.width / 2 - 100, y, 200, 20)
                        .build()
        );
        int var4;
        this.buttonResetDemo = this.addDrawableChild(
                ButtonWidget.builder(
                                Text.translatable("menu.resetdemo"),
                                button -> {
                                    LevelStorage levelStorage = this.client.getLevelStorage();

                                    try (LevelStorage.Session session = levelStorage.createSessionWithoutSymlinkCheck("Demo_World")) {
                                        if (session.levelDatExists()) {
                                            this.client
                                                    .setScreen(
                                                            new ConfirmScreen(
                                                                    this::onDemoDeletionConfirmed,
                                                                    Text.translatable("selectWorld.deleteQuestion"),
                                                                    Text.translatable("selectWorld.deleteWarning", MinecraftServer.DEMO_LEVEL_INFO.getLevelName()),
                                                                    Text.translatable("selectWorld.deleteButton"),
                                                                    ScreenTexts.CANCEL
                                                            )
                                                    );
                                        }
                                    } catch (IOException var8) {
                                        SystemToast.addWorldAccessFailureToast(this.client, "Demo_World");
                                        LOGGER.warn("Failed to access demo world", (Throwable)var8);
                                    }
                                }
                        )
                        .dimensions(this.width / 2 - 100, var4 = y + spacingY, 200, 20)
                        .build()
        );
        this.buttonResetDemo.active = bl;
        return var4;
    }

    private boolean canReadDemoWorldData() {
        try {
            boolean var2;
            try (LevelStorage.Session session = this.client.getLevelStorage().createSessionWithoutSymlinkCheck("Demo_World")) {
                var2 = session.levelDatExists();
            }

            return var2;
        } catch (IOException var6) {
            SystemToast.addWorldAccessFailureToast(this.client, "Demo_World");
            LOGGER.warn("Failed to read demo world data", (Throwable)var6);
            return false;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        /*if (this.backgroundFadeStart == 0L && this.doBackgroundFade) {
            this.backgroundFadeStart = Util.getMeasuringTimeMs();
        }

        float f = 1.0F;
        if (this.doBackgroundFade) {
            float g = (float)(Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 2000.0F;
            if (g > 1.0F) {
                this.doBackgroundFade = false;
                this.backgroundAlpha = 1.0F;
            } else {
                g = MathHelper.clamp(g, 0.0F, 1.0F);
                f = MathHelper.clampedMap(g, 0.5F, 1.0F, 0.0F, 1.0F);
                this.backgroundAlpha = MathHelper.clampedMap(g, 0.0F, 0.5F, 0.0F, 1.0F);
            }

            this.setWidgetAlpha(f);
        }

        this.renderPanoramaBackground(context, delta);
        int i = MathHelper.ceil(f * 255.0F) << 24;
        if ((i & -67108864) != 0) {
            super.render(context, mouseX, mouseY, delta);
            this.logoDrawer.draw(context, this.width, f);
            if (this.splashText != null && !this.client.options.getHideSplashTexts().getValue()) {
                this.splashText.render(context, this.width, this.textRenderer, i);
            }

            String string = "Minecraft " + SharedConstants.getGameVersion().getName();
            if (this.client.isDemo()) {
                string = string + " Demo";
            } else {
                string = string + ("release".equalsIgnoreCase(this.client.getVersionType()) ? "" : "/" + this.client.getVersionType());
            }

            if (MinecraftClient.getModStatus().isModded()) {
                string = string + I18n.translate("menu.modded");
            }

            context.drawTextWithShadow(this.textRenderer, string, 2, this.height - 10, 16777215 | i);
            if (this.isRealmsNotificationsGuiDisplayed() && f >= 1.0F) {
                this.realmsNotificationGui.render(context, mouseX, mouseY, delta);
            }
        }
*/
        if (client.getOverlay() == null && bgFadeTimer < bgFadeTimerGoal) {
            bgFadeTimer = Math.clamp(bgFadeTimer + delta / 20f, 0, bgFadeTimerGoal);
        }

        int deltaAmount = 20;
        float largeWidth = width + onWidth(deltaAmount);
        float largeHeight = ((width + onWidth(deltaAmount)) / 1920f) * 1080f;

        boolean bl = largeHeight < height;
        if (bl) {
            largeWidth = ((height + onHeight(deltaAmount)) / 1080f) * 1920f;
            largeHeight = height + onHeight(deltaAmount);
        }

        float xMouseDelta = (float) mouseX / width;
        float yMouseDelta = (float) mouseY / height;

        float xDelta = deltaAmount * xMouseDelta;
        float yDelta = deltaAmount * yMouseDelta;

        GoopyScreen.drawRecolorableTexture(context, BG, 0, 0, 0, width, height, 0, 0, width, height, 1, 1, 1, 1);

        Random random = Random.create();
        if (renderGlitchTimer > renderGlitchTimerGoal - 0.20f && renderGlitchIndex == 0)
            renderGlitchIndex = random.nextBetween(0, RENDERS[renderIndex].length - 1);
        renderGlitchTimer = Math.clamp(renderGlitchTimer + delta / 20f, 0, renderGlitchTimerGoal);
        if (renderGlitchTimer == renderGlitchTimerGoal) {
            renderGlitchTimer = 0;
            renderGlitchIndex = 0;
            renderGlitchTimerGoal = random.nextBetween(50, 2000) / 100f;
        }

        Identifier render = RENDERS[renderIndex][renderGlitchIndex];

        int renderX = !bl ? (int) ((deltaAmount / 2) - xDelta) : (int) ((deltaAmount / 2) - xDelta - (largeWidth / 2f) + (width / 2f));
        int renderY = bl ? (int) ((deltaAmount / 2) - yDelta) : (int) ((deltaAmount / 2) - yDelta - (largeHeight / 2f) + (height / 2f));
        GoopyScreen.drawRecolorableTexture(context, render, renderX, renderY, 0, largeWidth, largeHeight, 0, 0, largeWidth, largeHeight, 1, 1, 1, MathHelper.lerp(bgFadeTimer / bgFadeTimerGoal, bgFadeStart, bgFadeGoal));

        offsetX = renderX;
        offsetY = renderY;

        float sprite_width = onHeight(2048);
        float sprite_height = onHeight(2048);

        staticIndex = (int) (staticIndex + 0.5f) == STATIC.length ? 0 : staticIndex + 0.5f;

        if (tab == 1) {
            bgScroll++;
            float scroll_width = onHeight(1500);
            float scroll_height = onHeight(1500);
            context.getMatrices().push();
            context.getMatrices().translate(-scroll_width / 2.25f, scroll_height / 1.5f, 0);
            context.getMatrices().multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(75));
            GoopyScreen.drawRecolorableTexture(context, SCROLLING_TEXTURE, 0, 0, 0, scroll_height, scroll_width, -bgScroll / 2f, 0, scroll_height, scroll_width, 1, 1, 1, 0.75f);
            context.getMatrices().pop();


            GoopyScreen.drawRecolorableTexture(context, BUTTONS, (int) onHeight(32), (int) onHeight(55), 0, sprite_width / 2f, sprite_height / 2f, 0, sprite_height / 2f, sprite_width, sprite_height, 1, 1, 1, 1);

            float arrow_width = onHeight(66);
            float arrow_height = onHeight(33);
            float star_height = onHeight(50);
            float y = -200;
            if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onHeight(120), (int) onHeight(536), (int) onHeight(500), (int) onHeight(64))) {
                y = 546;
            }
            if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onHeight(120), (int) onHeight(636), (int) onHeight(500), (int) onHeight(64))) {
                y = 646;
            }
            if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onHeight(120), (int) onHeight(736), (int) onHeight(500), (int) onHeight(64))) {
                y = 746;
            }
            if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onHeight(120), (int) onHeight(836), (int) onHeight(500), (int) onHeight(64))) {
                y = 846;
            }
            if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onHeight(120), (int) onHeight(936), (int) onHeight(500), (int) onHeight(64))) {
                y = 946;
            }

            y = onHeight(y);

            GoopyScreen.drawRecolorableTexture(context, BUTTONS, (int) onHeight(75), (int) y, 0, arrow_width, arrow_height, sprite_width - arrow_width, sprite_height - star_height - arrow_height, sprite_width, sprite_height, 1, 1, 1, 1);

        }

        GoopyScreen.drawRecolorableTexture(context, OUTLINE, 0, 0, 0, width, height, 0, 0, width, height, 1, 1, 1, 1);

        if (tab == 0 || tab == -1) {
            float star_width = onHeight(448);
            float star_height = onHeight(50);
            float y = -200; // 680 825 952

            if (tab == 0) {
                if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(715), (int) onHeight(635), (int) onWidth(490), (int) onHeight(137))) {
                    y = 680;
                }
                if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(715), (int) onHeight(812), (int) onWidth(490), (int) onHeight(85))) {
                    y = 825;
                }
                if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(715), (int) onHeight(938), (int) onWidth(490), (int) onHeight(85))) {
                    y = 952;
                }
            }

            y = onHeight(y);

            GoopyScreen.drawRecolorableTexture(context, BUTTONS, (int) ((width / 2) - (sprite_width / 2)), 0, 0, sprite_width, sprite_height / 2f, 0, 0, sprite_width, sprite_height, 1, 1, 1, 1);
            GoopyScreen.drawRecolorableTexture(context, BUTTONS, (int) ((width / 2) - (star_width / 2)), (int) y, 0, star_width, star_height, sprite_width - star_width, sprite_height - star_height, sprite_width, sprite_height, 1, 1, 1, 1);

            context.drawText(textRenderer, Text.literal(FnafUniverseRebuilt.MOD_VERSION), width / 2 - textRenderer.getWidth(FnafUniverseRebuilt.MOD_VERSION) / 2, height - textRenderer.fontHeight - 1, 0x88FFFFFF, false);

            if (tab == -1) {

                if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(636), (int) onHeight(789), (int) onWidth(660), (int) onHeight(98))) {
                    y = 818;
                }
                y = onHeight(y);
                GoopyScreen.drawRecolorableTexture(context, BG, 0, 0, 0, width, height, 0, 0, width, height, 1, 1, 1, 0.5f);

                float disclaimer_width = onHeight(1024);
                float disclaimer_height = onHeight(1024);
                float popup_height = onHeight(840);
                float star2_height = onHeight(42);

                GoopyScreen.drawRecolorableTexture(context, DISCLAIMER, (int) ((width / 2) - (disclaimer_width / 2)), (int) ((height / 2) - ((popup_height) / 2)), 0, disclaimer_width, popup_height, 0, 0, disclaimer_width, disclaimer_height, 1, 1, 1, 1);
                GoopyScreen.drawRecolorableTexture(context, DISCLAIMER, (int) ((width / 2) - (disclaimer_width / 2)), (int) y, 0, disclaimer_width, star2_height, 0, popup_height, disclaimer_width, disclaimer_height, 1, 1, 1, 1);
            }
        }


        int deltaAmount2 = 10;
        float largeWidth2 = width + onWidth(deltaAmount);
        float largeHeight2 = height + onHeight(deltaAmount);

        float xDelta2 = deltaAmount2 * xMouseDelta;
        float yDelta2 = deltaAmount2 * yMouseDelta;

        GoopyScreen.drawRecolorableTexture(context, STARS, (int) ((deltaAmount2 / 2) - xDelta2), (int) ((deltaAmount2 / 2) - yDelta2), 0, largeWidth2, largeHeight2, 0, 0, largeWidth2, largeHeight2, 1, 1, 1, 0.55f);
        GoopyScreen.drawRecolorableTexture(context, STATIC[(int) staticIndex], 0, 0, 0, width, height, 0, 0, width, height, 1, 1, 1, 0.15f);
        GoopyScreen.drawRecolorableTexture(context, PIXELS, 0, 0, 0, width, height, 0, 0, width, height, 1, 1, 1, 0.05f);


        // SET TO TRUE TO VISUALIZE EASTER EGG HIT-BOXES
        if (false) {
            if(easterEggMap.containsKey(RENDERS[renderIndex][renderGlitchIndex])) {
                triggerSoundZone zone = easterEggMap.get(RENDERS[renderIndex][renderGlitchIndex]);

                int x = (int) (offsetX + onWidth(zone.x));
                int y = (int) (offsetY + onWidth(zone.y));
                int width = (int) onWidth(zone.width);
                int height = (int) onWidth(zone.height);
                if (bl) {
                    x = (int) (offsetX + onHeight(zone.x));
                    y = (int) (offsetY + onHeight(zone.y));
                    width = (int) onHeight(zone.width);
                    height = (int) onHeight(zone.height);
                }

                context.fill(x, y, x + width, y + height, 0xFFFFFFFF);
            }
        }

    }

    void fadeBackground(float time, float opacity){
        bgFadeTimer = 0;
        bgFadeTimerGoal = time;
        bgFadeStart = bgFadeGoal;
        bgFadeGoal = opacity;
    }

    float onWidth(float pixel){
        return (pixel / 1920f) * width;
    }
    float onHeight(float pixel){
        return (pixel / 1080f) * height;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    @Override
    protected void renderPanoramaBackground(DrawContext context, float delta) {
        ROTATING_PANORAMA_RENDERER.render(context, this.width, this.height, this.backgroundAlpha, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        int deltaAmount = 20;
        float largeHeight = ((width + onWidth(deltaAmount)) / 1920f) * 1080f;

        boolean bl = largeHeight < height;

        if(easterEggMap.containsKey(RENDERS[renderIndex][renderGlitchIndex])) {
            triggerSoundZone zone = easterEggMap.get(RENDERS[renderIndex][renderGlitchIndex]);

            int x = (int) (offsetX + onWidth(zone.x));
            int y = (int) (offsetY + onWidth(zone.y));
            int width = (int) onWidth(zone.width);
            int height = (int) onWidth(zone.height);
            if(bl){
                x = (int) (offsetX + onHeight(zone.x));
                y = (int) (offsetY + onHeight(zone.y));
                width = (int) onHeight(zone.width);
                height = (int) onHeight(zone.height);
            }


            if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, x, y, width, height)) {
                PositionedSoundInstance sound = new PositionedSoundInstance(zone.sound().id(), SoundCategory.MASTER, 1, 1, Random.create(), false, 0, SoundInstance.AttenuationType.NONE, 0, 0, 0, false);
                MinecraftClient.getInstance().getSoundManager().play(sound);
            }
        }

        if(tab == -1){
            if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(636), (int) onHeight(789), (int) onWidth(660), (int) onHeight(98))) {
                tab = 0;
                SoundInstance instance = new PositionedSoundInstance(SoundsInit.CAM_SWITCH, SoundCategory.MASTER, 1, 1.15f, Random.create(), BlockPos.ORIGIN);
                MinecraftClient.getInstance().getSoundManager().play(instance);
            }
        }
        else if(tab == 0){

            if(GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(715), (int) onHeight(635), (int) onWidth(490), (int) onHeight(137))){
                tab = 1;
                fadeBackground(0.75f, 0.65f);

                SoundInstance instance = new PositionedSoundInstance(SoundsInit.CAM_SWITCH, SoundCategory.MASTER, 1, 1.15f, Random.create(), BlockPos.ORIGIN);
                MinecraftClient.getInstance().getSoundManager().play(instance);
            }
            if(GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(715), (int) onHeight(812), (int) onWidth(490), (int) onHeight(85))){
                SoundInstance instance = new PositionedSoundInstance(SoundsInit.CAM_SWITCH, SoundCategory.MASTER, 1, 1f, Random.create(), BlockPos.ORIGIN);
                MinecraftClient.getInstance().getSoundManager().play(instance);

                this.client.setScreen(new OptionsScreen(this, this.client.options));
            }
            if(GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(715), (int) onHeight(938), (int) onWidth(490), (int) onHeight(85))){
                this.client.scheduleStop();
            }
        }
        else if(tab == 1){
            if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(120), (int) onHeight(536), (int) onWidth(500), (int) onHeight(64))) {
                // TYCOON
                SoundInstance instance = new PositionedSoundInstance(SoundsInit.OFFICE_DOOR_ERROR.id(), SoundCategory.MASTER, 0.5f, 1, Random.create(), false, 0, SoundInstance.AttenuationType.NONE, 0, 0, 0, false);
                MinecraftClient.getInstance().getSoundManager().play(instance);
            }
            if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(120), (int) onHeight(636), (int) onWidth(500), (int) onHeight(64))) {
                SoundInstance instance = new PositionedSoundInstance(SoundsInit.CAM_SWITCH, SoundCategory.MASTER, 1, 1.2f, Random.create(), BlockPos.ORIGIN);
                MinecraftClient.getInstance().getSoundManager().play(instance);
                this.client.setScreen(new SelectWorldScreen(this));
            }
            if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(120), (int) onHeight(736), (int) onWidth(500), (int) onHeight(64))) {
                SoundInstance instance = new PositionedSoundInstance(SoundsInit.CAM_SWITCH, SoundCategory.MASTER, 1, 1.2f, Random.create(), BlockPos.ORIGIN);
                MinecraftClient.getInstance().getSoundManager().play(instance);
                this.client.setScreen(new MultiplayerScreen(this));
            }
            if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(120), (int) onHeight(836), (int) onWidth(500), (int) onHeight(64))) {
                SoundInstance instance = new PositionedSoundInstance(SoundsInit.CAM_SWITCH, SoundCategory.MASTER, 1, 1.2f, Random.create(), BlockPos.ORIGIN);
                MinecraftClient.getInstance().getSoundManager().play(instance);
                this.client.setScreen(new RealmsMainScreen(this));
            }
            if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(120), (int) onHeight(936), (int) onWidth(500), (int) onHeight(64))) {
                SoundInstance instance = new PositionedSoundInstance(SoundsInit.CAM_SWITCH, SoundCategory.MASTER, 1, 1.1f, Random.create(), BlockPos.ORIGIN);
                MinecraftClient.getInstance().getSoundManager().play(instance);

                tab = 0;
                fadeBackground(0.75f, 0.35f);
            }
        }
        return true;
    }

    @Override
    public void removed() {
        if (this.realmsNotificationGui != null) {
            this.realmsNotificationGui.removed();
        }
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().getSoundManager().stop(music);
        music = null;
        super.close();
    }

    @Override
    public void onDisplayed() {
        super.onDisplayed();
        if (this.realmsNotificationGui != null) {
            this.realmsNotificationGui.onDisplayed();
        }
    }

    private void onDemoDeletionConfirmed(boolean delete) {
        if (delete) {
            try (LevelStorage.Session session = this.client.getLevelStorage().createSessionWithoutSymlinkCheck("Demo_World")) {
                session.deleteSessionLock();
            } catch (IOException var7) {
                SystemToast.addWorldDeleteFailureToast(this.client, "Demo_World");
                LOGGER.warn("Failed to delete demo world", (Throwable)var7);
            }
        }

        this.client.setScreen(this);
    }
}
