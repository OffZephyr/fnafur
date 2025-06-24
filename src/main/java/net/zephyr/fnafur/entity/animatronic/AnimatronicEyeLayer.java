package net.zephyr.fnafur.entity.animatronic;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.util.CustomDataTickets;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class AnimatronicEyeLayer<T extends AnimatronicEntity, O, R extends GeoRenderState> extends GeoRenderLayer<T, O, R> {

    public AnimatronicEyeLayer(GeoRenderer<T, O, R> renderer) {
        super(renderer);
    }

    @Override
    public void render(R renderState, MatrixStack poseStack, BakedGeoModel bakedModel, @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, int renderColor) {

        NbtCompound altNbt = renderState.getGeckolibData(CustomDataTickets.ENTITY_DATA).getCompound("alt").orElse(new NbtCompound());

        if(altNbt.isEmpty()) return;

        String texture = altNbt.getString("eyes_texture").orElse("");

        if(texture.isEmpty()) return;

        RenderLayer translucentRenderType = RenderLayer.getEntityTranslucent(Identifier.of(FnafUniverseRebuilt.MOD_ID, texture));

        getRenderer().reRender(renderState, poseStack, getDefaultBakedModel(renderState), bufferSource, translucentRenderType, bufferSource.getBuffer(translucentRenderType), packedLight, packedOverlay, 0xFFFFFFFF);
    }
}
