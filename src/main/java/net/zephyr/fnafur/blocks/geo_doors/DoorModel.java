package net.zephyr.fnafur.blocks.geo_doors;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.geo_doors.GeoDoorEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class DoorModel extends GeoModel<GeoDoorEntity> {
    @Override
    public Identifier getModelResource(GeoDoorEntity animatable, @Nullable GeoRenderer<GeoDoorEntity> renderer) {
        return animatable.getModel();
    }

    @Override
    public Identifier getTextureResource(GeoDoorEntity animatable, @Nullable GeoRenderer<GeoDoorEntity> renderer) {
        return animatable.getTexture();
    }

    @Override
    public Identifier getAnimationResource(GeoDoorEntity animatable) {
        return Identifier.of(FnafUniverseResuited.MOD_ID, "animations/block/door/geo_door.animation.json");
    }
}
