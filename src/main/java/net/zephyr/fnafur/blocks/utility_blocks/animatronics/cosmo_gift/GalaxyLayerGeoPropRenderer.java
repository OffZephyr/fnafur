package net.zephyr.fnafur.blocks.utility_blocks.animatronics.cosmo_gift;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropModel;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropRenderer;
import net.zephyr.fnafur.util.mixinAccessing.IUniverseRenderLayers;

@Environment(EnvType.CLIENT)
public class GalaxyLayerGeoPropRenderer<T extends GalaxyLayerGeoPropEntity> extends GeoPropRenderer<T> implements BlockEntityRenderer<T> {
    public GalaxyLayerGeoPropRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
        addRenderLayer(new GeoPropAddedLayer<>(this, ((IUniverseRenderLayers)RenderLayer.getEndGateway()).getCosmoGift()));
    }
    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPosition) {

        super.render(entity, tickDelta, matrices, vertexConsumers, light, overlay, cameraPosition);

        ((GeoPropModel)getGeoModel()).reRender = true;
        super.render(entity, tickDelta, matrices, vertexConsumers, light, overlay, cameraPosition);
        ((GeoPropModel)getGeoModel()).reRender = false;
    }
}
