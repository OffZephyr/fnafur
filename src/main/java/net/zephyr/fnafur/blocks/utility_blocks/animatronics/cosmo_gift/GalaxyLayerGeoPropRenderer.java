package net.zephyr.fnafur.blocks.utility_blocks.animatronics.cosmo_gift;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.blocks.common_block_entity.CommonBlockEntityRenderState;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropModel;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropRenderer;
import net.zephyr.fnafur.util.mixinAccessing.IUniverseRenderLayers;
import software.bernie.geckolib.renderer.base.GeoRenderState;

@Environment(EnvType.CLIENT)
public class GalaxyLayerGeoPropRenderer<T extends GalaxyLayerGeoPropEntity, R extends CommonBlockEntityRenderState & GeoRenderState> extends GeoPropRenderer<T, R> implements BlockEntityRenderer<T, R> {
    public GalaxyLayerGeoPropRenderer(BlockEntityRendererFactory.Context context) {
        super(context);

        //addRenderLayer(new GeoPropAddedLayer<>(this, ((IUniverseRenderLayers)RenderLayer.getEndGateway()).getCosmoGift()));
    }

    @Override
    public void render(R renderState, MatrixStack matrices, OrderedRenderCommandQueue renderTasks, CameraRenderState cameraRenderState) {

        super.render(renderState, matrices, renderTasks, cameraRenderState);
        ((GeoPropModel)getGeoModel()).reRender = true;
        super.render(renderState, matrices, renderTasks, cameraRenderState);
        ((GeoPropModel)getGeoModel()).reRender = false;
    }
}
