package net.zephyr.fnafur.entity.animatronic.data;

import net.zephyr.fnafur.util.jsonReaders.character_models.ModelData;
import net.zephyr.fnafur.util.jsonReaders.character_models.Eye;

import java.util.HashMap;
import java.util.Map;

public class CharacterData {
    public static Map<String, ModelData> EYE_LIST = new HashMap<>();

    public final String ID;
    public final String CATEGORY;
    public Map<String, ModelData> PRESET_LIST = new HashMap<>();
    public CharacterData(String category, String id){
        ID = id;
        CATEGORY = category;
    }

    public ModelData getDefaultModel(){
        return PRESET_LIST.get("default");
    }
}
