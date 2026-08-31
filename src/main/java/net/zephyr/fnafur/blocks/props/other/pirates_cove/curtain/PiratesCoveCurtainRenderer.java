package net.zephyr.fnafur.blocks.props.other.pirates_cove.curtain;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.zephyr.fnafur.blocks.common_block_entity.CommonBlockEntityRenderState;
import com.geckolib.renderer.GeoBlockRenderer;
import com.geckolib.renderer.base.GeoRenderState;

public class PiratesCoveCurtainRenderer<R extends CommonBlockEntityRenderState & GeoRenderState> extends GeoBlockRenderer<PiratesCoveCurtainBlockEntity, R> {
    public PiratesCoveCurtainRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new PiratesCoveCurtainModel());
    }
}
