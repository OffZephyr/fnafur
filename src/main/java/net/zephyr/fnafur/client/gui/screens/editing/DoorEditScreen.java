package net.zephyr.fnafur.client.gui.screens.editing;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;

public class DoorEditScreen extends GoopyScreen {
    static final Identifier TEXTURE = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/edit_screens/large_door.png");

    int cornerX = 0;
    int cornerY = 0;
    float speed = 0;
    boolean isInverted = false;
    String direction = "up";
    public DoorEditScreen(Text text, NbtCompound nbtCompound, Object o) {
        super(text, nbtCompound, o);
    }

    @Override
    protected void init() {
        cornerX = (width / 2) - 92;
        cornerY = (height / 2) - 44;

        speed = getNbtData().getFloat("speed").orElse(0f);
        isInverted = getNbtData().getBoolean("inverted").orElse(false);
        direction = getNbtData().getString("direction").orElse("up");
        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, cornerX, cornerY, 0, 0, 184, 88, 256, 256);

        int adjustedSpeed = (int) (speed * 4);
        int speedWidth = 2 + (adjustedSpeed * 6);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, cornerX + 46, cornerY + 18, 0, 90, speedWidth, 28, 256, 256);

        float dial = (9 * (adjustedSpeed / 20f));
        int u = 26 * (int) (dial - 0.1f);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, cornerX + 6, cornerY + 18, u, 118, 26, 26, 256, 256);

        float invertU = isInverted ? 190 : 186;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, cornerX + 10, cornerY + 68, invertU, 30, 4, 12, 256, 256);

        float invertTextV = isInverted ? 42 : 52;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, cornerX + 24, cornerY + 68, 186, invertTextV, 22, 10, 256, 256);

        if (isOnButton(mouseX, mouseY, cornerX + 92, cornerY + 64, 12, 18)) {
            context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, cornerX + 92, cornerY + 64, 186, 12, 14, 18, 256, 256);
        }
        if (isOnButton(mouseX, mouseY, cornerX + 106, cornerY + 64, 12, 18)) {
            context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, cornerX + 104, cornerY + 64, 198, 12, 14, 18, 256, 256);
        }

        boolean bl1 = isOnButton(mouseX, mouseY, cornerX + 124, cornerY + 64, 10, 18);

        boolean bl2 = isOnButton(mouseX, mouseY, cornerX + 136, cornerY + 64, 10, 18);

        boolean bl3 = isOnButton(mouseX, mouseY, cornerX + 148, cornerY + 64, 10, 18);

        boolean bl4 = isOnButton(mouseX, mouseY, cornerX + 160, cornerY + 64, 10, 18);

        if (bl1 || direction.equals("up")) {
            int color = bl1 && !direction.equals("up") ? 0x66FFFFFF : 0xFFFFFFFF;
            context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, cornerX + 124, cornerY + 68, 186, 0, 10, 10, 256, 256, color);
        }
        if (bl2 || direction.equals("down")) {
            int color = bl2 && !direction.equals("down") ? 0x66FFFFFF : 0xFFFFFFFF;
            context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, cornerX + 136, cornerY + 68, 196, 0, 10, 10, 256, 256, color);
        }
        if (bl3 || direction.equals("left")) {
            int color = bl3 && !direction.equals("left") ? 0x66FFFFFF : 0xFFFFFFFF;
            context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, cornerX + 148, cornerY + 68, 206, 0, 10, 10, 256, 256, color);
        }
        if (bl4 || direction.equals("right")) {
            int color = bl4 && !direction.equals("right") ? 0x66FFFFFF : 0xFFFFFFFF;
            context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, cornerX + 160, cornerY + 68, 216, 0, 10, 10, 256, 256, color);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        NbtCompound nbt = getNbtData().copy();
        nbt.putBoolean("inverted", isInverted);
        nbt.putFloat("speed", speed);
        nbt.putString("direction", direction);
        GoopyNetworkingUtils.saveBlockNbt(getBlockPos(), nbt, MinecraftClient.getInstance().world);

        super.close();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(button != 0) return false;
        updateSpeed(mouseX, mouseY);

        if(isOnButton(mouseX, mouseY, cornerX + 6, cornerY + 64, 44, 18)){
            isInverted = !isInverted;
        }

        if(isOnButton(mouseX, mouseY, cornerX + 124, cornerY + 64, 10, 18)){
            direction = "up";
        }
        if(isOnButton(mouseX, mouseY, cornerX + 136, cornerY + 64, 10, 18)){
            direction = "down";
        }
        if(isOnButton(mouseX, mouseY, cornerX + 148, cornerY + 64, 10, 18)){
            direction = "left";
        }
        if(isOnButton(mouseX, mouseY, cornerX + 160, cornerY + 64, 10, 18)){
            direction = "right";
        }

        if (isOnButton(mouseX, mouseY, cornerX + 92, cornerY + 64, 12, 18)) {
            switch (direction){
                case "up" -> direction = "right";
                case "down" -> direction = "up";
                case "left" -> direction = "down";
                case "right" -> direction = "left";
            }
        }
        if (isOnButton(mouseX, mouseY, cornerX + 106, cornerY + 64, 12, 18)) {
            switch (direction){
                case "up" -> direction = "down";
                case "down" -> direction = "left";
                case "left" -> direction = "right";
                case "right" -> direction = "up";
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if(button != 0) return false;
        updateSpeed(mouseX, mouseY);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    void updateSpeed(double mouseX, double mouseY){
        if(isOnButton(mouseX, mouseY, cornerX + 46, cornerY + 18, 120, 28)){
            double deltaX = ((mouseX - 3) - (cornerX + 46)) / 120;
            speed = (float) Math.clamp((deltaX * 5) + 0.25f, 0.25, 5);
        }
    }
}
