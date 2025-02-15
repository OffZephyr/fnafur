package net.zephyr.fnafur.blocks.geo_doors;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class GeoDoorWindowLayer extends GeoRenderLayer<GeoDoorEntity> {
    public GeoDoorWindowLayer(GeoRenderer<GeoDoorEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack poseStack, GeoDoorEntity animatable, BakedGeoModel bakedModel, @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int renderColor) {
        if(animatable.getWindowTexture() != animatable.getTexture()) {
            RenderLayer glowRenderType = RenderLayer.getEntityTranslucent(animatable.getWindowTexture());
            getRenderer().reRender(getDefaultBakedModel(animatable, renderer), poseStack, bufferSource, animatable, glowRenderType, bufferSource.getBuffer(glowRenderType), partialTick, packedLight, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
        }
    }
}
