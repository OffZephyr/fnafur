package net.zephyr.fnafur.entity.animatronic;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.command.RenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.util.CustomDataTickets;
import net.zephyr.fnafur.util.jsonReaders.animatronics.AnimatronicDataHandler;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.layer.CustomBoneTextureGeoLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtil;

import java.util.ArrayList;
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

//        for(String boneName : EYE_BONE_NAME_TEXTURE_LIST){
//            System.out.println("TEST2 " + getGeoModel().getAnimationProcessor().getRegisteredBones().size());
//            for(GeoBone bone : getGeoModel().getAnimationProcessor().getRegisteredBones()) {
//                System.out.println(boneName + " ?= " + bone.getName());
//                if (bone.getName().contains(boneName)) {
//                    this.withRenderLayer(new CustomBoneTextureGeoLayer<>(this, bone.getName(), Identifier.of("")) {
//                        @Override
//                        protected @Nullable Identifier getTextureResource(R renderState) {
//                            return renderState.getGeckolibData(CustomDataTickets.EYE_TEXTURE);
//                        }
//                    });
//                }
//            }
//        }

        withRenderLayer(new AnimatronicEyeLayer<>(this, EYE_BONE_NAME_TEXTURE_LIST));
        //addRenderLayer(new AnimatronicColoredLayer<>(this));
        //addRenderLayer(new AnimatronicEyeLayer<>(this));
        //addRenderLayer(new AnimatronicColoredEyeLayer<>(this));
    }

    @Override
    public void render(R renderState, MatrixStack poseStack, OrderedRenderCommandQueue renderTasks, CameraRenderState cameraState) {

        float scale = 1;
        if(Boolean.TRUE.equals(renderState.getGeckolibData(CustomDataTickets.IS_ENTITY_PREVIEW))){
            scale = AnimatronicDataHandler.getPreviewScale(renderState.getGeckolibData(CustomDataTickets.ENTITY_CHARA), renderState.getGeckolibData(CustomDataTickets.ENTITY_ALT));

        }
        poseStack.push();
        poseStack.scale(scale, scale, scale);
        this.renderTasks = renderTasks;
        super.render(renderState, poseStack, renderTasks, cameraState);
        poseStack.pop();
    }

    @Override
    public void buildRenderTask(R renderState, MatrixStack poseStack, BakedGeoModel model, RenderCommandQueue renderTasks, CameraRenderState cameraState, @Nullable RenderLayer renderType, int packedLight, int packedOverlay, int renderColor) {
        super.buildRenderTask(renderState, poseStack, model, renderTasks, cameraState, renderType, packedLight, packedOverlay, renderColor);
    }

    @Override
    public void addRenderData(T animatable, Void relatedObject, R renderState, float partialTick) {
        super.addRenderData(animatable, relatedObject, renderState, partialTick);
    }

    @Override
    public R fillRenderState(T animatable, Void relatedObject, R renderState, float partialTick) {

        super.fillRenderState(animatable, relatedObject, renderState, partialTick);

        if(animatable.isMenu){
            renderState.addGeckolibData(DataTickets.PACKED_LIGHT, LightmapTextureManager.MAX_LIGHT_COORDINATE);
            renderState.addGeckolibData(DataTickets.PACKED_OVERLAY, OverlayTexture.DEFAULT_UV);
            renderState.addGeckolibData(DataTickets.RENDER_COLOR, 0xFFFFFFFF);
        }

        renderState.addGeckolibData(CustomDataTickets.IS_ENTITY_PREVIEW, animatable.isMenu);
        renderState.addGeckolibData(CustomDataTickets.ENTITY_CHARA, animatable.getChara());
        renderState.addGeckolibData(CustomDataTickets.ENTITY_ALT, animatable.getAlt());
        renderState.addGeckolibData(CustomDataTickets.ENTITY_EYES, animatable.getEyes());
        renderState.addGeckolibData(CustomDataTickets.ENTITY_DATA, ((IEntityDataSaver)animatable).getPersistentData());
        renderState.addGeckolibData(CustomDataTickets.TEXTURE, animatable.getTexture(animatable.getEntityWorld()));
        renderState.addGeckolibData(CustomDataTickets.EYE_TEXTURE, animatable.getEyeTexture(animatable.getEntityWorld()));
        renderState.addGeckolibData(CustomDataTickets.EYE_MAP_TEXTURE, animatable.getEyeMapTexture(animatable.getEntityWorld()));
        renderState.addGeckolibData(CustomDataTickets.SUIT_MAP_TEXTURE, animatable.getEyeTexture(animatable.getEntityWorld()));
        renderState.addGeckolibData(CustomDataTickets.EYE_NONE, animatable.isEmptyEye());
        renderState.addGeckolibData(CustomDataTickets.MODEL, animatable.getModel(animatable.getEntityWorld()));
        renderState.addGeckolibData(CustomDataTickets.RE_RENDER_TEXTURE, animatable.getReRenderTexture(animatable.getEntityWorld()));
        renderState.addGeckolibData(CustomDataTickets.RE_RENDER_MODEL, animatable.getReRenderModel(animatable.getEntityWorld()));
        renderState.addGeckolibData(CustomDataTickets.RENDER_LAYER, animatable.getRenderType(animatable.getTexture(animatable.getEntityWorld())));

        return renderState;
    }

    @Override
    public void renderBone(R renderState, MatrixStack poseStack, GeoBone bone, VertexConsumer buffer, CameraRenderState cameraState, int packedLight, int packedOverlay, int renderColor) {
        boolean hide = isUsingEyeTexture(renderState);

        if(!isEyeBone(renderState)) {
            for (String eyeBoneName : EYE_BONE_NAME_TEXTURE_LIST) {
                if (bone.getName().contains(eyeBoneName)) {
                    renderState.addGeckolibData(CustomDataTickets.IS_EYE_BONE, true);
                    break;
                }
            }
        }
        else {
            if (renderState.getGeckolibData(CustomDataTickets.EYE_NONE).booleanValue()) return;
            hide = !hide;
        }

        poseStack.push();
        RenderUtil.translateMatrixToBone(poseStack, bone);
        RenderUtil.translateToPivotPoint(poseStack, bone);
        RenderUtil.rotateMatrixAroundBone(poseStack, bone);
        RenderUtil.scaleMatrixForBone(poseStack, bone);

        if (bone.isTrackingMatrices()) {
            Matrix4f poseState = new Matrix4f(poseStack.peek().getPositionMatrix());
            Matrix4f localMatrix = RenderUtil.invertAndMultiplyMatrices(poseState, renderState.getGeckolibData(DataTickets.OBJECT_RENDER_POSE));

            bone.setModelSpaceMatrix(RenderUtil.invertAndMultiplyMatrices(poseState, renderState.getGeckolibData(DataTickets.MODEL_RENDER_POSE)));
            bone.setLocalSpaceMatrix(RenderUtil.translateMatrix(localMatrix, getPositionOffset(renderState).toVector3f()));
            bone.setWorldSpaceMatrix(RenderUtil.translateMatrix(new Matrix4f(localMatrix), new Vector3f((float)renderState.x, (float)renderState.y, (float)renderState.z)));
        }

        RenderUtil.translateAwayFromPivotPoint(poseStack, bone);

        if(!hide){
            renderCubesOfBone(renderState, bone, poseStack, buffer, cameraState, packedLight, packedOverlay, renderColor);
        }

        if(isEyeBone(renderState)){
            for(GeoBone childBone : bone.getChildBones()){
                if(childBone.getParent() == bone){
                    renderState.addGeckolibData(CustomDataTickets.IS_EYE_BONE, true);
                    renderBone(renderState, poseStack, childBone, buffer, cameraState, packedLight, packedOverlay, renderColor);
                }
            }
        }
        else {
            renderChildBones(renderState, bone, poseStack, buffer, cameraState, packedLight, packedOverlay, renderColor);
        }
        poseStack.pop();

        renderState.addGeckolibData(CustomDataTickets.IS_EYE_BONE, false);
    }

    public boolean isUsingEyeTexture(R renderState){
        return renderState.hasGeckolibData(CustomDataTickets.USE_EYE_TEXTURE) && Boolean.TRUE.equals(renderState.getGeckolibData(CustomDataTickets.USE_EYE_TEXTURE));
    }
    public boolean isEyeBone(R renderState){
        return renderState.hasGeckolibData(CustomDataTickets.IS_EYE_BONE) && Boolean.TRUE.equals(renderState.getGeckolibData(CustomDataTickets.IS_EYE_BONE));
    }

}
