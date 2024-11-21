package net.zephyr.fnafur.entity.classic.cl_chica;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.zephyr.fnafur.entity.GlowLayer;
import net.zephyr.fnafur.entity.base.DefaultEntityRenderer;

public class cl_chica_renderer extends DefaultEntityRenderer<cl_chica> {
    public cl_chica_renderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new cl_chica_model());
        addRenderLayer(new GlowLayer<>(this));
    }
}
