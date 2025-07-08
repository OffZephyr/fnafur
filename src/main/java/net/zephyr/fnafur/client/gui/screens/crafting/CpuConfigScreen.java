package net.zephyr.fnafur.client.gui.screens.crafting;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.cpu_config_panel.CpuConfigPanelBlock;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;

public class CpuConfigScreen extends GoopyScreen {
    int stalk = 0;
    int sight = 12;
    int speed = 2;
    int voice = 0;
    public static final Identifier TEXTURE = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/cpu_config_screen.png");
    public CpuConfigScreen(Text text, NbtCompound nbtCompound, Object o) {

        super(text, nbtCompound, o);

        Boolean bl = MinecraftClient.getInstance().world.getBlockState(getBlockPos()).get(CpuConfigPanelBlock.TOP_CPU);

        if(!bl){
            nbtCompound = new NbtCompound();
        }

        windowSizeX = 256;
        windowSizeY = 256;

        NbtCompound cpu = nbtCompound.getCompoundOrEmpty("cpu");

        new GUIToggle(10, 29, 10, 14, false, "canCrawl", cpu.getBoolean("canCrawl", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 42, 10, 14, false, "canHear", cpu.getBoolean("canHear", bl))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 55, 10, 14, false, "canSee", cpu.getBoolean("canSee", bl))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 68, 10, 14, false, "wandersAround", cpu.getBoolean("wandersAround", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 81, 10, 14, false, "isAggressive", cpu.getBoolean("isAggressive", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 94, 10, 14, false, "killsOnScare", cpu.getBoolean("killsOnScare", bl))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(87, 29, 10, 14, false, "shreddyOnKill", cpu.getBoolean("shreddyOnKill", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(87, 42, 10, 14, false, "checkHiding", cpu.getBoolean("checkHiding", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(87, 55, 10, 14, false, "alertOthers", cpu.getBoolean("alertOthers", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(87, 68, 10, 14, false, "followOnSignt", cpu.getBoolean("followOnSignt", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(87, 81, 10, 14, false, "disableLights", cpu.getBoolean("disableLights", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(87, 94, 10, 14, false, "ignoreMask", cpu.getBoolean("canSwim", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);


        new GUIToggle(10, 202, 10, 14, false, "freezeOnSight", cpu.getBoolean("freezeOnSight", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 215, 10, 14, false, "freezeOnCam", cpu.getBoolean("freezeOnCam", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 228, 10, 14, false, "avoidLight", cpu.getBoolean("avoidLight", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(87, 202, 10, 14, false, "stunnedByLight", cpu.getBoolean("stunnedByLight", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(87, 215, 10, 14, false, "resetByLight", cpu.getBoolean("resetByLight", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(87, 228, 10, 14, false, "resetByDoor", cpu.getBoolean("resetByDoor", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);
    }
    private void ToggleClick(GUIToggle button) {
        button.on = !button.on;
        FnafUniverseRebuilt.print(button.setting + ": " + button.on);
    }

    @Override
    public void renderToggle(DrawContext context, double mouseX, double mouseY, GUIToggle button) {

        printTape(context, Text.literal(button.setting), button.x, windowY + button.y + 3, 0.75f, button.leftText);

        super.renderToggle(context, mouseX, mouseY, button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        drawRecolorableTexture(context, TEXTURE, windowX, windowY, 256, 256, 0, 0, 512, 512, 0xFFFFFFFF);

        printTape(context, Text.literal("Behavior"), 2, windowY + 17, 0.85f, false);
        printTape(context, Text.literal("Stats"), 2, windowY + 126, 0.85f, false);
        printTape(context, Text.literal("Weaknesses"), 2, windowY + 190, 0.85f, false);

        renderDial(context, Text.literal("walkSpeed"), windowX + 8, windowY + 142, speed % 2 == 0);
        renderDial(context, Text.literal("voiceID"), windowX + 8, windowY + 156, voice % 2 == 0);

        renderDial(context, Text.literal("stalkLength"), windowX + 85, windowY + 142, stalk % 2 == 0);
        renderDial(context, Text.literal("sightRange"), windowX + 85, windowY + 156, sight % 2 == 0);
        super.render(context, mouseX, mouseY, delta);

    }

    public void renderDial(DrawContext context, Text label, int x, int y, boolean isInverted) {
        int v = isInverted ? 98 : 85;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 270, v, 11, 13, 512, 512);

        printTape(context, label, x - windowX + 14, y + 3, 0.75f, false);
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
    NbtCompound fillCPU(){
        NbtCompound nbt = new NbtCompound();

        for(GUIButton button : BUTTONS) {
            if (button instanceof GUIToggle toggle) {
                nbt.putBoolean(toggle.setting, toggle.on);
            }
        }
        nbt.putInt("walkSpeed", speed);
        nbt.putInt("voiceID", voice);
        nbt.putInt("stalkLength", stalk);
        nbt.putInt("sightRange", sight);

        return nbt;
    }

    @Override
    public void close() {
        NbtCompound nbt = new NbtCompound();
        nbt.put("cpu", NbtCompound.CODEC, fillCPU());
        putNbtData(nbt);
        GoopyNetworkingUtils.saveBlockNbt(getBlockPos(), getNbtData(), MinecraftClient.getInstance().world);

        super.close();
    }
}
