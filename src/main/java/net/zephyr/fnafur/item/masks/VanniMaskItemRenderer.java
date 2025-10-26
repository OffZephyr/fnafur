package net.zephyr.fnafur.item.masks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.client.gui.screens.FnafInventoryScreen;
import net.zephyr.fnafur.util.CustomDataTickets;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IUniversePlayer;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.processing.AnimationState;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtil;

import java.util.List;

public class VanniMaskItemRenderer<T extends Item & GeoAnimatable, O, R extends GeoRenderState> extends GeoItemRenderer<T> {
    MatrixStack leftStack;
    MatrixStack rightStack;
    public VanniMaskItemRenderer() {
        super(new VanniMaskItemModel<>());
    }

    @Override
    public List<GeoRenderLayer<T, RenderData, GeoRenderState>> getRenderLayers() {
        return List.of(
                new VanniMaskRenderLayer<>(this)
        );
    }

    @Override
    public void addRenderData(T animatable, RenderData relatedObject, GeoRenderState renderState, float partialTick) {
        if(relatedObject.itemOwner().getEntity() instanceof PlayerEntity p){
            renderState.addGeckolibData(CustomDataTickets.IS_MASK_ON, ((IUniversePlayer)p).hasVanniMaskOn());
            renderState.addGeckolibData(CustomDataTickets.CAN_ANIMATE_MASK, ((IUniversePlayer)p).canAnimateMask());
            renderState.addGeckolibData(CustomDataTickets.IS_IN_MASK_SLOT, p.getInventory().getStack(FnafInventoryScreen.SLOTS_OFFSET).equals(relatedObject.itemStack()));
        }
        super.addRenderData(animatable, relatedObject, renderState, partialTick);
    }

    @Override
    public void renderBone(GeoRenderState renderState, MatrixStack poseStack, GeoBone bone, VertexConsumer buffer, CameraRenderState cameraState, int packedLight, int packedOverlay, int renderColor) {

        super.renderBone(renderState, poseStack, bone, buffer, cameraState, packedLight, packedOverlay, renderColor);

        poseStack.push();
        if((bone.getName().contains("left") || bone.getName().contains("right"))){

            if(bone.getName().contains("left") ){
                poseStack.push();
                RenderUtil.prepMatrixForBone(poseStack, bone);
                poseStack.translate(0, 0.125f, 0.5f);
                poseStack.multiply(new Quaternionf().rotateXYZ(-((float)Math.PI/2), (float)Math.PI, 0));
                poseStack.push();
                leftStack = poseStack;
                poseStack.pop();
                poseStack.pop();
            }
            else {
                poseStack.push();
                RenderUtil.prepMatrixForBone(poseStack, bone);
                poseStack.translate(0, 0.125f, 0.5f);
                poseStack.multiply(new Quaternionf().rotateXYZ(-((float)Math.PI/2), (float)Math.PI, 0));
                poseStack.push();
                rightStack = poseStack;
                poseStack.pop();
                poseStack.pop();
            }
        }
        poseStack.pop();
    }

    @Override
    public void postRender(GeoRenderState renderState, MatrixStack poseStack, BakedGeoModel model, OrderedRenderCommandQueue renderTasks, CameraRenderState cameraState, int packedLight, int packedOverlay, int renderColor) {
        super.postRender(renderState, poseStack, model, renderTasks, cameraState, packedLight, packedOverlay, renderColor);

        AbstractClientPlayerEntity abstractClientPlayerEntity = MinecraftClient.getInstance().player;
        PlayerEntityRenderer playerEntityRenderer = (PlayerEntityRenderer) MinecraftClient.getInstance().getEntityRenderDispatcher()
                .<AbstractClientPlayerEntity>getRenderer(abstractClientPlayerEntity);
        Identifier identifier = abstractClientPlayerEntity.getSkin().body().texturePath();

        renderTasks.submitCustom(poseStack, getRenderType(renderState, identifier), (entry, buffer) -> {

            renderArm(poseStack, buffer, packedLight, (PlayerEntityModel) playerEntityRenderer.getModel(), Arm.LEFT, true);
            renderArm(poseStack, buffer, packedLight, (PlayerEntityModel) playerEntityRenderer.getModel(), Arm.RIGHT, true);
        });

    }

    void renderArm(MatrixStack matrices, VertexConsumer buffer, int light, PlayerEntityModel playerEntityModel, Arm arm, boolean sleeveVisible){

        ModelPart part = arm == Arm.RIGHT ? playerEntityModel.rightArm : playerEntityModel.leftArm;
        part.resetTransform();
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
        part.render(matrices, buffer, light, OverlayTexture.DEFAULT_UV);
    }
}
