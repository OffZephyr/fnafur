package net.zephyr.fnafur.blocks.geo_doors;

import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.util.CustomDataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class DoorModel extends GeoModel<GeoDoorEntity> {

    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return renderState.getGeckolibData(CustomDataTickets.MODEL);
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return renderState.getGeckolibData(CustomDataTickets.TEXTURE);
    }

    @Override
    public Identifier getAnimationResource(GeoDoorEntity animatable) {
        return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "block/door/geo_door");
    }
}
