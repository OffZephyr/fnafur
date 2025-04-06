package net.zephyr.fnafur.blocks.props.base.geo;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class GeoPropModel<T extends GeoPropBlockEntity> extends GeoModel<T> {

    public boolean reRender = false;
    @Override
    public Identifier getModelResource(T animatable, @Nullable GeoRenderer<T> renderer) {
        if (animatable != null && animatable.getWorld() != null) {
            return reRender ? animatable.getReRenderModel(animatable.getWorld()) : animatable.getModel(animatable.getWorld());
        }
        return null;
    }

    @Override
    public Identifier getTextureResource(T animatable, @Nullable GeoRenderer<T> renderer) {
        if(animatable != null && animatable.getWorld() != null)
            return reRender ? animatable.getReRenderTexture(animatable.getWorld()) : animatable.getTexture(animatable.getWorld());
        return null;
    }

    @Override
    public Identifier getAnimationResource(T animatable) {
        if(animatable != null && animatable.getWorld() != null)
            return animatable.getAnimations(animatable.getWorld());
        return null;
    }

    @Override
    public @Nullable RenderLayer getRenderType(T animatable, Identifier texture) {
        if(animatable != null && animatable.getWorld() != null && animatable.getRenderType() != null)
            return animatable.getRenderType();

        return super.getRenderType(animatable, texture);
    }
}
