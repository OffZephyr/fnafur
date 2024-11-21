package net.zephyr.fnafur.entity.classic.cl_foxy;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.zephyr.fnafur.entity.GlowLayer;
import net.zephyr.fnafur.entity.base.DefaultEntityRenderer;

public class cl_foxy_renderer extends DefaultEntityRenderer<cl_foxy> {
    public cl_foxy_renderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new cl_foxy_model());
        addRenderLayer(new GlowLayer<>(this));
    }
}
