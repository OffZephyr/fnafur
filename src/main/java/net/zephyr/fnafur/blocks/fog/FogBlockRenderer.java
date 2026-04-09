package net.zephyr.fnafur.blocks.fog;

import net.fabricmc.fabric.api.renderer.v1.render.RenderLayerHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.CameraRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class FogBlockRenderer implements BlockEntityRenderer<FogBlockEntity, FogBlockEntityRenderState> {
    Minecraft client;
    BlockRenderDispatcher manager;

    public FogBlockRenderer(BlockEntityRendererProvider.Context context){
        client = Minecraft.getInstance();
        manager = context.blockRenderDispatcher();
    }

    @Override
    public FogBlockEntityRenderState createRenderState() {
        return new FogBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(FogBlockEntity blockEntity, FogBlockEntityRenderState state, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.visible = blockEntity.visible;
    }

    @Override
    public void submit(FogBlockEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        BlockPos pos = state.blockPos;
        BlockState blockState = state.blockState;

        if(blockState.getBlock() instanceof FogBlock) {
            if(state.visible) {
                queue.submitBlockModel(matrices, RenderTypes.solidMovingBlock(), this.manager.getBlockModel(blockState), 1, 1, 1, state.lightCoords, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            }
        }
    }
}
