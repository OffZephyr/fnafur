package net.zephyr.fnafur.entity.classic.cl_fred;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.zephyr.fnafur.entity.GlowLayer;
import net.zephyr.fnafur.entity.base.DefaultEntityRenderer;

public class cl_fred_renderer extends DefaultEntityRenderer<cl_fred> {
    public cl_fred_renderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new cl_fred_model());
        addRenderLayer(new GlowLayer<>(this));
    }

    @Override
    public void render(cl_fred entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
