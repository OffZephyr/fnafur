package net.zephyr.fnafur.client.gui.screens;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.client.gui.TabOverlayClass;
import net.zephyr.fnafur.init.SoundsInit;
import net.zephyr.fnafur.util.CameraMapUiDrawer;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.joml.Matrix3x2fStack;

import java.util.ArrayList;
import java.util.List;

public class CameraTabletScreen extends GoopyScreen {
    boolean closing = false;
    Identifier overlay = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/camera/camera_overlay.png");
    Identifier normalShader = Identifier.withDefaultNamespace("post_effects/camera.json");
    Identifier nvShader = Identifier.withDefaultNamespace("post_effects/camera_nightvision.json");
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

    public CameraTabletScreen(Component text, CompoundTag CompoundTag, Object o) {
        super(text, CompoundTag, o);
    }

    @Override
    protected void init() {

        mapDrawer = new CameraMapUiDrawer();
        this.closing = false;
        CompoundTag data = getNbtData();
        holding = false;
        mapCornerPosX = this.width - this.width/2;
        mapCornerPosY = this.height - (this.height/2 + this.height/12);
        mapEndPosX = this.width - this.width/18;
        mapEndPosY = this.height - this.height/8;
        minPos = BlockPos.of(data.getLong("mapMinCorner").get());
        maxPos = BlockPos.of(data.getLong("mapMaxCorner").get());

        cams = new ArrayList<>();
        long[] camsData = getNbtData().getLongArray("Cameras").get();
        for (long cam : camsData) {
            cams.add(cam);
        }
        curCamIndex = getNbtData().getInt("currentCam").get();
        currentCam = !cams.isEmpty() && getNbtData().getInt("currentCam").get() < cams.size() ? cams.get(curCamIndex) : 0;
        this.enableNightVision = false;
        this.allowNightVision = false;
        this.doubleClick = 0;


        if (data.getBoolean("closing").get()) {
            data.putBoolean("closing", false);
        }
        compileData(data);
        this.transition = 4;

        this.updateNightVision();

        Identifier shader = nightVision ? nvShader : normalShader;
        Minecraft.getInstance().gameRenderer.setPostEffect(Identifier.withDefaultNamespace("creeper"));
        super.init();
    }
    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {

        doubleClick = doubleClick - delta > 0 ? doubleClick - delta : 0;

        BlockEntity ent = Minecraft.getInstance().level.getBlockEntity(BlockPos.of(currentCam));
        boolean bl = ent != null;
        CompoundTag nbt = bl ? ((IEntityDataSaver)ent).getPersistentData().copy() : new CompoundTag();
        boolean Active = nbt.getBoolean("Active").get();
        this.allowNightVision = nbt.getByte("NightVision").get() == 2;
        updateNightVision();

        boolean hasNoSignal = currentCam == 0 || !Active || !bl;
        if(hasNoSignal) context.fill(0, 0, this.width, this.height, 0xFF000000);

        Identifier staticTexture = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/gui/static/" + this.Static + ".png");
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
            Component noVisual = bl ? Component.translatable("fnafur.screens.camera_tablet.no_visual") : Component.translatable("fnafur.screens.camera_tablet.no_cams");
            Component audioOnly = bl ? Component.translatable("fnafur.screens.camera_tablet.audio_only") : Component.translatable("fnafur.screens.camera_tablet.no_cams_desc");

            drawResizableText(context, this.font, noVisual, 2f, this.width/2f, 16, 0xFFFFFFFF, 0, false, true);
            drawResizableText(context, this.font, audioOnly, 1.5f, this.width/2f, 36, 0xFFFFFFFF, 0, false, true);
        }

        if(nbt.getByte("ModeX").get() == 2)
            drawSlider(context, mouseX, mouseY, this.width / 4, 20, this.width / 2, 2, delta);
        if(nbt.getByte("ModeY").get() == 2)
            drawSlider(context, mouseX, mouseY, 25, this.height / 4, 2, this.height / 2, delta);

        String Hour = TabOverlayClass.renderClock()[0];
        String Day = TabOverlayClass.renderClock()[1];

        drawResizableText(context, this.font, Component.literal(Hour), 2, this.width - (this.font.width(Hour) * 2) - 16, 18, 0x64FFFFFF, 0x00000000, false, false);
        drawResizableText(context, this.font, Component.literal(Day), 1, this.width - (this.font.width(Day)) - 16, 36, 0x64FFFFFF, 0x00000000, false, false);



        drawRecolorableTexture(context, overlay, 0, 0, 0, this.width, this.height, 0, 0, this.width, this.height, 1, 1, 1, 1);

        super.extractRenderState(context, mouseX, mouseY, delta);
    }

    void drawSlider(GuiGraphicsExtractor context, int mouseX, int mouseY, int x, int y, int width, int height, float delta){
        BlockEntity ent = Minecraft.getInstance().level.getBlockEntity(BlockPos.of(currentCam));
        if(ent != null) {
            CompoundTag nbt = ((IEntityDataSaver)ent).getPersistentData();

            int sliderWidth = 12;
            int sliderHeight = 24;

            boolean hoverX = isOnButton(mouseX, mouseY, x - sliderWidth / 2, y - sliderHeight / 2, width + sliderWidth, sliderHeight);
            boolean hoverY = isOnButton(mouseX, mouseY, x - sliderHeight / 2, y - sliderWidth / 2, sliderHeight, height + sliderWidth);

            double minYaw = nbt.getDouble("minYaw").get();
            double maxYaw = nbt.getDouble("maxYaw").get();
            double yaw = nbt.getDouble("yaw").get();

            double minPitch = nbt.getDouble("minPitch").get();
            double maxPitch = nbt.getDouble("maxPitch").get();
            double pitch = nbt.getDouble("pitch").get();

            float sliderCenterX = x - (sliderWidth / 2f) + (width / 2f);
            float sliderCenterY = y - (sliderWidth / 2f) + (height / 2f);

            double sliderX = sliderCenterX + ((yaw + (minYaw / 2) + (maxYaw / 2)) / (maxYaw - minYaw)) * (width);
            double sliderY = sliderCenterY + ((pitch + (minPitch / 2) + (maxPitch / 2)) / (maxPitch - minPitch)) * (height);

            float deltaMultiplier = delta * 48;
            if (width > height) {
                int alphaX = (int) sliderAlphaX;
                int whiteX = ARGB.color(alphaX, 255, 255, 255);
                int colorX = hoverX ? ARGB.color(alphaX, 150, 150, 150) : ARGB.color(alphaX, 100, 100, 100);

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
                int whiteY = ARGB.color(alphaY, 255, 255, 255);
                int colorY = hoverY ? ARGB.color(alphaY, 150, 150, 150) : ARGB.color(alphaY, 100, 100, 100);

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

                    double newYaw = yaw + ((((nbt.getByte("yawSpeed").get() + 1) / 2f) * side) * delta);
                    nbt.putDouble("yaw", newYaw);
                    if (!nbt.isEmpty()) {
                        GoopyNetworkingUtils.saveBlockNbt(getBlockPos(), nbt);
                    }
                }
                if(hoverY && width < height){
                    int side = isOnButton(mouseX, mouseY, x - sliderHeight / 2, (int)sliderY, sliderHeight, sliderWidth) ? 0
                            : mouseY > (sliderY + sliderWidth / 2f) ? 1 : -1;

                    double newPitch = pitch + ((((nbt.getByte("pitchSpeed").get() + 1)/2f) * side) * delta);
                    nbt.putDouble("pitch", newPitch);
                    if (!nbt.isEmpty()) {
                        GoopyNetworkingUtils.saveBlockNbt(getBlockPos(), nbt);
                    }
                }
            } else if(f || b || l || r){
                int sideX = r ? 1 : l ? -1 : 0;
                int sideY = b ? 1 : f ? -1 : 0;

                double newYaw = yaw + ((((nbt.getByte("yawSpeed").get() + 1) / 2f) * sideX) * delta);
                double newPitch = pitch + ((((nbt.getByte("pitchSpeed").get() + 1) / 2f) * sideY) * delta);

                if(newYaw > -maxYaw && newYaw < -minYaw && nbt.getByte("ModeX").get() == 2) nbt.putDouble("yaw", newYaw);
                if(newPitch > -maxPitch && newPitch < -minPitch && nbt.getByte("ModeY").get() == 2) nbt.putDouble("pitch", newPitch);
                if (!nbt.isEmpty()) {
                    GoopyNetworkingUtils.saveBlockNbt(getBlockPos(), nbt);

                }
            }
        }
    }

    void drawActionButton(GuiGraphicsExtractor context, int mouseX, int mouseY){
        if(Minecraft.getInstance().level.getBlockEntity(BlockPos.of(currentCam)) != null) {
            CompoundTag nbt = ((IEntityDataSaver) Minecraft.getInstance().level.getBlockEntity(BlockPos.of(currentCam))).getPersistentData().copy();
            if (nbt.getBoolean("Action").get()) {
                int width = font.width(nbt.getString("ActionName").get() + 10);
                int height = 30;
                int diff = mapEndPosX + (mapMultiplier * 2) - (mapEndPosX - (mapWidth * mapMultiplier) - mapMultiplier);
                int x = (mapEndPosX - (mapWidth * mapMultiplier) - mapMultiplier) + (diff / 2) - width / 2;
                if (width - 10 > diff) x = mapEndPosX + (mapMultiplier * 2) + 5 - width;
                int y = (mapEndPosY - (mapHeight * mapMultiplier) - mapMultiplier) - 5 - height;
                context.fill(x - 1, y - 1, x + width + 1, y + height + 1, 0xFFFFFFFF);
                boolean bl = isOnButton(mouseX, mouseY, x, y, width, height);
                int color = bl ? holding ? ARGB.color(255, 75, 255, 75) : ARGB.color(255, 150, 150, 150) : ARGB.color(255, 100, 100, 100);
                context.fill(x, y, x + width, y + height, color);
                context.centeredText(font, nbt.getString("ActionName").get(), x + width / 2, (y + (height / 2)) - 4, 0xFFFFFFFF);
            }
        }
    }
    void buttonCheck(double mouseX, double mouseY){
        if( Minecraft.getInstance().level.getBlockEntity(BlockPos.of(currentCam)) != null) {
            CompoundTag nbt = ((IEntityDataSaver) Minecraft.getInstance().level.getBlockEntity(BlockPos.of(currentCam))).getPersistentData().copy();

            int width = font.width(nbt.getString("ActionName").get() + 10);
            int height = 30;
            int diff = mapEndPosX + (mapMultiplier * 2) - (mapEndPosX - (mapWidth * mapMultiplier) - mapMultiplier);
            int x = (mapEndPosX - (mapWidth * mapMultiplier) - mapMultiplier) + (diff / 2) - width / 2;
            if (width - 10 > diff) x = mapEndPosX + (mapMultiplier * 2) + 5 - width;
            int y = (mapEndPosY - (mapHeight * mapMultiplier) - mapMultiplier) - 5 - height;
            boolean bl = isOnButton(mouseX, mouseY, x, y, width, height);

            setPowered(bl && holding);
        }
    }
    void drawMap(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta){
        CompoundTag data = getNbtData();

        boolean bl = data.getList("CamMap").get().isEmpty();
        if(!bl) {

            int mapMaxWidth = mapEndPosX - mapCornerPosX;
            int mapMaxHeight = mapEndPosY - mapCornerPosY;

            ListTag mapNbt = data.getList("CamMap").get();

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

            int color = ARGB.color(alpha, 255, 255, 255);
            //context.fill(bg1, bg2, mapEndPosX + (mapMultiplier*2), mapEndPosY + (mapMultiplier*2), 0x55000000);

            for(int i = 0; i < mapNbt.size(); i++) {
                if (mapNbt.get(i).getId() == Tag.TAG_LONG_ARRAY) {
                    BlockPos pos1 = BlockPos.of(mapNbt.getLongArray(i).get()[0]);
                    BlockPos pos2 = BlockPos.of(mapNbt.getLongArray(i).get()[1]);

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
            Matrix3x2fStack matrices = context.pose();

            matrices.pushMatrix();
            matrices.scale(camScale, camScale);
            for (Long cam : cams) {
                BlockPos pos = BlockPos.of(cam);

                int x = (pos.getX() - minPos.getX()) * mapMultiplier;
                int z = (pos.getZ() - minPos.getZ()) * mapMultiplier;

                x += mapEndPosX - (mapWidth * mapMultiplier);
                z += mapEndPosY - (mapHeight * mapMultiplier);

                boolean bl2 = isOnButton(mouseX, mouseY, x, z, mapMultiplier, mapMultiplier);

                x *= (int)(1 / camScale);
                z *= (int)(1 / camScale);

                if (bl2 && Minecraft.getInstance().level != null && false) {
                    String name = ((IEntityDataSaver) Minecraft.getInstance().level.getBlockEntity(pos)).getPersistentData().getString("Name").get();
                    matrices.scale(1 / camScale, 1 / camScale);
                    context.setTooltipForNextFrame(Minecraft.getInstance().font, Component.literal(name), mouseX, mouseY);
                    matrices.scale(camScale, camScale);
                }
                int camColor = bl2 || cam == currentCam ? ARGB.color(alpha, 75, 255, 75) : ARGB.color(alpha, 100, 100, 100);
                int camOutline = allowNightVision && enableNightVision && cam == currentCam ? ARGB.color(alpha, 133, 210, 255) : ARGB.color(alpha, 255, 255, 255);
                context.fill(x - (mapMultiplier / 2) * (int)(1 / camScale), z - (mapMultiplier / 2) * (int)(1 / camScale), x + mapMultiplier * (int)(1 / camScale) + ((mapMultiplier / 2) * (int)(1 / camScale)), z + mapMultiplier * (int)(1 / camScale) + ((mapMultiplier / 2) * (int)(1 / camScale)), camOutline);
                context.fill(x - ((mapMultiplier / 4) * (int)(1 / camScale)), z - ((mapMultiplier / 4) * (int)(1 / camScale)), x + mapMultiplier * (int)(1 / camScale) + ((mapMultiplier / 4) * (int)(1 / camScale)), z + mapMultiplier * (int)(1 / camScale) + ((mapMultiplier / 4) * (int)(1 / camScale)), camColor);
            }
            matrices.scale(1 / camScale, 1 / camScale);
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        this.holding = true;
        if(this.doubleClick == 0) this.doubleClick = 5f;
        buttonCheck(click.x(), click.y());
        changeCam(click.x(), click.y());
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent click, double offsetX, double offsetY) {

        changeCam(click.x() + offsetX, click.y() + offsetY);
        buttonCheck(click.x() + offsetX, click.y() + offsetY);
        return super.mouseDragged(click, offsetX, offsetY);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent click) {
        this.holding = false;
        buttonCheck(click.x(), click.y());
        return super.mouseReleased(click);
    }

    void changeCam(double mouseX, double mouseY) {
        for (Long cam : cams) {
            BlockPos pos = BlockPos.of(cam);
            CompoundTag nbt = getNbtData();

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
                Minecraft.getInstance().player.playSound(SoundsInit.CAM_SWITCH, 1, 1);
                SoundEvent sound = enableNightVision ? SoundsInit.CAM_NV_ON : SoundsInit.CAM_NV_OFF;
                Minecraft.getInstance().player.playSound(sound, 1, 1);
            }

            if (bl2) {
                currentCam = cam;
                curCamIndex = cams.indexOf(currentCam);
                nbt.putInt("currentCam", curCamIndex);
                compileData(nbt);
                this.transition = 4;
                doubleClick = 0;
                Minecraft.getInstance().player.playSound(SoundsInit.CAM_SWITCH, 1, 1);
            }

            updateNightVision();


            if(bl2 || bl3){
                Identifier shader = nightVision ? nvShader : normalShader;
                Minecraft.getInstance().gameRenderer.setPostEffect(Identifier.withDefaultNamespace("creeper"));
            }
        }
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
        if(Minecraft.getInstance().options.keyJump.matches(input)){
            setLight(currentCam, !closing);
        }
        if(Minecraft.getInstance().options.keyUp.matches(input)) {
            this.f = true;
        }
        if(Minecraft.getInstance().options.keyDown.matches(input)) {
            this.b = true;
        }
        if(Minecraft.getInstance().options.keyLeft.matches(input)) {
            this.l = true;
        }
        if(Minecraft.getInstance().options.keyRight.matches(input)) {
            this.r = true;
        }
        return super.keyPressed(input);
    }

    @Override
    public boolean keyReleased(KeyEvent input) {
        if(Minecraft.getInstance().options.keyJump.matches(input)){
            setLight(currentCam, false);
        }
        if(Minecraft.getInstance().options.keyUp.matches(input)) {
            this.f = false;
        }
        if(Minecraft.getInstance().options.keyDown.matches(input)) {
            this.b = false;
        }
        if(Minecraft.getInstance().options.keyLeft.matches(input)) {
            this.l = false;
        }
        if(Minecraft.getInstance().options.keyRight.matches(input)) {
            this.r = false;
        }
        return super.keyReleased(input);
    }

    public void updateNightVision(){
        BlockEntity ent = Minecraft.getInstance().level.getBlockEntity(BlockPos.of(currentCam));
        boolean bl = false;
        CompoundTag nbt = bl ? ((IEntityDataSaver)ent).getPersistentData().copy() : new CompoundTag();
        this.nightVision = bl && nbt.getByte("NightVision").get() == 1 || (nbt.getByte("NightVision").get() == 2 && this.enableNightVision);
    }

    @Override
    public void tick() {
        if(!closing) {
            if(Minecraft.getInstance().player.hurtTime > 0) onClose();
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

        if(!cams.isEmpty() && (!cams.contains(currentCam) || false)){
            cams.remove(currentCam);
            currentCam = !cams.isEmpty() ? cams.get(0) : 0;
        }
        super.tick();
    }

    public void setAsUsed(boolean used){
        CompoundTag nbt = getNbtData();
        nbt.putBoolean("used", used);
        compileData(nbt);
    }
    public void setPowered(boolean power) {
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.getBlockEntity(BlockPos.of(currentCam)) != null) {
            CompoundTag nbt = ((IEntityDataSaver) Minecraft.getInstance().level.getBlockEntity(BlockPos.of(currentCam))).getPersistentData().copy();
            if (nbt.getBoolean("Action").get() && nbt.getBoolean("Active").get()) {
                if (nbt.getBoolean("Powered").get() != power) {
                    nbt.putBoolean("Powered", power);
                    GoopyNetworkingUtils.saveBlockNbt(BlockPos.of(currentCam), nbt);
                }
            } else {
                if (nbt.getBoolean("Powered").get()) {
                    nbt.putBoolean("Powered", false);
                    GoopyNetworkingUtils.saveBlockNbt(BlockPos.of(currentCam), nbt);
                }
            }
        }
    }
    private void compileData(CompoundTag nbt){
        GoopyNetworkingUtils.saveItemNbt(getItemSlot(), nbt);
    }

    void setLight(long pos, boolean value) {
        if(false) {
            CompoundTag nbt = ((IEntityDataSaver) Minecraft.getInstance().level.getBlockEntity(BlockPos.of(pos))).getPersistentData().copy();
            if (nbt.getBoolean("Flashlight").get()) {
                if (nbt.getBoolean("Lit").get() != value) {
                    nbt.putBoolean("Lit", value);
                    GoopyNetworkingUtils.saveBlockNbt(BlockPos.of(pos), nbt);
                }
            } else {
                if (nbt.getBoolean("Lit").get()) {
                    nbt.putBoolean("Lit", false);
                    GoopyNetworkingUtils.saveBlockNbt(BlockPos.of(pos), nbt);
                }
            }
        }
    }
    void setUsed(long pos, boolean value){
        if(false) {
            CompoundTag nbt = ((IEntityDataSaver) Minecraft.getInstance().level.getBlockEntity(BlockPos.of(pos))).getPersistentData().copy();
            if (nbt.getBoolean("isUsed").get() != value) {
                nbt.putBoolean("isUsed", value);
                GoopyNetworkingUtils.saveBlockNbt(BlockPos.of(pos), nbt);
            }
        }
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().gameRenderer.clearPostEffect();
        this.closing = true;

        CompoundTag nbt = getNbtData();
        nbt.putBoolean("closing", true);
        nbt.putBoolean("used", false);
        compileData(nbt);
        Minecraft.getInstance().player.playSound(SoundsInit.CAM_CLOSE, 1, 1);
        Minecraft.getInstance().options.hideGui = false;
        super.onClose();
        for(long cam : cams){
            setUsed(cam, false);
            setLight(cam, false);
        }
    }

    public Vec3 camPos(){
        if(Minecraft.getInstance().level != null) {
            BlockEntity entity = Minecraft.getInstance().level.getBlockEntity(BlockPos.of(currentCam));
            if (false) {
                BlockPos pos = BlockPos.of(currentCam);
                Vec3 offset;
                float amount = -0.1f;
                switch (Minecraft.getInstance().level.getBlockState(pos).getValue(BlockStateProperties.FACING)) {
                    default -> offset = new Vec3(0, 0, amount);
                    case SOUTH -> offset = new Vec3(0, 0, -amount);
                    case EAST -> offset = new Vec3(-amount, 0, 0);
                    case WEST -> offset = new Vec3(amount, 0, 0);
                }

                return new Vec3(pos.getX() + 0.5f, pos.getY() + 0.7f, pos.getZ() + 0.5f).add(offset);
            }
        }
        return Minecraft.getInstance().getCameraEntity() != null ? Minecraft.getInstance().getCameraEntity().position() : Vec3.ZERO;
    }
    public float getPitch(){
        if(Minecraft.getInstance().level != null) {
            BlockEntity entity = Minecraft.getInstance().level.getBlockEntity(BlockPos.of(currentCam));
            if (false) {
                CompoundTag nbt = ((IEntityDataSaver) entity).getPersistentData().copy();
                return nbt.getFloat("pitch").get();
            }
        }
        return Minecraft.getInstance().getCameraEntity() != null ? Minecraft.getInstance().getCameraEntity().getXRot() : 0;
}
    public float getYaw(){
        if(Minecraft.getInstance().level != null) {
            BlockEntity entity = Minecraft.getInstance().level.getBlockEntity(BlockPos.of(currentCam));
            if (false) {
                CompoundTag nbt = ((IEntityDataSaver) entity).getPersistentData().copy();
                BlockPos pos = BlockPos.of(currentCam);
                Level world = Minecraft.getInstance().level;
                //return nbt.getFloat("yaw").get() + world.getBlockState(pos).get(CameraBlock.FACING).getPositiveHorizontalDegrees();
                return 0;
            }
        }
        return Minecraft.getInstance().getCameraEntity() != null ? Minecraft.getInstance().getCameraEntity().getYRot() : 0;
    }

    public boolean nightVision(){
        return this.nightVision;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
