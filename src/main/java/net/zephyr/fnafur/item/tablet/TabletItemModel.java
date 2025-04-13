package net.zephyr.fnafur.item.tablet;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class TabletItemModel extends GeoModel<TabletItem> {
    @Override
    public Identifier getModelResource(TabletItem animatable, @Nullable GeoRenderer<TabletItem> renderer) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geo/items/tablet.geo.json");
    }

    @Override
    public Identifier getTextureResource(TabletItem animatable, @Nullable GeoRenderer<TabletItem> renderer) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/item/security_tablet.png");
    }

    @Override
    public Identifier getAnimationResource(TabletItem animatable) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "animations/items/tablet.animation.json");
    }
}
