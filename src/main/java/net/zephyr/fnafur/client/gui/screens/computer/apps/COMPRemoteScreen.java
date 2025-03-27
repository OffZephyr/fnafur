package net.zephyr.fnafur.client.gui.screens.computer.apps;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.utility_blocks.computer.ComputerData;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.item.EntitySpawnItem;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.fnafur.util.mixinAccessing.IEditCamera;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class COMPRemoteScreen extends COMPBaseAppScreen {
    List<DefaultEntity> entities = new ArrayList<>();
    public Identifier LIST = Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/computer/window_smallbar.png");
    public Identifier LIST_OFF = Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/computer/window_smallbar_off.png");
    DefaultEntity control = null;

    int selected = -1;

    float listOffset = 0;

    public COMPRemoteScreen(Text title, NbtCompound nbt, long l) {
        super(title, nbt, l);
        updateAnimatronics(MinecraftClient.getInstance().world);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void tick() {
        FnafUniverseResuited.print(MinecraftClient.getInstance().getSoundManager().getDebugString());
        super.tick();
    }


    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if(control == null) {
            if(selected > -1 && isOnButton(mouseX, mouseY, this.width / 2 + 248 / 2 - 2 - 32, this.height / 2 - 248 / 2 + 2, 16, 16)) {
                this.control = this.entities.get(selected);
                MinecraftClient.getInstance().options.hudHidden = true;
                ((IEditCamera)MinecraftClient.getInstance().gameRenderer.getCamera()).setThirsPerson(true);
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(control == null) {
            if(mouseY > topCornerY) {
                for (int i = 0; i < entities.size(); i++) {

                    float x = topCornerX;
                    float y = topCornerY + 18 * i + listOffset;

                    if (isOnButton(mouseX, mouseY, (int) x, (int) y, (int) appAvailableSizeX, 18)) {
                        selected = selected != i ? i : -1;
                    }
                }
            }
        }
                return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount)
    {
    listOffset += 12 * verticalAmount;
    listOffset = MathHelper.clamp(listOffset, Math.min(0, -((18 * this.entities.size()) - appAvailableSizeX) - 16), 0);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        listOffset += deltaY;
        listOffset = MathHelper.clamp(listOffset, Math.min(0, -((18 * this.entities.size()) - appAvailableSizeX) - 16), 0);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(control != null) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE && this.shouldCloseOnEsc()) {
                resetEntity();
                return true;
            }
            return false;
        }
        else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if(isOnButton(mouseX,mouseY, 0, 0, this.width / 4, this.height)) {

        }
        return super.isMouseOver(mouseX, mouseY);
    }

    @Override
    public void close() {
        resetEntity();
        super.close();
    }

    public void resetEntity() {
        MinecraftClient.getInstance().setCameraEntity(MinecraftClient.getInstance().player);
        updateAnimatronics(MinecraftClient.getInstance().world);
        ((IEditCamera)MinecraftClient.getInstance().gameRenderer.getCamera()).setThirsPerson(false);
        MinecraftClient.getInstance().options.hudHidden = false;
        control = null;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(control == null) {
            context.fill((int)topCornerX, (int)topCornerY, (int)topCornerX + (int)appAvailableSizeX, (int)topCornerY + (int)appAvailableSizeY, 0xFF182562);
            for(int i = 0; i < entities.size(); i++) {
                DefaultEntity entity = entities.get(i);

                float x = topCornerX;
                float y = topCornerY + 18 * i + listOffset;

                Identifier list = i == selected ? LIST_OFF : LIST;
                drawTextureOnScreen(context, list, (int)x, (int)y, 0, appAvailableSizeX, 18, 0, 0, appAvailableSizeX, 18, 1, 1, 1, 1);

                if(y >= topCornerY && y + 18 <= topCornerY + appAvailableSizeY) {
                    EntitySpawnItem spawnItem = EntitySpawnItem.forEntity(entity.getType());
                    if (spawnItem != null) {
                        ItemStack stack = new ItemStack(spawnItem);
                        context.drawItem(stack, (int) x + 2, (int) y + 1, -10);
                    }

                    Text entityName = Text.literal("§r§l" + entity.getName().getString());
                    String skin = ((IEntityDataSaver) entity).getPersistentData().getString("Reskin");
                    Text skinName = Text.literal("§r | " + Text.translatable(skin).getString());
                    float textX = x + 20;

                    Text entityText = Text.literal(entityName.getString() + skinName.getString());
                    drawAutoResizedText(context, textRenderer, entityText, 1.1f, (int) appAvailableSizeX - 36, textX, y + 5, 0xFF000000, 0, false, false);

                }
                    int blockX = getBlockPos().getX();
                    int blockY = getBlockPos().getY();
                    int blockZ = getBlockPos().getZ();

                    int entityX = entity.getBlockPos().getX();
                    int entityY = entity.getBlockPos().getY();
                    int entityZ = entity.getBlockPos().getZ();

                    double distance = Math.abs(Math.sqrt(
                            ((entityX - blockX) * (entityX - blockX)) +
                                    ((entityY - blockY) * (entityY - blockY)) +
                                    ((entityZ - blockZ) * (entityZ - blockZ))
                    ));

                    int strength = (int) distance / 25;
                    Identifier signal = Identifier.of(FnafUniverseResuited.MOD_ID, "textures/gui/computer/signal_" + strength + ".png");
                    int signalTextureSize = 16;
                    drawTextureOnScreen(context, signal, (int) topCornerX + (int) appAvailableSizeX - signalTextureSize - 1, (int) y + 1, 0, signalTextureSize, signalTextureSize, 0, 0, signalTextureSize, signalTextureSize, 1, 1, 1, 1);

                    String pos = "[" + entity.getBlockPos().getX() + ", " + entity.getBlockPos().getY() + ", " + entity.getBlockPos().getZ() + "]";
                    String distanceText = (int) distance + " blocks away...";
                    List<Text> tooltip = new ArrayList<>();
                    tooltip.add(Text.literal("§7" + distanceText));
                    tooltip.add(Text.literal("§8" + pos));
                    if (isOnButton(mouseX, mouseY, (int) topCornerX + (int) appAvailableSizeX - signalTextureSize - 1, (int) y + 1, signalTextureSize, signalTextureSize)) {
                        context.drawTooltip(textRenderer, tooltip, mouseX, mouseY);
                    }
            }
            super.render(context, mouseX, mouseY, delta);
            renderButton(BUTTONS, context, this.width / 2 + 248 / 2 - 2 - 32, this.height / 2 - 248 / 2 + 2, 16*3, 16*4, 16*4, 16*4, 16*5, 16*4, 16, 16, 128, 128, mouseX, mouseY, getHolding(), selected == -1);
        }
        else {
            final Vector3f ENTITY_TRANSLATION = new Vector3f();
            float yaw = control.getHeadYaw() + 160;
            Quaternionf rotation = new Quaternionf().rotationXYZ(0.2f, -yaw * (MathHelper.PI / 180), MathHelper.PI);
            control.isGUI = true;
            InventoryScreen.drawEntity(context, (this.width / 8f) * 7, (this.height / 12f) * 11, 45, ENTITY_TRANSLATION, rotation, null, control);
            control.isGUI = false;
        }
    }


    void updateAnimatronics(World world) {
        Vec3d vec = getBlockPos().toCenterPos();
        final double boxSize = 100;
        double x1 = vec.getX() - boxSize;
        double y1 = vec.getY() - boxSize;
        double z1 = vec.getZ() - boxSize;
        double x2 = vec.getX() + boxSize;
        double y2 = vec.getY() + boxSize;
        double z2 = vec.getZ() + boxSize;
        Box box = new Box(x1, y1, z1, x2, y2, z2);
        List<DefaultEntity> list = world.getNonSpectatingEntities(DefaultEntity.class, box);
        List<DefaultEntity> finalList = new ArrayList<>();
        for(DefaultEntity entity : list) {
            if(Objects.equals(entity.getBehavior(), ComputerData.getAIBehavior("controllable").getId())) {
                finalList.add(entity);
            }
        }

        this.entities = finalList;
    }

    @Override
    public String appName() {
        return "remote";
    }

    public DefaultEntity getControl() {
        return this.control;
    }
}
