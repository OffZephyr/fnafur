package net.zephyr.fnafur.blocks.props.wall_props.clocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropRenderer;
import net.zephyr.fnafur.util.CustomDataTickets;
import software.bernie.geckolib.cache.model.GeoBone;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class GeoClockPropRenderer<T extends GeoClockPropBlockEntity, R extends BlockEntityRenderState & GeoRenderState> extends GeoPropRenderer<T, R> {
    MinecraftClient client;
    BlockRenderManager manager;
    public GeoClockPropRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
        client = MinecraftClient.getInstance();
        manager = client.getBlockRenderManager();
    }

    @Override
    public void render(R renderState, MatrixStack matrices, OrderedRenderCommandQueue renderTasks, CameraRenderState cameraRenderState) {
        GeoBone seconds = getGeoModel().getBakedModel(getGeoModel().getModelResource(renderState)).getBone("second").orElse(null);
        GeoBone minutes = getGeoModel().getBakedModel(getGeoModel().getModelResource(renderState)).getBone("minute").orElse(null);
        GeoBone hours = getGeoModel().getBakedModel(getGeoModel().getModelResource(renderState)).getBone("hour").orElse(null);

        float deltaMinute = renderState.getGeckolibData(CustomDataTickets.CLOCK_DELTA_MINUTE).floatValue();
        float deltaHour = renderState.getGeckolibData(CustomDataTickets.CLOCK_DELTA_HOUR).floatValue();

        float rot = 0;
        if(seconds != null && seconds.frameSnapshot != null){
            rot = deltaMinute * 60;
            seconds.frameSnapshot.setRotation(0, 0, 360 * rot);
        }
        if(minutes != null && minutes.frameSnapshot != null){
            rot = deltaMinute;
            minutes.frameSnapshot.setRotation(0, 0, 360 * rot);
        }
        if(hours != null && hours.frameSnapshot != null){
            rot = (deltaHour + (((1 / 12f) * (deltaMinute))));
            hours.frameSnapshot.setRotation(0, 0, 360 * rot);
        }
        super.render(renderState, matrices, renderTasks, cameraRenderState);
    }

    @Override
    public R fillRenderState(T animatable, Void relatedObject, R renderState, float partialTick) {
        R state = super.fillRenderState(animatable, relatedObject, renderState, partialTick);

        state.addGeckolibData(CustomDataTickets.CLOCK_DELTA_MINUTE, animatable.deltaMinute);
        state.addGeckolibData(CustomDataTickets.CLOCK_DELTA_HOUR, animatable.deltaHour);

        return state;
    }
}
