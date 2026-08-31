package net.zephyr.fnafur.util.jsonReaders.animatronics;

import com.geckolib.cache.animation.BakedAnimations;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Tuple;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.entity.animatronic.data.CpuData;
import com.geckolib.cache.GeckoLibResources;

import java.util.*;

public class AnimatronicDataHandler {
    public static String DEFAULT_ANIMATIONS = "";
    public static String DEFAULT_SOUNDS = "";
    public static Chara DEFAULT_CHARA;

    public static final List<String> CATEGORIES = new ArrayList<>();
    public static final Map<String, List<String>> CHARAS_PER_CATEGORY = new HashMap<>();
    public static final Map<String, Chara> CHARACTERS = new HashMap<>();

    public static final Map<String, Map<String, String>> ANIMATIONS_PER_CATEGORY = new HashMap<>();
    public static final Map<String, String> ALL_ANIMATIONS = new HashMap<>();
    public static final Map<String, List<String>> ANIMATION_NAMES_PER_CATEGORY = new HashMap<>();
    public static final List<String> ALL_ANIMATION_NAMES = new ArrayList<>();

    public static final Map<String, Map<String, List<String>>> SOUNDS_PER_CATEGORY = new HashMap<>();
    public static final Map<String, List<String>> ALL_SOUNDS = new HashMap<>();
    public static final Map<String, List<String>> SOUND_NAMES_PER_CATEGORY = new HashMap<>();
    public static final List<String> ALL_SOUND_NAMES = new ArrayList<>();

    public static final List<String> MISSING_CHARACTERS = new ArrayList<>();
    public static final List<String> EMPTY_CATEGORIES = new ArrayList<>();

    public static String getPath(String category, String character){
        return "entity/animatronics/" + category + "/" + character + "/";
    }
    public static Identifier getTexture(Chara chara, String path){
        String category = chara.CATEGORY;
        return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/" + getPath(category, chara.NAME) + path + ".png");
    }
    public static Identifier getAltTexture(String character, String alt){
        Chara chara = CHARACTERS.get(character);
        return getTexture(chara, chara.ALTS.get(alt).texture);
    }
    public static Identifier getEyeTexture(String character, String alt, String eye_alt){
        Chara chara = CHARACTERS.get(character);
        String id = chara.EYE_ALTS.get(eye_alt).texture;
        if(!chara.ALTS.get(alt).eye_path_subfolder.isEmpty()){
            id = chara.ALTS.get(alt).eye_path_subfolder + "/" + id;
        }
        return getTexture(chara, id);
    }
    public static Identifier getEyeMapTexture(String character, String alt, String eye_alt){
        Chara chara = CHARACTERS.get(character);
        String id = chara.EYE_ALTS.get(eye_alt).map;
        if(!chara.ALTS.get(alt).eye_path_subfolder.isEmpty()){
            id = chara.ALTS.get(alt).eye_path_subfolder + "/" + id;
        }
        return getTexture(chara, id);
    }
    public static Identifier getModel(String character, String alt){
        Chara chara = CHARACTERS.get(character);
        if(chara != null) {
            String category = chara.CATEGORY;
            String model = chara.MODEL;
            if (!chara.ALTS.get(alt).model_override.isEmpty()) {
                model = chara.ALTS.get(alt).model_override;
            }
            return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "" + getPath(category, character) + model + "");
        }
        return getDefaultModel();
    }
    public static Identifier getEndoMask(String character, String alt){
        Chara chara = CHARACTERS.get(character);
        if(chara != null) {
            String mask = chara.ENDO_MASK;
            if (!chara.ALTS.get(alt).endo_mask_override.isEmpty()) {
                mask = chara.ALTS.get(alt).endo_mask_override;
            }
            return getTexture(chara, mask);
        }
        return getDefaultEndoMask();
    }

    public static Identifier getDefaultTexture(String path){
        return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "textures/" + getPath(DEFAULT_CHARA.CATEGORY, DEFAULT_CHARA.NAME) + path + ".png");
    }
    public static Identifier getDefaultAltTexture(){
        return getDefaultTexture(DEFAULT_CHARA.ALTS.get(DEFAULT_CHARA.DEFAULT_ALT).texture);
    }
    public static Identifier getDefaultEyeTexture(){
        Chara chara = DEFAULT_CHARA;
        Alt alt = DEFAULT_CHARA.ALTS.get(DEFAULT_CHARA.DEFAULT_ALT);
        String id = chara.EYE_ALTS.get(alt.default_eyes).texture;
        if(!DEFAULT_CHARA.ALTS.get(DEFAULT_CHARA.DEFAULT_ALT).eye_path_subfolder.isEmpty()){
            id = alt.eye_path_subfolder + "/" + id;
        }
        return getDefaultTexture(id);
    }
    public static Identifier getDefaultEyeMapTexture(){
        Alt alt = DEFAULT_CHARA.ALTS.get(DEFAULT_CHARA.DEFAULT_ALT);
        String id = DEFAULT_CHARA.EYE_ALTS.get(alt.default_eyes).map;
        if(!DEFAULT_CHARA.ALTS.get(DEFAULT_CHARA.DEFAULT_ALT).eye_path_subfolder.isEmpty()){
            id = alt.eye_path_subfolder + "/" + id;
        }
        return getDefaultTexture(id);
    }
    public static Identifier getDefaultModel() {
        String category = DEFAULT_CHARA.CATEGORY;
        String model = DEFAULT_CHARA.MODEL;
        if (!DEFAULT_CHARA.ALTS.get(DEFAULT_CHARA.DEFAULT_ALT).model_override.isEmpty()) {
            model = DEFAULT_CHARA.ALTS.get(DEFAULT_CHARA.DEFAULT_ALT).model_override;
        }
        return Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "" + getPath(category, DEFAULT_CHARA.NAME) + model + "");
    }
    public static Identifier getDefaultEndoMask() {
        String mask = DEFAULT_CHARA.ENDO_MASK;
        if (!DEFAULT_CHARA.ALTS.get(DEFAULT_CHARA.DEFAULT_ALT).endo_mask_override.isEmpty()) {
            mask = DEFAULT_CHARA.ALTS.get(DEFAULT_CHARA.DEFAULT_ALT).endo_mask_override;
        }
        return getDefaultTexture(mask);
    }

    public static float getPreviewScale(String character, String alt){
        Chara chara = CHARACTERS.get(character);
        if(chara != null) {
            if(chara.ALTS.containsKey(alt)){
                return chara.ALTS.get(alt).preview_scale;
            }
        }
        return 1;
    }

    public static String getAnimationFilePath(String name){
        return getAnimationFilePath("loweridle", name);
    }
    public static String getAnimationFilePath(String currentAnim, String name){
        return prefixAnim(currentAnim, name).getA();
    }
    public static String getAnimationFullName(String currentAnim, String name){
        return prefixAnim(currentAnim, name).getB();
    }
    public static Tuple<String, String> prefixAnim(String currentAnim, String name){
        String path = ALL_ANIMATIONS.get("default");

        if(ALL_ANIMATION_NAMES.contains(name)){
            path = ALL_ANIMATIONS.get(name);
        }

        Identifier location = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, path);
        Map<Identifier, BakedAnimations> animations = GeckoLibResources.getBakedAnimations().cache();
        BakedAnimations bakedAnimations = animations.get(location);
        String anim = "animation." + name + "." + currentAnim;
        anim = currentAnim;
//        System.out.println("animation." + currentAnim);
        if(bakedAnimations == null || !bakedAnimations.animations().containsKey(anim)) {
            path = ALL_ANIMATIONS.get("default");
            anim = "animation.default." + currentAnim;
            anim = currentAnim;
        }
        return new Tuple<>(path, anim);
    }

    public static class Chara {

        public final String NAME;
        public final String CATEGORY;
        public final String DEFAULT_ALT;
        public final String MODEL;
        public final String ENDO_MASK;
        public final Map<String, Alt> ALTS;
        public final Map<String, EyesAlt> EYE_ALTS;
        public final Map<String, String> GLOW_MASKS;

        public List<String> alt_names = new ArrayList<>();
        public List<String> eye_names = new ArrayList<>();
        public List<String> glow_mask_names = new ArrayList<>();

        public Chara(String name, String category, String default_alt, String model, String endo_mask, Map<String, Alt> alts, Map<String, EyesAlt> eye_alts, Map<String, String> glow_masks) {
            this.NAME = name;
            this.CATEGORY = category;
            this.DEFAULT_ALT = default_alt;
            this.MODEL = model;
            this.ENDO_MASK = endo_mask;
            this.ALTS = alts;
            this.EYE_ALTS = eye_alts;
            this.GLOW_MASKS = glow_masks;

            alt_names.addAll(alts.keySet());
            eye_names.addAll(eye_alts.keySet());
            glow_mask_names.addAll(glow_masks.keySet());
        }
    }
    public record Alt(String texture, String emissive_mask, String default_eyes, String preview_anim, float preview_scale, int colors, String model_override, String endo_mask_override, String eye_path_subfolder){}
    public record EyesAlt(String texture, String map,String default_glow, int colors){}
}
