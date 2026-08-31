package net.zephyr.fnafur.entity.animatronic;

import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.GeoRenderer;
import com.geckolib.renderer.layer.GeoRenderLayer;

public class AnimatronicColoredEyeLayer<T extends AnimatronicEntity, O, R extends GeoRenderState> extends GeoRenderLayer<T, O, R> {

    public AnimatronicColoredEyeLayer(GeoRenderer<T, O, R> renderer) {
        super(renderer);
    }

//    @Override
//    public void render(R renderState, MatrixStack poseStack, BakedGeoModel bakedModel, @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, int renderColor) {
//
//        CompoundTag altNbt = renderState.getGeckolibData(CustomDataTickets.ENTITY_DATA).getCompound("alt").orElse(new CompoundTag());
//
//        if(altNbt.isEmpty()) return;
//
//        if(!altNbt.isEmpty() && altNbt.contains("eyes_recolorable_textures_size")){
//            int size = altNbt.getInt("eyes_recolorable_textures_size").orElse(0);
//
//            for(int i = 0; i < size; i++){
//                String texture = altNbt.getString("eyes_recolorable_textures" + i).orElse("");
//                int[] color = altNbt.getIntArray("eye_color" + i).orElse(new int[0]);
//
//                RenderLayer translucentRenderType = RenderLayer.getEntityTranslucent(Identifier.of(FnafUniverseRebuilt.MOD_ID, texture));
//
//                getRenderer().reRender(renderState, poseStack, getDefaultBakedModel(renderState), bufferSource, translucentRenderType, bufferSource.getBuffer(translucentRenderType), packedLight, packedOverlay, ColorHelper.getArgb(255, color[0], color[1], color[2]));
//            }
//        }
//    }
}
