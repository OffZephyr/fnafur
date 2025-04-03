package net.zephyr.fnafur.blocks.props.base.geo;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.List;
import java.util.Map;

public interface GeoPropBlock {
    public void setModelInfo(Identifier texture, Identifier model, Identifier animations);
    public Identifier getTexture();
    public Identifier getModel();
    public Identifier getAnimations();
}
