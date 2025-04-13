package net.zephyr.fnafur.util.jsonReaders.layered_block;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import net.zephyr.fnafur.FnafUniverseRebuilt;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class LayeredBlockManager extends SinglePreparationResourceReloader<List<LayeredBlockLayer>> {
    static final Gson GSON = new Gson();
    private static final TypeToken<Map<String, LayeredBlockEntry>> LIST_TYPE = new TypeToken<>() {};
    private List<LayeredBlockLayer> Layers = new ArrayList<>();

    public List<LayeredBlockLayer> getLayers(){
        return Layers;
    }

    public LayeredBlockLayer getLayer(String textureName){
        for(LayeredBlockLayer layer : Layers){
            if(Objects.equals(layer.getName(), textureName))
                return layer;
        }
        return null;
    }

    @Override
    protected List<LayeredBlockLayer> prepare(ResourceManager resourceManager, Profiler profiler) {
        List<LayeredBlockLayer> layers = new ArrayList<>();
        for (String string : resourceManager.getAllNamespaces()) {
            try {
                List<Resource> list = resourceManager.getAllResources(Identifier.of(string, "layer_data.json"));
                for (Resource resource : list) {
                    try (BufferedReader reader = resource.getReader();) {
                        Map<String, LayeredBlockEntry> layerEntries = JsonHelper.deserialize(GSON, reader, LIST_TYPE);

                        for (Map.Entry<String, LayeredBlockEntry> entry : layerEntries.entrySet()) {
                            boolean canRecolor = entry.getValue().canRecolor();
                            String overlayString = entry.getValue().overlayTexture() == null ? "" : entry.getValue().overlayTexture();
                            Identifier overlay = Identifier.of(resource.getPackId(), overlayString);

                            Identifier[] textures = new Identifier[entry.getValue().textures().length];
                            for(int i = 0; i < entry.getValue().textures().length; i++) {
                                textures[i] = Identifier.of(resource.getPackId(), entry.getValue().textures()[i]);
                            }
                            LayeredBlockLayer layer = new LayeredBlockLayer(entry.getKey(), canRecolor, overlay, textures);
                            layers.add(layer);
                        }
                    } catch (RuntimeException runtimeException) {
                        FnafUniverseRebuilt.LOGGER.warn("Invalid {} in resourcepack: '{}'", "layer_data.json", resource.getPackId(), runtimeException);
                    }
                }
            } catch (IOException iOException) {
                // empty catch block
            }
        }
        return layers;
    }

    @Override
    protected void apply(List<LayeredBlockLayer> prepared, ResourceManager manager, Profiler profiler) {
        this.Layers.clear();
        this.Layers = prepared;
        for (LayeredBlockLayer layer : prepared) {
            FnafUniverseRebuilt.print(layer.getName());
        }
    }
}
