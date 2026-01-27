package net.zephyr.fnafur.datagen.animations;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.zephyr.fnafur.datagen.JawFromOggAnimationProvider;

import java.util.Locale;

public final class GeckoAnimationJsonBuilder {
    private GeckoAnimationJsonBuilder() {}

    public static JsonObject buildJawOnly(JawFromOggAnimationProvider.GeneratedJawAnimation anim) {
        JsonObject animation = new JsonObject();
        animation.addProperty("loop", false);

        animation.addProperty("animation_length", round4(anim.lengthSec()));

        JsonObject bones = new JsonObject();
        JsonObject jaw = new JsonObject();

        JsonObject rotation = new JsonObject();

        for (JawFromOggAnimationProvider.Keyframe k : anim.keyframes()) {
            String tKey = formatTimeKey(k.timeSec());

            JsonObject vecObj = new JsonObject();
            JsonArray vec = new JsonArray();
            vec.add(round3(k.jawDeg()));
            vec.add(0);
            vec.add(0);
            vecObj.add("vector", vec);

            rotation.add(tKey, vecObj);
        }

        JsonObject head = new JsonObject();
        JsonObject headRotation = new JsonObject();

        for (JawFromOggAnimationProvider.Keyframe k : anim.keyframes()) {
            if (Math.abs(k.headDeg()) < 0.01f) continue;

            JsonObject vecObj = new JsonObject();
            JsonArray vec = new JsonArray();
            vec.add(round3(k.headDeg()));
            vec.add(0);
            vec.add(0);
            vecObj.add("vector", vec);

            headRotation.add(formatTimeKey(k.timeSec()), vecObj);
        }

        if (!headRotation.isEmpty()) {
            head.add("rotation", headRotation);
        }


        jaw.add("rotation", rotation);
        bones.add("jaw", jaw);
        bones.add("head", head);

        animation.add("bones", bones);
        return animation;
    }

    private static String formatTimeKey(float sec) {
        return String.format(Locale.ROOT, "%.4f", sec);
    }

    private static float round3(float v) {
        return Math.round(v * 1000f) / 1000f;
    }

    private static float round4(float v) {
        return Math.round(v * 10000f) / 10000f;
    }
}