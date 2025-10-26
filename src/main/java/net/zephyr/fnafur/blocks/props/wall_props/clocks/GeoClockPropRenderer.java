package net.zephyr.fnafur.blocks.props.wall_props.clocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.zephyr.fnafur.blocks.common_block_entity.CommonBlockEntityRenderState;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropRenderer;
import net.zephyr.fnafur.util.CustomDataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class GeoClockPropRenderer<T extends GeoClockPropBlockEntity, R extends CommonBlockEntityRenderState & GeoRenderState> extends GeoPropRenderer<T, R> {
    MinecraftClient client;
    BlockRenderManager manager;
    public GeoClockPropRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
        client = MinecraftClient.getInstance();
        manager = client.getBlockRenderManager();
    }

    @Override
    public R fillRenderState(T animatable, Void relatedObject, R renderState, float partialTick) {
        R state = super.fillRenderState(animatable, relatedObject, renderState, partialTick);

        state.addGeckolibData(CustomDataTickets.CLOCK_DELTA_MINUTE, animatable.deltaMinute);
        state.addGeckolibData(CustomDataTickets.CLOCK_DELTA_HOUR, animatable.deltaHour);

        return state;
    }
    @Override
    public void renderBone(R renderState, MatrixStack poseStack, GeoBone bone, VertexConsumer buffer, CameraRenderState cameraState, int packedLight, int packedOverlay, int renderColor) {
        poseStack.push();

        float deltaMinute = renderState.getGeckolibData(CustomDataTickets.CLOCK_DELTA_MINUTE).floatValue();
        float deltaHour = renderState.getGeckolibData(CustomDataTickets.CLOCK_DELTA_HOUR).floatValue();

        float rot = 0;
        if(Objects.equals(bone.getName(), "second")){
            rot = deltaMinute * 60;
            poseStack.translate(0, 0.5f, 0);
            poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(360 * rot));
            poseStack.translate(0, -0.5f, 0);
        }
        if(Objects.equals(bone.getName(), "minute")){
            rot = deltaMinute;
            poseStack.translate(0, 0.5f, 0);
            poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(360 * rot));
            poseStack.translate(0, -0.5f, 0);
        }
        else if(Objects.equals(bone.getName(), "hour")){
            rot = (deltaHour + (((1 / 12f) * (deltaMinute))));
            poseStack.translate(0, 0.5f, 0);
            poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180 * rot));
            poseStack.translate(0, -0.5f, 0);
        }

        super.renderBone(renderState, poseStack, bone, buffer, cameraState, packedLight, packedOverlay, renderColor);
        poseStack.pop();
    }
}
