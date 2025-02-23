package net.zephyr.fnafur.item.tablet;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.renderer.GeckolibSpecialRenderer;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class TabletItemRenderer extends GeoItemRenderer<TabletItem> {
    public TabletItemRenderer(TabletItemModel model) {
        super(model);
        addRenderLayer(new GlowLayer(this));
    }

    @Override
    public void render(GeckolibSpecialRenderer.RenderData renderData, ModelTransformationMode transformType, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, int packedOverlay, boolean hasGlint) {
        ItemStack stack = renderData.itemstack();
        this.animatable = (TabletItem) stack.getItem();
        this.currentItemStack = stack;
        this.renderPerspective = transformType;

        AnimatableInstanceCache cache = this.animatable.getAnimatableInstanceCache();
        cache.getManagerForId(GeoItem.getId(stack));
        AnimationController<GeoAnimatable> controller = cache.getManagerForId(GeoItem.getId(stack)).getAnimationControllers().get("Use");

        if (transformType != ModelTransformationMode.FIRST_PERSON_RIGHT_HAND && transformType != ModelTransformationMode.FIRST_PERSON_LEFT_HAND) {
            poseStack.push();
            float partialTick = MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true);
            if (transformType == ModelTransformationMode.GUI) {
                RenderLayer renderType = getRenderType(this.animatable, getTextureLocation(this.animatable), bufferSource, partialTick);
                VertexConsumer buffer = ItemRenderer.getItemGlintConsumer(bufferSource, renderType, false, this.currentItemStack != null && this.currentItemStack.hasGlint());
                preRender(poseStack, this.animatable, getGeoModel().getBakedModel(getGeoModel().getModelResource(animatable, this)), bufferSource, buffer, false, partialTick, packedLight, packedOverlay, 0xFFFFFFFF);
                poseStack.translate(-0.5f, -0.51f, -0.5f);
            }

            RenderLayer renderType = getRenderType(this.animatable, getTextureLocation(this.animatable), bufferSource, partialTick);
            VertexConsumer buffer = ItemRenderer.getItemGlintConsumer(bufferSource, renderType, false, this.currentItemStack != null && this.currentItemStack.hasGlint());

            if (model != null && model.getAnimationProcessor().getBone("monitor") != null) {
                GeoCube cube = model.getAnimationProcessor().getBone("monitor").getCubes().get(0);
                GeoCube cube2 = model.getAnimationProcessor().getBone("monitor").getCubes().get(1);
                GeoCube screen = model.getAnimationProcessor().getBone("monitor").getChildBones().get(0).getCubes().get(0);
                poseStack.push();
                poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
                poseStack.translate(0.525f, -1f, 0);
                renderCube(poseStack, cube2, buffer, packedLight, packedOverlay, 0xFFFFFFFF);
                poseStack.pop();
                poseStack.push();
                poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
                poseStack.translate(0.525f, -1f, 0);
                renderCube(poseStack, cube, buffer, packedLight, packedOverlay, 0xFFFFFFFF);
                poseStack.pop();

                if (stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getBoolean("used")) {

                    poseStack.push();
                    poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
                    poseStack.translate(0.525f, -1f, 0);
                    renderCube(poseStack, screen, buffer, LightmapTextureManager.MAX_LIGHT_COORDINATE, packedOverlay, 0xFFFFFFFF);
                    poseStack.pop();
                }
            }
            poseStack.pop();
        }
        else {
            GeoBone right = model.getAnimationProcessor().getBone("right");
            GeoBone left = model.getAnimationProcessor().getBone("left");
            if (stack.getItem() instanceof TabletItem tab) {
                if (right != null && left != null && transformType.isFirstPerson() && tab.renderArms()) {
                    renderArm(poseStack, bufferSource, packedLight, Arm.RIGHT, right);
                    renderArm(poseStack, bufferSource, packedLight, Arm.LEFT, left);
                }
            }
            super.render(renderData, transformType, poseStack, bufferSource, packedLight, packedOverlay, hasGlint);
        }

    }

    private void renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Arm arm, GeoBone bone) {
        PlayerEntityRenderer playerEntityRenderer = (PlayerEntityRenderer)MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(MinecraftClient.getInstance().player);
        matrices.push();
        float f = arm == Arm.RIGHT ? 1.0f : -1.0f;

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-2));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f * 2));

        matrices.translate(0.5, 0.7, 1);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));

        matrices.translate(bone.getPosX() / 15, bone.getPosY() / 15, (-bone.getPosZ()) / 15);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-bone.getRotX() * MathHelper.DEGREES_PER_RADIAN));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-bone.getRotZ() * MathHelper.DEGREES_PER_RADIAN));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-bone.getRotY() * MathHelper.DEGREES_PER_RADIAN));

        if (arm == Arm.RIGHT) {
            playerEntityRenderer.renderRightArm(matrices, vertexConsumers, light, MinecraftClient.getInstance().player.getSkinTextures().texture(), true);
        } else {
            playerEntityRenderer.renderLeftArm(matrices, vertexConsumers, light, MinecraftClient.getInstance().player.getSkinTextures().texture(), true);
        }
        matrices.pop();
    }
}
