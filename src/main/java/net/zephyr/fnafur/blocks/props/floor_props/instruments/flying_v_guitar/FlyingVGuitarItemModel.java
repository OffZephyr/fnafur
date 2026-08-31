package net.zephyr.fnafur.blocks.props.floor_props.instruments.flying_v_guitar;

import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;

public class FlyingVGuitarItemModel extends GeoModel<FlyingVGuitarItem> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/flying_v_guitar");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/block/props/flying_v_guitar.png");
    }

    @Override
    public Identifier getAnimationResource(FlyingVGuitarItem animatable) {
        return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/props/fnaf1desk");
    }
}
