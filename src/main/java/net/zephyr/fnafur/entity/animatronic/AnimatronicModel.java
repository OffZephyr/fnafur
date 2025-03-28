package net.zephyr.fnafur.entity.animatronic;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class AnimatronicModel<T extends AnimatronicEntity> extends GeoModel<T> {
    @Override
    public Identifier getModelResource(T animatable, @Nullable GeoRenderer<T> renderer) {
        //return Identifier.of(FnafUniverseResuited.MOD_ID, "geo/entity/classic/" + "cl_fred" + "/" + "cl_fred" + ".geo.json");
        return Identifier.of(FnafUniverseResuited.MOD_ID, "geo/entity/default/endo_01/endo_01.geo.json");
    }

    @Override
    public Identifier getTextureResource(T animatable, @Nullable GeoRenderer<T> renderer) {
        //return Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/classic/" + "cl_fred" + "/" + "cl_fred" + ".png");
        return Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/default/endo_01/endo_01.png");
    }

    @Override
    public Identifier getAnimationResource(T animatable) {
        //return Identifier.of(FnafUniverseResuited.MOD_ID, "animations/entity/classic/" + "cl_fred" + "/" + "cl_fred" + ".animation.json");
        return Identifier.of(FnafUniverseResuited.MOD_ID, "animations/entity/classic/cl_fred/cl_fred.animation.json");
    }
}
