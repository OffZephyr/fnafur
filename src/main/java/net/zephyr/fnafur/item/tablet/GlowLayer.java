package net.zephyr.fnafur.item.tablet;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class GlowLayer extends GeoRenderLayer<TabletItem> {
    private static final String texture = "textures/item/security_tablet_e.png";
    private static Identifier LAYER = Identifier.of(FnafUniverseRebuilt.MOD_ID, texture);

    public GlowLayer(GeoRenderer<TabletItem> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack poseStack, TabletItem animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int renderColor) {
        RenderLayer glowRenderType = RenderLayer.getEyes(LAYER);
        getRenderer().reRender(getDefaultBakedModel(animatable, renderer), poseStack, bufferSource, animatable, glowRenderType, bufferSource.getBuffer(glowRenderType), partialTick, packedLight, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
    }
}
