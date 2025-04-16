package net.zephyr.fnafur.client.gui.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.init.DecalInit;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.ItemNbtUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecalBookEditScreen extends GoopyScreen{

    static final int PER_PAGE = 6;
    static final Identifier TEXTURE = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/decal_book_gui.png");
    int page = 0;
    int pageAmount = 1;

    int cornerX = 0;
    int cornerY = 0;

    Map<String, Integer> categoryMap = new HashMap<>();
    Map<String, Integer> categoryNextMap = new HashMap<>();

    public DecalBookEditScreen(Text text, NbtCompound nbtCompound, Object o) {
        super(text, nbtCompound, o);
        for(int i = 0; i < DecalInit.CATEGORIES.size(); i++){

            String category = DecalInit.CATEGORIES_LIST.get(i);
            List<DecalInit.Decal> list = DecalInit.CATEGORIES.get(category);

            if(pageAmount % 2 != 0) pageAmount++;
            categoryMap.put(category, pageAmount);
            pageAmount += (Math.max(0, (list.size() - 6)) / 6) + 1;
            categoryNextMap.put(category, pageAmount);
        }
        pageAmount--;
    }

    String getCategory(){
        if(this.page >= 2) {
            for(int i = 0; i < DecalInit.CATEGORIES.size(); i++){
                String category = DecalInit.CATEGORIES_LIST.get(i);
                if (this.page <= categoryNextMap.get(category)) {
                    return category;
                }
            }
        }
        return "";
    }
    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void init() {
        cornerX = (width / 2) - 128;
        cornerY = (height / 2) - 94;

        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(RenderLayer.getGuiOverlay(), 0, 0, width, height, 10, 0x66000000);
        context.drawTexture((identifier -> RenderLayer.getGuiTexturedOverlay(TEXTURE)), TEXTURE, cornerX, cornerY, 0, 0, 256, 187, 256, 256);

        String category = getCategory();

        if(!category.isEmpty()) {
            int pageOffset = categoryMap.get(category);
            int page = this.page - pageOffset;

            renderPage(context, page, category, mouseX, mouseY);
            renderPage(context,  page + 1, category, mouseX, mouseY);
        }
        else{
            drawResizableText(context, textRenderer, Text.translatable("decal_book.summary"), 1, cornerX + 13, cornerY + 12, 0xFF554422, 0x00000000, false, false);

            for(int i = 0; i < DecalInit.CATEGORIES.size(); i++){
                category = DecalInit.CATEGORIES_LIST.get(i);
                int y = cornerY + 26 + (i * 10);

                Text text = Text.literal((categoryMap.get(category) + 1) + ": " + Text.translatable("decal.category." + category).getString());

                Style style = text.getStyle().withUnderline(isOnButton(mouseX, mouseY, cornerX + 13, y, textRenderer.getWidth(text), textRenderer.fontHeight));

                drawResizableText(context, textRenderer, text.getWithStyle(style).getFirst(), 1, cornerX + 13, y, 0xFF554422, 0x00000000, false, false);

            }
        }

        int u0 = 54;
        int u1 = 54;
        if(page > 0) {
            if (isOnButton(mouseX, mouseY, cornerX, cornerY + 190, 9, 9)) {
                u0 = 54 + 18;
            } else {
                u0 = 54 + 9;
            }
        }
        if(page < pageAmount) {
            if (isOnButton(mouseX, mouseY, cornerX + 256 - 9, cornerY + 190, 9, 9)) {
                u1 = 54 + 18;
            } else {
                u1 = 54 + 9;
            }
        }

        int u2 = 0;
        int u3 = 0;
        if(page > 0) {
            if (isOnButton(mouseX, mouseY, cornerX + 12, cornerY + 189, 18, 10)) {
                u2 = 36;
            } else {
                u2 = 18;
            }
        }
        if(page < pageAmount) {
            if (isOnButton(mouseX, mouseY, cornerX + 256 - 30, cornerY + 189, 18, 10)) {
                u3 = 36;
            } else {
                u3 = 18;
            }
        }

        context.drawTexture((identifier -> RenderLayer.getGuiTexturedOverlay(TEXTURE)), TEXTURE, cornerX, cornerY + 190, u0, 198, 9, 9, 256, 256);
        context.drawTexture((identifier -> RenderLayer.getGuiTexturedOverlay(TEXTURE)), TEXTURE, cornerX + 12, cornerY + 189, u2, 198, 18, 10, 256, 256);
        context.drawTexture((identifier -> RenderLayer.getGuiTexturedOverlay(TEXTURE)), TEXTURE, cornerX + 256 - 30, cornerY + 189, u3, 188, 18, 10, 256, 256);
        context.drawTexture((identifier -> RenderLayer.getGuiTexturedOverlay(TEXTURE)), TEXTURE, cornerX + 256 - 9, cornerY + 190, u1, 188, 9, 9, 256, 256);

        super.render(context, mouseX, mouseY, delta);
    }

    void renderPage(DrawContext context, int page, String category, int mouseX, int mouseY){
        int xOffset = page % 2 == 0 ? 0 : 129;

        List<DecalInit.Decal> list = DecalInit.CATEGORIES.get(category);

        drawResizableText(context, textRenderer, Text.translatable("decal.category." + category).getWithStyle(Style.EMPTY.withBold(true)).getFirst(), 1, cornerX + 13, cornerY + 12, 0xFF554422, 0x00000000, false, false);

        for(int i = 0; i < PER_PAGE; i++) {
            if (list != null && list.size() > (i + (page * PER_PAGE)))
            {
                DecalInit.Decal decal = list.get(Math.clamp(i + (page * PER_PAGE), 0, list.size() - 1));

                int x = xOffset + cornerX + 13 + ((i % 2) * 52);
                int y = cornerY + 24 + ((i / 2) * 52);

                String namespace = decal.getTextures()[0].getNamespace();
                String path = "textures/" + decal.getTextures()[0].getPath() + ".png";
                Identifier id = Identifier.of(namespace, path);

                int color = isOnButton(mouseX, mouseY, x - 1, y  - 1, 50, 50) ? 0x55AA8877 : 0x55886655;

                context.fill(RenderLayer.getGuiOverlay(), x - 1, y  - 1, x + 49, y + 49, color);
                context.drawTexture((identifier -> RenderLayer.getGuiTexturedOverlay(id)), id, x, y, 0, 0, 48, 48, 48, 48);

            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int add = verticalAmount > 0 ? 1 : -1;
        page += add;
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        if(page > 0) {
            if (isOnButton(mouseX, mouseY, cornerX, cornerY + 190, 9, 9)) {
                page = 0;
            }
        }
        if(page < pageAmount) {
            if (isOnButton(mouseX, mouseY, cornerX + 256 - 9, cornerY + 190, 9, 9)) {
                page = pageAmount;
            }
        }
        if(page > 0) {
            if (isOnButton(mouseX, mouseY, cornerX + 12, cornerY + 189, 18, 10)) {
                page -= 2;
            }
        }
        if(page < pageAmount) {
            if (isOnButton(mouseX, mouseY, cornerX + 256 - 30, cornerY + 189, 18, 10)) {
                page += 2;
            }
        }

        if(this.page == 0) {
            for(int i = 0; i < DecalInit.CATEGORIES.size(); i++){
                String category = DecalInit.CATEGORIES_LIST.get(i);
                int y = cornerY + 26 + (i * 10);

                Text text = Text.literal(categoryMap.get(category) + ": " + Text.translatable("decal.category." + category).getString());

                if(isOnButton(mouseX, mouseY, cornerX + 13, y, textRenderer.getWidth(text), textRenderer.fontHeight)){
                    this.page = categoryMap.get(category);
                }

            }
        }
        else {
            String category = getCategory();
            int pageOffset = categoryMap.get(category);
            int page = this.page - pageOffset;

            for (int j = 0; j < 2; j++) {
                int xOffset = (page + j) % 2 == 0 ? 0 : 129;
                List<DecalInit.Decal> list = DecalInit.CATEGORIES.get(category);

                for (int i = 0; i < PER_PAGE; i++) {
                    if (list != null && list.size() > (i + ((page + j) * PER_PAGE))) {
                        DecalInit.Decal decal = list.get(Math.clamp(i + ((page + j) * PER_PAGE), 0, list.size() - 1));

                        int x = xOffset + cornerX + 13 + ((i % 2) * 52);
                        int y = cornerY + 24 + ((i / 2) * 52);

                        if (isOnButton(mouseX, mouseY, x - 1, y - 1, 50, 50)) {
                            NbtCompound nbt = ItemNbtUtil.getNbt(MinecraftClient.getInstance().player.getMainHandStack());
                            nbt.putString("activeDecal", decal.name());

                            GoopyNetworkingUtils.saveItemNbt(EquipmentSlot.MAINHAND.getName(), nbt);

                            close();
                        }
                    }
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void close() {

        super.close();
    }
}
