package net.zephyr.fnafur.blocks.geo_doors;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.util.CustomDataTickets;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class GeoDoorWindowLayer extends GeoRenderLayer<GeoDoorEntity, Void, GeoRenderState> {
    public GeoDoorWindowLayer(GeoDoorRenderer entityRendererIn) {
        super(entityRendererIn);
    }


//    @Override
//    public void render(GeoRenderState renderState, MatrixStack poseStack, BakedGeoModel bakedModel, @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, int renderColor) {
//
//        Identifier windowTexture = renderState.getGeckolibData(CustomDataTickets.DOOR_WINDOW_TEXTURE);
//        Identifier texture = renderState.getGeckolibData(CustomDataTickets.TEXTURE);
//
//        if(windowTexture != texture) {
//            RenderLayer glowRenderType = RenderLayer.getEntityTranslucent(windowTexture);
//            getRenderer().reRender(renderState, poseStack, bakedModel, bufferSource, glowRenderType, bufferSource.getBuffer(glowRenderType), packedLight, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
//        }
//    }
}
