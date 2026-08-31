package net.zephyr.fnafur.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;

import java.util.HashMap;
import java.util.Map;

public class SoundsInit {
    public static Map<String, SoundEvent> sounds = new HashMap<>();
    public static Map<SoundEvent, Boolean> soundRepeats = new HashMap<>();
    public static Map<SoundEvent, String> soundKeys = new HashMap<>();

    public static final SoundEvent CAM_OPEN = registerSoundEvent("cam_open");
    public static final SoundEvent CAM_CLOSE = registerSoundEvent("cam_close");
    public static final SoundEvent CAM_SWITCH = registerSoundEvent("cam_switch");
    public static final SoundEvent CAM_NV_ON = registerSoundEvent("cam_nv_on");
    public static final SoundEvent CAM_NV_OFF = registerSoundEvent("cam_nv_off");
    public static final SoundEvent CLICK_PRESS = registerSoundEvent("click_press");
    public static final SoundEvent CLICK_RELEASE = registerSoundEvent("click_release");
    public static final SoundEvent CASUAL_BONGOS = registerSoundEvent("casual_bongos");
    public static final SoundEvent MAIN_MENU = registerSoundEvent("main_menu");
    public static final SoundEvent CREDITS_MENU_LOOP = registerSoundEvent("credits_loop");
    public static final SoundEvent THANK_YOU_FOR_YOUR_PATIENCE = registerSoundEvent("thank_you_for_your_patience");
    public static final SoundEvent ZEPHYR_JUMPSCARE = registerSoundEvent("zephyr_jumpscare");
    public static final SoundEvent FNAF1_JUMPSCARE = registerSoundEvent("fnaf1_jumpscare");
    public static final SoundEvent FNAF1_FOOTSTEPS = registerSoundEvent("fnaf1_footsteps");
    public static final SoundEvent FOXY_FOOTSTEPS = registerSoundEvent("foxy_footsteps");
    public static final SoundEvent BEAR5 = registerSoundEvent("bear5", true);
    public static final SoundEvent ANIMATRONIC_HURT = registerSoundEvent("animatronic_hurt");
    public static final SoundEvent ANIMATRONIC_DEATH = registerSoundEvent("animatronic_death");
    public static final SoundEvent COSMO_GIFT_USE = registerSoundEvent("cosmo_gift_use");

    public static final SoundEvent FREDDY_LAUGH1 = registerSoundEvent("freddy_laugh1");
    public static final SoundEvent FREDDY_LAUGH2 = registerSoundEvent("freddy_laugh2");
    public static final SoundEvent FREDDY_LAUGH3 = registerSoundEvent("freddy_laugh3");
    public static final SoundEvent ANIMATRONIC_GROAN1 = registerSoundEvent("animatronic_groan1");
    public static final SoundEvent ANIMATRONIC_GROAN2 = registerSoundEvent("animatronic_groan2");
    public static final SoundEvent ANIMATRONIC_GROAN3 = registerSoundEvent("animatronic_groan3");
    public static final SoundEvent ANIMATRONIC_GROAN4 = registerSoundEvent("animatronic_groan4");
    public static final SoundEvent SOMEBODYS_WATCHING_ME = registerSoundEvent("somebodys_watching_me");

    public static final SoundEvent FREDDY_MUSIC_BOX = registerSoundEvent("freddy_music_box");
    public static final SoundEvent HONK = registerSoundEvent("honk");
    public static final SoundEvent HONK_MENU = registerSoundEvent("honk_menu");
    public static final SoundEvent OFFICE_DOOR_ACTIVATE = registerSoundEvent("office_door_activate");
    public static final SoundEvent OFFICE_DOOR_ERROR = registerSoundEvent("office_door_error");
    public static final SoundEvent OFFICE_DOOR_KNOCK = registerSoundEvent("office_door_knock");
    public static final SoundEvent OFFICE_DOOR_LIGHT = registerSoundEvent("office_door_light");
    public static final SoundEvent ELECTRICAL_LOCKER_OPEN = registerSoundEvent("electrical_locker_open");
    public static final SoundEvent ELECTRICAL_LOCKER_CLOSE = registerSoundEvent("electrical_locker_close");
    public static final SoundEvent ELECTRICAL_LOCKER_SWITCH = registerSoundEvent("electrical_locker_switch");
    public static final SoundEvent LIGHT_SWITCH_FLIP = registerSoundEvent("switch_flip");
    public static final SoundEvent DESK_FAN = registerSoundEvent("desk_fan", true);
    public static final SoundEvent VANNI_MASK_EQUIP = registerSoundEvent("vanni_mask_equip");
    public static final SoundEvent VANNI_MASK_UNEQUIP = registerSoundEvent("vanni_mask_unequip");
    public static final SoundEvent AR_REVEAL = registerSoundEvent("ar_reveal");
    public static final SoundEvent AR_LEAVE = registerSoundEvent("ar_leave");

    // CUTOUTS
    public static final SoundEvent DAWKO_PLACE = registerSoundEvent("dawko_place");
    public static final SoundEvent DAWKO = registerSoundEvent("dawko");
    public static final SoundEvent MARKIPLIER_PLACE = registerSoundEvent("markiplier_place");
    public static final SoundEvent MARKIPLIER = registerSoundEvent("markiplier");
    public static final SoundEvent MATPAT_PLACE = registerSoundEvent("matpat_place");
    public static final SoundEvent MATPAT = registerSoundEvent("matpat");
    public static final SoundEvent DASEN = registerSoundEvent("dasen");
    public static final SoundEvent DASEN_PLACE = registerSoundEvent("dasen_place");
    public static final SoundEvent HELPULES_PLACE = registerSoundEvent("helpules_place");
    public static final SoundEvent HELPULES = registerSoundEvent("helpules");

    // NAVIGATION
    public static final SoundEvent CREDITS_SCROLL = registerSoundEvent("credits_scroll");
    public static final SoundEvent CREDITS_HOVER = registerSoundEvent("credits_hover");

    public static SoundEvent getSound(String name){
        return sounds.get(name);
    }
    public static String getSoundName(SoundEvent sound){
        return soundKeys.get(sound);
    }
    public static boolean soundRepeats(SoundEvent sound){
        return soundRepeats.get(sound);
    }
    public static SoundEvent registerSoundEvent(String name){
        return registerSoundEvent(name, false);
    }
    public static SoundEvent registerSoundEvent(String name, boolean repeats){
        Identifier id = Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, name);
        SoundEvent sound = Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
        sounds.put(name, sound);
        soundKeys.put(sound, name);
        soundRepeats.put(sound, repeats);
        return sound;
    }

    public static void registerSounds() {
        FnafUniverseRebuilt.LOGGER.info("Registering Sounds for " + FnafUniverseRebuilt.MOD_ID.toUpperCase());
        //VideoInit.registerVideos();
    }
}
