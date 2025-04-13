package net.zephyr.fnafur.client.gui.screens.computer.apps;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.utility_blocks.computer.ComputerBlock;
import net.zephyr.fnafur.blocks.utility_blocks.computer.ComputerData;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.networking.nbt_updates.computer.ComputerEjectPayload;
import net.zephyr.fnafur.util.Computer.ComputerAI;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class COMPCodeScreen extends COMPBaseAppScreen {
    private final int DEFAULT = 0;
    private final int BEHAVIOR_LIST = 1;
    private final int BEHAVIOR_TEXT_EDIT = 2;
    private final int ANIMATRONIC_SELECTION = 100;
    boolean Dirty = false;
    String[] animCategory = new String[16];
    int[] pathIndex = new int[16];
    int subAnimCategory = 0;
    String currentAnimatronic = "";
    String currentBehavior = "";
    int subListX = 0, subListY = 0;
    int subWindow = 0;
    int subScroll = 0;
    List<?> popupList = new ArrayList<>();
    int maxSubScroll = 0;
    Identifier BASE = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/code_window.png");
    Identifier LIST = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/computer_list.png");
    Identifier BUTTONS = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/computer_buttons.png");
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final World world;
    private NbtCompound diskData;
    private NbtCompound hourCompound;
    private final byte rollOver = 96;
    private byte hour = 0;
    ItemStack cpu;
    private static final Vector3f ENTITY_TRANSLATION = new Vector3f();
    private static final Quaternionf ENTITY_ROTATION = new Quaternionf().rotationXYZ(0.2f, (float) Math.PI, (float) Math.PI);
    private LivingEntity entity;
    private int subListNum = 0;
    String numEdit = "";
    int numEditIndex = 0;
    private int editNum = 0;
    private int editNumIndex = 0;

    public COMPCodeScreen(Text text, NbtCompound nbtCompound, Object o) {
        super(text, nbtCompound, o);
        world = client.world != null ? client.world : null;
        if (world == null) return;
        cpu = ItemStack.fromNbtOrEmpty(world.getRegistryManager(), getNbtData().getCompound("ai_data"));
        diskData = cpu.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        this.hourCompound = diskData.getCompound("" + this.hour);

        this.currentBehavior = hourCompound.getString("Behavior");
        this.currentAnimatronic = diskData.getString("entity");
        if (ComputerData.getAIAnimatronic(this.currentAnimatronic) instanceof ComputerData.Initializer.AnimatronicAI ai) {
            updateEntity(ai.entityType());
        }
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void tick() {
        if (this.entity instanceof LivingEntity) {
            float rotation = this.entity.getHeadYaw() + 1;
            this.entity.setHeadYaw(rotation);
            this.entity.setBodyYaw(rotation);
        }
        super.tick();
    }

    @Override
    public void closeButton() {
        if (!Dirty) {
            super.closeButton();
        }
    }

    @Override
    void renderCloseButton(DrawContext context, int mouseX, int mouseY) {
        renderButton(BUTTONS, context, this.width / 2 + 248 / 2 - 2 - 16, this.height / 2 - 248 / 2 + 2, 0, 0, 16, 0, 16 * 2, 0, 16, 16, 128, 128, mouseX, mouseY, getHolding(), Dirty);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isOnButton(mouseX, mouseY, this.width / 2 + 248 / 2 - 2 - (int) (16 * 2f), this.height / 2 - 248 / 2 + 2, 16, 16)) {
            if (Dirty) {
                saveChanges();
                this.Dirty = false;
            }
        }
        if (isOnButton(mouseX, mouseY, this.width / 2 + 248 / 2 - 2 - (int) (16 * 3f), this.height / 2 - 248 / 2 + 2, 16, 16)) {
            if (!cpu.isEmpty()) {
                close();

                ClientPlayNetworking.send(new ComputerEjectPayload(getBlockPos().asLong()));
                BlockState state = MinecraftClient.getInstance().world.getBlockState(getBlockPos());
                if(state.isOf(BlockInit.COMPUTER)){
                    ((ComputerBlock)state.getBlock()).ejectFloppy(MinecraftClient.getInstance().world, getBlockPos());
                }
            }
        }

        if (!cpu.isEmpty()) {
            int x = (int) topCornerX + ((int) appAvailableSizeX / 2) + 10;
            if (this.subWindow == DEFAULT) {
                if (isOnButton(mouseX, mouseY, x, (int) topCornerY, 90, 22)) {
                    this.subWindow = ANIMATRONIC_SELECTION;
                } else if (cpu.isOf(ItemInit.CPU) && isOnButton(mouseX, mouseY, (int) topCornerX, (int) topCornerY, 126, 22)) {
                    openSubList((int) mouseX, (int) mouseY, ComputerData.getAIBehaviors());
                }


                if (cpu.isOf(ItemInit.CPU) && ComputerData.getAIBehavior(currentBehavior) instanceof ComputerAI ai) {
                    for (int i = 0; i < ai.getList().size(); i++) {
                        ComputerAI.Option<?> option = ai.getList().get(i);
                        int dependency = ai.getList().indexOf(ai.getOption(option.getDependency()));
                        boolean bl = !option.getDependency().isEmpty() && !hourCompound.getBoolean("" + dependency);
                        bl = option.isInvert() != bl;

                        if (option.getDefaultValue() instanceof Boolean) {
                            if (!bl && isOnButton(mouseX, mouseY, (int) topCornerX + 3, 22 + 3 + (int) topCornerY + (i * 18), 12, 12)) {
                                hourCompound.putBoolean("" + i, !hourCompound.getBoolean("" + i));
                                Dirty = true;
                            }
                        } else if (option.getDefaultValue() instanceof List<?>) {
                            if (!bl && isOnButton(mouseX, mouseY, (int) topCornerX + 3, 22 + 3 + (int) topCornerY + (i * 18), 12, 12)) {
                                if (this.entity instanceof DefaultEntity ent) {
                                    subListNum = i;
                                    openSubList((int) mouseX, (int) mouseY, ent.getDataList(ai.getId() + "." + option.getId()));
                                }
                            }
                        } else if (option.getDefaultValue() instanceof Integer) {
                            if (!bl && isOnButton(mouseX, mouseY, (int) topCornerX, 22 + (int) topCornerY + (i * 18), 126, 18)) {
                                this.subWindow = BEHAVIOR_TEXT_EDIT;
                                this.numEdit = "" + hourCompound.getInt("" + i);
                                if(this.numEdit.equals("0")) this.numEdit = "";
                                this.editNum = i;
                                this.numEditIndex = numEdit.length();
                                this.editNumIndex = 0;
                            }
                        } else if (option.getDefaultValue() instanceof BlockPos) {
                            for(int j = 0; j < 3; j++){
                                if (!bl && isOnButton(mouseX, mouseY, (int) topCornerX + (j * 42), 22 + (int) topCornerY + (i * 18), 42, 18)) {
                                    this.subWindow = BEHAVIOR_TEXT_EDIT;
                                    BlockPos pos = BlockPos.fromLong(hourCompound.getLong("" + i));
                                    int num = j == 0 ? pos.getX() : j == 1 ? pos.getY() : pos.getZ();
                                    this.numEdit = "" + num;
                                    if(this.numEdit.equals("0")) this.numEdit = "";
                                    this.editNum = i;
                                    this.numEditIndex = numEdit.length();
                                    this.editNumIndex = j + 1;
                                }
                            }
                        }
                    }
                }

            } else if (this.subWindow == ANIMATRONIC_SELECTION) {
                if (isOnButton(mouseX, mouseY, x, (int) topCornerY + 22, 90, (int) this.appAvailableSizeY - 22)) {
                    if (animCategory[subAnimCategory] == null || subAnimCategory == 0) {
                        setAnimCategory(ComputerData.getAICategories(), (int) mouseX, (int) mouseY);
                    } else {
                        if (getCurrentCategory() != null) {
                            setAnimCategory(getCurrentCategory().list, (int) mouseX, (int) mouseY);
                        }
                    }
                } else {
                    closeAllSubwindows();
                }
            } else if (this.subWindow == BEHAVIOR_LIST) {
                if (isOnButton(mouseX, mouseY, subListX, subListY, 94, 151)) {
                    for (int i = 0; i < popupList.size(); i++) {
                        int y = 4 + subListY + (i * 16) + subScroll;
                        if (y > subListY + 3 && y < subListY + 147) {

                            if (isOnButton(mouseX, mouseY, 4 + subListX, y, 86, 16)) {
                                if (popupList.get(i) instanceof ComputerAI ai) {
                                    this.currentBehavior = ai.getId();
                                    this.Dirty = true;
                                    setNewBehavior();
                                    closeAllSubwindows();
                                } else if (popupList.get(i) instanceof String st) {
                                    hourCompound.putString("" + subListNum, st);
                                    this.Dirty = true;
                                    closeAllSubwindows();
                                }
                            }
                        }
                    }
                } else {
                    closeAllSubwindows();
                }
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void openSubList(int mouseX, int mouseY, List<?> list) {
        this.subListX = mouseX;
        this.subListY = mouseY;
        this.subWindow = BEHAVIOR_LIST;
        this.popupList = list;
    }

    private void closeAllSubwindows() {
        this.subWindow = DEFAULT;
        this.subAnimCategory = 0;
        this.animCategory = new String[16];
        this.pathIndex = new int[16];
        this.subScroll = 0;
        this.subListX = 0;
        this.subListY = 0;
        this.popupList = new ArrayList<>();
        this.subListNum = 0;
        this.editNum = 0;
        this.editNumIndex = 0;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.subWindow == DEFAULT && !Dirty) {
            this.hour = (byte) (verticalAmount < 0 ? hour + 1 : hour - 1);
            this.hour = (byte) (this.hour < 0 ? this.rollOver - 1 : this.hour > this.rollOver - 1 ? 0 : this.hour);

            this.hourCompound = diskData.getCompound("" + hour);
            this.currentBehavior = hourCompound.getString("Behavior");
        } else if (this.subWindow == ANIMATRONIC_SELECTION) {
            int diff = verticalAmount < 0 ? -15 : 15;
            this.subScroll = this.subScroll + diff < -this.maxSubScroll ? -this.maxSubScroll : Math.min(this.subScroll + diff, 0);
        } else if (this.subWindow == BEHAVIOR_LIST) {
            int diff = verticalAmount < 0 ? -16 : 16;
            this.subScroll = this.subScroll + diff < -this.maxSubScroll ? -this.maxSubScroll : Math.min(this.subScroll + diff, 0);
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.subWindow == BEHAVIOR_TEXT_EDIT) {

            String add = switch (keyCode) {
                default -> "";
                case GLFW.GLFW_KEY_0, GLFW.GLFW_KEY_KP_0 -> "0";
                case GLFW.GLFW_KEY_1, GLFW.GLFW_KEY_KP_1 -> "1";
                case GLFW.GLFW_KEY_2, GLFW.GLFW_KEY_KP_2 -> "2";
                case GLFW.GLFW_KEY_3, GLFW.GLFW_KEY_KP_3 -> "3";
                case GLFW.GLFW_KEY_4, GLFW.GLFW_KEY_KP_4 -> "4";
                case GLFW.GLFW_KEY_5, GLFW.GLFW_KEY_KP_5 -> "5";
                case GLFW.GLFW_KEY_6, GLFW.GLFW_KEY_KP_6 -> "6";
                case GLFW.GLFW_KEY_7, GLFW.GLFW_KEY_KP_7 -> "7";
                case GLFW.GLFW_KEY_8, GLFW.GLFW_KEY_KP_8 -> "8";
                case GLFW.GLFW_KEY_9, GLFW.GLFW_KEY_KP_9 -> "9";
                case GLFW.GLFW_KEY_MINUS, GLFW.GLFW_KEY_KP_SUBTRACT -> "-";
            };

            int limit = editNumIndex == 2 ? 3 : 6;
            if(numEdit.isEmpty()) numEdit = add;
            else if(numEdit.length() < limit) numEdit = new StringBuilder(numEdit).insert(numEditIndex, add).toString();

            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if(!numEdit.isEmpty()) {
                    if(numEditIndex > 0) {
                        numEditIndex--;
                        numEdit = numEdit.substring(0, numEditIndex) + numEdit.substring(numEditIndex + 1);
                    }
                    return true;
                }
            }
            if (keyCode == GLFW.GLFW_KEY_RIGHT) {
                if(numEditIndex < numEdit.length()){
                    numEditIndex++;
                }
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_LEFT) {
                if(numEditIndex > 0){
                    numEditIndex--;
                }
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_ENTER) {
                this.Dirty = true;
                if(numEdit.isEmpty()) numEdit = "0";
                int num = Integer.parseInt(numEdit);

                if(editNumIndex == 0) {
                    this.hourCompound.putInt("" + editNum, num);
                }
                else {
                    BlockPos pos = BlockPos.fromLong(this.hourCompound.getLong("" + editNum));
                    BlockPos newPos = switch (editNumIndex){
                        case 1 -> new BlockPos(num, pos.getY(), pos.getZ());
                        case 2 -> new BlockPos(pos.getX(), num, pos.getZ());
                        case 3 -> new BlockPos(pos.getX(), pos.getY(), num);
                        default -> new BlockPos(0, 0, 0);
                    };
                    this.hourCompound.putLong("" + editNum, newPos.asLong());
                }
                this.closeAllSubwindows();
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                this.closeAllSubwindows();
                return true;
            }
            numEditIndex++;
            return !add.isEmpty();
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill((int)topCornerX, (int) topCornerY, (int)(topCornerX + appAvailableSizeX), (int)(topCornerY + appAvailableSizeY), -100, 0xFF182562);

        if(!cpu.isEmpty()) {
            context.drawTexture(RenderLayer::getGuiTextured, BASE, (int) topCornerX + ((int) appAvailableSizeX / 2) + 6, (int) topCornerY - 1, 0, 0, 114, (int) appAvailableSizeY + 2, 256, 256);
            drawEntitySelection(context, mouseX, mouseY);
            if(cpu.isOf(ItemInit.CPU)) {
                drawBehaviorSelection(context, mouseX, mouseY);
                sideScrollWheel(context);
            }
            else {
                context.fill((int)topCornerX, (int)topCornerY, (int)topCornerX + 126, (int)topCornerY + (int)appAvailableSizeY, 0x66000000);
                context.fill((int)topCornerX + (int)appAvailableSizeX - 16, (int)topCornerY, (int)topCornerX + (int)appAvailableSizeX, (int)topCornerY + (int)appAvailableSizeY, 0x66000000);
            }

            drawTextEdit(context);
        }
        else {
            Text noDisk = Text.translatable("fnafur.screens.computer_code.no_cpu");
            drawResizableText(context, client.textRenderer, noDisk, 2.5f, this.width /2f, this.height /2f, 0xFFFFFFFF, 0x00000000, false, true);
        }
        super.render(context, mouseX, mouseY, delta);
        boolean blockSaveBL = !Dirty;
        renderButton(BUTTONS, context, this.width / 2 + 248 / 2 - 2 - (int) (16 * 2f), this.height / 2 - 248 / 2 + 2, 16*3, 16*2, 16*4, 16*2, 16*5, 16*2, 16, 16, 128, 128, mouseX, mouseY, getHolding(), blockSaveBL);

        renderButton(BUTTONS, context, this.width / 2 + 248 / 2 - 2 - (int) (16 * 3f), this.height / 2 - 248 / 2 + 2, 16*3, 16*3, 16*4, 16*3, 16*5, 16*3, 16, 16, 128, 128, mouseX, mouseY, getHolding(), cpu.isEmpty());
    }
    void drawBehaviorSelection(DrawContext context, int mouseX, int mouseY){
        if(ComputerData.getAIBehavior(currentBehavior) instanceof ComputerAI ai){
            for(int i = 0; i < ai.getList().size(); i++){
                ComputerAI.Option<?> option = ai.getList().get(i);
                context.drawTexture(RenderLayer::getGuiTextured, BASE, (int)topCornerX, 22 + (int)topCornerY + i * 18, 118, 132, 126, 18, 256, 256);

                int dependency = ai.getList().indexOf(ai.getOption(option.getDependency()));
                boolean bl = !option.getDependency().isEmpty() && !hourCompound.getBoolean("" + dependency);
                bl = option.isInvert() != bl;
                if(bl) context.drawTexture(RenderLayer::getGuiTextured, BASE, (int)topCornerX, 22 + (int)topCornerY + i * 18, 118, 150, 126, 18, 256, 256);

                int availableSpace = 116;
                if(!bl) {
                    if (option.getDefaultValue() instanceof Boolean) {
                        boolean bl2 = hourCompound.getBoolean("" + i);
                        super.renderButton(BASE, context, (int) topCornerX + 3, 22 + 3 + (int) topCornerY + (i * 18), 118, 168, 118, 168, 130, 168, 12, 12, 256, 256, mouseX, mouseY, false, bl2);
                        drawAutoResizedText(context, textRenderer, option.getName(), 0.8f, availableSpace, (int) topCornerX + 18, 22 + 5 + (int) topCornerY + (i * 18), 0xFF000000, 0x00000000, false, false);
                    } else if (option.getDefaultValue() instanceof List<?>) {
                        boolean bl2 = this.entity instanceof DefaultEntity;
                        context.drawTexture(RenderLayer::getGuiTextured, BASE, (int) topCornerX, 22 + (int) topCornerY + i * 18, 118, 180, 126, 18, 256, 256);
                        drawAutoResizedText(context, textRenderer, option.getName(), 0.5f, availableSpace, (int) topCornerX + 18, 22 + 2 + (int) topCornerY + (i * 18), 0xFF000000, 0x00000000, false, false);
                        String st = hourCompound.getString("" + i);
                        if (!st.isEmpty() && bl2) {
                            drawAutoResizedText(context, textRenderer, Text.translatable(st), 0.5f, availableSpace, (int) topCornerX + 20, 22 + 9 + (int) topCornerY + (i * 18), 0xFF000000, 0x00000000, false, false);
                        }
                        super.renderButton(BASE, context, (int) topCornerX + 3, 22 + 3 + (int) topCornerY + (i * 18), 142, 168, 142, 168, 154, 168, 12, 12, 256, 256, mouseX, mouseY, false, bl2);
                    } else if (option.getDefaultValue() instanceof BlockPos) {
                        context.drawTexture(RenderLayer::getGuiTextured, BASE, (int) topCornerX, 22 + (int) topCornerY + i * 18, 118, 198, 126, 18, 256, 256);

                        BlockPos pos = BlockPos.fromLong(hourCompound.getLong("" + i));
                        drawAutoResizedText(context, textRenderer, Text.literal("X: " + pos.getX()), 0.5f, 126, (int) topCornerX + 5, 22 + 9 + (int) topCornerY + (i * 18), 0xFF000000, 0x00000000, false, false);
                        drawAutoResizedText(context, textRenderer, Text.literal("Y: " + pos.getY()), 0.5f, 126, (int) topCornerX + (availableSpace / 2f) - 7, 22 + 9 + (int) topCornerY + (i * 18), 0xFF000000, 0x00000000, false, false);
                        drawAutoResizedText(context, textRenderer, Text.literal("Z: " + pos.getZ()), 0.5f, 126, (int) topCornerX + ((availableSpace / 3f) * 2) + 20, 22 + 9 + (int) topCornerY + (i * 18), 0xFF000000, 0x00000000, false, false);

                        drawAutoResizedText(context, textRenderer, option.getName(), 0.5f, availableSpace, (int) topCornerX + 63, 22 + 2 + (int) topCornerY + (i * 18), 0xFF000000, 0x00000000, false, true);

                    } else if (option.getDefaultValue() instanceof Integer) {
                        int num = hourCompound.getInt("" + i);
                        context.drawTexture(RenderLayer::getGuiTextured, BASE, (int) topCornerX, 22 + (int) topCornerY + i * 18, 118, 216, 126, 18, 256, 256);

                        drawAutoResizedText(context, textRenderer, Text.literal("" + num), 0.5f, 126, (int) topCornerX + 5, 22 + 9 + (int) topCornerY + (i * 18), 0xFF000000, 0x00000000, false, false);
                        drawAutoResizedText(context, textRenderer, option.getName(), 0.5f, availableSpace, (int) topCornerX + 63, 22 + 2 + (int) topCornerY + (i * 18), 0xFF000000, 0x00000000, false, true);

                    }
                }
            }
        }


        Text behaviorName = ComputerData.getAIBehavior(this.currentBehavior) instanceof ComputerAI ai ? ai.getName() : Text.translatable("fnafur.screens.computer_code.no_behavior");
        super.renderButton(BASE, context, (int) topCornerX, (int) topCornerY, 118, 0, 118, 22, 118, 44,126, 22, 256, 256, mouseX, mouseY, this.getHolding(), this.popupList != null && !this.popupList.isEmpty() && this.popupList.getFirst() instanceof ComputerAI);
        drawAutoResizedText(context, textRenderer, behaviorName, 1f, 121, topCornerX + 63, topCornerY + 7, 0xFF000000, 0x00000000, false, true);

        if(subWindow == BEHAVIOR_LIST){
            drawList(context, mouseX, mouseY);
        }
    }

    void drawList(DrawContext context, int mouseX, int mouseY){
        context.fill(subListX, subListY, subListX + 94, subListY + 151, 0xFF666666);
        context.drawTexture(RenderLayer::getGuiTextured, LIST, subListX, subListY, 0, 0, 94, 151, 256, 256);

        this.maxSubScroll = Math.max((this.popupList.size() - 9) * 16, 0);
        for(int i = 0; i < this.popupList.size(); i++){
            int y = 4 + subListY + (i * 16) + subScroll;
            int height = 16;
            if(y > subListY + 3 && y < subListY + 147) {
                super.renderButton(LIST, context, 4 + subListX, y, 94, 0, 94, 16, 94, 32, 86, height, 256, 256, mouseX, mouseY, this.getHolding());
                if (this.popupList.get(i) instanceof ComputerAI ai) {
                    super.drawAutoResizedText(context, textRenderer, ai.getName(), 0.85f, 81, subListX + 47, y + 5, 0xFF000000, 0x00000000, false, true);
                }
                else if(this.popupList.get(i) instanceof String anim) {
                    super.drawAutoResizedText(context, textRenderer, Text.translatable(anim), 0.85f, 81, subListX + 47, y + 5, 0xFF000000, 0x00000000, false, true);
                }
            }
        }
    }
    void drawTextEdit(DrawContext context){
        if(this.subWindow == BEHAVIOR_TEXT_EDIT){
            context.fill((int)topCornerX, (int) topCornerY, (int)(topCornerX + appAvailableSizeX), (int)(topCornerY + appAvailableSizeY), 0xAA000000);

            drawResizableText(context, textRenderer, Text.translatable("fnafur.screens.computer_code.edit_number"), 1.5f, this.width /2f, this.height /2f - 10, 0xFFFFFFFF, 0x00000000, false, true);
            drawResizableText(context, textRenderer, Text.translatable("fnafur.screens.computer_code.edit_number.esc"), 0.85f, topCornerX + 3, topCornerY + 3, 0xFFFFFFFF, 0x00000000, false, false);
            drawResizableText(context, textRenderer, Text.translatable("fnafur.screens.computer_code.edit_number.ent"), 0.85f, topCornerX + 3, topCornerY + 15, 0xFFFFFFFF, 0x00000000, false, false);

            String st = this.numEdit;
            if(numEditIndex > numEdit.length()) numEditIndex = numEdit.length();
            if(!st.isEmpty()) {
                int limit = editNumIndex == 2 ? 3 : 6;
                if (numEditIndex == st.length() && st.length() < limit) st = st + "_";
                else st = new StringBuilder(st).insert(numEditIndex, "|").toString();
            }
            else st = "_";

            String prefix = switch (this.editNumIndex){
                default -> "";
                case 1 -> "X: ";
                case 2 -> "Y: ";
                case 3 -> "Z: ";
            };

            st = prefix + st;
            Text txt = Text.literal(st);
            drawResizableText(context, textRenderer, txt, 1.75f, this.width /2f, this.height /2f + 10, 0xFFFFFFFF, 0x00000000, false, true);
        }
    }
    void drawEntitySelection(DrawContext context, int mouseX, int mouseY){
        drawEntity(context, this.topCornerX - 3 + (this.appAvailableSizeX / 4) * 3, this.topCornerY + (this.appAvailableSizeY / 12) * 11);
        Text entityName = this.entity instanceof LivingEntity ? this.entity.getName() : Text.translatable("fnafur.screens.computer_code.no_entity");

        if(this.subWindow == ANIMATRONIC_SELECTION) {
            if(animCategory[subAnimCategory] == null || subAnimCategory == 0) {
                drawCategories(context, ComputerData.getAICategories(), mouseX, mouseY);
            }
            else {
                if(getCurrentCategory() != null){
                    drawCategories(context, getCurrentCategory().list, mouseX, mouseY);
                }
            }
        }

        super.drawAutoResizedText(context, textRenderer, entityName, 1, 85, topCornerX + (appAvailableSizeX / 2f) + 10 + 45, topCornerY + 7, 0xFF000000, 0x00000000, false, true);
        super.renderButton(BASE, context, (int) topCornerX + ((int) appAvailableSizeX / 2) + 10, (int) topCornerY, 118, 66, 118, 88, 118, 110,90, 22, 256, 256, mouseX, mouseY, this.getHolding(), this.subWindow == this.ANIMATRONIC_SELECTION);

    }
    void drawCategories(DrawContext context, List<?> list, int mouseX, int mouseY){
        this.maxSubScroll = Math.max((list.size() * 22) - ((int)appAvailableSizeY - 22), 0);
        for (int i = 0; i < list.size(); i++) {
            int x = (int) topCornerX + ((int) appAvailableSizeX / 2) + 10;
            int y = subScroll + (int) topCornerY + 22 + i * 22;
            float textY = y + 7;
            if(list.get(i) instanceof ComputerData.Initializer.AnimatronicCategory<?> category) {
                if (category.texture != null) {
                    super.renderButton(category.texture, context, x, y, 0, 0, 0, 22, 0, 44, 90, 22, 90, 66, mouseX, mouseY, this.getHolding());
                } else {
                    Text name = category.text;

                    super.renderButton(BASE, context, x, y, 118, 66, 118, 88, 118, 110, 90, 22, 256, 256, mouseX, mouseY, this.getHolding());
                    if(textY < topCornerY + appAvailableSizeY && textY > topCornerY + 22)
                        super.drawAutoResizedText(context, textRenderer, name, 1, 85, x + 45, textY, 0xFF000000, 0x00000000, false, true);
                }
            }
            else if(list.get(i) instanceof ComputerData.Initializer.AnimatronicAI ai){
                Text name = ai.entityType().getName();

                super.renderButton(BASE, context, x, y, 118, 66, 118, 88, 118, 110, 90, 22, 256, 256, mouseX, mouseY, this.getHolding());

                if(textY < topCornerY + appAvailableSizeY && textY > topCornerY + 22)
                    super.drawAutoResizedText(context, textRenderer, name, 1, 85, x + 45, (int)textY, 0xFF000000, 0x00000000, false, true);
            }
        }
    }
    void setAnimCategory(List<?> list, int mouseX, int mouseY){
        for (int i = 0; i < list.size(); i++) {
            int x = (int) topCornerX + ((int) appAvailableSizeX / 2) + 10;
            int y = subScroll + (int) topCornerY + 22 + i * 22;
            if(isOnButton(mouseX, mouseY, x, y, 90, 22)) {
                if (list.get(i) instanceof ComputerData.Initializer.AnimatronicCategory<?> category) {
                    this.subAnimCategory++;
                    this.pathIndex[subAnimCategory] = i;
                    this.animCategory[subAnimCategory] = category.id;
                    this.subScroll = 0;
                } else if (list.get(i) instanceof ComputerData.Initializer.AnimatronicAI ai) {
                    this.subAnimCategory = 0;
                    this.pathIndex = new int[16];
                    this.animCategory = new String[16];
                    this.currentAnimatronic = ai.id();
                    this.subWindow = DEFAULT;
                    this.updateEntity(ai.entityType());
                    this.Dirty = true;
                }
            }
        }
    }

    ComputerData.Initializer.AnimatronicCategory<?> getCurrentCategory(){
        if (ComputerData.getAICategory(animCategory[1]) instanceof ComputerData.Initializer.AnimatronicCategory<?> cat) {
            if (subAnimCategory == 1) {
                return cat;
            } else {

                List<?> category = cat.list;
                for (int i = 2; i < subAnimCategory; i++) {
                    if (category.get(pathIndex[i]) instanceof ComputerData.Initializer.AnimatronicCategory<?> subCat) {
                        category = subCat.list;
                    }
                }
                if (category.get(pathIndex[subAnimCategory]) instanceof ComputerData.Initializer.AnimatronicCategory<?> finalCategory) {
                    return finalCategory;
                }
            }
        }
        return null;
    }
    @Override
    public String appName() {
        return "code";
    }

    protected void updateEntity(EntityType<? extends LivingEntity> entityType) {
        this.entity = entityType.create(this.client.world, SpawnReason.TRIGGERED);
        if(this.entity instanceof DefaultEntity ent){
            ent.menuTick = true;
        }
    }
    protected void drawEntity(DrawContext context, float x, float y) {
        if(this.entity instanceof DefaultEntity ent) {
            context.getMatrices().push();
            context.getMatrices().translate(0, 0, -50.0);
            context.getMatrices().translate(0, 0, (float) -50);
            InventoryScreen.drawEntity(context, x, y, ent.demo_scale() * ((float) 40.0), COMPCodeScreen.ENTITY_TRANSLATION, COMPCodeScreen.ENTITY_ROTATION, null, this.entity);
            context.getMatrices().pop();
        }

    }
    private void sideScrollWheel(DrawContext context){
        for (int i = -6; i < 6; i = i + 1) {
            int time = hour + i;
            int num = time < 0 ? time + rollOver : time > rollOver - 1 ? time - rollOver : time;
            num = num / 4;
            float scale = 0.4f + (0.3f / (Math.abs(i) == 0 ? 1 : Math.abs(i)));
            float x = appAvailableSizeX + topCornerX - 5 - (scale * 8);
            float y = ((this.height / 2f) + 6 + (i * 16)) - (3f * scale);

            if(time%4 == 0)
                drawResizableText(context, client.textRenderer, Text.literal("" + num), scale, x, y, 0xBBFFFFFF, 0x00000000, false, true);
        }
        if(Dirty){
            context.fill((int) (appAvailableSizeX + topCornerX - 16), (int) topCornerY, (int) (appAvailableSizeX + topCornerX), (int) (topCornerY + appAvailableSizeY), 0x88000000);
        }
    }
    private void setNewBehavior(){
        if(ComputerData.getAIBehavior(currentBehavior) instanceof ComputerAI ai) {
            hourCompound.putString("Behavior", currentBehavior);
            for (int i = 0; i < ai.getList().size(); i++) {
                setDefaultProperty(i, ai.getList().get(i), ai);
            }
        }
    }
    private void setDefaultProperty(int index, ComputerAI.Option<?> option, ComputerAI ai){
        if(option.getDefaultValue() instanceof Boolean val){
            hourCompound.putBoolean("" + index, val);
        }
        else if(option.getDefaultValue() instanceof Integer val){
            hourCompound.putInt("" + index, val);
        }
        else if(option.getDefaultValue() instanceof Float val){
            hourCompound.putFloat("" + index, val);
        }
        else if(option.getDefaultValue() instanceof BlockPos val){
            hourCompound.putLong("" + index, val.asLong());
        }
        else if(option.getDefaultValue() instanceof List<?>){
            if(this.entity instanceof DefaultEntity ent) {
                List<?> val = ent.getDataList(ai.getId() + "." + option.getId());
                if (val.getFirst() instanceof String st) {
                    hourCompound.putString("" + index, st);
                }
            }
        }

    }
    private void saveChanges(){
        diskData.put("" + hour, hourCompound);
        diskData.putString("entity", currentAnimatronic);
        ItemStack stack = this.cpu.copy();
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
            currentNbt.copyFrom(diskData);
        }));
        getNbtData().put("ai_data", stack.toNbtAllowEmpty(world.getRegistryManager()));
        this.cpu = stack.copy();
        GoopyNetworkingUtils.saveBlockNbt(getBlockPos(), getNbtData());
    }
}
