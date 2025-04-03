package net.zephyr.fnafur.entity.animatronic;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class AnimatronicModel<T extends AnimatronicEntity> extends GeoModel<T> {
    @Override
    public Identifier getModelResource(T animatable, @Nullable GeoRenderer<T> renderer) {
        String model = ((IEntityDataSaver)animatable).getPersistentData().getCompound("alt").getString("model");
        if(!model.isEmpty()){
            return Identifier.of(FnafUniverseResuited.MOD_ID, model);
        }

        return Identifier.of(FnafUniverseResuited.MOD_ID, "geo/entity/default/endo_01/endo_01.geo.json");
    }

    @Override
    public Identifier getTextureResource(T animatable, @Nullable GeoRenderer<T> renderer) {
        String texture = ((IEntityDataSaver)animatable).getPersistentData().getCompound("alt").getString("texture");
        if(!texture.isEmpty()){
            return Identifier.of(FnafUniverseResuited.MOD_ID, texture);
        }

        return Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/default/endo_01/endo_01.png");
    }

    @Override
    public Identifier getAnimationResource(T animatable) {
        String animations = ((IEntityDataSaver) animatable).getPersistentData().getCompound("alt").getString("animations");
        if (!animations.isEmpty()) {
            System.out.println(animations);
            return Identifier.of(FnafUniverseResuited.MOD_ID, animations);
        }

        return Identifier.of(FnafUniverseResuited.MOD_ID, "animations/entity/classic/cl_fred/cl_fred.animation.json");
    }
}
