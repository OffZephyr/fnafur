package net.zephyr.fnafur.client.gui.screens.crafting;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import net.zephyr.fnafur.init.entity_init.EntityInit;
import net.zephyr.fnafur.util.jsonReaders.character_models.CharacterModelManager;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class WorkbenchScreen extends GoopyScreen {
    public static final Identifier TEXTURE = Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/workbench.png");
    int tab = 0;
    String suit_subtab = "";
    Object[] right_list = new String[0];
    AnimatronicEntity entity;

    String prefix = "";
    public WorkbenchScreen(Text text, NbtCompound nbtCompound, Object o) {
        super(text, nbtCompound, o);
        entity = EntityInit.ANIMATRONIC.create(this.client.world, SpawnReason.TRIGGERED);
        entity.isMenu = true;

        /*new GUIToggle(windowX + 6, windowY + 6, 56, 19, "endo")
                .offSprite(TEXTURE, 256, 92, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 312, 92, 512, 512, 0xFFFFFFFF)
                .hoverSprite(TEXTURE, 312, 92, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);*/

        new GUIToggle(windowX + 6, windowY + 6, 56, 19, "suit")
                .offSprite(TEXTURE, 256, 92 + 19, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 312, 92 + 19, 512, 512, 0xFFFFFFFF)
                .hoverSprite(TEXTURE, 312, 92 + 19, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(windowX + 6, windowY + 6 + 23, 56, 19, "alt")
                .offSprite(TEXTURE, 256, 92 + 19 + 19, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 312, 92 + 19 + 19, 512, 512, 0xFFFFFFFF)
                .hoverSprite(TEXTURE, 312, 92 + 19 + 19, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        new GUIToggle(windowX + 6, windowY + 6 + 23 + 23, 56, 19, "eyes")
                .offSprite(TEXTURE, 256, 92 + 19 + 19 + 19, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 312, 92 + 19 + 19 + 19, 512, 512, 0xFFFFFFFF)
                .hoverSprite(TEXTURE, 312, 92 + 19 + 19 + 19, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);

        /*new GUIToggle(windowX + 6, windowY + 6 + 23 + 23 + 23 + 23, 56, 19, "cpu")
                .offSprite(TEXTURE, 256, 92 + 19 + 19 + 19 + 19, 512, 512, 0xFFFFFFFF)
                .onSprite(TEXTURE, 312, 92 + 19 + 19 + 19 + 19, 512, 512, 0xFFFFFFFF)
                .hoverSprite(TEXTURE, 312, 92 + 19 + 19 + 19 + 19, 512, 512, 0xFFFFFFFF)
                .toggleExec(this::ToggleClick);*/
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

        drawRecolorableTexture(context, TEXTURE, windowX + 65, windowY + 6, 0, 19, 19, 256, 187, 512, 512, 1.0f, 1.0f, 1.0f, 1.0f);
        drawRecolorableTexture(context, TEXTURE, windowX + 65, windowY + 6 + 23, 0, 19, 19, 256, 187, 512, 512, 1.0f, 1.0f, 1.0f, 1.0f);
        drawRecolorableTexture(context, TEXTURE, windowX + 65, windowY + 6 + 23 + 23, 0, 19, 19, 256, 187, 512, 512, 1.0f, 1.0f, 1.0f, 1.0f);
        //drawRecolorableTexture(context, TEXTURE, windowX + 65, windowY + 6 + 23 + 23 + 23, 0, 19, 19, 256, 187, 512, 512, 1.0f, 1.0f, 1.0f, 1.0f);
        //drawRecolorableTexture(context, TEXTURE, windowX + 65, windowY + 6 + 23 + 23 + 23 + 23, 0, 19, 19, 256, 187, 512, 512, 1.0f, 1.0f, 1.0f, 1.0f);

        if(tab != 2) suit_subtab = "";

        if(tab == 2 || tab == 3 || tab == 4) drawRecolorableTexture(context, TEXTURE,  windowX + 246, windowY + 4, 5, 248, 434, 0, 512, 512, 0xFFFFFFFF);

        right_list = new String[0];
        switch (tab){
            case 2: {
                if (suit_subtab.isEmpty()) {
                    right_list = CharacterModelManager.CHARACTER_CATEGORIES.keySet().toArray();
                    prefix = "entity_category.fnafur.";
                }
                else{
                    List<CharacterModelManager.Chara> charaList = CharacterModelManager.CHARACTER_CATEGORIES.get(suit_subtab);
                    String[] list = new String[charaList.size() + 1];
                    list[0] = "<<BACK>>";
                    for(int i = 0; i < charaList.size(); i++){
                        list[i + 1] = charaList.get(i).name();
                    }
                    right_list = list;
                    prefix = "entity.fnafur.";
                }
            }
            case 3: {

            }
            case 4: {

            }
        }

        for (int i = 0; i < right_list.length; i++) {
            String s = (String) right_list[i];
            Text t = s.equals("<<BACK>>") ? Text.literal(s) : Text.translatable(prefix + s);
            int sWidth = textRenderer.getWidth(t);
            int sX = windowX - sWidth + 242;
            int sY = windowY + 6 + 14 * i;

            context.fill(sX - 2, sY - 2,  sX + sWidth + 2, sY - 1, 0, 0xFFFFFFFF);
            context.fill(sX - 2, sY + 9, sX + sWidth + 2, sY + 10, 0, 0xFFFFFFFF);
            context.fill(sX - 2, sY - 2, sX - 1, sY + 10, 0, 0xFFFFFFFF);
            context.fill(sX + sWidth + 1, sY - 2, sX + sWidth + 2, sY + 10, 0, 0xFFFFFFFF);
            context.drawText(textRenderer, t, sX, sY, 0xFFFFFFFF, false);

            if(isOnButton(mouseX, mouseY, sX - 2, sY - 2, sWidth + 4, 12)){
                context.fill(sX - 2, sY - 2, sX + sWidth + 2, sY + 10, 0x55FFFFFF);
            }

        }

        drawEntity(context, entity, 1.35f * 40, windowX + 128, windowY + 251, 200, new Quaternionf().rotationXYZ((float) 0f, (float) Math.PI, (float) Math.PI));

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        switch (tab) {
            case 2: {

                for (int i = 0; i < right_list.length; i++) {
                    String s = (String) right_list[i];
                    Text t = Text.translatable(prefix + s);
                    int sWidth = textRenderer.getWidth(t);
                    int sX = windowX - sWidth + 242;
                    int sY = windowY + 6 + 14 * i;

                    if(isOnButton(mouseX, mouseY, sX - 2, sY - 2, sWidth + 4, 13)){
                        if (suit_subtab.isEmpty()) {
                            suit_subtab = s;
                        }
                        else{
                            if(s.equals("<<BACK>>")){
                                suit_subtab = "";
                            }
                        }
                    }
                }

            }
            case 3: {

            }
            case 4: {

            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void close() {
        entity.isMenu = false;
        super.close();
    }

    @Override
    public boolean shouldPause() {
        return false;
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
