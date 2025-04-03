package net.zephyr.fnafur.util.jsonReaders.character_models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundEntry;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.entity.animatronic.data.CharacterData;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class CharacterModelManager extends SinglePreparationResourceReloader<List<ModelData>> {

    public static final Map<String, List<Chara>> CHARACTER_CATEGORIES = new HashMap<>();
    public static final Map<String, String> ACCESSORY_FILES = new HashMap<>();
    public static final Map<String, String> MAPS = new HashMap<>();
    public static final Map<String, Chara> ALL_CHARACTERS = new HashMap<>();
    public static final Map<Chara, Map<String, ModelEntry>> MODEL_DATA = new HashMap<>();
    public static final Map<String, Map<String, MapEntry>> MAP_DATA = new HashMap<>();
    public static final Map<String, Map<String, ModelEntry>> ACCESSORY_MODEL_DATA = new HashMap<>();
    public static final Map<Chara, Map<String, DataEntry.Alt>> CHARA_ALT_MAP = new HashMap<>();
    public static final Map<Chara, String> CHARA_DEFAULT_ALT_MAP = new HashMap<>();



    public static final String EYES = "eyes";
    public static final String TORSO_ACCESSORIES = "torso_accessories";

    static final Gson GSON = new Gson();
    public static Identifier DEFAULT_MODEL = Identifier.of(FnafUniverseResuited.MOD_ID, "geo/entity/default/endo_01/endo_01.geo.json");
    private static final TypeToken<Map<String, CharacterEntry>> CHARACTER_LIST_TYPE = new TypeToken<>() {};
    private static final TypeToken<Map<String, String>> STRING_LIST = new TypeToken<>() {};
    private static final TypeToken<Map<String, ModelDataEntry>> MODEL_LIST_TYPE = new TypeToken<>() {};
    private static final TypeToken<Map<String, ModelEntry>> MODEL_DATA_TYPE = new TypeToken<>() {};
    private static final TypeToken<Map<String, MapEntry>> MAP_DATA_TYPE = new TypeToken<>() {};
    private static final TypeToken<Map<String, DataEntry>> CHARA_DATA_TYPE = new TypeToken<>() {};
    @Override
    protected List<ModelData> prepare(ResourceManager resourceManager, Profiler profiler) {
        List<ModelData> models = new ArrayList<>();
        for (String string : resourceManager.getAllNamespaces()) {
            //models.addAll(CharacterModels(resourceManager, profiler, string));
            getMaps(resourceManager, profiler, string);
            getCharacters(resourceManager, profiler, string);
            getAccessories(resourceManager, profiler, string);
            for(String map : MAPS.keySet()){
                getMapData(resourceManager, profiler, string, MAPS.get(map), map);
            }
            for(String category : CHARACTER_CATEGORIES.keySet()){
                for(Chara character : CHARACTER_CATEGORIES.get(category)){
                    getModelData(resourceManager, profiler, string, category, character);
                    getCharacterData(resourceManager, profiler, string, category, character);
                }
            }
            for(String accessory : ACCESSORY_FILES.keySet()){
                getAccessoryData(resourceManager, profiler, string, accessory, ACCESSORY_FILES.get(accessory));
            }

            //models.addAll(CharacterEyes(resourceManager, profiler, string));
        }
        return models;
    }

    void getMaps(ResourceManager resourceManager, Profiler profiler, String namespace) {
        String path = "modeldata/map_list.json";
        List<Resource> list = resourceManager.getAllResources(Identifier.of(namespace, path));
        for (Resource resource : list) {
            try (BufferedReader reader = resource.getReader();) {
                Map<String, String> layerEntries = JsonHelper.deserialize(GSON, reader, STRING_LIST);
                for (Map.Entry<String, String> entry : layerEntries.entrySet()) {
                    MAPS.put(entry.getKey(), entry.getValue());

                    FnafUniverseResuited.print(entry.getKey() + ": " + entry.getValue());
                }
            } catch (RuntimeException | IOException runtimeException) {
                FnafUniverseResuited.LOGGER.warn("Invalid {} in resourcepack: '{}'", path, resource.getPackId(), runtimeException);
            }
        }
    }

    void getCharacters(ResourceManager resourceManager, Profiler profiler, String namespace) {
        String path = "modeldata/chara_list.json";
        List<Resource> list = resourceManager.getAllResources(Identifier.of(namespace, path));
        for (Resource resource : list) {
            try (BufferedReader reader = resource.getReader();) {
                Map<String, CharacterEntry> layerEntries = JsonHelper.deserialize(GSON, reader, CHARACTER_LIST_TYPE);
                for (Map.Entry<String, CharacterEntry> entry : layerEntries.entrySet()) {
                    CHARACTER_CATEGORIES.put(entry.getKey(), entry.getValue().characters());

                    for(Chara character : entry.getValue().characters()){
                        ALL_CHARACTERS.put(character.name(), character);
                    }

                    for(int i = 0; i <  entry.getValue().characters().size(); i++){
                        FnafUniverseResuited.print(entry.getKey() + ": " + entry.getValue().characters().get(i).name);
                    }
                }
            } catch (RuntimeException | IOException runtimeException) {
                FnafUniverseResuited.LOGGER.warn("Invalid {} in resourcepack: '{}'", path, resource.getPackId(), runtimeException);
            }
        }
    }
    void getAccessories(ResourceManager resourceManager, Profiler profiler, String namespace) {
        String path = "modeldata/accessory_list.json";
        List<Resource> list = resourceManager.getAllResources(Identifier.of(namespace, path));
        for (Resource resource : list) {
            try (BufferedReader reader = resource.getReader();) {
                Map<String, String> layerEntries = JsonHelper.deserialize(GSON, reader, STRING_LIST);
                for (Map.Entry<String, String> entry : layerEntries.entrySet()) {
                    ACCESSORY_FILES.put(entry.getKey(), entry.getValue());

                    FnafUniverseResuited.print(entry.getKey() + ": " + entry.getValue());
                }
            } catch (RuntimeException | IOException runtimeException) {
                FnafUniverseResuited.LOGGER.warn("Invalid {} in resourcepack: '{}'", path, resource.getPackId(), runtimeException);
            }
        }
    }
    void getMapData(ResourceManager resourceManager, Profiler profiler, String namespace, String name, String mapName) {
        String path = "modeldata/" + name + ".map.json";
        List<Resource> list = resourceManager.getAllResources(Identifier.of(namespace, path));
        for (Resource resource : list) {
            try (BufferedReader reader = resource.getReader();) {
                Map<String, MapEntry> layerEntries = JsonHelper.deserialize(GSON, reader, MAP_DATA_TYPE);
                for (Map.Entry<String, MapEntry> entry : layerEntries.entrySet()) {

                    Map<String, MapEntry> skinMap = MAP_DATA.containsKey(mapName) ? MAP_DATA.get(mapName) : new HashMap<>();
                    skinMap.put(entry.getKey(), entry.getValue());
                    MAP_DATA.put(mapName, skinMap);

                    FnafUniverseResuited.print(mapName);
                    FnafUniverseResuited.print(entry.getKey() + ": " + entry.getValue().sub().size());
                }
            } catch (RuntimeException | IOException runtimeException) {
                FnafUniverseResuited.LOGGER.warn("Invalid {} in resourcepack: '{}'", path, resource.getPackId(), runtimeException);
            }
        }
    }
    void getCharacterData(ResourceManager resourceManager, Profiler profiler, String namespace, String category, Chara character) {
        String path = "modeldata/" + category + "/" + character.name + ".data.json";
        List<Resource> list = resourceManager.getAllResources(Identifier.of(namespace, path));
        for (Resource resource : list) {
            try (BufferedReader reader = resource.getReader();) {
                Map<String, DataEntry> layerEntries = JsonHelper.deserialize(GSON, reader, CHARA_DATA_TYPE);
                for (Map.Entry<String, DataEntry> entry : layerEntries.entrySet()) {
                    System.out.println(character.name() + " has " + entry.getValue().alts().size() + " alts");

                    CHARA_ALT_MAP.put(character, entry.getValue().alts());
                    CHARA_DEFAULT_ALT_MAP.put(character, entry.getValue().default_alt());
                }
            } catch (RuntimeException | IOException runtimeException) {
                FnafUniverseResuited.LOGGER.warn("Invalid {} in resourcepack: '{}'", path, resource.getPackId(), runtimeException);
            }
        }
    }

    void getModelData(ResourceManager resourceManager, Profiler profiler, String namespace, String category, Chara character) {
        String path = "modeldata/" + category + "/" + character.name + ".modeldata.json";
        List<Resource> list = resourceManager.getAllResources(Identifier.of(namespace, path));
        for (Resource resource : list) {
            try (BufferedReader reader = resource.getReader();) {
                Map<String, ModelEntry> layerEntries = JsonHelper.deserialize(GSON, reader, MODEL_DATA_TYPE);
                for (Map.Entry<String, ModelEntry> entry : layerEntries.entrySet()) {

                    Map<String, ModelEntry> skinMap = MODEL_DATA.containsKey(character) ? MODEL_DATA.get(character) : new HashMap<>();
                    skinMap.put(entry.getKey(), entry.getValue());
                    MODEL_DATA.put(character, skinMap);

                    FnafUniverseResuited.print(character.name);
                    FnafUniverseResuited.print(entry.getValue().model);
                    for(int i = 0; i <  entry.getValue().layers().size(); i++){
                        for(int j = 0; j <  entry.getValue().layers().get(i).variants.size(); j++){
                            FnafUniverseResuited.print(entry.getValue().layers().get(i).name + ": " + entry.getValue().layers().get(i).variants.get(j).name);
                        }
                    }
                }
            } catch (RuntimeException | IOException runtimeException) {
                FnafUniverseResuited.LOGGER.warn("Invalid {} in resourcepack: '{}'", path, resource.getPackId(), runtimeException);
            }
        }
    }
    void getAccessoryData(ResourceManager resourceManager, Profiler profiler, String namespace, String boneName, String name) {
        String path = "modeldata/other/" + name + ".modeldata.json";
        List<Resource> list = resourceManager.getAllResources(Identifier.of(namespace, path));
        for (Resource resource : list) {
            try (BufferedReader reader = resource.getReader();) {
                Map<String, ModelEntry> layerEntries = JsonHelper.deserialize(GSON, reader, MODEL_DATA_TYPE);
                for (Map.Entry<String, ModelEntry> entry : layerEntries.entrySet()) {

                    Map<String, ModelEntry> skinMap = ACCESSORY_MODEL_DATA.containsKey(boneName) ? ACCESSORY_MODEL_DATA.get(boneName) : new HashMap<>();
                    skinMap.put(entry.getKey(), entry.getValue());
                    ACCESSORY_MODEL_DATA.put(boneName, skinMap);

                    FnafUniverseResuited.print(boneName);
                    FnafUniverseResuited.print(entry.getValue().model);

                    for(int i = 0; i <  entry.getValue().layers().size(); i++){
                        for(int j = 0; j <  entry.getValue().layers().get(i).variants.size(); j++){
                            FnafUniverseResuited.print(entry.getValue().layers().get(i).name + ": " + entry.getValue().layers().get(i).variants.get(j).name);
                        }
                    }
                }
            } catch (RuntimeException | IOException runtimeException) {
                FnafUniverseResuited.LOGGER.warn("Invalid {} in resourcepack: '{}'", path, resource.getPackId(), runtimeException);
            }
        }
    }

    @Override
    protected void apply(List<ModelData> prepared, ResourceManager manager, Profiler profiler) {

    }
    public record Chara(String name, String endo){}
    public record CharacterEntry(List<Chara> characters){
    }
    public record MapEntry(boolean isLimb, boolean isAccessory, List<Map<String, MapEntry>> sub){

    }
    public record ModelEntry(String model, List<Layer> layers){
        public record Layer(String name, List<Texture> variants){}
        public record Texture(String name, String color_texture, String color_emissive, Boolean can_be_recolored, String overlay_texture, String overlay_emissive){}
    }
    public record DataEntry(String default_alt, Map<String, Alt> alts){
        public record Alt(String model, String texture, List<String> recolorable_textures, String emissive, List<String> recolorable_emissive, String animations, String default_eyes, Map<String, Eyes> eyes){}
        public record Eyes(String texture, List<String> recolorable_textures, String emissive, List<String> recolorable_emissive){}
    }

    public static List<String> getVariantNames(Chara character, String variant){
        List<String> names = new ArrayList<>();
        Map<String, ModelEntry> map = MODEL_DATA.get(character);
        if (map != null) {
            if (map.get(variant) instanceof ModelEntry entry) {
                for (ModelEntry.Layer layer : entry.layers()) {
                    names.add(layer.name());
                }
            }
        }
        return names;
    }
    public static ModelEntry.Layer getVariantLayer(Chara character, String variant, String layer){
        for(ModelEntry.Layer l : MODEL_DATA.get(character).get(variant).layers()){
            if(Objects.equals(l.name(), layer)) return l;
        }
        return null;
    }
    public static ModelEntry.Texture getVariantTexture(Chara character, String modelVariant, String layer, String textureVariant){
        List<String> names = getVariantNames(character, modelVariant);

        if(names.contains(layer)){

            for(ModelEntry.Texture t : getVariantLayer(character, modelVariant, layer).variants()){
                if(Objects.equals(t.name(), textureVariant)) return t;
            }

        }
        return null;
    }
    public static Identifier getVariantModel(Chara character, String variant){
        Map<String, ModelEntry> map = MODEL_DATA.get(character);
        if(map != null && map.get(variant) instanceof ModelEntry entry){
            return Identifier.of(FnafUniverseResuited.MOD_ID, entry.model());
        }
        return null;
    }

    public static Identifier getAccessoryModel(String type, String variant){
        Map<String, ModelEntry> map = ACCESSORY_MODEL_DATA.get(type);
        if(map != null) {
            if (map.get(variant) instanceof ModelEntry entry) {
                return Identifier.of(FnafUniverseResuited.MOD_ID, entry.model());
            }
        }
        return null;
    }
    public static List<String> getAccessoryVariantNames(String type, String variant) {
        List<String> names = new ArrayList<>();
        Map<String, ModelEntry> map = ACCESSORY_MODEL_DATA.get(type);
        if (map != null) {
            if (map.get(variant) instanceof ModelEntry entry) {
                for (ModelEntry.Layer layer : entry.layers()) {
                    names.add(layer.name());
                }
            }
        }
        return names;
    }
    public static ModelEntry.Layer getAccessoryVariantLayer(String type, String variant, String layer){
        Map<String, ModelEntry> map = ACCESSORY_MODEL_DATA.get(type);
        if (map != null) {
            for (ModelEntry.Layer l : map.get(variant).layers()) {
                if (Objects.equals(l.name(), layer)) return l;
            }
        }
        return null;
    }
    public static ModelEntry.Texture getAccessoryTexture(String type, String modelVariant, String layer, String textureVariant){
        List<String> names = getAccessoryVariantNames(type, modelVariant);

        if(names.contains(layer)){

            for(ModelEntry.Texture t : getAccessoryVariantLayer(type, modelVariant, layer).variants()){
                //System.out.println(type + " " + modelVariant + " " + layer + " " + textureVariant);
                if(Objects.equals(t.name(), textureVariant)) return t;
            }

        }
        return null;
    }
}
