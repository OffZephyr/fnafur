package net.zephyr.fnafur.client.gui.screens.crafting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.cpu_config_panel.CpuConfigPanelBlock;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;

public class CpuConfigScreenBackup extends GoopyScreen {
    int stalk = 0;
    int sight = 12;
    int speed = 2;
    int voice = 0;
    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/cpu_config_screen.png");
    public CpuConfigScreenBackup(Component text, CompoundTag CompoundTag, Object o) {

        super(text, CompoundTag, o);

//        Boolean bl = Minecraft.getInstance().level.getBlockState(getBlockPos()).get(CpuConfigPanelBlock.TOP_CPU);
        Boolean bl = false;

        if(!bl){
            CompoundTag = new CompoundTag();
        }

        windowSizeX = 256;
        windowSizeY = 256;

        CompoundTag cpu = CompoundTag.getCompoundOrEmpty("cpu");

        new GUIToggle(10, 29, 10, 14, false, "canCrawl", cpu.getBooleanOr("canCrawl", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 42, 10, 14, false, "canHear", cpu.getBooleanOr("canHear", bl))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 55, 10, 14, false, "canSee", cpu.getBooleanOr("canSee", bl))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 68, 10, 14, false, "wandersAround", cpu.getBooleanOr("wandersAround", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 81, 10, 14, false, "isAggressive", cpu.getBooleanOr("isAggressive", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 94, 10, 14, false, "killsOnScare", cpu.getBooleanOr("killsOnScare", bl))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(87, 29, 10, 14, false, "shreddyOnKill", cpu.getBooleanOr("shreddyOnKill", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(87, 42, 10, 14, false, "checkHiding", cpu.getBooleanOr("checkHiding", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(87, 55, 10, 14, false, "alertOthers", cpu.getBooleanOr("alertOthers", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(87, 68, 10, 14, false, "followOnSight", cpu.getBooleanOr("followOnSight", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(87, 81, 10, 14, false, "disableLights", cpu.getBooleanOr("disableLights", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(87, 94, 10, 14, false, "ignoreMask", cpu.getBooleanOr("canSwim", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);


        new GUIToggle(10, 202, 10, 14, false, "freezeOnSight", cpu.getBooleanOr("freezeOnSight", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 215, 10, 14, false, "freezeOnCam", cpu.getBooleanOr("freezeOnCam", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(10, 228, 10, 14, false, "avoidLight", cpu.getBooleanOr("avoidLight", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(87, 202, 10, 14, false, "stunnedByLight", cpu.getBooleanOr("stunnedByLight", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(87, 215, 10, 14, false, "resetByLight", cpu.getBooleanOr("resetByLight", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(87, 228, 10, 14, false, "resetByDoor", cpu.getBooleanOr("resetByDoor", false))
                .offSprite(TEXTURE, 256, 85, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 256, 99, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);
    }
    private void ToggleClick(GUIToggle button) {
        button.on = !button.on;
        FnafUniverseRebuilt.print(button.setting + ": " + button.on);
    }

    @Override
    public void renderToggle(GuiGraphicsExtractor context, double mouseX, double mouseY, GUIToggle button) {

        printTape(context, Component.literal(button.setting), button.x, windowY + button.y + 3, 0.75f, button.leftText);

        super.renderToggle(context, mouseX, mouseY, button);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        drawRecolorableTexture(context, TEXTURE, windowX, windowY, 256, 256, 0, 0, 512, 512, 0xFFFFFFFF);

        printTape(context, Component.literal("Behavior"), 2, windowY + 17, 0.85f, false);
        printTape(context, Component.literal("Stats"), 2, windowY + 126, 0.85f, false);
        printTape(context, Component.literal("Weaknesses"), 2, windowY + 190, 0.85f, false);

        renderDial(context, Component.literal("walkSpeed"), windowX + 8, windowY + 142, speed % 2 == 0);
        renderDial(context, Component.literal("voiceID"), windowX + 8, windowY + 156, voice % 2 == 0);

        renderDial(context, Component.literal("stalkLength"), windowX + 85, windowY + 142, stalk % 2 == 0);
        renderDial(context, Component.literal("sightRange"), windowX + 85, windowY + 156, sight % 2 == 0);
        super.extractRenderState(context, mouseX, mouseY, delta);

    }

    public void renderDial(GuiGraphicsExtractor context, Component label, int x, int y, boolean isInverted) {
        int v = isInverted ? 98 : 85;
        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 270, v, 11, 13, 512, 512);

        printTape(context, label, x - windowX + 14, y + 3, 0.75f, false);
    }

    void printTape(GuiGraphicsExtractor context, Component text, int x, int y, float textScale, boolean left){

        int width = (int)((font.width(text) + 2) * textScale);

        int i = left ? windowX + x - width - 6 : windowX + x + 12;

        drawRecolorableTexture(context, TEXTURE, i, y, 2, 9, 256, 113, 512, 512, 0xFFFFFFFF);
        drawRecolorableTexture(context, TEXTURE, i + 2, y, width, 9, 256 + 4, 113, 512, 512, 0xFFFFFFFF);
        drawRecolorableTexture(context, TEXTURE, i + width + 2, y, 2, 9, 256 + 2, 113, 512, 512, 0xFFFFFFFF);
        //context.drawText(textRenderer, button.setting, x + 3, y+ 1, 0xFF112233, false);
        drawResizableText(context, font, text, textScale, i + 3, y + 2, 0xFF112233, 0x00000000, false, false);
    }
    CompoundTag fillCPU(){
        CompoundTag nbt = new CompoundTag();

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
    public void onClose() {
        CompoundTag nbt = new CompoundTag();
        nbt.store("cpu", CompoundTag.CODEC, fillCPU());
        putNbtData(nbt);
        GoopyNetworkingUtils.saveBlockNbt(getBlockPos(), getNbtData(), Minecraft.getInstance().level);

        super.onClose();
    }
}
