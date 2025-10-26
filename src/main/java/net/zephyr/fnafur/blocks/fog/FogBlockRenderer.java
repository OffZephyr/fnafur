package net.zephyr.fnafur.blocks.fog;

import net.fabricmc.fabric.api.renderer.v1.render.RenderLayerHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FogBlockRenderer implements BlockEntityRenderer<FogBlockEntity, FogBlockEntityRenderState> {
    MinecraftClient client;
    BlockRenderManager manager;

    public FogBlockRenderer(BlockEntityRendererFactory.Context context){
        client = MinecraftClient.getInstance();
        manager = context.renderManager();
    }

    @Override
    public FogBlockEntityRenderState createRenderState() {
        return new FogBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(FogBlockEntity blockEntity, FogBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.visible = blockEntity.visible;
    }

    @Override
    public void render(FogBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        BlockPos pos = state.pos;
        BlockState blockState = state.blockState;

        if(blockState.getBlock() instanceof FogBlock) {
            if(state.visible) {
                queue.submitBlockStateModel(matrices, RenderLayer.getSolid(), this.manager.getModel(blockState), 1, 1, 1, state.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
            }
        }
    }
}
