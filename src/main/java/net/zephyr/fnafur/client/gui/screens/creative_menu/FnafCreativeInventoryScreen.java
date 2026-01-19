package net.zephyr.fnafur.client.gui.screens.creative_menu;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.impl.itemgroup.FabricItemGroupImpl;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.HotbarStorage;
import net.minecraft.client.option.HotbarStorageEntry;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.texture.*;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.MimicFrames;
import net.zephyr.fnafur.blocks.dynamic.illusion_block.diagonal.DiagonalMimicFrame;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.client.gui.screens.other.FullTexturedQuadGuiElementRenderState;
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
public class FnafCreativeInventoryScreen extends CreativeInventoryScreen {
    public static final int SLOTS_X = 127;
    public static final int SLOT_AR_Y = 10;
    public static final int SLOT_IL_Y = 29;
    public static boolean isFnafTab = false;
    public float floorX = 0;
    public float garageDoorIndex = 0;
    public static FnafSubTab SubTab = FnafSubTab.DEFAULT;
    public static FnafSubTab GoalSubTab = FnafSubTab.DEFAULT;
    public static FnafSubTab PreviousSubTab = FnafSubTab.DEFAULT;
    public static final Identifier SLOTS = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/creative_inventory_mask_slot.png");
    public static final Identifier BLOCKS_BACK = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/blocks_back.png");
    public static final Identifier ITEM_SELECTION_WITH_ALTS_BACK = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/slots.png");
    public static final Identifier ITEM_SELECTION_WITH_ALTS_BACK_BLUE = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/creative_inventory/slots_blue.png");
    public CategoryPicker activeCategory = null;
    private int selectedItemCategory = -1;
    private int selectedItemCategoryOffset = 0;
    private int lastPreviewStackIndex = 0;
    private float backgroundOffset = 0;
    private boolean isPreviewTypeTiling = false;

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

    public FnafCreativeInventoryScreen(ClientPlayerEntity player, FeatureSet enabledFeatures, boolean operatorTabEnabled) {
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
            //this.renderBackground(context, mouseX, mouseY, delta);
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

            renderCursorStack(context, mouseX, mouseY);
            this.drawMouseoverTooltip(context, mouseX, mouseY);
            return;
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void drawSlot(DrawContext context, Slot slot, int mouseX, int mouseY) {
        super.drawSlot(context, slot, mouseX, mouseY);
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

        if(!input.isEscape() && isFnafTab && SubTab == FnafSubTab.DEFAULT){

            String in = InputUtil.fromKeyCode(input).getLocalizedText().getString();
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
                    MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, Math.min(1, 0.5f + (currentCode.length() / 10f)));
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
    public void renderItemSelectionBackground(DrawContext context, Identifier back_texture, int text_color, int mouseX, int mouseY, float delta) {
        backgroundOffset += delta*20f;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, back_texture, this.x + 7, this.y + 20, 0, 0, this.backgroundWidth - 8, this.backgroundHeight - 21, 256, 256);
        int leaveV = GoopyScreen.isOnButton(mouseX, mouseY, this.x + 9, this.y + 22, 9, 9) ? 9 : 0;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, back_texture, this.x + 9, this.y + 22, this.backgroundWidth-14, leaveV, 9, 9, 256, 256);

        for(int i = 0; i < 9; i++) {
            ItemStack stack = this.handler.slots.get(45 + i).getStack();
            Slot oldSlot = this.handler.slots.get(45 + i);
            //context.drawItem(stack, this.x + 18 + (i * 18), this.y + 110);
            Slot slot = new Slot(oldSlot.inventory, oldSlot.getIndex(), this.x + 18 + (i * 18), this.y + 110);
            slot.setStack(oldSlot.getStack());
            drawSlot(context, slot, mouseX, mouseY);
            if(GoopyScreen.isOnButton(mouseX, mouseY, this.x + 18 + (i * 18), this.y + 110, 16, 16)) {
                context.fill(this.x + 18 + (i * 18), this.y + 110, this.x + 34 + (i * 18), this.y + 126, 0x80FFFFFF);
                if(!stack.isEmpty()) {
                    context.drawTooltip(this.textRenderer, stack.getName(), mouseX, mouseY);
                }
            }
        }

        if(activeCategory != null) {
            context.drawTexture(RenderPipelines.GUI_TEXTURED, back_texture, this.x + 81, this.y + 36, this.backgroundWidth, 27, 7, 16, 256, 256);
            context.drawTexture(RenderPipelines.GUI_TEXTURED, back_texture, this.x + 180, this.y + 36, this.backgroundWidth, 43, 7, 16, 256, 256);
            if(activeCategory.itemCategory != null) {

                if(selectedItemCategory != -1) {
                    int leftArrowU = GoopyScreen.isOnButton(mouseX, mouseY, this.x + 81, this.y + 36, 7, 16) ? this.backgroundWidth - 7 : this.backgroundWidth - 14;
                    context.drawTexture(RenderPipelines.GUI_TEXTURED, back_texture, this.x + 81, this.y + 36, leftArrowU, 27, 7, 16, 256, 256);
                    int rightArrowU = GoopyScreen.isOnButton(mouseX, mouseY, this.x + 180, this.y + 36, 7, 16) ? this.backgroundWidth - 7 : this.backgroundWidth - 14;
                    context.drawTexture(RenderPipelines.GUI_TEXTURED, back_texture, this.x + 180, this.y + 36, rightArrowU, 43, 7, 16, 256, 256);

                    context.drawTexture(RenderPipelines.GUI_TEXTURED, back_texture, this.x + 89, this.y + 34, 0, 108, 100, 19, 256, 256);

                    ItemCategory.Entry entry = (ItemCategory.Entry) activeCategory.itemCategory.ITEM_ENTRIES.toArray()[selectedItemCategory];

                    int slotY = this.y + 36;

                    int x1 = this.x + 90;
                    int y1 = this.y + 54;
                    int x2 = this.x + 178;
                    int y2 = this.y + 106;
                    ItemStack previewStack = (ItemStack) entry.items().toArray()[lastPreviewStackIndex];
                    if (previewStack.getItem() instanceof BlockItem item) {
                        BlockState previewState = item.getBlock().getDefaultState();
                        if (item.getBlock() instanceof GeoPropBlock) {

                        } else {

                            BlockStateModel model = client.getBakedModelManager().getBlockModels().getModel(item.getBlock().getDefaultState());

                            Sprite sprite;

                            if(item.getBlock() instanceof MimicFrames frame){
                                int frameSize = frame.getMatrixSize() * frame.getMatrixSize();
                                sprite = MinecraftClient.getInstance().getBlockRenderManager().spriteHolder.getSprite(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/mimic_frame_" + frameSize)));
                            }
                            else if(item.getBlock() instanceof DiagonalMimicFrame){
                                sprite = MinecraftClient.getInstance().getBlockRenderManager().spriteHolder.getSprite(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/mimic_frame_1")));
                            }
                            else{
                                sprite =  model.getParts(Random.create()).getFirst().getQuads(Direction.NORTH).getFirst().sprite();

                            }


                            TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
                            Identifier id = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/" + sprite.getContents().getId().getPath() + ".png");
                            context.drawTexture(RenderPipelines.GUI_TEXTURED, id, x1, y1, (backgroundOffset / 250f), (backgroundOffset / 500f), x2 - x1, y2 - y1, 16, 16);
                        }
                        context.fill(x1, y1, x2, y2, 0x22000000);
                        if (!isPreviewTypeTiling) {
                            int scale = 30;
                            context.fill(x1, y1, x2, y2, 0xBB000000);

                            if (item.getBlock() instanceof GeoPropBlock || item.getBlock() instanceof MimicFrames || item.getBlock() instanceof DiagonalMimicFrame) {
                                float itemsize = 2.5f;
                                context.getMatrices().pushMatrix();
                                context.getMatrices().scale(itemsize);
                                context.getMatrices().translate((this.x + 114)/itemsize, (this.y + 60)/itemsize);
                                context.drawItem(previewStack, 0, 0);
                                context.getMatrices().popMatrix();
                            } else {

                                Quaternionf quaternionf = new Quaternionf().rotateX(-0.25f).rotateY((float) Math.toRadians((backgroundOffset / 20) % 360));

                                FallingBlockEntity entity = FallingBlockEntity.spawnFromBlock(MinecraftClient.getInstance().world, new BlockPos(0, 250, 0), previewState);

                                GoopyScreen.drawEntity(context, x1, y1, x2, y2, scale, 0, quaternionf, entity, 0);

                            }
                        }
                    }
                    else{
                        float scale = 3f;
                        context.getMatrices().pushMatrix();
                        context.getMatrices().scale(scale);
                        context.getMatrices().translate((this.x + 110)/scale, (this.y + 54)/scale);
                        context.drawItem(previewStack, 0, 0);
                        context.getMatrices().popMatrix();
                    }

                    for (int x = 0; x < 5; x++){
                        int slotX = this.x + 90 + (x * 18);

                        if(entry.items().isEmpty()) continue;
                        int index = (x + selectedItemCategoryOffset)%entry.items().size();
                        ItemStack stack = (ItemStack) entry.items().toArray()[index];
                        context.drawItem(stack, slotX, slotY);

                        if (GoopyScreen.isOnButton(mouseX, mouseY, slotX, slotY, 16, 16)) {
                            lastPreviewStackIndex = index;
                            context.fill(slotX, slotY, slotX + 16, slotY + 16, 0x80FFFFFF);
                            context.drawTooltip(this.textRenderer, stack.getName(), mouseX, mouseY);
                        }
                    }
                }

                for (int y = 0; y < 4; y++) {
                    for (int x = 0; x < 3; x++) {
                        int slotX = this.x + 18 + (x * 18);
                        int slotY = this.y + 36 + (y * 18);

                        int index = (y * 3) + x;

                        if(index >= activeCategory.itemCategory.ITEM_ENTRIES.size()) continue;
                        ItemCategory.Entry entry = (ItemCategory.Entry) activeCategory.itemCategory.ITEM_ENTRIES.toArray()[index];
                        ItemStack stack = entry.icon();
                        context.drawItem(stack, slotX, slotY);
                        if (GoopyScreen.isOnButton(mouseX, mouseY, slotX, slotY, 16, 16) || selectedItemCategory == index) {
                            context.fill(slotX, slotY, slotX + 16, slotY + 16, 0x80FFFFFF);
                            if(GoopyScreen.isOnButton(mouseX, mouseY, slotX, slotY, 16, 16)) {
                                context.drawTooltip(this.textRenderer, stack.getName(), mouseX, mouseY);
                            }
                        }
                    }
                }
            }
        }
        int cubeTypeV = GoopyScreen.isOnButton(mouseX, mouseY, this.x + 90, this.y + 54, 9, 9) ? 9 : 0;
        int cubeTypeU = isPreviewTypeTiling ? 0 : 9;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, back_texture, this.x + 90, this.y + 54, this.backgroundWidth-5 + cubeTypeU, cubeTypeV, 9, 9, 256, 256);

        Text text = Text.translatable("fnafur.creative.subtab." + activeCategory.name);
        context.drawText(textRenderer, text, this.x + 20, this.y + 24, text_color, false);

        Text text2 = Text.translatable("fnafur.creative.selection");
        int moveWidth = textRenderer.getWidth(text2)/2;
        context.drawText(textRenderer, text2, this.x + 135 - moveWidth, this.y + 24, text_color, false);

    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
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
        int x1 = this.x + 10;
        int y1 = this.y + 23;

        int x2 = this.x + 101;
        int y2 = this.y + 78;

        if (GoopyScreen.isOnButton(mouseX, mouseY, x1, y1, 84, 48)) {
            GoalSubTab = FnafSubTab.ANIMATRONICS;
            MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
        }
        if (GoopyScreen.isOnButton(mouseX, mouseY, x2, y1, 84, 48)) {
            GoalSubTab = FnafSubTab.BLOCKS;
            MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
        }
        if (GoopyScreen.isOnButton(mouseX, mouseY, x1, y2, 84, 48)) {
            GoalSubTab = FnafSubTab.DECORATION;
            MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
        }
        if (GoopyScreen.isOnButton(mouseX, mouseY, x2, y2, 84, 48)) {
            GoalSubTab = FnafSubTab.TECHNICAL;
            MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
        }


    }

    public void clickAnimatronics(double mouseX, double mouseY, int button) {

    }

    public void clickBlocks(double mouseX, double mouseY, int button) {
        if(CategoryPicker.HOVERED != null){
            CategoryPicker.HOVERED.onClick();
            activeCategory = CategoryPicker.HOVERED;
            MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
        }
    }

    public void clickItemWithAlts(double mouseX, double mouseY, int button) {
        boolean shiftKey = InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT);
        if(GoopyScreen.isOnButton(mouseX, mouseY, this.x + 9, this.y + 22, 9, 9)){
            selectedItemCategory = -1;
            selectedItemCategoryOffset = 0;
            GoalSubTab = PreviousSubTab;
            MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
            return;
        }
        if(GoopyScreen.isOnButton(mouseX, mouseY, this.x + 90, this.y + 54, 9, 9)){
            isPreviewTypeTiling = !isPreviewTypeTiling;
            MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
            return;
        }
        if(activeCategory != null) {
            if(selectedItemCategory != -1) {
                if(GoopyScreen.isOnButton(mouseX, mouseY, this.x + 81, this.y + 36, 7, 16)) {
                    selectedItemCategoryOffset--;
                    if(selectedItemCategoryOffset < 0){
                        selectedItemCategoryOffset += activeCategory.itemCategory.ITEM_ENTRIES.size();
                    }
                    MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
                    return;
                }
                if(GoopyScreen.isOnButton(mouseX, mouseY, this.x + 180, this.y + 36, 7, 16)){
                    selectedItemCategoryOffset++;
                    MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
                    return;
                }

            }

            for(int i = 0; i < 9; i++) {
                if(GoopyScreen.isOnButton(mouseX, mouseY, this.x + 18 + (i * 18), this.y + 110, 16, 16)) {
                    int slotID = 45 + i;
                    if(shiftKey){

                        this.handler.setStackInSlot(slotID, this.handler.nextRevision(), ItemStack.EMPTY);
                    }
                    else{
                        Slot slot = this.handler.getSlot(slotID);
                        if (slot == null) {
                            return;
                        }
                        if(slot.canInsert(this.handler.getCursorStack())){
                            if(button == 1 && this.handler.getCursorStack().isEmpty()){
                                ItemStack slotStack = slot.getStack().copy();
                                int half = (int) Math.ceil(slotStack.getCount()/2f);
                                slotStack.setCount(slotStack.getCount() - half);
                                this.handler.setStackInSlot(slotID, this.handler.nextRevision(), slotStack);
                                ItemStack cursorStack = slot.getStack().copy();
                                cursorStack.setCount(half);
                                this.handler.setCursorStack(cursorStack);
                                return;
                            }
                            if(button == 0 || (!slot.getStack().isEmpty() && slot.getStack().getItem() != this.handler.getCursorStack().getItem())){
                                if(slot.getStack().getItem() == this.handler.getCursorStack().getItem()){
                                    ItemStack cursorStack = this.handler.getCursorStack().copy();
                                    ItemStack slotStack = slot.getStack().copy();
                                    int count = Math.clamp(slotStack.getCount() + cursorStack.getCount(), 0, Math.min(slotStack.getMaxCount(), this.client.player.getInventory().getMaxCountPerStack()));
                                    slotStack.setCount(count);
                                    this.handler.setStackInSlot(slotID, this.handler.nextRevision(), slotStack);
                                    int remaining = cursorStack.getCount() - count;
                                    cursorStack.setCount(remaining);
                                    this.handler.setCursorStack(cursorStack);
                                    return;
                                }
                                ItemStack cursorStack = slot.getStack().copy();
                                this.handler.setStackInSlot(slotID, this.handler.nextRevision(), this.handler.getCursorStack().copy());
                                this.handler.setCursorStack(cursorStack);
                            }
                            else{
                                ItemStack cursorStack = this.handler.getCursorStack().copy();
                                cursorStack.decrement(1);
                                ItemStack slotStack = cursorStack.copy();
                                slotStack.setCount(slot.getStack().getCount() + 1);
                                this.handler.setStackInSlot(slotID, this.handler.nextRevision(), slotStack);


                                this.handler.setCursorStack(cursorStack);
                            }
                        }
                        //this.client.interactionManager.clickSlot(this.handler.syncId, slotID, button, SlotActionType.PICKUP, this.client.player);
                    }
                    this.client.player.playerScreenHandler.sendContentUpdates();
                    return;
                }
            }
            if(activeCategory.itemCategory != null) {

                for (int y = 0; y < 4; y++) {
                    for (int x = 0; x < 3; x++) {
                        int slotX = this.x + 18 + (x * 18);
                        int slotY = this.y + 36 + (y * 18);

                        int index = (y * 3) + x;
                        if(index >= activeCategory.itemCategory.ITEM_ENTRIES.size()) continue;
                        if(GoopyScreen.isOnButton(mouseX, mouseY, slotX, slotY, 16, 16)) {
                            if(selectedItemCategory == index) {
                                selectedItemCategory = -1;
                                MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
                                return;
                            }
                            this.selectedItemCategory = index;
                            selectedItemCategoryOffset = 0;
                            lastPreviewStackIndex = 0;
                            MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
                            return;
                        }
                    }
                }
                if(selectedItemCategory > -1) {
                    ItemCategory.Entry entry = (ItemCategory.Entry) activeCategory.itemCategory.ITEM_ENTRIES.toArray()[selectedItemCategory];
                    for (int x = 0; x < 5; x++) {
                        int slotX = this.x + 90 + (x * 18);

                        if (entry.items().isEmpty()) continue;
                        int index = (x + selectedItemCategoryOffset) % entry.items().size();
                        ItemStack stack = (ItemStack) entry.items().toArray()[index];
                        if (GoopyScreen.isOnButton(mouseX, mouseY, slotX, this.y + 36, 16, 16)) {

                            boolean isEmpty = this.handler.getCursorStack().isEmpty();
                            if (!isEmpty) {
                                if (ItemStack.areItemsAndComponentsEqual(stack, this.handler.getCursorStack())) {
                                    ItemStack cursorStack = this.handler.getCursorStack();
                                    int totalCount = cursorStack.getCount() + (button == 0 ? stack.getCount() : -stack.getCount());
                                    int maxCount = Math.min(stack.getMaxCount(), this.client.player.getInventory().getMaxCountPerStack());
                                    if (totalCount <= maxCount) {
                                        ItemStack newStack = stack.copy();
                                        newStack.setCount(totalCount);
                                        if (shiftKey) {
                                            newStack.setCount(newStack.getMaxCount());
                                        }
                                        ((CreativeScreenHandler) this.handler).setCursorStack(newStack);
                                        this.client.player.playerScreenHandler.sendContentUpdates();
                                        return;
                                    }
                                } else {
                                    stack = ItemStack.EMPTY;
                                    ((CreativeScreenHandler) this.handler).setCursorStack(stack);
                                    this.client.player.playerScreenHandler.sendContentUpdates();
                                }
                            }
                            ((CreativeScreenHandler) this.handler).setCursorStack(ItemStack.EMPTY);

                            ItemStack newStack = stack.copy();
                            if (shiftKey) {
                                newStack.setCount(newStack.getMaxCount());
                            }
                            ((CreativeScreenHandler) this.handler).setCursorStack(newStack);
                            this.client.player.playerScreenHandler.sendContentUpdates();
                            return;
                        }
                    }
                }
            }
            this.client.interactionManager.dropCreativeStack(((CreativeScreenHandler)this.handler).getCursorStack());
            ((CreativeScreenHandler)this.handler).setCursorStack(ItemStack.EMPTY);
            this.client.player.playerScreenHandler.sendContentUpdates();
            return;
        }
    }

    public void clickProps(double mouseX, double mouseY, int button) {

    }

    public void clickTech(double mouseX, double mouseY, int button) {

    }



    protected void drawFnafBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        for (ItemGroup itemGroup : ItemGroups.getGroupsToDisplay()) {
            if (itemGroup != getSelectedItemGroup() || Objects.equals(itemGroup.getDisplayName().getString(), FnafUniverseRebuilt.MOD_ID)) {
                this.renderTabIcon(context, mouseX, mouseY, itemGroup);
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

        context.drawTexture(RenderPipelines.GUI_TEXTURED, getSelectedItemGroup().getTexture(), this.x, this.y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);

        context.drawTexture(RenderPipelines.GUI_TEXTURED, getSelectedItemGroup().getTexture(), this.x + 8, this.y + 6, 0.0F, this.backgroundHeight + (SubTab.index * 5), 28, 5, 256, 256);

        if (!isFnafTab) {
            this.renderTabIcon(context, mouseX, mouseY, getSelectedItemGroup());
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
        AbstractTexture gpuTextureView = this.client.getTextureManager().getTexture(texture);
        drawFloor(pipeline, context, delta, mouseX, mouseY, gpuTextureView, x, maxX, y, maxY);
    }
    private void renderRoom(RenderPipeline pipeline, DrawContext context, float delta, int mouseX, int mouseY, GpuTextureView texture, int x, int maxX, int y, int maxY) {

    }
    private void drawFloor(RenderPipeline pipeline, DrawContext context, float delta, int mouseX, int mouseY, AbstractTexture texture, int x, int maxX, int y, int maxY) {

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
                                    pipeline,  TextureSetup.of(texture.getGlTextureView(), texture.getSampler()), new Matrix3x2f(context.getMatrices()), (int)thisX, y1, (int)thisX, y2, (int)nextX, y2, (int)nextX, y1, u + floorX, bottomU + floorX, bottomU2 + floorX, u2 + floorX, v1, v2, v2, v1, 0xFFFFFFFF, context.scissorStack.peekLast()
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
    protected void renderTabIcon(DrawContext context, int mouseX, int mouseY, ItemGroup group) {
        final FabricItemGroupImpl fabricItemGroup = (FabricItemGroupImpl) group;

        if (!Objects.equals(group.getDisplayName().getString(), FnafUniverseRebuilt.MOD_ID) || fabricItemGroup.fabric_getPage() != getCurrentPage()) {
            super.renderTabIcon(context, mouseX, mouseY, group);
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
        ITEM_WITH_ALTS_SELECTION(5),
        BEAR5(6),
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
        isFnafTab = Objects.equals(getSelectedItemGroup().getDisplayName().getString(), FnafUniverseRebuilt.MOD_ID);
        if (isFnafTab) {
            return;
        }
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
