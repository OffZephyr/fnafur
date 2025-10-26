package net.zephyr.fnafur.blocks.props.other.pirates_cove.curtain;

import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.zephyr.fnafur.blocks.common_block_entity.CommonBlockEntityRenderState;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class PiratesCoveCurtainRenderer<R extends CommonBlockEntityRenderState & GeoRenderState> extends GeoBlockRenderer<PiratesCoveCurtainBlockEntity, R> {
    public PiratesCoveCurtainRenderer(BlockEntityRendererFactory.Context context) {
        super(new PiratesCoveCurtainModel());
    }
}
