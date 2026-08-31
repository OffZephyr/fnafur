package net.zephyr.fnafur.client.gui.screens.creative_menu;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.impl.itemgroup.FabricItemGroupImpl;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen.ItemPickerMenu;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen.SlotWrapper;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.HotbarManager;
import net.minecraft.client.player.inventory.Hotbar;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.MimicFrames;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.diagonal.DiagonalMimicFrame;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.client.gui.screens.other.FullTexturedQuadGuiElementRenderState;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.item_init.ItemCategoriesInit;
import net.zephyr.fnafur.networking.entity.player.UpdateCreativeExtraSlotsC2SPayload;
import net.zephyr.fnafur.util.EasingMathUtil;
import net.zephyr.fnafur.util.mixinAccessing.ICreativeScreenSlotAccessor;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;
import org.joml.Quaternionf;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Environment(EnvType.CLIENT)
public class FnafCreativeInventoryScreen extends CreativeModeInventoryScreen {
    public static final int SLOTS_X = 127;
    public static final int SLOT_AR_Y = 10;
    public static final int SLOT_IL_Y = 29;
    public static boolean isFnafTab = false;
    public float floorX = 0;
    public float garageDoorIndex = 0;
    public static FnafSubTab SubTab = FnafSubTab.DEFAULT;
    public static FnafSubTab GoalSubTab = FnafSubTab.DEFAULT;
    public static FnafSubTab PreviousSubTab = FnafSubTab.DEFAULT;
    public static final Identifier SLOTS = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/creative_inventory_mask_slot.png");
    public static final Identifier BLOCKS_BACK = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/blocks_back.png");
    public static final Identifier ITEM_SELECTION_WITH_ALTS_BACK = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/slots.png");
    public static final Identifier ITEM_SELECTION_WITH_ALTS_BACK_BLUE = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/slots_blue.png");
    public CategoryPicker activeCategory = null;
    private int selectedItemCategory = -1;
    private int selectedItemCategoryOffset = 0;
    private int lastPreviewStackIndex = 0;
    private int itemCategoryScroll = 0;
    private float backgroundOffset = 0;
    private boolean isPreviewTypeTiling = false;
    int hovered_slot = -1;

    static Map<String, List<String>> CODE_MAP = Map.of(
            "bear5", List.of("BEAR5", "BEARFIVE", "FIVEBEAR", "5BEAR", "BEQR5", "BEQRFIVE", "FIVEBEQR", "5BEQR")
    );

    static Map<String, FnafSubTab> CODE_FOR_TAB = Map.of(
            "bear5", FnafSubTab.BEAR5
    );

    static Map<String, CategoryPicker> CODE_FOR_CATEGORY = Map.of(
            "bear5", new CategoryPicker("bear5", 54, 18, 14, 16, 98, 125, 98, 109, BLOCKS_BACK, ItemCategoriesInit.BEAR_FIVE)
    );

    String currentCode = "";

    public FnafCreativeInventoryScreen(LocalPlayer player, FeatureFlagSet enabledFeatures, boolean operatorTabEnabled) {
        super(player, enabledFeatures, operatorTabEnabled);
        SubTab = FnafSubTab.DEFAULT;
        GoalSubTab = FnafSubTab.DEFAULT;

        new CategoryPicker("back", 0, 0, 14, 16, 0, 125, 0, 109, BLOCKS_BACK, null, this::BLOCKS_BACK);
        new CategoryPicker("tiles", 18, 0, 14, 16, 14, 125, 14, 109, BLOCKS_BACK, ItemCategoriesInit.TILES, this::SUBTAB_OPEN);
        new CategoryPicker("bricks", 36, 0, 14, 16, 28, 125, 28, 109, BLOCKS_BACK, ItemCategoriesInit.BRICKS, this::SUBTAB_OPEN);
        new CategoryPicker("carpets", 54, 0, 14, 16, 42, 125, 42, 109, BLOCKS_BACK, ItemCategoriesInit.CARPETS, this::SUBTAB_OPEN);
        new CategoryPicker("wood", 0, 18, 14, 16, 56, 125, 56, 109, BLOCKS_BACK, ItemCategoriesInit.WOOD, this::SUBTAB_OPEN);
        new CategoryPicker("glass", 18, 18, 14, 16, 70, 125, 70, 109, BLOCKS_BACK, ItemCategoriesInit.GLASS, this::SUBTAB_OPEN);
        new CategoryPicker("building", 36, 18, 14, 16, 84, 125, 84, 109, BLOCKS_BACK, ItemCategoriesInit.BUILDING, this::SUBTAB_OPEN);
        new CategoryPicker("other", 54, 18, 14, 16, 98, 125, 98, 109, BLOCKS_BACK, ItemCategoriesInit.OTHER, this::SUBTAB_OPEN);
    }

    private void SUBTAB_OPEN(String s) {
        System.out.println(s);
        PreviousSubTab = SubTab;
        GoalSubTab = FnafSubTab.ITEM_WITH_ALTS_SELECTION;

    }

    private void BLOCKS_BACK(String name) {
        GoalSubTab = FnafSubTab.DEFAULT;
    }

    @Override
    protected void init() {
        BlockInit.randomize = false;
        super.init();
    }

    @Override
    public void containerTick() {
        super.containerTick();
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        floorX += delta/100f;

        isFnafTab = Objects.equals(getSelectedItemGroup().getDisplayName().getString(), FnafUniverseRebuilt.MOD_ID);
        if (isFnafTab) {
            //this.renderBackground(context, mouseX, mouseY, delta);
            context.fill(this.leftPos + 1, this.topPos + 1, this.leftPos + this.imageWidth - 2, this.topPos + this.imageHeight - 2, 0xFF000000);

            int i = this.leftPos;
            int j = this.topPos;
            this.drawFnafBackground(context, delta, mouseX, mouseY);

            for (CreativeModeTab itemGroup : CreativeModeTabs.tabs()) {
                if (this.checkTabHovering(context, itemGroup, mouseX, mouseY)) {
                    break;
                }
            }

            if (GoalSubTab != SubTab) {
                if (garageDoorIndex <= 0) garageDoorIndex = 1;
                if (garageDoorIndex <= 0.5f) SubTab = GoalSubTab;
            }

            garageDoorIndex = garageDoorIndex > 0 ? garageDoorIndex - (delta / 20f) : 0;
            double garageHeightIndex = EasingMathUtil.easeInOutQuad(1 - Mth.abs(((garageDoorIndex)*2) - 1));
            int garageHeight = (int) Mth.lerp(garageHeightIndex, 0, this.imageHeight - 26);

            context.blit(RenderPipelines.GUI_TEXTURED, getSelectedItemGroup().getBackgroundTexture(), this.leftPos + 7, this.topPos + 20, 32, 136 + (this.imageHeight - 27 - garageHeight), this.imageWidth - 14, garageHeight, 256, 256);

            renderCarriedItem(context, mouseX, mouseY);
            this.renderTooltip(context, mouseX, mouseY);
            return;
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void renderSlot(GuiGraphics context, Slot slot, int mouseX, int mouseY) {
        super.renderSlot(context, slot, mouseX, mouseY);
    }

    public void renderDefaultBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {
        Identifier gridTexture = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/tab_grid.png");
        context.blit(RenderPipelines.GUI_TEXTURED, gridTexture, this.leftPos + 7, this.topPos + 20, 0, 0, this.imageWidth - 2, this.imageHeight - 2, 256, 256);

        Identifier icon1 = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/icon_animatronics.png");
        Identifier icon2 = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/icon_blocks.png");
        Identifier icon3 = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/icon_props.png");
        Identifier icon4 = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/icon_technical.png");

        int x1 = this.leftPos + 10;
        int y1 = this.topPos + 23;

        int x2 = this.leftPos + 101;
        int y2 = this.topPos + 78;

        boolean b1 = GoopyScreen.isOnButton(mouseX, mouseY, x1, y1, 84, 48);
        boolean b2 = GoopyScreen.isOnButton(mouseX, mouseY, x2, y1, 84, 48);
        boolean b3 = GoopyScreen.isOnButton(mouseX, mouseY, x1, y2, 84, 48);
        boolean b4 = GoopyScreen.isOnButton(mouseX, mouseY, x2, y2, 84, 48);
        int v1 = b1 ? 96 : 48;
        int v2 = b2 ? 96 : 48;
        int v3 = b3 ? 96 : 48;
        int v4 = b4 ? 96 : 48;

        context.blit(RenderPipelines.GUI_TEXTURED, icon1, x1, y1, 0, 0, 84, 48, 256, 256);
        context.blit(RenderPipelines.GUI_TEXTURED, icon2, x2, y1, 0, 0, 84, 48, 256, 256);
        context.blit(RenderPipelines.GUI_TEXTURED, icon3, x1, y2, 0, 0, 84, 48, 256, 256);
        context.blit(RenderPipelines.GUI_TEXTURED, icon4, x2, y2, 0, 0, 84, 48, 256, 256);
        context.blit(RenderPipelines.GUI_TEXTURED, icon1, x1, y1, 0, v1, 84, 48, 256, 256);
        context.blit(RenderPipelines.GUI_TEXTURED, icon2, x2, y1, 0, v2, 84, 48, 256, 256);
        context.blit(RenderPipelines.GUI_TEXTURED, icon3, x1, y2, 0, v3, 84, 48, 256, 256);
        context.blit(RenderPipelines.GUI_TEXTURED, icon4, x2, y2, 0, v4, 84, 48, 256, 256);

        if(b1 || b2 || b3 || b4){
            Component text = b1 ? Component.translatable("fnafur.screens.creative_categories.animatronics") : b2 ? Component.translatable("fnafur.screens.creative_categories.blocks") : b3 ? Component.translatable("fnafur.screens.creative_categories.props") : Component.translatable("fnafur.screens.creative_categories.technical");
            context.setTooltipForNextFrame(font, text, mouseX, mouseY);
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        if(isFnafTab){
            if(garageDoorIndex == 0 && SubTab != FnafSubTab.DEFAULT){
                selectedItemCategory = -1;
                itemCategoryScroll = 0;
                selectedItemCategoryOffset = 0;
                lastPreviewStackIndex = 0;
                if(PreviousSubTab != null){
                    GoalSubTab = PreviousSubTab;
                    PreviousSubTab = FnafSubTab.DEFAULT;
                    return false;
                }
                GoalSubTab = FnafSubTab.DEFAULT;
                return false;
            }
            if(garageDoorIndex != 0){
                SubTab = GoalSubTab;
                garageDoorIndex = 0;
                return false;
            }
        }
        return super.shouldCloseOnEsc();
    }

    @Override
    public boolean keyPressed(KeyEvent input) {

        if(isFnafTab){
            if(!input.isEscape() && SubTab == FnafSubTab.ITEM_WITH_ALTS_SELECTION && selectedItemCategory != -1){
                ItemCategory.Entry entry = (ItemCategory.Entry) activeCategory.itemCategory.ITEM_ENTRIES.toArray()[selectedItemCategory];

                for (int x = 0; x < 9; x++) {
                    if (entry.items().isEmpty()) continue;
                    if (lastPreviewStackIndex > entry.items().size() - 1) continue;
                    ItemStack stack = (ItemStack) entry.items().toArray()[lastPreviewStackIndex];
                    if(hovered_slot - selectedItemCategoryOffset == x) {
                        int slotOffset = input.getDigit() - 1;
                        if(slotOffset < 0 || slotOffset > 8)  continue;
                        int slotID = 45 + slotOffset;

                        Slot slot = this.menu.getSlot(slotID);
                        if (slot == null) {
                            continue;
                        }
                        this.menu.setItem(slotID, this.menu.incrementStateId(), stack.copyWithCount(stack.getMaxStackSize()));

                        this.minecraft.player.inventoryMenu.broadcastChanges();
                        return false;
                    }
                }
            }
            if(!input.isEscape() && SubTab == FnafSubTab.DEFAULT){

                String in = InputConstants.getKey(input).getDisplayName().getString();
                String combined = currentCode + in;
                AtomicBoolean playSound = new AtomicBoolean(true);
                AtomicBoolean doPlaySound = new AtomicBoolean(false);

                List<String> codes = new ArrayList<>();
                CODE_MAP.forEach((index, list) ->{
                    list.forEach((code) -> {
                        if(code.contains(currentCode)){

                            if(code.contains(combined)) {
                                currentCode = combined;
                                codes.add(code);
                                if (code.equals(currentCode)) {
                                    GoalSubTab = CODE_FOR_TAB.get(index);
                                    activeCategory = CODE_FOR_CATEGORY.get(index);
                                    currentCode = in;
                                }
                            }
                        }

                        if((currentCode.length() <= 1 && code.charAt(0) == in.charAt(0)) || (!currentCode.isEmpty() && code.charAt(0) == currentCode.charAt(0))){
                            doPlaySound.set(true);
                        }
                    });

                    if(codes.isEmpty()){
                        currentCode = in;
                    }

                    if(playSound.get() && doPlaySound.get()){
                        Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, Math.min(1, 0.5f + (currentCode.length() / 10f)));
                        playSound.set(false);
                        doPlaySound.set(false);
                    }
                });
                System.out.println(currentCode);
                if(currentCode.length() <= 1){
                    return super.keyPressed(input);
                }
                return false;
            }
        }
        return super.keyPressed(input);
    }

    public void renderAnimatronicsBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {

    }

    public void renderBlocksBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {
        //Identifier floorTexture = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/storage_floor.png");
        //drawFloor(RenderPipelines.GUI_TEXTURED, context, delta, mouseX, mouseY, floorTexture, this.x + 1, this.x + this.backgroundWidth - 1, this.y + ((this.backgroundHeight) / 4 * 3), this.y + this.backgroundHeight - 1);
        context.blit(RenderPipelines.GUI_TEXTURED, BLOCKS_BACK, this.leftPos + 7, this.topPos + 20, 0, 0, this.imageWidth - 8, this.imageHeight - 21, 256, 256);
        for(CategoryPicker picker : CategoryPicker.ALL){
            picker.draw(context, this.leftPos + 7 + 29, this.topPos + 20 + 14, mouseX, mouseY);
        }
    }

    public void renderPropsBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {
        Identifier floorTexture = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/storage_floor.png");
        drawFloor(RenderPipelines.GUI_TEXTURED, context, delta, mouseX, mouseY, floorTexture, this.leftPos + 1, this.leftPos + this.imageWidth - 1, this.topPos + ((this.imageHeight) / 4 * 3), this.topPos + this.imageHeight - 1);

    }

    public void renderTechBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {

    }
    public void renderItemSelectionBackground(GuiGraphics context, Identifier back_texture, int text_color, int mouseX, int mouseY, float delta) {
        backgroundOffset += delta*20f;
        context.blit(RenderPipelines.GUI_TEXTURED, back_texture, this.leftPos + 7, this.topPos + 20, 0, 0, this.imageWidth - 8, this.imageHeight - 21, 256, 256);
        int leaveV = GoopyScreen.isOnButton(mouseX, mouseY, this.leftPos + 9, this.topPos + 22, 9, 9) ? 9 : 0;
        context.blit(RenderPipelines.GUI_TEXTURED, back_texture, this.leftPos + 9, this.topPos + 22, this.imageWidth -14, leaveV, 9, 9, 256, 256);

        for(int i = 0; i < 9; i++) {
            ItemStack stack = this.menu.slots.get(45 + i).getItem();
            Slot oldSlot = this.menu.slots.get(45 + i);
            //context.drawItem(stack, this.x + 18 + (i * 18), this.y + 110);
            Slot slot = new Slot(oldSlot.container, oldSlot.getContainerSlot(), this.leftPos + 18 + (i * 18), this.topPos + 110);
            slot.setByPlayer(oldSlot.getItem());
            renderSlot(context, slot, mouseX, mouseY);
            if(GoopyScreen.isOnButton(mouseX, mouseY, this.leftPos + 18 + (i * 18), this.topPos + 110, 16, 16)) {
                context.fill(this.leftPos + 18 + (i * 18), this.topPos + 110, this.leftPos + 34 + (i * 18), this.topPos + 126, 0x80FFFFFF);
                if(!stack.isEmpty()) {
                    context.setTooltipForNextFrame(this.font, stack.getHoverName(), mouseX, mouseY);
                }
            }
        }

        if(activeCategory != null) {
            context.blit(RenderPipelines.GUI_TEXTURED, back_texture, this.leftPos + 9, this.topPos + 90, this.imageWidth, 27, 7, 16, 256, 256);
            context.blit(RenderPipelines.GUI_TEXTURED, back_texture, this.leftPos + 180, this.topPos + 90, this.imageWidth, 43, 7, 16, 256, 256);
            if(activeCategory.itemCategory != null) {

                if(selectedItemCategory != -1) {

                    ItemCategory.Entry entry = (ItemCategory.Entry) activeCategory.itemCategory.ITEM_ENTRIES.toArray()[selectedItemCategory];

                    if(selectedItemCategoryOffset > 0){
                        int leftArrowU = GoopyScreen.isOnButton(mouseX, mouseY, this.leftPos + 9, this.topPos + 90, 7, 16) ? this.imageWidth - 7 : this.imageWidth - 14;
                        context.blit(RenderPipelines.GUI_TEXTURED, back_texture, this.leftPos + 9, this.topPos + 90, leftArrowU, 27, 7, 16, 256, 256);
                    }

                    if(selectedItemCategoryOffset < entry.items().size() - 9){
                        int rightArrowU = GoopyScreen.isOnButton(mouseX, mouseY, this.leftPos + 180, this.topPos + 90, 7, 16) ? this.imageWidth - 7 : this.imageWidth - 14;
                        context.blit(RenderPipelines.GUI_TEXTURED, back_texture, this.leftPos + 180, this.topPos + 90, rightArrowU, 43, 7, 16, 256, 256);
                    }

                    context.blit(RenderPipelines.GUI_TEXTURED, back_texture, this.leftPos + 17, this.topPos + 88, 0, 108, 162, 19, 256, 256);

                    int slotY = this.topPos + 90;

                    int x1 = this.leftPos + 90;
                    int y1 = this.topPos + 34;
                    int x2 = this.leftPos + 178;
                    int y2 = this.topPos + 86;
                    ItemStack previewStack = (ItemStack) entry.items().toArray()[lastPreviewStackIndex];
                    if (previewStack.getItem() instanceof BlockItem item) {
                        BlockState previewState = item.getBlock().defaultBlockState();
                        if (item.getBlock() instanceof GeoPropBlock) {

                        } else {

                            BlockStateModel model = minecraft.getModelManager().getBlockModelShaper().getBlockModel(item.getBlock().defaultBlockState());

                            TextureAtlasSprite sprite;

                            if(item.getBlock() instanceof MimicFrames frame){
                                int frameSize = frame.getMatrixSize() * frame.getMatrixSize();
                                sprite = Minecraft.getInstance().getBlockRenderer().materials.get(new Material(TextureAtlas.LOCATION_BLOCKS, Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/mimic_frame_" + frameSize)));
                            }
                            else if(item.getBlock() instanceof DiagonalMimicFrame){
                                sprite = Minecraft.getInstance().getBlockRenderer().materials.get(new Material(TextureAtlas.LOCATION_BLOCKS, Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/mimic_frame_1")));
                            }
                            else{
                                sprite =  model.collectParts(RandomSource.create()).getFirst().getQuads(Direction.NORTH).getFirst().sprite();

                            }


                            TextureManager textureManager = Minecraft.getInstance().getTextureManager();
                            Identifier id = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/" + sprite.contents().name().getPath() + ".png");
                            context.blit(RenderPipelines.GUI_TEXTURED, id, x1, y1, (backgroundOffset / 250f), (backgroundOffset / 500f), x2 - x1, y2 - y1, 16, 16);
                        }
                        context.fill(x1, y1, x2, y2, 0x22000000);
                        if (!isPreviewTypeTiling) {
                            int scale = 30;
                            context.fill(x1, y1, x2, y2, 0xBB000000);

                            if (item.getBlock() instanceof GeoPropBlock || item.getBlock() instanceof MimicFrames || item.getBlock() instanceof DiagonalMimicFrame) {
                                float itemsize = 2.5f;
                                context.pose().pushMatrix();
                                context.pose().scale(itemsize);
                                context.pose().translate((this.leftPos + 114)/itemsize, (this.topPos + 40)/itemsize);
                                context.renderItem(previewStack, 0, 0);
                                context.pose().popMatrix();
                            } else {

                                Quaternionf quaternionf = new Quaternionf().rotateX(-0.25f).rotateY((float) Math.toRadians((backgroundOffset / 20) % 360));

                                FallingBlockEntity entity = FallingBlockEntity.fall(Minecraft.getInstance().level, new BlockPos(0, 250, 0), previewState);

                                GoopyScreen.drawEntity(context, x1, y1, x2, y2, scale, 0, quaternionf, entity, 0);

                            }
                        }
                    }
                    else{
                        float scale = 3f;
                        context.pose().pushMatrix();
                        context.pose().scale(scale);
                        context.pose().translate((this.leftPos + 110)/scale, (this.topPos + 36)/scale);
                        context.renderItem(previewStack, 0, 0);
                        context.pose().popMatrix();
                    }

                    hovered_slot = -1;
                    for (int x = 0; x < 9; x++){

                        int slotX = this.leftPos + 18 + (x * 18);

                        if(entry.items().isEmpty()) continue;
                        int index = (x + selectedItemCategoryOffset)%entry.items().size();
                        if (x + selectedItemCategoryOffset > entry.items().size() - 1) continue;

                        ItemStack stack = (ItemStack) entry.items().toArray()[index];
                        context.renderItem(stack, slotX, slotY);

                        if (GoopyScreen.isOnButton(mouseX, mouseY, slotX, slotY, 16, 16)) {
                            hovered_slot = index;
                            lastPreviewStackIndex = index;
                            context.fill(slotX, slotY, slotX + 16, slotY + 16, 0x80FFFFFF);
                            context.setTooltipForNextFrame(this.font, stack.getHoverName(), mouseX, mouseY);
                        }
                    }
                }

                if((activeCategory.itemCategory.ITEM_ENTRIES.size()/3f) - 4 > 0) {

                    int minScrollY = this.topPos + 34;
                    int maxScrollY = this.topPos + 83;

                    float scrollIndex = itemCategoryScroll / Math.max((activeCategory.itemCategory.ITEM_ENTRIES.size() / 3f) - 3, 1f);

                    int scrollY = Mth.floor(Mth.lerpInt(scrollIndex, minScrollY, maxScrollY));

                    context.blit(RenderPipelines.GUI_TEXTURED, back_texture, this.leftPos + 72, scrollY, this.imageWidth - 14, 59, 5, 3, 256, 256);

                }

                for (int y = 0; y < 3; y++) {
                    for (int x = 0; x < 3; x++) {
                        int slotX = this.leftPos + 18 + (x * 18);
                        int slotY = this.topPos + 34 + (y * 18);

                        int offsetY = y + itemCategoryScroll;

                        int index = (offsetY * 3) + x ;

                        if(index < 0 || index >= activeCategory.itemCategory.ITEM_ENTRIES.size()) continue;
                        ItemCategory.Entry entry = (ItemCategory.Entry) activeCategory.itemCategory.ITEM_ENTRIES.toArray()[index];
                        ItemStack stack = entry.icon();
                        context.renderItem(stack, slotX, slotY);
                        if (GoopyScreen.isOnButton(mouseX, mouseY, slotX, slotY, 16, 16) || selectedItemCategory == index) {
                            context.fill(slotX, slotY, slotX + 16, slotY + 16, 0x80FFFFFF);
                            if(GoopyScreen.isOnButton(mouseX, mouseY, slotX, slotY, 16, 16)) {
                                context.setTooltipForNextFrame(this.font, stack.getHoverName(), mouseX, mouseY);
                            }
                        }
                    }
                }
            }
        }
        int cubeTypeV = GoopyScreen.isOnButton(mouseX, mouseY, this.leftPos + 90, this.topPos + 34, 9, 9) ? 9 : 0;
        int cubeTypeU = isPreviewTypeTiling ? 0 : 9;
        context.blit(RenderPipelines.GUI_TEXTURED, back_texture, this.leftPos + 90, this.topPos + 34, this.imageWidth -5 + cubeTypeU, cubeTypeV, 9, 9, 256, 256);

        Component text = Component.translatable("fnafur.creative.subtab." + activeCategory.name);
        context.drawString(font, text, this.leftPos + 20, this.topPos + 24, text_color, false);

        Component text2 = Component.translatable("fnafur.creative.selection");
        int moveWidth = font.width(text2)/2;
        context.drawString(font, text2, this.leftPos + 135 - moveWidth, this.topPos + 24, text_color, false);

    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        if (isFnafTab) {
            if(garageDoorIndex != 0){
                return false;
            }
            switch (SubTab) {
                case ANIMATRONICS -> clickAnimatronics(click.x(), click.y(), click.button());
                case BLOCKS -> clickBlocks(click.x(), click.y(), click.button());
                case DECORATION -> clickProps(click.x(), click.y(), click.button());
                case TECHNICAL -> clickTech(click.x(), click.y(), click.button());
                case ITEM_WITH_ALTS_SELECTION, BEAR5 -> clickItemWithAlts(click.x(), click.y(), click.button());
                default -> clickDefault(click.x(), click.y(), click.button());
            }
        }
        else{
            selectedItemCategory = -1;
            selectedItemCategoryOffset = 0;
            lastPreviewStackIndex = 0;
            garageDoorIndex = 0;
            SubTab = FnafSubTab.DEFAULT;
            GoalSubTab = FnafSubTab.DEFAULT;
        }

        return super.mouseClicked(click, doubled);
    }

    public void clickDefault(double mouseX, double mouseY, int button) {
        int x1 = this.leftPos + 10;
        int y1 = this.topPos + 23;

        int x2 = this.leftPos + 101;
        int y2 = this.topPos + 78;

        if (GoopyScreen.isOnButton(mouseX, mouseY, x1, y1, 84, 48)) {
            GoalSubTab = FnafSubTab.ANIMATRONICS;
            Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
        }
        if (GoopyScreen.isOnButton(mouseX, mouseY, x2, y1, 84, 48)) {
            GoalSubTab = FnafSubTab.BLOCKS;
            Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
        }
        if (GoopyScreen.isOnButton(mouseX, mouseY, x1, y2, 84, 48)) {
            GoalSubTab = FnafSubTab.DECORATION;
            Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
        }
        if (GoopyScreen.isOnButton(mouseX, mouseY, x2, y2, 84, 48)) {
            GoalSubTab = FnafSubTab.TECHNICAL;
            Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
        }


    }

    public void clickAnimatronics(double mouseX, double mouseY, int button) {

    }

    public void clickBlocks(double mouseX, double mouseY, int button) {
        if(CategoryPicker.HOVERED != null){
            CategoryPicker.HOVERED.onClick();
            activeCategory = CategoryPicker.HOVERED;
            Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
        }
    }

    public void clickItemWithAlts(double mouseX, double mouseY, int button) {
        boolean shiftKey = InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT);
        if(GoopyScreen.isOnButton(mouseX, mouseY, this.leftPos + 9, this.topPos + 22, 9, 9)){
            selectedItemCategory = -1;
            itemCategoryScroll = 0;
            selectedItemCategoryOffset = 0;
            lastPreviewStackIndex = 0;
            GoalSubTab = PreviousSubTab;
            Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
            return;
        }
        if(GoopyScreen.isOnButton(mouseX, mouseY, this.leftPos + 90, this.topPos + 34, 9, 9)){
            isPreviewTypeTiling = !isPreviewTypeTiling;
            Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
            return;
        }
        if(activeCategory != null) {
            if(selectedItemCategory != -1) {
                if(selectedItemCategoryOffset != 0 && GoopyScreen.isOnButton(mouseX, mouseY, this.leftPos + 9, this.topPos + 90, 7, 16)) {
                    selectedItemCategoryOffset--;
                    Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
                    return;
                }
                ItemCategory.Entry entry = (ItemCategory.Entry) activeCategory.itemCategory.ITEM_ENTRIES.toArray()[selectedItemCategory];

                if(selectedItemCategoryOffset < entry.items().size() - 9 && GoopyScreen.isOnButton(mouseX, mouseY, this.leftPos + 180, this.topPos + 90, 7, 16)){
                    selectedItemCategoryOffset++;
                    Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
                    return;
                }

            }

            for(int i = 0; i < 9; i++) {
                if(GoopyScreen.isOnButton(mouseX, mouseY, this.leftPos + 18 + (i * 18), this.topPos + 110, 16, 16)) {
                    int slotID = 45 + i;
                    if(shiftKey){

                        this.menu.setItem(slotID, this.menu.incrementStateId(), ItemStack.EMPTY);
                    }
                    else{
                        Slot slot = this.menu.getSlot(slotID);
                        if (slot == null) {
                            return;
                        }
                        if(slot.mayPlace(this.menu.getCarried())){
                            if(button == 1 && this.menu.getCarried().isEmpty()){
                                ItemStack slotStack = slot.getItem().copy();
                                int half = (int) Math.ceil(slotStack.getCount()/2f);
                                slotStack.setCount(slotStack.getCount() - half);
                                this.menu.setItem(slotID, this.menu.incrementStateId(), slotStack);
                                ItemStack cursorStack = slot.getItem().copy();
                                cursorStack.setCount(half);
                                this.menu.setCarried(cursorStack);
                                return;
                            }
                            if(button == 0 || (!slot.getItem().isEmpty() && slot.getItem().getItem() != this.menu.getCarried().getItem())){
                                if(slot.getItem().getItem() == this.menu.getCarried().getItem()){
                                    ItemStack cursorStack = this.menu.getCarried().copy();
                                    ItemStack slotStack = slot.getItem().copy();
                                    int count = Math.clamp(slotStack.getCount() + cursorStack.getCount(), 0, Math.min(slotStack.getMaxStackSize(), this.minecraft.player.getInventory().getMaxStackSize()));
                                    slotStack.setCount(count);
                                    this.menu.setItem(slotID, this.menu.incrementStateId(), slotStack);
                                    int remaining = cursorStack.getCount() - count;
                                    cursorStack.setCount(remaining);
                                    this.menu.setCarried(cursorStack);
                                    return;
                                }
                                ItemStack cursorStack = slot.getItem().copy();
                                this.menu.setItem(slotID, this.menu.incrementStateId(), this.menu.getCarried().copy());
                                this.menu.setCarried(cursorStack);
                            }
                            else{
                                ItemStack cursorStack = this.menu.getCarried().copy();
                                cursorStack.shrink(1);
                                ItemStack slotStack = cursorStack.copy();
                                slotStack.setCount(slot.getItem().getCount() + 1);
                                this.menu.setItem(slotID, this.menu.incrementStateId(), slotStack);


                                this.menu.setCarried(cursorStack);
                            }
                        }
                        //this.client.interactionManager.clickSlot(this.handler.syncId, slotID, button, SlotActionType.PICKUP, this.client.player);
                    }
                    this.minecraft.player.inventoryMenu.broadcastChanges();
                    return;
                }
            }
            if(activeCategory.itemCategory != null) {

                for (int y = 0; y < 3; y++) {
                    for (int x = 0; x < 3; x++) {
                        int slotX = this.leftPos + 18 + (x * 18);
                        int slotY = this.topPos + 34 + (y * 18);

                        int offsetY = y + itemCategoryScroll;
                        int index = (offsetY * 3) + x;
                        if(index >= activeCategory.itemCategory.ITEM_ENTRIES.size()) continue;
                        if(GoopyScreen.isOnButton(mouseX, mouseY, slotX, slotY, 16, 16)) {
                            if(selectedItemCategory == index) {
                                selectedItemCategory = -1;
                                Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
                                return;
                            }
                            this.selectedItemCategory = index;
                            selectedItemCategoryOffset = 0;
                            lastPreviewStackIndex = 0;
                            Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
                            return;
                        }
                    }
                }
                if(selectedItemCategory > -1) {
                    ItemCategory.Entry entry = (ItemCategory.Entry) activeCategory.itemCategory.ITEM_ENTRIES.toArray()[selectedItemCategory];
                    for (int x = 0; x < 9; x++) {
                        int slotX = this.leftPos + 18 + (x * 18);

                        if (entry.items().isEmpty()) continue;
                        int index = (x + selectedItemCategoryOffset) % entry.items().size();
                        if (x + selectedItemCategoryOffset > entry.items().size() - 1) continue;
                        ItemStack stack = (ItemStack) entry.items().toArray()[index];
                        if (GoopyScreen.isOnButton(mouseX, mouseY, slotX, this.topPos + 90, 16, 16)) {

                            boolean isEmpty = this.menu.getCarried().isEmpty();
                            if (!isEmpty) {
                                if (ItemStack.isSameItemSameComponents(stack, this.menu.getCarried())) {
                                    ItemStack cursorStack = this.menu.getCarried();
                                    int totalCount = cursorStack.getCount() + (button == 0 ? stack.getCount() : -stack.getCount());
                                    int maxCount = Math.min(stack.getMaxStackSize(), this.minecraft.player.getInventory().getMaxStackSize());
                                    if (totalCount <= maxCount) {
                                        ItemStack newStack = stack.copy();
                                        newStack.setCount(totalCount);
                                        if (shiftKey) {
                                            newStack.setCount(newStack.getMaxStackSize());
                                        }
                                        ((ItemPickerMenu) this.menu).setCarried(newStack);
                                        this.minecraft.player.inventoryMenu.broadcastChanges();
                                        return;
                                    }
                                } else {
                                    stack = ItemStack.EMPTY;
                                    ((ItemPickerMenu) this.menu).setCarried(stack);
                                    this.minecraft.player.inventoryMenu.broadcastChanges();
                                }
                            }
                            ((ItemPickerMenu) this.menu).setCarried(ItemStack.EMPTY);

                            ItemStack newStack = stack.copy();
                            if (shiftKey) {
                                newStack.setCount(newStack.getMaxStackSize());
                            }
                            ((ItemPickerMenu) this.menu).setCarried(newStack);
                            this.minecraft.player.inventoryMenu.broadcastChanges();
                            return;
                        }
                    }
                }
            }
            this.minecraft.gameMode.handleCreativeModeItemDrop(((ItemPickerMenu)this.menu).getCarried());
            ((ItemPickerMenu)this.menu).setCarried(ItemStack.EMPTY);
            this.minecraft.player.inventoryMenu.broadcastChanges();
            return;
        }
    }

    public void clickProps(double mouseX, double mouseY, int button) {

    }

    public void clickTech(double mouseX, double mouseY, int button) {

    }



    protected void drawFnafBackground(GuiGraphics context, float delta, int mouseX, int mouseY) {
        for (CreativeModeTab itemGroup : CreativeModeTabs.tabs()) {
            if (itemGroup != getSelectedItemGroup() || Objects.equals(itemGroup.getDisplayName().getString(), FnafUniverseRebuilt.MOD_ID)) {
                this.renderTabButton(context, mouseX, mouseY, itemGroup);
            }
        }

        switch (SubTab) {
            case ANIMATRONICS -> renderAnimatronicsBackground(context, mouseX, mouseY, delta);
            case BLOCKS -> renderBlocksBackground(context, mouseX, mouseY, delta);
            case DECORATION -> renderPropsBackground(context, mouseX, mouseY, delta);
            case TECHNICAL -> renderTechBackground(context, mouseX, mouseY, delta);
            case ITEM_WITH_ALTS_SELECTION -> renderItemSelectionBackground(context, ITEM_SELECTION_WITH_ALTS_BACK, 0xFF74705F, mouseX, mouseY, delta);
            case BEAR5 -> renderItemSelectionBackground(context, ITEM_SELECTION_WITH_ALTS_BACK_BLUE, 0xFF000055, mouseX, mouseY, delta);
            default -> renderDefaultBackground(context, mouseX, mouseY, delta);
        }

        context.blit(RenderPipelines.GUI_TEXTURED, getSelectedItemGroup().getBackgroundTexture(), this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

        context.blit(RenderPipelines.GUI_TEXTURED, getSelectedItemGroup().getBackgroundTexture(), this.leftPos + 8, this.topPos + 6, 0.0F, this.imageHeight + (SubTab.index * 5), 28, 5, 256, 256);

        if (!isFnafTab) {
            this.renderTabButton(context, mouseX, mouseY, getSelectedItemGroup());
        }
    }

    @Override
    protected void renderBg(GuiGraphics context, float deltaTicks, int mouseX, int mouseY) {
        super.renderBg(context, deltaTicks, mouseX, mouseY);
        if (isInventoryOpen()) {
            context.blit(RenderPipelines.GUI_TEXTURED, SLOTS, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        }
    }

    protected void drawFloor(RenderPipeline pipeline, GuiGraphics context, float delta, int mouseX, int mouseY, Identifier texture, int x, int maxX, int y, int maxY) {
        AbstractTexture gpuTextureView = this.minecraft.getTextureManager().getTexture(texture);
        drawFloor(pipeline, context, delta, mouseX, mouseY, gpuTextureView, x, maxX, y, maxY);
    }
    private void renderRoom(RenderPipeline pipeline, GuiGraphics context, float delta, int mouseX, int mouseY, GpuTextureView texture, int x, int maxX, int y, int maxY) {

    }
    private void drawFloor(RenderPipeline pipeline, GuiGraphics context, float delta, int mouseX, int mouseY, AbstractTexture texture, int x, int maxX, int y, int maxY) {

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

            float bottomU = Mth.lerp(u, 0 + multiplier, 1 - multiplier);
            float bottomU2 = Mth.lerp(u2, 0 + multiplier, 1 - multiplier);


            context.guiRenderState
                    .submitGuiElement(
                            new FullTexturedQuadGuiElementRenderState(
                                    pipeline,  TextureSetup.singleTexture(texture.getTextureView(), texture.getSampler()), new Matrix3x2f(context.pose()), (int)thisX, y1, (int)thisX, y2, (int)nextX, y2, (int)nextX, y1, u + floorX, bottomU + floorX, bottomU2 + floorX, u2 + floorX, v1, v2, v2, v1, 0xFFFFFFFF, context.scissorStack.peek()
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
    public void onClose() {

        BlockInit.randomize = true;
        //FreddyEntity.remove(Entity.RemovalReason.DISCARDED);

        super.onClose();
    }

    @Override
    protected void renderTabButton(GuiGraphics context, int mouseX, int mouseY, CreativeModeTab group) {
        final FabricItemGroupImpl fabricItemGroup = (FabricItemGroupImpl) group;

        if (!Objects.equals(group.getDisplayName().getString(), FnafUniverseRebuilt.MOD_ID) || fabricItemGroup.fabric_getPage() != getCurrentPage()) {
            super.renderTabButton(context, mouseX, mouseY, group);
            return;
        }

        boolean bl = group == getSelectedItemGroup();
        boolean bl2 = group.row() == CreativeModeTab.Row.TOP;
        int i = group.column();
        int j = this.leftPos + this.getTabX(group);
        int k = this.topPos - (bl2 ? 28 : -(this.imageHeight - 4));

        Identifier texture;
        if (bl2) {
            texture = bl ? Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/tab_top_selected.png") : Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/tab_top_unselected.png");
        } else {
            texture = bl ? Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/tab_bottom_selected.png") : Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/tab_bottom_unselected.png");
        }

        //GoopyScreen.drawRecolorableTexture(context, texture, j, k, 32, 26, 0, 0, 32, 26, 0xFFFFFFFF);
        context.blit(RenderPipelines.GUI_TEXTURED, texture, j, k, 0, 0, 26, 32, 26, 32);
    }

    private int getTabX(CreativeModeTab group) {
        int i = group.column();
        int j = 27;
        int k = 27 * i;
        if (group.isAlignedRight()) {
            k = this.imageWidth - 27 * (7 - i) + 1;
        }

        return k;
    }

    public enum FnafSubTab {
        DEFAULT(0),
        ANIMATRONICS(1),
        BLOCKS(2),
        DECORATION(3),
        TECHNICAL(4),
        ITEM_WITH_ALTS_SELECTION(5),
        BEAR5(6),
        ;
        public final int index;

        FnafSubTab(int index) {
            this.index = index;
        }
    }

    public List<Slot> createSlots(CreativeModeTab selectedTab, CreativeModeTab itemGroup, List<Slot> slots, Slot deleteItemSlot) {
        this.quickCraftSlots.clear();
        this.menu.items.clear();
        this.clearDraggingState();
        if (selectedTab.getType() == CreativeModeTab.Type.HOTBAR) {
            HotbarManager hotbarStorage = this.minecraft.getHotbarManager();

            for (int i = 0; i < 9; i++) {
                Hotbar hotbarStorageEntry = hotbarStorage.get(i);
                if (hotbarStorageEntry.isEmpty()) {
                    for (int j = 0; j < 9; j++) {
                        if (j == i) {
                            ItemStack itemStack = new ItemStack(Items.PAPER);
                            itemStack.set(DataComponents.CREATIVE_SLOT_LOCK, Unit.INSTANCE);
                            Component text = this.minecraft.options.keyHotbarSlots[i].getTranslatedKeyMessage();
                            Component text2 = this.minecraft.options.keySaveHotbarActivator.getTranslatedKeyMessage();
                            itemStack.set(DataComponents.ITEM_NAME, Component.translatable("inventory.hotbarInfo", text2, text));
                            this.menu.items.add(itemStack);
                        } else {
                            this.menu.items.add(ItemStack.EMPTY);
                        }
                    }
                } else {
                    this.menu.items.addAll(hotbarStorageEntry.load(this.minecraft.level.registryAccess()));
                }
            }
        } else if (selectedTab.getType() == CreativeModeTab.Type.CATEGORY) {
            this.menu.items.addAll(selectedTab.getDisplayItems());
        }

        if (selectedTab.getType() == CreativeModeTab.Type.INVENTORY) {
            AbstractContainerMenu screenHandler = this.minecraft.player.inventoryMenu;
            if (slots == null) {
                slots = ImmutableList.copyOf(this.menu.slots);
            }

            this.menu.slots.clear();

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
                } else if (screenHandler.slots.get(ix).getContainerSlot() == FnafInventoryScreen.SLOTS_OFFSET) {
                    n = 127;
                    jx = 10;
                } else if (screenHandler.slots.get(ix).getContainerSlot() == FnafInventoryScreen.SLOTS_OFFSET + 1) {
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
                this.menu.slots.add(slot);
            }
            this.menu.slots.add(deleteItemSlot);
        } else if (itemGroup.getType() == CreativeModeTab.Type.INVENTORY) {
            this.menu.slots.clear();
            this.menu.slots.addAll(slots);
            slots = null;
        }
        this.menu.scrollTo(0.0F);

        return slots;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if(isFnafTab){
            if(SubTab == FnafSubTab.ITEM_WITH_ALTS_SELECTION){
                itemCategoryScroll = verticalAmount > 0 ? itemCategoryScroll - 1 : itemCategoryScroll + 1;
            }
            else{
                itemCategoryScroll = 0;
            }

            itemCategoryScroll = Math.clamp(itemCategoryScroll, 0, Math.max((activeCategory.itemCategory.ITEM_ENTRIES.size()/3) - 3, 0));
        }

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void onMouseClickAction(@Nullable Slot slot, ClickType actionType) {
        super.onMouseClickAction(slot, actionType);
    }

    @Override
    protected void slotClicked(@Nullable Slot slot, int slotId, int button, ClickType actionType) {
        isFnafTab = Objects.equals(getSelectedItemGroup().getDisplayName().getString(), FnafUniverseRebuilt.MOD_ID);
        if (isFnafTab) {
            return;
        }
        super.slotClicked(slot, slotId, button, actionType);

        CompoundTag data = new CompoundTag();
        ItemStack stack1 = Minecraft.getInstance().player.getInventory().getItem(FnafInventoryScreen.SLOTS_OFFSET);
        ItemStack stack2 = Minecraft.getInstance().player.getInventory().getItem(FnafInventoryScreen.SLOTS_OFFSET + 1);
        data.putBoolean("hasStack1", !stack1.isEmpty());
        data.putBoolean("hasStack2", !stack2.isEmpty());
        if(data.getBooleanOr("hasStack1", false)){
            data.store("stack1", ItemStack.CODEC, stack1);
        }
        if(data.getBooleanOr("hasStack2", false)){
            data.store("stack2", ItemStack.CODEC, stack2);
        }
        ClientPlayNetworking.send(new UpdateCreativeExtraSlotsC2SPayload(data));
    }


    @Environment(EnvType.CLIENT)
    public static class FnafCreativeSlot extends SlotWrapper {
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
        public ItemCategory itemCategory = null;
        public static List<CategoryPicker> ALL = new ArrayList<>();
        public static CategoryPicker HOVERED = null;

        public CategoryPicker(String name, int posX, int posY, int width, int height, int u, int v, int u_active, int v_active, Identifier texture, ItemCategory category, ButtonAction action){
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
            this.itemCategory = category;
            ALL.add(this);
        }
        public CategoryPicker(String name, int posX, int posY, int width, int height, int u, int v, int u_active, int v_active, Identifier texture, ItemCategory category){
            this(name, posX, posY, width, height, u, v, u_active, v_active, texture, category, CategoryPicker::SUBTAB_OPEN);
        }

        public void draw(GuiGraphics context, int offsetX, int offsetY, int mouseX, int mouseY){
            isHovered = GoopyScreen.isOnButton(mouseX, mouseY, posX + offsetX, posY + offsetY, width, height);
            context.blit(RenderPipelines.GUI_TEXTURED, texture, offsetX + posX, offsetY + posY, isHovered ? u_active : u, isHovered ? v_active : v, width, height, 256, 256);

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

        private static void SUBTAB_OPEN(String s) {
            System.out.println(s);
            PreviousSubTab = SubTab;
            GoalSubTab = FnafSubTab.ITEM_WITH_ALTS_SELECTION;
        }
    }

    @FunctionalInterface
    public interface ButtonAction {
        void execute(String name);
    }
}
