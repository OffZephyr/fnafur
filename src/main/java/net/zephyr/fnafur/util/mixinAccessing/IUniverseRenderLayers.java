package net.zephyr.fnafur.util.mixinAccessing;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public interface IUniverseRenderLayers {
    RenderLayer getCosmoGift();
    RenderLayer getLoading(Identifier texture);
}
