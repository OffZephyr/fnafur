package net.zephyr.fnafur.entity.other.bear5;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;

public class Bear5Renderer extends EntityRenderer<Bear5Entity, Bear5RenderState> {
    public Bear5Renderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Bear5RenderState createRenderState() {
        return new Bear5RenderState();
    }

    @Override
    public void updateRenderState(Bear5Entity entity, Bear5RenderState state, float tickDelta) {
        if(entity.target != null) {
            state.target = entity.target;
        }
    }

    @Override
    public void render(Bear5RenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

        //if(state.target != null && MinecraftClient.getInstance().player.getUuid() == state.target.getUuid()){
            Identifier texture = Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/other/bear_five.png");

            RenderSystem.enableCull();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();

            RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);

            var buffer = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

            //VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(texture));

            matrices.push();
            matrices.translate(0, 1.5f, 0);
            matrices.multiply(MinecraftClient.getInstance().gameRenderer.getCamera().getRotation());
            RenderSystem.setShaderTexture(0, texture);
            RenderSystem.setShaderColor(1, 1, 1, 1);

            buffer
                    .vertex(matrices.peek().getPositionMatrix(), 0.75f, -1.5f, 0)
                    .texture(1, 1)
                    //.light(255, 255)
                    //.overlay(0, 0)
                    //.normal(0, 0, 0)
                    .color(0xFFFFFFFF);
            buffer
                    .vertex(matrices.peek().getPositionMatrix(), 0.75f, 1.5f, 0)
                    .texture(1, 0)
                    //.light(255, 255)
                    //.overlay(0, 0)
                    //.normal(0, 0, 0)
                    .color(0xFFFFFFFF);
            buffer
                    .vertex(matrices.peek().getPositionMatrix(), -0.75f, 1.5f, 0)
                    .texture(0, 0)
                    //.light(255, 255)
                    //.overlay(0, 0)
                    //.normal(0, 0, 0)
                    .color(0xFFFFFFFF);
            buffer
                    .vertex(matrices.peek().getPositionMatrix(), -0.75f, -1.5f, 0)
                    .texture(0, 1)
                    //.light(255, 255)
                    //.overlay(0, 0)
                    //.normal(0, 0, 0)
                    .color(0xFFFFFFFF);

            RenderSystem.setShaderColor(1, 1, 1, 1f);

            BufferRenderer.drawWithGlobalProgram(buffer.end());

            RenderSystem.disableDepthTest();
            matrices.pop();
        //}
        super.render(state, matrices, vertexConsumers, light);
    }
}
