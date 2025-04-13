package net.zephyr.fnafur.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.itemgroup.FabricItemGroupImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.init.entity_init.ClassicInit;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class FnafCreativeInventoryScreen extends CreativeInventoryScreen {

    public static boolean isFnafTab = false;
    public float floorX = 0;
    public float garageDoorIndex = 0;
    public static FnafSubTab SubTab = FnafSubTab.DEFAULT;
    public static FnafSubTab GoalSubTab = FnafSubTab.DEFAULT;
    LivingEntity FreddyEntity = ClassicInit.CL_FRED.create(MinecraftClient.getInstance().world, SpawnReason.LOAD);

    public FnafCreativeInventoryScreen(ClientPlayerEntity player, FeatureSet enabledFeatures, boolean operatorTabEnabled) {
        super(player, enabledFeatures, operatorTabEnabled);
        SubTab = FnafSubTab.DEFAULT;
        GoalSubTab = FnafSubTab.DEFAULT;
    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //floorX += delta/100f;
        isFnafTab = Objects.equals(getSelectedItemGroup().getDisplayName().getString(), FnafUniverseRebuilt.MOD_ID);
        if(isFnafTab){
            this.renderBackground(context, mouseX, mouseY, delta);
            context.fill(this.x + 1, this.y + 1, this.x + this.backgroundWidth - 2, this.y + this.backgroundHeight - 2, 0xFF000000);

            int i = this.x;
            int j = this.y;
            this.drawFnafBackground(context, delta, mouseX, mouseY);

            for (ItemGroup itemGroup : ItemGroups.getGroupsToDisplay()) {
                if (this.renderTabTooltipIfHovered(context, itemGroup, mouseX, mouseY)) {
                    break;
                }
            }

            if(GoalSubTab != SubTab){
                if(garageDoorIndex <= 0) garageDoorIndex = 1;
                if(garageDoorIndex <= 0.5f) SubTab = GoalSubTab;
            }

            garageDoorIndex = garageDoorIndex > 0 ? garageDoorIndex - (delta/20f) : 0;
            double garageHeightIndex = Math.sin(garageDoorIndex * Math.PI);
            int garageHeight = (int) MathHelper.lerp(garageHeightIndex, 0, this.backgroundHeight - 26);

            context.drawTexture(RenderLayer::getGuiTextured, getSelectedItemGroup().getTexture(), this.x + 7, this.y + 20, 32, 136 + (this.backgroundHeight - 27 - garageHeight), this.backgroundWidth - 14, garageHeight, 256, 256);

            this.drawMouseoverTooltip(context, mouseX, mouseY);
            return;
        }
        super.render(context, mouseX, mouseY, delta);
    }

    public void renderDefaultBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        Identifier gridTexture = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/tab_grid.png");
        context.drawTexture(RenderLayer::getGuiTextured, gridTexture, this.x + 7, this.y + 20, 0, 0, this.backgroundWidth - 2, this.backgroundHeight - 2, 256, 256);

        Identifier icon1 = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/icon_animatronics.png");
        Identifier icon2 = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/icon_blocks.png");
        Identifier icon3 = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/icon_props.png");
        Identifier icon4 = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/icon_technical.png");

        int x1 = this.x + 10;
        int y1 = this.y + 23;

        int x2 = this.x + 101;
        int y2 = this.y + 78;

        int v1 = GoopyScreen.isOnButton(mouseX, mouseY, x1, y1, 84, 48) ? 48 : 0;
        int v2 = GoopyScreen.isOnButton(mouseX, mouseY, x2, y1, 84, 48) ? 48 : 0;
        int v3 = GoopyScreen.isOnButton(mouseX, mouseY, x1, y2, 84, 48) ? 48 : 0;
        int v4 = GoopyScreen.isOnButton(mouseX, mouseY, x2, y2, 84, 48) ? 48 : 0;

        context.drawTexture(RenderLayer::getGuiTextured, icon1, x1, y1, 0, v1, 84, 48, 128, 128);
        context.drawTexture(RenderLayer::getGuiTextured, icon2, x2, y1, 0, v2, 84, 48, 128, 128);
        context.drawTexture(RenderLayer::getGuiTextured, icon3, x1, y2, 0, v3, 84, 48, 128, 128);
        context.drawTexture(RenderLayer::getGuiTextured, icon4, x2, y2, 0, v4, 84, 48, 128, 128);

    }
    public void renderAnimatronicsBackground(DrawContext context, int mouseX, int mouseY, float delta) {

    }
    public void renderBlocksBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        Identifier floorTexture = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/storage_floor.png");
        drawFloor(context, delta, mouseX, mouseY, floorTexture, this.x + 1, this.x + this.backgroundWidth - 1, this.y + ((this.backgroundHeight) / 4 * 3), this.y + this.backgroundHeight - 1);

    }
    public void renderPropsBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        Identifier floorTexture = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/storage_floor.png");
        drawFloor(context, delta, mouseX, mouseY, floorTexture, this.x + 1, this.x + this.backgroundWidth - 1, this.y + ((this.backgroundHeight) / 4 * 3), this.y + this.backgroundHeight - 1);

    }
    public void renderTechBackground(DrawContext context, int mouseX, int mouseY, float delta) {

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(isFnafTab) {
            switch (SubTab) {
                default -> clickDefault(mouseX, mouseY, button);
                case ANIMATRONICS -> clickAnimatronics(mouseX, mouseY, button);
                case BLOCKS -> clickBlocks(mouseX, mouseY, button);
                case DECORATION -> clickProps(mouseX, mouseY, button);
                case TECHNICAL -> clickTech(mouseX, mouseY, button);
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void clickDefault(double mouseX, double mouseY, int button){
        int x1 = this.x + 10;
        int y1 = this.y + 23;

        int x2 = this.x + 101;
        int y2 = this.y + 78;

        if(GoopyScreen.isOnButton(mouseX, mouseY, x1, y1, 84, 48)) GoalSubTab = FnafSubTab.ANIMATRONICS;
        if(GoopyScreen.isOnButton(mouseX, mouseY, x2, y1, 84, 48)) GoalSubTab = FnafSubTab.BLOCKS;
        if(GoopyScreen.isOnButton(mouseX, mouseY, x1, y2, 84, 48)) GoalSubTab = FnafSubTab.DECORATION;
        if(GoopyScreen.isOnButton(mouseX, mouseY, x2, y2, 84, 48)) GoalSubTab = FnafSubTab.TECHNICAL;


    }
    public void clickAnimatronics(double mouseX, double mouseY, int button){

    }
    public void clickBlocks(double mouseX, double mouseY, int button){

    }
    public void clickProps(double mouseX, double mouseY, int button){

    }
    public void clickTech(double mouseX, double mouseY, int button){

    }

    protected void drawFnafBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        for (ItemGroup itemGroup : ItemGroups.getGroupsToDisplay()) {
            if (itemGroup != getSelectedItemGroup() || Objects.equals(itemGroup.getDisplayName().getString(), FnafUniverseRebuilt.MOD_ID)) {
                this.renderTabIcon(context, itemGroup);
            }
        }

        switch (SubTab){
            default -> renderDefaultBackground(context, mouseX, mouseY, delta);
            case ANIMATRONICS -> renderAnimatronicsBackground(context, mouseX, mouseY, delta);
            case BLOCKS -> renderBlocksBackground(context, mouseX, mouseY, delta);
            case DECORATION -> renderPropsBackground(context, mouseX, mouseY, delta);
            case TECHNICAL -> renderTechBackground(context, mouseX, mouseY, delta);
        }

        context.drawTexture(RenderLayer::getGuiTextured, getSelectedItemGroup().getTexture(), this.x, this.y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);

        context.drawTexture(RenderLayer::getGuiTextured, getSelectedItemGroup().getTexture(), this.x + 8, this.y + 6, 0.0F, this.backgroundHeight + (SubTab.index * 5), 28, 5, 256, 256);

        if(!isFnafTab) {
            this.renderTabIcon(context, getSelectedItemGroup());
        }
    }
    protected void drawFloor(DrawContext context, float delta, int mouseX, int mouseY, Identifier texture, int x, int maxX, int y, int maxY){

        float v1 = 0;
        float v2 = 1;

        int y1 = y;
        int y2 = maxY;

        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX);
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

        int max = 128;
        float multiplier = 0.25f;
        for(int i = 0; i < max; i++){
            float thisX = x + ((maxX - (1.0f * x))/max) * i;
            float nextX = x + ((maxX - (1.0f * x))/max) * (i + 1);

            float u = (1.0f / max) * i;
            float u2 = ((1.0f / max) * i) + (1.0f / max);

            float bottomU = MathHelper.lerp(u, 0 + multiplier, 1 - multiplier);
            float bottomU2 = MathHelper.lerp(u2, 0 + multiplier, 1 - multiplier);

            bufferBuilder.vertex(matrix4f, (float)thisX, (float)y1, (float)0).texture(u + floorX, v1);
            bufferBuilder.vertex(matrix4f, (float)thisX, (float)y2, (float)0).texture(bottomU + floorX, v2);
            bufferBuilder.vertex(matrix4f, (float)nextX, (float)y2, (float)0).texture(bottomU2 + floorX, v2);
            bufferBuilder.vertex(matrix4f, (float)nextX, (float)y1, (float)0).texture(u2 + floorX, v1);
        }

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }

    @Override
    public void close() {
        FreddyEntity.remove(Entity.RemovalReason.DISCARDED);
        super.close();
    }

    @Override
    protected void renderTabIcon(DrawContext context, ItemGroup group) {
        final FabricItemGroupImpl fabricItemGroup = (FabricItemGroupImpl) group;

        if(!Objects.equals(group.getDisplayName().getString(), FnafUniverseRebuilt.MOD_ID) || fabricItemGroup.fabric_getPage() != getCurrentPage()){
            super.renderTabIcon(context, group);
            return;
        }

        boolean bl = group == getSelectedItemGroup();
        boolean bl2 = group.getRow() == ItemGroup.Row.TOP;
        int i = group.getColumn();
        int j = this.x + this.getTabX(group);
        int k = this.y - (bl2 ? 28 : -(this.backgroundHeight - 4));

        Identifier texture;
        if (bl2) {
            texture = bl ? Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/tab_top_selected.png") : Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/tab_top_unselected.png");
        } else {
            texture = bl ? Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/tab_bottom_selected.png") : Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/tab_bottom_unselected.png");
        }

        float p = FreddyEntity.getScale();

        Quaternionf quaternionf = new Quaternionf().rotateZ((float) Math.PI);
        Quaternionf quaternionf2 = new Quaternionf().rotateY((float) Math.PI);
        Vector3f vector3f = new Vector3f(0.0F, FreddyEntity.getHeight() / 2.0F + 0.0625F * p, 0.0F);
        quaternionf.mul(quaternionf2);
        //InventoryScreen.drawEntity(context, j + 13, k, 20, vector3f, quaternionf, quaternionf2, FreddyEntity);

        //GoopyScreen.drawRecolorableTexture(context, texture, j, k, 32, 26, 0, 0, 32, 26, 0xFFFFFFFF);
        context.drawTexture(RenderLayer::getGuiTextured, texture, j, k, 0, 0, 26, 32, 26, 32);
    }
    private int getTabX(ItemGroup group) {
        int i = group.getColumn();
        int j = 27;
        int k = 27 * i;
        if (group.isSpecial()) {
            k = this.backgroundWidth - 27 * (7 - i) + 1;
        }

        return k;
    }
    public enum FnafSubTab{
        DEFAULT(0),
        ANIMATRONICS(1),
        BLOCKS(2),
        DECORATION(3),
        TECHNICAL(4),
        ;
        public final int index;
        FnafSubTab(int index){
            this.index = index;
        }
    }
}
