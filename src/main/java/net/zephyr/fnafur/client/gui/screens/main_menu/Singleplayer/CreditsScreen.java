package net.zephyr.fnafur.client.gui.screens.main_menu.Singleplayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.client.gui.screens.main_menu.FnafTitleScreen;
import net.zephyr.fnafur.init.SoundsInit;
import net.zephyr.fnafur.util.CustomSoundInstance;
import net.zephyr.fnafur.util.EasingMathUtil;
import net.zephyr.fnafur.util.jsonReaders.credits.CreditsDataHandler;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.awt.Desktop;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreditsScreen extends Screen {
    SoundInstance music;
    public final FnafTitleScreen TITLE_SCREEN;
    private static final Identifier SCROLLING_TEXTURE = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/credits/scrolling_texture.png");
    private static final Identifier COMMENT_BACKDROP = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/credits/comment_backdrop.png");
    private static final Identifier CHECKER = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/mainmenu/credits/checker.png");

    public CreditsScreen(Component title, FnafTitleScreen titleScreen) {
        super(title);
        TITLE_SCREEN = titleScreen;
        updateCreditsList();
    }

    float sidebars_u = 0;
    float checker_u = 0;
    int scrollIndex = 0;

    float currentR = 0;
    float currentG = 0;
    float currentB = 0;
    float goalR = 1;
    float goalG = 1;
    float goalB = 1;

    float scrollY = 0;
    List<String> creditsList = new ArrayList<>();
    List<String> mains = new ArrayList<>();
    List<String> subs = new ArrayList<>();

    @Override
    protected void init() {
        if(music == null) {
            music = new SimpleSoundInstance(SoundsInit.THANK_YOU_FOR_YOUR_PATIENCE.location(), SoundSource.MASTER, 0.35f, 1, RandomSource.create(), true, 0, SoundInstance.Attenuation.NONE, 0, 0, 0, false);
            Minecraft.getInstance().getSoundManager().play(music);
            scrollList(1);
        }
        super.init();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks) {
        TITLE_SCREEN.width = this.width;
        TITLE_SCREEN.height = this.height;

        TITLE_SCREEN.renderBackgroundextractRenderState(context, (int) (width/2f), (int) (height/2f), deltaTicks);
        scrollY = Mth.lerp(deltaTicks, scrollY, scrollIndex);
        currentR = Mth.lerp(deltaTicks/5f, currentR, goalR);
        currentG = Mth.lerp(deltaTicks/5f, currentG, goalG);
        currentB = Mth.lerp(deltaTicks/5f, currentB, goalB);
        sidebars_u += deltaTicks/2f;
        checker_u += deltaTicks/20f;

        int color = ARGB.colorFromFloat(1, currentR, currentG,currentB);
        int inverted_color = ARGB.colorFromFloat(1, 1 - currentR, 1 - currentG, 1 - currentB);
        int color_dark = ARGB.colorFromFloat(1, currentR/3f, currentG/3f,currentB/3f);
        context.fill(0, 0, width, height, color_dark);

        context.pose().pushMatrix();
        context.pose().translate(width/2f, height/2f);
        context.pose().rotate(15f * Mth.DEG_TO_RAD);
        context.pose().scale(onHeight(25f));
        context.blit(RenderPipelines.GUI_TEXTURED, CHECKER, -width/2, -height/2, checker_u, checker_u/2f, width, height, 64,64, ARGB.color(0.25f, color));
        context.pose().popMatrix();

        int scrollTextureWidth = (int) onHeight(1024);
        int scrollTextureHeight = (int) onHeight(1024);
        int scrollTextureX = (int) onHeight(0);
        int scrollTextureY = (int) onHeight(-1024 + 200);
        int scrollTextureY2 = (int) onHeight(200);

        for(int i = 0; i < creditsList.size(); i++){
            float offset = 20;
            float j = i - (scrollY);
            String string = creditsList.get(i);
            boolean main = mains.contains(string);
            boolean sub = subs.contains(string);
            FontDescription spriteFont = new FontDescription.Resource(Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "lemon_bold"));
            Style style = Style.EMPTY.withFont(spriteFont);
            Component text = main ? Component.translatable("credits.main." + string) : sub ? Component.translatable("credits.sub." + string) : Component.translatable(string);
            text = text.toFlatList(style).getFirst();
            float y = height / 2f + (j * offset);
            float scale = main ? 3f : sub ? 2f : i == scrollIndex ? 1.25f : 1f;
            float positionIndex = Math.abs(y - (height / 2f))/(height / 2f);
            scale = scale * (1 - positionIndex);
            y += 15 * positionIndex;
            double x = 30 * EasingMathUtil.easeOutSine(1 - positionIndex);
            if(i == scrollIndex) x += 20;

            int text_color = ARGB.color(Mth.lerpInt(positionIndex, 255, 0), inverted_color);

            context.pose().pushMatrix();
            context.pose().translate((float)x, y);
            context.pose().scale(scale);
            context.text(font, text, 0, (int) (scale * -1), text_color, false);
            context.pose().popMatrix();
        }

        context.blit(RenderPipelines.GUI_TEXTURED, SCROLLING_TEXTURE, scrollTextureX, scrollTextureY, sidebars_u, 0, width, scrollTextureHeight, width, scrollTextureWidth, scrollTextureWidth, scrollTextureWidth, color);
        context.blit(RenderPipelines.GUI_TEXTURED, SCROLLING_TEXTURE, scrollTextureX, height - scrollTextureY2, -sidebars_u, 0, width, scrollTextureHeight, width, scrollTextureWidth, scrollTextureWidth, scrollTextureWidth, color);


        FontDescription spriteFont = new FontDescription.Resource(Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "lemon_bold"));
        String checkName = StringUtils.replaceIgnoreCase(creditsList.get(scrollIndex), "credits.name.", "");
        Style style = Style.EMPTY.withFont(spriteFont);

        if(CreditsDataHandler.ALL_ENTRIES.get(checkName) instanceof CreditsDataHandler.CreditsEntry entry) {
            if (!(entry.ROLES().contains("fazbear_family") || entry.ROLES().contains("pizza_lover"))) {
                int commentBackWidth = (int) onHeight(1010);
                int commentBackHeight = (int) onHeight(670);
                int commentBackTextureWidth = (int) onHeight((1024 / 1280f) * 1920f);
                int commentBackTextureHeight = (int) onHeight((1024 / 720f) * 1080f);
                int commentX = width - commentBackWidth - (int) onHeight(30);
                int commentY = height / 2 - commentBackHeight / 2;
                context.blit(RenderPipelines.GUI_TEXTURED, COMMENT_BACKDROP, commentX, commentY, 0, 0, commentBackWidth, commentBackHeight, commentBackWidth, commentBackHeight, commentBackTextureWidth, commentBackTextureHeight, color);

                Component intro = Component.translatable("credits.comment.intro", Component.translatable("credits.name." + entry.NAME()).getString());
                Component comment = Component.translatable("credits.comment." + entry.NAME());
                comment = comment.toFlatList(style).getFirst();

                intro = Component.literal(intro.getString()).setStyle(style);

                float comment_scale = 3.25f;
                context.pose().pushMatrix();
                context.pose().translate(commentX + (int) onHeight(63), commentY + (int) onHeight(63));
                context.pose().scale(onHeight(comment_scale));
                context.text(font, intro, 0, 0, inverted_color, false);
                context.pose().translate(0, (int) onHeight(141 - 63));
                context.textWithWordWrap(font, comment, 0, 0, (int)(900 / comment_scale), inverted_color, false);
                context.pose().popMatrix();

            }
        }


        int scrollX = (int) onHeight(8);
        int scrollWidth = (int) onHeight(24);
        GoopyScreen.drawOutline(context, scrollX, (int) (scrollTextureY2), scrollWidth, height - (scrollTextureY2*2), inverted_color);
        int scrollX2 = scrollX + 2;
        int scrollWidth2 = scrollX + scrollWidth - 1;

        int scrollY = (int) (height - onHeight(400f));
        int scrollTotalHeight = (((scrollY)) - 2)/getTotalWithoutTitles();
        int scrollStart = scrollTextureY2 + 2 + (scrollTotalHeight * getRealScrollIndex());
        int scrollHeightPos = scrollStart + scrollTotalHeight;

        context.fill(scrollX2, scrollStart, scrollWidth2, scrollHeightPos,inverted_color);

        Component text = Component.literal(checkName);
        if(CreditsDataHandler.ALL_ENTRIES.get(checkName) instanceof CreditsDataHandler.CreditsEntry entry){
            text = Component.literal("");
            for(int i = 0; i < entry.ROLES().size(); i++){
                String s = entry.ROLES().get(i);
                Component translatable = Component.translatable("credits.role." + s);

                String end = i < entry.ROLES().size() - 1 ? " | " : "";
                translatable = Component.literal(translatable.getString() + end);

                text = Component.literal(text.getString() + translatable.tryCollapseToString());
            }
        }
        Component rolesText = text.toFlatList(style).getFirst();


        context.pose().pushMatrix();
        context.pose().translate(width/2f, height - onHeight(75));
        context.pose().scale(1.25f);
        context.text(font, rolesText, -(font.width(rolesText)/2), -3, inverted_color, false);
        context.pose().popMatrix();


        if(CreditsDataHandler.ALL_ENTRIES.get(checkName) instanceof CreditsDataHandler.CreditsEntry entry) {
            if(!(entry.ROLES().contains("fazbear_family") || entry.ROLES().contains("pizza_lover"))){
                Component quote_text = Component.translatable("credits.quote." + entry.NAME()).toFlatList(style).getFirst();
                context.pose().pushMatrix();
                context.pose().translate(width / 2f, onHeight(75));
                context.pose().scale(1.25f);
                context.text(font, quote_text, -(font.width(quote_text) / 2), -3, inverted_color, false);
                context.pose().popMatrix();
            }
        }

        TITLE_SCREEN.renderForeground(context, (int) (width/2f), (int) (height/2f), deltaTicks, 0.5f);
        super.extractRenderState(context, mouseX, mouseY, deltaTicks);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollList( verticalAmount > 0 ? -1 : 1);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    void scrollList(int amount){
        scrollIndex += amount;
        scrollIndex = Mth.clamp(scrollIndex, 0, creditsList.size() - 1);
        while(mains.contains(creditsList.get(scrollIndex)) || subs.contains(creditsList.get(scrollIndex))){
            if(scrollIndex + amount <= 0 || scrollIndex + amount >= creditsList.size()){
                amount *= -1;
            }
            scrollIndex += amount;
        }

        Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(SoundsInit.CREDITS_SCROLL, SoundSource.UI, 0.25f, 1, RandomSource.create(), 0, 0, 0));

        String checkName = StringUtils.replaceIgnoreCase(creditsList.get(scrollIndex), "credits.name.", "");
        if(CreditsDataHandler.ALL_ENTRIES.get(checkName) instanceof CreditsDataHandler.CreditsEntry entry){
            int color = entry.COLOR();
            goalR = ARGB.redFloat(color);
            goalG = ARGB.greenFloat(color);
            goalB = ARGB.blueFloat(color);
        }
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
        if(input.isUp()) scrollList(-1);
        if(input.isDown()) scrollList(1);

        if(input.isConfirmation()) {
            String checkName = StringUtils.replaceIgnoreCase(creditsList.get(scrollIndex), "credits.name.", "");
            if (CreditsDataHandler.ALL_ENTRIES.get(checkName) instanceof CreditsDataHandler.CreditsEntry entry) {
                String link = entry.LINK();

                Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(SoundsInit.CREDITS_HOVER, SoundSource.UI, 1, 1, RandomSource.create(), 0, 0, 0));
                if(!link.isEmpty()){
                    try {
                        Util.getPlatform().openUri(new URI(link));
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return super.keyPressed(input);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().getSoundManager().stop(music);
        music = null;
        this.minecraft.setScreen(TITLE_SCREEN);
        TITLE_SCREEN.fadeBackground(6, 0.35f);
    }

    float onWidth(float pixel){
        return (pixel / 1920f) * width;
    }
    float onHeight(float pixel){
        return (pixel / 1080f) * height;
    }

    int getTotalWithoutTitles(){
        List<String> list = new ArrayList<>();
        creditsList.forEach((s) -> {
            if(!(mains.contains(s) || subs.contains(s))) list.add(s);
        });
        return list.size();
    }
    int getRealScrollIndex(){
        List<String> list = new ArrayList<>();
        creditsList.forEach((s) -> {
            if(!(mains.contains(s) || subs.contains(s))) list.add(s);
        });
        return list.indexOf(creditsList.get(scrollIndex));
    }

    void updateCreditsList(){
        for(String main : CreditsDataHandler.MAIN_CATEGORIES){
            creditsList.add(main);
            mains.add(main);
            for(String sub : CreditsDataHandler.SUB_PER_MAIN_CATEGORY.get(main)){
                creditsList.add(sub);
                subs.add(sub);
                if(CreditsDataHandler.ENTRIES.get(main) != null){
                    for(CreditsDataHandler.CreditsEntry entries : CreditsDataHandler.ENTRIES.get(main).get(sub)){
                        String text = main.contains("supporters") ? entries.NAME() : "credits.name." + entries.NAME();
                        creditsList.add(text);
                    }
                }
            }
        }
    }
}
