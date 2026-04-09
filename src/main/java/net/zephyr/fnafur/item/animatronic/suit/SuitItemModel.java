package net.zephyr.fnafur.item.animatronic.suit;

import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.client.CustomRenderingPipelines;
import net.zephyr.fnafur.util.CustomDataTickets;
import net.zephyr.fnafur.util.jsonReaders.animatronics.AnimatronicDataHandler;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class SuitItemModel extends GeoModel<SuitItem> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        if(renderState.hasGeckolibData(CustomDataTickets.MODEL)){
            return renderState.getGeckolibData(CustomDataTickets.MODEL);
        }
        return AnimatronicDataHandler.getDefaultModel();
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        if(renderState.hasGeckolibData(CustomDataTickets.TEXTURE)){
            return renderState.getGeckolibData(CustomDataTickets.TEXTURE);
        }
        return AnimatronicDataHandler.getDefaultAltTexture();
    }


    @Override
    public Identifier getAnimationResource(SuitItem animatable) {
        return null;
    }
}
