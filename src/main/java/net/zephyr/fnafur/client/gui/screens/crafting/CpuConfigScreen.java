package net.zephyr.fnafur.client.gui.screens.crafting;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.input.KeyInput;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.blocks.utility_blocks.animatronics.cpu_config_panel.CpuConfigPanelBlock;
import net.zephyr.fnafur.client.ClientHook;
import net.zephyr.fnafur.client.gui.screens.InWorldScreen;
import net.zephyr.fnafur.init.block_init.BlockInit;
import net.zephyr.fnafur.util.mixinAccessing.IEditCamera;
import org.joml.Vector3f;

public class CpuConfigScreen extends InWorldScreen {

    public CpuConfigScreen(Text title, NbtCompound nbt, long l) {
        super(title, nbt, l);
    }

    public CpuConfigScreen(Text text, NbtCompound nbtCompound, Object o) {
        super(text, nbtCompound, o);
    }

    @Override
    public Vec3d getCameraPos() {
        BlockState blockState = MinecraftClient.getInstance().world.getBlockState(getBlockPos());
        if(blockState.isOf(BlockInit.CPU_CONFIG_PANEL)){
            Vec3d offset = blockState.get(CpuConfigPanelBlock.FACING).getDoubleVector().multiply(0.3f);
            Vec3d offset2 = blockState.get(CpuConfigPanelBlock.FACING).rotateYClockwise().getDoubleVector().multiply(-0.175f);
            return getBlockPos().toCenterPos().add(0, -0.04f, 0).add(offset.add(offset2));
        }
        return getBlockPos().toCenterPos().add(new Vec3d(0, 0, 0));
    }

    @Override
    public Vector3f getCameraAngle() {
        BlockState blockState = MinecraftClient.getInstance().world.getBlockState(getBlockPos());
        if(blockState.isOf(BlockInit.CPU_CONFIG_PANEL)){
            return new Vector3f(blockState.get(CpuConfigPanelBlock.FACING).getPositiveHorizontalDegrees() + 180, 0, 0);
        }
        return new Vector3f(0, 0, 0);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(ClientHook.tickTransitionToScreen == 1){
            Text test = Text.literal("TEST");
            drawResizableText(context, textRenderer, test, 3, width/2f, height/2f - 4, 0xFFFFFFFF, 0x00000000, false, true);
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void renderDarkening(DrawContext context) {
        //super.renderDarkening(context);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        return super.mouseDragged(click, offsetX, offsetY);
    }

    @Override
    public boolean mouseReleased(Click click) {
        return super.mouseReleased(click);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        return super.keyPressed(input);
    }

    @Override
    public boolean keyReleased(KeyInput input) {
        return super.keyReleased(input);
    }
}
