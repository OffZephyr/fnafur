package net.zephyr.fnafur.blocks.props.base.geo;

import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.util.CustomDataTickets;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class GeoPropModel<T extends GeoPropBlockEntity> extends GeoModel<T> {

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
        if(animatable != null && animatable.getLevel() != null)
            return animatable.getAnimations(animatable.getLevel());
        return null;
    }
}
