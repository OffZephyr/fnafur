package net.zephyr.fnafur.entity.animatronic;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.zephyr.fnafur.entity.animatronic.data.PartType;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.RenderUtil;

import java.util.*;

public class AnimatronicRenderer<T extends AnimatronicEntity> extends GeoEntityRenderer<T> {
    public String character = "cl_fred";
    public String character_prev = "cl_fred";
    public static final Map<String, String> BodyMap = new HashMap<>();
    public String[] characterList = {"cl_fred", "cl_bon", "cl_foxy"};
    T entity;
    int popAmount = 0;
    int subPopAmount = 0;
    public AnimatronicRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new AnimatronicModel<>());
        BodyMap.put("torso", "torso_main");
        BodyMap.put("torso", "torso_main");
    }

    @Override
    public void updateRenderState(T entity, @Nullable EntityRenderState entityRenderState, float partialTick) {
        this.entity = entity;
        super.updateRenderState(entity, entityRenderState, partialTick);
    }

    @Override
    public void render(EntityRenderState entityRenderState, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        super.render(entityRenderState, poseStack, bufferSource, packedLight);
    }

    @Override
    public void preRender(MatrixStack poseStack, T animatable, BakedGeoModel model, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int renderColor) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, renderColor);
    }

    @Override
    public void renderRecursively(MatrixStack poseStack, T animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int renderColor) {

        //super.renderRecursively(poseStack, animatable, bone ,renderType, bufferSource, buffer, isReRender, partialTick ,packedLight, packedOverlay, renderColor);

        //if(true) return;

        //TODO ADD MISSING PARTS

        popAmount = 0;
        this.character = "cl_fred";
        this.character_prev = this.character;
        poseStack.push();

        String animationCharacter = this.character;

        GeoBone animationBase = getAnimationBone("Root");
        GeoBone root = updateAnimationBone(animationBase, getModelBone("Root", animationCharacter));

        GeoBone pelvis = renderPart("pelvis", "cl_fred", this.character_prev, root, poseStack, animatable, bufferSource, buffer, isReRender, false, false, false, partialTick, packedLight, packedOverlay, renderColor);

        GeoBone torso = renderPart("torso", "endo_01", this.character_prev, pelvis, poseStack, animatable, bufferSource, buffer, isReRender, false, false, false, partialTick, packedLight, packedOverlay, renderColor);
        renderPart("torso_accessory", "cl_fred", this.character_prev, torso, poseStack, animatable, bufferSource, buffer, isReRender, false, true, false, partialTick, packedLight, packedOverlay, renderColor);

        GeoBone head = renderPart("head", "endo_01", this.character_prev, torso, poseStack, animatable, bufferSource, buffer, isReRender, false, false, false, partialTick, packedLight, packedOverlay, renderColor);
        renderPart("jaw", "cl_fred", this.character_prev, head, poseStack, animatable, bufferSource, buffer, isReRender, false, true, false, partialTick, packedLight, packedOverlay, renderColor);
        renderPart("left_ear", "cl_fred", this.character_prev, head, poseStack, animatable, bufferSource, buffer, isReRender, false, true, false, partialTick, packedLight, packedOverlay, renderColor);
        renderPart("right_ear", "cl_fred", this.character_prev, head, poseStack, animatable, bufferSource, buffer, isReRender, false, true, false, partialTick, packedLight, packedOverlay, renderColor);
        renderPart("head_accessory", "cl_fred", this.character_prev, head, poseStack, animatable, bufferSource, buffer, isReRender, false, true, false, partialTick, packedLight, packedOverlay, renderColor);

        backTrack(poseStack, 1);

        GeoBone left_arm = renderPart("left_arm", "cl_fred", this.character_prev, torso, poseStack, animatable, bufferSource, buffer, isReRender, false, false, true, partialTick, packedLight, packedOverlay, renderColor);
        GeoBone left_hand = renderPart("left_hand", "endo_01", this.character_prev, left_arm, poseStack, animatable, bufferSource, buffer, isReRender, false, false, false, partialTick, packedLight, packedOverlay, renderColor);
        renderPart("left_hand_accessory", "cl_fred", this.character_prev, left_hand, poseStack, animatable, bufferSource, buffer, isReRender, false, true, false, partialTick, packedLight, packedOverlay, renderColor);

        subBackTrack(poseStack, subPopAmount);
        backTrack(poseStack, 2);

        GeoBone right_arm = renderPart("right_arm", "endo_01", this.character_prev, torso, poseStack, animatable, bufferSource, buffer, isReRender, false, false, true, partialTick, packedLight, packedOverlay, renderColor);
        GeoBone right_hand = renderPart("right_hand", "cl_fred", this.character_prev, right_arm, poseStack, animatable, bufferSource, buffer, isReRender, false, false, false, partialTick, packedLight, packedOverlay, renderColor);
        renderPart("right_hand_accessory", "cl_fred", this.character_prev, right_hand, poseStack, animatable, bufferSource, buffer, isReRender, false, true, false, partialTick, packedLight, packedOverlay, renderColor);

        subBackTrack(poseStack, subPopAmount);
        backTrack(poseStack, 4);

        GeoBone right_leg = renderPart("right_leg", "endo_01", this.character_prev, root, poseStack, animatable, bufferSource, buffer, isReRender, false, false, true, partialTick, packedLight, packedOverlay, renderColor);
        renderPart("right_foot", "cl_fred", this.character_prev, right_leg, poseStack, animatable, bufferSource, buffer, isReRender, false, true, false, partialTick, packedLight, packedOverlay, renderColor);

        subBackTrack(poseStack, subPopAmount);
        backTrack(poseStack, 1);

        GeoBone left_leg = renderPart("left_leg", "cl_fred", this.character_prev, root, poseStack, animatable, bufferSource, buffer, isReRender, false, false, true, partialTick, packedLight, packedOverlay, renderColor);
        renderPart("left_foot", "endo_01", this.character_prev, left_leg, poseStack, animatable, bufferSource, buffer, isReRender, false, true, false, partialTick, packedLight, packedOverlay, renderColor);

        subBackTrack(poseStack, subPopAmount);
        backTrack(poseStack, 1);

        for(int i = 0; i < popAmount; i++){
            poseStack.pop();
        }

        poseStack.pop();
    }

    MatrixStack backTrack(MatrixStack poseStack, int amount){
        for(int i = 0; i < amount; i++){
            poseStack.pop();
        }
        popAmount -= amount;
        return poseStack;
    }
    MatrixStack subBackTrack(MatrixStack poseStack, int amount){
        for(int i = 0; i < amount; i++){
            poseStack.pop();
        }
        subPopAmount -= amount;
        return poseStack;
    }

    public GeoBone renderPart(String boneName, String character, String previousCharacter, GeoBone previousBone, MatrixStack poseStack, T animatable, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, boolean isSubPart, boolean isEnd, boolean isLimb, float partialTick, int packedLight,
                           int packedOverlay, int renderColor) {

        if(Objects.equals(character, "null")){
            this.character_prev = character;
            return previousBone;
        }
        poseStack.push();
        this.character = character;
        GeoBone animationBone = getAnimationBone(boneName);
        GeoBone modelBone = updateAnimationBone(animationBone, getModelBone(boneName, character));
        GeoBone connectorBone = getConnectorBone(boneName, previousCharacter);

        GeoBone pivotBone = connectorBone != null && !isSubPart ? connectorBone : modelBone;

        RenderUtil.translateMatrixToBone(poseStack, pivotBone);
        RenderUtil.translateToPivotPoint(poseStack, pivotBone);
        RenderUtil.rotateMatrixAroundBone(poseStack, modelBone);
        RenderUtil.scaleMatrixForBone(poseStack, modelBone);

        if (modelBone.isTrackingMatrices()) {
            Matrix4f poseState = new Matrix4f(poseStack.peek().getPositionMatrix());
            Matrix4f localMatrix = RenderUtil.invertAndMultiplyMatrices(poseState, this.entityRenderTranslations);

            modelBone.setModelSpaceMatrix(RenderUtil.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
            modelBone.setLocalSpaceMatrix(RenderUtil.translateMatrix(localMatrix, getPositionOffset(getEntityRenderState()).toVector3f()));
            modelBone.setWorldSpaceMatrix(RenderUtil.translateMatrix(new Matrix4f(localMatrix), this.animatable.getPos().toVector3f()));
        }

        RenderUtil.translateAwayFromPivotPoint(poseStack, modelBone);

        this.character = character;
        VertexConsumer renderLayer = bufferSource.getBuffer(RenderLayer.getEntityTranslucent(getGeoModel().getTextureResource(animatable, this)));

        renderCubesOfBone(poseStack, modelBone, renderLayer, packedLight, packedOverlay, renderColor);

        this.character = AnimatronicModel.DEFAULT_CHARACTER;

        GeoBone returnBone = renderChildBonesOfPart(character, poseStack, animatable, modelBone, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, renderColor, isLimb);

        if(isSubPart){
            if(isLimb)
                subPopAmount++;
            else
                poseStack.pop();
        }
        else if(isEnd) {
            popAmount ++;
            backTrack(poseStack, 1);
        }

        if(!isSubPart && !isEnd) {
            popAmount++;
            this.character_prev = character;
        }

        return returnBone;
    }

    GeoBone renderChildBonesOfPart(String character, MatrixStack poseStack, T animatable, GeoBone bone, VertexConsumerProvider bufferSource, VertexConsumer buffer,
                                boolean isReRender, float partialTick, int packedLight, int packedOverlay, int renderColor, boolean isLimb) {
        if (bone.isHidingChildren())
            return bone;

        GeoBone returnBone = bone;

        for (GeoBone childBone : bone.getChildBones()) {

            boolean shouldContinue = true;
            for (PartType type : PartType.values()) {
                if (Objects.equals(childBone.getName(), type.NAME)) shouldContinue = false;
            }

            if (shouldContinue) {
                returnBone = renderPart(childBone.getName(), character, character, bone, poseStack, animatable, bufferSource, buffer, isReRender, true, false, isLimb, partialTick, packedLight, packedOverlay, renderColor);
            }
        }
        return isLimb ? returnBone : bone;
    }

    public GeoBone getConnectorBone(String boneName, String character_prev) {
        return getModelBone(boneName, character_prev);
    }
    public GeoBone getAnimationBone(String boneName) {
        return getModelBone(boneName, AnimatronicModel.DEFAULT_CHARACTER);
    }
    public GeoBone getModelBone(String boneName) {
        return getModelBone(boneName, this.character);
    }
    public GeoBone getModelBone(String boneName, String character) {
        this.character = character;
        Optional<GeoBone> optional = getGeoModel().getBakedModel(getGeoModel().getModelResource(animatable, this)).getBone(boneName);

        return optional.orElse(null);
    }

    @Override
    public GeoModel<T> getGeoModel() {
        ((AnimatronicModel<T>)this.model).character = this.character;
        return super.getGeoModel();
    }

    public GeoBone updateAnimationBone(GeoBone animationBone, GeoBone modelBone) {
        if(animationBone != null) {
            modelBone.setModelSpaceMatrix(animationBone.getModelSpaceMatrix());
            modelBone.setRotX(animationBone.getRotX());
            modelBone.setRotY(animationBone.getRotY());
            modelBone.setRotZ(animationBone.getRotZ());
            modelBone.setRotX(animationBone.getRotX());
            modelBone.setRotY(animationBone.getRotY());
            modelBone.setRotZ(animationBone.getRotZ());
        }
        return modelBone;
    }
}
