package net.zephyr.fnafur.blocks.props.wall_props.clocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.CameraRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import com.mojang.math.Axis;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropRenderer;
import net.zephyr.fnafur.util.CustomDataTickets;
import software.bernie.geckolib.cache.model.GeoBone;
import software.bernie.geckolib.renderer.base.BoneSnapshots;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.RenderPassInfo;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class GeoClockPropRenderer<T extends GeoClockPropBlockEntity, R extends BlockEntityRenderState & GeoRenderState> extends GeoPropRenderer<T, R> {
    Minecraft client;
    BlockRenderDispatcher manager;
    public GeoClockPropRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        client = Minecraft.getInstance();
        manager = client.getBlockRenderer();
    }

    @Override
    public void submit(R renderState, PoseStack matrices, SubmitNodeCollector renderTasks, CameraRenderState cameraRenderState) {
        super.submit(renderState, matrices, renderTasks, cameraRenderState);
    }

    @Override
    public void adjustModelBonesForRender(RenderPassInfo<R> renderPassInfo, BoneSnapshots snapshots) {

        GeoBone seconds = renderPassInfo.model().getBone("second").orElse(null);
        GeoBone minutes = renderPassInfo.model().getBone("minute").orElse(null);
        GeoBone hours = renderPassInfo.model().getBone("hour").orElse(null);

        float deltaMinute = renderPassInfo.renderState().getGeckolibData(CustomDataTickets.CLOCK_DELTA_MINUTE).floatValue();
        float deltaHour = renderPassInfo.renderState().getGeckolibData(CustomDataTickets.CLOCK_DELTA_HOUR).floatValue();

        float rot = 0;

        if(seconds != null) {
            rot = deltaMinute * 60;
            snapshots.get(seconds).setRotation(0, 0, 180 * rot * Mth.DEG_TO_RAD);
        }
        if(minutes != null){
            rot = deltaMinute;
            snapshots.get(minutes).setRotation(0, 0, 180 * rot * Mth.DEG_TO_RAD);
        }
        if(hours != null){
            rot = (deltaHour + (((1 / 12f) * (deltaMinute))));
            snapshots.get(hours).setRotation(0, 0, 180 * rot * Mth.DEG_TO_RAD);
        }

        super.adjustModelBonesForRender(renderPassInfo, snapshots);
    }

    @Override
    public R fillRenderState(T animatable, Void relatedObject, R renderState, float partialTick) {
        R state = super.fillRenderState(animatable, relatedObject, renderState, partialTick);

        state.addGeckolibData(CustomDataTickets.CLOCK_DELTA_MINUTE, animatable.deltaMinute);
        state.addGeckolibData(CustomDataTickets.CLOCK_DELTA_HOUR, animatable.deltaHour);

        return state;
    }
}
