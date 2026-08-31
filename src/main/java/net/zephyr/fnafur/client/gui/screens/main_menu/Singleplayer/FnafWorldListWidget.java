//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.zephyr.fnafur.client.gui.screens.main_menu.Singleplayer;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.worldselection.*;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.ErrorScreen;
import net.minecraft.client.gui.screens.LoadingDotsText;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.AlertScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.NoticeWithLinkScreen;
import net.minecraft.client.gui.screens.FaviconTexture;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.nbt.ReportedNbtException;
import net.minecraft.nbt.NbtException;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.CrashReport;
import net.minecraft.world.level.validation.ForbiddenSymlinkInfo;
import net.minecraft.world.level.validation.ContentValidationException;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelStorageException;
import net.minecraft.world.level.storage.LevelSummary;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class FnafWorldListWidget extends ObjectSelectionList<FnafWorldListWidget.Entry> {
    public static final DateTimeFormatter DATE_FORMAT;
    static final Identifier ERROR_HIGHLIGHTED_TEXTURE;
    static final Identifier ERROR_TEXTURE;
    static final Identifier MARKED_JOIN_HIGHLIGHTED_TEXTURE;
    static final Identifier MARKED_JOIN_TEXTURE;
    static final Identifier WARNING_HIGHLIGHTED_TEXTURE;
    static final Identifier WARNING_TEXTURE;
    static final Identifier JOIN_HIGHLIGHTED_TEXTURE;
    static final Identifier JOIN_TEXTURE;
    static final Logger LOGGER;
    static final Component FROM_NEWER_VERSION_FIRST_LINE;
    static final Component FROM_NEWER_VERSION_SECOND_LINE;
    static final Component SNAPSHOT_FIRST_LINE;
    static final Component SNAPSHOT_SECOND_LINE;
    static final Component LOCKED_TEXT;
    static final Component CONVERSION_TOOLTIP;
    static final Component INCOMPATIBLE_TOOLTIP;
    static final Component EXPERIMENTAL_TEXT;
    private final Screen parent;
    private CompletableFuture<List<LevelSummary>> levelsFuture;
    @Nullable
    private List<LevelSummary> levels;
    private final LoadingEntry loadingEntry;
    final WorldListType worldListType;
    private String search;
    private boolean failedToGetLevels;
    @Nullable
    private final Consumer<LevelSummary> selectionCallback;
    @Nullable
    final Consumer<WorldEntry> confirmationCallback;

    FnafWorldListWidget(Screen parent, Minecraft client, int width, int height, String search, @Nullable FnafWorldListWidget predecessor, @Nullable Consumer<LevelSummary> selectionCallback, @Nullable Consumer<WorldEntry> confirmationCallback, WorldListType worldListType) {
        super(client, width, height, 0, 36);
        this.parent = parent;
        this.loadingEntry = new LoadingEntry(client);
        this.search = search;
        this.selectionCallback = selectionCallback;
        this.confirmationCallback = confirmationCallback;
        this.worldListType = worldListType;
        if (predecessor != null) {
            this.levelsFuture = predecessor.levelsFuture;
        } else {
            this.levelsFuture = this.loadLevels();
        }

        this.addEntry(this.loadingEntry);
        this.show(this.tryGet());
    }

    protected void clearEntries() {
        this.children().forEach(Entry::close);
        super.clearEntries();
    }

    @Nullable
    private List<LevelSummary> tryGet() {
        try {
            List<LevelSummary> list = (List)this.levelsFuture.getNow(null);
            if (this.worldListType == FnafWorldListWidget.WorldListType.UPLOAD_WORLD) {
                if (list == null || this.failedToGetLevels) {
                    return null;
                }

                this.failedToGetLevels = true;
                list = list.stream().filter(LevelSummary::canUpload).toList();
            }

            return list;
        } catch (CancellationException | CompletionException var2) {
            return null;
        }
    }

    public void load() {
        this.levelsFuture = this.loadLevels();
    }

    public void renderWidget(GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks) {
        List<LevelSummary> list = this.tryGet();
        if (list != this.levels) {
            this.show(list);
        }

        super.renderWidget(context, mouseX, mouseY, deltaTicks);
    }

    private void show(@Nullable List<LevelSummary> summaries) {
        if (summaries != null) {
            if (summaries.isEmpty()) {
                switch (this.worldListType.ordinal()) {
                    case 0:
                        CreateWorldScreen.openFresh(this.minecraft, () -> this.minecraft.setScreen((Screen)null));
                        break;
                    case 1:
                        this.clearEntries();
                        this.addEntry(new EmptyListEntry(Component.translatable("mco.upload.select.world.none"), this.parent.getFont()));
                }
            } else {
                this.showSummaries(this.search, summaries);
                this.levels = summaries;
            }

        }
    }

    public void setSearch(String search) {
        if (this.levels != null && !search.equals(this.search)) {
            this.showSummaries(search, this.levels);
        }

        this.search = search;
    }

    private CompletableFuture<List<LevelSummary>> loadLevels() {
        LevelStorageSource.LevelCandidates levelList;
        try {
            levelList = this.minecraft.getLevelSource().findLevelCandidates();
        } catch (LevelStorageException levelStorageException) {
            LOGGER.error("Couldn't load level list", levelStorageException);
            this.showUnableToLoadScreen(levelStorageException.getMessageComponent());
            return CompletableFuture.completedFuture(List.of());
        }

        return this.minecraft.getLevelSource().loadLevelSummaries(levelList).exceptionally((throwable) -> {
            this.minecraft.delayCrash(CrashReport.forThrowable(throwable, "Couldn't load level list"));
            return List.of();
        });
    }

    private void showSummaries(String search, List<LevelSummary> summaries) {
        List<Entry> list = new ArrayList();
        Optional<WorldEntry> optional = this.getSelectedAsOptional();
        WorldEntry worldEntry = null;

        for(LevelSummary levelSummary : summaries.stream().filter((summary) -> this.shouldShow(search.toLowerCase(Locale.ROOT), summary)).toList()) {
            WorldEntry worldEntry2 = new WorldEntry(this, levelSummary);
            if (optional.isPresent() && ((WorldEntry)optional.get()).getLevel().getLevelId().equals(worldEntry2.getLevel().getLevelId())) {
                worldEntry = worldEntry2;
            }

            list.add(worldEntry2);
        }

        this.removeEntries(this.children().stream().filter((child) -> !list.contains(child)).toList());
        list.forEach((entry) -> {
            if (!this.children().contains(entry)) {
                this.addEntry(entry);
            }

        });
        this.setSelected((Entry)worldEntry);
        this.narrateScreenIfNarrationEnabled();
    }

    private boolean shouldShow(String search, LevelSummary summary) {
        return summary.getLevelName().toLowerCase(Locale.ROOT).contains(search) || summary.getLevelId().toLowerCase(Locale.ROOT).contains(search);
    }

    private void narrateScreenIfNarrationEnabled() {
        this.refreshScrollAmount();
        this.parent.triggerImmediateNarration(true);
    }

    private void showUnableToLoadScreen(Component message) {
        this.minecraft.setScreen(new ErrorScreen(Component.translatable("selectWorld.unable_to_load"), message));
    }

    public int getRowWidth() {
        return 270;
    }

    public void setSelected(@Nullable Entry entry) {
        super.setSelected(entry);
        if (this.selectionCallback != null) {
            Consumer var10000 = this.selectionCallback;
            LevelSummary var10001;
            if (entry instanceof WorldEntry) {
                WorldEntry worldEntry = (WorldEntry)entry;
                var10001 = worldEntry.level;
            } else {
                var10001 = null;
            }

            var10000.accept(var10001);
        }

    }

    public Optional<WorldEntry> getSelectedAsOptional() {
        Entry entry = (Entry)this.getSelected();
        if (entry instanceof WorldEntry worldEntry) {
            return Optional.of(worldEntry);
        } else {
            return Optional.empty();
        }
    }

    public void refresh() {
        this.load();
        this.minecraft.setScreen(this.parent);
    }

    public Screen getParent() {
        return this.parent;
    }

    public void updateWidgetNarration(NarrationElementOutput builder) {
        if (this.children().contains(this.loadingEntry)) {
            this.loadingEntry.updateNarration(builder);
        } else {
            super.updateWidgetNarration(builder);
        }
    }

    static {
        DATE_FORMAT = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault());
        ERROR_HIGHLIGHTED_TEXTURE = Identifier.withDefaultNamespace("world_list/error_highlighted");
        ERROR_TEXTURE = Identifier.withDefaultNamespace("world_list/error");
        MARKED_JOIN_HIGHLIGHTED_TEXTURE = Identifier.withDefaultNamespace("world_list/marked_join_highlighted");
        MARKED_JOIN_TEXTURE = Identifier.withDefaultNamespace("world_list/marked_join");
        WARNING_HIGHLIGHTED_TEXTURE = Identifier.withDefaultNamespace("world_list/warning_highlighted");
        WARNING_TEXTURE = Identifier.withDefaultNamespace("world_list/warning");
        JOIN_HIGHLIGHTED_TEXTURE = Identifier.withDefaultNamespace("world_list/join_highlighted");
        JOIN_TEXTURE = Identifier.withDefaultNamespace("world_list/join");
        LOGGER = LogUtils.getLogger();
        FROM_NEWER_VERSION_FIRST_LINE = Component.translatable("selectWorld.tooltip.fromNewerVersion1").withStyle(ChatFormatting.RED);
        FROM_NEWER_VERSION_SECOND_LINE = Component.translatable("selectWorld.tooltip.fromNewerVersion2").withStyle(ChatFormatting.RED);
        SNAPSHOT_FIRST_LINE = Component.translatable("selectWorld.tooltip.snapshot1").withStyle(ChatFormatting.GOLD);
        SNAPSHOT_SECOND_LINE = Component.translatable("selectWorld.tooltip.snapshot2").withStyle(ChatFormatting.GOLD);
        LOCKED_TEXT = Component.translatable("selectWorld.locked").withStyle(ChatFormatting.RED);
        CONVERSION_TOOLTIP = Component.translatable("selectWorld.conversion.tooltip").withStyle(ChatFormatting.RED);
        INCOMPATIBLE_TOOLTIP = Component.translatable("selectWorld.incompatible.tooltip").withStyle(ChatFormatting.RED);
        EXPERIMENTAL_TEXT = Component.translatable("selectWorld.experimental");
    }

    @Environment(EnvType.CLIENT)
    public abstract static class Entry extends ObjectSelectionList.Entry<FnafWorldListWidget.Entry> implements AutoCloseable {
        public void close() {
        }

        @Nullable
        public LevelSummary getLevel() {
            return null;
        }
    }

    @Environment(EnvType.CLIENT)
    public static final class EmptyListEntry extends Entry {
        private final StringWidget widget;

        public EmptyListEntry(Component text, Font textRenderer) {
            this.widget = new StringWidget(text, textRenderer);
        }

        public Component getNarration() {
            return this.widget.getMessage();
        }

        public void renderContent(GuiGraphicsExtractor context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            this.widget.setPosition(this.getContentXMiddle() - this.widget.getWidth() / 2, this.getContentYMiddle() - this.widget.getHeight() / 2);
            this.widget.render(context, mouseX, mouseY, deltaTicks);
        }
    }

    @Environment(EnvType.CLIENT)
    public static final class WorldEntry extends Entry {
        private static final int field_32435 = 32;
        private static final int field_32436 = 32;
        private final FnafWorldListWidget parent;
        private final Minecraft client;
        private final Screen screen;
        final LevelSummary level;
        private final FaviconTexture icon;
        private final StringWidget displayNameWidget;
        private final StringWidget nameWidget;
        private final StringWidget detailsWidget;
        @Nullable
        private Path iconPath;

        public WorldEntry(FnafWorldListWidget parent, LevelSummary summary) {
            this.parent = parent;
            this.client = parent.minecraft;
            this.screen = parent.getParent();
            this.level = summary;
            this.icon = FaviconTexture.forWorld(this.client.getTextureManager(), summary.getLevelId());
            this.iconPath = summary.getIcon();
            int i = parent.getRowWidth() - this.getTextX() - 2;
            Component text = Component.literal(summary.getLevelName());
            this.displayNameWidget = new StringWidget(text, this.client.font);
            this.displayNameWidget.setMaxWidth(i);
            if (this.client.font.width(text) > i) {
                this.displayNameWidget.setTooltip(Tooltip.create(text));
            }

            String string = summary.getLevelId();
            long l = summary.getLastPlayed();
            if (l != -1L) {
                string = string + " (" + FnafWorldListWidget.DATE_FORMAT.format(Instant.ofEpochMilli(l)) + ")";
            }

            Component text2 = Component.literal(string);
            //.setTextColor(-8355712)
            this.nameWidget = (new StringWidget(text2, this.client.font));
            this.nameWidget.setMaxWidth(i);
            if (this.client.font.width(string) > i) {
                this.nameWidget.setTooltip(Tooltip.create(text2));
            }

            Component text3 = summary.getInfo();
            //.setTextColor(-8355712)
            this.detailsWidget = (new StringWidget(text3, this.client.font));
            this.detailsWidget.setMaxWidth(i);
            if (this.client.font.width(text3) > i) {
                this.detailsWidget.setTooltip(Tooltip.create(text3));
            }

            this.validateIconPath();
            this.loadIcon();
        }

        private void validateIconPath() {
            if (this.iconPath != null) {
                try {
                    BasicFileAttributes basicFileAttributes = Files.readAttributes(this.iconPath, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
                    if (basicFileAttributes.isSymbolicLink()) {
                        List<ForbiddenSymlinkInfo> list = this.client.directoryValidator().validateSymlink(this.iconPath);
                        if (!list.isEmpty()) {
                            FnafWorldListWidget.LOGGER.warn("{}", ContentValidationException.getMessage(this.iconPath, list));
                            this.iconPath = null;
                        } else {
                            basicFileAttributes = Files.readAttributes(this.iconPath, BasicFileAttributes.class);
                        }
                    }

                    if (!basicFileAttributes.isRegularFile()) {
                        this.iconPath = null;
                    }
                } catch (NoSuchFileException var3) {
                    this.iconPath = null;
                } catch (IOException iOException) {
                    FnafWorldListWidget.LOGGER.error("could not validate symlink", iOException);
                    this.iconPath = null;
                }

            }
        }

        public Component getNarration() {
            Component text = Component.translatable("narrator.select.world_info", new Object[]{this.level.getLevelName(), Component.translationArg(new Date(this.level.getLastPlayed())), this.level.getInfo()});
            if (this.level.isLocked()) {
                text = CommonComponents.joinForNarration(new Component[]{text, FnafWorldListWidget.LOCKED_TEXT});
            }

            if (this.level.isExperimental()) {
                text = CommonComponents.joinForNarration(new Component[]{text, FnafWorldListWidget.EXPERIMENTAL_TEXT});
            }

            return Component.translatable("narrator.select", new Object[]{text});
        }

        public void renderContent(GuiGraphicsExtractor context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            int i = this.getTextX();
            this.displayNameWidget.setPosition(i, this.getContentY() + 1);
            this.displayNameWidget.render(context, mouseX, mouseY, deltaTicks);
            StringWidget var10000 = this.nameWidget;
            int var10002 = this.getContentY();
            Objects.requireNonNull(this.client.font);
            var10000.setPosition(i, var10002 + 9 + 3);
            this.nameWidget.render(context, mouseX, mouseY, deltaTicks);
            var10000 = this.detailsWidget;
            var10002 = this.getContentY();
            Objects.requireNonNull(this.client.font);
            var10002 += 9;
            Objects.requireNonNull(this.client.font);
            var10000.setPosition(i, var10002 + 9 + 3);
            this.detailsWidget.render(context, mouseX, mouseY, deltaTicks);
            context.blit(RenderPipelines.GUI_TEXTURED, this.icon.textureLocation(), this.getContentX(), this.getContentY(), 0.0F, 0.0F, 32, 32, 32, 32);
            if (this.parent.worldListType == FnafWorldListWidget.WorldListType.SINGLEPLAYER && ((Boolean)this.client.options.touchscreen().get() || hovered)) {
                context.fill(this.getContentX(), this.getContentY(), this.getContentX() + 32, this.getContentY() + 32, -1601138544);
                int j = mouseX - this.getContentX();
                boolean bl = j < 32;
                Identifier identifier = bl ? FnafWorldListWidget.JOIN_HIGHLIGHTED_TEXTURE : FnafWorldListWidget.JOIN_TEXTURE;
                Identifier identifier2 = bl ? FnafWorldListWidget.WARNING_HIGHLIGHTED_TEXTURE : FnafWorldListWidget.WARNING_TEXTURE;
                Identifier identifier3 = bl ? FnafWorldListWidget.ERROR_HIGHLIGHTED_TEXTURE : FnafWorldListWidget.ERROR_TEXTURE;
                Identifier identifier4 = bl ? FnafWorldListWidget.MARKED_JOIN_HIGHLIGHTED_TEXTURE : FnafWorldListWidget.MARKED_JOIN_TEXTURE;
                if (this.level instanceof LevelSummary.SymlinkLevelSummary || this.level instanceof LevelSummary.CorruptedLevelSummary) {
                    context.blitSprite(RenderPipelines.GUI_TEXTURED, identifier3, this.getContentX(), this.getContentY(), 32, 32);
                    context.blitSprite(RenderPipelines.GUI_TEXTURED, identifier4, this.getContentX(), this.getContentY(), 32, 32);
                    return;
                }

                if (this.level.isLocked()) {
                    context.blitSprite(RenderPipelines.GUI_TEXTURED, identifier3, this.getContentX(), this.getContentY(), 32, 32);
                    if (bl) {
                        context.setTooltipForNextFrame(this.client.font.split(FnafWorldListWidget.LOCKED_TEXT, 175), mouseX, mouseY);
                    }
                } else if (this.level.requiresManualConversion()) {
                    context.blitSprite(RenderPipelines.GUI_TEXTURED, identifier3, this.getContentX(), this.getContentY(), 32, 32);
                    if (bl) {
                        context.setTooltipForNextFrame(this.client.font.split(FnafWorldListWidget.CONVERSION_TOOLTIP, 175), mouseX, mouseY);
                    }
                } else if (!this.level.isCompatible()) {
                    context.blitSprite(RenderPipelines.GUI_TEXTURED, identifier3, this.getContentX(), this.getContentY(), 32, 32);
                    if (bl) {
                        context.setTooltipForNextFrame(this.client.font.split(FnafWorldListWidget.INCOMPATIBLE_TOOLTIP, 175), mouseX, mouseY);
                    }
                } else if (this.level.shouldBackup()) {
                    context.blitSprite(RenderPipelines.GUI_TEXTURED, identifier4, this.getContentX(), this.getContentY(), 32, 32);
                    if (this.level.isDowngrade()) {
                        context.blitSprite(RenderPipelines.GUI_TEXTURED, identifier3, this.getContentX(), this.getContentY(), 32, 32);
                        if (bl) {
                            context.setTooltipForNextFrame(ImmutableList.of(FnafWorldListWidget.FROM_NEWER_VERSION_FIRST_LINE.getVisualOrderText(), FnafWorldListWidget.FROM_NEWER_VERSION_SECOND_LINE.getVisualOrderText()), mouseX, mouseY);
                        }
                    } else if (!SharedConstants.getCurrentVersion().stable()) {
                        context.blitSprite(RenderPipelines.GUI_TEXTURED, identifier2, this.getContentX(), this.getContentY(), 32, 32);
                        if (bl) {
                            context.setTooltipForNextFrame(ImmutableList.of(FnafWorldListWidget.SNAPSHOT_FIRST_LINE.getVisualOrderText(), FnafWorldListWidget.SNAPSHOT_SECOND_LINE.getVisualOrderText()), mouseX, mouseY);
                        }
                    }
                } else {
                    context.blitSprite(RenderPipelines.GUI_TEXTURED, identifier, this.getContentX(), this.getContentY(), 32, 32);
                }
            }

        }

        private int getTextX() {
            return this.getContentX() + 32 + 3;
        }

        public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
            if (this.allowConfirmationByKeyboard() && (doubled || click.x() - (double)this.parent.getRowLeft() <= (double)32.0F && this.parent.worldListType == FnafWorldListWidget.WorldListType.SINGLEPLAYER)) {
                this.client.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                Consumer<WorldEntry> consumer = this.parent.confirmationCallback;
                if (consumer != null) {
                    consumer.accept(this);
                    return true;
                }
            }

            return super.mouseClicked(click, doubled);
        }

        public boolean keyPressed(KeyEvent input) {
            if (input.isSelection() && this.allowConfirmationByKeyboard()) {
                this.client.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                Consumer<WorldEntry> consumer = this.parent.confirmationCallback;
                if (consumer != null) {
                    consumer.accept(this);
                    return true;
                }
            }

            return super.keyPressed(input);
        }

        public boolean allowConfirmationByKeyboard() {
            return this.level.primaryActionActive() || this.parent.worldListType == FnafWorldListWidget.WorldListType.UPLOAD_WORLD;
        }

        public void play() {
            if (this.level.primaryActionActive()) {
                if (this.level instanceof LevelSummary.SymlinkLevelSummary) {
                    this.client.setScreen(NoticeWithLinkScreen.createWorldSymlinkWarningScreen(() -> this.client.setScreen(this.screen)));
                } else {
                    WorldOpenFlows var10000 = this.client.createWorldOpenFlows();
                    String var10001 = this.level.getLevelId();
                    FnafWorldListWidget var10002 = this.parent;
                    Objects.requireNonNull(var10002);
                    var10000.openWorld(var10001, var10002::refresh);
                }
            }
        }

        public void deleteIfConfirmed() {
            this.client.setScreen(new ConfirmScreen((confirmed) -> {
                if (confirmed) {
                    this.client.setScreen(new ProgressScreen(true));
                    this.delete();
                }

                this.parent.refresh();
            }, Component.translatable("selectWorld.deleteQuestion"), Component.translatable("selectWorld.deleteWarning", new Object[]{this.level.getLevelName()}), Component.translatable("selectWorld.deleteButton"), CommonComponents.GUI_CANCEL));
        }

        public void delete() {
            LevelStorageSource levelStorage = this.client.getLevelSource();
            String string = this.level.getLevelId();

            try (LevelStorageSource.LevelStorageAccess session = levelStorage.createAccess(string)) {
                session.deleteLevel();
            } catch (IOException iOException) {
                SystemToast.onWorldDeleteFailure(this.client, string);
                FnafWorldListWidget.LOGGER.error("Failed to delete world {}", string, iOException);
            }

        }

        public void edit() {
            this.openReadingWorldScreen();
            String string = this.level.getLevelId();

            LevelStorageSource.LevelStorageAccess session;
            try {
                session = this.client.getLevelSource().validateAndCreateAccess(string);
            } catch (IOException iOException) {
                SystemToast.onWorldAccessFailure(this.client, string);
                FnafWorldListWidget.LOGGER.error("Failed to access level {}", string, iOException);
                this.parent.load();
                return;
            } catch (ContentValidationException symlinkValidationException) {
                FnafWorldListWidget.LOGGER.warn("{}", symlinkValidationException.getMessage());
                this.client.setScreen(NoticeWithLinkScreen.createWorldSymlinkWarningScreen(() -> this.client.setScreen(this.screen)));
                return;
            }

            EditWorldScreen editWorldScreen;
            try {
                editWorldScreen = EditWorldScreen.create(this.client, session, (edited) -> {
                    session.safeClose();
                    this.parent.refresh();
                });
            } catch (NbtException | ReportedNbtException | IOException exception) {
                session.safeClose();
                SystemToast.onWorldAccessFailure(this.client, string);
                FnafWorldListWidget.LOGGER.error("Failed to load world data {}", string, exception);
                this.parent.load();
                return;
            }

            this.client.setScreen(editWorldScreen);
        }

        public void recreate() {
            this.openReadingWorldScreen();

            try (LevelStorageSource.LevelStorageAccess session = this.client.getLevelSource().validateAndCreateAccess(this.level.getLevelId())) {
                Pair<LevelSettings, WorldCreationContext> pair = this.client.createWorldOpenFlows().recreateWorldData(session);
                LevelSettings levelInfo = (LevelSettings)pair.getFirst();
                WorldCreationContext generatorOptionsHolder = (WorldCreationContext)pair.getSecond();
                Path path = CreateWorldScreen.createTempDataPackDirFromExistingWorld(session.getLevelPath(LevelResource.DATAPACK_DIR), this.client);
                generatorOptionsHolder.validate();
                if (generatorOptionsHolder.options().isOldCustomizedWorld()) {
                    this.client.setScreen(new ConfirmScreen((confirmed) -> {
                        Minecraft var10000 = this.client;
                        Object var5;
                        if (confirmed) {
                            Minecraft var10001 = this.client;
                            FnafWorldListWidget var10002 = this.parent;
                            Objects.requireNonNull(var10002);
                            var5 = CreateWorldScreen.createFromExisting(var10001, var10002::refresh, levelInfo, generatorOptionsHolder, path);
                        } else {
                            var5 = this.screen;
                        }

                        var10000.setScreen((Screen)var5);
                    }, Component.translatable("selectWorld.recreate.customized.title"), Component.translatable("selectWorld.recreate.customized.text"), CommonComponents.GUI_PROCEED, CommonComponents.GUI_CANCEL));
                } else {
                    Minecraft var10000 = this.client;
                    Minecraft var10001 = this.client;
                    FnafWorldListWidget var10002 = this.parent;
                    Objects.requireNonNull(var10002);
                    var10000.setScreen(CreateWorldScreen.createFromExisting(var10001, var10002::refresh, levelInfo, generatorOptionsHolder, path));
                }
            } catch (ContentValidationException symlinkValidationException) {
                FnafWorldListWidget.LOGGER.warn("{}", symlinkValidationException.getMessage());
                this.client.setScreen(NoticeWithLinkScreen.createWorldSymlinkWarningScreen(() -> this.client.setScreen(this.screen)));
            } catch (Exception exception) {
                FnafWorldListWidget.LOGGER.error("Unable to recreate world", exception);
                this.client.setScreen(new AlertScreen(() -> this.client.setScreen(this.screen), Component.translatable("selectWorld.recreate.error.title"), Component.translatable("selectWorld.recreate.error.text")));
            }

        }

        private void openReadingWorldScreen() {
            this.client.setScreenAndShow(new GenericMessageScreen(Component.translatable("selectWorld.data_read")));
        }

        private void loadIcon() {
            boolean bl = this.iconPath != null && Files.isRegularFile(this.iconPath, new LinkOption[0]);
            if (bl) {
                try (InputStream inputStream = Files.newInputStream(this.iconPath)) {
                    this.icon.upload(NativeImage.read(inputStream));
                } catch (Throwable throwable) {
                    FnafWorldListWidget.LOGGER.error("Invalid icon for world {}", this.level.getLevelId(), throwable);
                    this.iconPath = null;
                }
            } else {
                this.icon.clear();
            }

        }

        public void close() {
            if (!this.icon.isClosed()) {
                this.icon.close();
            }

        }

        public String getLevelDisplayName() {
            return this.level.getLevelName();
        }

        public LevelSummary getLevel() {
            return this.level;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class LoadingEntry extends Entry {
        private static final Component LOADING_LIST_TEXT = Component.translatable("selectWorld.loading_list");
        private final Minecraft client;

        public LoadingEntry(Minecraft client) {
            this.client = client;
        }

        public void renderContent(GuiGraphicsExtractor context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            int i = (this.client.screen.width - this.client.font.width(LOADING_LIST_TEXT)) / 2;
            int var10000 = this.getContentY();
            int var10001 = this.getContentHeight();
            Objects.requireNonNull(this.client.font);
            int j = var10000 + (var10001 - 9) / 2;
            context.text(this.client.font, LOADING_LIST_TEXT, i, j, -1);
            String string = LoadingDotsText.get(Util.getMillis());
            int k = (this.client.screen.width - this.client.font.width(string)) / 2;
            Objects.requireNonNull(this.client.font);
            int l = j + 9;
            context.text(this.client.font, string, k, l, -8355712);
        }

        public Component getNarration() {
            return LOADING_LIST_TEXT;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Builder {
        private final Minecraft client;
        private final Screen parent;
        private int width;
        private int height;
        private String search = "";
        private WorldListType worldListType;
        @Nullable
        private FnafWorldListWidget predecessor;
        @Nullable
        private Consumer<LevelSummary> selectionCallback;
        @Nullable
        private Consumer<WorldEntry> confirmationCallback;

        public Builder(Minecraft client, Screen parent) {
            this.worldListType = FnafWorldListWidget.WorldListType.SINGLEPLAYER;
            this.predecessor = null;
            this.selectionCallback = null;
            this.confirmationCallback = null;
            this.client = client;
            this.parent = parent;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder search(String search) {
            this.search = search;
            return this;
        }

        public Builder predecessor(@Nullable FnafWorldListWidget predecessor) {
            this.predecessor = predecessor;
            return this;
        }

        public Builder selectionCallback(Consumer<LevelSummary> selectionCallback) {
            this.selectionCallback = selectionCallback;
            return this;
        }

        public Builder confirmationCallback(Consumer<WorldEntry> confirmationCallback) {
            this.confirmationCallback = confirmationCallback;
            return this;
        }

        public Builder uploadWorld() {
            this.worldListType = FnafWorldListWidget.WorldListType.UPLOAD_WORLD;
            return this;
        }

        public FnafWorldListWidget toWidget() {
            return new FnafWorldListWidget(this.parent, this.client, this.width, this.height, this.search, this.predecessor, this.selectionCallback, this.confirmationCallback, this.worldListType);
        }
    }

    @Environment(EnvType.CLIENT)
    public static enum WorldListType {
        SINGLEPLAYER,
        UPLOAD_WORLD;
    }
}
