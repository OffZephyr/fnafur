package net.zephyr.fnafur.blocks.props.wall_props.clocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.shape.VoxelShapes;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.blocks.props.base.WallPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropModel;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropRenderer;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class GeoClockPropRenderer<T extends GeoClockPropBlockEntity> extends GeoPropRenderer<T> {
    MinecraftClient client;
    BlockRenderManager manager;
    public GeoClockPropRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
        client = MinecraftClient.getInstance();
        manager = client.getBlockRenderManager();
    }

    @Override
    public void renderRecursively(MatrixStack poseStack, T animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int renderColor) {
        poseStack.push();

        float rot = 0;
        if(Objects.equals(bone.getName(), "second")){
            System.out.println("sec");
            rot = animatable.deltaMinute * 60;
            poseStack.translate(0, 0.5f, 0);
            poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(360 * rot));
            poseStack.translate(0, -0.5f, 0);
        }
        if(Objects.equals(bone.getName(), "minute")){
            rot = animatable.deltaMinute;
            poseStack.translate(0, 0.5f, 0);
            poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(360 * rot));
            poseStack.translate(0, -0.5f, 0);
        }
        else if(Objects.equals(bone.getName(), "hour")){
            rot = (animatable.deltaHour + (((1 / 12f) * (animatable.deltaMinute))));
            poseStack.translate(0, 0.5f, 0);
            poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180 * rot));
            poseStack.translate(0, -0.5f, 0);
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, renderColor);
        poseStack.pop();
    }
}
