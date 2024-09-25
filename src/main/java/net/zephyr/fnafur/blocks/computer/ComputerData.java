package net.zephyr.fnafur.blocks.computer;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.util.Computer.ComputerAI;
import net.zephyr.fnafur.util.Computer.ComputerApp;
import net.zephyr.fnafur.util.Computer.ComputerPlaylist;
import net.zephyr.fnafur.util.Computer.ComputerSong;

import java.util.*;

public class ComputerData {
    protected static final List<Initializer> Initializers = new ArrayList<>();
    private static final List<Initializer.Wallpaper> Wallpapers = new ArrayList<>();
    private static final List<ComputerApp> Apps = new ArrayList<>();
    private static final List<ComputerSong> Songs = new ArrayList<>();
    private static final List<ComputerPlaylist> Playlists = new ArrayList<>();
    private static final List<Initializer.AnimatronicAI> AIAnimatronics = new ArrayList<>();
    private static final List<Initializer.AnimatronicCategory<?>> AICategories = new ArrayList<>();
    private static final List<ComputerAI> AIBehaviors = new ArrayList<>();

    public static void addInitializer(Initializer init){
        Initializers.add(init);
    }

    public static void runInitializers(){
        for (Initializer init: Initializers) {
            AIAnimatronics.addAll(init.getAnimatronics());
            AICategories.addAll(init.getAICategories());
            AIBehaviors.addAll(init.getAIBehaviors());
        }
    }
    public static void runInitializersClient(){
        for (Initializer init: Initializers) {
            Wallpapers.addAll(init.getWallpapers());
            Apps.addAll(init.getApps());
            Songs.addAll(init.getSongs());
            Playlists.addAll(init.getPlaylists());
            AIAnimatronics.addAll(init.getAnimatronics());
            AICategories.addAll(init.getAICategories());
            AIBehaviors.addAll(init.getAIBehaviors());
        }
    }
    public static List<Initializer.Wallpaper> getWallpapers(){
        return Wallpapers;
    }
    public static List<ComputerApp> getApps(){
        return Apps;
    }
    public static List<ComputerSong> getSongs(){
        return Songs;
    }
    public static ComputerSong getSong(String name) {
        for(ComputerSong song : getSongs()){
            if(Objects.equals(song.getName(), name)) return song;
        }
        return null;
    }
    public static List<ComputerPlaylist> getPlaylists(){
        return Playlists;
    }
    public static ComputerPlaylist getPlaylist(String name){
        for(ComputerPlaylist list : Playlists){
            if(Objects.equals(list.getName(), name)) return list;
        }
        return null;
    }
    public static List<Initializer.AnimatronicAI> getAIAnimatronics(){
        return AIAnimatronics;
    }
    public static Initializer.AnimatronicAI getAIAnimatronic(String id){
        for (Initializer.AnimatronicAI ai: AIAnimatronics) {
            if(ai.id().equals(id)) return ai;
        }
        return null;
    }
    public static List<Initializer.AnimatronicCategory<?>> getAICategories(){
        return AICategories;
    }
    public static Initializer.AnimatronicCategory<?> getAICategory(String id){
        for (Initializer.AnimatronicCategory<?> category: AICategories) {
            if(category.id.equals(id)) return category;
        }
        return null;
    }
    public static List<ComputerAI> getAIBehaviors(){
        return AIBehaviors;
    }
    public static ComputerAI getAIBehavior(String id){
        for (ComputerAI ai: AIBehaviors) {
            if(ai.getId().equals(id)) return ai;
        }
        return null;
    }


    public interface Initializer {
        default void register(){
            ComputerData.addInitializer(this);
        };
        List<Wallpaper> getWallpapers();
        List<ComputerApp> getApps();
        List<ComputerSong> getSongs();
        List<ComputerPlaylist> getPlaylists();
        List<AnimatronicAI> getAnimatronics();
        List<ComputerAI> getAIBehaviors();
        List<AnimatronicCategory<?>> getAICategories();

        record Wallpaper(String id, Identifier texture){
            public String getId(){
                return id();
            }
            public Identifier getTexture() {
                return texture;
            }
        }

        record AnimatronicAI(Text subTitle, EntityType<? extends LivingEntity> entityType, String id) {}

        public class AnimatronicCategory<E>{
            public List<E> list;
            public Identifier texture;
            public Text text;
            public String id;
            boolean subCategory = false;
            public AnimatronicCategory(List<E> list, Identifier texture, String id){
                this.subCategory = false;
                this.list = list;
                this.texture = texture;
                this.id = id;
            }
            public AnimatronicCategory(List<E> list, Text text, String id){
                this.subCategory = true;
                this.list = list;
                this.text = text;
                this.id = id;
            }
        }
    }
}
