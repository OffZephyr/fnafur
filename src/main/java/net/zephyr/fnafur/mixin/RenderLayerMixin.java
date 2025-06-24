package net.zephyr.fnafur.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.util.Identifier;
import net.minecraft.util.TriState;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.util.mixinAccessing.IUniverseRenderLayers;
import net.zephyr.fnafur.util.mixinAccessing.IUniverseRenderPipelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(RenderLayer.class)
public class RenderLayerMixin implements IUniverseRenderLayers {
    @Shadow
    public static RenderLayer.MultiPhase of(
            String name,
            int size,
            boolean hasCrumbling,
            boolean translucent,
            RenderPipeline pipeline,
            RenderLayer.MultiPhaseParameters params
    ) {
        return null;
    }
    @Shadow
    public static RenderLayer.MultiPhase of(String name, int size, RenderPipeline pipeline, RenderLayer.MultiPhaseParameters params) {
        return null;
    }

    // CUSTOM SPACE LAYERS

    @Unique
    private static final Identifier COSMO_SKY_TEXTURE = Identifier.of(FnafUniverseRebuilt.MOD_ID,"textures/block/props/gift_boxes/cosmo_sky.png");
    @Unique
    private static final Identifier COSMO_STARS_TEXTURE = Identifier.of(FnafUniverseRebuilt.MOD_ID,"textures/block/props/gift_boxes/cosmo_stars.png");

    @Unique
    private static final RenderLayer COSMO_SPACE = of(
            "end_portal",
            1536,
            false,
            false,
            IUniverseRenderPipelines.getCosmoSpace(),
            RenderLayer.MultiPhaseParameters.builder()
                    .texture(
                            RenderPhase.Textures.create()
                                    .add(COSMO_SKY_TEXTURE, false)
                                    .add(COSMO_STARS_TEXTURE, false)
                                    .build()
                    )
                    .build(false)
    );

    @Override
    public RenderLayer getCosmoGift() {
        return COSMO_SPACE;
    }
}
