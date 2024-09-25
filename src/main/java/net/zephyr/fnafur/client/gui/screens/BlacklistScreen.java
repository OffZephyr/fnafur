package net.zephyr.fnafur.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.zephyr.fnafur.FnafUniverseResuited;

public class BlacklistScreen extends Screen {
    long backgroundFadeStart = 0;
    Identifier BACKGROUND = Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/blacklist/blacklist_bg.png");
    Identifier TEXT1 = Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/blacklist/blacklist_text_1.png");
    Identifier TEXT2 = Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/blacklist/blacklist_text_2.png");
    Identifier TEXT_SCROLL = Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/blacklist/blacklist_text_scroll.png");
    Identifier ZEPHYR = Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/blacklist/blacklist_zeph_1.png");
    int textScroll = 0, bgScroll = 0, zephTimer;
    final int zephTimerMax = 40;
    int clickcolor = 0x00FFFFFF;
    public BlacklistScreen() {
        super(Text.empty());
    }

    @Override
    public void tick() {
        ZEPHYR = zephTimer > zephTimerMax/2 ?
                Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/blacklist/blacklist_zeph_2.png")
                : Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/blacklist/blacklist_zeph_1.png");

        zephTimer = zephTimer > zephTimerMax ? 0 : zephTimer + 1;
        super.tick();
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if(mouseX > this.width/2f && mouseY > (this.height/3f) * 2 && mouseY < (this.height/3f) * 2 + (this.height/12f)){
            clickcolor = 0x33FFFFFF;
        }
        else {
            clickcolor = 0x00FFFFFF;
        }
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(mouseX > this.width/2f && mouseY > (this.height/3f) * 2 && mouseY < (this.height/3f) * 2 + (this.height/12f)){
            clickcolor = 0x66FFFFFF;
            Util.getOperatingSystem().open("https://discord.gg/wTsstGhAJQ");
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        clickcolor = 0x00FFFFFF;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        RenderSystem.enableBlend();

        textScroll = textScroll > this.width*2 ? 0 : textScroll + 1;
        bgScroll = bgScroll > this.width*3 ? 0 : bgScroll + 1;

        context.drawTexture(BACKGROUND, 0, 0, bgScroll/3f, 0, this.width, this.height, this.width, this.height);
        context.drawTexture(TEXT_SCROLL, 0, this.height/36, -textScroll/2f, this.height/12, 1710, this.height/12, this.width, this.height/12);
        context.drawTexture(ZEPHYR, 0, 0, 0, 0, this.width, this.height, this.width, this.height);

        context.fill(this.width/2, (this.height/3) * 2, this.width, (this.height/3) * 2 + (this.height/12), clickcolor);

        context.drawTexture(TEXT1, 0, 0, 0, 0, this.width, this.height, this.width, this.height);
        context.drawTexture(TEXT2, 0, 0, 0, 0, this.width, this.height, this.width, this.height);


        RenderSystem.enableBlend();

        super.render(context, mouseX, mouseY, delta);
    }
}
