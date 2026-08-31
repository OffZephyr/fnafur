package net.zephyr.fnafur.item.masks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.state.CameraRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import net.zephyr.fnafur.util.CustomDataTickets;
import net.zephyr.fnafur.util.mixinAccessing.IUniversePlayer;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.model.BakedGeoModel;
import software.bernie.geckolib.cache.model.GeoBone;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtil;

import java.util.List;

public class VanniMaskItemRenderer<T extends Item & GeoAnimatable, O, R extends GeoRenderState> extends GeoItemRenderer<T> {
    PoseStack.Pose leftStack;
    PoseStack.Pose rightStack;
    public VanniMaskItemRenderer() {
        super(new VanniMaskItemModel<>());
    }

    @Override
    public List<GeoRenderLayer<T, RenderData, GeoRenderState>> getRenderLayers() {
        return List.of(
                new VanniMaskRenderLayer<>(this)
        );
    }

//    @Override
//    public void buildRenderTask(GeoRenderState renderState, MatrixStack poseStack, BakedGeoModel model, RenderCommandQueue renderTasks, CameraRenderState cameraState, @Nullable RenderLayer renderType, int packedLight, int packedOverlay, int renderColor) {
//        renderState.addGeckolibData(CustomDataTickets.IS_RENDERING_ARMS, false);
//        super.buildRenderTask(renderState, poseStack, model, renderTasks, cameraState, renderType, packedLight, packedOverlay, renderColor);
//        if (renderType == null)
//            return;
//
//        AbstractClientPlayerEntity abstractClientPlayerEntity = Minecraft.getInstance().player;
//        Identifier identifier = abstractClientPlayerEntity.getSkin().body().texturePath();
//
//        renderTasks.submitCustom(poseStack, getRenderType(renderState, identifier), (pose, vertexConsumer) -> {
//            renderState.addGeckolibData(CustomDataTickets.IS_RENDERING_ARMS, true);
//            final MatrixStack poseStack2 = new MatrixStack();
//            final boolean skipBoneTasks = getPerBoneTasks(renderState).isEmpty();
//
//            poseStack2.peek().copy(pose);
//
//            for (GeoBone bone : model.topLevelBones()) {
//                bone.setHidden(true);
//                bone.setChildrenHidden(true);
//                renderBone(renderState, poseStack2, bone, vertexConsumer, cameraState, packedLight, packedOverlay, renderColor);
//                bone.setHidden(false);
//                bone.setChildrenHidden(false);
//            }
//            renderState.addGeckolibData(CustomDataTickets.IS_RENDERING_ARMS, false);
//        });
//        renderState.addGeckolibData(CustomDataTickets.IS_RENDERING_ARMS, false);
//    }

    @Override
    public void addRenderData(T animatable, RenderData relatedObject, GeoRenderState renderState, float partialTick) {
        if(relatedObject.itemOwner() != null && relatedObject.itemOwner().asLivingEntity() instanceof Player p){
            renderState.addGeckolibData(CustomDataTickets.IS_MASK_ON, ((IUniversePlayer)p).hasVanniMaskOn());
            renderState.addGeckolibData(CustomDataTickets.CAN_ANIMATE_MASK, ((IUniversePlayer)p).canAnimateMask());
            renderState.addGeckolibData(CustomDataTickets.IS_IN_MASK_SLOT, p.getInventory().getItem(FnafInventoryScreen.SLOTS_OFFSET).equals(relatedObject.itemStack()));
        }
        super.addRenderData(animatable, relatedObject, renderState, partialTick);
    }

//    @Override
//    public void renderBone(GeoRenderState renderState, MatrixStack poseStack, GeoBone bone, VertexConsumer buffer, CameraRenderState cameraState, int packedLight, int packedOverlay, int renderColor) {
//
//        super.renderBone(renderState, poseStack, bone, buffer, cameraState, packedLight, packedOverlay, renderColor);
//
//        AbstractClientPlayerEntity abstractClientPlayerEntity = Minecraft.getInstance().player;
//        PlayerEntityRenderer playerEntityRenderer = (PlayerEntityRenderer) Minecraft.getInstance().getEntityRenderDispatcher()
//                .<AbstractClientPlayerEntity>getRenderer(abstractClientPlayerEntity);
//
//        if((bone.getName().contains("left") || bone.getName().contains("right"))){
//            if(renderState.hasGeckolibData(CustomDataTickets.IS_RENDERING_ARMS) && Boolean.TRUE.equals(renderState.getGeckolibData(CustomDataTickets.IS_RENDERING_ARMS))) {
//                bone.setHidden(false);
//                bone.setChildrenHidden(false);
//            }
//            else{
//                return;
//            }
//            if(bone.getName().contains("left") ){
//                poseStack.push();
//                RenderUtil.prepMatrixForBone(poseStack, bone);
//                poseStack.translate(0, 0.125f, 0.5f);
//                poseStack.multiply(new Quaternionf().rotateXYZ(-((float)Math.PI/2), (float)Math.PI, 0));
//                poseStack.push();
//                renderArm(poseStack, buffer, packedLight, (PlayerEntityModel) playerEntityRenderer.getModel(), Arm.LEFT, true);
//                poseStack.pop();
//                poseStack.pop();
//            }
//            else {
//                poseStack.push();
//                RenderUtil.prepMatrixForBone(poseStack, bone);
//                poseStack.translate(0, 0.125f, 0.5f);
//                poseStack.multiply(new Quaternionf().rotateXYZ(-((float)Math.PI/2), (float)Math.PI, 0));
//                poseStack.push();
//                renderArm(poseStack, buffer, packedLight, (PlayerEntityModel) playerEntityRenderer.getModel(), Arm.RIGHT, true);
//                poseStack.pop();
//                poseStack.pop();
//            }
//        }
//    }
//
//    @Override
//    public void postRender(GeoRenderState renderState, MatrixStack poseStack, BakedGeoModel model, OrderedRenderCommandQueue renderTasks, CameraRenderState cameraState, int packedLight, int packedOverlay, int renderColor) {
//        super.postRender(renderState, poseStack, model, renderTasks, cameraState, packedLight, packedOverlay, renderColor);
//
////        AbstractClientPlayerEntity abstractClientPlayerEntity = Minecraft.getInstance().player;
////        PlayerEntityRenderer playerEntityRenderer = (PlayerEntityRenderer) Minecraft.getInstance().getEntityRenderDispatcher()
////                .<AbstractClientPlayerEntity>getRenderer(abstractClientPlayerEntity);
////        Identifier identifier = abstractClientPlayerEntity.getSkin().body().texturePath();
//
//
//
//    }

    void renderArm(PoseStack matrices, VertexConsumer buffer, int light, PlayerModel playerEntityModel, HumanoidArm arm, boolean sleeveVisible){

        ModelPart part = arm == HumanoidArm.RIGHT ? playerEntityModel.rightArm : playerEntityModel.leftArm;
        part.resetPose();
        part.visible = true;
        playerEntityModel.leftSleeve.visible = sleeveVisible;
        playerEntityModel.rightSleeve.visible = sleeveVisible;

        //for (ModelPart.Cuboid cuboid : part.cuboids) {

        //    matrix4f = entry.getPositionMatrix();
        //    Vector3f vector3f = new Vector3f();
//
        //    for (ModelPart.Quad quad : cuboid.sides) {
        //        Vector3f vector3f2 = entry.transformNormal(quad.direction, vector3f);
        //        float f = vector3f2.x();
        //        float g = vector3f2.y();
        //        float h = vector3f2.z();
//
        //        for (ModelPart.Vertex vertex : quad.vertices) {
        //            float i = vertex.pos.x() / 16.0F;
        //            float j = vertex.pos.y() / 16.0F;
        //            float k = vertex.pos.z() / 16.0F;
        //            Vector3f vector3f3 = matrix4f.transformPosition(i, j, k, vector3f);
        //            buffer.vertex(vector3f3.x(), vector3f3.y(), vector3f3.z(), 0xFFFFFFFF, vertex.u, vertex.v, OverlayTexture.DEFAULT_UV, light, f, g, h);
        //        }
        //    }
        //    //cuboid.renderCuboid(entry, buffer, light, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
        //}
        part.render(matrices, buffer, light, OverlayTexture.NO_OVERLAY);
    }
    private void renderArm(PoseStack matrices, PlayerModel playerEntityModel, SubmitNodeCollector queue, int light, Identifier skinTexture, HumanoidArm arm, boolean sleeveVisible) {

        ModelPart part = arm == HumanoidArm.RIGHT ? playerEntityModel.rightArm : playerEntityModel.leftArm;
        part.resetPose();
        part.visible = true;
        playerEntityModel.leftSleeve.visible = sleeveVisible;
        playerEntityModel.rightSleeve.visible = sleeveVisible;
        playerEntityModel.leftArm.zRot = -0.1F;
        playerEntityModel.rightArm.zRot = 0.1F;
        queue.submitModelPart(part, matrices, RenderTypes.entityTranslucent(skinTexture), light, OverlayTexture.NO_OVERLAY, null);
    }
}
