package net.zephyr.fnafur.client.gui.screens.arcademachine;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import org.lwjgl.glfw.GLFW;

public class ArcademachineScreen extends GoopyScreen {
    Identifier texture = Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/arcademachines/floorboards.png");
    Identifier CHARACTER = Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/arcademachines/freddyidle.png");
    Identifier WALLS = Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/arcademachines/caketemp.png");
    private int characterX;
    private int characterY;
    private int WALLX;
    private int WALLY;
    private final int imageWidth = 256;
    private final int imageHeight = 256;
    private final int CHARACTERSIZE = 64;
    private final int WALLSIZE = 50;
    int newCharacterX = characterX;
    int newCharacterY = characterY;
    int ZEPHYRCHARACTERX = characterX;
    int ZEPHYRCHARACTERY = characterY;

    public ArcademachineScreen(Text title, NbtCompound nbt, long l) {
        super(title, nbt, l);
        this.characterX = (this.width - CHARACTERSIZE) / 2;
        this.characterY = (this.height - CHARACTERSIZE) / 2;
    }

    public ArcademachineScreen(Text text, NbtCompound nbtCompound, Object o) {
        super(text, nbtCompound, o);
    }


    @Override
    protected void init() {
        super.init();
        this.WALLX = (this.width - WALLSIZE) / 4;
        this.WALLY = (this.height - WALLSIZE) / 4;
        //POSITION STUFF DON'T MIND IT ZEPH UNLESS YOU WANT TO MOVE IT
    }



    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        drawContext.fill(0, 0, this.width, this.height, 0xFF000000);

        this.renderBackground(drawContext, mouseX, mouseY, delta);
        RenderSystem.setShaderTexture(0, texture);

        drawContext.drawTexture(texture, (this.width - imageWidth) / 2, (this.height - imageHeight) / 2, 0, 0, imageWidth, imageHeight);
        RenderSystem.setShaderTexture(0, WALLS);
        drawContext.drawTexture(WALLS, WALLX, WALLY, 0, 0, WALLSIZE, WALLSIZE, WALLSIZE, WALLSIZE);

        drawContext.drawTexture(CHARACTER, ZEPHYRCHARACTERX, ZEPHYRCHARACTERY, 0, 0, CHARACTERSIZE, CHARACTERSIZE, CHARACTERSIZE, CHARACTERSIZE);

        super.render(drawContext, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        int newX = ZEPHYRCHARACTERX;
        int newY = ZEPHYRCHARACTERY;

        switch (keyCode) {
            case GLFW.GLFW_KEY_UP:
                newY -= 5;
                break;
            case GLFW.GLFW_KEY_DOWN:
                newY += 5;
                break;
            case GLFW.GLFW_KEY_LEFT:
                newX -= 5;
                break;
            case GLFW.GLFW_KEY_RIGHT:
                newX += 5;
                break;
            default:
                return super.keyPressed(keyCode, scanCode, modifiers);
        }

        if (!(newX + CHARACTERSIZE > WALLX && newX < WALLX + WALLSIZE &&
                newY + CHARACTERSIZE > WALLY && newY < WALLY + WALLSIZE)) {
            ZEPHYRCHARACTERX = newX;
            ZEPHYRCHARACTERY = newY;
        }

        return true;
    }
}