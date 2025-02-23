package net.zephyr.fnafur.util.jsonReaders.character_models;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.entity.animatronic.data.PartType;

public record PartEntry(String type, String model, String texture, String emissive_texture, String... colorable_textures) {
}
