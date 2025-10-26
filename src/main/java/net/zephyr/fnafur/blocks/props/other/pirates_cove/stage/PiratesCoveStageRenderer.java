package net.zephyr.fnafur.blocks.props.other.pirates_cove.stage;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.blocks.common_block_entity.CommonBlockEntityRenderState;
import net.zephyr.fnafur.init.block_init.GeoBlockInit;
import net.zephyr.fnafur.util.CustomDataTickets;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class PiratesCoveStageRenderer<R extends CommonBlockEntityRenderState & GeoRenderState> extends GeoBlockRenderer<PiratesCoveStageBlockEntity, R> {
    public PiratesCoveStageRenderer(BlockEntityRendererFactory.Context context) {
        super(new PiratesCoveStageModel());
    }


    @Override
    public R fillRenderState(PiratesCoveStageBlockEntity animatable, Void relatedObject, R renderState, float partialTick) {
        renderState.addGeckolibData(CustomDataTickets.IS_VISIBLE, animatable.getWorld().getBlockState(animatable.getPos()).isOf(GeoBlockInit.PIRATES_COVE_STAGE) && !animatable.getWorld().getBlockState(animatable.getPos()).get(PiratesCoveStage.MAIN));
        return super.fillRenderState(animatable, relatedObject, renderState, partialTick);
    }

    @Override
    public void render(R renderState, MatrixStack poseStack, OrderedRenderCommandQueue renderTasks, CameraRenderState cameraRenderState) {
        if(Boolean.FALSE.equals(renderState.getGeckolibData(CustomDataTickets.IS_VISIBLE))) return;
        super.render(renderState, poseStack, renderTasks, cameraRenderState);
    }
}

