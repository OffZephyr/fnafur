package net.zephyr.fnafur.blocks.props.pirates_cove.curtain;

import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class PiratesCoveCurtainRenderer extends GeoBlockRenderer<PiratesCoveCurtainBlockEntity> {
    public PiratesCoveCurtainRenderer(BlockEntityRendererFactory.Context context) {
        super(new PiratesCoveCurtainModel());
    }
}
