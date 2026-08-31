package net.zephyr.fnafur.client.gui.screens.main_menu.Singleplayer;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.minecraft.util.FileUtil;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraft.world.level.gamerules.GameRules;
import net.zephyr.fnafur.client.gui.screens.main_menu.FnafTitleScreen;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class FnafSelectWorldScreen extends Screen {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final WorldOptions DEBUG_GENERATOR_OPTIONS = new WorldOptions((long)"test1".hashCode(), true, false);
    protected final FnafTitleScreen parent;
    private final HeaderAndFooterLayout layout;
    @Nullable
    private Button deleteButton;
    @Nullable
    private Button selectButton;
    @Nullable
    private Button editButton;
    @Nullable
    private Button recreateButton;
    @Nullable
    protected EditBox searchBox;
    @Nullable
    private FnafWorldListWidget levelList;

    public FnafSelectWorldScreen(FnafTitleScreen parent) {
        super(Component.translatable("selectWorld.title"));
        Objects.requireNonNull(Minecraft.getInstance().font);
        this.layout = new HeaderAndFooterLayout(this, 8 + 9 + 8 + 20 + 4, 60);
        this.parent = parent;
    }

    @Override
    protected void init() {
        LinearLayout directionalLayoutWidget = (LinearLayout)this.layout.addToHeader(LinearLayout.vertical().spacing(4));
        directionalLayoutWidget.defaultCellSetting().alignHorizontallyCenter();
        directionalLayoutWidget.addChild(new StringWidget(this.title, this.font));
        LinearLayout directionalLayoutWidget2 = (LinearLayout)directionalLayoutWidget.addChild(LinearLayout.horizontal().spacing(4));
        if (SharedConstants.DEBUG_WORLD_RECREATE) {
            directionalLayoutWidget2.addChild(this.createDebugRecreateButton());
        }

        this.searchBox = (EditBox)directionalLayoutWidget2.addChild(new EditBox(this.font, this.width / 2 - 100, 22, 200, 20, this.searchBox, Component.translatable("selectWorld.search")));
        this.searchBox.setResponder((search) -> {
            if (this.levelList != null) {
                this.levelList.setSearch(search);
            }

        });
        Consumer<FnafWorldListWidget.WorldEntry> consumer = FnafWorldListWidget.WorldEntry::play;
        this.levelList = (FnafWorldListWidget)this.layout.addToContents((new FnafWorldListWidget.Builder(this.minecraft, this)).width(this.width).height(this.layout.getContentHeight()).search(this.searchBox.getValue()).predecessor(this.levelList).selectionCallback(this::worldSelected).confirmationCallback(consumer).toWidget());
        this.addButtons(consumer, this.levelList);
        this.layout.visitWidgets((child) -> {
            AbstractWidget var10000 = (AbstractWidget)this.addRenderableWidget(child);
        });
        this.repositionElements();
        this.worldSelected((LevelSummary)null);
    }

    private void addButtons(Consumer<FnafWorldListWidget.WorldEntry> playAction, FnafWorldListWidget levelList) {
        GridLayout gridWidget = (GridLayout)this.layout.addToFooter((new GridLayout()).columnSpacing(8).rowSpacing(4));
        gridWidget.defaultCellSetting().alignHorizontallyCenter();
        GridLayout.RowHelper adder = gridWidget.createRowHelper(4);
        this.selectButton = (Button)adder.addChild(Button.builder(LevelSummary.PLAY_WORLD, (button) -> levelList.getSelectedAsOptional().ifPresent(playAction)).build(), 2);
        adder.addChild(Button.builder(Component.translatable("selectWorld.create"), (button) -> {
            Minecraft var10000 = this.minecraft;
            Objects.requireNonNull(levelList);
            CreateWorldScreen.openFresh(var10000, levelList::refresh);
        }).build(), 2);
        this.editButton = (Button)adder.addChild(Button.builder(Component.translatable("selectWorld.edit"), (button) -> levelList.getSelectedAsOptional().ifPresent(FnafWorldListWidget.WorldEntry::edit)).width(71).build());
        this.deleteButton = (Button)adder.addChild(Button.builder(Component.translatable("selectWorld.delete"), (button) -> levelList.getSelectedAsOptional().ifPresent(FnafWorldListWidget.WorldEntry::deleteIfConfirmed)).width(71).build());
        this.recreateButton = (Button)adder.addChild(Button.builder(Component.translatable("selectWorld.recreate"), (button) -> levelList.getSelectedAsOptional().ifPresent(FnafWorldListWidget.WorldEntry::recreate)).width(71).build());
        adder.addChild(Button.builder(CommonComponents.GUI_BACK, (button) -> {
            parent.moveBackgroundScroll(0.5f, 200, -75f);
            parent.fadeBackground(0.75f, 0.65f);
            this.minecraft.setScreen(this.parent);
        }).width(71).build());
    }

    private Button createDebugRecreateButton() {
        return Button.builder(Component.literal("DEBUG recreate"), (button) -> {
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

                LevelSettings levelInfo = new LevelSettings("DEBUG world", GameType.SPECTATOR, LevelSettings.DifficultySettings.DEFAULT, true, WorldDataConfiguration.DEFAULT);
                String string2 = FileUtil.findAvailableName(this.minecraft.getLevelSource().getBaseDir(), "DEBUG world", "");
                this.minecraft.createWorldOpenFlows().createFreshLevel(string2, levelInfo, DEBUG_GENERATOR_OPTIONS, WorldPresets::createNormalWorldDimensions, this);
            } catch (IOException iOException) {
                LOGGER.error("Failed to recreate the debug world", iOException);
            }

        }).width(72).build();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks) {
        parent.width = this.width;
        parent.height = this.height;
        parent.renderBackgroundextractRenderState(context, mouseX, mouseY, deltaTicks);
        super.extractRenderState(context, mouseX, mouseY, deltaTicks);
        parent.renderForeground(context, mouseX, mouseY, deltaTicks);
    }

    @Override
    public void tick() {
        parent.tick();
        super.tick();
    }

    protected void repositionElements() {
        if (this.levelList != null) {
            this.levelList.updateSize(this.width, this.layout);
        }

        this.layout.arrangeElements();
    }

    protected void setInitialFocus() {
        if (this.searchBox != null) {
            this.setInitialFocus(this.searchBox);
        }

    }

    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    public void worldSelected(@Nullable LevelSummary levelSummary) {
        if (this.selectButton != null && this.editButton != null && this.recreateButton != null && this.deleteButton != null) {
            if (levelSummary == null) {
                this.selectButton.setMessage(LevelSummary.PLAY_WORLD);
                this.selectButton.active = false;
                this.editButton.active = false;
                this.recreateButton.active = false;
                this.deleteButton.active = false;
            } else {
                this.selectButton.setMessage(levelSummary.primaryActionMessage());
                this.selectButton.active = levelSummary.primaryActionActive();
                this.editButton.active = levelSummary.canEdit();
                this.recreateButton.active = levelSummary.canRecreate();
                this.deleteButton.active = levelSummary.canDelete();
            }

        }
    }

    public void removed() {
        if (this.levelList != null) {
            this.levelList.children().forEach(FnafWorldListWidget.Entry::close);
        }

    }
}

