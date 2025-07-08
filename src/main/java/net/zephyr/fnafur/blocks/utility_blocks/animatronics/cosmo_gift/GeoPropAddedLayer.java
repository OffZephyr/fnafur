package net.zephyr.fnafur.blocks.utility_blocks.animatronics.cosmo_gift;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropModel;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class GeoPropAddedLayer<T extends GeoPropBlockEntity, O, R extends GeoRenderState> extends GeoRenderLayer<T, O, R> {
    final RenderLayer layer;
    public GeoPropAddedLayer(GeoRenderer<T, O, R> entityRendererIn, RenderLayer layer) {
        super(entityRendererIn);
        this.layer = layer;
    }

    @Override
    public void render(R renderState, MatrixStack poseStack, BakedGeoModel bakedModel, @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, int renderColor) {
        if(((GeoPropModel<T>)getGeoModel()).reRender){
            getRenderer().reRender(renderState, poseStack, bakedModel, bufferSource, layer, bufferSource.getBuffer(layer), packedLight, packedOverlay, renderColor);
        }
    }
}
