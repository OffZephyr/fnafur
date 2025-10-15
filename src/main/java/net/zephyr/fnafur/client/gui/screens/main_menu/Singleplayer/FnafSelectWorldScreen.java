package net.zephyr.fnafur.client.gui.screens.main_menu.Singleplayer;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.storage.LevelSummary;
import net.zephyr.fnafur.client.gui.screens.main_menu.FnafTitleScreen;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class FnafSelectWorldScreen extends Screen {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final GeneratorOptions DEBUG_GENERATOR_OPTIONS = new GeneratorOptions("test1".hashCode(), true, false);
    protected final Screen parent;
    private ButtonWidget deleteButton;
    private ButtonWidget selectButton;
    private ButtonWidget editButton;
    private ButtonWidget recreateButton;
    protected TextFieldWidget searchBox;
    private FnafWorldListWidget levelList;

    public FnafSelectWorldScreen(Screen parent) {
        super(Text.translatable("selectWorld.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.searchBox = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 22, 200, 20, this.searchBox, Text.translatable("selectWorld.search"));
        this.searchBox.setChangedListener(search -> this.levelList.setSearch(search));
        this.addSelectableChild(this.searchBox);
        this.levelList = this.addDrawableChild(
                new FnafWorldListWidget(this, this.client, this.width, this.height - 112, 48, 36, this.searchBox.getText(), this.levelList)
        );
        this.selectButton = this.addDrawableChild(
                ButtonWidget.builder(LevelSummary.SELECT_WORLD_TEXT, button -> this.levelList.getSelectedAsOptional().ifPresent(FnafWorldListWidget.WorldEntry::play))
                        .dimensions(this.width / 2 - 154, this.height - 52, 150, 20)
                        .build()
        );
        this.addDrawableChild(
                ButtonWidget.builder(Text.translatable("selectWorld.create"), button -> CreateWorldScreen.show(this.client, this))
                        .dimensions(this.width / 2 + 4, this.height - 52, 150, 20)
                        .build()
        );
        this.editButton = this.addDrawableChild(
                ButtonWidget.builder(Text.translatable("selectWorld.edit"), button -> this.levelList.getSelectedAsOptional().ifPresent(FnafWorldListWidget.WorldEntry::edit))
                        .dimensions(this.width / 2 - 154, this.height - 28, 72, 20)
                        .build()
        );
        this.deleteButton = this.addDrawableChild(
                ButtonWidget.builder(
                                Text.translatable("selectWorld.delete"), button -> this.levelList.getSelectedAsOptional().ifPresent(FnafWorldListWidget.WorldEntry::deleteIfConfirmed)
                        )
                        .dimensions(this.width / 2 - 76, this.height - 28, 72, 20)
                        .build()
        );
        this.recreateButton = this.addDrawableChild(
                ButtonWidget.builder(
                                Text.translatable("selectWorld.recreate"), button -> this.levelList.getSelectedAsOptional().ifPresent(FnafWorldListWidget.WorldEntry::recreate)
                        )
                        .dimensions(this.width / 2 + 4, this.height - 28, 72, 20)
                        .build()
        );
        this.addDrawableChild(
                ButtonWidget.builder(ScreenTexts.BACK, button -> {
                    this.client.setScreen(this.parent);

                    if(this.parent instanceof FnafTitleScreen t){
                        t.moveBackgroundScroll(0.5f, 200, -75f);
                        t.fadeBackground(0.75f, 0.65f);
                    }
                }).dimensions(this.width / 2 + 82, this.height - 28, 72, 20).build()
        );
        this.worldSelected(null);
    }

    @Override
    protected void setInitialFocus() {
        this.setInitialFocus(this.searchBox);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        if(this.parent instanceof FnafTitleScreen t){
            t.renderBackgroundRender(context, mouseX, mouseY, deltaTicks);
        }
        super.render(context, mouseX, mouseY, deltaTicks);

        if(this.parent instanceof FnafTitleScreen t){
            t.renderForeground(context, mouseX, mouseY, deltaTicks);
        }
        this.searchBox.render(context, mouseX, mouseY, deltaTicks);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 8, Colors.WHITE);
    }

    public void worldSelected(@Nullable LevelSummary levelSummary) {
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

    @Override
    public void removed() {
        if (this.levelList != null) {
            this.levelList.children().forEach(FnafWorldListWidget.Entry::close);
        }
    }
}

