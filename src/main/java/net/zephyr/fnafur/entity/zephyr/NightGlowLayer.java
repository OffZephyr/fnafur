package net.zephyr.fnafur.entity.zephyr;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.entity.base.DefaultEntity;
import net.zephyr.fnafur.util.jsonReaders.entity_skins.EntityDataManager;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.fnafur.util.mixinAccessing.IGetClientManagers;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class NightGlowLayer<T extends DefaultEntity> extends GeoRenderLayer<T> {
    public NightGlowLayer(GeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack poseStack, T animatable, BakedGeoModel bakedModel, @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        String skin = ((IEntityDataSaver) animatable).getPersistentData().getString("Reskin");

        Identifier TEXTURE = animatable.getSkin(skin).glow_texture;

        RenderLayer glowRenderType = RenderLayer.getEyes(TEXTURE);

        if (animatable.getAIHour(animatable.getWorld()) < 24 || animatable.getAIHour(animatable.getWorld()) > 72) {
            getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, glowRenderType, bufferSource.getBuffer(glowRenderType), partialTick, packedLight, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
        }
    }
}
