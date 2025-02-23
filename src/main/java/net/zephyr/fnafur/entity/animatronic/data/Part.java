package net.zephyr.fnafur.entity.animatronic.data;

import net.minecraft.util.Identifier;

public record Part(PartType type, Identifier texture, Identifier emissive_texture, Identifier[] colorable_textures, Identifier model) {
}
