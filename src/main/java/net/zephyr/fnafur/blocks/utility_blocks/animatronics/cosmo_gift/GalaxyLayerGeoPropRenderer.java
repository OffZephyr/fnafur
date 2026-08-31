package net.zephyr.fnafur.blocks.utility_blocks.animatronics.cosmo_gift;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.CameraRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.phys.Vec3;
import net.zephyr.fnafur.blocks.common_block_entity.CommonBlockEntityRenderState;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropModel;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

@Environment(EnvType.CLIENT)
public class GalaxyLayerGeoPropRenderer<T extends GalaxyLayerGeoPropEntity, R extends CommonBlockEntityRenderState & GeoRenderState> extends GeoPropRenderer<T, R> implements BlockEntityRenderer<T, R> {
    public GalaxyLayerGeoPropRenderer(BlockEntityRendererProvider.Context context) {
        super(context);

        //addRenderLayer(new GeoPropAddedLayer<>(this, ((IUniverseRenderLayers)RenderLayer.getEndGateway()).getCosmoGift()));
    }

    @Override
    public void submit(R renderState, PoseStack matrices, SubmitNodeCollector renderTasks, CameraRenderState cameraRenderState) {

        super.submit(renderState, matrices, renderTasks, cameraRenderState);
        ((GeoPropModel)getGeoModel()).reRender = true;
        super.submit(renderState, matrices, renderTasks, cameraRenderState);
        ((GeoPropModel)getGeoModel()).reRender = false;
    }
}
