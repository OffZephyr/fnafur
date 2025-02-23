package net.zephyr.fnafur.init.entity_init;

import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.entity.animatronic.data.CharacterData;

import java.util.HashMap;
import java.util.Map;

public class CharacterInit {

    public static final Map<String, CharacterData> CHARACTER_MAP = new HashMap<>();
    public static final CharacterData ENDO_01 = registerCharacter("default", "endo_01");
    public static final CharacterData CL_FRED = registerCharacter("classic", "cl_fred");
    public static final CharacterData CL_BON = registerCharacter("classic", "cl_bon");
    public static final CharacterData CL_CHICA = registerCharacter("classic", "cl_chica");
    public static final CharacterData CL_FOXY = registerCharacter("classic", "cl_foxy");

    public static CharacterData registerCharacter(String category, String id){
        CharacterData chara = new CharacterData(category, id);
        CHARACTER_MAP.put(id, chara);
        return chara;
    }

    public static void registerCharacters(){
        FnafUniverseResuited.LOGGER.info("Registering Entities on CLIENT for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }
}
