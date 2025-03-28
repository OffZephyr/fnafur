package net.zephyr.fnafur.entity.animatronic;

import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AnimatronicRenderer<T extends AnimatronicEntity> extends GeoEntityRenderer<T> {
    public AnimatronicRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new AnimatronicModel<>());
    }

}
