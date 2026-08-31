package net.zephyr.fnafur.client.gui.screens.main_menu;

import com.mojang.authlib.minecraft.BanDetails;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.CommonButtons;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
import net.minecraft.client.gui.screens.options.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.CreditsAndAttributionScreen;
import net.minecraft.client.gui.screens.options.LanguageSelectScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.components.SpriteIconButton;
import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.client.gui.screens.main_menu.Singleplayer.CreditsScreen;
import net.zephyr.fnafur.client.gui.screens.main_menu.Singleplayer.FnafSelectWorldScreen;
import net.zephyr.fnafur.init.SoundsInit;
import net.zephyr.fnafur.util.EasingMathUtil;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class FnafTitleScreen extends Screen {
    SoundInstance music;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Component NARRATOR_SCREEN_TITLE = Component.translatable("narrator.screen.title");
    private static final Component COPYRIGHT = Component.translatable("title.credits");
    private static final String DEMO_WORLD_NAME = "Demo_World";
    private static final Identifier PIXELS = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/pixels.png");
    private static final Identifier STARS = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/stars.png");
    private static final Identifier SCROLLING_TEXTURE = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/scrolling_texture.png");
    private static final Identifier SCROLLING_TEXTURE_WHITE = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/credits/scrolling_texture.png");
    private static final Identifier BG = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/bg.png");
    private static final Identifier OUTLINE = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/outline.png");
    private static final Identifier BUTTONS = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/buttons.png");
    private static final Identifier DISCLAIMER = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/demo_disclaimer.png");
    float bgFadeTimer = 0;
    float bgFadeTimerGoal = 0;
    float bgFadeStart = 0;
    float bgFadeGoal = 0;
    boolean isTransitioning = false;
    float transitionFadeTimer = 1;
    float transitionFadeTimerGoal = 1;
    float transitionFadeStart = 1;
    float transitionFadeGoal = 1;
    Screen transitionScreen = this;
    float bgDeltaAmount = 0;
    int tab = 0;
    int bgScroll = 0;
    float bgScrollMoveTimer = 1;
    float bgScrollMoveTimerGoal = 1;
    float bgScrollMoveStart = -1500;
    float bgScrollMoveGoal = -1500;
    float bgScrollRotStart = -90;
    float bgScrollRotGoal = -90;
    float staticIndex = 0;
    float renderGlitchTimer = 0;
    float  renderGlitchTimerGoal = 0;
    int renderGlitchIndex = 0;
    int renderIndex = 0;


    float scrollShapeRot = 0;
    Vec2 scrollShapePos = new Vec2(0, 0);

    private record triggerSoundZone(SoundEvent sound, int x, int y, int width, int height){

    }

    //context.fill(renderX + (int)(onHeight(1550)), renderY + (int)(onHeight(567)), renderX + (int)(onHeight(1550)) + (int)(onHeight(250)), renderY + (int)(onHeight(567) )+ (int)(onHeight(136)), 0xFFFFFFFF);

    boolean isVertical = false;
    int offsetX = 0, offsetY = 0;
    private static final Identifier[][] RENDERS = new Identifier[][]{
        {
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/renders/fnafone/main.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/renders/fnafone/freddyglitch1.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/renders/fnafone/freddyglitch2.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/renders/fnafone/bonnieglitch1.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/renders/fnafone/bonnieglitch2.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/renders/fnafone/chicaglitch1.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/renders/fnafone/chicaglitch2.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/renders/fnafone/foxyglitch1.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/renders/fnafone/foxyglitch2.png"),
        }
    };
    private static final Identifier[] STATIC = new Identifier[]{
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/static/0.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/static/1.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/static/2.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/static/3.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/static/4.png"),
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/static/5.png")
    };
    private Map<Identifier, triggerSoundZone> easterEggMap = Map.of(
            RENDERS[0][0], new triggerSoundZone(SoundsInit.HONK_MENU, 1550, 560, 187, 90)
    );
    private static final float field_49900 = 2000.0F;
    @Nullable
    private SplashRenderer splashText;
    private Button buttonResetDemo;
    @Nullable
    private RealmsNotificationsScreen realmsNotificationGui;
    private float backgroundAlpha = 1.0F;
    private boolean doBackgroundFade;
    private long backgroundFadeStart;
    private final LogoRenderer logoDrawer;
    //private MediaPlayerInstance media_test;

    public FnafTitleScreen() {
        this(false);
    }

    public FnafTitleScreen(boolean doBackgroundFade) {
        this(doBackgroundFade, null);
    }

    public FnafTitleScreen(boolean doBackgroundFade, @Nullable LogoRenderer logoDrawer) {
        super(NARRATOR_SCREEN_TITLE);
        this.doBackgroundFade = doBackgroundFade;
        this.logoDrawer = (LogoRenderer) Objects.requireNonNullElseGet(logoDrawer, () -> new LogoRenderer(false));


//        video_test1 = new VideoInstance(VideoInit.IGNITED_BONER, false);
//        video_test2 = new VideoInstance(VideoInit.CHINESE, false);
//        video_test = video_test1;
        //media_test = new MediaPlayerInstance(Identifier.of(FnafUniverseRebuilt.MOD_ID, "ignited_boner"), 632, 320, false, Vec3d.ZERO, 0);
    }

    private boolean isRealmsNotificationsGuiDisplayed() {
        return this.realmsNotificationGui != null;
    }

    @Override
    public void tick() {
        if(Minecraft.getInstance().getSituationalMusic() != null){
            Minecraft.getInstance().getMusicManager().stopPlaying();
        }
        Minecraft.getInstance().getMusicManager().stopPlaying();
        if (this.isRealmsNotificationsGuiDisplayed()) {
            this.realmsNotificationGui.tick();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void init() {
        if (this.splashText == null) {
            this.splashText = this.minecraft.getSplashManager().getSplash();
        }

        int i = this.font.width(COPYRIGHT);
        int j = this.width - i - 2;
        int k = 24;
        int l = this.height / 4 + 48;
        if (this.minecraft.isDemo()) {
            l = this.addDemoWidgets(l, 24);
        } else {
            l = this.addNormalWidgets(l, 24);
        }

        l = this.addDevelopmentWidgets(l, 24);
        SpriteIconButton textIconButtonWidget = this.addRenderableWidget(
                CommonButtons.language(
                        20, button -> this.minecraft.setScreen(new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager())), true
                )
        );
        int var10001 = this.width / 2 - 124;
        l += 36;
        textIconButtonWidget.setPosition(var10001, l);
        this.addRenderableWidget(
                Button.builder(Component.translatable("menu.options"), button -> this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options, false)))
                        .bounds(this.width / 2 - 100, l, 98, 20)
                        .build()
        );
        this.addRenderableWidget(
                Button.builder(Component.translatable("menu.quit"), button -> this.minecraft.stop()).bounds(this.width / 2 + 2, l, 98, 20).build()
        );
        SpriteIconButton textIconButtonWidget2 = this.addRenderableWidget(
                CommonButtons.accessibility(
                        20, button -> this.minecraft.setScreen(new AccessibilityOptionsScreen(this, this.minecraft.options)), true
                )
        );
        textIconButtonWidget2.setPosition(this.width / 2 + 104, l);
        this.addRenderableWidget(
                new PlainTextButton(j, this.height - 10, i, 10, COPYRIGHT, button -> this.minecraft.setScreen(new CreditsAndAttributionScreen(this)), this.font)
        );
        if (this.realmsNotificationGui == null) {
            this.realmsNotificationGui = new RealmsNotificationsScreen();
        }

        if (this.isRealmsNotificationsGuiDisplayed()) {
            this.realmsNotificationGui.init(this.width, this.height);
        }
        renderIndex = RandomSource.create().nextIntBetweenInclusive(0, RENDERS.length - 1);
        if(music == null) {
            music = new SimpleSoundInstance(SoundsInit.MAIN_MENU.location(), SoundSource.MASTER, 0.35f, 1, RandomSource.create(), true, 0, SoundInstance.Attenuation.NONE, 0, 0, 0, false);
            Minecraft.getInstance().getSoundManager().play(music);
        }
        fadeBackground(6, 0.35f);
    }

    private int addDevelopmentWidgets(int y, int spacingY) {
//        if (SharedConstants.isDevelopment) {
//            this.addDrawableChild(
//                    ButtonWidget.builder(Text.literal("Create Test World"), button -> CreateWorldScreen.showTestWorld(this.client, this))
//                            .dimensions(this.width / 2 - 100, y += spacingY, 200, 20)
//                            .build()
//            );
//        }

        return y;
    }

    private int addNormalWidgets(int y, int spacingY) {
        this.addRenderableWidget(
                Button.builder(Component.translatable("menu.singleplayer"), button -> this.minecraft.setScreen(new FnafSelectWorldScreen(this)))
                        .bounds(this.width / 2 - 100, y, 200, 20)
                        .build()
        );
        Component text = this.getMultiplayerDisabledText();
        boolean bl = text == null;
        Tooltip tooltip = text != null ? Tooltip.create(text) : null;
        int var6;
        this.addRenderableWidget(Button.builder(Component.translatable("menu.multiplayer"), button -> {
            Screen screen = (Screen)(this.minecraft.options.skipMultiplayerWarning ? new JoinMultiplayerScreen(this) : new SafetyScreen(this));
            this.minecraft.setScreen(screen);
        }).bounds(this.width / 2 - 100, var6 = y + spacingY, 200, 20).tooltip(tooltip).build()).active = bl;
        this.addRenderableWidget(
                Button.builder(Component.translatable("menu.online"), button -> this.minecraft.setScreen(new RealmsMainScreen(this)))
                        .bounds(this.width / 2 - 100, y = var6 + spacingY, 200, 20)
                        .tooltip(tooltip)
                        .build()
        )
                .active = bl;
        return y;
    }

    @Nullable
    private Component getMultiplayerDisabledText() {
        if (this.minecraft.allowsMultiplayer()) {
            return null;
        } else if (this.minecraft.isNameBanned()) {
            return Component.translatable("title.multiplayer.disabled.banned.name");
        } else {
            BanDetails banDetails = this.minecraft.multiplayerBan();
            if (banDetails != null) {
                return banDetails.expires() != null
                        ? Component.translatable("title.multiplayer.disabled.banned.temporary")
                        : Component.translatable("title.multiplayer.disabled.banned.permanent");
            } else {
                return Component.translatable("title.multiplayer.disabled");
            }
        }
    }

    private int addDemoWidgets(int y, int spacingY) {
        boolean bl = this.canReadDemoWorldData();
        this.addRenderableWidget(
                Button.builder(
                                Component.translatable("menu.playdemo"),
                                button -> {
                                    if (bl) {
                                        this.minecraft.createWorldOpenFlows().openWorld("Demo_World", () -> this.minecraft.setScreen(this));
                                    } else {
                                        this.minecraft
                                                .createWorldOpenFlows()
                                                .createFreshLevel("Demo_World", MinecraftServer.DEMO_SETTINGS, WorldOptions.DEMO_OPTIONS, WorldPresets::createNormalWorldDimensions, this);
                                    }
                                }
                        )
                        .bounds(this.width / 2 - 100, y, 200, 20)
                        .build()
        );
        int var4;
        this.buttonResetDemo = this.addRenderableWidget(
                Button.builder(
                                Component.translatable("menu.resetdemo"),
                                button -> {
                                    LevelStorageSource levelStorage = this.minecraft.getLevelSource();

                                    try (LevelStorageSource.LevelStorageAccess session = levelStorage.createAccess("Demo_World")) {
                                        if (session.hasWorldData()) {
                                            this.minecraft
                                                    .setScreen(
                                                            new ConfirmScreen(
                                                                    this::onDemoDeletionConfirmed,
                                                                    Component.translatable("selectWorld.deleteQuestion"),
                                                                    Component.translatable("selectWorld.deleteWarning", MinecraftServer.DEMO_SETTINGS.levelName()),
                                                                    Component.translatable("selectWorld.deleteButton"),
                                                                    CommonComponents.GUI_CANCEL
                                                            )
                                                    );
                                        }
                                    } catch (IOException var8) {
                                        SystemToast.onWorldAccessFailure(this.minecraft, "Demo_World");
                                        LOGGER.warn("Failed to access demo world", (Throwable)var8);
                                    }
                                }
                        )
                        .bounds(this.width / 2 - 100, var4 = y + spacingY, 200, 20)
                        .build()
        );
        this.buttonResetDemo.active = bl;
        return var4;
    }

    private boolean canReadDemoWorldData() {
        try {
            boolean var2;
            try (LevelStorageSource.LevelStorageAccess session = this.minecraft.getLevelSource().createAccess("Demo_World")) {
                var2 = session.hasWorldData();
            }

            return var2;
        } catch (IOException var6) {
            SystemToast.onWorldAccessFailure(this.minecraft, "Demo_World");
            LOGGER.warn("Failed to read demo world data", (Throwable)var6);
            return false;
        }
    }

    public void renderForeground(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta){
        renderForeground(context, mouseX, mouseY, delta, 1);

    }
    public void renderForeground(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta, float opacity){

        int deltaAmount2 = 10;
        float largeWidth2 = width + onWidth(bgDeltaAmount);
        float largeHeight2 = height + onHeight(bgDeltaAmount);

        float xMouseDelta = (float) mouseX / width;
        float yMouseDelta = (float) mouseY / height;
        float xDelta2 = deltaAmount2 * xMouseDelta;
        float yDelta2 = deltaAmount2 * yMouseDelta;

        staticIndex = (int) (staticIndex + 0.5f) == STATIC.length ? 0 : staticIndex + 0.5f;

        GoopyScreen.drawRecolorableTexture(context, STARS, (int) ((deltaAmount2 / 2) - xDelta2), (int) ((deltaAmount2 / 2) - yDelta2), 0, largeWidth2, largeHeight2, 0, 0, largeWidth2, largeHeight2, 1, 1, 1, 0.55f * opacity);
        GoopyScreen.drawRecolorableTexture(context, STATIC[(int) staticIndex], 0, 0, 0, width, height, 0, 0, width, height, 1, 1, 1, 0.15f * opacity);
        GoopyScreen.drawRecolorableTexture(context, PIXELS, 0, 0, 0, width, height, 0, 0, width, height, 1, 1, 1, 0.05f * opacity);

    }
    public void renderBackgroundextractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta){
        if (minecraft.getOverlay() == null && bgFadeTimer < bgFadeTimerGoal) {
            if(FnafUniverseRebuilt.MENU_REDUCE_MOVEMENTS) bgFadeTimer = bgFadeTimerGoal;
            else bgFadeTimer = Math.clamp(bgFadeTimer + delta / 20f, 0, bgFadeTimerGoal);
        }
        if (minecraft.getOverlay() == null && bgScrollMoveTimer < bgScrollMoveTimerGoal) {
            if(FnafUniverseRebuilt.MENU_REDUCE_MOVEMENTS) bgScrollMoveTimer = bgScrollMoveTimerGoal;
            else bgScrollMoveTimer = Math.clamp(bgScrollMoveTimer + delta / 20f, 0, bgScrollMoveTimerGoal);
        }
        if (minecraft.getOverlay() == null && transitionFadeTimer < transitionFadeTimerGoal) {
            if(FnafUniverseRebuilt.MENU_REDUCE_MOVEMENTS) transitionFadeTimer = transitionFadeTimerGoal;
            else transitionFadeTimer = Math.clamp(transitionFadeTimer + delta / 20f, 0, transitionFadeTimerGoal);
        }

        bgDeltaAmount = 20;
        float largeWidth = width + onWidth(bgDeltaAmount);
        float largeHeight = ((width + onWidth(bgDeltaAmount)) / 1920f) * 1080f;

        isVertical = largeHeight < height;
        if (isVertical) {
            largeWidth = ((height + onHeight(bgDeltaAmount)) / 1080f) * 1920f;
            largeHeight = height + onHeight(bgDeltaAmount);
        }

        float xMouseDelta = (float) mouseX / width;
        float yMouseDelta = (float) mouseY / height;

        float xDelta = bgDeltaAmount * xMouseDelta;
        float yDelta = bgDeltaAmount * yMouseDelta;

        GoopyScreen.drawRecolorableTexture(context, BG, 0, 0, 0, width, height, 0, 0, width, height, 1, 1, 1, 1);


        RandomSource random = RandomSource.create();
        if (renderGlitchTimer > renderGlitchTimerGoal - 0.20f && renderGlitchIndex == 0)
            renderGlitchIndex = random.nextIntBetweenInclusive(0, RENDERS[renderIndex].length - 1);
        renderGlitchTimer = Math.clamp(renderGlitchTimer + delta / 20f, 0, renderGlitchTimerGoal);
        if (renderGlitchTimer == renderGlitchTimerGoal) {
            renderGlitchTimer = 0;
            renderGlitchIndex = 0;
            renderGlitchTimerGoal = random.nextIntBetweenInclusive(50, 2000) / 100f;
        }

        Identifier render = RENDERS[renderIndex][renderGlitchIndex];

        int renderX = !isVertical ? (int) ((bgDeltaAmount / 2) - xDelta) : (int) ((bgDeltaAmount / 2) - xDelta - (largeWidth / 2f) + (width / 2f));
        int renderY = isVertical ? (int) ((bgDeltaAmount / 2) - yDelta) : (int) ((bgDeltaAmount / 2) - yDelta - (largeHeight / 2f) + (height / 2f));
        GoopyScreen.drawRecolorableTexture(context, render, renderX, renderY, 0, largeWidth, largeHeight, 0, 0, largeWidth, largeHeight, 1, 1, 1, Mth.lerp(bgFadeTimer / bgFadeTimerGoal, bgFadeStart, bgFadeGoal));


        offsetX = renderX;
        offsetY = renderY;

        float bgScrollMoveIndex = bgScrollMoveTimer / bgScrollMoveTimerGoal;

        bgScrollMoveIndex = (float) EasingMathUtil.easeInOutCubic(bgScrollMoveIndex);
        bgScroll++;
        float scroll_width = onHeight(1500);
        float scroll_height = onHeight(1500);
        float translate = onWidth(Mth.lerp(bgScrollMoveIndex, bgScrollMoveStart, bgScrollMoveGoal));
        context.pose().pushMatrix();
        //context.getMatrices().translate(-scroll_width / 2.25f, scroll_height / 1.5f);
        context.pose().translate(translate,  (height / 2f));
        context.pose().rotate(Mth.lerp(bgScrollMoveIndex, bgScrollRotStart, bgScrollRotGoal) * Mth.DEG_TO_RAD);
        context.pose().translate(-(scroll_width / 2f), -(scroll_height /2f));
        GoopyScreen.drawRecolorableTexture(context, SCROLLING_TEXTURE, 0, 0, 0, scroll_height, scroll_width, -bgScroll / 2f, 0, scroll_height, scroll_width, 1, 1, 1, 0.75f);
        context.pose().popMatrix();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {

        renderBackgroundextractRenderState(context, mouseX, mouseY, delta);

        float sprite_width = onHeight(2048);
        float sprite_height = onHeight(2048);

        if (tab == 1) {

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
                    y = 828;
                }
                if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(715), (int) onHeight(938), (int) onWidth(490), (int) onHeight(85))) {
                    y = 956;
                }
            }

            y = onHeight(y);

            float credits_width = onHeight(329);
            float credits_height = onHeight(57);

            GoopyScreen.drawRecolorableTexture(context, BUTTONS, (int) ((width / 2) - (sprite_width / 2)), 0, 0, sprite_width, sprite_height / 2f, 0, 0, sprite_width, sprite_height, 1, 1, 1, 1);
            GoopyScreen.drawRecolorableTexture(context, BUTTONS, (int) ((width / 2) - (star_width / 2)), (int) y, 0, star_width, star_height, sprite_width - star_width, sprite_height - star_height, sprite_width, sprite_height, 1, 1, 1, 1);

            GoopyScreen.drawRecolorableTexture(context, BUTTONS, (int) (width - (credits_width * 1.25f)), (int) (sprite_height/2 - credits_height), 0, credits_width, credits_height, sprite_width/2, sprite_height - credits_height, sprite_width, sprite_height, 1, 1, 1, 1);

            if(GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) (width - (credits_width * 1.25f)), (int) (sprite_height/2 - credits_height), (int) credits_width, (int) credits_height)){
                GoopyScreen.drawRecolorableTexture(context, BUTTONS, (int) (width - (credits_width * 1.25f)), (int) (sprite_height/2 - credits_height), 0, credits_width, credits_height, sprite_width/2, sprite_height - credits_height*2, sprite_width, sprite_height, 1, 1, 1, 1);
            }

            FontDescription spriteFont = new FontDescription.Resource(Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "metropolis"));
            Style style = Style.EMPTY.withFont(spriteFont);
            Component version_text = Component.literal(FnafUniverseRebuilt.MOD_VERSION).setStyle(style);
            context.text(font, version_text, width / 2 - font.width(version_text) / 2, height - font.lineHeight - 1, 0x88FFFFFF, false);

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

        if(isTransitioning){
            renderTransition(context, mouseX, mouseY, delta);
            updateTransition();
        }

        renderForeground(context, mouseX, mouseY, delta);


        if (FnafUniverseRebuilt.DEBUG) {
            if(easterEggMap.containsKey(RENDERS[renderIndex][renderGlitchIndex])) {
                triggerSoundZone zone = easterEggMap.get(RENDERS[renderIndex][renderGlitchIndex]);

                int x = (int) (offsetX + onWidth(zone.x));
                int y = (int) (offsetY + onWidth(zone.y));
                int width = (int) onWidth(zone.width);
                int height = (int) onWidth(zone.height);
                if (isVertical) {
                    x = (int) (offsetX + onHeight(zone.x));
                    y = (int) (offsetY + onHeight(zone.y));
                    width = (int) onHeight(zone.width);
                    height = (int) onHeight(zone.height);
                }

                context.fill(x, y, x + width, y + height, 0xFFFFFFFF);
            }
        }

//        video_test.update();
//        int videoWidth = (int) onHeight(video_test.getImageWidth());
//        int videoHeight = (int) onHeight(video_test.getImageHeight());
//        context.drawTexture(RenderPipelines.GUI_TEXTURED, video_test.getTextureId(), 0, 0, 0, 0, videoWidth, videoHeight, videoWidth, videoHeight);
    }

    public void moveBackgroundScroll(float time, float pos, float rot){
        bgScrollMoveTimer = 0;
        bgScrollMoveTimerGoal = time;
        bgScrollMoveStart = bgScrollMoveGoal;
        bgScrollMoveGoal = pos;
        bgScrollRotStart = bgScrollRotGoal;
        bgScrollRotGoal = rot;
    }
    public void fadeBackground(float time, float opacity){
        bgFadeTimer = 0;
        bgFadeTimerGoal = time;
        bgFadeStart = bgFadeGoal;
        bgFadeGoal = opacity;
    }
    public void renderTransition(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta){
        float index = 1 - Mth.lerp(transitionFadeTimer / transitionFadeTimerGoal, transitionFadeStart, transitionFadeGoal);

        int scale = (int) onHeight(1400);
        int y = -scale;

        int y2 = (int) Mth.lerp(EasingMathUtil.easeInOutQuad(index - 0.1f), y, y + (int) onHeight(1250));
        int y3 = (int) Mth.lerp(EasingMathUtil.easeInOutQuad(index - 0.05f), y, y + (int) onHeight(1250));
        int y4 = (int) Mth.lerp(EasingMathUtil.easeInOutQuad(index), y, y + (int) onHeight(1250));

        context.blit(RenderPipelines.GUI_TEXTURED, SCROLLING_TEXTURE_WHITE, 0, y4, 0, 0, width, scale, scale, scale, 0xFF000000);
        context.blit(RenderPipelines.GUI_TEXTURED, SCROLLING_TEXTURE_WHITE, 0, y3, 0, 0, width, scale, scale, scale, 0xFF432248);
        context.blit(RenderPipelines.GUI_TEXTURED, SCROLLING_TEXTURE_WHITE, 0, y2, 0, 0, width, scale, scale, scale, 0xFF000000);
        //context.drawTexture(RenderPipelines.GUI_TEXTURED, SCROLLING_TEXTURE_WHITE, 0, y1, 0, 0, width, scale, scale, scale, 0xFFFFFFFF);
    }
    public void updateTransition(){
        float index = Mth.lerp(transitionFadeTimer / transitionFadeTimerGoal, transitionFadeStart, transitionFadeGoal);

        Minecraft.getInstance().getSoundManager().soundEngine.instanceToChannel.get(music).execute(
                source -> source.setVolume(index)
        );
        if(index <= 0){

            Minecraft.getInstance().getSoundManager().stop(music);
            music = null;
            Minecraft.getInstance().setScreen(transitionScreen);

            isTransitioning = false;
            transitionFadeTimer = 1;
            transitionFadeTimerGoal = 1;
            transitionFadeStart = 1;
            transitionFadeGoal = 1;
        }
    }
    public void startTransition(float time, float volume, Screen screen){
        transitionFadeTimer = 0;
        transitionFadeTimerGoal = time;
        transitionFadeStart = transitionFadeGoal;
        transitionFadeGoal = volume;
        transitionScreen = screen;

        isTransitioning = true;
        fadeBackground(time, 0);
    }

    float onWidth(float pixel){
        return (pixel / 1920f) * width;
    }
    float onHeight(float pixel){
        return (pixel / 1080f) * height;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
    }

    @Override
    protected void extractPanorama(GuiGraphicsExtractor graphics, float a) {
        super.extractPanorama(graphics, a);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
//        if(click.hasShift()){
//            video_test.getMediaPlayer().stop();
//            video_test = video_test1;
//            video_test.restartVideo();
//        }
//        else if(click.hasCtrl()){
//            video_test.getMediaPlayer().stop();
//            video_test = video_test2;
//            video_test.restartVideo();
//        }
//        else{
//            video_test.restartVideo();
//        }

        if(isTransitioning) return false;
        double mouseX = click.x();
        double mouseY = click.y();
        float credits_width = onHeight(329);
        float credits_height = onHeight(57);
        float sprite_width = onHeight(2048);
        float sprite_height = onHeight(2048);

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
                SimpleSoundInstance sound = new SimpleSoundInstance(zone.sound().location(), SoundSource.MASTER, 1, 1, RandomSource.create(), false, 0, SoundInstance.Attenuation.NONE, 0, 0, 0, false);
                Minecraft.getInstance().getSoundManager().play(sound);
            }
        }

        if(tab == -1){
            if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(636), (int) onHeight(789), (int) onWidth(660), (int) onHeight(98))) {
                tab = 0;
                SoundInstance instance = new SimpleSoundInstance(SoundsInit.CAM_SWITCH, SoundSource.MASTER, 1, 1.15f, RandomSource.create(), BlockPos.ZERO);
                Minecraft.getInstance().getSoundManager().play(instance);
            }
        }
        else if(tab == 0){

            if(GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(715), (int) onHeight(635), (int) onWidth(490), (int) onHeight(137))){
                tab = 1;
                fadeBackground(0.75f, 0.65f);
                moveBackgroundScroll(0.5f, 200, -75f);

                SoundInstance instance = new SimpleSoundInstance(SoundsInit.CAM_SWITCH, SoundSource.MASTER, 1, 1.15f, RandomSource.create(), BlockPos.ZERO);
                Minecraft.getInstance().getSoundManager().play(instance);
            }
            if(GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(715), (int) onHeight(812), (int) onWidth(490), (int) onHeight(85))){
                SoundInstance instance = new SimpleSoundInstance(SoundsInit.CAM_SWITCH, SoundSource.MASTER, 1, 1f, RandomSource.create(), BlockPos.ZERO);
                Minecraft.getInstance().getSoundManager().play(instance);

                this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options, false));
            }
            if(GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(715), (int) onHeight(938), (int) onWidth(490), (int) onHeight(85))){
                this.minecraft.stop();
            }
            if(GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) (width - (credits_width * 1.25f)), (int) (sprite_height/2 - credits_height), (int) credits_width, (int) credits_height)){
                SoundInstance instance = new SimpleSoundInstance(SoundsInit.CAM_SWITCH, SoundSource.MASTER, 1, 1.25f, RandomSource.create(), BlockPos.ZERO);
                Minecraft.getInstance().getSoundManager().play(instance);

                //Minecraft.getInstance().getSoundManager().stop(music);
                startTransition(2, 0, new CreditsScreen(Component.literal("CREDITS"), this));

                //music = null;
                //this.client.setScreen(new CreditsScreen(Text.literal("CREDITS"), this));
            }
        }
        else if(tab == 1){
            if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(120), (int) onHeight(536), (int) onWidth(500), (int) onHeight(64))) {
                // TYCOON
                SoundInstance instance = new SimpleSoundInstance(SoundsInit.OFFICE_DOOR_ERROR.location(), SoundSource.MASTER, 0.5f, 1, RandomSource.create(), false, 0, SoundInstance.Attenuation.NONE, 0, 0, 0, false);
                Minecraft.getInstance().getSoundManager().play(instance);
            }
            if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(120), (int) onHeight(636), (int) onWidth(500), (int) onHeight(64))) {
                // SINGLEPLAYER
                SoundInstance instance = new SimpleSoundInstance(SoundsInit.CAM_SWITCH, SoundSource.MASTER, 1, 1.2f, RandomSource.create(), BlockPos.ZERO);
                Minecraft.getInstance().getSoundManager().play(instance);
                this.minecraft.setScreen(new FnafSelectWorldScreen(this));

                moveBackgroundScroll(0.5f, 1650, -105f);
                fadeBackground(0.75f, 0.35f);
            }
            if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(120), (int) onHeight(736), (int) onWidth(500), (int) onHeight(64))) {
                SoundInstance instance = new SimpleSoundInstance(SoundsInit.CAM_SWITCH, SoundSource.MASTER, 1, 1.2f, RandomSource.create(), BlockPos.ZERO);
                Minecraft.getInstance().getSoundManager().play(instance);
                this.minecraft.setScreen(new JoinMultiplayerScreen(this));
            }
            if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(120), (int) onHeight(836), (int) onWidth(500), (int) onHeight(64))) {
                SoundInstance instance = new SimpleSoundInstance(SoundsInit.CAM_SWITCH, SoundSource.MASTER, 1, 1.2f, RandomSource.create(), BlockPos.ZERO);
                Minecraft.getInstance().getSoundManager().play(instance);
                this.minecraft.setScreen(new RealmsMainScreen(this));
            }
            if (GoopyScreen.isOnButton((double) mouseX, (double) mouseY, (int) onWidth(120), (int) onHeight(936), (int) onWidth(500), (int) onHeight(64))) {
                SoundInstance instance = new SimpleSoundInstance(SoundsInit.CAM_SWITCH, SoundSource.MASTER, 1, 1.1f, RandomSource.create(), BlockPos.ZERO);
                Minecraft.getInstance().getSoundManager().play(instance);

                moveBackgroundScroll(0.5f, -1500, -90f);
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
    public void onClose() {
        Minecraft.getInstance().getSoundManager().stop(music);
        music = null;
        super.onClose();
    }

    @Override
    public void added() {
        super.added();
        if (this.realmsNotificationGui != null) {
            this.realmsNotificationGui.added();
        }
    }

    private void onDemoDeletionConfirmed(boolean delete) {
        if (delete) {
            try (LevelStorageSource.LevelStorageAccess session = this.minecraft.getLevelSource().createAccess("Demo_World")) {
                session.deleteLevel();
            } catch (IOException var7) {
                SystemToast.onWorldDeleteFailure(this.minecraft, "Demo_World");
                LOGGER.warn("Failed to delete demo world", (Throwable)var7);
            }
        }

        this.minecraft.setScreen(this);
    }
}
