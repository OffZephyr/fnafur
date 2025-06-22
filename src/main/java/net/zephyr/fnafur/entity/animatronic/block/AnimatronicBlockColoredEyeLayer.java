package net.zephyr.fnafur.entity.animatronic.block;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.util.CustomDataTickets;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class AnimatronicBlockColoredEyeLayer<T extends AnimatronicBlockEntity, O, R extends GeoRenderState> extends GeoRenderLayer<T, O, R> {

    public AnimatronicBlockColoredEyeLayer(GeoRenderer<T, O, R> renderer) {
        super(renderer);
    }

    @Override
    public void render(R renderState, MatrixStack poseStack, BakedGeoModel bakedModel, @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, int renderColor) {

        NbtCompound altNbt = renderState.getGeckolibData(CustomDataTickets.ENTITY_DATA).getCompound("alt").get();

        if(!altNbt.isEmpty() && altNbt.contains("eyes_recolorable_textures_size")){
            int size = altNbt.getInt("eyes_recolorable_textures_size").get();

            for(int i = 0; i < size; i++){
                String texture = altNbt.getString("eyes_recolorable_textures" + i).get();
                int[] color = altNbt.getIntArray("eye_color" + i).get();

                if(texture.isEmpty()) return;

                RenderLayer translucentRenderType = RenderLayer.getEntityTranslucent(Identifier.of(FnafUniverseRebuilt.MOD_ID, texture));

                getRenderer().reRender(renderState, poseStack, getDefaultBakedModel(renderState), bufferSource, translucentRenderType, bufferSource.getBuffer(translucentRenderType), packedLight, packedOverlay, ColorHelper.getArgb(255, color[0], color[1], color[2]));
            }
        }
    }
}
