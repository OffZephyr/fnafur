package net.zephyr.fnafur.blocks.geo_doors;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.zephyr.fnafur.util.CustomDataTickets;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class GeoDoorRenderer extends GeoBlockRenderer<GeoDoorEntity> {
    public GeoDoorRenderer(BlockEntityRendererFactory.Context context) {
        super(new DoorModel());
        addRenderLayer(new GeoDoorWindowLayer(this));
    }

    @Override
    public void render(GeoDoorEntity animatable, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, int packedOverlay, Vec3d cameraPosition) {

        BlockState state = animatable.getWorld().getBlockState(animatable.getPos());
        if(state.getBlock() instanceof GeoDoor && !state.get(GeoDoor.MAIN)) return;
        super.render(animatable, partialTick, poseStack, bufferSource, packedLight, packedOverlay, cameraPosition);
    }

    @Override
    public GeoRenderState fillRenderState(GeoDoorEntity animatable, Void relatedObject, GeoRenderState renderState, float partialTick) {

        GeoRenderState state = super.fillRenderState(animatable, relatedObject, renderState, partialTick);

        state.addGeckolibData(CustomDataTickets.MODEL, animatable.getModel());
        state.addGeckolibData(CustomDataTickets.TEXTURE, animatable.getTexture());
        state.addGeckolibData(CustomDataTickets.DOOR_WINDOW_TEXTURE, animatable.getWindowTexture());

        return state;
    }
}

