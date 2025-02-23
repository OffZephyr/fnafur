package net.zephyr.fnafur.util.jsonReaders.character_models;

import net.minecraft.util.Identifier;

public record Eye(String id, Identifier model, Identifier upper_eyelid, Identifier lower_eyelid, Identifier[] sclera, Identifier[] iris, Identifier[] pupil, Identifier[] overlay) {
    public enum SIDE{
        LEFT,
        RIGHT
    }
}
