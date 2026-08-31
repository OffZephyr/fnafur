package net.zephyr.fnafur.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.client.media_player.VideoAudioToSoundEvent;
import net.zephyr.fnafur.client.media_player.VideoEntry;

public class VideoInit {
    public static final VideoEntry IGNITED_BONER = makeInstance(
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "ignited_boner")
    );
    public static final VideoEntry CHINESE = makeInstance(
            Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "golden_freddy")
    );

    static VideoEntry makeInstance(Identifier id){
        SoundEvent event = SoundEvent.createVariableRangeEvent(id);
        Registry.register(BuiltInRegistries.SOUND_EVENT, id, event);
        return new VideoEntry(id, event);
    }

    public static void registerVideos() {
        FnafUniverseRebuilt.LOGGER.info("Registering Videos for " + FnafUniverseRebuilt.MOD_ID.toUpperCase());
    }
}
