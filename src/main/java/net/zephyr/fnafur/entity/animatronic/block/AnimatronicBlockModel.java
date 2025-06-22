package net.zephyr.fnafur.entity.animatronic.block;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.util.CustomDataTickets;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class AnimatronicBlockModel<T extends AnimatronicBlockEntity> extends GeoModel<T> {

    public boolean reRender = false;

    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return reRender ? renderState.getGeckolibData(CustomDataTickets.RE_RENDER_MODEL) : renderState.getGeckolibData(CustomDataTickets.MODEL);
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return reRender ? renderState.getGeckolibData(CustomDataTickets.RE_RENDER_TEXTURE) : renderState.getGeckolibData(CustomDataTickets.TEXTURE);
    }

    @Override
    public Identifier getAnimationResource(T animatable) {
        if(animatable != null && animatable.getWorld() != null)
            return animatable.getAnimations(animatable.getWorld());
        return null;
    }

    @Override
    public @Nullable RenderLayer getRenderType(GeoRenderState renderState, Identifier texture) {
        if(renderState.getGeckolibData(CustomDataTickets.RENDER_LAYER) != null)
            return renderState.getGeckolibData(CustomDataTickets.RENDER_LAYER);
        return super.getRenderType(renderState, texture);
    }

    @Override
    public Identifier[] getAnimationResourceFallbacks(T animatable) {
        Identifier[] array = new Identifier[]{
                Identifier.of(FnafUniverseRebuilt.MOD_ID, "geckolib/animations/entity/default.animation.json")
        };
        return array;
    }
}
