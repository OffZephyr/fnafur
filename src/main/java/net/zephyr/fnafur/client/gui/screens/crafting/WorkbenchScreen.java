package net.zephyr.fnafur.client.gui.screens.crafting;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtString;
import net.minecraft.nbt.NbtTypes;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import net.zephyr.fnafur.init.entity_init.EntityInit;
import net.zephyr.fnafur.networking.entity.WorkbenchSaveC2SPayload;
import net.zephyr.fnafur.util.jsonReaders.character_models.CharacterModelManager;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

public class WorkbenchScreen extends GoopyScreen {
    public static final Identifier TEXTURE = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/workbench.png");
    int tab = 0;
    String suit_subtab = "";
    Object[] right_list = new String[0];
    AnimatronicEntity entity;

    int[][] colors;

    String chara = "";
    String alt = "";
    String eyes = "";

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

        new GUIButton(windowX + 6, windowY + 6 + 23 + 23 + 23, 56, 19)
                .offSprite(TEXTURE, 256, 92 + 19 + 19 + 19 + 19, 512, 512, 0xFFFFFFFF)
                .hoverSprite(TEXTURE, 312, 92 + 19 + 19 + 19 + 19, 512, 512, 0xFFFFFFFF)
                .clickExec(this::SaveButton);

        if(!getNbtData().getCompound("GiftData").isEmpty()){
            ((IEntityDataSaver)entity).getPersistentData().put("alt", getNbtData().getCompound("GiftData"));
        }

    }

    private void SaveButton() {
        ClientPlayNetworking.send(new WorkbenchSaveC2SPayload(getBlockPos().up().asLong(), ((IEntityDataSaver)entity).getPersistentData().getCompound("alt")));
        close();
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
            case "save" -> 5;
        };

        suit_subtab = "";

        button.on = tab != goal;

        tab = button.on ? goal : 0;
        FnafUniverseRebuilt.print(button.setting);
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

        if(tab == 2 || tab == 3 || tab == 4) drawRecolorableTexture(context, TEXTURE,  windowX + 246, windowY + 4, 5, 248, 434, 0, 512, 512, 0xFFFFFFFF);

        right_list = new String[0];
        switch (tab){
            case 2: {
                if (suit_subtab.isEmpty()) {

                    String[] list = new String[CharacterModelManager.CHARACTER_CATEGORIES.keySet().toArray().length + 1];
                    list[0] = "<<NONE>>";
                    for(int i = 0; i < CharacterModelManager.CHARACTER_CATEGORIES.keySet().toArray().length; i++){
                        list[i + 1] = (String) CharacterModelManager.CHARACTER_CATEGORIES.keySet().toArray()[i];
                    }
                    right_list = list;

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
                break;
            }
            case 3: {
                if(!this.chara.isEmpty()){
                    Map<String, CharacterModelManager.DataEntry.Alt> alts = CharacterModelManager.CHARA_ALT_MAP.get(CharacterModelManager.ALL_CHARACTERS.get(this.chara));
                    right_list = alts.keySet().toArray();
                    prefix = "entity_alts.fnafur." + this.chara + ".";
                }


                NbtCompound altNbt = ((IEntityDataSaver)entity).getPersistentData().getCompound("alt");

                if(!altNbt.isEmpty() && altNbt.contains("recolorable_textures_size")) {
                    int size = altNbt.getInt("recolorable_textures_size");

                    for (int i = 0; i < size; i++) {

                        int height = 154 / size;
                        int offset = i * (height + 2);

                        int colorWidth = 12;
                        int width = colorWidth + 3;

                        boolean isOn = isOnButton(mouseX, mouseY, windowX + 4, windowY + 252 - height - offset, colorWidth * 4 + 3, height);

                        if(isOn){
                            width = colorWidth * 3 + 3;

                            int redNone = ColorHelper.getArgb(255, 0, colors[i][1], colors[i][2]);
                            int redAll = ColorHelper.getArgb(255, 255, colors[i][1], colors[i][2]);

                            int greenNone = ColorHelper.getArgb(255, colors[i][0], 0, colors[i][2]);
                            int greenAll = ColorHelper.getArgb(255, colors[i][0], 255, colors[i][2]);

                            int blueNone = ColorHelper.getArgb(255, colors[i][0], colors[i][1], 0);
                            int blueAll = ColorHelper.getArgb(255, colors[i][0], colors[i][1], 255);

                            float redHeight = windowY + 248 - ((float) (height - 6) * colors[i][0]/256f) - offset;
                            float greenHeight = windowY + 248 - ((float) (height - 6) * colors[i][1]/256f) - offset;
                            float blueHeight = windowY + 248 - ((float) (height - 6) * colors[i][2]/256f) - offset;

                            context.fill(windowX + 6 + (0 * colorWidth), windowY + 252 - height + 1 - offset, windowX + 6 + (1 * colorWidth) - 1, windowY + 252 - 2 - offset, 0xFFFFFFFF);
                            context.fillGradient(windowX + 6 + (0 * colorWidth) + 1, windowY + 252 - height + 2 - offset, windowX + 6 + (1 * colorWidth) - 2, windowY + 252 - 3 - offset, redAll, redNone);
                            context.fill(windowX + 6 + (0 * colorWidth), (int) redHeight - 1, windowX + 6 + (1 * colorWidth) - 1, (int) redHeight + 2, 0xFFFF0000);

                            context.fill(windowX + 6 + (1 * colorWidth), windowY + 252 - height + 1 - offset, windowX + 6 + (2 * colorWidth) - 1, windowY + 252 - 2 - offset, 0xFFFFFFFF);
                            context.fillGradient(windowX + 6 + (1 * colorWidth) + 1, windowY + 252 - height + 2 - offset, windowX + 6 + (2 * colorWidth) - 2, windowY + 252 - 3 - offset, greenAll, greenNone);
                            context.fill(windowX + 6 + (1 * colorWidth), (int) greenHeight - 1, windowX + 6 + (2 * colorWidth) - 1, (int) greenHeight + 2, 0xFF00FF00);

                            context.fill(windowX + 6 + (2 * colorWidth), windowY + 252 - height + 1 - offset, windowX + 6 + (3 * colorWidth) - 1, windowY + 252 - 2 - offset, 0xFFFFFFFF);
                            context.fillGradient(windowX + 6 + (2 * colorWidth) + 1, windowY + 252 - height + 2 - offset, windowX + 6 + (3 * colorWidth) - 2, windowY + 252 - 3 - offset, blueAll, blueNone);
                            context.fill(windowX + 6 + (2 * colorWidth), (int) blueHeight - 1, windowX + 6 + (3 * colorWidth) - 1, (int) blueHeight + 2, 0xFF0000FF);
                        }
                        else {
                            int color = ColorHelper.getArgb(255, colors[i][0], colors[i][1], colors[i][2]);

                            context.fill(windowX + 6 + (0 * colorWidth), windowY + 252 - height + 1 - offset, windowX + 6 + (1 * colorWidth) - 1, windowY + 252 - 2 - offset, 0xFFFFFFFF);
                            context.fill(windowX + 6 + (0 * colorWidth) + 1, windowY + 252 - height + 2 - offset, windowX + 6 + (1 * colorWidth) - 2, windowY + 252 - 3 - offset, color);
                        }

                        context.fill(windowX + 4, windowY + 251 - offset, windowX + 4 + width, windowY + 252 - offset, 0xFFFFFFFF);
                        context.fill(windowX + 4, windowY + 252 - height - offset, windowX + 5, windowY + 252 - offset, 0xFFFFFFFF);
                        context.fill(windowX + 4 + width - 1, windowY + 252 - height - offset, windowX + 4 + width, windowY + 252 - offset, 0xFFFFFFFF);
                        context.fill(windowX + 4, windowY + 252 - height - 1 - offset, windowX + 4 + width, windowY + 252 - height - offset, 0xFFFFFFFF);
                    }
                }
                break;
            }
            case 4: {

                break;
            }
        }

        for (int i = 0; i < right_list.length; i++) {
            String s = (String) right_list[i];
            Text t = s.contains("<<") ? Text.literal(s) : Text.translatable(prefix + s);
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
                            if(s.equals("<<NONE>>")){

                                this.chara = "";
                                this.alt = "";
                                this.eyes = "";

                                updateEntity();
                                ToggleClick((GUIToggle) BUTTONS.get(0));
                            }
                            else{
                                suit_subtab = s;
                            }
                        }
                        else{
                            if(s.equals("<<BACK>>")){
                                suit_subtab = "";
                            }
                            else {
                                if(CharacterModelManager.CHARA_ALT_MAP.containsKey(CharacterModelManager.ALL_CHARACTERS.get(s))) {
                                    this.chara = s;
                                    this.alt = CharacterModelManager.CHARA_DEFAULT_ALT_MAP.get(CharacterModelManager.ALL_CHARACTERS.get(s));
                                    this.eyes = CharacterModelManager.CHARA_ALT_MAP.get(CharacterModelManager.ALL_CHARACTERS.get(s)).get(this.alt).default_eyes();

                                    int size = CharacterModelManager.CHARA_ALT_MAP.get(CharacterModelManager.ALL_CHARACTERS.get(this.chara)).get(this.alt).recolorable_textures().size();
                                    this.colors = new int[size][3];

                                    for(int j = 0; j < size; j++){
                                        colors[j][0] = 255;
                                        colors[j][1] = 255;
                                        colors[j][2] = 255;
                                    }
                                }
                                else{
                                    this.chara = "";
                                    this.alt = "";
                                    this.eyes = "";
                                }
                                updateEntity();
                            }
                        }
                    }
                }
                break;
            }
            case 3: {
                updateColors(mouseX, mouseY);
                for (int i = 0; i < right_list.length; i++) {

                    String s = (String) right_list[i];
                    Text t = Text.translatable(prefix + s);
                    int sWidth = textRenderer.getWidth(t);
                    int sX = windowX - sWidth + 242;
                    int sY = windowY + 6 + 14 * i;

                    if(isOnButton(mouseX, mouseY, sX - 2, sY - 2, sWidth + 4, 13)) {

                        this.alt = s;

                        int size = CharacterModelManager.CHARA_ALT_MAP.get(CharacterModelManager.ALL_CHARACTERS.get(this.chara)).get(this.alt).recolorable_textures().size();
                        this.colors = new int[size][3];

                        for(int j = 0; j < size; j++){
                            colors[j][0] = 255;
                            colors[j][1] = 255;
                            colors[j][2] = 255;
                        }

                        updateEntity();
                    }
                }
                break;
            }
            case 4: {

                break;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if(tab == 3){
            updateColors(mouseX, mouseY);
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    void updateColors(double mouseX, double mouseY){
        NbtCompound altNbt = ((IEntityDataSaver)entity).getPersistentData().getCompound("alt");

        if(!altNbt.isEmpty() && altNbt.contains("recolorable_textures_size")) {
            int size = altNbt.getInt("recolorable_textures_size");

            for (int i = 0; i < size; i++) {

                int height = 154 / size;
                int offset = i * (height + 2);

                int colorWidth = 12;
                int width = colorWidth * 3 + 3;

                int x = windowX + 4;
                int y = windowY + 252 - height - offset;

                boolean isOn = isOnButton(mouseX, mouseY, x, y, width, height);

                int colorX = (int)(((mouseX - x) / width) * 3);
                double colorY = Math.clamp(1 - ((mouseY - y - 3) / (height - 6)), 0, 1);

                if (isOn) {
                    colors[i][colorX] = (int) (colorY * 255);
                }

                altNbt.putIntArray("color" + i, colors[i]);
            }
        }
    }

    void updateEntity(){
        if(chara.isEmpty()) {
            ((IEntityDataSaver)entity).getPersistentData().put("alt", new NbtCompound());
            return;
        }

        CharacterModelManager.Chara chara = CharacterModelManager.ALL_CHARACTERS.get(this.chara);
        CharacterModelManager.DataEntry.Alt alt = CharacterModelManager.CHARA_ALT_MAP.get(chara).get(this.alt);
        CharacterModelManager.DataEntry.Eyes eyes = alt.eyes().get(this.eyes);

        String model = alt.model();
        String texture = alt.texture();
        List<String> recolorable_textures = alt.recolorable_textures();
        String emissive = alt.emissive();
        List<String> recolorable_emissive = alt.recolorable_emissive();

        String animations = alt.animations();

        String eyes_texture = eyes.texture();
        List<String> eyes_recolorable_textures = eyes.recolorable_textures();
        String eyes_emissive = eyes.emissive();
        List<String> eyes_recolorable_emissive = eyes.recolorable_emissive();



        NbtCompound altNbt = new NbtCompound();
        altNbt.putString("chara", this.chara);
        altNbt.putString("alt", this.alt);
        altNbt.putString("eyes", this.eyes);

        altNbt.putString("model", model);
        altNbt.putString("texture", texture);

        altNbt.putInt("recolorable_textures_size", recolorable_textures.size());
        for(int i = 0; i < recolorable_textures.size(); i++){
            altNbt.putString("recolorable_textures" + i, recolorable_textures.get(i));

            altNbt.putIntArray("color" + i, colors[i]);
        }

        altNbt.putString("emissive", emissive);

        altNbt.putInt("recolorable_emissive_size", recolorable_emissive.size());
        for(int i = 0; i < recolorable_emissive.size(); i++){
            altNbt.putString("recolorable_emissive" + i, recolorable_emissive.get(i));
        }

        altNbt.putString("animations", animations);


        altNbt.putString("eyes_texture", eyes_texture);

        for(int i = 0; i < eyes_recolorable_textures.size(); i++){
            altNbt.putString("eyes_recolorable_textures" + i, eyes_recolorable_textures.get(i));
        }

        altNbt.putString("eyes_emissive", eyes_emissive);

        for(int i = 0; i < eyes_recolorable_emissive.size(); i++){
            altNbt.putString("eyes_recolorable_emissive" + i, eyes_recolorable_emissive.get(i));
        }

        ((IEntityDataSaver)entity).getPersistentData().put("alt", altNbt);
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
