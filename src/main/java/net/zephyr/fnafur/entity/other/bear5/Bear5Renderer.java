package net.zephyr.fnafur.entity.other.bear5;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.FnafUniverseRebuilt;

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
        super.updateRenderState(entity, state, tickDelta);
        if(entity.target != null) {
            state.target = entity.target;
        }
    }

    @Override
    public void render(Bear5RenderState renderState, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {


        //if(state.target != null && MinecraftClient.getInstance().player.getUuid() == state.target.getUuid()){
        Identifier texture = Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/entity/other/bear_five.png");

        matrices.push();
        //matrices.translate(new Vec3d(renderState.x, renderState.y, renderState.z));
        //matrices.translate(cameraState.entityPos.multiply(1));
        //matrices.translate(-1, 0, -1);
        queue.submitCustom(matrices, RenderLayers.outlineNoCull(texture), (entry, buffer) -> {

            matrices.translate(0, 1.5f, 0);
            matrices.multiply(MinecraftClient.getInstance().gameRenderer.getCamera().getRotation());

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
            matrices.pop();
        });
        super.render(renderState, matrices, queue, cameraState);
    }

    @Override
    protected boolean canBeCulled(Bear5Entity entity) {
        return false;
    }

    @Override
    public boolean shouldRender(Bear5Entity entity, Frustum frustum, double x, double y, double z) {
        return true;
    }
}
