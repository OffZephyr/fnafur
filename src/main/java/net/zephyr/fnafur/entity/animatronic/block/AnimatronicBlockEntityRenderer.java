package net.zephyr.fnafur.entity.animatronic.block;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropModel;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropRenderer;
import net.zephyr.fnafur.client.gui.screens.crafting.CpuConfigScreen;
import net.zephyr.fnafur.client.gui.screens.crafting.WorkbenchScreen;

public class AnimatronicBlockEntityRenderer<T extends AnimatronicBlockEntity> extends GeoPropRenderer<T> {
    public AnimatronicBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        super(new AnimatronicBlockModel<>());
        addRenderLayer(new AnimatronicBlockColoredLayer<>(this));
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if(!(MinecraftClient.getInstance().currentScreen instanceof WorkbenchScreen || MinecraftClient.getInstance().currentScreen instanceof CpuConfigScreen)){
            matrices.push();
            matrices.translate(0.17, 0, 0.17);
            matrices.scale(0.68f, 0.68f, 0.68f);
            super.render(entity, tickDelta, matrices, vertexConsumers, light, overlay);
            matrices.pop();
        }
    }
}
