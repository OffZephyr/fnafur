package net.zephyr.fnafur.entity.animatronic;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.zephyr.fnafur.client.CustomRenderingPipelines;
import net.zephyr.fnafur.entity.animatronic.voice.EntityVoiceSoundInstance;
import net.zephyr.fnafur.util.CustomDataTickets;
import net.zephyr.fnafur.util.jsonReaders.animatronics.AnimatronicDataHandler;
import org.jetbrains.annotations.Nullable;
import com.geckolib.cache.model.GeoBone;
import com.geckolib.constant.DataTickets;
import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.BoneSnapshots;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.RenderPassInfo;

import java.util.List;

public class AnimatronicRenderer<T extends AnimatronicEntity, R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<T, R> {
    private final List<String> EYE_BONE_NAME_TEXTURE_LIST = List.of(
         "eyeleft",
         "eyeright"
    );    private final List<String> EYE_BONE_NAME_NONE_LIST = List.of(
         "eyeleft",
         "eyeright"
    );
    SubmitNodeCollector renderTasks;
    VertexConsumer buffer2;

    public AnimatronicRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AnimatronicModel<>());
    }

    @Override
    public void submit(R renderState, PoseStack poseStack, SubmitNodeCollector renderTasks, CameraRenderState cameraState) {

        float scale = 1;
        if(Boolean.TRUE.equals(renderState.getGeckolibData(CustomDataTickets.IS_ENTITY_PREVIEW)) && renderState.hasGeckolibData(CustomDataTickets.RENDER_SCALE)){
            scale = renderState.getGeckolibData(CustomDataTickets.RENDER_SCALE);
        }
        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        this.renderTasks = renderTasks;
        super.submit(renderState, poseStack, renderTasks, cameraState);
        poseStack.popPose();
    }

    @Override
    public void addRenderData(T animatable, Void relatedObject, R renderState, float partialTick) {
        super.addRenderData(animatable, relatedObject, renderState, partialTick);
    }

    @Override
    public R fillRenderState(T animatable, Void relatedObject, R renderState, float partialTick) {

        if(animatable.isFrozen){
            renderState.bodyRot = animatable.frozenBodyYaw;
//            renderState.relativeHeadYaw = MathHelper.wrapDegrees(animatable.frozenHeadYaw - renderState.bodyYaw);
            renderState.yRot = Mth.wrapDegrees(animatable.frozenHeadYaw - renderState.bodyRot);
            renderState.xRot = (float) animatable.frozenPitch;
        }

        super.fillRenderState(animatable, relatedObject, renderState, partialTick);

        if(animatable.isMenu){
            renderState.addGeckolibData(DataTickets.PACKED_LIGHT, LightCoordsUtil.FULL_BRIGHT);
            renderState.addGeckolibData(DataTickets.PACKED_OVERLAY, OverlayTexture.NO_OVERLAY);
            renderState.addGeckolibData(DataTickets.RENDER_COLOR, 0xFFFFFFFF);
        }

        renderState.addGeckolibData(CustomDataTickets.IS_ENTITY_PREVIEW, animatable.isMenu);
        renderState.addGeckolibData(CustomDataTickets.RENDER_SCALE,AnimatronicDataHandler.getPreviewScale(animatable.getChara(), animatable.getAlt()));
        renderState.addGeckolibData(CustomDataTickets.TEXTURE, animatable.getTexture(animatable.level()));
        renderState.addGeckolibData(CustomDataTickets.EYE_TEXTURE, animatable.getEyeTexture(animatable.level()));
        renderState.addGeckolibData(CustomDataTickets.EYE_MAP_TEXTURE, animatable.getEyeMapTexture(animatable.level()));
        renderState.addGeckolibData(CustomDataTickets.EYE_GLOW_COLOR_TEXTURE, animatable.getEyesGlowColor().getIdentifier());
        renderState.addGeckolibData(CustomDataTickets.SUIT_MAP_TEXTURE, animatable.getEyeTexture(animatable.level()));
        renderState.addGeckolibData(CustomDataTickets.EYE_NONE, animatable.isEmptyEye());
        renderState.addGeckolibData(CustomDataTickets.EYES_GLOW, animatable.shouldEyesGlow());
        renderState.addGeckolibData(CustomDataTickets.EYES_GLOW_MODE, animatable.getEyesGlowMode());
        renderState.addGeckolibData(CustomDataTickets.MODEL, animatable.getModel(animatable.level()));
        renderState.addGeckolibData(CustomDataTickets.RE_RENDER_TEXTURE, animatable.getReRenderTexture(animatable.level()));
        renderState.addGeckolibData(CustomDataTickets.RE_RENDER_MODEL, animatable.getReRenderModel(animatable.level()));
        renderState.addGeckolibData(CustomDataTickets.RENDER_LAYER, animatable.getRenderType(animatable.getTexture(animatable.level())));

        renderState.addGeckolibData(CustomDataTickets.ANIMATRONIC_POSE, animatable.getAnimatronicPose());
        renderState.addGeckolibData(CustomDataTickets.FORCED_HEAD_YAW, Mth.wrapDegrees(animatable.getYHeadRot() - renderState.bodyRot));
        renderState.addGeckolibData(CustomDataTickets.FORCED_PITCH, animatable.getXRot());
        renderState.addGeckolibData(CustomDataTickets.IS_FROZEN, animatable.isFrozen);

        return renderState;
    }

    @Override
    public void adjustModelBonesForRender(RenderPassInfo<R> renderPassInfo, BoneSnapshots snapshots) {
        if(renderPassInfo.renderState().hasGeckolibData(CustomDataTickets.EYE_NONE)) {
            AnimatronicEntity.AnimatronicPose pose = renderPassInfo.renderState().getGeckolibData(CustomDataTickets.ANIMATRONIC_POSE);

            GeoBone head_main = renderPassInfo.model().getBone("head_main").orElse(null);
            GeoBone torso_main = renderPassInfo.model().getBone("torso_main").orElse(null);
            GeoBone eyeleft_main = renderPassInfo.model().getBone("eyeleft_main").orElse(null);
            GeoBone eyeright_main = renderPassInfo.model().getBone("eyeright_main").orElse(null);

            boolean isFrozen = renderPassInfo.renderState().hasGeckolibData(CustomDataTickets.IS_FROZEN) && renderPassInfo.renderState().hasGeckolibData(CustomDataTickets.FORCED_HEAD_YAW) && Boolean.TRUE.equals(renderPassInfo.renderState().getGeckolibData(CustomDataTickets.IS_FROZEN));

            float pitch = -renderPassInfo.renderState().xRot * Mth.DEG_TO_RAD;
            float yaw = -renderPassInfo.renderState().yRot * Mth.DEG_TO_RAD;

            if (head_main != null) {
                snapshots.get(head_main).setRotation(pitch / 2f, yaw / 4f, 0);
            }
            if (torso_main != null) {
                float halfYaw = pose == AnimatronicEntity.AnimatronicPose.CRAWLING ? 0 : yaw / 4f;
                float halfPitch = pose == AnimatronicEntity.AnimatronicPose.CRAWLING ? 0 : pitch / 4f;
                snapshots.get(torso_main).setRotation(halfPitch, halfYaw, 0);
            }

            float eyeYaw = !isFrozen ? yaw / 4f : -Math.clamp(renderPassInfo.renderState().getGeckolibData(CustomDataTickets.FORCED_HEAD_YAW), -45f, 45f) * Mth.DEG_TO_RAD;
            float eyePitch = !isFrozen ? pitch / 2f : -Math.clamp(renderPassInfo.renderState().getGeckolibData(CustomDataTickets.FORCED_PITCH), -30f, 30f) * Mth.DEG_TO_RAD;

            eyeYaw -= yaw/2f;

            if (eyeleft_main != null) {
                snapshots.get(eyeleft_main).setRotation(eyePitch, eyeYaw, 0);
            }
            if (eyeright_main != null) {
                snapshots.get(eyeright_main).setRotation(eyePitch, eyeYaw, 0);
            }
        }


        super.adjustModelBonesForRender(renderPassInfo, snapshots);
    }

    @Override
    protected boolean affectedByCulling(T entity) {
        return false;
    }

    @Override
    public @Nullable RenderType getRenderType(R renderState, Identifier texture) {
        if(renderState.hasGeckolibData(CustomDataTickets.EYE_NONE) && Boolean.TRUE.equals(renderState.getGeckolibData(CustomDataTickets.EYE_NONE))) return CustomRenderingPipelines.getAnimatronicNoEyes(texture, renderState.getGeckolibData(CustomDataTickets.EYE_TEXTURE), renderState.getGeckolibData(CustomDataTickets.EYE_MAP_TEXTURE), renderState.getGeckolibData(CustomDataTickets.EYE_GLOW_COLOR_TEXTURE));

        if(renderState.hasGeckolibData(CustomDataTickets.EYES_GLOW) && Boolean.TRUE.equals(renderState.getGeckolibData(CustomDataTickets.EYES_GLOW))) {

            if(renderState.hasGeckolibData(CustomDataTickets.EYES_GLOW_MODE)){
                return switch (renderState.getGeckolibData(CustomDataTickets.EYES_GLOW_MODE)){
                    case IRISES -> CustomRenderingPipelines.getAnimatronicGlowingEyesIrises(texture, renderState.getGeckolibData(CustomDataTickets.EYE_TEXTURE), renderState.getGeckolibData(CustomDataTickets.EYE_MAP_TEXTURE), renderState.getGeckolibData(CustomDataTickets.EYE_GLOW_COLOR_TEXTURE));
                    case IRISES_DOTS -> CustomRenderingPipelines.getAnimatronicGlowingEyesIrisesDots(texture, renderState.getGeckolibData(CustomDataTickets.EYE_TEXTURE), renderState.getGeckolibData(CustomDataTickets.EYE_MAP_TEXTURE), renderState.getGeckolibData(CustomDataTickets.EYE_GLOW_COLOR_TEXTURE));
                    case FULL_EYES -> CustomRenderingPipelines.getAnimatronicGlowingEyesFullEyes(texture, renderState.getGeckolibData(CustomDataTickets.EYE_TEXTURE), renderState.getGeckolibData(CustomDataTickets.EYE_MAP_TEXTURE), renderState.getGeckolibData(CustomDataTickets.EYE_GLOW_COLOR_TEXTURE));
                    case FULL_EYES_DOTS -> CustomRenderingPipelines.getAnimatronicGlowingEyesFullEyesDots(texture, renderState.getGeckolibData(CustomDataTickets.EYE_TEXTURE), renderState.getGeckolibData(CustomDataTickets.EYE_MAP_TEXTURE), renderState.getGeckolibData(CustomDataTickets.EYE_GLOW_COLOR_TEXTURE));
                    default -> CustomRenderingPipelines.getAnimatronicGlowingEyesDots(texture, renderState.getGeckolibData(CustomDataTickets.EYE_TEXTURE), renderState.getGeckolibData(CustomDataTickets.EYE_MAP_TEXTURE), renderState.getGeckolibData(CustomDataTickets.EYE_GLOW_COLOR_TEXTURE));
                };
            }
        }

        return CustomRenderingPipelines.getAnimatronic(texture, renderState.getGeckolibData(CustomDataTickets.EYE_TEXTURE), renderState.getGeckolibData(CustomDataTickets.EYE_MAP_TEXTURE), renderState.getGeckolibData(CustomDataTickets.EYE_GLOW_COLOR_TEXTURE));
        //return super.getRenderType(renderState, texture);
    }

//    @Override
//    public void renderBone(R renderState, MatrixStack poseStack, GeoBone bone, VertexConsumer buffer, CameraRenderState cameraState, int packedLight, int packedOverlay, int renderColor) {
//        boolean hide = isUsingEyeTexture(renderState);
//
//        if(!isEyeBone(renderState)) {
//            for (String eyeBoneName : EYE_BONE_NAME_TEXTURE_LIST) {
//                if (bone.getName().contains(eyeBoneName)) {
//                    renderState.addGeckolibData(CustomDataTickets.IS_EYE_BONE, true);
//                    break;
//                }
//            }
//        }
//        else {
//            hide = !hide;
//        }
//
//        poseStack.push();
//        RenderUtil.translateMatrixToBone(poseStack, bone);
//        RenderUtil.translateToPivotPoint(poseStack, bone);
//        RenderUtil.rotateMatrixAroundBone(poseStack, bone);
//        RenderUtil.scaleMatrixForBone(poseStack, bone);
//
//        if (bone.isTrackingMatrices()) {
//            Matrix4f poseState = new Matrix4f(poseStack.peek().getPositionMatrix());
//            Matrix4f localMatrix = RenderUtil.invertAndMultiplyMatrices(poseState, renderState.getGeckolibData(DataTickets.OBJECT_RENDER_POSE));
//
//            bone.setModelSpaceMatrix(RenderUtil.invertAndMultiplyMatrices(poseState, renderState.getGeckolibData(DataTickets.MODEL_RENDER_POSE)));
//            bone.setLocalSpaceMatrix(RenderUtil.translateMatrix(localMatrix, getPositionOffset(renderState).toVector3f()));
//            bone.setWorldSpaceMatrix(RenderUtil.translateMatrix(new Matrix4f(localMatrix), new Vector3f((float)renderState.x, (float)renderState.y, (float)renderState.z)));
//        }
//
//        RenderUtil.translateAwayFromPivotPoint(poseStack, bone);
//
//        if(!hide && !(isEyeBone(renderState) && renderState.getGeckolibData(CustomDataTickets.EYE_NONE).booleanValue())){
//            renderCubesOfBone(renderState, bone, poseStack, buffer, cameraState, packedLight, packedOverlay, renderColor);
//        }
//
//        if(isEyeBone(renderState)){
//            for(GeoBone childBone : bone.getChildBones()){
//                if(childBone.getParent() == bone){
//                    renderState.addGeckolibData(CustomDataTickets.IS_EYE_BONE, true);
//                    renderBone(renderState, poseStack, childBone, buffer, cameraState, packedLight, packedOverlay, renderColor);
//                }
//            }
//        }
//        else {
//            renderChildBones(renderState, bone, poseStack, buffer, cameraState, packedLight, packedOverlay, renderColor);
//        }
//        poseStack.pop();
//
//        renderState.addGeckolibData(CustomDataTickets.IS_EYE_BONE, false);
//    }

//    public boolean isUsingEyeTexture(R renderState){
//        return renderState.hasGeckolibData(CustomDataTickets.USE_EYE_TEXTURE) && Boolean.TRUE.equals(renderState.getGeckolibData(CustomDataTickets.USE_EYE_TEXTURE));
//    }
//    public boolean isEyeBone(R renderState){
//        return renderState.hasGeckolibData(CustomDataTickets.IS_EYE_BONE) && Boolean.TRUE.equals(renderState.getGeckolibData(CustomDataTickets.IS_EYE_BONE));
//    }

}
