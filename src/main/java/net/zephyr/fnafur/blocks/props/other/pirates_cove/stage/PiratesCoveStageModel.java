package net.zephyr.fnafur.blocks.props.other.pirates_cove.stage;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class PiratesCoveStageModel extends GeoModel<PiratesCoveStageBlockEntity> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geckolib/models/block/pirate_cove.geo.json");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/pirates_cove_purple.png");
    }

    @Override
    public Identifier getAnimationResource(PiratesCoveStageBlockEntity animatable) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geckolib/animations/block/pirate_cove.animation.json");
    }
}
