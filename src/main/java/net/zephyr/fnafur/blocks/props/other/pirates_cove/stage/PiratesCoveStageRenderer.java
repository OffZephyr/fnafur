package net.zephyr.fnafur.blocks.props.other.pirates_cove.stage;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.phys.Vec3;
import net.zephyr.fnafur.blocks.common_block_entity.CommonBlockEntityRenderState;
import net.zephyr.fnafur.init.block_init.GeoBlockInit;
import net.zephyr.fnafur.util.CustomDataTickets;
import com.geckolib.renderer.GeoBlockRenderer;
import com.geckolib.renderer.base.GeoRenderState;

public class PiratesCoveStageRenderer<R extends BlockEntityRenderState & GeoRenderState> extends GeoBlockRenderer<PiratesCoveStageBlockEntity, R> {
    public PiratesCoveStageRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new PiratesCoveStageModel());
    }


    @Override
    public R fillRenderState(PiratesCoveStageBlockEntity animatable, Void relatedObject, R renderState, float partialTick) {
        renderState.addGeckolibData(CustomDataTickets.IS_VISIBLE, animatable.getLevel().getBlockState(animatable.getBlockPos()).is(GeoBlockInit.PIRATES_COVE_STAGE) && animatable.getLevel().getBlockState(animatable.getBlockPos()).getValue(PiratesCoveStage.MAIN));
        return super.fillRenderState(animatable, relatedObject, renderState, partialTick);
    }

    @Override
    public void submit(R renderState, PoseStack poseStack, SubmitNodeCollector renderTasks, CameraRenderState cameraRenderState) {
        if(Boolean.FALSE.equals(renderState.getGeckolibData(CustomDataTickets.IS_VISIBLE))) return;
        super.submit(renderState, poseStack, renderTasks, cameraRenderState);
    }
}

