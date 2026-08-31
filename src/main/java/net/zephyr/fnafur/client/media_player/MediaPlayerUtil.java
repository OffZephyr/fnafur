package net.zephyr.fnafur.client.media_player;

public class MediaPlayerUtil {
//    private static final Map<Identifier, Path> VIDEO_CACHE = new HashMap<>();
//
//    public static FFmpegFrameGrabber fromIdentifier(Identifier id) throws IOException {
//        var resourceManager = Minecraft.getInstance().getResourceManager();
//
//        List<Resource> list = resourceManager.getAllResources(
//                Identifier.of(id.getNamespace(), "videos/" + id.getPath() + ".mp4")
//        );
//        if (list.isEmpty()) throw new IOException("Video not found: " + id);
//        Resource resource = list.get(0);
//
//        Path tempFile;
//        if (VIDEO_CACHE.containsKey(id)) {
//            tempFile = VIDEO_CACHE.get(id);
//        } else {
//            tempFile = Files.createTempFile(id.getPath(), ".mp4");
//            try (InputStream in = resource.getInputStream()) {
//                Files.copy(in, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
//            }
//            VIDEO_CACHE.put(id, tempFile);
//        }
//
//        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(tempFile.toFile());
//        grabber.start();
//        return grabber;
//    }
//
//    public static NativeImage toNativeImage(BufferedImage img) {
//        NativeImage nativeImage = new NativeImage(img.getWidth(), img.getHeight(), false);
//        for (int x = 0; x < img.getWidth(); x++) {
//            for (int y = 0; y < img.getHeight(); y++) {
//                int argb = img.getRGB(x, y);
//                int a = (argb >> 24) & 0xFF;
//                int b = (argb >> 16) & 0xFF;
//                int g = (argb >> 8) & 0xFF;
//                int r = argb & 0xFF;
//                nativeImage.setColor(x, y, ColorHelper.getArgb(a, r, g, b));
//            }
//        }
//        return nativeImage;
//    }
}
