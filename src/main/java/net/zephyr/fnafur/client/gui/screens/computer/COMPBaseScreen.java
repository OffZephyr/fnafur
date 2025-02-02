package net.zephyr.fnafur.client.gui.screens.computer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.utility_blocks.computer.ComputerData;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.client.gui.screens.computer.apps.COMPBaseAppScreen;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.init.SoundsInit;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;

import java.util.Objects;

public abstract class COMPBaseScreen extends GoopyScreen {
    public Identifier WALLPAPER = ComputerData.getWallpapers().get(0).getTexture();
    public Identifier OUTLINE = Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/computer/computer_outline.png");
    public COMPBaseScreen(Text title, NbtCompound nbt, Object o) {
        super(title, nbt, o);
    }
    final int screenSizeBase = 256;
    public int screenSize = 256;
    public int minScreenX = 0;
    public int minScreenY = 0;
    public int maxScreenX = 0;
    public int maxScreenY = 0;

    private boolean doubleClicking = false;
    private int doubleClickingTime = 0;

    @Override
    protected void init() {
        changeWallpaper(getNbtData().getString("wallpaper"));

        super.init();
    }

    public void updateIndex(String currentScreen){
        this.getNbtData().putString("Window", currentScreen);
    }

    @Override
    public void tick() {

        if(!(MinecraftClient.getInstance().world.getBlockState(getBlockPos()).isOf(BlockInit.COMPUTER))){
            close();
        }

        doubleClickingTime = doubleClickingTime > 0 ? doubleClickingTime - 1 : 0;
        doubleClicking = doubleClickingTime > 0;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        BlockPos pos = MinecraftClient.getInstance().gameRenderer.getCamera().getBlockPos();
        MinecraftClient.getInstance().world.playSound(MinecraftClient.getInstance().player, pos, SoundsInit.CLICK_PRESS, SoundCategory.PLAYERS, 1, 1);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        BlockPos pos = MinecraftClient.getInstance().gameRenderer.getCamera().getBlockPos();
        MinecraftClient.getInstance().world.playSound(MinecraftClient.getInstance().player, pos, SoundsInit.CLICK_RELEASE, SoundCategory.PLAYERS, 1, 1);

        if(!doubleClicking) {
            doubleClickingTime = 10;
            doubleClicking = true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public void changeWallpaper(String name){
        getNbtData().putString("wallpaper", name);
        saveData();

        for(ComputerData.Initializer.Wallpaper wallpaper : ComputerData.getWallpapers()) {
            if(Objects.equals(wallpaper.getId(), getNbtData().getString("wallpaper"))){
                WALLPAPER = wallpaper.getTexture();
            }
        }
    }

    public boolean isDoubleClicking(){
        return doubleClicking;
    }

    public void saveData() {
        if(MinecraftClient.getInstance().currentScreen instanceof COMPBootupScreen) return;

        if (MinecraftClient.getInstance().currentScreen instanceof COMPBaseAppScreen appScreen) {
            this.updateIndex(appScreen.appName());
            GoopyNetworkingUtils.saveBlockNbt(getBlockPos(), getNbtData());
            return;
        }

        this.updateIndex("default");
        GoopyNetworkingUtils.saveBlockNbt(getBlockPos(), getNbtData());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        minScreenX = (this.width/2 - screenSize/2);
        maxScreenX = (this.width/2 + screenSize/2);
        minScreenY = (this.height/2 - screenSize/2);
        maxScreenY = (this.height/2 + screenSize/2);

        context.fill(0, 0, this.width, (this.height/2 - screenSize/2), 0xAA000000);
        context.fill(0, (this.height/2 - screenSize/2), (this.width/2 - screenSize/2), (this.height/2 + screenSize/2), 0xAA000000);
        context.fill((this.width/2 + screenSize/2), (this.height/2 - screenSize/2), this.width, (this.height/2 + screenSize/2), 0xAA000000);
        context.fill(0, (this.height/2 + screenSize/2), this.width, this.height, 0xAA000000);

        int x = (this.width/2 - screenSize);
        int y = (this.height/2 - screenSize);

        drawRecolorableTexture(context, OUTLINE, x, y, 0, screenSize * 2, screenSize * 2, 0, 0, screenSize * 2, screenSize * 2, 1, 1, 1, 1);
    }

    public void drawTextureOnScreen(DrawContext context, Identifier texture, int x, int y, int z, float regionWidth, float regionHeight, float u, float v, float textureWidth, float textureHeight, float red, float green, float blue, float alpha){

        float clampedWidth = x < minScreenX ? regionWidth + (x - minScreenX) :
                x > (maxScreenX - regionWidth) ? (regionWidth) - (x + regionWidth - maxScreenX):
                        regionWidth;

        float clampedHeight = y < minScreenY ? regionHeight + (y - minScreenY) :
                y > (maxScreenY - regionHeight) ? (regionHeight) - (y + regionHeight - maxScreenY):
                        regionHeight;

        float clampedU = x < minScreenX ? u + (regionWidth - clampedWidth) : u;
        float clampedV = y < minScreenY ? v + (regionHeight - clampedHeight) : v;

        int clampedX = MathHelper.clamp(x, minScreenX, maxScreenX);
        int clampedY = MathHelper.clamp(y, minScreenY, maxScreenY);

        drawRecolorableTexture(context, texture, clampedX, clampedY, z, clampedWidth, clampedHeight, clampedU, clampedV, textureWidth, textureHeight, red, green, blue, alpha);
    }
    public void fillShapeOnScreen(DrawContext context, int x, int y, int z, int regionWidth, int regionHeight, int color){

        int clampedWidth = x < minScreenX ? regionWidth + (x - minScreenX) :
                x > (maxScreenX - regionWidth) ? (regionWidth) - (x + regionWidth - maxScreenX):
                        regionWidth;

        int clampedHeight = y < minScreenY ? regionHeight + (y - minScreenY) :
                y > (maxScreenY - regionHeight) ? (regionHeight) - (y + regionHeight - maxScreenY):
                        regionHeight;

        int clampedX = MathHelper.clamp(x, minScreenX, maxScreenX);
        int clampedY = MathHelper.clamp(y, minScreenY, maxScreenY);

        context.fill(clampedX, clampedY, clampedX + clampedWidth, clampedY + clampedHeight, z, color);
    }

    @Override
    public void close() {
        saveData();
        super.close();
    }
}
