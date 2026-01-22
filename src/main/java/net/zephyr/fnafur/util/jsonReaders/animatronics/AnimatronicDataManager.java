package net.zephyr.fnafur.util.jsonReaders.animatronics;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.util.jsonReaders.entity_skins.DefaultEntityData;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

@Environment(EnvType.CLIENT)
public class AnimatronicDataManager extends SinglePreparationResourceReloader<Map<EntityType<?>, DefaultEntityData>> {
    static final Gson GSON = new Gson();

    private static final TypeToken<Map<String, List<String>>> STRING_LIST_TYPE = new TypeToken<>() {};
    private static final TypeToken<Map<String, Map<String, String>>> STRING_MAP_TYPE = new TypeToken<>() {};
    private static final TypeToken<Map<String, CharaGetter>> CHARACTER_TYPE = new TypeToken<>() {};

    @Override
    protected Map<EntityType<?>, DefaultEntityData> prepare(ResourceManager resourceManager, Profiler profiler) {

        clear();
        Map<EntityType<?>, DefaultEntityData> data = new HashMap<>();
        for (String string : resourceManager.getAllNamespaces()) {
            getCategories(resourceManager, profiler, string);
            getAnimations(resourceManager, profiler, string);
            getAmbientSounds(resourceManager, profiler, string);
            for(String category : AnimatronicDataHandler.CATEGORIES){
                for(String name : AnimatronicDataHandler.CHARAS_PER_CATEGORY.get(category)){
                    getCharacters(category, name, resourceManager, profiler, string);
                }
            }
        }
        return data;
    }

    @Override
    protected void apply(Map<EntityType<?>, DefaultEntityData> prepared, ResourceManager manager, Profiler profiler) {
    }


    void getCategories(ResourceManager resourceManager, Profiler profiler, String namespace) {
        String path = FnafUniverseRebuilt.MOD_ID + "/characters.json";
        List<Resource> list = resourceManager.getAllResources(Identifier.of(namespace, path));
        for (Resource resource : list) {
            try (BufferedReader reader = resource.getReader();) {
                Map<String, List<String>> layerEntries = JsonHelper.deserialize(GSON, reader, STRING_LIST_TYPE);
                for (Map.Entry<String, List<String>> entry : layerEntries.entrySet()) {
                    AnimatronicDataHandler.CATEGORIES.add(entry.getKey());
                    AnimatronicDataHandler.CHARAS_PER_CATEGORY.put(entry.getKey(), entry.getValue());
                    if(entry.getValue().isEmpty()) AnimatronicDataHandler.EMPTY_CATEGORIES.add(entry.getKey());
                    //for(int i = 0; i <  entry.getValue().size(); i++){
                        //System.out.println(entry.getKey() + ": " + entry.getValue().get(i));
                    //}
                }
            } catch (RuntimeException | IOException runtimeException) {
                FnafUniverseRebuilt.LOGGER.warn("Invalid {} in resourcepack: '{}'", path, resource.getPackId(), runtimeException);
            }
        }
    }
    void getAnimations(ResourceManager resourceManager, Profiler profiler, String namespace) {
        String path = FnafUniverseRebuilt.MOD_ID + "/animations.json";
        List<Resource> list = resourceManager.getAllResources(Identifier.of(namespace, path));
        for (Resource resource : list) {
            try (BufferedReader reader = resource.getReader();) {
                Map<String, Map<String, String>> layerEntries = JsonHelper.deserialize(GSON, reader, STRING_MAP_TYPE);
                for (Map.Entry<String, Map<String, String>> entry : layerEntries.entrySet()) {
                    if(Objects.equals(entry.getKey(), "default")){
                        AnimatronicDataHandler.DEFAULT_ANIMATIONS = entry.getValue().get("default");
                    }
                    AnimatronicDataHandler.ANIMATIONS_PER_CATEGORY.put(entry.getKey(), entry.getValue());

                    for(String string : entry.getValue().keySet()){
                        AnimatronicDataHandler.ALL_ANIMATION_NAMES.add(string);
                        AnimatronicDataHandler.ALL_ANIMATIONS.put(string, entry.getValue().get(string));
                    }

                    //for(String string : entry.getValue().keySet()){
                    //    System.out.println(entry.getKey() + ": " + string + ": " + entry.getValue().get(string));
                    //}
                }

                AnimatronicDataHandler.ANIMATIONS_PER_CATEGORY.forEach((category, animations) ->{
                    List<String> animations_list = new ArrayList<>();
                    animations.forEach((animation, animation_path) -> {
                        animations_list.add(animation);
                    });
                    AnimatronicDataHandler.ANIMATION_NAMES_PER_CATEGORY.put(category, animations_list);
                });
            } catch (RuntimeException | IOException runtimeException) {
                FnafUniverseRebuilt.LOGGER.warn("Invalid {} in resourcepack: '{}'", path, resource.getPackId(), runtimeException);
            }
        }
    }
    void getAmbientSounds(ResourceManager resourceManager, Profiler profiler, String namespace) {
        String path = FnafUniverseRebuilt.MOD_ID + "/ambient_sounds.json";
        List<Resource> list = resourceManager.getAllResources(Identifier.of(namespace, path));
        for (Resource resource : list) {
            try (BufferedReader reader = resource.getReader()) {
                Map<String, Map<String, String>> layerEntries = JsonHelper.deserialize(GSON, reader, STRING_MAP_TYPE);
                for (Map.Entry<String, Map<String, String>> entry : layerEntries.entrySet()) {
                    if(Objects.equals(entry.getKey(), "default")){
                        AnimatronicDataHandler.DEFAULT_SOUNDS = entry.getValue().get("default");
                    }
                    AnimatronicDataHandler.SOUNDS_PER_CATEGORY.put(entry.getKey(), entry.getValue());

                    for(String string : entry.getValue().keySet()){
                        AnimatronicDataHandler.ALL_SOUND_NAMES.add(string);
                        AnimatronicDataHandler.ALL_SOUNDS.put(string, entry.getValue().get(string));
                    }

                    //for(String string : entry.getValue().keySet()){
                    //    System.out.println(entry.getKey() + ": " + string + ": " + entry.getValue().get(string));
                    //}
                }

                AnimatronicDataHandler.SOUNDS_PER_CATEGORY.forEach((category, sounds) ->{
                    List<String> sounds_list = new ArrayList<>();
                    sounds.forEach((sound, sound_path) -> {
                        sounds_list.add(sound);
                    });
                    AnimatronicDataHandler.SOUND_NAMES_PER_CATEGORY.put(category, sounds_list);
                });
            } catch (RuntimeException | IOException runtimeException) {
                FnafUniverseRebuilt.LOGGER.warn("Invalid {} in resourcepack: '{}'", path, resource.getPackId(), runtimeException);
            }
        }
    }
    void getCharacters(String category, String name, ResourceManager resourceManager, Profiler profiler, String namespace) {
        String path = FnafUniverseRebuilt.MOD_ID + "/" + category + "/" + name + ".data.json";
        List<Resource> list = resourceManager.getAllResources(Identifier.of(namespace, path));
        for (Resource resource : list) {
            try (BufferedReader reader = resource.getReader();) {
                Map<String, CharaGetter> layerEntries = JsonHelper.deserialize(GSON, reader, CHARACTER_TYPE);
                for (Map.Entry<String, CharaGetter> entry : layerEntries.entrySet()) {
                    CharaGetter getter = entry.getValue();
                    if (!AnimatronicDataHandler.CHARACTERS.containsKey(name)) {

                        AnimatronicDataHandler.EyesAlt defaultAlt = getter.eyes.alts().get(getter.alts.get(getter.default_alt).default_eyes());
                        AnimatronicDataHandler.EyesAlt noneEyes = new AnimatronicDataHandler.EyesAlt(defaultAlt.texture(), defaultAlt.map(), "none", 0);
                        getter.eyes.alts.put("none", noneEyes);

                        AnimatronicDataHandler.Chara chara = new AnimatronicDataHandler.Chara(name, category, getter.default_alt, getter.model, getter.endo_mask, getter.alts, getter.eyes.alts, getter.eyes.glow_masks);

                        if(Objects.equals(name, "cl_endo")){
                            AnimatronicDataHandler.DEFAULT_CHARA = chara;
                        }
                        AnimatronicDataHandler.CHARACTERS.put(name, chara);
                    }
                }
            } catch (RuntimeException | IOException runtimeException) {
                FnafUniverseRebuilt.LOGGER.warn("Invalid {} in resourcepack: '{}'", path, resource.getPackId(), runtimeException);
            }
        }

        if(!AnimatronicDataHandler.CHARACTERS.containsKey(name) && !AnimatronicDataHandler.MISSING_CHARACTERS.contains(name)) {
            AnimatronicDataHandler.MISSING_CHARACTERS.add(name);
            FnafUniverseRebuilt.LOGGER.warn("MISSING " + name.toUpperCase() + " DATA FILE (" + path + ")");
        }
    }

    void clear(){
        AnimatronicDataHandler.CATEGORIES.clear();
        AnimatronicDataHandler.CHARAS_PER_CATEGORY.clear();
        AnimatronicDataHandler.CHARACTERS.clear();
        AnimatronicDataHandler.ANIMATIONS_PER_CATEGORY.clear();
        AnimatronicDataHandler.ANIMATION_NAMES_PER_CATEGORY.clear();
        AnimatronicDataHandler.ALL_ANIMATIONS.clear();
        AnimatronicDataHandler.ALL_ANIMATION_NAMES.clear();
        AnimatronicDataHandler.MISSING_CHARACTERS.clear();
    }

    public record CharaGetter(String default_alt, String model, String endo_mask, Map<String, AnimatronicDataHandler.Alt> alts, EyesGetter eyes){}
    public record EyesGetter(Map<String, AnimatronicDataHandler.EyesAlt> alts, Map<String, String> glow_masks){}
}
