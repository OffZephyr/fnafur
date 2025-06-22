package net.zephyr.fnafur.entity.animatronic;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.zephyr.fnafur.util.CustomDataTickets;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class AnimatronicRenderer<T extends AnimatronicEntity, R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<T, R> {
    public AnimatronicRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new AnimatronicModel<>());

        addRenderLayer(new AnimatronicColoredLayer<>(this));
        addRenderLayer(new AnimatronicEyeLayer<>(this));
        addRenderLayer(new AnimatronicColoredEyeLayer<>(this));
    }

    @Override
    public void updateRenderState(T entity, R entityRenderState, float partialTick) {

        entityRenderState.addGeckolibData(CustomDataTickets.ENTITY_DATA, ((IEntityDataSaver)entity).getPersistentData());
        entityRenderState.addGeckolibData(CustomDataTickets.TEXTURE, entity.getTexture(entity.getWorld()));
        entityRenderState.addGeckolibData(CustomDataTickets.MODEL, entity.getModel(entity.getWorld()));
        entityRenderState.addGeckolibData(CustomDataTickets.RE_RENDER_TEXTURE, entity.getReRenderTexture(entity.getWorld()));
        entityRenderState.addGeckolibData(CustomDataTickets.RE_RENDER_MODEL, entity.getReRenderModel(entity.getWorld()));
        entityRenderState.addGeckolibData(CustomDataTickets.RENDER_LAYER, entity.getRenderType());

        super.updateRenderState(entity, entityRenderState, partialTick);
    }

    // TODO ADD data
    /*
     */
}
