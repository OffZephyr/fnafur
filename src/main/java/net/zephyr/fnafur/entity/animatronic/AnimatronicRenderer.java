package net.zephyr.fnafur.entity.animatronic;

import net.minecraft.client.render.*;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.zephyr.fnafur.client.CustomRenderingPipelines;
import net.zephyr.fnafur.entity.animatronic.voice.EntityVoiceSoundInstance;
import net.zephyr.fnafur.util.CustomDataTickets;
import net.zephyr.fnafur.util.jsonReaders.animatronics.AnimatronicDataHandler;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.model.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.BoneSnapshots;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.RenderPassInfo;

import java.util.List;

public class AnimatronicRenderer<T extends AnimatronicEntity, R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<T, R> {
    private final List<String> EYE_BONE_NAME_TEXTURE_LIST = List.of(
         "eyeleft",
         "eyeright"
    );    private final List<String> EYE_BONE_NAME_NONE_LIST = List.of(
         "eyeleft",
         "eyeright"
    );
    OrderedRenderCommandQueue renderTasks;
    VertexConsumer buffer2;

    public AnimatronicRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new AnimatronicModel<>());
    }

    @Override
    public void render(R renderState, MatrixStack poseStack, OrderedRenderCommandQueue renderTasks, CameraRenderState cameraState) {

        float scale = 1;
        if(Boolean.TRUE.equals(renderState.getGeckolibData(CustomDataTickets.IS_ENTITY_PREVIEW)) && renderState.hasGeckolibData(CustomDataTickets.RENDER_SCALE)){
            scale = renderState.getGeckolibData(CustomDataTickets.RENDER_SCALE);
        }
        poseStack.push();
        poseStack.scale(scale, scale, scale);
        this.renderTasks = renderTasks;
        super.render(renderState, poseStack, renderTasks, cameraState);
        poseStack.pop();
    }

    @Override
    public void addRenderData(T animatable, Void relatedObject, R renderState, float partialTick) {
        super.addRenderData(animatable, relatedObject, renderState, partialTick);
    }

    @Override
    public R fillRenderState(T animatable, Void relatedObject, R renderState, float partialTick) {

        if(animatable.isFrozen){
            renderState.bodyYaw = animatable.frozenBodyYaw;
//            renderState.relativeHeadYaw = MathHelper.wrapDegrees(animatable.frozenHeadYaw - renderState.bodyYaw);
            renderState.relativeHeadYaw = MathHelper.wrapDegrees(animatable.frozenHeadYaw - renderState.bodyYaw);
            renderState.pitch = (float) animatable.frozenPitch;
        }

        super.fillRenderState(animatable, relatedObject, renderState, partialTick);

        if(animatable.isMenu){
            renderState.addGeckolibData(DataTickets.PACKED_LIGHT, LightmapTextureManager.MAX_LIGHT_COORDINATE);
            renderState.addGeckolibData(DataTickets.PACKED_OVERLAY, OverlayTexture.DEFAULT_UV);
            renderState.addGeckolibData(DataTickets.RENDER_COLOR, 0xFFFFFFFF);
        }

        renderState.addGeckolibData(CustomDataTickets.IS_ENTITY_PREVIEW, animatable.isMenu);
        renderState.addGeckolibData(CustomDataTickets.RENDER_SCALE,AnimatronicDataHandler.getPreviewScale(animatable.getChara(), animatable.getAlt()));
        renderState.addGeckolibData(CustomDataTickets.TEXTURE, animatable.getTexture(animatable.getEntityWorld()));
        renderState.addGeckolibData(CustomDataTickets.EYE_TEXTURE, animatable.getEyeTexture(animatable.getEntityWorld()));
        renderState.addGeckolibData(CustomDataTickets.EYE_MAP_TEXTURE, animatable.getEyeMapTexture(animatable.getEntityWorld()));
        renderState.addGeckolibData(CustomDataTickets.EYE_GLOW_COLOR_TEXTURE, animatable.getEyesGlowColor().getIdentifier());
        renderState.addGeckolibData(CustomDataTickets.SUIT_MAP_TEXTURE, animatable.getEyeTexture(animatable.getEntityWorld()));
        renderState.addGeckolibData(CustomDataTickets.EYE_NONE, animatable.isEmptyEye());
        renderState.addGeckolibData(CustomDataTickets.EYES_GLOW, animatable.shouldEyesGlow());
        renderState.addGeckolibData(CustomDataTickets.EYES_GLOW_MODE, animatable.getEyesGlowMode());
        renderState.addGeckolibData(CustomDataTickets.MODEL, animatable.getModel(animatable.getEntityWorld()));
        renderState.addGeckolibData(CustomDataTickets.RE_RENDER_TEXTURE, animatable.getReRenderTexture(animatable.getEntityWorld()));
        renderState.addGeckolibData(CustomDataTickets.RE_RENDER_MODEL, animatable.getReRenderModel(animatable.getEntityWorld()));
        renderState.addGeckolibData(CustomDataTickets.RENDER_LAYER, animatable.getRenderType(animatable.getTexture(animatable.getEntityWorld())));

        renderState.addGeckolibData(CustomDataTickets.ANIMATRONIC_POSE, animatable.getAnimatronicPose());
        renderState.addGeckolibData(CustomDataTickets.FORCED_HEAD_YAW, MathHelper.wrapDegrees(animatable.getHeadYaw() - renderState.bodyYaw));
        renderState.addGeckolibData(CustomDataTickets.FORCED_PITCH, animatable.getPitch());
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

            float pitch = -renderPassInfo.renderState().pitch * MathHelper.RADIANS_PER_DEGREE;
            float yaw = -renderPassInfo.renderState().relativeHeadYaw * MathHelper.RADIANS_PER_DEGREE;

            if (head_main != null) {
                snapshots.get(head_main).setRotation(pitch / 2f, yaw / 4f, 0);
            }
            if (torso_main != null) {
                float halfYaw = pose == AnimatronicEntity.AnimatronicPose.CRAWLING ? 0 : yaw / 4f;
                float halfPitch = pose == AnimatronicEntity.AnimatronicPose.CRAWLING ? 0 : pitch / 4f;
                snapshots.get(torso_main).setRotation(halfPitch, halfYaw, 0);
            }

            float eyeYaw = !isFrozen ? yaw / 4f : -Math.clamp(renderPassInfo.renderState().getGeckolibData(CustomDataTickets.FORCED_HEAD_YAW), -45f, 45f) * MathHelper.RADIANS_PER_DEGREE;
            float eyePitch = !isFrozen ? pitch / 2f : -Math.clamp(renderPassInfo.renderState().getGeckolibData(CustomDataTickets.FORCED_PITCH), -30f, 30f) * MathHelper.RADIANS_PER_DEGREE;

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
    protected boolean canBeCulled(T entity) {
        return false;
    }

    @Override
    public @Nullable RenderLayer getRenderType(R renderState, Identifier texture) {
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
