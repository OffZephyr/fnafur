package net.zephyr.fnafur.client.gui.screens.crafting;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;

import java.util.*;

public class CpuConfigScreen extends GoopyScreen {
    int time = 24;
    int aggressivity = 0;
    int speed = 2;
    public static final int[][] HOUR_MAP = {
            {1, 2},
            {0, 1},
            {0, 2},
            {0, 3},
            {0, 4},
            {0, 5},
            {0, 6},
            {0, 7},
            {0, 8},
            {0, 9},
            {1, 0},
            {1, 1}
    };
    public static final int[][] MINUTE_MAP = {
            {0, 0},
            {1, 5},
            {3, 0},
            {4, 5}
    };
    public static final Identifier TEXTURE = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/cpu_config_screen.png");
    public CpuConfigScreen(Text text, NbtCompound nbtCompound, Object o) {

        super(text, nbtCompound, o);
        windowSizeX = 256;
        windowSizeY = 256;
        new GUIButton(5, 13, 14, 22)
                .offSprite(TEXTURE,256, 0, 512, 512, 0xFFFFFFFF)
                .hoverSprite(TEXTURE, 271, 0, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 286, 0, 512, 512, 0xFFFFFFFF)
                .clickExec(this::UpClick);
        new GUIButton(5, 35, 14, 21)
                .offSprite(TEXTURE,256, 22, 512, 512, 0xFFFFFFFF)
                .hoverSprite(TEXTURE, 271, 22, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 286, 22, 512, 512, 0xFFFFFFFF)
                .clickExec(this::ConfirmClick);
        new GUIButton(5, 55, 14, 21)
                .offSprite(TEXTURE,256, 42, 512, 512, 0xFFFFFFFF)
                .hoverSprite(TEXTURE, 271, 42, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 286, 42, 512, 512, 0xFFFFFFFF)
                .clickExec(this::BackClick);
        new GUIButton(5, 75, 14, 22)
                .offSprite(TEXTURE,256, 62, 512, 512, 0xFFFFFFFF)
                .hoverSprite(TEXTURE, 271, 62, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 286, 62, 512, 512, 0xFFFFFFFF)
                .clickExec(this::DownClick);

        new GUIToggle(10, 115, 10, 14, "isActivated")
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 128, 10, 14, "canCrawl")
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 141, 10, 14, "stopOnSeen")
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 154, 10, 14, "stopOnRec")
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 167, 10, 14, "avoidLight")
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 180, 10, 14, "resetByDoor")
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 193, 10, 14, "resetByLight")
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 206, 10, 14, "stunByLight")
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 219, 10, 14, "wanderAround")
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 232, 10, 14, "usePathing")
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);


        new GUIToggle(135, 131, 10, 14, "useTime")
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);
        new GUIToggle(135, 162, 10, 14, "aggressive")
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);
    }

    public void UpClick() {
        FnafUniverseRebuilt.print("UP");
    }
    private void ConfirmClick() {
        FnafUniverseRebuilt.print("CONFIRM");
    }
    private void BackClick() {
        FnafUniverseRebuilt.print("BACK");
    }
    private void DownClick() {
        FnafUniverseRebuilt.print("DOWN");
    }
    private void ToggleClick(GUIToggle button) {
        button.on = !button.on;
        FnafUniverseRebuilt.print(button.setting + ": " + button.on);
    }

    @Override
    public void renderToggle(DrawContext context, double mouseX, double mouseY, GUIToggle button) {

        List<String> left = new ArrayList<>();
        left.add("useTime");
        left.add("aggressive");

        printTape(context, Text.literal(button.setting), button.x, windowY + button.y + 3, 0.75f, left.contains(button.setting));

        super.renderToggle(context, mouseX, mouseY, button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        drawRecolorableTexture(context, TEXTURE, windowX, windowY, 256, 256, 0, 0, 512, 512, 0xFFFFFFFF);

        int timeX = windowX + 130;
        int timeY = windowY + 122;
        boolean pm = time > 47;
        int hour = (time - ((time /48) * 48)) / 4;
        int minute = time - ((time / 4) * 4);
        int hu1 = 256 + (3 * HOUR_MAP[hour][0]);
        int hu2 = 256 + (3 * HOUR_MAP[hour][1]);
        int mu1 = 256 + (3 * MINUTE_MAP[minute][0]);
        int mu2 = 256 + (3 * MINUTE_MAP[minute][1]);

        int amu = pm ? 289 : 286;

        if(HOUR_MAP[hour][0] > 0) drawRecolorableTexture(context, TEXTURE, timeX, timeY, 3, 5, hu1, 122, 512, 512, 0xFFFFFFFF);
        drawRecolorableTexture(context, TEXTURE, timeX + 4, timeY, 3, 5, hu2, 122, 512, 512, 0xFFFFFFFF);

        drawRecolorableTexture(context, TEXTURE, timeX + 8, timeY, 1, 5, 297, 122, 512, 512, 0xFFFFFFFF);

        drawRecolorableTexture(context, TEXTURE, timeX + 10, timeY, 3, 5, mu1, 122, 512, 512, 0xFFFFFFFF);
        drawRecolorableTexture(context, TEXTURE, timeX + 14, timeY, 3, 5, mu2, 122, 512, 512, 0xFFFFFFFF);

        drawRecolorableTexture(context, TEXTURE, timeX + 18, timeY, 3, 5, amu, 122, 512, 512, 0xFFFFFFFF);
        drawRecolorableTexture(context, TEXTURE, timeX + 22, timeY, 5, 5, 292, 122, 512, 512, 0xFFFFFFFF);

        printTape(context, Text.literal("Behavior->"), 136, windowY + 151, 0.75f, true);

        int dialX = windowX + 148;

        int timeDialY = windowY + 131;
        int aggressivityDialY = windowY + 162;
        int speedDialY = windowY + 189;

        int timeDialV = time % 2 == 0 ? 85 : 98;
        int aggressivityDialV = aggressivity % 2 == 0 ? 85 : 98;
        int speedDialV = speed % 2 == 0 ? 85 : 98;

        drawRecolorableTexture(context, TEXTURE, dialX, timeDialY, 11, 13, 270, timeDialV, 512, 512, 0xFFFFFFFF);
        drawRecolorableTexture(context, TEXTURE, dialX, aggressivityDialY, 11, 13, 270, aggressivityDialV, 512, 512, 0xFFFFFFFF);
        drawRecolorableTexture(context, TEXTURE, dialX, speedDialY, 11, 13, 270, speedDialV, 512, 512, 0xFFFFFFFF);

        printTape(context, Text.literal("Time/Event->"), 128, timeY - 2, 0.75f, true);

        printTape(context, Text.literal("speed"), 147, speedDialY + 4, 0.75f, true);
        super.render(context, mouseX, mouseY, delta);

    }

    void printTape(DrawContext context, Text text, int x, int y, float textScale, boolean left){

        int width = (int)((textRenderer.getWidth(text) + 2) * textScale);

        int i = left ? windowX + x - width - 6 : windowX + x + 12;

        drawRecolorableTexture(context, TEXTURE, i, y, 2, 9, 256, 113, 512, 512, 0xFFFFFFFF);
        drawRecolorableTexture(context, TEXTURE, i + 2, y, width, 9, 256 + 4, 113, 512, 512, 0xFFFFFFFF);
        drawRecolorableTexture(context, TEXTURE, i + width + 2, y, 2, 9, 256 + 2, 113, 512, 512, 0xFFFFFFFF);
        //context.drawText(textRenderer, button.setting, x + 3, y+ 1, 0xFF112233, false);
        drawResizableText(context, textRenderer, text, textScale, i + 3, y + 2, 0xFF112233, 0x00000000, false, false);
    }

}
