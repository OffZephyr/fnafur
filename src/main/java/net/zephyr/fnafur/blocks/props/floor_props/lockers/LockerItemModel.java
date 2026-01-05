package net.zephyr.fnafur.blocks.props.floor_props.lockers;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.props.floor_props.plushies.bonnie_plush.BonniePlushItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class LockerItemModel extends GeoModel<LockerItem> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/props/locker");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/block/props/plushies/locker_gray.png");
    }

    @Override
    public Identifier getAnimationResource(LockerItem animatable) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "block/props/fnaf1desk");
    }
}
