package net.zephyr.fnafur.entity.animatronic;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.client.CustomRenderingPipelines;
import net.zephyr.fnafur.util.CustomDataTickets;
import net.zephyr.fnafur.util.jsonReaders.animatronics.AnimatronicDataHandler;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class AnimatronicModel<T extends AnimatronicEntity> extends GeoModel<T> {

    public boolean reRender = false;
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        if(!renderState.hasGeckolibData(CustomDataTickets.MODEL)) return AnimatronicDataHandler.getDefaultModel();
        return reRender && renderState.hasGeckolibData(CustomDataTickets.RE_RENDER_MODEL) ? renderState.getGeckolibData(CustomDataTickets.RE_RENDER_MODEL) : renderState.getGeckolibData(CustomDataTickets.MODEL);
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        if(!renderState.hasGeckolibData(CustomDataTickets.TEXTURE)) return AnimatronicDataHandler.getDefaultAltTexture();

        if(renderState.hasGeckolibData(CustomDataTickets.USE_EYE_TEXTURE) && Boolean.TRUE.equals(renderState.getGeckolibData(CustomDataTickets.USE_EYE_TEXTURE)) && renderState.hasGeckolibData(CustomDataTickets.EYE_TEXTURE)) {
            return renderState.getGeckolibData(CustomDataTickets.EYE_TEXTURE);
        }

        return reRender && renderState.hasGeckolibData(CustomDataTickets.RE_RENDER_TEXTURE) ? renderState.getGeckolibData(CustomDataTickets.RE_RENDER_TEXTURE) : renderState.getGeckolibData(CustomDataTickets.TEXTURE);
    }
    @Override
    public Identifier getAnimationResource(T animatable) {
        if(animatable != null)
            return animatable.getAnimations();
        return Identifier.of(FnafUniverseRebuilt.MOD_ID, AnimatronicDataHandler.getAnimationFilePath("default"));
    }

    @Override
    public Identifier[] getAnimationResourceFallbacks(T animatable) {
        return new Identifier[]{
                Identifier.of(FnafUniverseRebuilt.MOD_ID, AnimatronicDataHandler.getAnimationFilePath("default"))
        };
    }
}
