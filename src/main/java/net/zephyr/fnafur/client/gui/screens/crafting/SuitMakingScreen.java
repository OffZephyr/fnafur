package net.zephyr.fnafur.client.gui.screens.crafting;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.StyleSpriteSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import net.zephyr.fnafur.init.entity_init.EntityInit;
import net.zephyr.fnafur.networking.block.DropItemFromWorkbenchC2SPayload;
import net.zephyr.fnafur.util.EasingMathUtil;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.jsonReaders.animatronics.AnimatronicDataHandler;
import org.joml.Quaternionf;

import java.util.List;
import java.util.Objects;

public class SuitMakingScreen extends GoopyScreen {
    public static final Identifier TEXTURE = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/workbench/workbench.png");

    AnimatronicEntity preview;
    AnimatronicEntity icon_preview;

    String previous_category = "";
    String category = "";
    String previous_character = "";
    String character = "";
    String previous_alt = "";
    String alt = "";
    String previous_eyes = "";
    String eyes = "";

    float category_index = 0;
    float alt_index = 0;
    float eyes_index = 0;
    float age = 0;

    float[] shakingLocks;

    boolean show_preview = true;
    float back_offset_index = 1;
    double preview_rotation_x = Math.PI;
    double preview_rotation_y = -(Math.PI/15.0);

    int selectedEye = -1;
    int prevSelectedEye = -1;
    int selectedAlt = -1;
    int prevSelectedAlt = -1;
    int selectedCat = -1;
    int prevSelectedCat = -1;

    boolean needsSave = false;

    public SuitMakingScreen(Text text, NbtCompound nbtCompound, Object o) {
        super(text, nbtCompound, o);
        preview = EntityInit.ANIMATRONIC.create(this.client.world, SpawnReason.TRIGGERED);
        preview.isMenu = true;
        icon_preview = EntityInit.ANIMATRONIC.create(this.client.world, SpawnReason.TRIGGERED);
        icon_preview.isMenu = true;
        shakingLocks = new float[AnimatronicDataHandler.CATEGORIES.size()];


        if(nbtCompound.contains("chara")) {
            this.previous_character = nbtCompound.getString("chara").orElse("");
            this.previous_alt = nbtCompound.getString("alt").orElse("");
            this.previous_eyes = nbtCompound.getString("eyes").orElse("");

            preview.setChara(this.previous_character, this.previous_alt, this.previous_eyes);
        }
    }

    @Override
    public void tick() {
        super.tick();
        icon_preview.tick();
        preview.tick();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        preview.force_age += delta;
        icon_preview.force_age += delta;
        super.render(context, mouseX, mouseY, delta);

        float deltaTicks = delta/20f;

        age += deltaTicks;

        // BACK
        show_preview = this.category.isEmpty();
        back_offset_index += (show_preview ? 1 : -1) * (deltaTicks*2);
        back_offset_index = (float) Math.clamp(back_offset_index, 0, 1);

        // BOTTOM
        int back_x = (int) MathHelper.lerp(EasingMathUtil.easeInOutQuad(back_offset_index), (width / 2f) - 74, (width / 2f) + 55);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, back_x, (height / 2) - 127, 364, 0, 148, 256, 512, 512);

        int preview_x = back_x;
        int preview_y = (height / 2) - 92;

        GoopyScreen.drawEntity(context, preview_x, preview_y, preview_x + 148, preview_y + 221, 80, 0, new Quaternionf().rotationXYZ((float) (preview_rotation_y), (float) (preview_rotation_x), 0), preview, true);

        int save_x = preview_x + 8;
        int export_x = preview_x + 40;
        int close_x = preview_x + 148 - 35;
        int save_y = preview_y - 32;

        int save_v2 = !needsSave || isOnButton(mouseX, mouseY, save_x, save_y, 32, 32) ? 0 : 32;
        int export_v2 = (needsSave || preview.getChara().isEmpty()) || isOnButton(mouseX, mouseY, export_x, save_y, 32, 32) ? 0 : 32;
        int close_v2 = needsSave || isOnButton(mouseX, mouseY, close_x, save_y, 32, 32) ? 64 : 96;

        int color_save = !needsSave ? 0xFF223366 : 0xFFFFFFFF;
        int color_export = needsSave || preview.getChara().isEmpty() ? 0xFF223366 : 0xFFFFFFFF;
        int color_close = needsSave ? 0xFF223366 : 0xFFFFFFFF;

        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, save_x, save_y, 320, save_v2, 32, 32, 512, 512, color_save);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, export_x, save_y, 288, export_v2, 32, 32, 512, 512, color_export);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, close_x, save_y, 320, close_v2, 32, 32, 512, 512, color_close);

        // TOP
        int front_x = (int) MathHelper.lerp(EasingMathUtil.easeInOutQuad(back_offset_index), (width / 2f) - 128, (width / 2f) - 192);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, front_x, (height / 2) - 128, 0, 0, 256, 256, 512, 512);

        drawAltTitle(context, front_x + 128, (height / 2) - 118, "fnafur.screens.workbench.title", 2);
        drawAltTitle(context, front_x + 128, (height / 2) - 96, "fnafur.screens.workbench.title2", 1.25f);

        for(int i = 0; i < AnimatronicDataHandler.CATEGORIES.size(); i++){
            String category = AnimatronicDataHandler.CATEGORIES.get(i);
            if(Objects.equals(category, "default")) continue;

            int padding = 124;
            int x = (front_x + 64) + ((padding)/2) - padding + 1;
            int y = (height / 2) - 128 + 48;

            int uoffset = (i/4) == 0 ? 0 : 2;

            x += (i/4) * padding;
            y += i * 48 - ((i/4) * 192);

            boolean isHovering = isOnButton(mouseX, mouseY, x, y, 124, 48);
            String suffix = isHovering || AnimatronicDataHandler.EMPTY_CATEGORIES.contains(category) ? "_list_select" : "_list";
            Identifier texture = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/workbench/" + category + suffix + ".png");


            boolean empty_category = AnimatronicDataHandler.EMPTY_CATEGORIES.contains(category);
            int color = empty_category ? 0xFF223366 : isHovering ? 0xFFFFFFFF : 0xFFFFFFFF;
            context.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, uoffset, 0, 126, 48, 128, 48, color);
            if(empty_category){
                int lockX = x + 47;
                if(shakingLocks[i] > 0){
                    shakingLocks[i] -= deltaTicks;
                    lockX += (int) (MathHelper.sin(shakingLocks[i] * 20) * (10 * shakingLocks[i]));
                }
                int v = isHovering ? 192 : 224;
                int color2 = isHovering ? 0xFFFFFFFF : 0xFFFFFFFF;
                context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, lockX, y + 8, 256, v, 32, 32, 512, 512, color2);
            }
        }


        // CATEGORY
        category_index += (this.category.isEmpty() ? -1 : 1) * (deltaTicks*2);
        category_index = Math.clamp(category_index, 0, 1);
        // ALTS
        alt_index += (this.character.isEmpty() ? -1 : 1) * (deltaTicks * 2);
        alt_index = Math.clamp(alt_index, 0, 1);
        // EYES
        eyes_index += (this.alt.isEmpty() ? -1 : 1) * (deltaTicks * 2);
        eyes_index = Math.clamp(eyes_index, 0, 1);

        if(!this.previous_category.isEmpty()) {

            if(AnimatronicDataHandler.CHARAS_PER_CATEGORY.get(this.previous_category) != null) {
                renderList(this.previous_category, context, mouseX, mouseY, 3, 2, deltaTicks, AnimatronicDataHandler.CHARAS_PER_CATEGORY.get(this.previous_category), category_index, "entity." + FnafUniverseRebuilt.MOD_ID + ".", this::getMissingCharacter, this::drawCategoryTitle, true);


                if (AnimatronicDataHandler.CHARACTERS.get(this.previous_character) != null) {

                    renderList("entity." + FnafUniverseRebuilt.MOD_ID + "." + this.previous_character, context, mouseX, mouseY, -1, 4, deltaTicks, AnimatronicDataHandler.CHARACTERS.get(this.previous_character).alt_names, alt_index, "entity_alts." + FnafUniverseRebuilt.MOD_ID + "." + previous_character + ".", this::getMissingAlt, this::drawSuitTitle, false);

                    if (AnimatronicDataHandler.CHARACTERS.get(this.previous_character).alt_names.contains(this.previous_alt)) {


                        renderList("entity_alts." + FnafUniverseRebuilt.MOD_ID + "." + previous_character + "." + this.previous_alt, context, mouseX, mouseY, 1, 1, deltaTicks, AnimatronicDataHandler.CHARACTERS.get(this.previous_character).eye_names, eyes_index, "entity_eyes." + FnafUniverseRebuilt.MOD_ID + "." + previous_character + "." + previous_alt + ".", this::getMissingAlt, this::drawEyesTitle, false);
                    }
                }
            }
        }
        //super.render(context, mouseX, mouseY, delta);
    }

    void renderList(String title, DrawContext context, int mouseX, int mouseY, int offsetX, int offsetY, float deltaTicks, List<String> arraylist, float index, String name_prefix, GetMissing missingCheck, DrawTitle drawTitle, boolean show_icons) {

        int front_x = (int) MathHelper.lerp(EasingMathUtil.easeInOutQuad(back_offset_index), (width / 2f) - 128, (width / 2f) - 192);

        int x1 = -256;
        int x2 = (front_x + offsetX);
        int x = (int) MathHelper.lerp(EasingMathUtil.easeOutQuad(index), x1, x2);
        int y = (height / 2) - 128 + offsetY;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, 256, 256, 512, 512);

        // BACK ICON
        int back_v2 = isOnButton(mouseX, mouseY, x + 3, y + 3, 32, 32) ? 64 : 96;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 3, y + 3, 320, back_v2, 32, 32, 512, 512);

        drawTitle.draw(context, x + 128, y + 18, title, 2);
        //context.drawTexture(RenderPipelines.GUI_TEXTURED, category_name, category_x + 64, category_y, 0, 0, 128, 48, 128, 48);

        for (int i = 0; i < arraylist.size(); i++) {
            String list_name = arraylist.get(i);
            boolean missing = missingCheck.isMissing(list_name);
            boolean selected = isSelected(i);

            int list_x = 128 - 96 + x + i % 2 * 111;
            int list_y = 55 + y + (i / 2 * 66);
            int color = missing ? 0xFFAA3344 : selected ? 0xFFFFFFFF : 0xFFDDCCFF;
            drawOutline(context, list_x, list_y, 80, 55, color);
            drawOutline(context, list_x - 3, list_y - 3, 86, 61, color);

            if (selected) {
                GoopyScreen.drawEntity(context, list_x + 1, list_y + 1, list_x + 79, list_y + 55, 80, 1, new Quaternionf().rotationXYZ(0, (float) Math.PI, 0), icon_preview);
            }

            int offset = selected ? -20 : 0;
            StyleSpriteSource spriteFont = new StyleSpriteSource.Font(Identifier.of(FnafUniverseRebuilt.MOD_ID, "lemon_bold"));
            Style style = Style.EMPTY.withStrikethrough(missing).withFont(spriteFont);
            Text text = Text.translatable(name_prefix + list_name).setStyle(style);
            int textWidth = textRenderer.getWidth(text);

            boolean textVisible = !selected;
            if(show_icons && !missing){
                float size = 0.5f;
                int icon_y = (int) (list_y + 55 - (32 * size));
                boolean isOn = selected && isOnButton(mouseX, mouseY, list_x, icon_y - (int) (32 * size), (int) (32 * size), (int) (64 * size));

                int iconU = 256;
                int altU = 256 + 32;
                int eyeU = 256 + 64;
                int V = isOn ? 128 : 128 + 32;

                if(isOn){
                    context.fill(list_x + 1, list_y + 1, list_x + 80, list_y + 55, 0x66556699);
                    textVisible = true;
                    context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, list_x, icon_y - (int) (32 * size), altU * size, V * size, (int) (32 * size), (int) (32 * size), (int) (512 * size), (int) (512 * size));
                    context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, list_x, icon_y, eyeU * size, V * size, (int) (32 * size), (int) (32 * size), (int) (512 * size), (int) (512 * size));

                    AnimatronicDataHandler.Chara chara = AnimatronicDataHandler.CHARACTERS.get(list_name);
                    int altCount = chara.ALTS.size();
                    int eyeCount = chara.EYE_ALTS.size();
                    Text alts_text = altCount == 1 ? Text.translatable("fnafur.screens.workbench.alt", altCount).setStyle(style) : Text.translatable("fnafur.screens.workbench.alts", altCount).setStyle(style);
                    Text eyes_text = Text.translatable("fnafur.screens.workbench.eyes", eyeCount).setStyle(style);

                    context.getMatrices().pushMatrix();
                    context.getMatrices().translate(list_x + (int) (34 * size), icon_y);
                    context.drawText(textRenderer, eyes_text,0, 4, 0xFFFFFFFF, false);
                    context.getMatrices().translate(0, - (int) (32 * size));
                    context.drawText(textRenderer, alts_text,0, 4, 0xFFFFFFFF, false);
                    context.getMatrices().popMatrix();
                }
                else{
                    context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, list_x, icon_y, iconU * size, 128 * size, (int) (32 * size), (int) (32 * size), (int) (512 * size), (int) (512 * size), color);
                }

            }
            if(textVisible){
                context.getMatrices().pushMatrix();
                context.getMatrices().translate(list_x + 41, offset + list_y + 27);
                if (textWidth > 76) {
                    float diff = (1f / textWidth) * 76;
                    context.getMatrices().scale(diff);
                }
                context.drawText(textRenderer, text, -(textWidth / 2), -4, color, false);
                context.getMatrices().popMatrix();
            }
        }
    }

    void drawCategoryTitle(DrawContext context, int x, int y, String extra, float scale){
        Identifier category_name = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/workbench/" + extra + "_list_select.png");
        context.drawTexture(RenderPipelines.GUI_TEXTURED, category_name, x - 64, y - 16, 0, 0, 128, 48, 128, 48);

        float size = 1f;
        int U = 256;
        //int y2 = 234 - (int) (32 * size);
        int y2 = 6 - (int) (16 * size);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 124 - (int) (32 * size), y + y2, U * size, 128 * size, (int) (32 * size), (int) (32 * size), (int) (512 * size), (int) (512 * size));
    }

    void drawSuitTitle(DrawContext context, int x, int y, String extra, float scale) {

        float size = 1f;
        int U = 256 + 32;
        //int y2 = 234 - (int) (32 * size);
        int y2 = 2 - (int) (16 * size);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 124 - (int) (32 * size), y + y2, U * size, 128 * size, (int) (32 * size), (int) (32 * size), (int) (512 * size), (int) (512 * size));
        drawAltTitle(context, x, y, extra, scale);
    }
    void drawEyesTitle(DrawContext context, int x, int y, String extra, float scale) {

        float size = 1f;
        int U = 256 + 64;
        //int y2 = 234 - (int) (32 * size);
        int y2 = 2 - (int) (16 * size);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 124 - (int) (32 * size), y + y2, U * size, 128 * size, (int) (32 * size), (int) (32 * size), (int) (512 * size), (int) (512 * size));
        drawAltTitle(context, x, y, extra, scale);
    }
    void drawAltTitle(DrawContext context, int x, int y, String extra, float scale){

        StyleSpriteSource spriteFont = new StyleSpriteSource.Font(Identifier.of(FnafUniverseRebuilt.MOD_ID, "lemon_bold"));
        Style style = Style.EMPTY.withFont(spriteFont);
        Text text = Text.translatable(extra).setStyle(style);

        context.getMatrices().pushMatrix();
        context.getMatrices().translate(x, y);
        context.getMatrices().translate(-(textRenderer.getWidth(text)/2f)*scale, 0);
        context.getMatrices().scale(scale);
        context.drawText(textRenderer, text, 0, 0, 0xFFFFFFFF, false);
        context.getMatrices().popMatrix();
    }

    boolean isSelected(int i){
        if(this.character.isEmpty()){
            return selectedCat == i;
        }
        else if(this.alt.isEmpty()){
            return selectedAlt == i;
        }
        else if(this.eyes.isEmpty()){
            return selectedEye == i;
        }
        return false;
    }

    boolean getMissingCharacter(String chara){
        return AnimatronicDataHandler.MISSING_CHARACTERS.contains(chara);
    }

    boolean getMissingAlt(String alt){
        return false;
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        int front_x = (int) MathHelper.lerp(EasingMathUtil.easeInOutQuad(back_offset_index), (width / 2f) - 128, (width / 2f) - 192);

        if(category.isEmpty()) {
            for (int i = 0; i < AnimatronicDataHandler.CATEGORIES.size(); i++) {
                String category = AnimatronicDataHandler.CATEGORIES.get(i);

                if (Objects.equals(category, "default")) continue;

                int padding = 124;
                int x = (front_x + 64) + ((padding)/2) - padding + 1;
                int y = (height / 2) - 128 + 48;

                x += (i/4) * padding;
                y += i * 48 - ((i/4) * 192);

                if (isOnButton(click.x(), click.y(), x, y, 126, 48)) {
                    if(AnimatronicDataHandler.EMPTY_CATEGORIES.contains(category)) {
                        shakingLocks[i] = 0.75f;
                        continue;
                    }
                    this.category = category;
                    this.previous_category = category;
                }
            }

            int back_x = (int) MathHelper.lerp(EasingMathUtil.easeInOutQuad(back_offset_index), (width / 2f) - 74, (width / 2f) + 55);
            int preview_x = back_x;
            int preview_y = (height / 2) - 92;

            int save_x = preview_x + 8;
            int export_x = preview_x + 40;
            int close_x = preview_x + 148 - 35;
            int save_y = preview_y - 32;
            if(needsSave){
                if(isOnButton(click.x(), click.y(), save_x, save_y, 32, 32)){
                    needsSave = false;
                    getNbtData().putString("chara", preview.getChara());
                    getNbtData().putString("alt", preview.getAlt());
                    getNbtData().putString("eyes", preview.getEyes());

                    GoopyNetworkingUtils.saveBlockNbt(getBlockPos(), getNbtData(), MinecraftClient.getInstance().world);
                }
            }
            else{
                if(!preview.getChara().isEmpty()){
                    if(isOnButton(click.x(), click.y(), export_x, save_y, 32, 32)){
                        getNbtData().remove("chara");
                        getNbtData().remove("alt");
                        getNbtData().remove("eyes");

                        GoopyNetworkingUtils.saveBlockNbt(getBlockPos(), getNbtData(), MinecraftClient.getInstance().world);
                        ClientPlayNetworking.send(new DropItemFromWorkbenchC2SPayload(getBlockPos().asLong(), preview.getChara(), preview.getAlt(), preview.getEyes()));
                        close();
                    }
                }
                if(isOnButton(click.x(), click.y(), close_x, save_y, 32, 32)){
                    close();
                }
            }
        }
        else {
            int offsetX = 3;
            int offsetY = 2;
            if (this.character.isEmpty()) {
                offsetX = 3;
                offsetY = 2;
            } else if (this.alt.isEmpty()) {
                offsetX = -1;
                offsetY = 4;
            } else if (this.eyes.isEmpty()) {
                offsetX = 1;
                offsetY = 1;
            }
            int category_x = front_x + offsetX;
            int category_y = (height / 2) - 128 + offsetY;

            if (isOnButton(click.x(), click.y(), category_x + offsetX, category_y + offsetY, 32, 32)) {
                if (this.character.isEmpty()) {
                    this.previous_category = category;
                    this.category = "";
                } else if (this.alt.isEmpty()) {
                    this.previous_character = character;
                    this.character = "";
                } else if (this.eyes.isEmpty()) {
                    this.previous_alt = alt;
                    this.alt = "";
                }
            }

            if (this.character.isEmpty()) {
                if (selectedCat >= 0) {
                    String chara_name = AnimatronicDataHandler.CHARAS_PER_CATEGORY.get(this.previous_category).get(selectedCat);
                    if (!AnimatronicDataHandler.MISSING_CHARACTERS.contains(chara_name)) {
                        int x = 128 - 96 + category_x + selectedCat % 2 * 111;
                        int y = 55 + category_y + (selectedCat / 2 * 66);
                        if (isOnButton(click.x(), click.y(), x - 3, y - 3, 86, 61)) {
                            this.character = chara_name;
                            this.previous_character = character;
                        }
                    }
                }
            } else if (this.alt.isEmpty()) {
                AnimatronicDataHandler.Chara chara = AnimatronicDataHandler.CHARACTERS.get(this.previous_character);
                if (selectedAlt >= 0) {
                    String alt_name = chara.alt_names.get(selectedAlt);
                    int x = 128 - 96 + category_x + selectedAlt % 2 * 111;
                    int y = 55 + category_y + (selectedAlt / 2 * 66);
                    if (isOnButton(click.x(), click.y(), x - 3, y - 3, 86, 61)) {
                        this.alt = alt_name;
                        this.previous_alt = alt;
                    }
                }
            } else if (this.eyes.isEmpty()) {
                AnimatronicDataHandler.Chara chara = AnimatronicDataHandler.CHARACTERS.get(this.previous_character);
                if (selectedEye >= 0) {
                    String eye_name = chara.eye_names.get(selectedEye);
                    int x = 128 - 96 + category_x + selectedEye % 2 * 111;
                    int y = 55 + category_y + (selectedEye / 2 * 66);
                    if (isOnButton(click.x(), click.y(), x - 3, y - 3, 86, 61)) {
                        this.eyes = eye_name;
                        this.previous_eyes = this.eyes;
                        preview.setChara(this.previous_character, this.previous_alt, this.previous_eyes);
                        this.category = "";
                        this.character = "";
                        this.alt = "";
                        this.eyes = "";
                        needsSave = true;
                    }
                }
            }
        }

        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        int back_x = (int) MathHelper.lerp(EasingMathUtil.easeInOutQuad(back_offset_index), (width / 2f) - 74, (width / 2f) + 55);
        int preview_x = back_x;
        int preview_y = (height / 2) - 92;

        if(isOnButton(click.x(), click.y(), preview_x, preview_y, 148, 221)){
            if(this.category.isEmpty()){
                preview_rotation_x += (offsetX/50.0);
                preview_rotation_y += (-offsetY/75.0);
                preview_rotation_y = MathHelper.clamp(preview_rotation_y, -(Math.PI/7.0), (Math.PI/7.0));
            }
        }
        return super.mouseDragged(click, offsetX, offsetY);
    }

    @Override
    public boolean mouseReleased(Click click) {
        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if(!category.isEmpty()) {
            int offsetX = 3;
            int offsetY = 2;
            if (this.character.isEmpty()) {
                offsetX = 3;
                offsetY = 2;
            } else if (this.alt.isEmpty()) {
                offsetX = -1;
                offsetY = 4;
            } else if (this.eyes.isEmpty()) {
                offsetX = 1;
                offsetY = 1;
            }
            int front_x = (int) MathHelper.lerp(EasingMathUtil.easeInOutQuad(back_offset_index), (width / 2f) - 128, (width / 2f) - 192);

            int category_x = front_x + offsetX;
            int category_y = (height / 2) - 128 + offsetY;

            selectedCat = -1;
            selectedAlt = -1;
            selectedEye = -1;

            boolean moving = back_offset_index != 0;
            if (this.character.isEmpty() && AnimatronicDataHandler.CHARAS_PER_CATEGORY.get(this.previous_category) != null) {
                for (int i = 0; i < AnimatronicDataHandler.CHARAS_PER_CATEGORY.get(this.previous_category).size(); i++) {
                    String chara_name = AnimatronicDataHandler.CHARAS_PER_CATEGORY.get(this.previous_category).get(i);
                    boolean missing = AnimatronicDataHandler.MISSING_CHARACTERS.contains(chara_name);
                    int x = 128 - 96 + category_x + i % 2 * 111;
                    int y = 55 + category_y + (i / 2 * 66);
                    if (!moving && !missing && isOnButton(mouseX, mouseY, x - 3, y - 3, 86, 61)) {
                        selectedCat = i;
                        prevSelectedCat = i;

                        icon_preview.setChara(chara_name, null, null);
                    }

                }
            }
            else if(this.alt.isEmpty() && AnimatronicDataHandler.CHARACTERS.get(this.previous_character) != null) {
                for (int i = 0; i < AnimatronicDataHandler.CHARACTERS.get(this.previous_character).ALTS.size(); i++) {
                    String alt_name = (String)AnimatronicDataHandler.CHARACTERS.get(this.previous_character).ALTS.keySet().toArray()[i];
                    int x = 128 - 96 + category_x + i % 2 * 111;
                    int y = 55 + category_y + (i / 2 * 66);
                    if (!moving && isOnButton(mouseX, mouseY, x - 3, y - 3, 86, 61)) {
                        selectedAlt = i;
                        prevSelectedAlt = i;

                        icon_preview.setChara(this.previous_character, alt_name, null);
                    }

                }
            }
            else if(this.eyes.isEmpty() && AnimatronicDataHandler.CHARACTERS.get(this.previous_character).ALTS.get(this.previous_alt) != null) {
                for (int i = 0; i < AnimatronicDataHandler.CHARACTERS.get(this.previous_character).EYE_ALTS.size(); i++) {
                    String eyes_name = (String)AnimatronicDataHandler.CHARACTERS.get(this.previous_character).EYE_ALTS.keySet().toArray()[i];
                    int x = 128 - 96 + category_x + i % 2 * 111;
                    int y = 55 + category_y + (i / 2 * 66);
                    if (!moving && isOnButton(mouseX, mouseY, x - 3, y - 3, 86, 61)) {
                        selectedEye = i;
                        prevSelectedEye = i;

                        icon_preview.setChara(this.previous_character, this.previous_alt, eyes_name);
                    }

                }
            }
        }
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return !needsSave;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @FunctionalInterface
    public interface DrawTitle {
        void draw(DrawContext context, int x, int y, String extra, float scale);
    }
    @FunctionalInterface
    public interface GetMissing {
        boolean isMissing(String name);
    }
}
