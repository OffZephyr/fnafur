package net.zephyr.fnafur.blocks.props.other.pirates_cove.stage;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class PiratesCoveStageModel extends GeoModel<PiratesCoveStageBlockEntity> {
    @Override
    public Identifier getModelResource(PiratesCoveStageBlockEntity animatable, @Nullable GeoRenderer<PiratesCoveStageBlockEntity> renderer) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geo/block/pirate_cove.geo.json");
    }

    @Override
    public Identifier getTextureResource(PiratesCoveStageBlockEntity animatable, @Nullable GeoRenderer<PiratesCoveStageBlockEntity> renderer) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/pirates_cove_purple.png");
    }

    @Override
    public Identifier getAnimationResource(PiratesCoveStageBlockEntity animatable) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "animations/block/pirate_cove.animation.json");
    }
}
