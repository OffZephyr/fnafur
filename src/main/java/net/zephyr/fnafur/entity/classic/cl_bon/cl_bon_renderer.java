package net.zephyr.fnafur.entity.classic.cl_bon;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.zephyr.fnafur.entity.GlowLayer;
import net.zephyr.fnafur.entity.base.DefaultEntityRenderer;

public class cl_bon_renderer extends DefaultEntityRenderer<cl_bon> {
    public cl_bon_renderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new cl_bon_model());
        addRenderLayer(new GlowLayer<>(this));
    }
}
