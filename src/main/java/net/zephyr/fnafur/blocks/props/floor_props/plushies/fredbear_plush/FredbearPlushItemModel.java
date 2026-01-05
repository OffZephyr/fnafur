package net.zephyr.fnafur.blocks.props.floor_props.plushies.fredbear_plush;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.props.floor_props.plushies.freddy_plush.FreddyPlushItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class FredbearPlushItemModel extends GeoModel<FredbearPlushItem> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/props/freddy_plush");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/props/plushies/fredbear_plush.png");
    }

    @Override
    public Identifier getAnimationResource(FredbearPlushItem animatable) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/props/fnaf1desk");
    }
}
