package net.zephyr.fnafur.util.jsonReaders.credits;

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
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.profiler.Profiler;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.util.jsonReaders.animatronics.AnimatronicDataHandler;
import net.zephyr.fnafur.util.jsonReaders.entity_skins.DefaultEntityData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Environment(EnvType.CLIENT)
public class CreditsDataManager extends SinglePreparationResourceReloader<Map<String, Map<String, String>>> {
    static final Gson GSON = new Gson();

    private static final TypeToken<Map<String, Map<String, Map<String, Entry>>>> STRING_LIST_TYPE = new TypeToken<>() {};

    @Override
    protected Map<String, Map<String, String>> prepare(ResourceManager resourceManager, Profiler profiler) {

        clear();
        Map<String, Map<String, String>> data = new HashMap<>();
        for (String string : resourceManager.getAllNamespaces()) {
            getEntries(resourceManager, profiler, string);
        }

        getSubscribers("https://raw.githubusercontent.com/OffZephyr/fnafur_data/refs/heads/main/kofi-complete-subscribers.json");

//        for(String main : CreditsDataHandler.MAIN_CATEGORIES) {
//            System.out.println(main);
//            List<String> subCategories = CreditsDataHandler.SUB_PER_MAIN_CATEGORY.get(main);
//            if(subCategories != null){
//                for (String sub : subCategories) {
//                    System.out.println(" " + sub);
//                    List<CreditsDataHandler.CreditsEntry> entries = CreditsDataHandler.ENTRIES.get(main).get(sub);
//                    if(entries != null) {
//                        for (CreditsDataHandler.CreditsEntry entry : entries) {
//                            System.out.println("  " + entry.NAME());
//                        }
//                    }
//                }
//            }
//        }
        return data;
    }

    @Override
    protected void apply(Map<String, Map<String, String>> prepared, ResourceManager manager, Profiler profiler) {

    }

    void getSubscribers(String link){

        Map<String, String> subscribers = new HashMap<>();
        try {
            subscribers = SubscriberFetcher.fetchSubscribers(link);
        } catch (IOException e) {
            System.err.println("Error fetching subscribers: " + e.getMessage());
        }

        if(!subscribers.isEmpty()) {
            CreditsDataHandler.MAIN_CATEGORIES.add("supporters");
            CreditsDataHandler.SUB_PER_MAIN_CATEGORY.putIfAbsent("supporters", List.of("kofi_supporters"));
            Map<String, List<CreditsDataHandler.CreditsEntry>> category = new HashMap<>();
            List<CreditsDataHandler.CreditsEntry> entries = new ArrayList<>();
            subscribers.forEach((name, rank) -> {
                String rank2 = rank.toLowerCase().contains("family") || rank.toLowerCase().contains("gold") ? "fazbear_family" : "pizza_lover";

                int color = rank2.equals("fazbear_family") ? 0xFF2d74ad : 0xFFad4f2d;

                CreditsDataHandler.CreditsEntry entry = new CreditsDataHandler.CreditsEntry(name, "https://ko-fi.com/fnafuniverserebuilt", Identifier.of(""), color, List.of(rank2));
                entries.add(entry);
                CreditsDataHandler.ALL_ENTRIES.putIfAbsent(entry.NAME(), entry);
            });
            category.putIfAbsent("kofi_supporters", entries);
            CreditsDataHandler.ENTRIES.putIfAbsent("supporters", category);
        }
    }

    void getEntries(ResourceManager resourceManager, Profiler profiler, String namespace) {
        String path = FnafUniverseRebuilt.MOD_ID + "/credits.json";
        List<Resource> list = resourceManager.getAllResources(Identifier.of(namespace, path));
        for (Resource resource : list) {
            try (BufferedReader reader = resource.getReader();) {
                Map<String, Map<String, Map<String, Entry>>> layerEntries = JsonHelper.deserialize(GSON, reader, STRING_LIST_TYPE);
                for (Map.Entry<String, Map<String, Map<String, Entry>>> entry : layerEntries.entrySet()) {

                    CreditsDataHandler.MAIN_CATEGORIES.add(entry.getKey());
                    CreditsDataHandler.SUB_PER_MAIN_CATEGORY.putIfAbsent(entry.getKey(), entry.getValue().keySet().stream().toList());

                    Map<String, List<CreditsDataHandler.CreditsEntry>> category = new HashMap<>();
                    for(String sub : entry.getValue().keySet()){
                        List<CreditsDataHandler.CreditsEntry> entries = new ArrayList<>();

                        for(String key : entry.getValue().get(sub).keySet()){
                            Entry entry1 = entry.getValue().get(sub).get(key);
                            System.out.println(entry1.color.get(0) + " " + entry1.color.get(1) + " " + entry1.color.get(2));
                            int color = ColorHelper.getArgb(255, entry1.color.get(0), entry1.color.get(1), entry1.color.get(2));
                            CreditsDataHandler.CreditsEntry creditsEntry = new CreditsDataHandler.CreditsEntry(key, entry1.link, Identifier.of(namespace, entry1.logo), color, entry1.roles);
                            entries.add(creditsEntry);
                            CreditsDataHandler.ALL_ENTRIES.putIfAbsent(creditsEntry.NAME(), creditsEntry);
                        }
                        category.putIfAbsent(sub, entries);
                    }
                    CreditsDataHandler.ENTRIES.putIfAbsent(entry.getKey(), category);
                    //for(int i = 0; i <  entry.getValue().size(); i++){
                        //System.out.println(entry.getKey() + ": " + entry.getValue().get(i));
                    //}
                }
            } catch (RuntimeException | IOException runtimeException) {
                FnafUniverseRebuilt.LOGGER.warn("Invalid {} in resourcepack: '{}'", path, resource.getPackId(), runtimeException);
            }
        }
    }

    void clear(){
        CreditsDataHandler.SUB_PER_MAIN_CATEGORY.clear();
        CreditsDataHandler.MAIN_CATEGORIES.clear();
        CreditsDataHandler.ENTRIES.clear();
        CreditsDataHandler.ALL_ENTRIES.clear();
    }

    public record Entry(String link, String logo, List<Integer> color, List<String> roles){};
}
