package net.zephyr.fnafur.blocks.props.floor_props.plushies.foxy_plush;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.props.floor_props.plushies.freddy_plush.FreddyPlushItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class FoxyPlushItemModel extends GeoModel<FoxyPlushItem> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geckolib/models/block/props/foxy_plush.geo.json");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/props/plushies/foxy_plush.png");
    }

    @Override
    public Identifier getAnimationResource(FoxyPlushItem animatable) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geckolib/animations/block/props/fnaf1desk.animation.json");
    }
}
