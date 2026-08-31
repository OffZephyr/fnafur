package net.zephyr.fnafur.entity.other.bear5;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.zephyr.fnafur.FnafUniverseRebuilt;

public class Bear5Renderer extends EntityRenderer<Bear5Entity, Bear5RenderState> {
    public Bear5Renderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Bear5RenderState createRenderState() {
        return new Bear5RenderState();
    }

    @Override
    public void extractRenderState(Bear5Entity entity, Bear5RenderState state, float tickDelta) {
        super.extractRenderState(entity, state, tickDelta);
        if(entity.target != null) {
            state.target = entity.target;
        }
    }

    @Override
    public void submit(Bear5RenderState renderState, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {


        //if(state.target != null && Minecraft.getInstance().player.getUuid() == state.target.getUuid()){
        Identifier texture = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/entity/other/bear_five.png");

        matrices.pushPose();
        //matrices.translate(new Vec3d(renderState.x, renderState.y, renderState.z));
        //matrices.translate(cameraState.entityPos.multiply(1));
        //matrices.translate(-1, 0, -1);
        matrices.translate(0, 1.5f, 0);
        matrices.mulPose(Minecraft.getInstance().gameRenderer.getMainCamera().rotation());
        queue.submitCustomGeometry(matrices, RenderTypes.outline(texture), (entry, buffer) -> {


            buffer
                    .addVertex(entry.pose(), 0.75f, -1.5f, 0)
                    .setUv(1, 1)
                    //.light(255, 255)
                    //.overlay(0, 0)
                    //.normal(0, 0, 0)
                    .setColor(0xFFFFFFFF);
            buffer
                    .addVertex(entry.pose(), 0.75f, 1.5f, 0)
                    .setUv(1, 0)
                    //.light(255, 255)
                    //.overlay(0, 0)
                    //.normal(0, 0, 0)
                    .setColor(0xFFFFFFFF);
            buffer
                    .addVertex(entry.pose(), -0.75f, 1.5f, 0)
                    .setUv(0, 0)
                    //.light(255, 255)
                    //.overlay(0, 0)
                    //.normal(0, 0, 0)
                    .setColor(0xFFFFFFFF);
            buffer
                    .addVertex(entry.pose(), -0.75f, -1.5f, 0)
                    .setUv(0, 1)
                    //.light(255, 255)
                    //.overlay(0, 0)
                    //.normal(0, 0, 0)
                    .setColor(0xFFFFFFFF);
        });
        matrices.popPose();
        super.submit(renderState, matrices, queue, cameraState);
    }

    @Override
    protected boolean affectedByCulling(Bear5Entity entity) {
        return false;
    }

    @Override
    public boolean shouldRender(Bear5Entity entity, Frustum frustum, double x, double y, double z) {
        return true;
    }
}
