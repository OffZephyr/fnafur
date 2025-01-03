package net.zephyr.fnafur.entity.zephyr;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.zephyr.fnafur.entity.GlowLayer;
import net.zephyr.fnafur.entity.base.DefaultEntityRenderer;

public class ZephyrRenderer extends DefaultEntityRenderer<ZephyrEntity> {
    public ZephyrRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new ZephyrModel());
        addRenderLayer(new GlowLayer<>(this));
    }
}

