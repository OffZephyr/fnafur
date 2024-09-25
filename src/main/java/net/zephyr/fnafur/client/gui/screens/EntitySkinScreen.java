package net.zephyr.fnafur.client.gui.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.jsonReaders.entity_skins.EntitySkin;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.Objects;

public class EntitySkinScreen extends GoopyScreen{
    Identifier background = Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/skins/quad_bg.png");
    float bg_scroll_x = 0;
    float bg_scroll_y = 0;
    float entity_rotation = 180;
    private static final Vector3f ENTITY_TRANSLATION = new Vector3f();
    private static final Quaternionf ENTITY_ROTATION = new Quaternionf().rotationXYZ(0.2f, (float) Math.PI, (float) Math.PI);

    private LivingEntity entity;

    public EntitySkinScreen(Text text, NbtCompound nbtCompound, Object o) {
        super(text, nbtCompound, o);
        DefaultEntity actualEntity = ((DefaultEntity)MinecraftClient.getInstance().world.getEntityById(getEntityID()));
        updateEntity((EntityType<? extends LivingEntity>) actualEntity.getType());
    }

    protected void updateEntity(EntityType<? extends LivingEntity> entityType) {
        this.entity = entityType.create(MinecraftClient.getInstance().world);
        ((IEntityDataSaver)this.entity).getPersistentData().copyFrom(getNbtData());
        if(this.entity instanceof DefaultEntity ent){
            ent.menuTick = true;
        }
    }
    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void tick() {
        bg_scroll_x += 1;
        bg_scroll_y += 0.5f;
        super.tick();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if(isOnButton(mouseX, mouseY, (this.width / 3) * 2, 0, this.width / 3, this.height - 30)){
            entity_rotation -= deltaX;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int confirmX = (this.width / 3) * 2 + (this.width / 32);
        int confirmY = this.height - (this.height / 7);
        int confirmWidth = this.width - (this.width / 32) - confirmX;
        int confirmHeight = this.height - (this.height / 32) - confirmY;
        if(isOnButton(mouseX, mouseY, confirmX, confirmY, confirmWidth, confirmHeight)) {
            GoopyNetworkingUtils.saveEntityData(getEntityID(), ((IEntityDataSaver)entity).getPersistentData());
            close();
        }
        int minX = 0;
        List<DefaultEntity.EntitySkin> skinList = ((DefaultEntity)entity).getSkins();
        int minY = 0;
        int maxX = (this.width / 3) * 2;
        int maxY = this.height;
        int spaceX = maxX - minX;
        int spaceY = maxY - minY;

        final int maxRowLength = 8;
        int skinCount = skinList.size();
        int rowCount = ((skinCount - 1) / maxRowLength) + 1;
        for(int i = 0; i < skinCount; i++) {

            int currentRow = (i / maxRowLength) + 1;
            int rowNum = maxRowLength + (i - (currentRow * maxRowLength));
            int rowLength = currentRow == rowCount ? skinCount - ((currentRow - 1) * maxRowLength) : maxRowLength;


            float scale = rowCount == 1 && rowLength < maxRowLength ? MathHelper.clamp(3f / (((float)rowLength / maxRowLength) * 4), 1, 50) : 1;
            float iconWidth = ((spaceX) / (maxRowLength + 2f)) * scale;
            float iconHeight = ((spaceY) / 7f) * scale;

            int x = spaceX / (rowLength + 1) * (rowNum + 1);
            int x1 = x - (int)iconWidth / 2;
            int x2 = x + (int)iconWidth / 2;

            int y = spaceY / (rowCount + 1) * currentRow;
            int y1 = y - (int)iconHeight / 2;
            int y2 = y + (int)iconHeight / 2;

            DefaultEntity.EntitySkin skin = i >= skinList.size() ? skinList.get(skinList.size() - 1) : skinList.get(i);
            if(isOnButton(mouseX, mouseY, x1, y1, x2 - x1, y2 - y1)){
                ((IEntityDataSaver)entity).getPersistentData().putString("Reskin", skin.name);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        MatrixStack matrices = context.getMatrices();

        float scale = 0.5f;

        matrices.push();
        matrices.scale(scale, scale / 1.25f, scale);
        float sizex = this.width * (1f / scale);
        float sizey = this.height * (1f / (scale/ 1.25f));
        int sizex2 = (int)sizex;
        int sizey2 = (int)sizey;
        drawRecolorableTexture(context, background, 0, 0, 0, sizex2, sizey2, (int)bg_scroll_x, (int)bg_scroll_y, this.width, this.width, 0.7f, 0.7f, 0.7f, 1);
        matrices.pop();

        context.fill((this.width / 3) * 2, 0, this.width, this.height, 0x88000000);

        context.fill(((this.width / 3) * 2) - 1, 0, ((this.width / 3) * 2), this.height, 0xFFFFFFFF);
        Text entityName = entity.getName();

        String skin = ((IEntityDataSaver) entity).getPersistentData().getString("Reskin");

        if(entity instanceof DefaultEntity ent && ent.getSkin(skin) == null) skin = ent.defaultSkin().name;

        Text skinName = Text.translatable(skin);
        Text description = Text.translatable(skin + ".description");
        drawAutoResizedText(context, textRenderer, skinName, 2, (this.width / 6f) - 10, (this.width / 6f) * 5, 10, 0xFFFFFFFF, 0, false, true);

        float y = 15 + getResizedTextHeight(textRenderer, skinName, 2, (this.width / 6f) - 10);
        drawAutoResizedText(context, textRenderer, description, 1.5f, (this.width / 6f) - 10, (this.width / 6f) * 5, y, 0xFFFFFFFF, 0, false, true);

        drawEntity(context, (this.width / 6f) * 5, (this.height / 6f) * 5);

        renderIcons(context, mouseX, mouseY, delta);

        int confirmX = (this.width / 3) * 2 + (this.width / 32);
        int confirmY = this.height - (this.height / 7);
        int confirmWidth = this.width - (this.width / 32) - confirmX;
        int confirmHeight = this.height - (this.height / 32) - confirmY;
        context.fill(confirmX, confirmY, confirmX + confirmWidth, confirmY + confirmHeight, 0xFFFFFFFF);
        int color = isOnButton(mouseX, mouseY, confirmX, confirmY, confirmWidth, confirmHeight) ? 0xFF333333 : 0xFF000000;
        context.fill(confirmX + 1, confirmY + 1, confirmX + confirmWidth - 1, confirmY + confirmHeight - 1, color);
        Text text = Text.translatable("entity.fnafur.skin_screen.confirm");
        float confirmScale = 1.5f;
        drawAutoResizedText(context, textRenderer, text, confirmScale, confirmWidth - 50, (this.width / 6f) * 5, confirmY + (confirmHeight / 2f) - (getResizedTextHeight(textRenderer, text, confirmScale, confirmWidth - 10)/2), 0xFFFFFFFF, 0, false, true);
        super.render(context, mouseX, mouseY, delta);
    }

    void renderIcons(DrawContext context, int mouseX, int mouseY, float delta){
        List<DefaultEntity.EntitySkin> skinList = ((DefaultEntity)entity).getSkins();

        int minX = 0;
        int minY = 0;
        int maxX = (this.width / 3) * 2;
        int maxY = this.height;
        int spaceX = maxX - minX;
        int spaceY = maxY - minY;

        final int maxRowLength = 8;
        int skinCount = skinList.size();
        int rowCount = ((skinCount - 1) / maxRowLength) + 1;
        for(int i = 0; i < skinCount; i++) {

            int currentRow = (i / maxRowLength) + 1;
            int rowNum = maxRowLength + (i - (currentRow * maxRowLength));
            int rowLength = currentRow == rowCount ? skinCount - ((currentRow - 1) * maxRowLength) : maxRowLength;


            float scale = rowCount == 1 && rowLength < maxRowLength ? MathHelper.clamp(3f / (((float)rowLength / maxRowLength) * 4), 1, 50) : 1;
            float iconWidth = ((spaceX) / (maxRowLength + 2f)) * scale;
            float iconHeight = ((spaceY) / 7f) * scale;

            int x = spaceX / (rowLength + 1) * (rowNum + 1);
            int x1 = x - (int)iconWidth / 2;
            int x2 = x + (int)iconWidth / 2;

            int y = spaceY / (rowCount + 1) * currentRow;
            int y1 = y - (int)iconHeight / 2;
            int y2 = y + (int)iconHeight / 2;

            DefaultEntity.EntitySkin skin = i >= skinList.size() ? skinList.get(skinList.size() - 1) : skinList.get(i);

            float color = Objects.equals(skin.name, ((IEntityDataSaver) entity).getPersistentData().getString("Reskin")) ? 1 : 0.6f;

            drawRecolorableTexture(context, skin.icon, x1, y1, 0, x2 - x1, y2 - y1, 0, 0, x2 - x1, y2 - y1, 1, 1, 1, 1);
            drawRecolorableTexture(context, skin.icon_outline, x1 - 1, y1 - 1, 0, x2 - x1 + 2, y2 - y1 + 2, 0, 0, x2 - x1 + 2, y2 - y1 + 2, color, color, color, 1);
        }
    }

    protected void drawEntity(DrawContext context, float x, float y) {
        if(this.entity instanceof DefaultEntity ent) {
            context.getMatrices().push();
            context.getMatrices().translate(0, 0, 0);
            Quaternionf rotation = new Quaternionf().rotationXYZ(0.2f, entity_rotation * (MathHelper.PI / 180), MathHelper.PI);
            InventoryScreen.drawEntity(context, x, y, ent.demo_scale() * ((float) 60.0), ENTITY_TRANSLATION, rotation, null, ent);
            context.getMatrices().pop();
        }
    }
}
