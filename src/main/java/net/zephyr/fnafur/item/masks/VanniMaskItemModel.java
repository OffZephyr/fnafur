package net.zephyr.fnafur.item.masks;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class VanniMaskItemModel<T extends Item & GeoAnimatable> extends GeoModel<T> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geckolib/models/items/vanni_mask.geo.json");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/item/masks/vanni_mask.png");
    }

    @Override
    public Identifier getAnimationResource(T animatable) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "geckolib/animations/items/ar_mask.animation.json");
    }

    @Override
    public @Nullable RenderLayer getRenderType(GeoRenderState renderState, Identifier texture) {
        return RenderLayer.getEntityTranslucent(texture);
    }
}
