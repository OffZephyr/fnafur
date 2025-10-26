package net.zephyr.fnafur.blocks.geo_doors;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.zephyr.fnafur.blocks.common_block_entity.CommonBlockEntityRenderState;
import net.zephyr.fnafur.util.CustomDataTickets;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class GeoDoorRenderer<R extends CommonBlockEntityRenderState & GeoRenderState> extends GeoBlockRenderer<GeoDoorEntity, R> {
    public GeoDoorRenderer(BlockEntityRendererFactory.Context context) {
        super(new DoorModel());
        //addRenderLayer(new GeoDoorWindowLayer(this));
    }

    @Override
    public void render(R renderState, MatrixStack poseStack, OrderedRenderCommandQueue renderTasks, CameraRenderState cameraRenderState) {

        BlockState state = MinecraftClient.getInstance().world.getBlockState(renderState.pos);
        if(state.getBlock() instanceof GeoDoor && !state.get(GeoDoor.MAIN)) return;
        super.render(renderState, poseStack, renderTasks, cameraRenderState);
    }

    @Override
    public R fillRenderState(GeoDoorEntity animatable, Void relatedObject, R renderState, float partialTick) {
        R state = super.fillRenderState(animatable, relatedObject, renderState, partialTick);

        state.addGeckolibData(CustomDataTickets.MODEL, animatable.getModel());
        state.addGeckolibData(CustomDataTickets.TEXTURE, animatable.getTexture());
        state.addGeckolibData(CustomDataTickets.DOOR_WINDOW_TEXTURE, animatable.getWindowTexture());

        return state;
    }
}

