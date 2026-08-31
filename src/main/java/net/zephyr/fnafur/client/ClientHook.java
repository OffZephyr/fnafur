package net.zephyr.fnafur.client;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.state.LevelRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropRenderer;
import net.zephyr.fnafur.client.gui.screens.InWorldScreen;
import net.zephyr.fnafur.util.CustomDataTickets;
import net.zephyr.fnafur.util.EasingMathUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEditCamera;
import org.joml.Vector3f;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class ClientHook {
    public static float tickTransitionToScreen = 0;
    static Vec3 lastScreenPos;
    static Vector3f lastScreenAngle;
    static boolean wasHudHidden = false;
    public static void openScreen(String index, CompoundTag nbt, long l){
        /*if (ScreenUtils.getScreens().containsKey(index)) {
            Screen screen = ScreenUtils.getScreens().get(index).create(Text.translatable("screen." + index + ".title"), nbt, l);
            if(Minecraft.getInstance().currentScreen == null || Minecraft.getInstance().currentScreen.getClass() != screen.getClass()) {
                Minecraft.getInstance().setScreen(screen);
            }
        }*/
    }

    public static<T extends BlockEntity, R extends BlockEntityRenderState & GeoRenderState> void renderWorldBlockEntity(BlockEntityRenderer<T, R> blockEntityRenderer, R state, PoseStack matrices, LevelRenderState renderStates, SubmitNodeStorage queue){

        PoseStack.Pose pos = state.getGeckolibData(CustomDataTickets.ENTITY_RENDER_MATRIX_ENTRY);
        matrices.pushPose();
        //matrices.multiplyPositionMatrix(pos.getPositionMatrix());
        matrices.translate(Minecraft.getInstance().gameRenderer.getMainCamera().position.scale(-1));

        if(blockEntityRenderer != null){
            if(state.hasGeckolibData(CustomDataTickets.IS_ENTITY_PREVIEW) && Boolean.TRUE.equals(state.getGeckolibData(CustomDataTickets.IS_ENTITY_PREVIEW))){
                ((GeoPropRenderer<GeoPropBlockEntity, R>)blockEntityRenderer).renderPreview(state, matrices, queue, renderStates.cameraRenderState);
            }
        }
        matrices.popPose();
    }

    public static boolean updateCamera(Camera camera, Level area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickProgress) {

//        if (true) return false;
        float progress = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks()/20f;
        if(tickTransitionToScreen == 0){
            wasHudHidden = Minecraft.getInstance().options.hideGui;
        }

        if (Minecraft.getInstance().screen instanceof InWorldScreen screen) {
            tickTransitionToScreen = Math.clamp(tickTransitionToScreen + (progress * 1.1f), 0, 1);

            camera.setRotation(screen.getCameraAngle().x, screen.getCameraAngle().y);
            lastScreenPos = screen.getCameraPos();
            lastScreenAngle = screen.getCameraAngle();
        } else {
            tickTransitionToScreen = Math.clamp(tickTransitionToScreen - (progress * 1.1f), 0, 1);
        }

        if(lastScreenPos == null){
            lastScreenPos = new Vec3(focusedEntity.getX(), focusedEntity.getEyeY(), focusedEntity.getZ());
        }
        if(lastScreenAngle == null){
            lastScreenAngle = new Vector3f(((focusedEntity.getViewYRot(tickProgress) + 180f)%360f) + 180 + 360, focusedEntity.getViewXRot(tickProgress), 0);
        }

        float index = (float) EasingMathUtil.easeInOutQuad(tickTransitionToScreen);

        double entity_x = Mth.lerp((double) tickProgress, focusedEntity.xo, focusedEntity.getX());
        double entity_y = Mth.lerp((double) tickProgress, focusedEntity.yo + focusedEntity.getEyeY() - focusedEntity.getY(), focusedEntity.getEyeY());
        double entity_z = Mth.lerp((double) tickProgress, focusedEntity.zo, focusedEntity.getZ());

        float entity_yaw = focusedEntity.getViewYRot(tickProgress);
        while(entity_yaw < 0){
            entity_yaw += 360f;
        }
        //System.out.println("PRE: " + entity_yaw);
        //entity_yaw = ((entity_yaw + 360f)%360f);
        float start = entity_yaw%360f;
        float end = lastScreenAngle.x%360f;
        end = start > 180 && end < 180 ? end + 360 : end;
        float entity_pitch = focusedEntity.getViewXRot(tickProgress);

        double x = Mth.lerp(index, entity_x, lastScreenPos.x);
        double y = Mth.lerp(index, entity_y, lastScreenPos.y);
        double z = Mth.lerp(index, entity_z, lastScreenPos.z);


        float yaw = Mth.lerp(index, start, end);
        float pitch = Mth.lerp(index, entity_pitch, lastScreenAngle.y);
        float roll = Mth.lerp(index, 0, lastScreenAngle.z);

        ((IEditCamera) camera).setPos(x, y, z);
        ((IEditCamera) camera).setRotation(yaw, pitch, roll);
        ((IEditCamera) camera).setThirsPerson(tickTransitionToScreen > 0.85f);
        Minecraft.getInstance().options.hideGui = wasHudHidden || tickTransitionToScreen > 0;

        return tickTransitionToScreen > 0;
    }

}
