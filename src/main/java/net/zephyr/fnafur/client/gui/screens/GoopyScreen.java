package net.zephyr.fnafur.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.util.mixinAccessing.IDCVertexConsumersAcc;
import org.joml.Matrix4f;

public abstract class GoopyScreen extends Screen {
    BlockPos blockPos = BlockPos.ORIGIN;
    int entityID = 0;
    String itemSlot = "";
    NbtCompound nbtData = new NbtCompound();
    public void putNbtData(NbtCompound nbt){
        nbtData = nbt.copy();
    }
    public void putBlockPos(BlockPos newPos){
        blockPos = newPos;
    }
    public void putEntityID(int id){
        entityID = id;
    }
    public NbtCompound getNbtData(){
        return nbtData;
    }
    public BlockPos getBlockPos(){
        return blockPos;
    }
    public int getEntityID(){ return entityID; }
    public String getItemSlot(){ return itemSlot; }
    public GoopyScreen(Text title) {
        super(title);
    }
    public GoopyScreen(Text title, NbtCompound nbt, long l) {
        this(title);
        this.nbtData = nbt;
        this.blockPos = BlockPos.fromLong(l);
        this.entityID = l < Integer.MAX_VALUE ? (int) l : Integer.MAX_VALUE;
    }
    public GoopyScreen(Text text, NbtCompound nbtCompound, Object o) {
        this(text);
        this.nbtData = nbtCompound;
        if(o instanceof BlockPos pos) this.blockPos = pos;
        else if(o instanceof Integer num) this.entityID = num;
        else if(o instanceof String slot) this.itemSlot = slot;
    }

    public static boolean isOnButton(double mouseX, double mouseY, int x, int y, int width, int height) {
        return (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height);
    }

    public void renderButton(Identifier texture, DrawContext context, int x, int y, int u, int v, int u2, int v2, int width, int height, int textureWidth, int textureHeight, int mouseX, int mouseY){
        if(isOnButton(mouseX, mouseY, x, y, width, height)){
            context.drawTexture(RenderLayer::getGuiTextured, texture, x, y, u2, v2, width, height, textureWidth, textureHeight);
        }
        else {
            context.drawTexture(RenderLayer::getGuiTextured, texture, x, y, u, v, width, height, textureWidth, textureHeight);
        }
    }
    public void renderButton(Identifier texture, DrawContext context, int x, int y, int u, int v, int u2, int v2, int u3, int v3, int width, int height, int textureWidth, int textureHeight, int mouseX, int mouseY, boolean holding){
        if(isOnButton(mouseX, mouseY, x, y, width, height)) {
            if(holding){
                context.drawTexture(RenderLayer::getGuiTextured, texture, x, y, u3, v3, width, height, textureWidth, textureHeight);
            }
            else {
                context.drawTexture(RenderLayer::getGuiTextured, texture, x, y, u2, v2, width, height, textureWidth, textureHeight);
            }
        }
        else {
            context.drawTexture(RenderLayer::getGuiTextured, texture, x, y, u, v, width, height, textureWidth, textureHeight);
        }
    }
    public void renderButton(Identifier texture, DrawContext context, int x, int y, int u, int v, int u2, int v2, int u3, int v3, int width, int height, int textureWidth, int textureHeight, int mouseX, int mouseY, boolean holding, boolean condition){
        if(condition){
            context.drawTexture(RenderLayer::getGuiTextured, texture, x, y, u3, v3, width, height, textureWidth, textureHeight);
        }
        else if(isOnButton(mouseX, mouseY, x, y, width, height)) {
            if(holding){
                context.drawTexture(RenderLayer::getGuiTextured, texture, x, y, u3, v3, width, height, textureWidth, textureHeight);
            }
            else {
                context.drawTexture(RenderLayer::getGuiTextured, texture, x, y, u2, v2, width, height, textureWidth, textureHeight);
            }
        }
        else {
            context.drawTexture(RenderLayer::getGuiTextured, texture, x, y, u, v, width, height, textureWidth, textureHeight);
        }
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    public void drawRecolorableTexture(DrawContext context, Identifier texture, int x, int y, int z, float regionWidth, float regionHeight, float u, float v, float textureWidth, float textureHeight, float red, float green, float blue, float alpha) {
        float u1 = (u + 0.0f) /textureWidth;
        float u2 = (u + regionWidth) / textureWidth;
        float v1 = (v + 0.0f) / textureHeight;
        float v2 =  (v + regionHeight) / textureHeight;

        int x1 = x;
        int y1 = y;
        int x2 = x + (int)regionWidth;
        int y2 = y + (int)regionHeight;
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX);
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(red, green, blue, alpha);
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, (float)x1, (float)y1, (float)z).texture(u1, v1);
        bufferBuilder.vertex(matrix4f, (float)x1, (float)y2, (float)z).texture(u1, v2);
        bufferBuilder.vertex(matrix4f, (float)x2, (float)y2, (float)z).texture(u2, v2);
        bufferBuilder.vertex(matrix4f, (float)x2, (float)y1, (float)z).texture(u2, v1);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }

    public static void drawResizableText(DrawContext context, TextRenderer textRenderer, Text text, float scale, float x, float y, int color, int backgroundColor, boolean shadow, boolean centered){

        x = x / scale;
        y = y / scale;
        if(centered) x -= (textRenderer.getWidth(text) / 2f);

        MatrixStack matrices = context.getMatrices();
        VertexConsumerProvider verticies = ((IDCVertexConsumersAcc)context).getVertexConsumers();

        matrices.push();
        matrices.scale(scale, scale, scale);
        textRenderer.draw(text, x, y, color, shadow, matrices.peek().getPositionMatrix(), verticies, TextRenderer.TextLayerType.NORMAL, backgroundColor, 0xF000F0);
        matrices.pop();
    }
    public void drawAutoResizedText(DrawContext context, TextRenderer textRenderer, Text text, float baseScale, float maxTextWidth, float x, float y, int color, int backgroundColor, boolean shadow, boolean centered){
        float scale = (textRenderer.getWidth(text) * baseScale) > maxTextWidth ? (baseScale / textRenderer.getWidth(text)) * maxTextWidth : baseScale;
        drawResizableText(context, textRenderer, text, scale, x, y + (1 / scale), color, backgroundColor, shadow, centered);
    }
    public static float getResizedTextHeight(TextRenderer textRenderer, Text text, float baseScale, float maxTextWidth){
        float scale = (textRenderer.getWidth(text) * baseScale) > maxTextWidth ? (baseScale / textRenderer.getWidth(text)) * maxTextWidth : baseScale;
        return 8 * scale;
    }
}
