package net.zephyr.fnafur.mixin;

import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.TriState;
import net.minecraft.util.Util;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.util.mixinAccessing.IUniverseRenderLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Function;

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
    @Shadow
    public static RenderLayer.MultiPhase of(
            String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, RenderLayer.MultiPhaseParameters phaseData
    ) {
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
    private static final Function<Identifier, RenderLayer> LOADING = Util.memoize(
            (Function<Identifier, RenderLayer>)(texture -> of(
                    "mojang_logo",
                    VertexFormats.POSITION_TEXTURE_COLOR,
                    VertexFormat.DrawMode.QUADS,
                    786432,
                    RenderLayer.MultiPhaseParameters.builder()
                            .texture(new RenderPhase.Texture(texture, TriState.DEFAULT, false))
                            .program(RenderPhase.POSITION_TEXTURE_COLOR_PROGRAM)
                            .transparency(RenderPhase.MOJANG_LOGO_TRANSPARENCY)
                            .depthTest(RenderPhase.ALWAYS_DEPTH_TEST)
                            .writeMaskState(RenderPhase.COLOR_MASK)
                            .build(false)
            ))
    );

    @Override
    public RenderLayer getCosmoGift() {
        return COSMO_SPACE;
    }

    @Override
    public RenderLayer getLoading(Identifier texture) {
        return LOADING.apply(texture);
    }
}
