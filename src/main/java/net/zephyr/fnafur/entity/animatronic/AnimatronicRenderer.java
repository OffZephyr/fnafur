package net.zephyr.fnafur.entity.animatronic;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.entity.animatronic.data.PartType;
import net.zephyr.fnafur.util.jsonReaders.character_models.CharacterModelManager;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.RenderUtil;

import java.util.*;

public class AnimatronicRenderer<T extends AnimatronicEntity> extends GeoEntityRenderer<T> {
    Map<String, GeoBone> boneMap = new HashMap<>();
    String previous = "root";
    int layer = 0;
    public String character = "cl_fred";
    public String character_prev = "cl_fred";
    public static final Map<PartType, String> AccessoryMap = Map.ofEntries(
            Map.entry(PartType.TORSO_ACCESSORY, "cl_fred"),
            Map.entry(PartType.HEAD_ACCESSORY, "cl_fred"),
            Map.entry(PartType.LEFT_HAND_ACCESSORY, "cl_fred"),
            Map.entry(PartType.RIGHT_HAND_ACCESSORY, "cl_fred"),
            Map.entry(PartType.LEFT_EYE, "endo01_eye"),
            Map.entry(PartType.LEFT_EYELID_TOP, "endo01_eye"),
            Map.entry(PartType.LEFT_EYELID_BOTTOM, "endo01_eye"),
            Map.entry(PartType.RIGHT_EYE, "endo01_eye"),
            Map.entry(PartType.RIGHT_EYELID_TOP, "endo01_eye"),
            Map.entry(PartType.RIGHT_EYELID_BOTTOM, "endo01_eye"),
            Map.entry(PartType.GUITAR, "cl_fred")
    );
    public static final Map<PartType, String> ModelMap = Map.ofEntries(
            Map.entry(PartType.ROOT, "cl_fred"),
            Map.entry(PartType.PELVIS, "cl_fred"),
            Map.entry(PartType.TORSO, "cl_fred"),
            Map.entry(PartType.TORSO_ACCESSORY, "cl_fred"),
            Map.entry(PartType.GUITAR, "cl_fred"), // TODO GUITARS
            Map.entry(PartType.HEAD, "cl_fred"),
            Map.entry(PartType.JAW, "cl_fred"),
            Map.entry(PartType.LEFT_EAR, "cl_fred"),
            Map.entry(PartType.RIGHT_EAR, "cl_fred"),
            Map.entry(PartType.HEAD_ACCESSORY, "cl_fred"), // TODO HEAD ACCESSORIES
            Map.entry(PartType.LEFT_EYE, "endo01_eye"),
            Map.entry(PartType.LEFT_EYELID_TOP, "endo01_eye"),
            Map.entry(PartType.LEFT_EYELID_BOTTOM, "endo01_eye"),
            Map.entry(PartType.RIGHT_EYE, "endo01_eye"),
            Map.entry(PartType.RIGHT_EYELID_TOP, "endo01_eye"),
            Map.entry(PartType.RIGHT_EYELID_BOTTOM, "endo01_eye"),
            Map.entry(PartType.LEFT_ARM, "cl_fred"),
            Map.entry(PartType.LEFT_LOWER_ARM, "cl_fred"),
            Map.entry(PartType.LEFT_HAND, "cl_fred"),
            Map.entry(PartType.LEFT_HAND_ACCESSORY, "cl_fred"), // TODO HAND ACCESSORIES
            Map.entry(PartType.RIGHT_ARM, "cl_fred"),
            Map.entry(PartType.RIGHT_LOWER_ARM, "cl_fred"),
            Map.entry(PartType.RIGHT_HAND, "cl_fred"),
            Map.entry(PartType.RIGHT_HAND_ACCESSORY, "cl_fred"), // TODO HAND ACCESSORIES
            Map.entry(PartType.LEFT_LEG, "cl_fred"),
            Map.entry(PartType.LEFT_LOWER_LEG, "cl_fred"),
            Map.entry(PartType.LEFT_FOOT, "cl_fred"),
            Map.entry(PartType.RIGHT_LEG, "cl_fred"),
            Map.entry(PartType.RIGHT_LOWER_LEG, "cl_fred"),
            Map.entry(PartType.RIGHT_FOOT, "cl_fred")
    );
    public Identifier currentModel = CharacterModelManager.DEFAULT_MODEL;
    public Identifier prevModel = CharacterModelManager.DEFAULT_MODEL;

    public static Identifier getTexture(String category, String character, String texture){
        return Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/" + category + "/" + character + "/" + texture + ".png");
    }
    public static Identifier getModel(String category, String character, String texture){
        return Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/" + category + "/" + character + "/" + texture + ".png");
    }
    public static Identifier getGeo(String category, String character, String texture){
        return Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/" + category + "/" + character + "/" + texture + ".png");
    }
    T entity;
    int popAmount = 0;
    int subPopAmount = 0;
    public AnimatronicRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new AnimatronicModel<>());
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

        popAmount = 0;
        this.currentModel = CharacterModelManager.getVariantModel(CharacterModelManager.ALL_CHARACTERS.get(ModelMap.get(PartType.PELVIS)), "default");
        this.prevModel = currentModel;
        poseStack.push();

        String animationCharacter = ModelMap.get(PartType.PELVIS);

        GeoBone animationBase = getAnimationBone(PartType.ROOT.NAME);
        GeoBone root = updateAnimationBone(animationBase, getModelBone(PartType.ROOT.NAME, CharacterModelManager.ALL_CHARACTERS.get(animationCharacter), "default"));

        //GeoBone root = renderPart(PartType.ROOT, root0, poseStack, animatable, bufferSource, buffer, isReRender, false, true, false, partialTick, packedLight, packedOverlay, renderColor);

        String variant = "default";

        double right_leg_height = 0;
        double left_leg_height = 0;
        double pelvis_height = 0;
        double root_height = getModelBone(PartType.ROOT.NAME, CharacterModelManager.ALL_CHARACTERS.get(ModelMap.get(PartType.PELVIS)), variant).getModelPosition().y();

        if(!Objects.equals(ModelMap.get(PartType.RIGHT_LEG), "null")){
            right_leg_height = getModelBone(PartType.RIGHT_LEG.NAME, CharacterModelManager.ALL_CHARACTERS.get(ModelMap.get(PartType.PELVIS)), variant).getModelPosition().y();
            right_leg_height = right_leg_height == 0 ? 0 : right_leg_height - root_height;
        }
        if(!Objects.equals(ModelMap.get(PartType.LEFT_LEG), "null")){
            left_leg_height = getModelBone(PartType.LEFT_LEG.NAME, CharacterModelManager.ALL_CHARACTERS.get(ModelMap.get(PartType.PELVIS)), variant).getModelPosition().y();
            left_leg_height = left_leg_height == 0 ? 0 : left_leg_height - root_height;
        }
        if(!Objects.equals(ModelMap.get(PartType.PELVIS), "null")){
            pelvis_height = getModelBone(PartType.PELVIS.NAME, CharacterModelManager.ALL_CHARACTERS.get(ModelMap.get(PartType.PELVIS)), variant).getModelPosition().y();
            pelvis_height = pelvis_height == 0 ? 0 : pelvis_height - root_height;
        }

        //poseStack.translate(0, -pelvis_height, 0);
        double legAmount = Math.max(right_leg_height, left_leg_height);
        //poseStack.translate(0, legAmount, 0);

        boneMap.put("root", root);
        previous = "root";

        for(String boneName : CharacterModelManager.MAP_DATA.get("default").keySet()){
            renderBones(boneName, CharacterModelManager.MAP_DATA.get("default").get(boneName), poseStack, animatable, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, renderColor);
        }

        poseStack.pop();
    }

    public void renderBones(String boneName, CharacterModelManager.MapEntry entry,MatrixStack poseStack, T animatable, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int renderColor){

        poseStack.push();
        layer++;
        boneMap.put(boneName, renderPart(PartType.getFromString(boneName), boneMap.get(previous), poseStack, animatable, bufferSource, buffer, isReRender, false, entry.isAccessory(), entry.isLimb(), partialTick, packedLight, packedOverlay, renderColor));

        if(!entry.sub().isEmpty()){

            previous = boneName;

            for(Map<String, CharacterModelManager.MapEntry> map : entry.sub()){
                for(String name : map.keySet()){
                    renderBones(name, map.get(name), poseStack, animatable, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, renderColor);
                }
            }
        }
        layer--;
        poseStack.pop();
    }

    void backTrack(MatrixStack poseStack, int amount){
        for(int i = 0; i < amount; i++){
            poseStack.pop();
        }
        popAmount -= amount;
    }
    void subBackTrack(MatrixStack poseStack, int amount){
        for(int i = 0; i < amount; i++){
            poseStack.pop();
        }
        subPopAmount -= amount;
    }

    public GeoBone renderPart(PartType part, GeoBone previousBone, MatrixStack poseStack, T animatable, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, boolean isSubPart, boolean isAccessory, boolean isLimb, float partialTick, int packedLight,
                              int packedOverlay, int renderColor) {
        return renderPart(part, part.NAME, previousBone, poseStack, animatable, bufferSource, buffer, isReRender, isSubPart, isAccessory, isLimb, partialTick, packedLight, packedOverlay, renderColor);
    }

    public GeoBone renderPart(PartType part, String modelBoneName, GeoBone previousBone, MatrixStack poseStack, T animatable, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, boolean isSubPart, boolean isAccessory, boolean isLimb, float partialTick, int packedLight,
                              int packedOverlay, int renderColor) {

        String boneName = part.NAME;
        String modelName = ModelMap.getOrDefault(part, "null");

        if(Objects.equals(modelName, "null")) {
            this.prevModel = this.currentModel;
            return previousBone;
        }

        String prevModelName = part.PREVIOUS == null ? AnimatronicModel.DEFAULT_CHARACTER : ModelMap.get(part.PREVIOUS);

        return renderPart(boneName, modelName, prevModelName, modelBoneName, previousBone, poseStack, animatable, bufferSource, buffer, isReRender, isSubPart, isAccessory, isLimb, partialTick, packedLight, packedOverlay, renderColor);
    }
    public GeoBone renderPart(String boneName, String modelName, String prevModelName, String modelBoneName, GeoBone previousBone, MatrixStack poseStack, T animatable, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, boolean isSubPart, boolean isAccessory, boolean isLimb, float partialTick, int packedLight,
                           int packedOverlay, int renderColor) {

        if(isSubPart) poseStack.push();

        String variant = "default";
        String prevVariant = "default";
        String textureVariant = "default";

        CharacterModelManager.Chara character = CharacterModelManager.ALL_CHARACTERS.get(modelName);
        String Accessory = isAccessory ? AccessoryMap.getOrDefault(PartType.getFromString(boneName), "null") : "null";

        List<String> layerNames = isAccessory ? CharacterModelManager.getAccessoryVariantNames(boneName, Accessory) : CharacterModelManager.getVariantNames(character, variant);

        //poseStack.multiplyPositionMatrix(previousBone.getModelSpaceMatrix());
        //poseStack.push();

        GeoBone animationBone = getAnimationBone(boneName);

        CharacterModelManager.Chara chara = character;
        Identifier model = isAccessory ? CharacterModelManager.getAccessoryModel(boneName, Accessory) : CharacterModelManager.getVariantModel(chara, variant);
        if(model != null) this.currentModel = model;

        GeoBone modelBone = updateAnimationBone(animationBone, getModelBone(modelBoneName, character, variant));

        chara = CharacterModelManager.ALL_CHARACTERS.get(prevModelName);
        model = CharacterModelManager.getVariantModel(chara, variant);
        if(model != null) this.currentModel = model;

        GeoBone connectorBone = getConnectorBone(boneName, CharacterModelManager.ALL_CHARACTERS.get(prevModelName), prevVariant);

        chara = character;
        model = isAccessory ? CharacterModelManager.getAccessoryModel(boneName, Accessory) : CharacterModelManager.getVariantModel(chara, variant);
        if(model != null) this.currentModel = model;

        GeoBone pivotBone = connectorBone != null && !isSubPart ? connectorBone : modelBone;

        if(pivotBone == null) {
            poseStack.pop();
            return previousBone;
        }

        modelBone = isAccessory && !boneName.contains("eye") ? updateAnimationBone(connectorBone, getModelBone(modelBoneName, character, variant)) : modelBone;

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

        for(String layerName : layerNames) {
            this.currentModel = CharacterModelManager.getVariantModel(character, variant);
            CharacterModelManager.ModelEntry.Texture textureData = isAccessory ? CharacterModelManager.getAccessoryTexture(boneName, Accessory, layerName, textureVariant) : CharacterModelManager.getVariantTexture(character, variant, layerName, textureVariant);

            if(textureData != null) {
                if (textureData.can_be_recolored() != null && textureData.can_be_recolored()) {
                    if (textureData.color_texture() != null && !textureData.color_texture().isEmpty()) {
                        Identifier texture = Identifier.of(FnafUniverseResuited.MOD_ID, textureData.color_texture());
                        VertexConsumer renderLayer = bufferSource.getBuffer(RenderLayer.getEntityTranslucent(texture));

                        renderCubesOfBone(poseStack, modelBone, renderLayer, packedLight, packedOverlay, renderColor);
                    }
                    if (textureData.color_emissive() != null && !textureData.color_emissive().isEmpty()) {
                        Identifier texture = Identifier.of(FnafUniverseResuited.MOD_ID, textureData.color_emissive());
                        VertexConsumer renderLayer = bufferSource.getBuffer(RenderLayer.getEyes(texture));

                        renderCubesOfBone(poseStack, modelBone, renderLayer, packedLight, packedOverlay, renderColor);
                    }
                }
                if (textureData.overlay_texture() != null && !textureData.overlay_texture().isEmpty()) {
                    Identifier texture = Identifier.of(FnafUniverseResuited.MOD_ID, textureData.overlay_texture());
                    //System.out.println(texture.getPath());
                    VertexConsumer renderLayer = bufferSource.getBuffer(RenderLayer.getEntityTranslucent(texture));

                    renderCubesOfBone(poseStack, modelBone, renderLayer, packedLight, packedOverlay, renderColor);
                }
                if (textureData.overlay_emissive() != null && !textureData.overlay_emissive().isEmpty()) {
                    Identifier texture = Identifier.of(FnafUniverseResuited.MOD_ID, textureData.overlay_emissive());
                    VertexConsumer renderLayer = bufferSource.getBuffer(RenderLayer.getEyes(texture));

                    renderCubesOfBone(poseStack, modelBone, renderLayer, packedLight, packedOverlay, renderColor);
                }
            }
        }
        this.currentModel = CharacterModelManager.DEFAULT_MODEL;

        GeoBone returnBone = renderChildBonesOfPart(modelName, modelBoneName, poseStack, animatable, modelBone, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, renderColor, isLimb);
        if(isSubPart) poseStack.pop();
        return returnBone;
    }

    GeoBone renderChildBonesOfPart(String modelName, String modelBoneName, MatrixStack poseStack, T animatable, GeoBone bone, VertexConsumerProvider bufferSource, VertexConsumer buffer,
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
                returnBone = renderPart(childBone.getName(), modelName, modelName, childBone.getName(), bone, poseStack, animatable, bufferSource, buffer, isReRender, true, false, isLimb, partialTick, packedLight, packedOverlay, renderColor);
            }
        }
        return isLimb ? returnBone : bone;
    }

    public GeoBone getConnectorBone(String boneName, CharacterModelManager.Chara character_prev, String variant) {
        return getModelBone(boneName, character_prev, variant);
    }
    public GeoBone getAnimationBone(String boneName) {
        return getModelBone(boneName, CharacterModelManager.ALL_CHARACTERS.get("endo_01"), "default");
    }
    public GeoBone getModelBone(String boneName, CharacterModelManager.Chara character, String variant) {
        ((AnimatronicModel<T>)this.model).currentModel = CharacterModelManager.getVariantModel(character, variant);
        Optional<GeoBone> optional = getGeoModel().getBakedModel(this.currentModel).getBone(boneName);

        return optional.orElse(null);
    }

    @Override
    public GeoModel<T> getGeoModel() {
        ((AnimatronicModel<T>)this.model).currentModel = this.currentModel;
        return super.getGeoModel();
    }

    public GeoBone updateAnimationBone(GeoBone animationBone, GeoBone modelBone) {
        if(animationBone != null && modelBone != null) {
            //modelBone.setModelSpaceMatrix(animationBone.getModelSpaceMatrix());
            modelBone.setRotX(animationBone.getRotX());
            modelBone.setRotY(animationBone.getRotY());
            modelBone.setRotZ(animationBone.getRotZ());
        }
        return modelBone;
    }

    public enum LayerType{
        BOTTOM_LAYER,
        EMISSIVE

    }
}
