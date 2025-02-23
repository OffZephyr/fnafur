package net.zephyr.fnafur.util.jsonReaders.character_models;

import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.entity.animatronic.data.Part;
import net.zephyr.fnafur.entity.animatronic.data.PartType;

import java.util.HashMap;
import java.util.Map;

public record CharacterModel(String name, Part[] modelParts) {
    public static final CharacterModel defaultModel = new CharacterModel("default",
            new Part[]{
                    new Part(PartType.PELVIS,
                            Identifier.of(
                                    FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_bon/cl_fred.png"),
                                    Identifier.of(""),
                                    new Identifier[0],
                                    Identifier.of(FnafUniverseResuited.MOD_ID, "geo/entity/classic/cl_bon/cl_fred.geo.json")
                    ),
                    new Part(PartType.TORSO,
                            Identifier.of(
                                    FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_bon/cl_fred.png"),
                                    Identifier.of(""),
                                    new Identifier[0],
                                    Identifier.of(FnafUniverseResuited.MOD_ID, "geo/entity/classic/cl_bon/cl_fred.geo.json")
                    ),
                    new Part(PartType.TORSO_ACCESSORY,
                            Identifier.of(
                                    FnafUniverseResuited.MOD_ID, "textures/entity/classic/cl_bon/cl_fred.png"),
                            Identifier.of(""),
                            new Identifier[0],
                            Identifier.of(FnafUniverseResuited.MOD_ID, "geo/entity/classic/cl_bon/cl_fred.geo.json")
                    ),
    });

    public Map<PartType, Part> getModelMap(){
        Map<PartType, Part> map = new HashMap<>();
        if(modelParts() == null) return map;
        for(Part part : modelParts()){
            map.put(part.type(), part);
        }

        for(Part part : defaultModel.modelParts()){
            if(!map.containsKey(part.type())){
                map.put(part.type(), part);
            }
        }
        return map;
    }
}
