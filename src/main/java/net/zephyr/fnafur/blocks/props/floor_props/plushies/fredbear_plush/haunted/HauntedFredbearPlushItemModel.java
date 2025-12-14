package net.zephyr.fnafur.blocks.props.floor_props.plushies.fredbear_plush.haunted;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.props.floor_props.plushies.fredbear_plush.FredbearPlushItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class HauntedFredbearPlushItemModel extends GeoModel<HauntedFredbearPlushItem> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geckolib/models/block/props/freddy_plush.geo.json");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/props/plushies/haunted_fredbear_plush.png");
    }

    @Override
    public Identifier getAnimationResource(HauntedFredbearPlushItem animatable) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geckolib/animations/block/props/fnaf1desk.animation.json");
    }
}
