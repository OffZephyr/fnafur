package net.zephyr.fnafur.datagen;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.CachedOutput;
import net.minecraft.util.Mth;
import net.zephyr.fnafur.datagen.animations.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public final class JawFromOggAnimationProvider implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static final int TARGET_SAMPLE_RATE = 48_000;
    private static final int RMS_WINDOW_SAMPLES = 1024;
    private static final int RMS_STEP_SAMPLES = 480;
    private static final float SMOOTH_ALPHA = 0.35f;

    private static final float KEYFRAME_MIN_DEG_DELTA = 0.5f;
    private static final float KEYFRAME_MIN_TIME_DELTA_SEC = 1f / 30f;

    private static final float JAW_TALK_MIN = 0;
    private static final float HEAD_TALK_MIN = 0;
    private static final float JAW_TALK_MAX = -25f;
    private static final float HEAD_TALK_MAX = 10f;
    private static final float JAW_CAP = -27.5f;
    private static final float LOUD_THRESHOLD = 0.80f;
    private static final float HEAD_THRESHOLD = 0.20f;

    private final FabricDataOutput output;

    public JawFromOggAnimationProvider(FabricDataOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {

        Path listFile = output.getModContainer()
                .findPath("assets/fnafur/fnafur/jaw_ogg_list.txt")
                .orElseThrow();

        List<Path> oggFiles;
        try {
            oggFiles = loadList(listFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read ogg list file: " + listFile.toAbsolutePath(), e);
        }

        Path ffmpeg = FfmpegLocator.findOrThrow();

        JsonObject root = new JsonObject();
        root.addProperty("format_version", "1.8.0");

        JsonObject animations = new JsonObject();
        root.add("animations", animations);

        for (Path ogg : oggFiles) {
            if (!Files.exists(ogg) || !ogg.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".ogg")) {
                throw new IllegalArgumentException("Not a readable .ogg file: " + ogg);
            }

            String animationName = "animation.talk." + baseName(ogg);
            GeneratedJawAnimation anim = generateOne(ffmpeg, ogg);

            animations.add(animationName, GeckoAnimationJsonBuilder.buildJawOnly(anim));
        }

        Path outPath = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve("fnafur/geckolib/animations/entity/" + "jaw_movements.animation.json");

        return DataProvider.saveStable(writer, root, outPath);
    }

    @Override
    public String getName() {
        return "Jaw animations from OGG";
    }

    private static GeneratedJawAnimation generateOne(Path ffmpeg, Path ogg) {
        float[] samples = decodeOggToFloatPcmMono(
                ffmpeg, ogg
        );

        List<Float> env = RmsEnvelope.compute(samples, RMS_WINDOW_SAMPLES, RMS_STEP_SAMPLES);

        RmsEnvelope.normalizeInPlace(env);

        RmsEnvelope.emaSmoothInPlace(env, SMOOTH_ALPHA);

        float stepSeconds = (float) RMS_STEP_SAMPLES / (float) TARGET_SAMPLE_RATE;

        List<Keyframe> frames = new ArrayList<>(env.size());
        for (int i = 0; i < env.size(); i++) {
            float t = i * stepSeconds;
            float amp = env.get(i);
            float jawDeg = mapAmplitudeToJawDegrees(amp);
            float headDeg = mapAmplitudeToHeadDegrees(amp);
            frames.add(new Keyframe(t, -jawDeg, -headDeg));
        }

        List<Keyframe> reduced = reduceKeyframes(
                frames
        );

        if (reduced.isEmpty()) {
            reduced.add(new Keyframe(0f, JAW_TALK_MIN, HEAD_TALK_MIN));
        } else {

            if (reduced.getFirst().timeSec > 0f) {
                reduced.addFirst(new Keyframe(0f, reduced.getFirst().jawDeg, reduced.getFirst().headDeg));
            }

            float end = frames.isEmpty() ? 0f : frames.getLast().timeSec;
            Keyframe last = reduced.getLast();
            if (Math.abs(last.timeSec - end) > 0.0001f) {
                reduced.add(new Keyframe(end, last.jawDeg, last.headDeg));
            }
        }

        float duration = frames.isEmpty() ? 0f : frames.getLast().timeSec;

        return new GeneratedJawAnimation(duration, reduced);
    }



    public List<Path> loadList(Path listFile) throws IOException {
        if (!Files.exists(listFile)) {
            throw new IllegalArgumentException("OGG list file does not exist: " + listFile.toAbsolutePath());
        }

        Path cwd = Path.of("").toAbsolutePath().normalize();
        List<String> lines = Files.readAllLines(listFile, StandardCharsets.UTF_8);

        List<Path> out = new ArrayList<>();
        for (int idx = 0; idx < lines.size(); idx++) {
            String raw = lines.get(idx).trim();

            if (raw.isEmpty() || raw.startsWith("#")) continue;

            int hash = raw.indexOf('#');
            if (hash >= 0) raw = raw.substring(0, hash).trim();
            if (raw.isEmpty()) continue;

            Path p = output.getModContainer()
                    .findPath("assets/fnafur/sounds/" + raw)
                    .orElseThrow();
            if (!p.isAbsolute()) p = cwd.resolve(p).normalize();

            if (!p.getFileName().toString().toLowerCase().endsWith(".ogg")) {
                throw new IllegalArgumentException("Line " + (idx + 1) + " is not an .ogg path: " + lines.get(idx));
            }

            out.add(p);
        }

        if (out.isEmpty()) {
            throw new IllegalArgumentException("OGG list file contained no usable paths: " + listFile.toAbsolutePath());
        }

        return out;
    }

    public static List<JawFromOggAnimationProvider.Keyframe> reduceKeyframes(
            List<JawFromOggAnimationProvider.Keyframe> input
    ) {
        if (input.isEmpty()) return List.of();

        List<JawFromOggAnimationProvider.Keyframe> out = new ArrayList<>();
        JawFromOggAnimationProvider.Keyframe lastKept = null;

        for (JawFromOggAnimationProvider.Keyframe k : input) {
            if (lastKept == null) {
                out.add(k);
                lastKept = k;
                continue;
            }

            float dt = k.timeSec() - lastKept.timeSec();
            float ddJaw  = Math.abs(k.jawDeg()  - lastKept.jawDeg());
            float ddHead = Math.abs(k.headDeg() - lastKept.headDeg());

            if (dt >= KEYFRAME_MIN_TIME_DELTA_SEC && ddJaw >= KEYFRAME_MIN_DEG_DELTA || ddHead >= KEYFRAME_MIN_DEG_DELTA) {
                out.add(k);
                lastKept = k;
            }
        }

        JawFromOggAnimationProvider.Keyframe last = input.getLast();
        JawFromOggAnimationProvider.Keyframe keptLast = out.getLast();
        if (Math.abs(last.timeSec() - keptLast.timeSec()) > 0.0001f) {
            out.add(last);
        }

        return out;
    }

    public static String baseName(Path oggPath) {
        String name = oggPath.getFileName().toString();
        int dot = name.lastIndexOf('.');
        if (dot > 0) name = name.substring(0, dot);

        name = name.toLowerCase()
                .replaceAll("[^a-z0-9_\\-]+", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_+", "")
                .replaceAll("_+$", "");

        if (name.isEmpty()) name = "line";
        if(name.contains("_voice")){
            name = name.replace("_voice", "");
        }
        System.out.println("Animation: " + name);
        return name;
    }

    public static float mapAmplitudeToHeadDegrees(float amp) {
        if (amp <= HEAD_THRESHOLD) return 0f;

        float t = (amp - HEAD_THRESHOLD) / (1f - HEAD_THRESHOLD);
        t = Math.min(t, 1f);

        float eased = t * t;

        return eased * HEAD_TALK_MAX;
    }

    public static float mapAmplitudeToJawDegrees(
            float amp
    ) {
        amp = Mth.clamp(amp, 0, 1);

        float curved = (float) Math.pow(amp, 0.65);

        float jaw = Mth.lerp(curved, JAW_TALK_MIN, JAW_TALK_MAX);

        if (amp > LOUD_THRESHOLD) {
            float extra = (amp - LOUD_THRESHOLD) / (1f - LOUD_THRESHOLD);
            float eased = extra * extra;
            jaw = jaw + eased * (JAW_CAP - JAW_TALK_MAX);
        }

        if (jaw < JAW_CAP) jaw = JAW_CAP;

        return jaw;
    }

    public static float[] decodeOggToFloatPcmMono(Path ffmpegExe, Path ogg) {
        try {
            List<String> cmd = new ArrayList<>();
            cmd.add(ffmpegExe.toString());
            cmd.add("-v"); cmd.add("error");
            cmd.add("-i"); cmd.add(ogg.toAbsolutePath().toString());
            cmd.add("-ac"); cmd.add("1");
            cmd.add("-ar"); cmd.add(Integer.toString(TARGET_SAMPLE_RATE));
            cmd.add("-f"); cmd.add("f32le");
            cmd.add("pipe:1");

            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.redirectErrorStream(false);

            Process proc = pb.start();

            ByteArrayOutputStream stderr = new ByteArrayOutputStream();
            Thread errThread = new Thread(() -> {
                try (InputStream es = proc.getErrorStream()) {
                    es.transferTo(stderr);
                } catch (IOException ignored) {}
            }, "ffmpeg-stderr-reader");
            errThread.start();

            byte[] pcmBytes;
            try (InputStream is = proc.getInputStream();
                 ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                is.transferTo(bos);
                pcmBytes = bos.toByteArray();
            }

            int exit = proc.waitFor();
            errThread.join();

            if (exit != 0) {
                String msg = stderr.toString();
                throw new IllegalStateException("FFmpeg failed for " + ogg + " (exit " + exit + "):\n" + msg);
            }

            if (pcmBytes.length % 4 != 0) {
                throw new IllegalStateException("FFmpeg PCM stream length not divisible by 4 for " + ogg);
            }

            FloatBuffer fb = ByteBuffer.wrap(pcmBytes).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();
            float[] samples = new float[fb.remaining()];
            fb.get(samples);
            return samples;

        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while decoding: " + ogg, ie);
        } catch (IOException ioe) {
            throw new RuntimeException("IO error decoding: " + ogg, ioe);
        }
    }



    public record GeneratedJawAnimation(float lengthSec, List<Keyframe> keyframes) {}

    public record Keyframe(float timeSec, float jawDeg, float headDeg) {}
}