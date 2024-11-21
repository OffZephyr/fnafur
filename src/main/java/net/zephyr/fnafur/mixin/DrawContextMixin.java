package net.zephyr.fnafur.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.zephyr.fnafur.util.mixinAccessing.IDCVertexConsumersAcc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DrawContext.class)
public class DrawContextMixin implements IDCVertexConsumersAcc {
    @Shadow
    private VertexConsumerProvider.Immediate vertexConsumers;

    @Override
    public VertexConsumerProvider.Immediate getVertexConsumers() {
        return vertexConsumers;
    }
}
