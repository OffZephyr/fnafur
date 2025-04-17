package net.zephyr.fnafur.blocks.props.floor_props.flying_v_guitar;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class FlyingVGuitarItemModel extends GeoModel<FlyingVGuitarItem> {
    @Override
    public Identifier getModelResource(FlyingVGuitarItem animatable, @Nullable GeoRenderer<FlyingVGuitarItem> renderer) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geo/block/props/flying_v_guitar.geo.json");
    }

    @Override
    public Identifier getTextureResource(FlyingVGuitarItem animatable, @Nullable GeoRenderer<FlyingVGuitarItem> renderer) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/props/flying_v_guitar.png");
    }

    @Override
    public Identifier getAnimationResource(FlyingVGuitarItem animatable) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "animations/block/props/fnaf1desk.animation.json");
    }
}
