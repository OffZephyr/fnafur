package net.zephyr.fnafur.init;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.client.media_player.VideoAudioToSoundEvent;
import net.zephyr.fnafur.client.media_player.VideoEntry;

public class VideoInit {
    public static final VideoEntry IGNITED_BONER = makeInstance(
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "ignited_boner")
    );
    public static final VideoEntry CHINESE = makeInstance(
            Identifier.of(FnafUniverseRebuilt.MOD_ID, "golden_freddy")
    );

    static VideoEntry makeInstance(Identifier id){
        SoundEvent event = SoundEvent.of(id);
        Registry.register(Registries.SOUND_EVENT, id, event);
        return new VideoEntry(id, event);
    }

    public static void registerVideos() {
        FnafUniverseRebuilt.LOGGER.info("Registering Videos for " + FnafUniverseRebuilt.MOD_ID.toUpperCase());
    }
}
