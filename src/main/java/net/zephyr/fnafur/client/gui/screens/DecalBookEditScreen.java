package net.zephyr.fnafur.client.gui.screens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.init.StickerInit;

import java.util.List;

public class DecalBookEditScreen extends GoopyScreen{

    static final int PER_PAGE = 6;
    static final Identifier TEXTURE = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/decal_book_gui.png");
    int page = 0;
    int pageAmount = 0;

    int cornerX = 0;
    int cornerY = 0;

    public DecalBookEditScreen(Text text, NbtCompound nbtCompound, Object o) {
        super(text, nbtCompound, o);
        for(List<StickerInit.Decal> list : StickerInit.CATEGORIES.values()){
            pageAmount += (list.size() % 6) + 1;
        }
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
        drawRecolorableTexture(context, TEXTURE, cornerX, cornerY,256, 187, 0, 0, 256, 256, 0xFFFFFFFF);

        renderPage(context, this.page, "wall_tiles");
        renderPage(context, this.page + 1, "wall_tiles");

        super.render(context, mouseX, mouseY, delta);
    }

    void renderPage(DrawContext context, int page, String category){
        int xOffset = page % 2 == 0 ? 0 : 129;

        List<StickerInit.Decal> list = StickerInit.CATEGORIES.get(category);

        for(int i = 0; i < PER_PAGE; i++) {
            if (list.size() > (i + (page * PER_PAGE)))
            {
                StickerInit.Decal decal = list.get(i + ((page + 1) * PER_PAGE));

                int x = xOffset + cornerX + 13 + ((i % 2) * 52);
                int y = cornerY + 24 + ((i / 2) * 52);

                String namespace = decal.getTextures()[0].getNamespace();
                String path = "textures/" + decal.getTextures()[0].getPath() + ".png";
                Identifier id = Identifier.of(namespace, path);

                context.fill(RenderLayer.getGuiOverlay(), x - 1, y  - 1, x + 49, y + 49, 0x55000000);
                context.drawTexture((identifier -> RenderLayer.getGuiTexturedOverlay(id)), id, x, y, 0, 0, 48, 48, 48, 48);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
