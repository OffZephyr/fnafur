package net.zephyr.fnafur.item.masks;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class VanniMaskRenderLayer<T extends Item & GeoAnimatable, O, R extends GeoRenderState> extends GeoRenderLayer<T, O, R> {
    public VanniMaskRenderLayer(GeoRenderer<T, O, R> renderer) {
        super(renderer);
    }

    @Override
    public void render(R renderState, MatrixStack poseStack, BakedGeoModel bakedModel, @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, int renderColor) {
        RenderLayer glowRenderType = RenderLayer.getEntityTranslucentEmissive(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/item/masks/vanni_mask_e.png"));
        getRenderer().reRender(renderState, poseStack, bakedModel, bufferSource, glowRenderType, bufferSource.getBuffer(glowRenderType), packedLight, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
    }
}
