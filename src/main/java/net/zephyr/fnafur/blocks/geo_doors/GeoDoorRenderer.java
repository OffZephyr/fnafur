package net.zephyr.fnafur.blocks.geo_doors;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.zephyr.fnafur.blocks.common_block_entity.CommonBlockEntityRenderState;
import net.zephyr.fnafur.util.CustomDataTickets;
import com.geckolib.renderer.GeoBlockRenderer;
import com.geckolib.renderer.base.GeoRenderState;

public class GeoDoorRenderer<R extends BlockEntityRenderState & GeoRenderState> extends GeoBlockRenderer<GeoDoorEntity, R> {
    public GeoDoorRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new DoorModel());
        //addRenderLayer(new GeoDoorWindowLayer(this));
    }

    @Override
    public void submit(R renderState, PoseStack poseStack, SubmitNodeCollector renderTasks, CameraRenderState cameraRenderState) {

        BlockState state = Minecraft.getInstance().level.getBlockState(renderState.blockPos);
        if(state.getBlock() instanceof GeoDoor && !state.getValue(GeoDoor.MAIN)) return;
        super.submit(renderState, poseStack, renderTasks, cameraRenderState);
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

