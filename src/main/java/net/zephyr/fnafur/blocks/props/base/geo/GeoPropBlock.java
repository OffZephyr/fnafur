package net.zephyr.fnafur.blocks.props.base.geo;

import net.minecraft.util.Identifier;

public interface GeoPropBlock {
    public void setModelInfo(Identifier texture, Identifier model, Identifier animations);
    public Identifier getTexture();
    public Identifier getModel();
    public Identifier getAnimations();
}
