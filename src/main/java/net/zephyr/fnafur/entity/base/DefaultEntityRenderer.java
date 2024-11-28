package net.zephyr.fnafur.entity.base;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShapes;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public abstract class DefaultEntityRenderer<T extends DefaultEntity> extends GeoEntityRenderer<T> {
    public DefaultEntityRenderer(EntityRendererFactory.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
    }
    public void render(T entity, EntityRenderState entityRenderState, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        animatable = entity;
        render(entityRenderState, poseStack, bufferSource, packedLight);
    }
    @Override
    public void render(EntityRenderState entityRenderState, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {

        T entity = getAnimatable();

        if(entity != null) {
            if(entity.mimic) System.out.println("t3");

            if (entity.boopBox != null && MinecraftClient.getInstance().getEntityRenderDispatcher().shouldRenderHitboxes()) {
                poseStack.push();
                poseStack.translate(-entity.getX(), -entity.getY(), -entity.getZ());
                VertexRendering.drawOutline(poseStack, bufferSource.getBuffer(RenderLayer.LINES), VoxelShapes.cuboid(entity.boopBox), 0, 0, 0, 0xFF00FF00);
                poseStack.pop();
            }

            if (this.getGeoModel() instanceof DefaultEntityModel<T> model) {
                model.entityRenderer = this;
            }

            PlayerEntity player = MinecraftClient.getInstance().player;
            Entity jumpscareEntity = MinecraftClient.getInstance().world.getEntityById(((IEntityDataSaver) player).getPersistentData().getInt("JumpscareID"));

            if (MinecraftClient.getInstance().player.isDead() && jumpscareEntity != null && entity.getId() != jumpscareEntity.getId())
                return;

            poseStack.push();
            float scale = ((DefaultEntityModel) getGeoModel()).scale();
            poseStack.scale(scale, scale, scale);
            super.render(entityRenderState, poseStack, bufferSource, packedLight);
            poseStack.pop();
        }
    }

    @Override
    public void preRender(MatrixStack poseStack, T animatable, BakedGeoModel model, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    protected float getDeathMaxRotation(T animatable, float partialTick) {
        return 0.0f;
    }

    @Override
    public int getPackedOverlay(T animatable, float u, float partialTick) {
        return OverlayTexture.packUv(OverlayTexture.getU(u),
                OverlayTexture.getV(false));
    }
}

