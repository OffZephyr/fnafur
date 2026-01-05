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
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "items/vanni_mask");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/item/masks/vanni_mask.png");
    }

    @Override
    public Identifier getAnimationResource(T animatable) {
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, "items/ar_mask");
    }
}
