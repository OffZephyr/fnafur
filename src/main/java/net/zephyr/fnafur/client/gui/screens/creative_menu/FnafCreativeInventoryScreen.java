package net.zephyr.fnafur.client.gui.screens.creative_menu;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.impl.itemgroup.FabricItemGroupImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.HotbarStorage;
import net.minecraft.client.option.HotbarStorageEntry;
import net.minecraft.client.texture.TextureSetup;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.util.math.MathHelper;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.client.gui.screens.other.FullTexturedQuadGuiElementRenderState;
import net.zephyr.fnafur.networking.entity.player.UpdateCreativeExtraSlotsC2SPayload;
import net.zephyr.fnafur.util.EasingMathUtil;
import net.zephyr.fnafur.util.mixinAccessing.ICreativeScreenSlotAccessor;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class FnafCreativeInventoryScreen extends CreativeInventoryScreen {
    public static final int SLOTS_X = 127;
    public static final int SLOT_AR_Y = 10;
    public static final int SLOT_IL_Y = 29;
    public static boolean isFnafTab = false;
    public float floorX = 0;
    public float garageDoorIndex = 0;
    public static FnafSubTab SubTab = FnafSubTab.DEFAULT;
    public static FnafSubTab GoalSubTab = FnafSubTab.DEFAULT;
    public static final Identifier SLOTS = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/creative_inventory_mask_slot.png");
    public static final Identifier BLOCKS_BACK = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/blocks_back.png");

    public FnafCreativeInventoryScreen(ClientPlayerEntity player, FeatureSet enabledFeatures, boolean operatorTabEnabled) {
        super(player, enabledFeatures, operatorTabEnabled);
        SubTab = FnafSubTab.DEFAULT;
        GoalSubTab = FnafSubTab.DEFAULT;

        new CategoryPicker("back", 0, 0, 14, 16, 0, 125, 0, 109, BLOCKS_BACK, this::BLOCKS_BACK);
        new CategoryPicker("tiles", 18, 0, 14, 16, 14, 125, 14, 109, BLOCKS_BACK, this::SUBTAB_OPEN);
        new CategoryPicker("bricks", 36, 0, 14, 16, 28, 125, 28, 109, BLOCKS_BACK, this::SUBTAB_OPEN);
        new CategoryPicker("carpets", 54, 0, 14, 16, 42, 125, 42, 109, BLOCKS_BACK, this::SUBTAB_OPEN);
        new CategoryPicker("wood", 0, 18, 14, 16, 56, 125, 56, 109, BLOCKS_BACK, this::SUBTAB_OPEN);
        new CategoryPicker("glass", 18, 18, 14, 16, 70, 125, 70, 109, BLOCKS_BACK, this::SUBTAB_OPEN);
        new CategoryPicker("building", 36, 18, 14, 16, 84, 125, 84, 109, BLOCKS_BACK, this::SUBTAB_OPEN);
        new CategoryPicker("other", 54, 18, 14, 16, 98, 125, 98, 109, BLOCKS_BACK, this::SUBTAB_OPEN);
    }

    private void SUBTAB_OPEN(String s) {
        System.out.println(s);
    }

    private void BLOCKS_BACK(String name) {
        GoalSubTab = FnafSubTab.DEFAULT;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        floorX += delta/100f;

        isFnafTab = Objects.equals(getSelectedItemGroup().getDisplayName().getString(), FnafUniverseRebuilt.MOD_ID);
        if (isFnafTab) {
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

            if (GoalSubTab != SubTab) {
                if (garageDoorIndex <= 0) garageDoorIndex = 1;
                if (garageDoorIndex <= 0.5f) SubTab = GoalSubTab;
            }

            garageDoorIndex = garageDoorIndex > 0 ? garageDoorIndex - (delta / 20f) : 0;
            double garageHeightIndex = EasingMathUtil.easeInOutQuad(1 - MathHelper.abs(((garageDoorIndex)*2) - 1));
            int garageHeight = (int) MathHelper.lerp(garageHeightIndex, 0, this.backgroundHeight - 26);

            context.drawTexture(RenderPipelines.GUI_TEXTURED, getSelectedItemGroup().getTexture(), this.x + 7, this.y + 20, 32, 136 + (this.backgroundHeight - 27 - garageHeight), this.backgroundWidth - 14, garageHeight, 256, 256);

            this.drawMouseoverTooltip(context, mouseX, mouseY);
            return;
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void drawSlot(DrawContext context, Slot slot) {

        super.drawSlot(context, slot);
    }

    public void renderDefaultBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        Identifier gridTexture = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/tab_grid.png");
        context.drawTexture(RenderPipelines.GUI_TEXTURED, gridTexture, this.x + 7, this.y + 20, 0, 0, this.backgroundWidth - 2, this.backgroundHeight - 2, 256, 256);

        Identifier icon1 = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/icon_animatronics.png");
        Identifier icon2 = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/icon_blocks.png");
        Identifier icon3 = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/icon_props.png");
        Identifier icon4 = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/icon_technical.png");

        int x1 = this.x + 10;
        int y1 = this.y + 23;

        int x2 = this.x + 101;
        int y2 = this.y + 78;

        boolean b1 = GoopyScreen.isOnButton(mouseX, mouseY, x1, y1, 84, 48);
        boolean b2 = GoopyScreen.isOnButton(mouseX, mouseY, x2, y1, 84, 48);
        boolean b3 = GoopyScreen.isOnButton(mouseX, mouseY, x1, y2, 84, 48);
        boolean b4 = GoopyScreen.isOnButton(mouseX, mouseY, x2, y2, 84, 48);
        int v1 = b1 ? 96 : 48;
        int v2 = b2 ? 96 : 48;
        int v3 = b3 ? 96 : 48;
        int v4 = b4 ? 96 : 48;

        context.drawTexture(RenderPipelines.GUI_TEXTURED, icon1, x1, y1, 0, 0, 84, 48, 256, 256);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, icon2, x2, y1, 0, 0, 84, 48, 256, 256);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, icon3, x1, y2, 0, 0, 84, 48, 256, 256);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, icon4, x2, y2, 0, 0, 84, 48, 256, 256);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, icon1, x1, y1, 0, v1, 84, 48, 256, 256);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, icon2, x2, y1, 0, v2, 84, 48, 256, 256);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, icon3, x1, y2, 0, v3, 84, 48, 256, 256);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, icon4, x2, y2, 0, v4, 84, 48, 256, 256);

        if(b1 || b2 || b3 || b4){
            Text text = b1 ? Text.translatable("fnafur.screens.creative_categories.animatronics") : b2 ? Text.translatable("fnafur.screens.creative_categories.blocks") : b3 ? Text.translatable("fnafur.screens.creative_categories.props") : Text.translatable("fnafur.screens.creative_categories.technical");
            context.drawTooltip(textRenderer, text, mouseX, mouseY);
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        if(garageDoorIndex != 0){
            return false;
        }
        if(isFnafTab && SubTab != FnafSubTab.DEFAULT){
            GoalSubTab = FnafSubTab.DEFAULT;
            return false;
        }
        return super.shouldCloseOnEsc();
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        return super.keyPressed(input);
    }

    public void renderAnimatronicsBackground(DrawContext context, int mouseX, int mouseY, float delta) {

    }

    public void renderBlocksBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        //Identifier floorTexture = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/storage_floor.png");
        //drawFloor(RenderPipelines.GUI_TEXTURED, context, delta, mouseX, mouseY, floorTexture, this.x + 1, this.x + this.backgroundWidth - 1, this.y + ((this.backgroundHeight) / 4 * 3), this.y + this.backgroundHeight - 1);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, BLOCKS_BACK, this.x + 7, this.y + 20, 0, 0, this.backgroundWidth - 8, this.backgroundHeight - 21, 256, 256);
        for(CategoryPicker picker : CategoryPicker.ALL){
            picker.draw(context, this.x + 7 + 29, this.y + 20 + 14, mouseX, mouseY);
        }
    }

    public void renderPropsBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        Identifier floorTexture = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/storage_floor.png");
        drawFloor(RenderPipelines.GUI_TEXTURED, context, delta, mouseX, mouseY, floorTexture, this.x + 1, this.x + this.backgroundWidth - 1, this.y + ((this.backgroundHeight) / 4 * 3), this.y + this.backgroundHeight - 1);

    }

    public void renderTechBackground(DrawContext context, int mouseX, int mouseY, float delta) {

    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (isFnafTab) {
            if(garageDoorIndex != 0){
                return false;
            }
            switch (SubTab) {
                default -> clickDefault(click.x(), click.y(), click.button());
                case ANIMATRONICS -> clickAnimatronics(click.x(), click.y(), click.button());
                case BLOCKS -> clickBlocks(click.x(), click.y(), click.button());
                case DECORATION -> clickProps(click.x(), click.y(), click.button());
                case TECHNICAL -> clickTech(click.x(), click.y(), click.button());
            }
        }
        else{
            garageDoorIndex = 0;
            SubTab = FnafSubTab.DEFAULT;
            GoalSubTab = FnafSubTab.DEFAULT;
        }

        return super.mouseClicked(click, doubled);
    }

    public void clickDefault(double mouseX, double mouseY, int button) {
        int x1 = this.x + 10;
        int y1 = this.y + 23;

        int x2 = this.x + 101;
        int y2 = this.y + 78;

        if (GoopyScreen.isOnButton(mouseX, mouseY, x1, y1, 84, 48)) GoalSubTab = FnafSubTab.ANIMATRONICS;
        if (GoopyScreen.isOnButton(mouseX, mouseY, x2, y1, 84, 48)) GoalSubTab = FnafSubTab.BLOCKS;
        if (GoopyScreen.isOnButton(mouseX, mouseY, x1, y2, 84, 48)) GoalSubTab = FnafSubTab.DECORATION;
        if (GoopyScreen.isOnButton(mouseX, mouseY, x2, y2, 84, 48)) GoalSubTab = FnafSubTab.TECHNICAL;


    }

    public void clickAnimatronics(double mouseX, double mouseY, int button) {

    }

    public void clickBlocks(double mouseX, double mouseY, int button) {
        if(CategoryPicker.HOVERED != null){
            CategoryPicker.HOVERED.onClick();
        }
    }

    public void clickProps(double mouseX, double mouseY, int button) {

    }

    public void clickTech(double mouseX, double mouseY, int button) {

    }

    protected void drawFnafBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        for (ItemGroup itemGroup : ItemGroups.getGroupsToDisplay()) {
            if (itemGroup != getSelectedItemGroup() || Objects.equals(itemGroup.getDisplayName().getString(), FnafUniverseRebuilt.MOD_ID)) {
                this.renderTabIcon(context, itemGroup);
            }
        }

        switch (SubTab) {
            default -> renderDefaultBackground(context, mouseX, mouseY, delta);
            case ANIMATRONICS -> renderAnimatronicsBackground(context, mouseX, mouseY, delta);
            case BLOCKS -> renderBlocksBackground(context, mouseX, mouseY, delta);
            case DECORATION -> renderPropsBackground(context, mouseX, mouseY, delta);
            case TECHNICAL -> renderTechBackground(context, mouseX, mouseY, delta);
        }

        context.drawTexture(RenderPipelines.GUI_TEXTURED, getSelectedItemGroup().getTexture(), this.x, this.y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);

        context.drawTexture(RenderPipelines.GUI_TEXTURED, getSelectedItemGroup().getTexture(), this.x + 8, this.y + 6, 0.0F, this.backgroundHeight + (SubTab.index * 5), 28, 5, 256, 256);

        if (!isFnafTab) {
            this.renderTabIcon(context, getSelectedItemGroup());
        }
    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
        super.drawBackground(context, deltaTicks, mouseX, mouseY);
        if (isInventoryTabSelected()) {
            context.drawTexture(RenderPipelines.GUI_TEXTURED, SLOTS, this.x, this.y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
        }
    }

    protected void drawFloor(RenderPipeline pipeline, DrawContext context, float delta, int mouseX, int mouseY, Identifier texture, int x, int maxX, int y, int maxY) {
        GpuTextureView gpuTextureView = this.client.getTextureManager().getTexture(texture).getGlTextureView();
        drawFloor(pipeline, context, delta, mouseX, mouseY, gpuTextureView, x, maxX, y, maxY);
    }
    private void renderRoom(RenderPipeline pipeline, DrawContext context, float delta, int mouseX, int mouseY, GpuTextureView texture, int x, int maxX, int y, int maxY) {

    }
    private void drawFloor(RenderPipeline pipeline, DrawContext context, float delta, int mouseX, int mouseY, GpuTextureView texture, int x, int maxX, int y, int maxY) {

        float v1 = 0;
        float v2 = 1;

        int y1 = y;
        int y2 = maxY;

        int max = 128;
        float multiplier = 0.25f;
        for(int i = 0; i < max; i++){
            float thisX = x + ((maxX - (1.0f * x))/max) * i;
            float nextX = x + ((maxX - (1.0f * x))/max) * (i + 1);

            float u = (1.0f / max) * i;
            float u2 = ((1.0f / max) * i) + (1.0f / max);

            float bottomU = MathHelper.lerp(u, 0 + multiplier, 1 - multiplier);
            float bottomU2 = MathHelper.lerp(u2, 0 + multiplier, 1 - multiplier);


            context.state
                    .addSimpleElement(
                            new FullTexturedQuadGuiElementRenderState(
                                    pipeline, TextureSetup.withoutGlTexture(texture), new Matrix3x2f(context.getMatrices()), (int)thisX, y1, (int)thisX, y2, (int)nextX, y2, (int)nextX, y1, u + floorX, bottomU + floorX, bottomU2 + floorX, u2 + floorX, v1, v2, v2, v1, 0xFFFFFFFF, context.scissorStack.peekLast()
                            )
                    );

            //buffer.vertex(matrix4f, (float)thisX, (float)y1, (float)0).texture(u + floorX, v1);
            //buffer.vertex(matrix4f, (float)thisX, (float)y2, (float)0).texture(bottomU + floorX, v2);
            //buffer.vertex(matrix4f, (float)nextX, (float)y2, (float)0).texture(bottomU2 + floorX, v2);
            //buffer.vertex(matrix4f, (float)nextX, (float)y1, (float)0).texture(u2 + floorX, v1);


            //context.drawTexturedQuad(texture, (int) nextX, (int) thisX, y2, y1, u + floorX, bottomU + floorX, v1, v2);
        }
    }

    @Override
    public void close() {

        //FreddyEntity.remove(Entity.RemovalReason.DISCARDED);

        super.close();
    }

    @Override
    protected void renderTabIcon(DrawContext context, ItemGroup group) {
        final FabricItemGroupImpl fabricItemGroup = (FabricItemGroupImpl) group;

        if (!Objects.equals(group.getDisplayName().getString(), FnafUniverseRebuilt.MOD_ID) || fabricItemGroup.fabric_getPage() != getCurrentPage()) {
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

        //GoopyScreen.drawRecolorableTexture(context, texture, j, k, 32, 26, 0, 0, 32, 26, 0xFFFFFFFF);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, texture, j, k, 0, 0, 26, 32, 26, 32);
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

    public enum FnafSubTab {
        DEFAULT(0),
        ANIMATRONICS(1),
        BLOCKS(2),
        DECORATION(3),
        TECHNICAL(4),
        ;
        public final int index;

        FnafSubTab(int index) {
            this.index = index;
        }
    }

    public List<Slot> createSlots(ItemGroup selectedTab, ItemGroup itemGroup, List<Slot> slots, Slot deleteItemSlot) {
        this.cursorDragSlots.clear();
        this.handler.itemList.clear();
        this.endTouchDrag();
        if (selectedTab.getType() == ItemGroup.Type.HOTBAR) {
            HotbarStorage hotbarStorage = this.client.getCreativeHotbarStorage();

            for (int i = 0; i < 9; i++) {
                HotbarStorageEntry hotbarStorageEntry = hotbarStorage.getSavedHotbar(i);
                if (hotbarStorageEntry.isEmpty()) {
                    for (int j = 0; j < 9; j++) {
                        if (j == i) {
                            ItemStack itemStack = new ItemStack(Items.PAPER);
                            itemStack.set(DataComponentTypes.CREATIVE_SLOT_LOCK, Unit.INSTANCE);
                            Text text = this.client.options.hotbarKeys[i].getBoundKeyLocalizedText();
                            Text text2 = this.client.options.saveToolbarActivatorKey.getBoundKeyLocalizedText();
                            itemStack.set(DataComponentTypes.ITEM_NAME, Text.translatable("inventory.hotbarInfo", text2, text));
                            this.handler.itemList.add(itemStack);
                        } else {
                            this.handler.itemList.add(ItemStack.EMPTY);
                        }
                    }
                } else {
                    this.handler.itemList.addAll(hotbarStorageEntry.deserialize(this.client.world.getRegistryManager()));
                }
            }
        } else if (selectedTab.getType() == ItemGroup.Type.CATEGORY) {
            this.handler.itemList.addAll(selectedTab.getDisplayStacks());
        }

        if (selectedTab.getType() == ItemGroup.Type.INVENTORY) {
            ScreenHandler screenHandler = this.client.player.playerScreenHandler;
            if (slots == null) {
                slots = ImmutableList.copyOf(this.handler.slots);
            }

            this.handler.slots.clear();

            for (int ix = 0; ix < screenHandler.slots.size(); ix++) {
                int n;
                int jx;
                if (ix >= 5 && ix < 9) {
                    int k = ix - 5;
                    int l = k / 2;
                    int m = k % 2;
                    n = 54 + l * 54;
                    jx = 6 + m * 27;
                } else if (ix >= 0 && ix < 5) {
                    n = -2000;
                    jx = -2000;
                } else if (ix == 45) {
                    n = 35;
                    jx = 20;
                } else if (screenHandler.slots.get(ix).getIndex() == FnafInventoryScreen.SLOTS_OFFSET) {
                    n = 127;
                    jx = 10;
                } else if (screenHandler.slots.get(ix).getIndex() == FnafInventoryScreen.SLOTS_OFFSET + 1) {
                    n = 127;
                    jx = 29;
                } else {
                    int k = ix - 9;
                    int l = k % 9;
                    int m = k / 9;
                    n = 9 + l * 18;
                    if (ix >= 36) {
                        jx = 112;
                    } else {
                        jx = 54 + m * 18;
                    }
                }

                Slot slot = ((ICreativeScreenSlotAccessor)this).createCreativeSlot(screenHandler.slots.get(ix), ix, n, jx);
                this.handler.slots.add(slot);
            }
            this.handler.slots.add(deleteItemSlot);
        } else if (itemGroup.getType() == ItemGroup.Type.INVENTORY) {
            this.handler.slots.clear();
            this.handler.slots.addAll(slots);
            slots = null;
        }
        this.handler.scrollItems(0.0F);

        return slots;
    }

    @Override
    public void onMouseClick(@Nullable Slot slot, SlotActionType actionType) {
        super.onMouseClick(slot, actionType);
    }

    @Override
    protected void onMouseClick(@Nullable Slot slot, int slotId, int button, SlotActionType actionType) {
        super.onMouseClick(slot, slotId, button, actionType);

        NbtCompound data = new NbtCompound();
        ItemStack stack1 = MinecraftClient.getInstance().player.getInventory().getStack(FnafInventoryScreen.SLOTS_OFFSET);
        ItemStack stack2 = MinecraftClient.getInstance().player.getInventory().getStack(FnafInventoryScreen.SLOTS_OFFSET + 1);
        data.putBoolean("hasStack1", !stack1.isEmpty());
        data.putBoolean("hasStack2", !stack2.isEmpty());
        if(data.getBoolean("hasStack1", false)){
            data.put("stack1", ItemStack.CODEC, stack1);
        }
        if(data.getBoolean("hasStack2", false)){
            data.put("stack2", ItemStack.CODEC, stack2);
        }
        ClientPlayNetworking.send(new UpdateCreativeExtraSlotsC2SPayload(data));
    }


    @Environment(EnvType.CLIENT)
    public static class FnafCreativeSlot extends CreativeSlot {
        public FnafCreativeSlot(Slot slot, int invSlot, int x, int y) {
            super(slot, invSlot, x, y);
        }
    }

    public static class CategoryPicker{
        public String name;
        public int posX;
        public int posY;
        public int width;
        public int height;
        public int u;
        public int v;
        public int u_active;
        public int v_active;
        public Identifier texture;
        public ButtonAction action;
        boolean isHovered = false;
        public static List<CategoryPicker> ALL = new ArrayList<>();
        public static CategoryPicker HOVERED = null;

        public CategoryPicker(String name, int posX, int posY, int width, int height, int u, int v, int u_active, int v_active, Identifier texture, ButtonAction action){
            this.name = name;
            this.posX = posX;
            this.posY = posY;
            this.width = width;
            this.height = height;
            this.u = u;
            this.v = v;
            this.u_active = u_active;
            this.v_active = v_active;
            this.texture = texture;
            this.action = action;
            ALL.add(this);
        }

        public void draw(DrawContext context, int offsetX, int offsetY, int mouseX, int mouseY){
            isHovered = GoopyScreen.isOnButton(mouseX, mouseY, posX + offsetX, posY + offsetY, width, height);
            context.drawTexture(RenderPipelines.GUI_TEXTURED, texture, offsetX + posX, offsetY + posY, isHovered ? u_active : u, isHovered ? v_active : v, width, height, 256, 256);

            if(isHovered){
                HOVERED = this;
            }
            if(HOVERED == this && !isHovered){
                HOVERED = null;
            }
        }

        public void onClick() {
            action.execute(name);
        }
    }

    @FunctionalInterface
    public interface ButtonAction {
        void execute(String name);
    }
}
