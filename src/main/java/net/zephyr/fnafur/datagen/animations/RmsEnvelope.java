package net.zephyr.fnafur.datagen.animations;

import java.util.ArrayList;
import java.util.List;

public final class RmsEnvelope {
    private RmsEnvelope() {}

    public static List<Float> compute(float[] samples, int window, int step) {
        if (samples.length < window) return List.of();

        List<Float> env = new ArrayList<>();
        for (int start = 0; start + window <= samples.length; start += step) {
            float sum = 0f;
            for (int i = 0; i < window; i++) {
                float s = samples[start + i];
                sum += s * s;
            }
            float rms = (float) Math.sqrt(sum / window);
            env.add(rms);
        }
        return env;
    }

    public static void normalizeInPlace(List<Float> env) {
        float max = 0f;
        for (float v : env) max = Math.max(max, v);
        if (max <= 1e-9f) return;

        for (int i = 0; i < env.size(); i++) {
            env.set(i, env.get(i) / max);
        }
    }

    public static void emaSmoothInPlace(List<Float> env, float alpha) {
        float prev = 0f;
        for (int i = 0; i < env.size(); i++) {
            float v = env.get(i);
            prev = prev + alpha * (v - prev);
            env.set(i, prev);
        }
    }
}