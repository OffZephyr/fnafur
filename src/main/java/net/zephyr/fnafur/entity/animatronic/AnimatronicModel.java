package net.zephyr.fnafur.entity.animatronic;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.init.entity_init.CharacterInit;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class AnimatronicModel<T extends AnimatronicEntity> extends GeoModel<T> {
    public String character = "endo_01";
    public static final String DEFAULT_CHARACTER = "endo_01";
    @Override
    public Identifier getModelResource(T animatable, @Nullable GeoRenderer<T> renderer) {
        String category = CharacterInit.CHARACTER_MAP.get(character).CATEGORY;
        Identifier model = Identifier.of(FnafUniverseResuited.MOD_ID, "geo/entity/" + category + "/" + character + "/" + character + ".geo.json");
        return model;
    }
    @Override
    public Identifier getTextureResource(T animatable, @Nullable GeoRenderer<T> renderer) {
        String category = CharacterInit.CHARACTER_MAP.get(character).CATEGORY;
        return Identifier.of(FnafUniverseResuited.MOD_ID, "textures/entity/" + category + "/" + character + "/" + character + ".png");
    }

    @Override
    public Identifier getAnimationResource(T animatable) {
        //return Identifier.of(FnafUniverseResuited.MOD_ID, "animations/entity/classic/" + characterBase + "/" + characterBase + ".animation.json");
        return Identifier.of(FnafUniverseResuited.MOD_ID, "animations/entity/classic/" + "cl_fred" + "/" + "cl_fred" + ".animation.json");
    }

    public AnimatronicModel<T> setCharacter(String character) {
        this.character = character;
        return this;
    }
}
