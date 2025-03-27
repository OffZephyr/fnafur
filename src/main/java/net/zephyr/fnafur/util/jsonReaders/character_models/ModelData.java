package net.zephyr.fnafur.util.jsonReaders.character_models;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.entity.animatronic.data.Part;
import net.zephyr.fnafur.entity.animatronic.data.PartType;

import java.util.HashMap;
import java.util.Map;

public record ModelData(String name, ModelDataEntry.VariantEntry[] variants) {

}
