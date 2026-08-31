package net.zephyr.fnafur.util.jsonReaders.entity_skins;

import com.google.gson.Gson;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.zephyr.fnafur.FnafUniverseRebuilt;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

@Environment(EnvType.CLIENT)
public class EntityDataManager extends SimplePreparableReloadListener<Map<EntityType<?>, DefaultEntityData>> {
    static final Gson GSON = new Gson();
    private Map<EntityType<?>, List<EntitySkin>> Skins = new HashMap<>();
    private Map<EntityType<?>, DefaultEntityData> EntityData = new HashMap<>();

    public List<EntitySkin> getSkins(EntityType<?> type){
        if(Skins.containsKey(type)) return Skins.get(type);
        return null;
    }

    public EntitySkin getSkin(EntityType<?> type, String textureName){
        List<EntitySkin> skins = Skins.get(type);
        for(EntitySkin layer : skins){
            if(Objects.equals(layer.getName(), textureName))
                return layer;
        }
        return getDefault(type);
    }
    public EntitySkin getDefault(EntityType<?> type){
        List<EntitySkin> skins = Skins.get(type);
        for(EntitySkin layer : skins){
            if(Objects.equals(layer.getName(), "default"))
                return layer;
        }
        return null;
    }
    public DefaultEntityData getEntityData(EntityType<?> type){
        return EntityData.get(type);
    }

    @Override
    protected Map<EntityType<?>, DefaultEntityData> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<EntityType<?>, DefaultEntityData> data = new HashMap<>();
        for (String string : resourceManager.getNamespaces()) {
            try {
                for(EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
                    String path = "entitydata/" + BuiltInRegistries.ENTITY_TYPE.getKey(entityType).getPath() + "_data.json";
                    List<Resource> list = resourceManager.getResourceStack(Identifier.fromNamespaceAndPath(string, path));
                    for (Resource resource : list) {
                        try (BufferedReader reader = resource.openAsReader();) {
                            DefaultEntityData skinEntry = GsonHelper.fromJson(GSON, reader, DefaultEntityData.class);
                            data.put(entityType, skinEntry);
                        } catch (RuntimeException runtimeException) {
                            FnafUniverseRebuilt.LOGGER.warn("Invalid {} in resourcepack: '{}'", "entity_skins.json", resource.sourcePackId(), runtimeException);
                        }
                    }
                }
            } catch (IOException iOException) {
                // empty catch block
            }
        }
        return data;
    }

    @Override
    protected void apply(Map<EntityType<?>, DefaultEntityData> prepared, ResourceManager manager, ProfilerFiller profiler) {
        this.EntityData.clear();
        this.Skins.clear();
        this.EntityData = prepared;
        for(EntityType<?> type : prepared.keySet()){
            this.Skins.put(type, prepared.get(type).skins());
            for(EntitySkin skin : prepared.get(type).skins()) {
                FnafUniverseRebuilt.print(skin.getName());
            }
        }
    }
}
