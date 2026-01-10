package net.zephyr.fnafur.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueueImpl;
import net.minecraft.client.render.state.WorldRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropRenderer;
import net.zephyr.fnafur.client.gui.screens.InWorldScreen;
import net.zephyr.fnafur.client.gui.screens.crafting.CpuConfigScreen;
import net.zephyr.fnafur.util.CustomDataTickets;
import net.zephyr.fnafur.util.EasingMathUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEditCamera;
import net.zephyr.fnafur.util.mixinAccessing.IWorldRendererAccessor;
import org.joml.Vector3f;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class ClientHook {
    public static float tickTransitionToScreen = 0;
    static Vec3d lastScreenPos;
    static Vector3f lastScreenAngle;
    static boolean wasHudHidden = false;
    public static void openScreen(String index, NbtCompound nbt, long l){
        /*if (ScreenUtils.getScreens().containsKey(index)) {
            Screen screen = ScreenUtils.getScreens().get(index).create(Text.translatable("screen." + index + ".title"), nbt, l);
            if(MinecraftClient.getInstance().currentScreen == null || MinecraftClient.getInstance().currentScreen.getClass() != screen.getClass()) {
                MinecraftClient.getInstance().setScreen(screen);
            }
        }*/
    }

    public static<T extends BlockEntity, R extends BlockEntityRenderState & GeoRenderState> void renderWorldBlockEntity(BlockEntityRenderer<T, R> blockEntityRenderer, R state, MatrixStack matrices, WorldRenderState renderStates, OrderedRenderCommandQueueImpl queue){

        MatrixStack.Entry pos = state.getGeckolibData(CustomDataTickets.ENTITY_RENDER_MATRIX_ENTRY);
        matrices.push();
        //matrices.multiplyPositionMatrix(pos.getPositionMatrix());
        matrices.translate(MinecraftClient.getInstance().gameRenderer.getCamera().pos.multiply(-1));

        if(blockEntityRenderer != null){
            if(state.hasGeckolibData(CustomDataTickets.IS_ENTITY_PREVIEW) && Boolean.TRUE.equals(state.getGeckolibData(CustomDataTickets.IS_ENTITY_PREVIEW))){
                ((GeoPropRenderer<GeoPropBlockEntity, R>)blockEntityRenderer).renderPreview(state, matrices, queue, renderStates.cameraRenderState);
            }
        }
        matrices.pop();
    }

    public static boolean updateCamera(Camera camera, World area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickProgress) {

        float progress = MinecraftClient.getInstance().getRenderTickCounter().getDynamicDeltaTicks()/20f;
        if(tickTransitionToScreen == 0){
            wasHudHidden = MinecraftClient.getInstance().options.hudHidden;
        }

        if (MinecraftClient.getInstance().currentScreen instanceof InWorldScreen screen) {
            tickTransitionToScreen = Math.clamp(tickTransitionToScreen + (progress * 1.1f), 0, 1);

            camera.setRotation(screen.getCameraAngle().x, screen.getCameraAngle().y);
            lastScreenPos = screen.getCameraPos();
            lastScreenAngle = screen.getCameraAngle();
        } else {
            tickTransitionToScreen = Math.clamp(tickTransitionToScreen - (progress * 1.1f), 0, 1);
        }

        if(lastScreenPos == null){
            lastScreenPos = new Vec3d(focusedEntity.getX(), focusedEntity.getEyeY(), focusedEntity.getZ());
        }
        if(lastScreenAngle == null){
            lastScreenAngle = new Vector3f(((focusedEntity.getYaw(tickProgress) + 180f)%360f) + 180 + 360, focusedEntity.getPitch(tickProgress), 0);
        }

        float index = (float) EasingMathUtil.easeInOutQuad(tickTransitionToScreen);

        double entity_x = MathHelper.lerp((double) tickProgress, focusedEntity.lastX, focusedEntity.getX());
        double entity_y = MathHelper.lerp((double) tickProgress, focusedEntity.lastY + focusedEntity.getEyeY() - focusedEntity.getY(), focusedEntity.getEyeY());
        double entity_z = MathHelper.lerp((double) tickProgress, focusedEntity.lastZ, focusedEntity.getZ());

        float entity_yaw = focusedEntity.getYaw(tickProgress);
        while(entity_yaw < 0){
            entity_yaw += 360f;
        }
        entity_yaw = ((entity_yaw + 180f)%360f) + 180 - 360;
        float entity_pitch = focusedEntity.getPitch(tickProgress);

        double x = MathHelper.lerp(index, entity_x, lastScreenPos.x);
        double y = MathHelper.lerp(index, entity_y, lastScreenPos.y);
        double z = MathHelper.lerp(index, entity_z, lastScreenPos.z);

        float yaw = MathHelper.lerp(index, entity_yaw, lastScreenAngle.x%360f);
        float pitch = MathHelper.lerp(index, entity_pitch, lastScreenAngle.y);
        float roll = MathHelper.lerp(index, 0, lastScreenAngle.z);

        ((IEditCamera) camera).setPosition(x, y, z);
        ((IEditCamera) camera).setRotation(yaw, pitch, roll);
        ((IEditCamera) camera).setThirsPerson(tickTransitionToScreen > 0.85f);
        MinecraftClient.getInstance().options.hudHidden = wasHudHidden || tickTransitionToScreen > 0;

        return tickTransitionToScreen > 0;
    }

}
