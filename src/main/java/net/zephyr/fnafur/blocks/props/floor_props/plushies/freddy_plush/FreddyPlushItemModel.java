package net.zephyr.fnafur.blocks.props.floor_props.plushies.freddy_plush;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.props.floor_props.instruments.flying_v_guitar.FlyingVGuitarItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class FreddyPlushItemModel extends GeoModel<FreddyPlushItem> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geckolib/models/block/props/freddy_plush.geo.json");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/props/plushies/freddy_plush.png");
    }

    @Override
    public Identifier getAnimationResource(FreddyPlushItem animatable) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geckolib/animations/block/props/fnaf1desk.animation.json");
    }
}
