package net.zephyr.fnafur.init;

import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.utility_blocks.computer.Apps.BrowserApp;
import net.zephyr.fnafur.blocks.utility_blocks.computer.Apps.CodeApp;
import net.zephyr.fnafur.blocks.utility_blocks.computer.Apps.MusicApp;
import net.zephyr.fnafur.blocks.utility_blocks.computer.Apps.RemoteApp;
import net.zephyr.fnafur.blocks.utility_blocks.computer.ComputerData;
import net.zephyr.fnafur.init.entity_init.ClassicInit;
import net.zephyr.fnafur.init.entity_init.EntityInit;
import net.zephyr.fnafur.util.Computer.ComputerAI;
import net.zephyr.fnafur.util.Computer.ComputerApp;
import net.zephyr.fnafur.util.Computer.ComputerPlaylist;
import net.zephyr.fnafur.util.Computer.ComputerSong;

import java.util.ArrayList;
import java.util.List;

public class DefaultComputerInit implements ComputerData.Initializer {

    @Override
    public List<Wallpaper> getWallpapers() {
        List<Wallpaper> wallpapers = new ArrayList<>();
        wallpapers.add(new Wallpaper("blue_checker", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/wallpapers/blue_checker.png")));
        wallpapers.add(new Wallpaper("red_checker", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/wallpapers/red_checker.png")));
        wallpapers.add(new Wallpaper("windows_xp", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/wallpapers/windows_xp.png")));
        wallpapers.add(new Wallpaper("markiplier", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/wallpapers/markiplier.png")));
        wallpapers.add(new Wallpaper("whistle", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/wallpapers/whistle.png")));
        wallpapers.add(new Wallpaper("rebuilt_logo", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/wallpapers/resuited_logo.png")));
        return wallpapers;
    }

    @Override
    public List<ComputerApp> getApps() {
        List<ComputerApp> apps = new ArrayList<>();
        apps.add(new MusicApp("music_player", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/icons/music_icon.png")));
        apps.add(new BrowserApp("browser", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/icons/browser_icon.png")));
        apps.add(new RemoteApp("remote", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/icons/remote_icon.png")));
        apps.add(new CodeApp(ScreensInit.COMPUTER_CODE, Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/icons/code_icon.png")));
        return apps;
    }

    @Override
    public List<ComputerSong> getSongs() {
        List<ComputerSong> songs = new ArrayList<>();
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_13.value(), "13", "C418", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/music_icons/alpha.png"), 178));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_CAT.value(), "Cat", "C418", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/music_icons/alpha.png"), 185));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_BLOCKS.value(), "Blocks", "C418", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/music_icons/beta.png"), 345));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_CHIRP.value(), "Chirp", "C418", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/music_icons/beta.png"), 185));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_FAR.value(), "Far", "C418", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/music_icons/beta.png"), 174));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_MALL.value(), "Mall", "C418", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/music_icons/beta.png"), 197));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_MELLOHI.value(), "Mellohi", "C418", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/music_icons/beta.png"), 96));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_STAL.value(), "Stal", "C418", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/music_icons/beta.png"), 150));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_STRAD.value(), "Strad", "C418", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/music_icons/beta.png"), 188));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_WARD.value(), "Ward", "C418", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/music_icons/beta.png"), 251));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_11.value(), "11", "C418", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/music_icons/beta.png"), 71));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_WAIT.value(), "Wait", "C418", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/music_icons/beta.png"), 238));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_PIGSTEP.value(), "Pigstep", "Lena Raine", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/music_icons/nether_update_ost.png"), 149));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_OTHERSIDE.value(), "Otherside", "Lena Raine", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/music_icons/caves_cliffs_ost.png"), 195));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_5.value(), "5", "Samuel Åberg", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/music_icons/wild_update_ost.png"), 178));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_RELIC.value(), "Relic", "Aaron Cherof", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/music_icons/trails_tales_ost.png"), 218));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_PRECIPICE.value(), "Precipice", "Aaron Cherof", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/music_icons/tricky_trials_ost.png"), 299));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_CREATOR.value(), "Creator", "Lena Raine", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/music_icons/tricky_trials_ost.png"), 176));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_CREATOR_MUSIC_BOX.value(), "Creator (Music Box)", "Lena Raine", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/music_icons/tricky_trials_ost.png"), 73));

        songs.add(new ComputerSong(SoundsInit.CASUAL_BONGOS, "Casual Bongos", "Some stock Library", Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/music_icons/casual_bongos.png"), 8));
        return songs;
    }

    @Override
    public List<ComputerPlaylist> getPlaylists() {

        List<ComputerPlaylist> playlists = new ArrayList<>();
        playlists.add(new ComputerPlaylist("Minecraft",
                "13",
                "Cat",
                "Blocks",
                "Chirp",
                "Far",
                "Mall",
                "Mellohi",
                "Stal",
                "Strad",
                "Ward",
                "11",
                "Wait",
                "Pigstep",
                "Otherside",
                "5",
                "Relic",
                "Precipice",
                "Creator",
                "Creator (Music Box)"
        ));

        playlists.add(new ComputerPlaylist("FnafUniverseRebuilt",
                "Casual Bongos"
        ));
        return playlists;
    }

    @Override
    public List<AnimatronicAI> getAnimatronics() {
        List<AnimatronicAI> AIs = new ArrayList<>();
        AIs.add(new AnimatronicAI(Text.translatable("fnafur.mod_name"), EntityInit.ZEPHYR, "fnafur.zephyr"));
        AIs.add(new AnimatronicAI(Text.translatable("fnafur.mod_name"), ClassicInit.CL_FRED, "fnafur.cl_fred"));
        AIs.add(new AnimatronicAI(Text.translatable("fnafur.mod_name"), ClassicInit.CL_BON, "fnafur.cl_bon"));
        AIs.add(new AnimatronicAI(Text.translatable("fnafur.mod_name"), ClassicInit.CL_CHICA, "fnafur.cl_chica"));
        AIs.add(new AnimatronicAI(Text.translatable("fnafur.mod_name"), ClassicInit.CL_FOXY, "fnafur.cl_foxy"));
        return AIs;
    }

    public List<AnimatronicAI> getClassics() {
        List<AnimatronicAI> AIs = new ArrayList<>();
        AIs.add(new AnimatronicAI(Text.translatable("fnafur.mod_name"), ClassicInit.CL_FRED, "fnafur.cl_fred"));
        AIs.add(new AnimatronicAI(Text.translatable("fnafur.mod_name"), ClassicInit.CL_BON, "fnafur.cl_bon"));
        AIs.add(new AnimatronicAI(Text.translatable("fnafur.mod_name"), ClassicInit.CL_CHICA, "fnafur.cl_chica"));
        AIs.add(new AnimatronicAI(Text.translatable("fnafur.mod_name"), ClassicInit.CL_FOXY, "fnafur.cl_foxy"));
        return AIs;
    }

    @Override
    public List<AnimatronicCategory<?>> getAICategories() {
        AnimatronicCategory<?> subCategory = new AnimatronicCategory<>(getAnimatronics(), Text.translatable("fnafur.computer.demo_subcategory"), "fnafur.entities");
        List<AnimatronicCategory<?>> subcategories = new ArrayList<>();
        subcategories.add(subCategory);

        AnimatronicCategory<?> Category = new AnimatronicCategory<>(subcategories, Identifier.of(FnafUniverseRebuilt.MOD_ID, "textures/gui/computer/gu_category_button.png"), "fnafur.main");

        AnimatronicCategory<?> Classics = new AnimatronicCategory<>(getClassics(), Text.translatable("fnafur.computer_category.classic"), "fnafur.classics");

        List<AnimatronicCategory<?>> categories = new ArrayList<>();
        categories.add(Category);
        categories.add(Classics);


        return categories;
    }
    @Override
    public List<ComputerAI> getAIBehaviors() {
        List<ComputerAI> AIs = new ArrayList<>();

        AIs.add(new ComputerAI("stage",
                new ComputerAI.Option<>("deactivated", false),
                new ComputerAI.Option<>("aggressive", false, "deactivated", true),
                new ComputerAI.Option<>("performing", false, "deactivated", true),
                new ComputerAI.Option<>("spawn_pos", true),
                new ComputerAI.Option<>("position", new BlockPos(0, 0, 0), "spawn_pos", true),
                new ComputerAI.Option<>("teleport", false),
                new ComputerAI.Option<>("spawn_rot", true),
                new ComputerAI.Option<>("rotation", 0, "spawn_rot", true)
        ));

        AIs.add(new ComputerAI("moving",
                //new ComputerAI.Option<>("weeping_angel", false),
                new ComputerAI.Option<>("aggressive", false),
                new ComputerAI.Option<>("look_around", true),
                new ComputerAI.Option<>("look_nearby_entities", true),
                new ComputerAI.Option<>("custom_moving_animation", false),
                new ComputerAI.Option<>("idle_animation", new ArrayList<String>(), "custom_moving_animation"),
                new ComputerAI.Option<>("walking_animation", new ArrayList<String>(), "custom_moving_animation"),
                new ComputerAI.Option<>("running_animation", new ArrayList<String>(), "custom_moving_animation")
        ));

        AIs.add(new ComputerAI("custom_path",
                new ComputerAI.Option<>("weeping_angel", false),
                new ComputerAI.Option<>("aggressive", false)));

        AIs.add(new ComputerAI("statue",
                new ComputerAI.Option<>("animation", new ArrayList<String>()),
                new ComputerAI.Option<>("spawn_pos", true),
                new ComputerAI.Option<>("position", new BlockPos(0, 0, 0), "spawn_pos", true),
                new ComputerAI.Option<>("teleport", false),
                new ComputerAI.Option<>("spawn_rot", true),
                new ComputerAI.Option<>("rotation", 0, "spawn_rot", true),
                new ComputerAI.Option<>("rotate_head", false),
                new ComputerAI.Option<>("head_pitch", 0, "rotate_head")
        ));

        AIs.add(new ComputerAI("controllable"));

        return AIs;
    }
}
