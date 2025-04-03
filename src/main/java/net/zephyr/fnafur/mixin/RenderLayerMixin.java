package net.zephyr.fnafur.mixin;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.util.mixinAccessing.IUniverseRenderLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(RenderLayer.class)
public class RenderLayerMixin implements IUniverseRenderLayers {
    @Shadow
    public static RenderLayer.MultiPhase of(
            String name,
            VertexFormat vertexFormat,
            VertexFormat.DrawMode drawMode,
            int expectedBufferSize,
            boolean hasCrumbling,
            boolean translucent,
            RenderLayer.MultiPhaseParameters phases
    ){
        return null;
    }

    // CUSTOM SPACE LAYERS

    @Unique
    private static final Identifier COSMO_SKY_TEXTURE = Identifier.of(FnafUniverseResuited.MOD_ID,"textures/block/props/gift_boxes/cosmo_sky.png");
    @Unique
    private static final Identifier COSMO_STARS_TEXTURE = Identifier.of(FnafUniverseResuited.MOD_ID,"textures/block/props/gift_boxes/cosmo_stars.png");

    @Unique
    private static final RenderLayer COSMO_SPACE = of(
        "end_portal",
        VertexFormats.POSITION,
        VertexFormat.DrawMode.QUADS,
        1536,
        false,
        false,
        RenderLayer.MultiPhaseParameters.builder()
                .program(RenderPhase.END_PORTAL_PROGRAM)
                .texture(
                        RenderPhase.Textures.create()
                                .add(COSMO_SKY_TEXTURE, false, false)
                                .add(COSMO_STARS_TEXTURE, false, false)
                                .build()
                )
                .build(false)
    );

    @Override
    public RenderLayer getCosmoGift() {
        return COSMO_SPACE;
    }
}
