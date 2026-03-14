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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.FatalErrorScreen;
import net.minecraft.client.gui.screen.LoadingDisplay;
import net.minecraft.client.gui.screen.MessageScreen;
import net.minecraft.client.gui.screen.NoticeScreen;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import net.minecraft.client.gui.screen.world.SymlinkWarningScreen;
import net.minecraft.client.gui.screen.world.WorldIcon;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtCrashException;
import net.minecraft.nbt.NbtException;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.integrated.IntegratedServerLoader;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.path.SymlinkEntry;
import net.minecraft.util.path.SymlinkValidationException;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.LevelStorageException;
import net.minecraft.world.level.storage.LevelSummary;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class FnafWorldListWidget extends AlwaysSelectedEntryListWidget<FnafWorldListWidget.Entry> {
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
    static final Text FROM_NEWER_VERSION_FIRST_LINE;
    static final Text FROM_NEWER_VERSION_SECOND_LINE;
    static final Text SNAPSHOT_FIRST_LINE;
    static final Text SNAPSHOT_SECOND_LINE;
    static final Text LOCKED_TEXT;
    static final Text CONVERSION_TOOLTIP;
    static final Text INCOMPATIBLE_TOOLTIP;
    static final Text EXPERIMENTAL_TEXT;
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

    FnafWorldListWidget(Screen parent, MinecraftClient client, int width, int height, String search, @Nullable FnafWorldListWidget predecessor, @Nullable Consumer<LevelSummary> selectionCallback, @Nullable Consumer<WorldEntry> confirmationCallback, WorldListType worldListType) {
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
                list = list.stream().filter(LevelSummary::isImmediatelyLoadable).toList();
            }

            return list;
        } catch (CancellationException | CompletionException var2) {
            return null;
        }
    }

    public void load() {
        this.levelsFuture = this.loadLevels();
    }

    public void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
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
                        CreateWorldScreen.show(this.client, () -> this.client.setScreen((Screen)null));
                        break;
                    case 1:
                        this.clearEntries();
                        this.addEntry(new EmptyListEntry(Text.translatable("mco.upload.select.world.none"), this.parent.getTextRenderer()));
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
        LevelStorage.LevelList levelList;
        try {
            levelList = this.client.getLevelStorage().getLevelList();
        } catch (LevelStorageException levelStorageException) {
            LOGGER.error("Couldn't load level list", levelStorageException);
            this.showUnableToLoadScreen(levelStorageException.getMessageText());
            return CompletableFuture.completedFuture(List.of());
        }

        return this.client.getLevelStorage().loadSummaries(levelList).exceptionally((throwable) -> {
            this.client.setCrashReportSupplierAndAddDetails(CrashReport.create(throwable, "Couldn't load level list"));
            return List.of();
        });
    }

    private void showSummaries(String search, List<LevelSummary> summaries) {
        List<Entry> list = new ArrayList();
        Optional<WorldEntry> optional = this.getSelectedAsOptional();
        WorldEntry worldEntry = null;

        for(LevelSummary levelSummary : summaries.stream().filter((summary) -> this.shouldShow(search.toLowerCase(Locale.ROOT), summary)).toList()) {
            WorldEntry worldEntry2 = new WorldEntry(this, levelSummary);
            if (optional.isPresent() && ((WorldEntry)optional.get()).getLevel().getName().equals(worldEntry2.getLevel().getName())) {
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
        return summary.getDisplayName().toLowerCase(Locale.ROOT).contains(search) || summary.getName().toLowerCase(Locale.ROOT).contains(search);
    }

    private void narrateScreenIfNarrationEnabled() {
        this.refreshScroll();
        this.parent.narrateScreenIfNarrationEnabled(true);
    }

    private void showUnableToLoadScreen(Text message) {
        this.client.setScreen(new FatalErrorScreen(Text.translatable("selectWorld.unable_to_load"), message));
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
        Entry entry = (Entry)this.getSelectedOrNull();
        if (entry instanceof WorldEntry worldEntry) {
            return Optional.of(worldEntry);
        } else {
            return Optional.empty();
        }
    }

    public void refresh() {
        this.load();
        this.client.setScreen(this.parent);
    }

    public Screen getParent() {
        return this.parent;
    }

    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        if (this.children().contains(this.loadingEntry)) {
            this.loadingEntry.appendNarrations(builder);
        } else {
            super.appendClickableNarrations(builder);
        }
    }

    static {
        DATE_FORMAT = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault());
        ERROR_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("world_list/error_highlighted");
        ERROR_TEXTURE = Identifier.ofVanilla("world_list/error");
        MARKED_JOIN_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("world_list/marked_join_highlighted");
        MARKED_JOIN_TEXTURE = Identifier.ofVanilla("world_list/marked_join");
        WARNING_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("world_list/warning_highlighted");
        WARNING_TEXTURE = Identifier.ofVanilla("world_list/warning");
        JOIN_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("world_list/join_highlighted");
        JOIN_TEXTURE = Identifier.ofVanilla("world_list/join");
        LOGGER = LogUtils.getLogger();
        FROM_NEWER_VERSION_FIRST_LINE = Text.translatable("selectWorld.tooltip.fromNewerVersion1").formatted(Formatting.RED);
        FROM_NEWER_VERSION_SECOND_LINE = Text.translatable("selectWorld.tooltip.fromNewerVersion2").formatted(Formatting.RED);
        SNAPSHOT_FIRST_LINE = Text.translatable("selectWorld.tooltip.snapshot1").formatted(Formatting.GOLD);
        SNAPSHOT_SECOND_LINE = Text.translatable("selectWorld.tooltip.snapshot2").formatted(Formatting.GOLD);
        LOCKED_TEXT = Text.translatable("selectWorld.locked").formatted(Formatting.RED);
        CONVERSION_TOOLTIP = Text.translatable("selectWorld.conversion.tooltip").formatted(Formatting.RED);
        INCOMPATIBLE_TOOLTIP = Text.translatable("selectWorld.incompatible.tooltip").formatted(Formatting.RED);
        EXPERIMENTAL_TEXT = Text.translatable("selectWorld.experimental");
    }

    @Environment(EnvType.CLIENT)
    public abstract static class Entry extends AlwaysSelectedEntryListWidget.Entry<Entry> implements AutoCloseable {
        public void close() {
        }

        @Nullable
        public LevelSummary getLevel() {
            return null;
        }
    }

    @Environment(EnvType.CLIENT)
    public static final class EmptyListEntry extends Entry {
        private final TextWidget widget;

        public EmptyListEntry(Text text, TextRenderer textRenderer) {
            this.widget = new TextWidget(text, textRenderer);
        }

        public Text getNarration() {
            return this.widget.getMessage();
        }

        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            this.widget.setPosition(this.getContentMiddleX() - this.widget.getWidth() / 2, this.getContentMiddleY() - this.widget.getHeight() / 2);
            this.widget.render(context, mouseX, mouseY, deltaTicks);
        }
    }

    @Environment(EnvType.CLIENT)
    public static final class WorldEntry extends Entry {
        private static final int field_32435 = 32;
        private static final int field_32436 = 32;
        private final FnafWorldListWidget parent;
        private final MinecraftClient client;
        private final Screen screen;
        final LevelSummary level;
        private final WorldIcon icon;
        private final TextWidget displayNameWidget;
        private final TextWidget nameWidget;
        private final TextWidget detailsWidget;
        @Nullable
        private Path iconPath;

        public WorldEntry(FnafWorldListWidget parent, LevelSummary summary) {
            this.parent = parent;
            this.client = parent.client;
            this.screen = parent.getParent();
            this.level = summary;
            this.icon = WorldIcon.forWorld(this.client.getTextureManager(), summary.getName());
            this.iconPath = summary.getIconPath();
            int i = parent.getRowWidth() - this.getTextX() - 2;
            Text text = Text.literal(summary.getDisplayName());
            this.displayNameWidget = new TextWidget(text, this.client.textRenderer);
            this.displayNameWidget.setMaxWidth(i);
            if (this.client.textRenderer.getWidth(text) > i) {
                this.displayNameWidget.setTooltip(Tooltip.of(text));
            }

            String string = summary.getName();
            long l = summary.getLastPlayed();
            if (l != -1L) {
                string = string + " (" + FnafWorldListWidget.DATE_FORMAT.format(Instant.ofEpochMilli(l)) + ")";
            }

            Text text2 = Text.literal(string);
            //.setTextColor(-8355712)
            this.nameWidget = (new TextWidget(text2, this.client.textRenderer));
            this.nameWidget.setMaxWidth(i);
            if (this.client.textRenderer.getWidth(string) > i) {
                this.nameWidget.setTooltip(Tooltip.of(text2));
            }

            Text text3 = summary.getDetails();
            //.setTextColor(-8355712)
            this.detailsWidget = (new TextWidget(text3, this.client.textRenderer));
            this.detailsWidget.setMaxWidth(i);
            if (this.client.textRenderer.getWidth(text3) > i) {
                this.detailsWidget.setTooltip(Tooltip.of(text3));
            }

            this.validateIconPath();
            this.loadIcon();
        }

        private void validateIconPath() {
            if (this.iconPath != null) {
                try {
                    BasicFileAttributes basicFileAttributes = Files.readAttributes(this.iconPath, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
                    if (basicFileAttributes.isSymbolicLink()) {
                        List<SymlinkEntry> list = this.client.getSymlinkFinder().validate(this.iconPath);
                        if (!list.isEmpty()) {
                            FnafWorldListWidget.LOGGER.warn("{}", SymlinkValidationException.getMessage(this.iconPath, list));
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

        public Text getNarration() {
            Text text = Text.translatable("narrator.select.world_info", new Object[]{this.level.getDisplayName(), Text.of(new Date(this.level.getLastPlayed())), this.level.getDetails()});
            if (this.level.isLocked()) {
                text = ScreenTexts.joinSentences(new Text[]{text, FnafWorldListWidget.LOCKED_TEXT});
            }

            if (this.level.isExperimental()) {
                text = ScreenTexts.joinSentences(new Text[]{text, FnafWorldListWidget.EXPERIMENTAL_TEXT});
            }

            return Text.translatable("narrator.select", new Object[]{text});
        }

        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            int i = this.getTextX();
            this.displayNameWidget.setPosition(i, this.getContentY() + 1);
            this.displayNameWidget.render(context, mouseX, mouseY, deltaTicks);
            TextWidget var10000 = this.nameWidget;
            int var10002 = this.getContentY();
            Objects.requireNonNull(this.client.textRenderer);
            var10000.setPosition(i, var10002 + 9 + 3);
            this.nameWidget.render(context, mouseX, mouseY, deltaTicks);
            var10000 = this.detailsWidget;
            var10002 = this.getContentY();
            Objects.requireNonNull(this.client.textRenderer);
            var10002 += 9;
            Objects.requireNonNull(this.client.textRenderer);
            var10000.setPosition(i, var10002 + 9 + 3);
            this.detailsWidget.render(context, mouseX, mouseY, deltaTicks);
            context.drawTexture(RenderPipelines.GUI_TEXTURED, this.icon.getTextureId(), this.getContentX(), this.getContentY(), 0.0F, 0.0F, 32, 32, 32, 32);
            if (this.parent.worldListType == FnafWorldListWidget.WorldListType.SINGLEPLAYER && ((Boolean)this.client.options.getTouchscreen().getValue() || hovered)) {
                context.fill(this.getContentX(), this.getContentY(), this.getContentX() + 32, this.getContentY() + 32, -1601138544);
                int j = mouseX - this.getContentX();
                boolean bl = j < 32;
                Identifier identifier = bl ? FnafWorldListWidget.JOIN_HIGHLIGHTED_TEXTURE : FnafWorldListWidget.JOIN_TEXTURE;
                Identifier identifier2 = bl ? FnafWorldListWidget.WARNING_HIGHLIGHTED_TEXTURE : FnafWorldListWidget.WARNING_TEXTURE;
                Identifier identifier3 = bl ? FnafWorldListWidget.ERROR_HIGHLIGHTED_TEXTURE : FnafWorldListWidget.ERROR_TEXTURE;
                Identifier identifier4 = bl ? FnafWorldListWidget.MARKED_JOIN_HIGHLIGHTED_TEXTURE : FnafWorldListWidget.MARKED_JOIN_TEXTURE;
                if (this.level instanceof LevelSummary.SymlinkLevelSummary || this.level instanceof LevelSummary.RecoveryWarning) {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, identifier3, this.getContentX(), this.getContentY(), 32, 32);
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, identifier4, this.getContentX(), this.getContentY(), 32, 32);
                    return;
                }

                if (this.level.isLocked()) {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, identifier3, this.getContentX(), this.getContentY(), 32, 32);
                    if (bl) {
                        context.drawTooltip(this.client.textRenderer.wrapLines(FnafWorldListWidget.LOCKED_TEXT, 175), mouseX, mouseY);
                    }
                } else if (this.level.requiresConversion()) {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, identifier3, this.getContentX(), this.getContentY(), 32, 32);
                    if (bl) {
                        context.drawTooltip(this.client.textRenderer.wrapLines(FnafWorldListWidget.CONVERSION_TOOLTIP, 175), mouseX, mouseY);
                    }
                } else if (!this.level.isVersionAvailable()) {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, identifier3, this.getContentX(), this.getContentY(), 32, 32);
                    if (bl) {
                        context.drawTooltip(this.client.textRenderer.wrapLines(FnafWorldListWidget.INCOMPATIBLE_TOOLTIP, 175), mouseX, mouseY);
                    }
                } else if (this.level.shouldPromptBackup()) {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, identifier4, this.getContentX(), this.getContentY(), 32, 32);
                    if (this.level.wouldBeDowngraded()) {
                        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, identifier3, this.getContentX(), this.getContentY(), 32, 32);
                        if (bl) {
                            context.drawTooltip(ImmutableList.of(FnafWorldListWidget.FROM_NEWER_VERSION_FIRST_LINE.asOrderedText(), FnafWorldListWidget.FROM_NEWER_VERSION_SECOND_LINE.asOrderedText()), mouseX, mouseY);
                        }
                    } else if (!SharedConstants.getGameVersion().stable()) {
                        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, identifier2, this.getContentX(), this.getContentY(), 32, 32);
                        if (bl) {
                            context.drawTooltip(ImmutableList.of(FnafWorldListWidget.SNAPSHOT_FIRST_LINE.asOrderedText(), FnafWorldListWidget.SNAPSHOT_SECOND_LINE.asOrderedText()), mouseX, mouseY);
                        }
                    }
                } else {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, identifier, this.getContentX(), this.getContentY(), 32, 32);
                }
            }

        }

        private int getTextX() {
            return this.getContentX() + 32 + 3;
        }

        public boolean mouseClicked(Click click, boolean doubled) {
            if (this.allowConfirmationByKeyboard() && (doubled || click.x() - (double)this.parent.getRowLeft() <= (double)32.0F && this.parent.worldListType == FnafWorldListWidget.WorldListType.SINGLEPLAYER)) {
                this.client.getSoundManager().play(PositionedSoundInstance.ui(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                Consumer<WorldEntry> consumer = this.parent.confirmationCallback;
                if (consumer != null) {
                    consumer.accept(this);
                    return true;
                }
            }

            return super.mouseClicked(click, doubled);
        }

        public boolean keyPressed(KeyInput input) {
            if (input.isEnterOrSpace() && this.allowConfirmationByKeyboard()) {
                this.client.getSoundManager().play(PositionedSoundInstance.ui(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                Consumer<WorldEntry> consumer = this.parent.confirmationCallback;
                if (consumer != null) {
                    consumer.accept(this);
                    return true;
                }
            }

            return super.keyPressed(input);
        }

        public boolean allowConfirmationByKeyboard() {
            return this.level.isSelectable() || this.parent.worldListType == FnafWorldListWidget.WorldListType.UPLOAD_WORLD;
        }

        public void play() {
            if (this.level.isSelectable()) {
                if (this.level instanceof LevelSummary.SymlinkLevelSummary) {
                    this.client.setScreen(SymlinkWarningScreen.world(() -> this.client.setScreen(this.screen)));
                } else {
                    IntegratedServerLoader var10000 = this.client.createIntegratedServerLoader();
                    String var10001 = this.level.getName();
                    FnafWorldListWidget var10002 = this.parent;
                    Objects.requireNonNull(var10002);
                    var10000.start(var10001, var10002::refresh);
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
            }, Text.translatable("selectWorld.deleteQuestion"), Text.translatable("selectWorld.deleteWarning", new Object[]{this.level.getDisplayName()}), Text.translatable("selectWorld.deleteButton"), ScreenTexts.CANCEL));
        }

        public void delete() {
            LevelStorage levelStorage = this.client.getLevelStorage();
            String string = this.level.getName();

            try (LevelStorage.Session session = levelStorage.createSessionWithoutSymlinkCheck(string)) {
                session.deleteSessionLock();
            } catch (IOException iOException) {
                SystemToast.addWorldDeleteFailureToast(this.client, string);
                FnafWorldListWidget.LOGGER.error("Failed to delete world {}", string, iOException);
            }

        }

        public void edit() {
            this.openReadingWorldScreen();
            String string = this.level.getName();

            LevelStorage.Session session;
            try {
                session = this.client.getLevelStorage().createSession(string);
            } catch (IOException iOException) {
                SystemToast.addWorldAccessFailureToast(this.client, string);
                FnafWorldListWidget.LOGGER.error("Failed to access level {}", string, iOException);
                this.parent.load();
                return;
            } catch (SymlinkValidationException symlinkValidationException) {
                FnafWorldListWidget.LOGGER.warn("{}", symlinkValidationException.getMessage());
                this.client.setScreen(SymlinkWarningScreen.world(() -> this.client.setScreen(this.screen)));
                return;
            }

            EditWorldScreen editWorldScreen;
            try {
                editWorldScreen = EditWorldScreen.create(this.client, session, (edited) -> {
                    session.tryClose();
                    this.parent.refresh();
                });
            } catch (NbtException | NbtCrashException | IOException exception) {
                session.tryClose();
                SystemToast.addWorldAccessFailureToast(this.client, string);
                FnafWorldListWidget.LOGGER.error("Failed to load world data {}", string, exception);
                this.parent.load();
                return;
            }

            this.client.setScreen(editWorldScreen);
        }

        public void recreate() {
            this.openReadingWorldScreen();

            try (LevelStorage.Session session = this.client.getLevelStorage().createSession(this.level.getName())) {
                Pair<LevelInfo, GeneratorOptionsHolder> pair = this.client.createIntegratedServerLoader().loadForRecreation(session);
                LevelInfo levelInfo = (LevelInfo)pair.getFirst();
                GeneratorOptionsHolder generatorOptionsHolder = (GeneratorOptionsHolder)pair.getSecond();
                Path path = CreateWorldScreen.copyDataPack(session.getDirectory(WorldSavePath.DATAPACKS), this.client);
                generatorOptionsHolder.initializeIndexedFeaturesLists();
                if (generatorOptionsHolder.generatorOptions().isLegacyCustomizedType()) {
                    this.client.setScreen(new ConfirmScreen((confirmed) -> {
                        MinecraftClient var10000 = this.client;
                        Object var5;
                        if (confirmed) {
                            MinecraftClient var10001 = this.client;
                            FnafWorldListWidget var10002 = this.parent;
                            Objects.requireNonNull(var10002);
                            var5 = CreateWorldScreen.create(var10001, var10002::refresh, levelInfo, generatorOptionsHolder, path);
                        } else {
                            var5 = this.screen;
                        }

                        var10000.setScreen((Screen)var5);
                    }, Text.translatable("selectWorld.recreate.customized.title"), Text.translatable("selectWorld.recreate.customized.text"), ScreenTexts.PROCEED, ScreenTexts.CANCEL));
                } else {
                    MinecraftClient var10000 = this.client;
                    MinecraftClient var10001 = this.client;
                    FnafWorldListWidget var10002 = this.parent;
                    Objects.requireNonNull(var10002);
                    var10000.setScreen(CreateWorldScreen.create(var10001, var10002::refresh, levelInfo, generatorOptionsHolder, path));
                }
            } catch (SymlinkValidationException symlinkValidationException) {
                FnafWorldListWidget.LOGGER.warn("{}", symlinkValidationException.getMessage());
                this.client.setScreen(SymlinkWarningScreen.world(() -> this.client.setScreen(this.screen)));
            } catch (Exception exception) {
                FnafWorldListWidget.LOGGER.error("Unable to recreate world", exception);
                this.client.setScreen(new NoticeScreen(() -> this.client.setScreen(this.screen), Text.translatable("selectWorld.recreate.error.title"), Text.translatable("selectWorld.recreate.error.text")));
            }

        }

        private void openReadingWorldScreen() {
            this.client.setScreenAndRender(new MessageScreen(Text.translatable("selectWorld.data_read")));
        }

        private void loadIcon() {
            boolean bl = this.iconPath != null && Files.isRegularFile(this.iconPath, new LinkOption[0]);
            if (bl) {
                try (InputStream inputStream = Files.newInputStream(this.iconPath)) {
                    this.icon.load(NativeImage.read(inputStream));
                } catch (Throwable throwable) {
                    FnafWorldListWidget.LOGGER.error("Invalid icon for world {}", this.level.getName(), throwable);
                    this.iconPath = null;
                }
            } else {
                this.icon.destroy();
            }

        }

        public void close() {
            if (!this.icon.isClosed()) {
                this.icon.close();
            }

        }

        public String getLevelDisplayName() {
            return this.level.getDisplayName();
        }

        public LevelSummary getLevel() {
            return this.level;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class LoadingEntry extends Entry {
        private static final Text LOADING_LIST_TEXT = Text.translatable("selectWorld.loading_list");
        private final MinecraftClient client;

        public LoadingEntry(MinecraftClient client) {
            this.client = client;
        }

        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            int i = (this.client.currentScreen.width - this.client.textRenderer.getWidth(LOADING_LIST_TEXT)) / 2;
            int var10000 = this.getContentY();
            int var10001 = this.getContentHeight();
            Objects.requireNonNull(this.client.textRenderer);
            int j = var10000 + (var10001 - 9) / 2;
            context.drawTextWithShadow(this.client.textRenderer, LOADING_LIST_TEXT, i, j, -1);
            String string = LoadingDisplay.get(Util.getMeasuringTimeMs());
            int k = (this.client.currentScreen.width - this.client.textRenderer.getWidth(string)) / 2;
            Objects.requireNonNull(this.client.textRenderer);
            int l = j + 9;
            context.drawTextWithShadow(this.client.textRenderer, string, k, l, -8355712);
        }

        public Text getNarration() {
            return LOADING_LIST_TEXT;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Builder {
        private final MinecraftClient client;
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

        public Builder(MinecraftClient client, Screen parent) {
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
