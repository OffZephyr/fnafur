package net.zephyr.fnafur.blocks.props.other.pirates_cove.curtain;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class PiratesCoveCurtainModel extends GeoModel<PiratesCoveCurtainBlockEntity> {
    @Override
    public Identifier getModelResource(PiratesCoveCurtainBlockEntity animatable, @Nullable GeoRenderer<PiratesCoveCurtainBlockEntity> renderer) {
        return Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/pirate_cove_curtains.geo.json");
    }

    @Override
    public Identifier getTextureResource(PiratesCoveCurtainBlockEntity animatable, @Nullable GeoRenderer<PiratesCoveCurtainBlockEntity> renderer) {
        return Identifier.of(FnafUniverseResuited.MOD_ID, "textures/block/pirates_cove_purple.png");
    }

    @Override
    public Identifier getAnimationResource(PiratesCoveCurtainBlockEntity animatable) {
        return Identifier.of(FnafUniverseResuited.MOD_ID, "animations/block/pirate_cove.animation.json");
    }
}
