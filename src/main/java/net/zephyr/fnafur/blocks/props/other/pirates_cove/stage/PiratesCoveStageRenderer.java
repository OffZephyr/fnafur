package net.zephyr.fnafur.blocks.props.other.pirates_cove.stage;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.zephyr.fnafur.init.block_init.GeoBlockInit;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class PiratesCoveStageRenderer extends GeoBlockRenderer<PiratesCoveStageBlockEntity> {
    public PiratesCoveStageRenderer(BlockEntityRendererFactory.Context context) {
        super(new PiratesCoveStageModel());
    }

    @Override
    public void render(PiratesCoveStageBlockEntity animatable, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, int packedOverlay) {
        if(animatable.getWorld().getBlockState(animatable.getPos()).isOf(GeoBlockInit.PIRATES_COVE_STAGE) && !animatable.getWorld().getBlockState(animatable.getPos()).get(PiratesCoveStage.MAIN)) return;
        super.render(animatable, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    }
}

