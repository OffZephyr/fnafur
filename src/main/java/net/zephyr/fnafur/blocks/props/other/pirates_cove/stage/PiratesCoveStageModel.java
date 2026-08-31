package net.zephyr.fnafur.blocks.props.other.pirates_cove.stage;

import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;

public class PiratesCoveStageModel extends GeoModel<PiratesCoveStageBlockEntity> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/pirate_cove");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/pirates_cove_purple.png");
    }

    @Override
    public Identifier getAnimationResource(PiratesCoveStageBlockEntity animatable) {
        return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/pirate_cove");
    }
}
