package net.zephyr.fnafur.blocks.geo_doors;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class GeoDoorRenderer extends GeoBlockRenderer<GeoDoorEntity> {
    public GeoDoorRenderer(BlockEntityRendererFactory.Context context) {
        super(new DoorModel());
    }

    @Override
    public void render(GeoDoorEntity animatable, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, int packedOverlay) {
        BlockState state = animatable.getWorld().getBlockState(animatable.getPos());
        if(state.getBlock() instanceof GeoDoor && !state.get(GeoDoor.MAIN)) return;
        super.render(animatable, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    }
}
