package net.zephyr.fnafur.item.masks;

import net.minecraft.item.Item;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class VanniMaskItemRenderer<T extends Item & GeoAnimatable, O, R extends GeoRenderState> extends GeoItemRenderer<T> {
    public VanniMaskItemRenderer() {
        super(new VanniMaskItemModel<>());
        addRenderLayer(new VanniMaskRenderLayer<>(this));
    }
}
