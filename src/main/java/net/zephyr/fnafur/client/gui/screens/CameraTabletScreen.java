package net.zephyr.fnafur.client.gui.screens;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.GoopyBlockEntity;
import net.zephyr.fnafur.blocks.camera.CameraBlock;
import net.zephyr.fnafur.blocks.camera.CameraBlockEntity;
import net.zephyr.fnafur.client.gui.TabOverlayClass;
import net.zephyr.fnafur.init.SoundsInit;
import net.zephyr.fnafur.util.CameraMapUiDrawer;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.fnafur.util.mixinAccessing.IPostProcessorLoader;

import java.util.ArrayList;
import java.util.List;

public class CameraTabletScreen extends GoopyScreen {
    boolean closing = false;
    Identifier overlay = Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/camera/camera_overlay.png");
    Identifier normalShader = Identifier.ofVanilla("post_effects/camera.json");
    Identifier nvShader = Identifier.ofVanilla("post_effects/camera_nightvision.json");
    int Static = 0;
    long currentCam = 0;
    int curCamIndex = 0;
    List<Long> cams;
    int mapMultiplier = 1;
    float transition = 4;
    int mapCornerPosX, mapCornerPosY, mapEndPosX, mapEndPosY, mapWidth, mapHeight;
    boolean holding = false;
    boolean nightVision = false;
    boolean enableNightVision = false;
    boolean allowNightVision = false;
    BlockPos minPos, maxPos;
    boolean f = false;
    boolean b = false;
    boolean l = false;
    boolean r = false;
    float mapAlpha = 100;
    float sliderAlphaX = 100;
    float sliderAlphaY = 100;
    private float doubleClick = 0;

    CameraMapUiDrawer mapDrawer;

    public CameraTabletScreen(Text text, NbtCompound nbtCompound, Object o) {
        super(text, nbtCompound, o);
    }

    @Override
    protected void init() {

        mapDrawer = new CameraMapUiDrawer();
        this.closing = false;
        NbtCompound data = getNbtData();
        holding = false;
        mapCornerPosX = this.width - this.width/2;
        mapCornerPosY = this.height - (this.height/2 + this.height/12);
        mapEndPosX = this.width - this.width/18;
        mapEndPosY = this.height - this.height/8;
        minPos = BlockPos.fromLong(data.getLong("mapMinCorner"));
        maxPos = BlockPos.fromLong(data.getLong("mapMaxCorner"));

        cams = new ArrayList<>();
        long[] camsData = getNbtData().getLongArray("Cameras");
        for (long cam : camsData) {
            cams.add(cam);
        }
        curCamIndex = getNbtData().getInt("currentCam");
        currentCam = !cams.isEmpty() && getNbtData().getInt("currentCam") < cams.size() ? cams.get(curCamIndex) : 0;
        this.enableNightVision = false;
        this.allowNightVision = false;
        this.doubleClick = 0;


        if (data.getBoolean("closing")) {
            data.putBoolean("closing", false);
        }
        compileData(data);
        this.transition = 4;

        this.updateNightVision();

        Identifier shader = nightVision ? nvShader : normalShader;
        MinecraftClient.getInstance().gameRenderer.setPostProcessor(Identifier.ofVanilla("creeper"));
        super.init();
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        doubleClick = doubleClick - delta > 0 ? doubleClick - delta : 0;

        BlockEntity ent = MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam));
        boolean bl = ent != null;
        NbtCompound nbt = bl ? ((IEntityDataSaver)ent).getPersistentData().copy() : new NbtCompound();
        boolean Active = nbt.getBoolean("Active");
        this.allowNightVision = nbt.getByte("NightVision") == 2;
        updateNightVision();

        boolean hasNoSignal = currentCam == 0 || !Active || !bl;
        if(hasNoSignal) context.fill(0, 0, this.width, this.height, 0xFF000000);

        Identifier staticTexture = Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/static/" + this.Static + ".png");
        float alpha = 0.15f + (0.85f * (transition / 4f));
        drawRecolorableTexture(context, staticTexture, 0, 0, 0, this.width, this.height, 0, 0, this.width, this.height, 1, 1, 1, alpha);

        if(!hasNoSignal) {
            drawActionButton(context, mouseX, mouseY);
        }
        if(bl){
            mapDrawer.drawMap(context, mouseX, mouseY, delta * 48, getNbtData(), mapEndPosX, mapEndPosY, mapCornerPosX, mapCornerPosY, mapAlpha, true, allowNightVision && enableNightVision, currentCam);
            mapWidth = mapDrawer.mapWidth;
            mapHeight = mapDrawer.mapHeight;
            mapMultiplier = mapDrawer.mapMultiplier;
            mapAlpha = mapDrawer.mapAlpha;
            //drawMap(context, mouseX, mouseY, delta * 48);
        }

        if(hasNoSignal) {
            Text noVisual = bl ? Text.translatable("fnafur.screens.camera_tablet.no_visual") : Text.translatable("fnafur.screens.camera_tablet.no_cams");
            Text audioOnly = bl ? Text.translatable("fnafur.screens.camera_tablet.audio_only") : Text.translatable("fnafur.screens.camera_tablet.no_cams_desc");

            drawResizableText(context, this.textRenderer, noVisual, 2f, this.width/2f, 16, 0xFFFFFFFF, 0, false, true);
            drawResizableText(context, this.textRenderer, audioOnly, 1.5f, this.width/2f, 36, 0xFFFFFFFF, 0, false, true);
        }

        if(nbt.getByte("ModeX") == 2)
            drawSlider(context, mouseX, mouseY, this.width / 4, 20, this.width / 2, 2, delta);
        if(nbt.getByte("ModeY") == 2)
            drawSlider(context, mouseX, mouseY, 25, this.height / 4, 2, this.height / 2, delta);

        String Hour = TabOverlayClass.renderClock()[0];
        String Day = TabOverlayClass.renderClock()[1];

        drawResizableText(context, this.textRenderer, Text.literal(Hour), 2, this.width - (this.textRenderer.getWidth(Hour) * 2) - 16, 18, 0x64FFFFFF, 0x00000000, false, false);
        drawResizableText(context, this.textRenderer, Text.literal(Day), 1, this.width - (this.textRenderer.getWidth(Day)) - 16, 36, 0x64FFFFFF, 0x00000000, false, false);



        drawRecolorableTexture(context, overlay, 0, 0, 0, this.width, this.height, 0, 0, this.width, this.height, 1, 1, 1, 1);

        super.render(context, mouseX, mouseY, delta);
    }

    void drawSlider(DrawContext context, int mouseX, int mouseY, int x, int y, int width, int height, float delta){
        BlockEntity ent = MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam));
        if(ent != null) {
            NbtCompound nbt = ((IEntityDataSaver)ent).getPersistentData();

            int sliderWidth = 12;
            int sliderHeight = 24;

            boolean hoverX = isOnButton(mouseX, mouseY, x - sliderWidth / 2, y - sliderHeight / 2, width + sliderWidth, sliderHeight);
            boolean hoverY = isOnButton(mouseX, mouseY, x - sliderHeight / 2, y - sliderWidth / 2, sliderHeight, height + sliderWidth);

            double minYaw = nbt.getDouble("minYaw");
            double maxYaw = nbt.getDouble("maxYaw");
            double yaw = nbt.getDouble("yaw");

            double minPitch = nbt.getDouble("minPitch");
            double maxPitch = nbt.getDouble("maxPitch");
            double pitch = nbt.getDouble("pitch");

            float sliderCenterX = x - (sliderWidth / 2f) + (width / 2f);
            float sliderCenterY = y - (sliderWidth / 2f) + (height / 2f);

            double sliderX = sliderCenterX + ((yaw + (minYaw / 2) + (maxYaw / 2)) / (maxYaw - minYaw)) * (width);
            double sliderY = sliderCenterY + ((pitch + (minPitch / 2) + (maxPitch / 2)) / (maxPitch - minPitch)) * (height);

            float deltaMultiplier = delta * 48;
            if (width > height) {
                int alphaX = (int) sliderAlphaX;
                int whiteX = ColorHelper.getArgb (alphaX, 255, 255, 255);
                int colorX = hoverX ? ColorHelper.getArgb(alphaX, 150, 150, 150) : ColorHelper.getArgb(alphaX, 100, 100, 100);

                int goalAlphaX = isOnButton(mouseX, mouseY, 0, 0, this.width, (y * 2) + sliderHeight) && width > height ? 255 : 100;
                if(sliderAlphaX > goalAlphaX){
                    this.sliderAlphaX = sliderAlphaX - deltaMultiplier < goalAlphaX ? goalAlphaX : sliderAlphaX - deltaMultiplier;
                }
                if(sliderAlphaX < goalAlphaX){
                    this.sliderAlphaX = sliderAlphaX + deltaMultiplier > goalAlphaX ? goalAlphaX : sliderAlphaX + deltaMultiplier;
                }
                context.fill(x, y, x + width, y + height, whiteX);
                context.fill((int) sliderX - 1, y + (height / 2) - (sliderHeight / 2) - 1, (int) sliderX + sliderWidth + 1, y + (height / 2) + (sliderHeight / 2) + 1, whiteX);
                context.fill((int) sliderX, y + (height / 2) - (sliderHeight / 2), (int) sliderX + sliderWidth, y + (height / 2) + (sliderHeight / 2), colorX);
            } else {
                int alphaY = (int) sliderAlphaY;
                int whiteY = ColorHelper.getArgb (alphaY, 255, 255, 255);
                int colorY = hoverY ? ColorHelper.getArgb(alphaY, 150, 150, 150) : ColorHelper.getArgb(alphaY, 100, 100, 100);

                int goalAlphaY = isOnButton(mouseX, mouseY, 0, 0, (x * 2) + sliderHeight, this.height) && width < height ? 255 : 100;
                if(sliderAlphaY > goalAlphaY){
                    this.sliderAlphaY = sliderAlphaY - deltaMultiplier < goalAlphaY ? goalAlphaY : sliderAlphaY - deltaMultiplier;
                }
                if(sliderAlphaY < goalAlphaY){
                    this.sliderAlphaY = sliderAlphaY + deltaMultiplier > goalAlphaY ? goalAlphaY : sliderAlphaY + deltaMultiplier;
                }
                context.fill(x, y, x + width, y + height, whiteY);
                context.fill(x + (width / 2) - (sliderHeight / 2) - 1, (int) sliderY - 1, x + (width / 2) + (sliderHeight / 2) + 1, (int) sliderY + sliderWidth + 1, whiteY);
                context.fill(x + (width / 2) - (sliderHeight / 2), (int) sliderY, x + (width / 2) + (sliderHeight / 2), (int) sliderY + sliderWidth, colorY);
            }

            if (holding) {
                if(hoverX && width > height){
                    int side = isOnButton(mouseX, mouseY, (int) sliderX, y - sliderHeight / 2, sliderWidth, sliderHeight) ? 0
                            : mouseX > (sliderX + sliderWidth / 2f) ? 1 : -1;

                    double newYaw = yaw + ((((nbt.getByte("yawSpeed") + 1) / 2f) * side) * delta);
                    nbt.putDouble("yaw", newYaw);
                    if (!nbt.isEmpty()) {
                        GoopyNetworkingUtils.saveBlockNbt(getBlockPos(), nbt);
                    }
                }
                if(hoverY && width < height){
                    int side = isOnButton(mouseX, mouseY, x - sliderHeight / 2, (int)sliderY, sliderHeight, sliderWidth) ? 0
                            : mouseY > (sliderY + sliderWidth / 2f) ? 1 : -1;

                    double newPitch = pitch + ((((nbt.getByte("pitchSpeed") + 1)/2f) * side) * delta);
                    nbt.putDouble("pitch", newPitch);
                    if (!nbt.isEmpty()) {
                        GoopyNetworkingUtils.saveBlockNbt(getBlockPos(), nbt);
                    }
                }
            } else if(f || b || l || r){
                int sideX = r ? 1 : l ? -1 : 0;
                int sideY = b ? 1 : f ? -1 : 0;

                double newYaw = yaw + ((((nbt.getByte("yawSpeed") + 1) / 2f) * sideX) * delta);
                double newPitch = pitch + ((((nbt.getByte("pitchSpeed") + 1) / 2f) * sideY) * delta);

                if(newYaw > -maxYaw && newYaw < -minYaw && nbt.getByte("ModeX") == 2) nbt.putDouble("yaw", newYaw);
                if(newPitch > -maxPitch && newPitch < -minPitch && nbt.getByte("ModeY") == 2) nbt.putDouble("pitch", newPitch);
                if (!nbt.isEmpty()) {
                    GoopyNetworkingUtils.saveBlockNbt(getBlockPos(), nbt);

                }
            }
        }
    }

    void drawActionButton(DrawContext context, int mouseX, int mouseY){
        if(MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam)) != null) {
            NbtCompound nbt = ((IEntityDataSaver) MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam))).getPersistentData().copy();
            if (nbt.getBoolean("Action")) {
                int width = textRenderer.getWidth(nbt.getString("ActionName") + 10);
                int height = 30;
                int diff = mapEndPosX + (mapMultiplier * 2) - (mapEndPosX - (mapWidth * mapMultiplier) - mapMultiplier);
                int x = (mapEndPosX - (mapWidth * mapMultiplier) - mapMultiplier) + (diff / 2) - width / 2;
                if (width - 10 > diff) x = mapEndPosX + (mapMultiplier * 2) + 5 - width;
                int y = (mapEndPosY - (mapHeight * mapMultiplier) - mapMultiplier) - 5 - height;
                context.fill(x - 1, y - 1, x + width + 1, y + height + 1, 0xFFFFFFFF);
                boolean bl = isOnButton(mouseX, mouseY, x, y, width, height);
                int color = bl ? holding ? ColorHelper.getArgb(255, 75, 255, 75) : ColorHelper.getArgb(255, 150, 150, 150) : ColorHelper.getArgb(255, 100, 100, 100);
                context.fill(x, y, x + width, y + height, color);
                context.drawCenteredTextWithShadow(textRenderer, nbt.getString("ActionName"), x + width / 2, (y + (height / 2)) - 4, 0xFFFFFFFF);
            }
        }
    }
    void buttonCheck(double mouseX, double mouseY){
        if( MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam)) != null) {
            NbtCompound nbt = ((IEntityDataSaver) MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam))).getPersistentData().copy();

            int width = textRenderer.getWidth(nbt.getString("ActionName") + 10);
            int height = 30;
            int diff = mapEndPosX + (mapMultiplier * 2) - (mapEndPosX - (mapWidth * mapMultiplier) - mapMultiplier);
            int x = (mapEndPosX - (mapWidth * mapMultiplier) - mapMultiplier) + (diff / 2) - width / 2;
            if (width - 10 > diff) x = mapEndPosX + (mapMultiplier * 2) + 5 - width;
            int y = (mapEndPosY - (mapHeight * mapMultiplier) - mapMultiplier) - 5 - height;
            boolean bl = isOnButton(mouseX, mouseY, x, y, width, height);

            setPowered(bl && holding);
        }
    }
    void drawMap(DrawContext context, int mouseX, int mouseY, float delta){
        NbtCompound data = getNbtData();

        boolean bl = data.getList("CamMap", NbtElement.LONG_ARRAY_TYPE).isEmpty();
        if(!bl) {

            int mapMaxWidth = mapEndPosX - mapCornerPosX;
            int mapMaxHeight = mapEndPosY - mapCornerPosY;

            NbtList mapNbt = data.getList("CamMap", NbtElement.LONG_ARRAY_TYPE).copy();

            mapWidth = Math.abs(maxPos.getX() - minPos.getX());
            mapHeight = Math.abs(maxPos.getZ() - minPos.getZ());

            mapMultiplier = 1;
            while((mapWidth * (mapMultiplier+1) <= mapMaxWidth) && (mapHeight * (mapMultiplier+1) <= mapMaxHeight)){
                mapMultiplier++;
            }

            int bg1 = mapEndPosX - (mapWidth * mapMultiplier) - mapMultiplier;
            int bg2 = mapEndPosY - (mapHeight * mapMultiplier) - mapMultiplier;

            int goalAlpha = mouseX > bg1 && mouseX < mapEndPosX && mouseY > bg2 && mouseY < mapEndPosY ?
                    255 : 100;
            if(mapAlpha > goalAlpha){
                this.mapAlpha = mapAlpha - delta < goalAlpha ? goalAlpha : mapAlpha - delta;
            }
            if(mapAlpha < goalAlpha){
                this.mapAlpha = mapAlpha + delta > goalAlpha ? goalAlpha : mapAlpha + delta;
            }
            int alpha = (int) mapAlpha;

            int color = ColorHelper.getArgb(alpha, 255, 255, 255);
            //context.fill(bg1, bg2, mapEndPosX + (mapMultiplier*2), mapEndPosY + (mapMultiplier*2), 0x55000000);

            for(int i = 0; i < mapNbt.size(); i++) {
                if (mapNbt.get(i).getType() == NbtElement.LONG_ARRAY_TYPE) {
                    BlockPos pos1 = BlockPos.fromLong(mapNbt.getLongArray(i)[0]);
                    BlockPos pos2 = BlockPos.fromLong(mapNbt.getLongArray(i)[1]);

                    int x1 = (Math.min(pos1.getX(), pos2.getX())- minPos.getX()) * mapMultiplier;
                    int z1 = (Math.min(pos1.getZ(), pos2.getZ())- minPos.getZ()) * mapMultiplier;
                    int x2 = mapMultiplier + ((Math.max(pos1.getX(), pos2.getX())- minPos.getX()) * mapMultiplier);
                    int z2 = mapMultiplier + ((Math.max(pos1.getZ(), pos2.getZ())- minPos.getZ()) * mapMultiplier);

                    x1 += mapEndPosX - (mapWidth * mapMultiplier);
                    x2 += mapEndPosX - (mapWidth * mapMultiplier);
                    z1 += mapEndPosY - (mapHeight * mapMultiplier);
                    z2 += mapEndPosY - (mapHeight * mapMultiplier);

                    context.fill(x1, z1, x2, z2, color);
                }
            }

            float camScale = 0.25f;
            MatrixStack matrices = context.getMatrices();

            matrices.push();
            matrices.scale(camScale, camScale, camScale);
            for (Long cam : cams) {
                BlockPos pos = BlockPos.fromLong(cam);

                int x = (pos.getX() - minPos.getX()) * mapMultiplier;
                int z = (pos.getZ() - minPos.getZ()) * mapMultiplier;

                x += mapEndPosX - (mapWidth * mapMultiplier);
                z += mapEndPosY - (mapHeight * mapMultiplier);

                boolean bl2 = isOnButton(mouseX, mouseY, x, z, mapMultiplier, mapMultiplier);

                x *= (int)(1 / camScale);
                z *= (int)(1 / camScale);

                if (bl2 && MinecraftClient.getInstance().world != null && MinecraftClient.getInstance().world.getBlockEntity(pos) instanceof CameraBlockEntity) {
                    String name = ((IEntityDataSaver) MinecraftClient.getInstance().world.getBlockEntity(pos)).getPersistentData().getString("Name");
                    matrices.scale(1 / camScale, 1 / camScale, 1 / camScale);
                    context.drawTooltip(MinecraftClient.getInstance().textRenderer, Text.literal(name), mouseX, mouseY);
                    matrices.scale(camScale, camScale, camScale);
                }
                int camColor = bl2 || cam == currentCam ? ColorHelper.getArgb(alpha, 75, 255, 75) : ColorHelper.getArgb(alpha, 100, 100, 100);
                int camOutline = allowNightVision && enableNightVision && cam == currentCam ? ColorHelper.getArgb(alpha, 133, 210, 255) : ColorHelper.getArgb(alpha, 255, 255, 255);
                context.fill(x - (mapMultiplier / 2) * (int)(1 / camScale), z - (mapMultiplier / 2) * (int)(1 / camScale), x + mapMultiplier * (int)(1 / camScale) + ((mapMultiplier / 2) * (int)(1 / camScale)), z + mapMultiplier * (int)(1 / camScale) + ((mapMultiplier / 2) * (int)(1 / camScale)), camOutline);
                context.fill(x - ((mapMultiplier / 4) * (int)(1 / camScale)), z - ((mapMultiplier / 4) * (int)(1 / camScale)), x + mapMultiplier * (int)(1 / camScale) + ((mapMultiplier / 4) * (int)(1 / camScale)), z + mapMultiplier * (int)(1 / camScale) + ((mapMultiplier / 4) * (int)(1 / camScale)), camColor);
            }
            matrices.scale(1 / camScale, 1 / camScale, 1 / camScale);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.holding = true;
        if(this.doubleClick == 0) this.doubleClick = 5f;
        buttonCheck(mouseX, mouseY);
        changeCam(mouseX, mouseY);

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        changeCam(mouseX, mouseY);
        buttonCheck(mouseX, mouseY);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.holding = false;
        buttonCheck(mouseX, mouseY);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    void changeCam(double mouseX, double mouseY) {
        for (Long cam : cams) {
            BlockPos pos = BlockPos.fromLong(cam);
            NbtCompound nbt = getNbtData();

            int x = (pos.getX() - minPos.getX()) * mapMultiplier;
            int z = (pos.getZ() - minPos.getZ()) * mapMultiplier;

            x += mapEndPosX - (mapWidth * mapMultiplier);
            z += mapEndPosY - (mapHeight * mapMultiplier);

            boolean bl2 = isOnButton(mouseX, mouseY, x, z, mapMultiplier, mapMultiplier) && cam != currentCam;
            boolean bl3 = isOnButton(mouseX, mouseY, x, z, mapMultiplier, mapMultiplier) && cam == currentCam
                    && doubleClick > 0 && doubleClick != 5 && this.allowNightVision;

            if(bl3){
                doubleClick = 0;
                this.enableNightVision = !this.enableNightVision;
                this.transition = 4;
                MinecraftClient.getInstance().player.playSound(SoundsInit.CAM_SWITCH, 1, 1);
                SoundEvent sound = enableNightVision ? SoundsInit.CAM_NV_ON : SoundsInit.CAM_NV_OFF;
                MinecraftClient.getInstance().player.playSound(sound, 1, 1);
            }

            if (bl2) {
                currentCam = cam;
                curCamIndex = cams.indexOf(currentCam);
                nbt.putInt("currentCam", curCamIndex);
                compileData(nbt);
                this.transition = 4;
                doubleClick = 0;
                MinecraftClient.getInstance().player.playSound(SoundsInit.CAM_SWITCH, 1, 1);
            }

            updateNightVision();


            if(bl2 || bl3){
                Identifier shader = nightVision ? nvShader : normalShader;
                MinecraftClient.getInstance().gameRenderer.setPostProcessor(Identifier.ofVanilla("creeper"));
            }
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(MinecraftClient.getInstance().options.jumpKey.matchesKey(keyCode, scanCode)){
            setLight(currentCam, !closing);
        }
        if(MinecraftClient.getInstance().options.forwardKey.matchesKey(keyCode, scanCode)) {
            this.f = true;
        }
        if(MinecraftClient.getInstance().options.backKey.matchesKey(keyCode, scanCode)) {
            this.b = true;
        }
        if(MinecraftClient.getInstance().options.leftKey.matchesKey(keyCode, scanCode)) {
            this.l = true;
        }
        if(MinecraftClient.getInstance().options.rightKey.matchesKey(keyCode, scanCode)) {
            this.r = true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if(MinecraftClient.getInstance().options.jumpKey.matchesKey(keyCode, scanCode)){
            setLight(currentCam, false);
        }
        if(MinecraftClient.getInstance().options.forwardKey.matchesKey(keyCode, scanCode)) {
            this.f = false;
        }
        if(MinecraftClient.getInstance().options.backKey.matchesKey(keyCode, scanCode)) {
            this.b = false;
        }
        if(MinecraftClient.getInstance().options.leftKey.matchesKey(keyCode, scanCode)) {
            this.l = false;
        }
        if(MinecraftClient.getInstance().options.rightKey.matchesKey(keyCode, scanCode)) {
            this.r = false;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    public void updateNightVision(){
        BlockEntity ent = MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam));
        boolean bl = ent instanceof GoopyBlockEntity;
        NbtCompound nbt = bl ? ((IEntityDataSaver)ent).getPersistentData().copy() : new NbtCompound();
        this.nightVision = bl && nbt.getByte("NightVision") == 1 || (nbt.getByte("NightVision") == 2 && this.enableNightVision);
    }

    @Override
    public void tick() {
        if(!closing) {
            if(MinecraftClient.getInstance().player.hurtTime > 0) close();
            for (long cam : cams) {
                setUsed(cam, cam == currentCam && !closing);
            }
            if (!cams.isEmpty() && !cams.contains(currentCam)) {
                currentCam = cams.get(0);
            } else if (cams.isEmpty()) {
                currentCam = 0;
            }
            this.Static = this.Static + 1 >= 4 ? 0 : this.Static + 1;
            this.transition = Math.max(this.transition - 1, 0);
        }

        if(!cams.isEmpty() && (!cams.contains(currentCam) || !(client.world.getBlockEntity(BlockPos.fromLong(currentCam)) instanceof CameraBlockEntity))){
            cams.remove(currentCam);
            currentCam = !cams.isEmpty() ? cams.get(0) : 0;
        }
        super.tick();
    }

    public void setAsUsed(boolean used){
        NbtCompound nbt = getNbtData();
        nbt.putBoolean("used", used);
        compileData(nbt);
    }
    public void setPowered(boolean power) {
        if (MinecraftClient.getInstance().world != null && MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam)) != null) {
            NbtCompound nbt = ((IEntityDataSaver) MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam))).getPersistentData().copy();
            if (nbt.getBoolean("Action") && nbt.getBoolean("Active")) {
                if (nbt.getBoolean("Powered") != power) {
                    nbt.putBoolean("Powered", power);
                    GoopyNetworkingUtils.saveBlockNbt(BlockPos.fromLong(currentCam), nbt);
                }
            } else {
                if (nbt.getBoolean("Powered")) {
                    nbt.putBoolean("Powered", false);
                    GoopyNetworkingUtils.saveBlockNbt(BlockPos.fromLong(currentCam), nbt);
                }
            }
        }
    }
    private void compileData(NbtCompound nbt){
        GoopyNetworkingUtils.saveItemNbt(getItemSlot(), nbt);
    }

    void setLight(long pos, boolean value) {
        if(MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(pos)) instanceof CameraBlockEntity) {
            NbtCompound nbt = ((IEntityDataSaver) MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(pos))).getPersistentData().copy();
            if (nbt.getBoolean("Flashlight")) {
                if (nbt.getBoolean("Lit") != value) {
                    nbt.putBoolean("Lit", value);
                    GoopyNetworkingUtils.saveBlockNbt(BlockPos.fromLong(pos), nbt);
                }
            } else {
                if (nbt.getBoolean("Lit")) {
                    nbt.putBoolean("Lit", false);
                    GoopyNetworkingUtils.saveBlockNbt(BlockPos.fromLong(pos), nbt);
                }
            }
        }
    }
    void setUsed(long pos, boolean value){
        if(MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(pos)) instanceof CameraBlockEntity) {
            NbtCompound nbt = ((IEntityDataSaver) MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(pos))).getPersistentData().copy();
            if (nbt.getBoolean("isUsed") != value) {
                nbt.putBoolean("isUsed", value);
                GoopyNetworkingUtils.saveBlockNbt(BlockPos.fromLong(pos), nbt);
            }
        }
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().gameRenderer.clearPostProcessor();
        this.closing = true;

        NbtCompound nbt = getNbtData();
        nbt.putBoolean("closing", true);
        nbt.putBoolean("used", false);
        compileData(nbt);
        MinecraftClient.getInstance().player.playSound(SoundsInit.CAM_CLOSE, 1, 1);
        MinecraftClient.getInstance().options.hudHidden = false;
        super.close();
        for(long cam : cams){
            setUsed(cam, false);
            setLight(cam, false);
        }
    }

    public Vec3d camPos(){
        if(MinecraftClient.getInstance().world != null) {
            BlockEntity entity = MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam));
            if (entity instanceof CameraBlockEntity) {
                BlockPos pos = BlockPos.fromLong(currentCam);
                Vec3d offset;
                float amount = -0.1f;
                switch (MinecraftClient.getInstance().world.getBlockState(pos).get(CameraBlock.FACING)) {
                    default -> offset = new Vec3d(0, 0, amount);
                    case SOUTH -> offset = new Vec3d(0, 0, -amount);
                    case EAST -> offset = new Vec3d(-amount, 0, 0);
                    case WEST -> offset = new Vec3d(amount, 0, 0);
                }

                return new Vec3d(pos.getX() + 0.5f, pos.getY() + 0.7f, pos.getZ() + 0.5f).add(offset);
            }
        }
        return MinecraftClient.getInstance().cameraEntity != null ? MinecraftClient.getInstance().cameraEntity.getPos() : Vec3d.ZERO;
    }
    public float getPitch(){
        if(MinecraftClient.getInstance().world != null) {
            BlockEntity entity = MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam));
            if (entity instanceof CameraBlockEntity) {
                NbtCompound nbt = ((IEntityDataSaver) entity).getPersistentData().copy();
                return nbt.getFloat("pitch");
            }
        }
        return MinecraftClient.getInstance().cameraEntity != null ? MinecraftClient.getInstance().cameraEntity.getPitch() : 0;
}
    public float getYaw(){
        if(MinecraftClient.getInstance().world != null) {
            BlockEntity entity = MinecraftClient.getInstance().world.getBlockEntity(BlockPos.fromLong(currentCam));
            if (entity instanceof CameraBlockEntity) {
                NbtCompound nbt = ((IEntityDataSaver) entity).getPersistentData().copy();
                BlockPos pos = BlockPos.fromLong(currentCam);
                World world = MinecraftClient.getInstance().world;
                return nbt.getFloat("yaw") + world.getBlockState(pos).get(CameraBlock.FACING).getPositiveHorizontalDegrees();
            }
        }
        return MinecraftClient.getInstance().cameraEntity != null ? MinecraftClient.getInstance().cameraEntity.getYaw() : 0;
    }

    public boolean nightVision(){
        return this.nightVision;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
