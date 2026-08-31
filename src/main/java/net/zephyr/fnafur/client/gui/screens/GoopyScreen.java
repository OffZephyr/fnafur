package net.zephyr.fnafur.client.gui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import org.joml.Matrix3x2fStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public abstract class GoopyScreen extends Screen {
    public static List<GUIButton> BUTTONS = new ArrayList<>();
    BlockPos blockPos = BlockPos.ZERO;
    int entityID = 0;
    public int windowSizeX = 256, windowSizeY = 256;
    public int windowX, windowY;
    String itemSlot = "";
    CompoundTag nbtData = new CompoundTag();
    private boolean holding;

    public void putNbtData(CompoundTag nbt){
        nbtData = nbt.copy();
    }
    public void putBlockPos(BlockPos newPos){
        blockPos = newPos;
    }
    public void putEntityID(int id){
        entityID = id;
    }
    public CompoundTag getNbtData(){
        return nbtData;
    }
    public BlockPos getBlockPos(){
        return blockPos;
    }
    public int getEntityID(){ return entityID; }
    public String getItemSlot(){ return itemSlot; }
    public GoopyScreen(Component title) {
        super(title);
        BUTTONS.clear();
    }
    public GoopyScreen(Component title, CompoundTag nbt, long l) {
        this(title);
        this.nbtData = nbt;
        this.blockPos = BlockPos.of(l);
        this.entityID = l < Integer.MAX_VALUE ? (int) l : Integer.MAX_VALUE;
    }
    public GoopyScreen(Component text, CompoundTag CompoundTag, Object o) {
        this(text);
        this.nbtData = CompoundTag;
        if(o instanceof BlockPos pos) this.blockPos = pos;
        else if(o instanceof Integer num) this.entityID = num;
        else if(o instanceof String slot) this.itemSlot = slot;
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        windowX = (Minecraft.getInstance().getWindow().getGuiScaledWidth()/2) - (windowSizeX/2);
        windowY = (Minecraft.getInstance().getWindow().getGuiScaledHeight()/2) - (windowSizeY/2);

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
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        this.holding = true;
        for(GUIButton guiButton : BUTTONS){
            int x = windowX + guiButton.x;
            int y = windowY + guiButton.y;
            int w = guiButton.width;
            int h = guiButton.height;
            if(guiButton instanceof GUIToggle guiToggle){
                if(isOnButton(click.x(), click.y(), x, y, w, h) && guiButton.toggle_exec != null){
                    guiButton.toggle_exec.toggle(guiToggle);
                }
            }
            else{
                if(isOnButton(click.x(), click.y(), x, y, w, h) && guiButton.click_exec != null){
                    guiButton.click_exec.execute();
                }
            }
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent click) {
        this.holding = false;
        return super.mouseReleased(click);
    }

    public void renderButton(GuiGraphics context, double mouseX, double mouseY, GUIButton button) {
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
    public void renderToggle(GuiGraphics context, double mouseX, double mouseY, GUIToggle button) {
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

    public void renderButton(Identifier texture, GuiGraphics context, int x, int y, int u, int v, int u2, int v2, int width, int height, int textureWidth, int textureHeight, int mouseX, int mouseY){
        if(isOnButton(mouseX, mouseY, x, y, width, height)){
            context.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, u2, v2, width, height, textureWidth, textureHeight);
        }
        else {
            context.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, textureWidth, textureHeight);
        }
    }
    public void renderButton(Identifier texture, GuiGraphics context, int x, int y, int u, int v, int u2, int v2, int u3, int v3, int width, int height, int textureWidth, int textureHeight, int mouseX, int mouseY, boolean holding){
        if(isOnButton(mouseX, mouseY, x, y, width, height)) {
            if(holding){
                context.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, u3, v3, width, height, textureWidth, textureHeight);
            }
            else {
                context.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, u2, v2, width, height, textureWidth, textureHeight);
            }
        }
        else {
            context.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, textureWidth, textureHeight);
        }
    }
    public void renderButton(Identifier texture, GuiGraphics context, int x, int y, int u, int v, int u2, int v2, int u3, int v3, int width, int height, int textureWidth, int textureHeight, int mouseX, int mouseY, boolean holding, boolean condition){
        if(condition){
            context.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, u3, v3, width, height, textureWidth, textureHeight);
        }
        else if(isOnButton(mouseX, mouseY, x, y, width, height)) {
            if(holding){
                context.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, u3, v3, width, height, textureWidth, textureHeight);
            }
            else {
                context.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, u2, v2, width, height, textureWidth, textureHeight);
            }
        }
        else {
            context.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, textureWidth, textureHeight);
        }
    }

    public static void drawRecolorableTexture(GuiGraphics context, Identifier texture, int x, int y, float regionWidth, float regionHeight, float u, float v, float textureWidth, float textureHeight, int color) {
        drawRecolorableTexture(context, texture, x, y, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight, ARGB.red(color) / 256f, ARGB.green(color) / 256f, ARGB.blue(color) / 256f, ARGB.alpha(color) / 256f);
    }

    public static void drawOutline(GuiGraphics context, int x, int y, int width, int height, int color){
        context.fill(x, y, x + width + 1, y + 1, color);
        context.fill(x, y + height, x + width + 1, y + height + 1, color);
        context.fill(x, y + 1, x + 1, y + height, color);
        context.fill(x + width, y + 1, x + width + 1, y + height, color);
    }

    public static void drawRecolorableTexture(GuiGraphics context, Identifier texture, int x, int y, int z, float regionWidth, float regionHeight, float u, float v, float textureWidth, float textureHeight, float red, float green, float blue, float alpha) {
        float u1 = (u + 0.0f) /textureWidth;
        float u2 = (u + regionWidth) / textureWidth;
        float v1 = (v + 0.0f) / textureHeight;
        float v2 =  (v + regionHeight) / textureHeight;

        int x1 = x;
        int y1 = y;
        int x2 = x + (int)regionWidth;
        int y2 = y + (int)regionHeight;

        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        AbstractTexture abstractTexture = textureManager.getTexture(texture);
        //RenderSystem.setShaderTexture(0, abstractTexture.getGlTexture());

        context.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, (int) regionWidth, (int) regionHeight, (int) textureWidth, (int) textureHeight, ARGB.color((int)(alpha * 255),(int)(red * 255),(int)(green * 255),(int)(blue * 255)));
        //RenderPipeline renderPipeline = RenderPipelines.GUI_TEXTURED;
        //renderPass.setPipeline(renderPipeline);
        //RenderSystem.setShader(RenderPipelines.POSITION_TEX);
        //RenderSystem.enableBlend();
        //RenderSystem.setShaderColor(red, green, blue, alpha);
        //Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        //BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        //bufferBuilder.vertex(matrix4f, (float)x1, (float)y1, (float)z).texture(u1, v1);
        //bufferBuilder.vertex(matrix4f, (float)x1, (float)y2, (float)z).texture(u1, v2);
        //bufferBuilder.vertex(matrix4f, (float)x2, (float)y2, (float)z).texture(u2, v2);
        //bufferBuilder.vertex(matrix4f, (float)x2, (float)y1, (float)z).texture(u2, v1);
        //BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        //RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        //RenderSystem.disableBlend();


    }
    public static void drawResizableText(GuiGraphics context, Font textRenderer, Component text, float scale, float x, float y, int color, int backgroundColor, boolean shadow, boolean centered){

        x = x / scale;
        y = y / scale;
        if(centered) x -= (textRenderer.width(text) / 2f);

        Matrix3x2fStack matrices = context.pose();

        matrices.pushMatrix();
        matrices.scale(scale, scale);
        context.drawString(textRenderer, text, (int)x, (int)y, color, false);
        //textRenderer.draw(text, x, y, color, shadow, matrices.peek().getPositionMatrix(), vertices, TextRenderer.TextLayerType.NORMAL, backgroundColor, 0xF000F0);
        matrices.popMatrix();
    }
    public void drawAutoResizedText(GuiGraphics context, Font textRenderer, Component text, float baseScale, float maxTextWidth, float x, float y, int color, int backgroundColor, boolean shadow, boolean centered){
        float scale = (textRenderer.width(text) * baseScale) > maxTextWidth ? (baseScale / textRenderer.width(text)) * maxTextWidth : baseScale;
        drawResizableText(context, textRenderer, text, scale, x, y + (1 / scale), color, backgroundColor, shadow, centered);
    }
    public static float getResizedTextHeight(Font textRenderer, Component text, float baseScale, float maxTextWidth){
        float scale = (textRenderer.width(text) * baseScale) > maxTextWidth ? (baseScale / textRenderer.width(text)) * maxTextWidth : baseScale;
        return 8 * scale;
    }

    public static void drawEntity(GuiGraphics context, int x1, int y1, int x2, int y2, int size, float scale, Quaternionf rotation, Entity entity) {
        drawEntity(context, x1, y1, x2, y2, size, scale,rotation, entity, 0, false);
    }
    public static void drawEntity(GuiGraphics context, int x1, int y1, int x2, int y2, int size, float scale, Quaternionf rotation, Entity entity, float entityYOffset) {
        drawEntity(context, x1, y1, x2, y2, size, scale,rotation, entity, entityYOffset, false);
    }
    public static void drawEntity(GuiGraphics context, int x1, int y1, int x2, int y2, int size, float scale, Quaternionf rotation, Entity entity, float entityYOffset, boolean entity2) {
        context.enableScissor(x1, y1, x2, y2);
        Quaternionf quaternionf = new Quaternionf().rotateZ((float) Math.PI);
        Quaternionf quaternionf2 = rotation;
        quaternionf.mul(quaternionf2);

        float k = entity.getYRot();
        float l = entity.getXRot();
        entity.setYRot(0);
        entity.setXRot(0);

        float j = 0;
        float m = 0;
        float n = 0;
        float o = 1;
        if(entity instanceof LivingEntity ent) {
            j = ent.yBodyRot;
            m = ent.yHeadRotO;
            n = ent.yHeadRot;
            ent.yBodyRot = 0;
            ent.yHeadRot = entity.getYRot();
            ent.yHeadRotO = entity.getYRot();
            o = ent.getScale();
        }

        Vector3f vector3f = new Vector3f(0.0F, entityYOffset + (entity.getBbHeight() / 2.0F + scale * o), 0.0F);
        float p = size / o;

        float f = (float)(x1 + x2) / 2.0F;
        float g = (float)(y1 + y2) / 2.0F;


        EntityRenderDispatcher entityRenderManager = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super Entity, ?> entityRenderer = entityRenderManager.getRenderer(entity);
        EntityRenderState entityRenderState = entityRenderer.createRenderState(entity, 1.0F);
        entityRenderState.lightCoords = 15728880;
        entityRenderState.shadowPieces.clear();
        entityRenderState.outlineColor = 0;

            if (entityRenderState instanceof LivingEntityRenderState livingEntityRenderState) {
                livingEntityRenderState.bodyRot = 0;
                livingEntityRenderState.yRot = 0;
                livingEntityRenderState.xRot = entity.getXRot();

                livingEntityRenderState.boundingBoxWidth /= livingEntityRenderState.scale;
                livingEntityRenderState.boundingBoxHeight /= livingEntityRenderState.scale;
                livingEntityRenderState.scale = 1.0F;
            }

            context.submitEntityRenderState(entityRenderState, (float)p, vector3f, quaternionf, quaternionf2, x1, y1, x2, y2);
            //InventoryScreen.drawEntity(context, x1, y1, x2, y2, size, scale, f, g, entity);
            //InventoryScreen.drawEntity(context, x1, y1, x2, y2, p, vector3f, quaternionf, quaternionf2, entity);

        entity.setYRot(k);
        entity.setXRot(l);
        if(entity instanceof LivingEntity ent) {
            ent.yBodyRot = j;
            ent.yHeadRotO = m;
            ent.yHeadRot = n;
        }
        context.disableScissor();
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
        public boolean leftText;
        public GUIToggle(int x, int y, int width, int height, boolean leftText, String setting) {
            this(x, y, width, height, leftText, setting, false);
        }
        public GUIToggle(int x, int y, int width, int height, boolean leftText,String setting,  boolean defaultValue) {
            super(x, y, width, height);
            this.setting = setting;
            this.on = defaultValue;
            this.leftText = leftText;
        }
    }
}
