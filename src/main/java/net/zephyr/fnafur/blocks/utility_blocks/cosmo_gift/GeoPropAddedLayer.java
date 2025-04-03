package net.zephyr.fnafur.blocks.utility_blocks.cosmo_gift;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class GeoPropAddedLayer<T extends GeoPropBlockEntity> extends GeoRenderLayer<T> {
    final RenderLayer layer;
    public GeoPropAddedLayer(GeoRenderer<T> entityRendererIn, RenderLayer layer) {
        super(entityRendererIn);
        this.layer = layer;
    }

    @Override
    public void render(MatrixStack poseStack, T animatable, BakedGeoModel bakedModel, @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int renderColor) {

        getRenderer().reRender(getGeoModel().getBakedModel(Identifier.of(FnafUniverseResuited.MOD_ID, "geo/block/props/giftbox_overlay.geo.json")), poseStack, bufferSource, animatable, layer, bufferSource.getBuffer(layer), partialTick, packedLight, packedOverlay, renderColor);
    }
}
