package net.zephyr.fnafur.entity.animatronic;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class AnimatronicColoredEyeLayer<T extends AnimatronicEntity> extends GeoRenderLayer<T> {
    public AnimatronicColoredEyeLayer(GeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack poseStack, T animatable, BakedGeoModel bakedModel, @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int renderColor) {

        NbtCompound altNbt = ((IEntityDataSaver)animatable).getPersistentData().getCompound("alt");

        if(!altNbt.isEmpty() && altNbt.contains("eyes_recolorable_textures_size")){
            int size = altNbt.getInt("eyes_recolorable_textures_size");

            for(int i = 0; i < size; i++){
                String texture = altNbt.getString("eyes_recolorable_textures" + i);
                int[] color = altNbt.getIntArray("eye_color" + i);

                RenderLayer translucentRenderType = RenderLayer.getEntityTranslucent(Identifier.of(FnafUniverseRebuilt.MOD_ID, texture));

                getRenderer().reRender(getDefaultBakedModel(animatable, renderer), poseStack, bufferSource, animatable, translucentRenderType, bufferSource.getBuffer(translucentRenderType), partialTick, packedLight, packedOverlay, ColorHelper.getArgb(255, color[0], color[1], color[2]));
            }
        }
    }
}
