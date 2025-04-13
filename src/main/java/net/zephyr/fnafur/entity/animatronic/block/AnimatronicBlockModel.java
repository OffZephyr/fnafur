package net.zephyr.fnafur.entity.animatronic.block;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class AnimatronicBlockModel<T extends AnimatronicBlockEntity> extends GeoModel<T> {

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

    @Override
    public Identifier[] getAnimationResourceFallbacks(T animatable, GeoRenderer<T> renderer) {
        Identifier[] array = new Identifier[]{
                Identifier.of(FnafUniverseRebuilt.MOD_ID, "animations/entity/classic/cl_fred/cl_fred.animation.json")
        };
        return array;
    }
}
