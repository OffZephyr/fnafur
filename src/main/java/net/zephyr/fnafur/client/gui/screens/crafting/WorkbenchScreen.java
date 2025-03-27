package net.zephyr.fnafur.client.gui.screens.crafting;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import org.joml.Matrix4f;

public class WorkbenchScreen extends GoopyScreen {
    public static final Identifier TEXTURE = Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/workbench.png");
    int tab = 0;
    public WorkbenchScreen(Text text, NbtCompound nbtCompound, Object o) {
        super(text, nbtCompound, o);

        new GUIToggle(windowX + 6, windowY + 6, 56, 19, "endo")
                .offSprite(TEXTURE, 256, 92, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 312, 92, 512, 512, 0xFFFFFFFF)
                .hoverSprite(TEXTURE, 312, 92, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(windowX + 6, windowY + 6 + 23, 56, 19, "suit")
                .offSprite(TEXTURE, 256, 92 + 19, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 312, 92 + 19, 512, 512, 0xFFFFFFFF)
                .hoverSprite(TEXTURE, 312, 92 + 19, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(windowX + 6, windowY + 6 + 23 + 23, 56, 19, "alt")
                .offSprite(TEXTURE, 256, 92 + 19 + 19, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 312, 92 + 19 + 19, 512, 512, 0xFFFFFFFF)
                .hoverSprite(TEXTURE, 312, 92 + 19 + 19, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(windowX + 6, windowY + 6 + 23 + 23 + 23, 56, 19, "eyes")
                .offSprite(TEXTURE, 256, 92 + 19 + 19 + 19, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 312, 92 + 19 + 19 + 19, 512, 512, 0xFFFFFFFF)
                .hoverSprite(TEXTURE, 312, 92 + 19 + 19 + 19, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(windowX + 6, windowY + 6 + 23 + 23 + 23 + 23, 56, 19, "cpu")
                .offSprite(TEXTURE, 256, 92 + 19 + 19 + 19 + 19, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 312, 92 + 19 + 19 + 19 + 19, 512, 512, 0xFFFFFFFF)
                .hoverSprite(TEXTURE, 312, 92 + 19 + 19 + 19 + 19, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);
    }

    private void ToggleClick(GUIToggle button) {
        for (GUIButton b : BUTTONS){
            if(b instanceof GUIToggle t){
                t.on = false;
            }
        }

        int goal = switch (button.setting){
            default -> 0;
            case "endo" -> 1;
            case "suit" -> 2;
            case "alt" -> 3;
            case "eyes" -> 4;
            case "cpu" -> 5;
        };

        button.on = tab != goal;

        tab = button.on ? goal : 0;
        FnafUniverseResuited.print(button.setting);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        drawBackground(context);
        drawRecolorableTexture(context, TEXTURE, windowX, windowY, 256, 256, 0, 0, 512, 512, 0xFFFFFFFF);

        drawRecolorableTexture(context, TEXTURE, windowX + 65, windowY + 6, 19, 19, 256, 187, 512, 512, 0xFFFFFFFF);
        drawRecolorableTexture(context, TEXTURE, windowX + 65, windowY + 6 + 23, 19, 19, 256, 187, 512, 512, 0xFFFFFFFF);
        drawRecolorableTexture(context, TEXTURE, windowX + 65, windowY + 6 + 23 + 23, 19, 19, 256, 187, 512, 512, 0xFFFFFFFF);
        drawRecolorableTexture(context, TEXTURE, windowX + 65, windowY + 6 + 23 + 23 + 23, 19, 19, 256, 187, 512, 512, 0xFFFFFFFF);
        drawRecolorableTexture(context, TEXTURE, windowX + 65, windowY + 6 + 23 + 23 + 23 + 23, 19, 19, 256, 187, 512, 512, 0xFFFFFFFF);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    public void drawBackground(DrawContext context) {
        float u1 = (0 + 0.0f) /512f;
        float u2 = (0 + 412f) / 512f;
        float v1 = (256 + 0.0f) / 512f;
        float v2 =  (256 + 256) / 512f;

        int x1 = 0;
        int y1 = 0;
        int x2 = width;
        int y2 = height;
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX);
        RenderSystem.enableBlend();
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, (float)x1, (float)y1, (float)50).texture(u1, v1);
        bufferBuilder.vertex(matrix4f, (float)x1, (float)y2, (float)50).texture(u1, v2);
        bufferBuilder.vertex(matrix4f, (float)x2, (float)y2, (float)50).texture(u2, v2);
        bufferBuilder.vertex(matrix4f, (float)x2, (float)y1, (float)50).texture(u2, v1);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }
}
