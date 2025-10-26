package net.zephyr.fnafur.entity.animatronic.block;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.blocks.common_block_entity.CommonBlockEntityRenderState;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropModel;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropRenderer;
import net.zephyr.fnafur.client.gui.screens.crafting.CpuConfigScreen;
import net.zephyr.fnafur.client.gui.screens.crafting.WorkbenchScreen;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class AnimatronicBlockEntityRenderer<T extends AnimatronicBlockEntity, R extends CommonBlockEntityRenderState & GeoRenderState> extends GeoPropRenderer<T, R> {
    public AnimatronicBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        super(new AnimatronicBlockModel<>());
//        addRenderLayer(new AnimatronicBlockColoredLayer<>(this));
//        addRenderLayer(new AnimatronicBlockEyeLayer<>(this));
//        addRenderLayer(new AnimatronicBlockColoredEyeLayer<>(this));
    }

    @Override
    public void render(R renderState, MatrixStack matrices, OrderedRenderCommandQueue renderTasks, CameraRenderState cameraRenderState) {
        if(!(MinecraftClient.getInstance().currentScreen instanceof WorkbenchScreen || MinecraftClient.getInstance().currentScreen instanceof CpuConfigScreen)) {
            matrices.push();
            super.render(renderState, matrices, renderTasks, cameraRenderState);
            matrices.pop();
        }
    }
}
