package net.zephyr.fnafur.entity.animatronic;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.zephyr.fnafur.util.CustomDataTickets;
import net.zephyr.fnafur.util.jsonReaders.animatronics.AnimatronicDataHandler;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import java.util.List;

public class AnimatronicRenderer<T extends AnimatronicEntity, R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<T, R> {
    private final List<String> EYE_BONE_NAME_LIST = List.of(
         "eyeleft",
         "eyeright"
    );

    public AnimatronicRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new AnimatronicModel<>());

        addRenderLayer(new AnimatronicColoredLayer<>(this));
        //addRenderLayer(new AnimatronicEyeLayer<>(this));
        addRenderLayer(new AnimatronicColoredEyeLayer<>(this));
    }

    @Override
    public void render(R renderState, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {

        float scale = 1;
        if(Boolean.TRUE.equals(renderState.getGeckolibData(CustomDataTickets.IS_ENTITY_PREVIEW))){
            scale = AnimatronicDataHandler.getPreviewScale(renderState.getGeckolibData(CustomDataTickets.ENTITY_CHARA), renderState.getGeckolibData(CustomDataTickets.ENTITY_ALT));

        }
        poseStack.push();
        poseStack.scale(scale, scale, scale);
        super.render(renderState, poseStack, bufferSource, packedLight);
        poseStack.pop();
    }

    @Override
    public R fillRenderState(T animatable, Void relatedObject, R renderState, float partialTick) {

        R state = super.fillRenderState(animatable, relatedObject, renderState, partialTick);

        if(animatable.isMenu){
            state.addGeckolibData(DataTickets.PACKED_LIGHT, LightmapTextureManager.MAX_LIGHT_COORDINATE);
            state.addGeckolibData(DataTickets.PACKED_OVERLAY, OverlayTexture.DEFAULT_UV);
            state.addGeckolibData(DataTickets.RENDER_COLOR, 0xFFFFFFFF);
        }

        state.addGeckolibData(CustomDataTickets.IS_ENTITY_PREVIEW, animatable.isMenu);
        state.addGeckolibData(CustomDataTickets.ENTITY_CHARA, animatable.getChara());
        state.addGeckolibData(CustomDataTickets.ENTITY_ALT, animatable.getAlt());
        state.addGeckolibData(CustomDataTickets.ENTITY_EYES, animatable.getEyes());
        state.addGeckolibData(CustomDataTickets.ENTITY_DATA, ((IEntityDataSaver)animatable).getPersistentData());
        state.addGeckolibData(CustomDataTickets.TEXTURE, animatable.getTexture(animatable.getWorld()));
        state.addGeckolibData(CustomDataTickets.EYE_TEXTURE, animatable.getEyeTexture(animatable.getWorld()));
        state.addGeckolibData(CustomDataTickets.EYE_NONE, animatable.isEmptyEye());
        state.addGeckolibData(CustomDataTickets.MODEL, animatable.getModel(animatable.getWorld()));
        state.addGeckolibData(CustomDataTickets.RE_RENDER_TEXTURE, animatable.getReRenderTexture(animatable.getWorld()));
        state.addGeckolibData(CustomDataTickets.RE_RENDER_MODEL, animatable.getReRenderModel(animatable.getWorld()));
        state.addGeckolibData(CustomDataTickets.RENDER_LAYER, animatable.getRenderType(animatable.getTexture(animatable.getWorld())));

        return state;
    }

    @Override
    public void renderRecursively(R renderState, MatrixStack poseStack, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, int packedLight, int packedOverlay, int renderColor) {

        if(!isReRender && EYE_BONE_NAME_LIST.contains(bone.getName())){
            if(renderState.getGeckolibData(CustomDataTickets.EYE_NONE).booleanValue()) return;
            renderState.addGeckolibData(CustomDataTickets.USE_EYE_TEXTURE, true);
            ((AnimatronicModel<T>)model).eyeTexture = true;
            renderType = getRenderType(renderState, getTextureLocation(renderState));
        }
        buffer = bufferSource.getBuffer(renderType);
        super.renderRecursively(renderState, poseStack, bone, renderType, bufferSource, buffer, isReRender, packedLight, packedOverlay, renderColor);
        ((AnimatronicModel<T>)model).eyeTexture = false;
        renderState.addGeckolibData(CustomDataTickets.USE_EYE_TEXTURE, false);
    }
}
