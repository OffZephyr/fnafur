package net.zephyr.fnafur.init;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseResuited;

public class SoundsInit {
    public static final SoundEvent CAM_OPEN = registerSoundEvent("cam_open");
    public static final SoundEvent CAM_CLOSE = registerSoundEvent("cam_close");
    public static final SoundEvent CAM_SWITCH = registerSoundEvent("cam_switch");
    public static final SoundEvent CAM_NV_ON = registerSoundEvent("cam_nv_on");
    public static final SoundEvent CAM_NV_OFF = registerSoundEvent("cam_nv_off");
    public static final SoundEvent CLICK_PRESS = registerSoundEvent("click_press");
    public static final SoundEvent CLICK_RELEASE = registerSoundEvent("click_release");
    public static final SoundEvent CASUAL_BONGOS = registerSoundEvent("casual_bongos");
    public static final SoundEvent ZEPHYR_JUMPSCARE = registerSoundEvent("zephyr_jumpscare");
    public static final SoundEvent FNAF1_JUMPSCARE = registerSoundEvent("fnaf1_jumpscare");
    public static final SoundEvent FNAF1_FOOTSTEPS = registerSoundEvent("fnaf1_footsteps");
    public static final SoundEvent FOXY_FOOTSTEPS = registerSoundEvent("foxy_footsteps");
    public static final SoundEvent ANIMATRONIC_HURT = registerSoundEvent("animatronic_hurt");
    public static final SoundEvent ANIMATRONIC_DEATH = registerSoundEvent("animatronic_death");
    public static final SoundEvent OFFICE_DOOR_ACTIVATE = registerSoundEvent("office_door_activate");
    public static final SoundEvent OFFICE_DOOR_ERROR = registerSoundEvent("office_door_error");
    public static final SoundEvent OFFICE_DOOR_KNOCK = registerSoundEvent("office_door_knock");
    public static final SoundEvent OFFICE_DOOR_LIGHT = registerSoundEvent("office_door_light");
    public static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(FnafUniverseResuited.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        FnafUniverseResuited.LOGGER.info("Registering Sounds for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }
}
