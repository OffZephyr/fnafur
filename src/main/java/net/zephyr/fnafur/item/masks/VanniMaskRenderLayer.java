package net.zephyr.fnafur.item.masks;

import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.rendertype.RenderType;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.Item;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import org.jetbrains.annotations.Nullable;
import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.cache.model.BakedGeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.GeoRenderer;
import com.geckolib.renderer.layer.GeoRenderLayer;

public class VanniMaskRenderLayer<T extends Item & GeoAnimatable, O, R extends GeoRenderState> extends GeoRenderLayer<T, O, R> {
    public VanniMaskRenderLayer(GeoRenderer<T, O, R> renderer) {
        super(renderer);
    }

    @Override
    protected Identifier getTextureResource(R renderState) {
        return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/item/masks/vanni_mask_e.png");
    }

//    @Override
//    public void render(R renderState, MatrixStack poseStack, BakedGeoModel bakedModel, @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, int renderColor) {
//
//        RenderLayer glowRenderType = RenderLayer.getEntityTranslucentEmissive(Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/item/masks/vanni_mask_e.png"));
//        getRenderer().reRender(renderState, poseStack, bakedModel, bufferSource, glowRenderType, bufferSource.getBuffer(glowRenderType), packedLight, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
//    }
}
