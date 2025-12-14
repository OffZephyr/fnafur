package net.zephyr.fnafur.client.gui.screens.main_menu.Singleplayer;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.resource.DataConfiguration;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.path.PathUtil;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.WorldPresets;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.storage.LevelSummary;
import net.zephyr.fnafur.client.gui.screens.main_menu.FnafTitleScreen;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class FnafSelectWorldScreen extends Screen {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final GeneratorOptions DEBUG_GENERATOR_OPTIONS = new GeneratorOptions((long)"test1".hashCode(), true, false);
    protected final FnafTitleScreen parent;
    private final ThreePartsLayoutWidget layout;
    @Nullable
    private ButtonWidget deleteButton;
    @Nullable
    private ButtonWidget selectButton;
    @Nullable
    private ButtonWidget editButton;
    @Nullable
    private ButtonWidget recreateButton;
    @Nullable
    protected TextFieldWidget searchBox;
    @Nullable
    private FnafWorldListWidget levelList;

    public FnafSelectWorldScreen(FnafTitleScreen parent) {
        super(Text.translatable("selectWorld.title"));
        Objects.requireNonNull(MinecraftClient.getInstance().textRenderer);
        this.layout = new ThreePartsLayoutWidget(this, 8 + 9 + 8 + 20 + 4, 60);
        this.parent = parent;
    }

    @Override
    protected void init() {
        DirectionalLayoutWidget directionalLayoutWidget = (DirectionalLayoutWidget)this.layout.addHeader(DirectionalLayoutWidget.vertical().spacing(4));
        directionalLayoutWidget.getMainPositioner().alignHorizontalCenter();
        directionalLayoutWidget.add(new TextWidget(this.title, this.textRenderer));
        DirectionalLayoutWidget directionalLayoutWidget2 = (DirectionalLayoutWidget)directionalLayoutWidget.add(DirectionalLayoutWidget.horizontal().spacing(4));
        if (SharedConstants.WORLD_RECREATE) {
            directionalLayoutWidget2.add(this.createDebugRecreateButton());
        }

        this.searchBox = (TextFieldWidget)directionalLayoutWidget2.add(new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 22, 200, 20, this.searchBox, Text.translatable("selectWorld.search")));
        this.searchBox.setChangedListener((search) -> {
            if (this.levelList != null) {
                this.levelList.setSearch(search);
            }

        });
        Consumer<FnafWorldListWidget.WorldEntry> consumer = FnafWorldListWidget.WorldEntry::play;
        this.levelList = (FnafWorldListWidget)this.layout.addBody((new FnafWorldListWidget.Builder(this.client, this)).width(this.width).height(this.layout.getContentHeight()).search(this.searchBox.getText()).predecessor(this.levelList).selectionCallback(this::worldSelected).confirmationCallback(consumer).toWidget());
        this.addButtons(consumer, this.levelList);
        this.layout.forEachChild((child) -> {
            ClickableWidget var10000 = (ClickableWidget)this.addDrawableChild(child);
        });
        this.refreshWidgetPositions();
        this.worldSelected((LevelSummary)null);
    }

    private void addButtons(Consumer<FnafWorldListWidget.WorldEntry> playAction, FnafWorldListWidget levelList) {
        GridWidget gridWidget = (GridWidget)this.layout.addFooter((new GridWidget()).setColumnSpacing(8).setRowSpacing(4));
        gridWidget.getMainPositioner().alignHorizontalCenter();
        GridWidget.Adder adder = gridWidget.createAdder(4);
        this.selectButton = (ButtonWidget)adder.add(ButtonWidget.builder(LevelSummary.SELECT_WORLD_TEXT, (button) -> levelList.getSelectedAsOptional().ifPresent(playAction)).build(), 2);
        adder.add(ButtonWidget.builder(Text.translatable("selectWorld.create"), (button) -> {
            MinecraftClient var10000 = this.client;
            Objects.requireNonNull(levelList);
            CreateWorldScreen.show(var10000, levelList::refresh);
        }).build(), 2);
        this.editButton = (ButtonWidget)adder.add(ButtonWidget.builder(Text.translatable("selectWorld.edit"), (button) -> levelList.getSelectedAsOptional().ifPresent(FnafWorldListWidget.WorldEntry::edit)).width(71).build());
        this.deleteButton = (ButtonWidget)adder.add(ButtonWidget.builder(Text.translatable("selectWorld.delete"), (button) -> levelList.getSelectedAsOptional().ifPresent(FnafWorldListWidget.WorldEntry::deleteIfConfirmed)).width(71).build());
        this.recreateButton = (ButtonWidget)adder.add(ButtonWidget.builder(Text.translatable("selectWorld.recreate"), (button) -> levelList.getSelectedAsOptional().ifPresent(FnafWorldListWidget.WorldEntry::recreate)).width(71).build());
        adder.add(ButtonWidget.builder(ScreenTexts.BACK, (button) -> {
            parent.moveBackgroundScroll(0.5f, 200, -75f);
            parent.fadeBackground(0.75f, 0.65f);
            this.client.setScreen(this.parent);
        }).width(71).build());
    }

    private ButtonWidget createDebugRecreateButton() {
        return ButtonWidget.builder(Text.literal("DEBUG recreate"), (button) -> {
            try {
                String string = "DEBUG world";
                if (this.levelList != null && !this.levelList.children().isEmpty()) {
                    FnafWorldListWidget.Entry entry = (FnafWorldListWidget.Entry)this.levelList.children().getFirst();
                    if (entry instanceof FnafWorldListWidget.WorldEntry) {
                        FnafWorldListWidget.WorldEntry worldEntry = (FnafWorldListWidget.WorldEntry)entry;
                        if (worldEntry.getLevelDisplayName().equals("DEBUG world")) {
                            worldEntry.delete();
                        }
                    }
                }

                LevelInfo levelInfo = new LevelInfo("DEBUG world", GameMode.SPECTATOR, false, Difficulty.NORMAL, true, new GameRules(DataConfiguration.SAFE_MODE.enabledFeatures()), DataConfiguration.SAFE_MODE);
                String string2 = PathUtil.getNextUniqueName(this.client.getLevelStorage().getSavesDirectory(), "DEBUG world", "");
                this.client.createIntegratedServerLoader().createAndStart(string2, levelInfo, DEBUG_GENERATOR_OPTIONS, WorldPresets::createDemoOptions, this);
            } catch (IOException iOException) {
                LOGGER.error("Failed to recreate the debug world", iOException);
            }

        }).width(72).build();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        parent.width = this.width;
        parent.height = this.height;
        parent.renderBackgroundRender(context, mouseX, mouseY, deltaTicks);
        super.render(context, mouseX, mouseY, deltaTicks);
        parent.renderForeground(context, mouseX, mouseY, deltaTicks);
    }

    @Override
    public void tick() {
        parent.tick();
        super.tick();
    }

    protected void refreshWidgetPositions() {
        if (this.levelList != null) {
            this.levelList.position(this.width, this.layout);
        }

        this.layout.refreshPositions();
    }

    protected void setInitialFocus() {
        if (this.searchBox != null) {
            this.setInitialFocus(this.searchBox);
        }

    }

    public void close() {
        this.client.setScreen(this.parent);
    }

    public void worldSelected(@Nullable LevelSummary levelSummary) {
        if (this.selectButton != null && this.editButton != null && this.recreateButton != null && this.deleteButton != null) {
            if (levelSummary == null) {
                this.selectButton.setMessage(LevelSummary.SELECT_WORLD_TEXT);
                this.selectButton.active = false;
                this.editButton.active = false;
                this.recreateButton.active = false;
                this.deleteButton.active = false;
            } else {
                this.selectButton.setMessage(levelSummary.getSelectWorldText());
                this.selectButton.active = levelSummary.isSelectable();
                this.editButton.active = levelSummary.isEditable();
                this.recreateButton.active = levelSummary.isRecreatable();
                this.deleteButton.active = levelSummary.isDeletable();
            }

        }
    }

    public void removed() {
        if (this.levelList != null) {
            this.levelList.children().forEach(FnafWorldListWidget.Entry::close);
        }

    }
}

