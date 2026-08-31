package net.zephyr.fnafur.blocks.props.other.pirates_cove.curtain;

import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class PiratesCoveCurtainModel extends GeoModel<PiratesCoveCurtainBlockEntity> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/pirate_cove_curtains");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/pirates_cove_purple.png");
    }

    @Override
    public Identifier getAnimationResource(PiratesCoveCurtainBlockEntity animatable) {
        return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/pirate_cove");
    }
}
