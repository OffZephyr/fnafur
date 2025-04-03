package net.zephyr.fnafur.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.zephyr.fnafur.blocks.utility_blocks.computer.ComputerData;
import net.zephyr.fnafur.client.gui.screens.computer.apps.COMPCodeScreen;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.util.mixinAccessing.IDCVertexConsumersAcc;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bernie.geckolib.loading.json.raw.FaceUV;

import java.util.ArrayList;
import java.util.List;

public abstract class GoopyScreen extends Screen {
    public static List<GUIButton> BUTTONS = new ArrayList<>();
    BlockPos blockPos = BlockPos.ORIGIN;
    int entityID = 0;
    public int windowSizeX = 256, windowSizeY = 256;
    public int windowX, windowY;
    String itemSlot = "";
    NbtCompound nbtData = new NbtCompound();
    private boolean holding;

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
        BUTTONS.clear();
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
        this.client = MinecraftClient.getInstance();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        windowX = (MinecraftClient.getInstance().getWindow().getScaledWidth()/2) - (windowSizeX/2);
        windowY = (MinecraftClient.getInstance().getWindow().getScaledHeight()/2) - (windowSizeY/2);

        for(GUIButton button : BUTTONS){
            if(button instanceof GUIToggle toggle){
                renderToggle(context, mouseX, mouseY, toggle);
            }
            else {
                renderButton(context, mouseX, mouseY, button);
            }
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.holding = true;
        for(GUIButton guiButton : BUTTONS){
            int x = windowX + guiButton.x;
            int y = windowY + guiButton.y;
            int w = guiButton.width;
            int h = guiButton.height;
            if(guiButton instanceof GUIToggle guiToggle){
                if(isOnButton(mouseX, mouseY, x, y, w, h) && guiButton.toggle_exec != null){
                    guiButton.toggle_exec.toggle(guiToggle);
                }
            }
            else{
                if(isOnButton(mouseX, mouseY, x, y, w, h) && guiButton.click_exec != null){
                    guiButton.click_exec.execute();
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.holding = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public void renderButton(DrawContext context, double mouseX, double mouseY, GUIButton button) {
        int x = windowX + button.x;
        int y = windowY + button.y;
        int w = button.width;
        int h = button.height;
        GUISprite texture = button.off_sprite;

        if (isOnButton(mouseX, mouseY, x, y, w, h)) {
            if (holding && button.on_sprite != null) {
                texture = button.on_sprite;
            } else {
                if(button.hover_sprite != null){
                    texture = button.hover_sprite;
                }
            }
        }
        drawRecolorableTexture(context, texture.texture, x, y, w, h, texture.u, texture.v, texture.textureWidth, texture.textureHeight, texture.color);
    }
    public void renderToggle(DrawContext context, double mouseX, double mouseY, GUIToggle button) {
        int x = windowX + button.x;
        int y = windowY + button.y;
        int w = button.width;
        int h = button.height;
        GUISprite texture = button.on ? button.on_sprite : button.off_sprite;

        if (isOnButton(mouseX, mouseY, x, y, w, h)) {
            if (holding && ((button.on && button.on_sprite != null) || (!button.on && button.off_sprite != null))) {
                texture = button.on ? button.on_sprite : button.off_sprite;
            } else {
                if(button.hover_sprite != null){
                    texture = button.hover_sprite;
                }
            }
        }

        drawRecolorableTexture(context, texture.texture, x, y, w, h, texture.u, texture.v, texture.textureWidth, texture.textureHeight, texture.color);
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

    public static void drawRecolorableTexture(DrawContext context, Identifier texture, int x, int y, float regionWidth, float regionHeight, float u, float v, float textureWidth, float textureHeight, int color) {
        drawRecolorableTexture(context, texture, x, y, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight, ColorHelper.getRed(color) / 256f, ColorHelper.getGreen(color) / 256f,ColorHelper.getBlue(color) / 256f,ColorHelper.getAlpha(color) / 256f);
    }
    public static void drawRecolorableTexture(DrawContext context, Identifier texture, int x, int y, int z, float regionWidth, float regionHeight, float u, float v, float textureWidth, float textureHeight, float red, float green, float blue, float alpha) {
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

    protected static void drawEntity(DrawContext context, LivingEntity entity, float scale, float x, float y, float z, Quaternionf rotation) {
        context.getMatrices().push();
        context.getMatrices().translate(0, 0, z);
        InventoryScreen.drawEntity(context, x, y, scale, new Vector3f(), rotation, null, entity);
        context.getMatrices().pop();
    }
    public record GUISprite(Identifier texture, int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight, int color){
    }
    public class GUIButton{

        @FunctionalInterface
        public interface ToggleAction {
            void toggle(GUIToggle button);
        }
        @FunctionalInterface
        public interface ButtonAction {
            void execute();
        }
        public final int x, y, width, height;
        public GUISprite off_sprite;
        public GUISprite hover_sprite;
        public GUISprite on_sprite;
        public ButtonAction hover_exec;
        public ButtonAction click_exec;
        public ToggleAction toggle_exec;
        public GUIButton(int x, int y, int width, int height){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            BUTTONS.add(this);
        }

        public GUIButton offSprite(Identifier texture, int u, int v, int textureWidth, int textureHeight, int color){
            off_sprite = new GUISprite(texture, x, y, width, height, u, v, textureWidth, textureHeight, color);
            return this;
        }
        public GUIButton hoverSprite(Identifier texture, int u, int v, int textureWidth, int textureHeight, int color){
            hover_sprite = new GUISprite(texture, x, y, width, height, u, v, textureWidth, textureHeight, color);
            return this;
        }
        public GUIButton onSprite(Identifier texture, int u, int v, int textureWidth, int textureHeight, int color){
            on_sprite = new GUISprite(texture, x, y, width, height, u, v, textureWidth, textureHeight, color);
            return this;
        }
        public GUIButton hoverExec(ButtonAction method){
            hover_exec = method;
            return this;
        }
        public GUIButton clickExec(ButtonAction method){
            click_exec = method;
            return this;
        }

        public GUIButton toggleExec(ToggleAction method){
            toggle_exec = method;
            return this;
        }
    }

    public class GUIToggle extends GUIButton{
        public String setting;
        public boolean on;
        public GUIToggle(int x, int y, int width, int height, String setting) {
            this(x, y, width, height, setting, false);
        }
        GUIToggle(int x, int y, int width, int height, String setting, boolean defaultValue) {
            super(x, y, width, height);
            this.setting = setting;
            this.on = defaultValue;
        }
    }
}
