package net.zephyr.fnafur.blocks.props.base.geo;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class GeoPropModel<T extends GeoPropBlockEntity> extends GeoModel<T> {

    @Override
    public Identifier getModelResource(T animatable, @Nullable GeoRenderer<T> renderer) {
        return animatable.getModel();
    }

    @Override
    public Identifier getTextureResource(T animatable, @Nullable GeoRenderer<T> renderer) {
        return animatable.getTexture();
    }

    @Override
    public Identifier getAnimationResource(T animatable) {
        return animatable.getAnimations();
    }
}
