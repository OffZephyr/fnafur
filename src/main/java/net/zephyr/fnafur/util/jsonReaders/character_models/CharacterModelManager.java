package net.zephyr.fnafur.util.jsonReaders.character_models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.entity.animatronic.data.CharacterData;
import net.zephyr.fnafur.init.entity_init.CharacterInit;
import net.zephyr.fnafur.entity.animatronic.data.Part;
import net.zephyr.fnafur.entity.animatronic.data.PartType;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CharacterModelManager extends SinglePreparationResourceReloader<List<CharacterModel>> {
    static final Gson GSON = new Gson();
    private static final TypeToken<Map<String, CharacterModelEntry>> CHARACTER_LIST_TYPE = new TypeToken<>() {};
    private static final TypeToken<Map<String, EyeEntry>> EYE_LIST_TYPE = new TypeToken<>() {};
    @Override
    protected List<CharacterModel> prepare(ResourceManager resourceManager, Profiler profiler) {
        List<CharacterModel> models = new ArrayList<>();
        for (String string : resourceManager.getAllNamespaces()) {
            models.addAll(CharacterModels(resourceManager, profiler, string));
            //CharacterEyes(resourceManager, profiler, string);
        }
        return models;
    }

    void CharacterEyes(ResourceManager resourceManager, Profiler profiler, String namespace){
        String path = "entitydata/models/eyes.modeldata.json";
        List<Resource> list = resourceManager.getAllResources(Identifier.of(namespace, path));
        for (Resource resource : list) {
            try (BufferedReader reader = resource.getReader();) {
                Map<String, EyeEntry> layerEntries = JsonHelper.deserialize(GSON, reader, EYE_LIST_TYPE);

                for (Map.Entry<String, EyeEntry> entry : layerEntries.entrySet()) {
                    String name = entry.getKey();

                    Identifier model = entry.getValue().model();
                    Identifier upper_eyelid = entry.getValue().upper_eyelid();
                    Identifier lower_eyelid = entry.getValue().lower_eyelid();
                    Identifier[] sclera = entry.getValue().sclera();
                    Identifier[] iris = entry.getValue().iris();
                    Identifier[] pupil = entry.getValue().pupil();
                    Identifier[] overlay = entry.getValue().overlay();

                    Eye eye = new Eye(name, model, upper_eyelid, lower_eyelid, sclera, iris, pupil, overlay);
                    CharacterData.EYE_LIST.put(name, eye);
                    System.out.println("Eye: " + name);
                }
            } catch (RuntimeException | IOException runtimeException) {
                FnafUniverseResuited.LOGGER.warn("Invalid {} in resourcepack: '{}'", path, resource.getPackId(), runtimeException);
            }
        }
    }
    List<CharacterModel> CharacterModels(ResourceManager resourceManager, Profiler profiler, String namespace){
        List<CharacterModel> models = new ArrayList<>();

        try {
        for(CharacterData character : CharacterInit.CHARACTER_MAP.values()) {
            String characterName = character.ID;
            String path = "entitydata/models/" + character.CATEGORY + "/" + characterName + ".modeldata.json";
            List<Resource> list = resourceManager.getAllResources(Identifier.of(namespace, path));
            for (Resource resource : list) {
                try (BufferedReader reader = resource.getReader();) {
                    Map<String, CharacterModelEntry> layerEntries = JsonHelper.deserialize(GSON, reader, CHARACTER_LIST_TYPE);

                    for (Map.Entry<String, CharacterModelEntry> entry : layerEntries.entrySet()) {
                        String name = entry.getKey();
                        PartEntry[] parts = entry.getValue().modelParts();
                        Part[] partArray = new Part[parts.length];

                        for(int i = 0; i < parts.length; i++){
                            PartEntry part = parts[i];

                            Identifier[] colorable_textures = new Identifier[part.colorable_textures().length];
                            for(int j = 0; j < part.colorable_textures().length; j++){
                                colorable_textures[j] = Identifier.of(resource.getPackId(), part.colorable_textures()[j]);
                            }

                            partArray[i] = new Part(
                                    PartType.valueOf(part.type()),
                                    Identifier.of(resource.getPackId(), part.texture()),
                                    Identifier.of(resource.getPackId(), part.emissive_texture()),
                                    colorable_textures,
                                    Identifier.of(resource.getPackId(), part.model())
                            );
                        }

                        CharacterModel model = new CharacterModel(name, partArray);
                        models.add(model);
                        character.PRESET_LIST.put(name, model);
                        System.out.println(characterName + ": " + name + " " + partArray.length + " parts");
                    }
                } catch (RuntimeException runtimeException) {
                    FnafUniverseResuited.LOGGER.error("Invalid {} in resourcepack: '{}'", path, resource.getPackId(), runtimeException);
                }
            }
        }

        } catch (IOException iOException) {
            // empty catch block
        }

        return models;
    }

    @Override
    protected void apply(List<CharacterModel> prepared, ResourceManager manager, Profiler profiler) {

    }
}
