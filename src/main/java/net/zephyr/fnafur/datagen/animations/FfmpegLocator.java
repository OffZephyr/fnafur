package net.zephyr.fnafur.datagen.animations;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public final class FfmpegLocator {
    private FfmpegLocator() {}

    public static Path findOrThrow() {
        List<Path> candidates = new ArrayList<>();

        boolean windows = System.getProperty("os.name").toLowerCase().contains("win");
        String exeName = windows ? "ffmpeg.exe" : "ffmpeg";

        if (isExecutableOnPath(exeName)) {
            Path fromWhich = tryWhich(exeName);
            if (fromWhich != null) return fromWhich;
            return Path.of(exeName);
        }

        if (windows) {
            candidates.add(Path.of("C:/ffmpeg/bin/ffmpeg.exe"));
            candidates.add(Path.of("D:/ffmpeg/bin/ffmpeg.exe"));
            candidates.add(Path.of("D:/PROGRAMS/ffmpeg/bin/ffmpeg.exe"));
            candidates.add(Path.of("C:/Program Files/ffmpeg/bin/ffmpeg.exe"));
            candidates.add(Path.of("C:/ProgramData/chocolatey/bin/ffmpeg.exe"));
            candidates.add(Path.of("C:/Program Files (x86)/ffmpeg/bin/ffmpeg.exe"));
        } else {
            candidates.add(Path.of("/usr/bin/ffmpeg"));
            candidates.add(Path.of("/usr/local/bin/ffmpeg"));
            candidates.add(Path.of("/opt/homebrew/bin/ffmpeg"));
            candidates.add(Path.of("/usr/local/opt/ffmpeg/bin/ffmpeg"));
        }

        candidates.add(Path.of("tools/ffmpeg/" + (windows ? "windows" : "unix") + "/" + exeName));

        for (Path p : candidates) {
            Path abs = p.toAbsolutePath().normalize();
            if (Files.exists(abs) && Files.isRegularFile(abs)) return abs;
        }

        throw new IllegalStateException(
                "FFmpeg not found.\n" +
                        "Install ffmpeg and ensure it's on PATH, or place it at one of these locations:\n" +
                        String.join("\n", candidates.stream().map(x -> "  - " + x.toString()).toList())
        );
    }

    private static boolean isExecutableOnPath(String exeName) {
        try {
            Process p = new ProcessBuilder(exeName, "-version")
                    .redirectErrorStream(true)
                    .start();
            int code = p.waitFor();
            return code == 0;
        } catch (Exception ignored) {
            return false;
        }
    }

    private static Path tryWhich(String exeName) {
        boolean windows = System.getProperty("os.name").toLowerCase().contains("win");
        String tool = windows ? "where" : "which";
        try {
            Process p = new ProcessBuilder(tool, exeName)
                    .redirectErrorStream(true)
                    .start();
            byte[] out = p.getInputStream().readAllBytes();
            int code = p.waitFor();
            if (code != 0) return null;

            String firstLine = new String(out).trim().split("\\R")[0].trim();
            if (firstLine.isEmpty()) return null;
            return Path.of(firstLine);
        } catch (IOException | InterruptedException ignored) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}