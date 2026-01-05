package net.zephyr.fnafur.blocks.props.floor_props.plushies.chica_plush;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.props.floor_props.plushies.bonnie_plush.BonniePlushItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class ChicaPlushItemModel extends GeoModel<ChicaPlushItem> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/props/chica_plush");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/props/plushies/chica_plush.png");
    }

    @Override
    public Identifier getAnimationResource(ChicaPlushItem animatable) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/props/fnaf1desk");
    }
}
