package net.zephyr.fnafur.util.jsonReaders.layered_block;

import net.minecraft.util.Identifier;

public record LayeredBlockEntry(boolean can_recolor, String overlay, String[] textures) {
    public boolean canRecolor(){
        return this.can_recolor;
    }
    public String overlayTexture(){
        return this.overlay;
    }
    public String[] textures(){
        return this.textures;
    }
}
