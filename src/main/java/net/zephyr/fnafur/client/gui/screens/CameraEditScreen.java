package net.zephyr.fnafur.client.gui.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.camera.CameraBlockEntity;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;

import java.util.ArrayList;
import java.util.List;

public class CameraEditScreen extends GoopyScreen {
    Identifier texture = Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/camera/camera_edit.png");
    boolean isActive = false;
    private TextFieldWidget nameField;
    private TextFieldWidget actionName;
    boolean holding = false;
    byte modeX = 0;
    byte modeY = 0;

    byte speedX = 0, speedY = 0;

    final short sliderSizeMin = -44;
    final short sliderSizeMax = 43;
    double xSlider = 0, ySlider = 0,
            xRange0 = 0, yRange0 = 0,
            xRange1 = 0, yRange1 = 0;

    boolean holdingXSlider = false, holdingYSlider = false,
            holdingXRangeMin = false, holdingXRangeMax = false,
            holdingYRangeMin = false, holdingYRangeMax = false;

    boolean flashlight = false;
    boolean action = false;
    byte nightvision = 0;
    boolean sneaking = false;
    boolean renameActionButton = false;

    public CameraEditScreen(Text text, NbtCompound nbtCompound, Object o) {
        super(text, nbtCompound, o);
    }

    @Override
    protected void init() {
        this.renameActionButton = false;
        this.sneaking = false;
        int i = this.width / 2 - 43;
        int j = this.height / 2 - 25;

        this.isActive = getNbtData().getBoolean("Active");
        this.modeX = getNbtData().getByte("ModeX");
        this.modeY = getNbtData().getByte("ModeY");
        this.xSlider = -getNbtData().getDouble("yaw");
        this.ySlider = -getNbtData().getDouble("pitch");
        this.xRange0 = getNbtData().getDouble("minYaw");
        this.xRange1 = getNbtData().getDouble("maxYaw");
        this.yRange0 = getNbtData().getDouble("minPitch");
        this.yRange1 = getNbtData().getDouble("maxPitch");
        this.speedX = getNbtData().getByte("yawSpeed");
        this.speedY = getNbtData().getByte("pitchSpeed");

        this.flashlight = getNbtData().getBoolean("Flashlight");
        this.action = getNbtData().getBoolean("Action");
        this.nightvision = getNbtData().getByte("NightVision");


        this.nameField = new TextFieldWidget(this.textRenderer, i, j, 82, 12, Text.translatable("container.repair"));
        this.nameField.setFocusUnlocked(true);
        this.nameField.setEditableColor(-1);
        this.nameField.setUneditableColor(-1);
        this.nameField.setDrawsBackground(false);
        this.nameField.setMaxLength(50);
        this.nameField.setChangedListener(this::onRenamed);
        this.nameField.setText(getNbtData().getString("Name"));
        this.addSelectableChild(this.nameField);
        this.nameField.setEditable(true);

        this.actionName = new TextFieldWidget(this.textRenderer, this.width / 3, this.height / 2 + 25, this.width/3, 15, Text.translatable("container.repair"));
        this.actionName.setFocusUnlocked(false);
        this.actionName.setEditableColor(-1);
        this.actionName.setUneditableColor(-1);
        this.actionName.setDrawsBackground(true);
        this.actionName.setMaxLength(24);
        this.actionName.setChangedListener(this::onActionRenamed);
        this.actionName.setText(getNbtData().getString("ActionName"));
        this.addSelectableChild(this.actionName);
        this.actionName.setEditable(true);

        this.holding = false;
        super.init();
    }

    @Override
    public void tick() {
        if(!(MinecraftClient.getInstance().world.getBlockEntity(getBlockPos()) instanceof CameraBlockEntity)) {
            MinecraftClient.getInstance().setScreen(null);
        }
        super.tick();
    }

    private void onRenamed(String name){
        NbtCompound newData = getNbtData().copy();
        newData.putString("Name", name);
        compileData(newData);
    }
    private void onActionRenamed(String name){
        NbtCompound newData = getNbtData().copy();
        newData.putString("ActionName", name);
        compileData(newData);
    }

    private void compileData(){
        NbtCompound newData = getNbtData().copy();
        compileData(newData);
    }
    private void compileData(NbtCompound nbt){
        nbt.putBoolean("Active", this.isActive);
        nbt.putByte("ModeX", this.modeX);
        nbt.putByte("ModeY", this.modeY);
        nbt.putDouble("yaw", -this.xSlider);
        nbt.putDouble("pitch", -this.ySlider);
        nbt.putDouble("minYaw", this.xRange0);
        nbt.putDouble("maxYaw", this.xRange1);
        nbt.putDouble("minPitch", this.yRange0);
        nbt.putDouble("maxPitch", this.yRange1);
        nbt.putByte("yawSpeed", this.speedX);
        nbt.putByte("pitchSpeed", this.speedY);
        nbt.putBoolean("Flashlight", this.flashlight);
        nbt.putBoolean("Action", this.action);
        nbt.putByte("NightVision", this.nightvision);

        GoopyNetworkingUtils.saveBlockNbt(getBlockPos(), nbt);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.holding = true;


        if(!this.actionName.isMouseOver(mouseX, mouseY)) {
            this.actionName.setFocused(false);
            this.renameActionButton = false;
            this.actionName.setFocusUnlocked(false);
            this.nameField.setFocusUnlocked(true);
        }
        if(!this.nameField.isMouseOver(mouseX, mouseY)) this.nameField.setFocused(false);


        if(!renameActionButton) {
            if (mouseX > this.width / 2f - 77 && mouseX < this.width / 2f - 66 && mouseY > this.height / 2f + 6 && mouseY < this.height / 2f + 20) {
                this.isActive = !this.isActive;
                float pitch = this.isActive ? 1f : 0.85f;
                MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, pitch);
                compileData();
                return super.mouseClicked(mouseX, mouseY, button);
            }


            if (isOnButton(mouseX, mouseY, this.width / 2 + 51, this.height / 2 + 9, 7, 7)) {
                if (button == 0) {
                    speedX = speedX + 1 > 8 ? 8 : (byte) (speedX + 1);
                    compileData();
                } else if (button == 1) {
                    speedX = speedX - 1 < 0 ? 0 : (byte) (speedX - 1);
                    compileData();
                }
            } else if (isOnButton(mouseX, mouseY, this.width / 2 + 51, this.height / 2 + 21, 7, 7)) {
                if (button == 0) {
                    speedX = speedX + 1 > 8 ? 8 : (byte) (speedX + 1);
                    compileData();
                } else if (button == 1) {
                    speedX = speedX - 1 < 0 ? 0 : (byte) (speedX - 1);
                    compileData();
                }
            }

            if (isActive) {
                for (byte i = 0; i < 3; i++) {
                    int xOffset = i * 13;
                    if (isOnButton(mouseX, mouseY, this.width / 2 - 47 + xOffset, this.height / 2 - 8, 11, 11)) {
                        if (this.modeX != i) {
                            MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
                            this.modeX = i;
                            compileData();
                        }
                    }
                }
                for (byte i = 0; i < 3; i++) {
                    int xOffset = i * 13;
                    if (isOnButton(mouseX, mouseY, this.width / 2 + 10 + xOffset, this.height / 2 - 8, 11, 11)) {
                        if (this.modeY != i) {
                            MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
                            this.modeY = i;
                            compileData();
                        }
                    }

                }

                if (modeX == 0) {
                    int x = this.width / 2 - 2 + (int) xSlider;
                    if (isOnButton(mouseX, mouseY, x, this.height / 2 + 9, 5, 7)) {
                        this.holdingXSlider = true;
                    }
                } else {
                    int x = this.width / 2 - 2;
                    int x1 = x + (int) xRange0;
                    int x2 = x + (int) xRange1;
                    if (isOnButton(mouseX, mouseY, x1, this.height / 2 + 9, 5, 7)) {
                        this.holdingXRangeMin = true;
                    } else if (isOnButton(mouseX, mouseY, x2, this.height / 2 + 9, 5, 7)) {
                        this.holdingXRangeMax = true;
                    }
                }
                if (modeY == 0) {
                    int x = this.width / 2 - 2 + (int) ySlider;
                    if (isOnButton(mouseX, mouseY, x, this.height / 2 + 21, 5, 7)) {
                        this.holdingYSlider = true;
                    }
                } else {
                    int x = this.width / 2 - 2;
                    int x1 = x + (int) yRange0;
                    int x2 = x + (int) yRange1;
                    if (isOnButton(mouseX, mouseY, x1, this.height / 2 + 21, 5, 7)) {
                        this.holdingYRangeMin = true;
                    } else if (isOnButton(mouseX, mouseY, x2, this.height / 2 + 21, 5, 7)) {
                        this.holdingYRangeMax = true;
                    }
                }
                if (isOnButton(mouseX, mouseY, this.width / 2 - 77, this.height / 2 - 28, 10, 8)) {
                    MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
                    this.flashlight = !flashlight;
                    compileData();
                }
                if (isOnButton(mouseX, mouseY, this.width / 2 - 77, this.height / 2 - 19, 10, 8)) {
                    MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
                    this.action = !action;
                    compileData();
                }
                if (isOnButton(mouseX, mouseY, this.width / 2 - 77, this.height / 2 - 10, 10, 8)) {
                    MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
                    this.nightvision = nightvision - 1 < 0 ? 2 : (byte) (nightvision - 1);
                    compileData();
                }

                if (action) {
                    if (isOnButton(mouseX, mouseY, this.width / 2 - 87, this.height / 2 - 18, 6, 7)) {
                        MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
                        this.renameActionButton = true;
                        this.nameField.setFocused(false);
                        this.nameField.setFocusUnlocked(false);
                        this.actionName.setFocusUnlocked(true);
                        this.actionName.setFocused(true);
                    }
                }
            }
        }
        else {

        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if(this.holdingXSlider){
            double comp = this.xSlider + deltaX;
            this.xSlider = comp < sliderSizeMin ? sliderSizeMin : comp > sliderSizeMax ? sliderSizeMax : comp;
        }
        else if(this.holdingYSlider){
            double comp = this.ySlider + deltaX;
            this.ySlider = comp < sliderSizeMin ? sliderSizeMin : comp > sliderSizeMax ? sliderSizeMax : comp;
        }
        else if(this.holdingXRangeMin){
            double comp = this.xRange0 + deltaX;
            this.xRange0 = comp < sliderSizeMin ? sliderSizeMin : Math.min(comp, xRange1 - 5);
        }
        else if(this.holdingXRangeMax){
            double comp = this.xRange1 + deltaX;
            this.xRange1 = comp < xRange0 + 5 ? xRange0 + 5 : comp > sliderSizeMax ? sliderSizeMax : comp;
        }
        else if(this.holdingYRangeMin){
            double comp = this.yRange0 + deltaX;
            this.yRange0 = comp < sliderSizeMin ? sliderSizeMin : Math.min(comp, yRange1 - 5);
        }
        else if(this.holdingYRangeMax){
            double comp = this.yRange1 + deltaX;
            this.yRange1 = comp < yRange0 + 5 ? yRange0 + 5 : comp > sliderSizeMax ? sliderSizeMax : comp;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.holding = false;
        if (this.holdingXSlider ||
                this.holdingYSlider ||
                this.holdingXRangeMin ||
                this.holdingXRangeMax ||
                this.holdingYRangeMin ||
                this.holdingYRangeMax) {

            this.holdingXSlider = false;
            this.holdingYSlider = false;
            this.holdingXRangeMin = false;
            this.holdingXRangeMax = false;
            this.holdingYRangeMin = false;
            this.holdingYRangeMax = false;
            compileData();
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if(isOnButton(mouseX, mouseY, this.width/2 + 51, this.height / 2 + 9, 7, 7)){
            if(horizontalAmount > 0 || verticalAmount > 0) {
                speedX = speedX + 1 > 8 ? 8 : (byte)(speedX + 1);
            }
            else {
                speedX = speedX - 1 < 0 ? 0 : (byte)(speedX - 1);
            }
            compileData();
        }
        else if(isOnButton(mouseX, mouseY, this.width/2 + 51, this.height / 2 + 21, 7, 7)){
            if(horizontalAmount > 0 || verticalAmount > 0) {
                speedY = speedY + 1 > 8 ? 8 : (byte)(speedY + 1);
            }
            else {
                speedY = speedY - 1 < 0 ? 0 : (byte)(speedY - 1);
            }
            compileData();
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        renderInGameBackground(context);

        context.drawTexture(RenderLayer::getGuiTextured, texture, this.width/2 - 81, this.height/2 - 36, 0, 0, 146, 72, 256, 256);

        if(this.isActive){
            if (mouseX > this.width / 2 - 77 && mouseX < this.width / 2 - 66 && mouseY > this.height / 2 + 6 && mouseY < this.height / 2 + 20) {
                context.drawTexture(RenderLayer::getGuiTextured, texture, this.width / 2 - 77, this.height / 2 + 6, 77, 72, 11, 15, 256, 256);
            }
            else {
                context.drawTexture(RenderLayer::getGuiTextured, texture, this.width / 2 - 77, this.height / 2 + 6, 66, 72, 11, 15, 256, 256);
            }

            if(flashlight){
                renderButton(texture, context, this.width / 2 - 77, this.height / 2 - 28, 98, 72, 98, 80, 10, 8, 256, 256, mouseX, mouseY);
            }

            if(action){
                renderButton(texture, context, this.width / 2 - 77, this.height / 2 - 19, 108, 72, 108, 80, 10, 8, 256, 256, mouseX, mouseY);
            }

            if(nightvision == 1){
                renderButton(texture, context, this.width / 2 - 77, this.height / 2 - 10, 118, 72, 118, 80, 10, 8, 256, 256, mouseX, mouseY);
            }
            else if(nightvision == 2){
                renderButton(texture, context, this.width / 2 - 77, this.height / 2 - 10, 128, 72, 128, 80, 10, 8, 256, 256, mouseX, mouseY);
            }

            for(int i = 0; i < 3; i++)
            {
                int u = i*11;
                int v = modeX == i ? 72 : 94;
                int xOffset = i*13;
                    renderButton(texture, context, this.width / 2 - 47 + xOffset, this.height / 2 - 8, u, v, u, 83, 11, 11, 256, 256, mouseX, mouseY);
            }
            for(int i = 0; i < 3; i++)
            {
                int u = i > 0 ? 22 + i*11 : 0;
                int v = modeY == i ? 72 : 94;
                int xOffset = i*13;
                renderButton(texture, context, this.width / 2 + 10 + xOffset, this.height / 2 - 8, u, v, u, 83, 11, 11, 256, 256, mouseX, mouseY);
            }

            if(modeX == 0){
                int x = this.width / 2 - 2 + (int)xSlider;
                renderButton(texture, context, x, this.height / 2 + 9, 88 ,72, 93, 72, 5, 7, 256, 256, mouseX, mouseY);
            }
            else {
                int x = this.width / 2 - 2;
                int x1 = x + (int)xRange0;
                int x2 = x + (int)xRange1;
                renderButton(texture, context, x1, this.height / 2 + 9, 88 ,72, 93, 72, 5, 7, 256, 256, mouseX, mouseY);
                renderButton(texture, context, x2, this.height / 2 + 9, 88 ,72, 93, 72, 5, 7, 256, 256, mouseX, mouseY);

                context.fill(x - 44, this.height / 2 + 9, x1, this.height / 2 + 16, 0xFF555555);
                context.fill(x2 + 5, this.height / 2 + 9, x + 48, this.height / 2 + 16, 0xFF555555);
            }

            if(modeY == 0){
                int x = this.width / 2 - 2 + (int)ySlider;
                renderButton(texture, context, x, this.height / 2 + 21, 88 ,72, 93, 72, 5, 7, 256, 256, mouseX, mouseY);
            }
            else {
                int x = this.width / 2 - 2;
                int x1 = x + (int)yRange0;
                int x2 = x + (int)yRange1;
                renderButton(texture, context, x1, this.height / 2 + 21, 88 ,72, 93, 72, 5, 7, 256, 256, mouseX, mouseY);
                renderButton(texture, context, x2, this.height / 2 + 21, 88 ,72, 93, 72, 5, 7, 256, 256, mouseX, mouseY);

                context.fill(x - 44, this.height / 2 + 21, x1, this.height / 2 + 28, 0xFF555555);
                context.fill(x2 + 5, this.height / 2 + 21, x + 48, this.height / 2 + 28, 0xFF555555);
            }

            //context.drawTexture(RenderLayer::getGuiTextured, texture, this.width/2 - 47, this.height/2 - 8, 0, 72, 11, 11, 256, 256);

            if(action) {
                renderButton(texture, context, this.width / 2 - 87, this.height / 2 - 18, 146, 63, 152, 63, 6, 7, 256, 256, mouseX, mouseY);
            }
        }
        else {
            if (mouseX > this.width / 2 - 77 && mouseX < this.width / 2 - 66 && mouseY > this.height / 2 + 6 && mouseY < this.height / 2 + 20) {
                context.drawTexture(RenderLayer::getGuiTextured, texture, this.width / 2 - 77, this.height / 2 + 6, 55, 72, 11, 15, 256, 256);
            }
        }

        context.drawTexture(RenderLayer::getGuiTextured, texture, this.width / 2 - 46, this.height / 2 - 29, 154, 0, 94, 16, 256, 256);

        context.drawTexture(RenderLayer::getGuiTextured, texture, this.width/2 + 51, this.height / 2 + 9, 146, speedX * 7, 7, 7, 256, 256);
        context.drawTexture(RenderLayer::getGuiTextured, texture, this.width/2 + 51, this.height / 2 + 21, 146, speedY * 7, 7, 7, 256, 256);

        String key = "§l" + MinecraftClient.getInstance().options.sneakKey.getBoundKeyLocalizedText().getString();
        Text tooltips = Text.translatable("fnafur.screens.camera_edit.tooltip", key);
        Text renameAction = Text.translatable("fnafur.screens.camera_edit.renameAction", key);

        if(renameActionButton){
            renderInGameBackground(context);
            context.drawCenteredTextWithShadow(textRenderer, renameAction, this.width/2, this.height / 2 - 32, 0xFFFFFFFF);

            int width = textRenderer.getWidth(this.actionName.getText() + 10);
            int height = 30;
            int x = this.width / 2 - width/2;
            int y = this.height / 2 - height/2;
            context.fill(x - 1, y - 1, x + width + 1, y + height + 1, 0xFFFFFFFF);
            context.fill(x, y, x + width, y + height, 0xFF666666);
            context.drawCenteredTextWithShadow(textRenderer, this.actionName.getText(), this.width / 2, this.height / 2 - 4, 0xFFFFFFFF);
            this.actionName.render(context, mouseX, mouseY, delta);
        }
        else {
            context.drawCenteredTextWithShadow(textRenderer, tooltips, this.width/2, this.height / 2 + 42, 0xFFFFFFFF);

            this.nameField.render(context, mouseX, mouseY, delta);
        }

        drawTooltip(context, mouseX, mouseY);super.render(context, mouseX, mouseY, delta);
    }

    void drawTooltip(DrawContext context, int mouseX, int mouseY){
        TextRenderer render = MinecraftClient.getInstance().textRenderer;

        boolean speedBL1 = isOnButton(mouseX, mouseY, this.width/2 + 51, this.height / 2 + 9, 7, 7);
        boolean speedBL2 = isOnButton(mouseX, mouseY, this.width/2 + 51, this.height / 2 + 21, 7, 7);

        boolean sliderBL1 = isOnButton(mouseX, mouseY, this.width/2 - 46, this.height / 2 + 9, 92, 7);
        boolean sliderBL2 = isOnButton(mouseX, mouseY, this.width/2 - 46, this.height / 2 + 21, 92, 7);

        boolean mode0BL1 = isOnButton(mouseX, mouseY, this.width / 2 - 47, this.height / 2 - 8, 11, 11);
        boolean mode1BL1 = isOnButton(mouseX, mouseY, this.width / 2 - 34, this.height / 2 - 8, 11, 11);
        boolean mode2BL1 = isOnButton(mouseX, mouseY, this.width / 2 - 21, this.height / 2 - 8, 11, 11);
        boolean mode0BL2 = isOnButton(mouseX, mouseY, this.width / 2 + 10, this.height / 2 - 8, 11, 11);
        boolean mode1BL2 = isOnButton(mouseX, mouseY, this.width / 2 + 23, this.height / 2 - 8, 11, 11);
        boolean mode2BL2 = isOnButton(mouseX, mouseY, this.width / 2 + 36, this.height / 2 - 8, 11, 11);

        boolean nameBL = this.nameField.isMouseOver(mouseX, mouseY);

        boolean flashlightBL = isOnButton(mouseX, mouseY, this.width / 2 - 77, this.height / 2 - 28, 10, 8);
        boolean actionBL = isOnButton(mouseX, mouseY, this.width / 2 - 77, this.height / 2 - 19, 10, 8);
        boolean nightvisionBL = isOnButton(mouseX, mouseY, this.width / 2 - 77, this.height / 2 - 10, 10, 8);
        boolean powerBL = isOnButton(mouseX, mouseY, this.width / 2 - 77, this.height / 2 + 6, 11, 14);

        enum TitleArg{
            NONE,
            X,
            Y
        }
        String argTitle;
        TitleArg titleArg = TitleArg.NONE;
        enum Buttons{
            SPEED,
            SLIDER,
            RANGE,
            MODE0,
            MODE1,
            MODE2,
            NAME,
            POWER,
            FLASHLIGHT,
            ACTION,
            NIGHT_VISION

        }
        String button = "";
        Buttons buttons = Buttons.POWER;

        if(speedBL1 || speedBL2) buttons = Buttons.SPEED;
        if(sliderBL1) buttons = modeX == 0 ? Buttons.SLIDER : Buttons.RANGE;
        if(sliderBL2) buttons = modeY == 0 ? Buttons.SLIDER : Buttons.RANGE;
        if(mode0BL1 || mode0BL2) buttons = Buttons.MODE0;
        if(mode1BL1 || mode1BL2) buttons = Buttons.MODE1;
        if(mode2BL1 || mode2BL2) buttons = Buttons.MODE2;
        if(nameBL) buttons = Buttons.NAME;
        if(flashlightBL) buttons = Buttons.FLASHLIGHT;
        if(actionBL) buttons = Buttons.ACTION;
        if(nightvisionBL) buttons = Buttons.NIGHT_VISION;
        if(powerBL) buttons = Buttons.POWER;

        int descriptionLength = 0;
        switch(buttons){
            case SPEED: {
                button = "speed";
                descriptionLength = 2;
                titleArg = speedBL1 ? TitleArg.X : TitleArg.Y;
                break;
            }
            case SLIDER: {
                button = "rotation";
                descriptionLength = sliderBL1 ? 1 : 2;
                titleArg = sliderBL1 ? TitleArg.X : TitleArg.Y;
                break;
            }
            case RANGE: {
                button = "range";
                descriptionLength = sliderBL1 ? 2 : 3;
                titleArg = sliderBL1 ? TitleArg.X : TitleArg.Y;
                break;
            }
            case MODE0: {
                button = "mode0";
                descriptionLength = 1;
                titleArg = mode0BL1 ? TitleArg.X : TitleArg.Y;
                break;
            }
            case MODE1: {
                button = "mode1";
                descriptionLength = 2;
                titleArg = mode1BL1 ? TitleArg.X : TitleArg.Y;
                break;
            }
            case MODE2: {
                button = "mode2";
                descriptionLength = 2;
                titleArg = mode2BL1 ? TitleArg.X : TitleArg.Y;
                break;
            }
            case NAME: {
                button = "name";
                descriptionLength = 1;
                break;
            }
            case FLASHLIGHT: {
                button = "flashlight";
                descriptionLength = 1;
                break;
            }
            case ACTION: {
                button = "action";
                descriptionLength = 2;
                break;
            }
            case NIGHT_VISION: {
                button = "nightvision";
                descriptionLength = 3;
                break;
            }
            case POWER: {
                button = "power";
                descriptionLength = 2;
                break;
            }
        }
        switch(titleArg){
            default -> argTitle = "";
            case X -> argTitle = "§lX";
            case Y -> argTitle = "§lY";
        }

        Text name = Text.translatable("fnafur.screens.camera_edit." + button + ".title", argTitle);
        String desc = "fnafur.screens.camera_edit." + button + ".description";

        if(this.sneaking && (speedBL1 || speedBL2 || sliderBL1 || sliderBL2 || mode0BL1 || mode0BL2 || mode1BL1 || mode1BL2 || mode2BL1 || mode2BL2 || nameBL || flashlightBL || actionBL || nightvisionBL || powerBL)) {
            List<Text> tooltip = new ArrayList<>();
            tooltip.add(name);
            for(int i = 0; i < descriptionLength; i++){
                tooltip.add(Text.translatable(desc + i));
            }
            context.drawTooltip(render, tooltip, mouseX, mouseY);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.sneaking = MinecraftClient.getInstance().options.sneakKey.matchesKey(keyCode, scanCode);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if(sneaking && MinecraftClient.getInstance().options.sneakKey.matchesKey(keyCode, scanCode)){
            this.sneaking = false;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
