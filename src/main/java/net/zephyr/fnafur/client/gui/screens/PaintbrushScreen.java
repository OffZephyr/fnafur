package net.zephyr.fnafur.client.gui.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.jsonReaders.layered_block.LayeredBlockLayer;
import net.zephyr.fnafur.util.jsonReaders.layered_block.LayeredBlockManager;
import net.zephyr.fnafur.util.mixinAccessing.IGetClientManagers;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.Objects;

public class PaintbrushScreen extends GoopyScreen {
    private final LayeredBlockManager layerManager;
    Identifier texture = Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/paintbrush.png");
    int cornerX, cornerY;
    int selectedLayer, direction;
    float transOff;
    float[] transColor;
    int selectedRow;
    int layerRows;
    boolean holding;
    boolean holdingScroll;
    double exMouseY = 0;
    double scrollBarY = 0;
    double[][] colorScrollX = new double[3][3];
    boolean[][] colorHoldingScroll = new boolean[3][3];

    public PaintbrushScreen(Text text, NbtCompound nbtCompound, Object o) {
        super(text, nbtCompound, o);
        layerManager = ((IGetClientManagers)MinecraftClient.getInstance()).getLayerManager();
    }

    @Override
    protected void init() {

        holding = false;
        direction = getNbtData().getByte("editSide");
        selectedLayer = 0;
        transOff = 0;
        selectedRow = 0;
        scrollBarY = 0;
        transColor = new float[]{0, 0, 0};

        cornerX = this.width / 2 - 118 / 2;
        cornerY = this.height / 2 - 176 / 2;

        if (layerManager.getLayers().size() > 28)
            layerRows = ((layerManager.getLayers().size() + 3) / 4) - 7;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.holding = true;
        for (int i = 0; i < 3; i++) {
            int posX = cornerX + 8 + i * (36);
            int posY = cornerY + 25;

            if (mouseY > posY && mouseY < posY + 30 && mouseX > posX && mouseX < posX + 30) {
                float pitch = selectedLayer == i + 1 ? 0.85f : 1f;
                MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, pitch);
                selectedLayer = selectedLayer == i + 1 ? 0 : i + 1;
                selectedRow = 0;
                scrollBarY = 0;
                updateColorScroll();
            }
        }

        if (selectedLayer > 0 && transOff > 102) {
            int xCorner = cornerX - (int) transOff;
            int yCorner = cornerY + 13;
            for (int k = 0; k < 28; k++) {
                int multiplier = k / 4;
                int iconXPos = (20 * k) - (multiplier * 80);
                int iconYPos = 20 * multiplier;
                if (k + (4 * selectedRow) < layerManager.getLayers().size() && mouseX > xCorner + 5 + iconXPos && (mouseX < xCorner + 25 + iconXPos && mouseY > yCorner + 5 + iconYPos && mouseY < yCorner + 25 + iconYPos)) {
                    String comp = getNbtData().getCompound("layer" + (selectedLayer - 1)).getString("" + direction);
                    LayeredBlockLayer checkingLayer = layerManager.getLayers().get(k + (4 * selectedRow));

                    float pitch = Objects.equals(checkingLayer.getName(), comp) ? 0.85f : 1f;
                    MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, pitch);

                    if (Objects.equals(checkingLayer.getName(), comp)) {
                        getNbtData().getCompound("layer" + (selectedLayer - 1)).putString("" + direction, "");
                    } else {
                        if (getNbtData().getCompound("layer" + (selectedLayer - 1)).isEmpty()) {
                            NbtCompound layer = grayScale(k + (4 * selectedRow));
                            getNbtData().put("layer" + (selectedLayer - 1), layer);
                        } else if (Objects.equals(getNbtData().getCompound("layer" + (selectedLayer - 1)).getString("" + direction), "")){
                            NbtCompound layer = getNbtData().getCompound("layer" + (selectedLayer - 1));
                            for (int i = 0; i < layerManager.getLayers().get(k).getRgbCount(); i++) {
                                float grayMultiplier = (1f / 3) * i;
                                float gray = Math.lerp(255, 76f, grayMultiplier);
                                int color = ColorHelper.Argb.getArgb(255, (int)gray, (int)gray, (int)gray);
                                layer.putInt(direction + "_" + i + "_color", color);
                            }
                            layer.putString("" + direction, layerManager.getLayers().get(k + (4 * selectedRow)).getName());
                        } else {
                            getNbtData().getCompound("layer" + (selectedLayer - 1)).putString("" + direction, layerManager.getLayers().get(k + (4 * selectedRow)).getName());
                        }
                    }
                    updateColorScroll();
                }
            }


            float scrollMultiplier = 1f / layerRows * selectedRow;
            float scrollHeight = Math.lerp(0, 123, scrollMultiplier) + (float) scrollBarY;
            if (mouseX > xCorner + 88 && mouseX < xCorner + 100 && mouseY > yCorner + 6 + (int) scrollBarY && mouseY < yCorner + 21 + (int) scrollBarY && this.holding && layerRows != 0) {

                exMouseY = mouseY;
                this.holdingScroll = true;
            }

            sliderCheck(mouseX, mouseY);
        }

        boolean rotatesCheck = getNbtData().getBoolean("rotates");
        if(mouseX > cornerX + 7 && mouseX < cornerX + 19 && mouseY > cornerY + 7 && mouseY < cornerY + 19){
            float pitch = rotatesCheck ? 0.85f : 1f;
            MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, pitch);

            getNbtData().putBoolean("rotates", !rotatesCheck);
        }
        if(mouseX > cornerX + 99 && mouseX < cornerX + 111 && mouseY > cornerY + 7 && mouseY < cornerY + 19){
            MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1f);
            MinecraftClient.getInstance().player.playSound(SoundEvents.ITEM_GLOW_INK_SAC_USE, 0.5f, 1f);
            saveData();
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    void saveData(){
        GoopyNetworkingUtils.saveBlockNbt(getBlockPos(), getNbtData());
        close();
    }

    void updateColorScroll() {
        for (int i = 0; i < 3; i++) {
            NbtCompound layerData = getNbtData().getCompound("layer" + (selectedLayer - 1));
            int color = layerData.getInt(direction + "_" + i + "_color");
            int r = ColorHelper.Argb.getRed(color);
            int g = ColorHelper.Argb.getGreen(color);
            int b = ColorHelper.Argb.getBlue(color);
            r = java.lang.Math.clamp(r, 0, 255);
            g = java.lang.Math.clamp(g, 0, 255);
            b = java.lang.Math.clamp(b, 0, 255);

            float[] hsv = Color.RGBtoHSB(r, g, b, null);
            float hue = hsv[0];
            float sat = hsv[1];
            float val = hsv[2];
            colorScrollX[i][0] = Math.lerp(0, 42, hue);
            colorScrollX[i][1] = Math.lerp(0, 42, sat);
            colorScrollX[i][2] = Math.lerp(0, 42, val);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        sliderCheck(mouseX, mouseY);

        if (this.holdingScroll) {
            scrollBarY += deltaY;
            scrollBarY = scrollBarY < 0 ? 0 : scrollBarY;
            scrollBarY = scrollBarY > 123 ? 123 : scrollBarY;
            if (scrollBarY > ((123f / layerRows) * selectedRow) + ((123f / layerRows) / 2)) selectedRow++;
            if (scrollBarY < ((123f / layerRows) * selectedRow) - ((123f / layerRows) / 2)) selectedRow--;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    private void sliderCheck(double mouseX, double mouseY){
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int xCorner2 = cornerX + 118 + 3;
                int yCorner2 = cornerY + 8 + ((49 + 6) * i) + 5 + (j * 9);
                if (isOnButton(mouseX, mouseY, xCorner2, yCorner2, 48, 7)) {
                    colorScrollX[i][j] = MathHelper.clamp(mouseX - xCorner2, 0, 42);

                    float hueIndex = (float)colorScrollX[i][0]/42;
                    float satIndex = (float)colorScrollX[i][1]/42;
                    float valIndex = (float)colorScrollX[i][2]/42;
                    float hue = Math.lerp(0, 1, hueIndex);
                    float sat = Math.lerp(0, 1, satIndex);
                    float val = Math.lerp(0, 1, valIndex);
                    int color = Color.HSBtoRGB(hue, sat, val);

                    NbtCompound layer = getNbtData().getCompound("layer" + (selectedLayer-1));
                    {
                        layer.putFloat(direction + "_" + i + "_color", color);
                    }
                }
            }
        }
    }

    @NotNull
    private NbtCompound grayScale(int k) {
        NbtCompound layer = new NbtCompound();
        layer.putString("" + direction, layerManager.getLayers().get(k).getName());
        for (int i = 0; i < layerManager.getLayers().get(k).getRgbCount(); i++) {
            float grayMultiplier = (1f / 3) * i;
            float gray = Math.lerp(255, 76f, grayMultiplier);
            int color = ColorHelper.Argb.getArgb(255, (int)gray, (int)gray, (int)gray);
            layer.putInt(direction + "_" + i + "_color", color);
        }
        return layer;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        holding = false;
        this.holdingScroll = false;
        for (boolean[] list : colorHoldingScroll){
            for(int i = 0; i < 3 ; i++){
                list[i] = false;
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    void closeColorWindow(int i) {
        float timeMultiplier = 4;
        if (transColor[i] > 0) transColor[i] -= transColor[i] / timeMultiplier;
        if (transColor[i] < 1) transColor[i] = 0;
    }
    void colorGradientSide(DrawContext context, VertexConsumer vertexConsumer, int startX, int startY, int endX, int endY, int z, int colorStart, int colorEnd){
        float f = (float)ColorHelper.Argb.getAlpha(colorStart) / 255.0f;
        float g = (float)ColorHelper.Argb.getRed(colorStart) / 255.0f;
        float h = (float)ColorHelper.Argb.getGreen(colorStart) / 255.0f;
        float i = (float)ColorHelper.Argb.getBlue(colorStart) / 255.0f;
        float j = (float)ColorHelper.Argb.getAlpha(colorEnd) / 255.0f;
        float k = (float)ColorHelper.Argb.getRed(colorEnd) / 255.0f;
        float l = (float)ColorHelper.Argb.getGreen(colorEnd) / 255.0f;
        float m = (float)ColorHelper.Argb.getBlue(colorEnd) / 255.0f;
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();

        vertexConsumer.vertex(matrix4f, startX, startY, z).color(g, h, i, f);
        vertexConsumer.vertex(matrix4f, startX, endY, z).color(g, h, i, f);
        vertexConsumer.vertex(matrix4f, endX, endY, z).color(k, l, m, j);
        vertexConsumer.vertex(matrix4f, endX, startY, z).color(k, l, m, j);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        float timeMultiplier = 4;
        if (selectedLayer > 0) {
            if (transOff < 103) transOff += (103 - transOff) / timeMultiplier;
            if (transOff > 102) transOff = 103;
        } else {
            if (transOff > 0) transOff -= transOff / timeMultiplier;
            if (transOff < 1) transOff = 0;
        }

        if (selectedLayer > 0 && layerManager.getLayers().get(selectedLayer - 1).getRgbCount() > 0) {
            for (int i = 0; i < 3; i++) {
                String comp = getNbtData().getCompound("layer" + (selectedLayer - 1)).getString("" + direction);
                LayeredBlockLayer layer = layerManager.getLayer(comp);
                if (layer != null) {
                    if (Objects.equals(layer.getName(), comp)) {
                        if (i < layerManager.getLayer(comp).getRgbCount()) {
                            if (transColor[i] < 55) transColor[i] += (55 - transColor[i]) / timeMultiplier;
                            if (transColor[i] > 54) transColor[i] = 55;
                        } else {
                            closeColorWindow(i);
                        }
                    } else if (getNbtData().getCompound("layer" + (selectedLayer - 1)).isEmpty() || Objects.equals(layer.getName(), "")) {
                        closeColorWindow(i);
                    }
                }
                else {
                    closeColorWindow(i);
                }
            }
        } else {
            for (int i = 0; i < 3; i++) {
                closeColorWindow(i);
            }
        }

        if (selectedLayer > 0) {
            if (transOff < 103) transOff += (103 - transOff) / timeMultiplier;
            if (transOff > 102) transOff = 103;
        } else {
            if (transOff > 0) transOff -= transOff / timeMultiplier;
            if (transOff < 1) transOff = 0;
        }

        for (int i = 0; i < 3; i++) {
            if (transColor[i] > 0) {
                int xCorner = cornerX + 118;
                int yCorner = cornerY + 8 + ((49 + 6) * i);
                context.drawTexture(texture, xCorner, yCorner, 75 - (int) transColor[i], 176, (int) transColor[i], 49, 256, 256);

                NbtCompound layerData = getNbtData().getCompound("layer" + (selectedLayer - 1));
                int color = layerData.getInt(direction + "_" + i + "_color");
                int r = ColorHelper.Argb.getRed(color);
                int g = ColorHelper.Argb.getGreen(color);
                int b = ColorHelper.Argb.getBlue(color);
                r = java.lang.Math.min(r, 255);
                g = java.lang.Math.min(g, 255);
                b = java.lang.Math.min(b, 255);
                context.fill(xCorner + 3 - 55 + (int) transColor[i], yCorner + 33, xCorner + 49 - 55 + (int) transColor[i], yCorner + 43, ColorHelper.Argb.getArgb(255, (int) r, (int) g, (int) b));


                float[] hsv = Color.RGBtoHSB(r, g, b, null);
                float hue = hsv[0];
                float sat = hsv[1];
                float val = hsv[2];

                int val1 = Color.HSBtoRGB(hue, sat, 0);
                int val2 = Color.HSBtoRGB(hue, sat, 1);
                int sat1 = Color.HSBtoRGB(hue, 0, val);
                int sat2 = Color.HSBtoRGB(hue, 1, val);

                colorGradientSide(context, context.getVertexConsumers().getBuffer(RenderLayer.getGui()), xCorner + 3 - 55 + (int) transColor[i], yCorner + 24, xCorner + 49 - 55 + (int) transColor[i], yCorner + 29, 1, val1, val2);
                colorGradientSide(context, context.getVertexConsumers().getBuffer(RenderLayer.getGui()), xCorner + 3 - 55 + (int) transColor[i], yCorner + 15, xCorner + 49 - 55 + (int) transColor[i], yCorner + 20, 1, sat1, sat2);
                for(int j = 0; j < 5; j++){
                    int hue1 = Color.HSBtoRGB((1/6f * j), sat, val);
                    int hue2 = Color.HSBtoRGB(1/6f + (1/6f * j), sat, val);
                    float width = 8;
                    float posX = width * j;
                    colorGradientSide(context, context.getVertexConsumers().getBuffer(RenderLayer.getGui()), xCorner + 3 + (int)posX - 55 + (int) transColor[i], yCorner + 6, xCorner + 3 + (int)posX + (int)width - 55 + (int) transColor[i], yCorner + 11, 1, hue1, hue2);
                }
                int hue1 = Color.HSBtoRGB((1/6f * 5), sat, val);
                int hue2 = Color.HSBtoRGB(1/6f + (1/6f * 5), sat, val);
                colorGradientSide(context, context.getVertexConsumers().getBuffer(RenderLayer.getGui()), xCorner + 43 - 55 + (int) transColor[i], yCorner + 6, xCorner + 49 - 55 + (int) transColor[i], yCorner + 11, 1, hue1, hue2);

                for(int j = 0; j < 3; j++) {
                    if ((mouseX > xCorner + 2 - 55 + (int) transColor[i] + (int) colorScrollX[i][j] &&
                            mouseX < xCorner + 8 - 55 + (int) transColor[i] + (int) colorScrollX[i][j] &&
                            mouseY > yCorner + 5 + (j * 9) &&
                            mouseY < yCorner + 12 + (j * 9)) || colorHoldingScroll[i][j]) {
                        context.drawTexture(texture, xCorner + 3 - 55 + (int) transColor[i] + (int) colorScrollX[i][j], yCorner + 6 + (j * 9), 2, 103, 176, 4, 5, 256, 256);
                    }
                    else {
                        context.drawTexture(texture, xCorner + 3 - 55 + (int) transColor[i] + (int) colorScrollX[i][j], yCorner + 6 + (j * 9), 2, 99, 176, 4, 5, 256, 256);
                    }
                }

            }
        }

        if (transOff > 0) {
            int xCorner = cornerX - (int) transOff;
            int yCorner = cornerY + 13;
            context.drawTexture(texture, xCorner, yCorner, 118, 0, (int) transOff, 150, 256, 256);
            for (int k = 0; k < 28; k++) {
                int multiplier = k / 4;
                int iconXPos = (20 * k) - (multiplier * 80);
                int iconYPos = 20 * multiplier;

                if (k + (4 * selectedRow) < layerManager.getLayers().size()) {

                    String comp = getNbtData().getCompound("layer" + (selectedLayer - 1)).getString("" + direction);
                    if (Objects.equals(layerManager.getLayers().get(k + (4 * selectedRow)).getName(), comp)) {
                        context.drawTexture(texture, xCorner + 5 + iconXPos, yCorner + 5 + iconYPos, 0, 196, 20, 20, 256, 256);
                    } else {
                        if (mouseX > xCorner + 5 + iconXPos && mouseX < xCorner + 25 + iconXPos && mouseY > yCorner + 5 + iconYPos && mouseY < yCorner + 25 + iconYPos) {
                            context.drawTexture(texture, xCorner + 5 + iconXPos, yCorner + 5 + iconYPos, 0, 216, 20, 20, 256, 256);
                        } else {
                            context.drawTexture(texture, xCorner + 5 + iconXPos, yCorner + 5 + iconYPos, 0, 176, 20, 20, 256, 256);
                        }
                    }
                    LayeredBlockLayer currentLayer = layerManager.getLayers().get(k + (4 * selectedRow));
                    boolean hasRgb = currentLayer.getRgbCount() > 0;

                    if (hasRgb) {
                        for (int i = 0; i < currentLayer.getRgbCount(); i++) {
                            Identifier texture = currentLayer.getRgbTexture(i);
                            Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(texture);
                            float grayMultiplier = (1f / 3) * i;
                            float color = Math.lerp(1, 0.3f, grayMultiplier);
                            context.drawSprite(xCorner + 7 + iconXPos, yCorner + 7 + iconYPos, 1, 16, 16, sprite, color, color, color, 1);
                        }
                    }
                    if (!hasRgb || currentLayer.cantRecolorLayer()) {
                        Identifier texture = currentLayer.getTexture();
                        Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(texture);
                        context.drawSprite(xCorner + 7 + iconXPos, yCorner + 7 + iconYPos, 1, 16, 16, sprite);
                    }
                    if(!currentLayer.getOverlay().getPath().isEmpty()) {
                        Identifier texture = currentLayer.getOverlay();
                        Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(texture);
                        context.drawSprite(xCorner + 7 + iconXPos, yCorner + 7 + iconYPos, 1, 16, 16, sprite);
                    }
                }
            }


            /*float scrollMultiplier = 1f/layerRows * selectedRow;
            float scrollHeight = Math.lerp(0, 123 ,scrollMultiplier) + (float)scrollBarOffset;*/
            if (mouseX > xCorner + 88 && mouseX < xCorner + 100 && mouseY > yCorner + 6 + (int) scrollBarY && mouseY < yCorner + 21 + (int) scrollBarY && this.holding || this.holdingScroll || layerRows == 0) {
                context.drawTexture(texture, xCorner + 88, yCorner + 6 + (int) scrollBarY, 87, 176, 12, 15, 256, 256);
            } else {
                context.drawTexture(texture, xCorner + 88, yCorner + 6 + (int) scrollBarY, 75, 176, 12, 15, 256, 256);
            }
        }

        context.drawTexture(texture, cornerX, cornerY, 2, 0, 0, 118, 176, 256, 256);
        for (int i = 0; i < 3; i++) {
            NbtCompound layerData = getNbtData().getCompound("layer" + i);
            if (!layerData.isEmpty() && !Objects.equals(layerData.getString("" + direction), "")) {
                String layerName = layerData.getString("" + direction);
                LayeredBlockLayer layer = layerManager.getLayer(layerName);
                boolean hasRgb = layer.getRgbCount() > 0;
                if (hasRgb) {
                    for (int j = 0; j < layer.getRgbCount(); j++) {
                        Identifier texture = layer.getRgbTexture(j);
                        Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(texture);
                        int color = layerData.getInt(direction + "_" + j + "_color");
                        float r = ColorHelper.Argb.getRed(color) / 255f;
                        float g = ColorHelper.Argb.getGreen(color) / 255f;
                        float b = ColorHelper.Argb.getBlue(color) / 255f;
                        context.drawSprite(cornerX + 8 + i * (36), cornerY + 25, 3, 30, 30, sprite, r, g, b, 1);

                        context.drawSprite(cornerX + 8, cornerY + 61, 3, 102, 102, sprite, r, g, b, 1);
                    }
                }
                if (layer.cantRecolorLayer()) {
                    Identifier texture = layer.getTexture();
                    Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(texture);
                    context.drawSprite(cornerX + 8 + i * (36), cornerY + 25, 3, 30, 30, sprite, 1, 1, 1, 1);

                    context.drawSprite(cornerX + 8, cornerY + 61, 3, 102, 102, sprite, 1, 1, 1, 1);
                }
                if(!layer.getOverlay().getPath().isEmpty()) {
                    Identifier texture = layer.getOverlay();
                    Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(texture);
                    context.drawSprite(cornerX + 8 + i * (36), cornerY + 25, 3, 30, 30, sprite, 1, 1, 1, 1);

                    context.drawSprite(cornerX + 8, cornerY + 61, 3, 102, 102, sprite, 1, 1, 1, 1);
                }
            }

            int posX = cornerX + 8 + i * (36);
            int posY = cornerY + 25;

            if (selectedLayer == i + 1 || (mouseY > posY && mouseY < posY + 30 && mouseX > posX && mouseX < posX + 30)) {
                context.drawTexture(texture, cornerX + 6 + i * (36), cornerY + 23, 4, 118, 150, 34, 34, 256, 256);
            }
        }

        boolean rotatesCheck = getNbtData().getBoolean("rotates");
        int u = rotatesCheck ? 75 : 87;
        int v = (mouseX > cornerX + 7 && mouseX < cornerX + 19 && mouseY > cornerY + 7 && mouseY < cornerY + 19) ? 203 : 191;
        context.drawTexture(texture, cornerX + 7, cornerY + 7, 5, u, v, 12, 12, 256, 256);


        int u2 = (mouseX > cornerX + 99 && mouseX < cornerX + 111 && mouseY > cornerY + 7 && mouseY < cornerY + 19) && !holding ? 87 : 75;
        context.drawTexture(texture, cornerX + 99, cornerY + 7, 5, u2, 215, 12, 12, 256, 256);
        super.render(context, mouseX, mouseY, delta);
    }
}
