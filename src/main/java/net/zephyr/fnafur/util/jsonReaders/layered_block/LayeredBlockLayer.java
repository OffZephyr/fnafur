package net.zephyr.fnafur.util.jsonReaders.layered_block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public record LayeredBlockLayer(String name, boolean can_recolor, Identifier overlayTexture, Identifier... textures) {
    public String getName() {
        return this.name;
    }

    public Boolean cantRecolorLayer(){
        return !this.can_recolor;
    }

    public Identifier getTexture() {
        return getRgbTexture(0);
    }
    public Identifier getOverlay() {
        return this.overlayTexture;
    }

    public Identifier getRgbTexture(Integer index) {
        return textures[index];
    }
    public int getRgbCount(){
        return cantRecolorLayer() ? 0 : Math.min(3, textures.length);
    }
}
