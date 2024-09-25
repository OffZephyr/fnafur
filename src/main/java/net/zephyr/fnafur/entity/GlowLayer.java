package net.zephyr.fnafur.entity;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class GlowLayer<T extends DefaultEntity> extends GeoRenderLayer<T> {
    public GlowLayer(GeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack poseStack, T animatable, BakedGeoModel bakedModel, @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        String skin = ((IEntityDataSaver) animatable).getPersistentData().getString("Reskin");

        Identifier TEXTURE = animatable.getSkin(skin).glow_texture;
        int glowType = animatable.getSkin(skin).glow_type;

        if(TEXTURE != null) {

            RenderLayer glowRenderType = RenderLayer.getEyes(TEXTURE);

            if (glowType == DefaultEntity.EntitySkin.CONSTANT) {
                getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, glowRenderType, bufferSource.getBuffer(glowRenderType), partialTick, packedLight, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
            } else if (glowType == DefaultEntity.EntitySkin.NIGHT && animatable.getAIHour(animatable.getWorld()) < 24 || animatable.getAIHour(animatable.getWorld()) > 72) {
                getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, glowRenderType, bufferSource.getBuffer(glowRenderType), partialTick, packedLight, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
            } else if (glowType == DefaultEntity.EntitySkin.AGGRESSIVE && animatable.getTarget() != null) {
                getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, glowRenderType, bufferSource.getBuffer(glowRenderType), partialTick, packedLight, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
            }
        }
    }
}
