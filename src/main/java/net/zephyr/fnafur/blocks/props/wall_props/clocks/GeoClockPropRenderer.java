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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropRenderer;
import net.zephyr.fnafur.util.CustomDataTickets;
import software.bernie.geckolib.cache.model.GeoBone;
import software.bernie.geckolib.renderer.base.BoneSnapshots;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.RenderPassInfo;

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
        super.render(renderState, matrices, renderTasks, cameraRenderState);
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
            snapshots.get(seconds).setRotation(0, 0, 180 * rot * MathHelper.RADIANS_PER_DEGREE);
        }
        if(minutes != null){
            rot = deltaMinute;
            snapshots.get(minutes).setRotation(0, 0, 180 * rot * MathHelper.RADIANS_PER_DEGREE);
        }
        if(hours != null){
            rot = (deltaHour + (((1 / 12f) * (deltaMinute))));
            snapshots.get(hours).setRotation(0, 0, 180 * rot * MathHelper.RADIANS_PER_DEGREE);
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
