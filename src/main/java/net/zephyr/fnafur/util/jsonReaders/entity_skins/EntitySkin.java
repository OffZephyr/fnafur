package net.zephyr.fnafur.util.jsonReaders.entity_skins;

import net.minecraft.util.Identifier;

public record EntitySkin(String name, String icon, String icon_outline, String texture, String glow_texture, String geo, String animations, String kill_screen_id) {
    public String getName() {
        return this.name;
    }
    public Identifier getIcon() {
        return Identifier.of(this.icon);
    }
    public Identifier getIconOutline() {
        return Identifier.of(this.icon_outline);
    }
    public Identifier getTexture() {
        return Identifier.of(this.texture);
    }
    public Identifier getGlowTexture() {
        return Identifier.of(this.glow_texture);
    }
    public Identifier getGeo() {
        return Identifier.of(this.geo);
    }
    public Identifier getAnimations() {
        return Identifier.of(this.animations);
    }
    public String getKillScreenID() {
        return this.kill_screen_id;
    }
}
