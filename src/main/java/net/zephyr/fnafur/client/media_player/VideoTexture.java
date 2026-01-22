package net.zephyr.fnafur.client.media_player;

public class VideoTexture {
//    private final Identifier id;
//    public final NativeImageBackedTexture texture;
//    //private final FrameGrab grab;
//    public int width;
//    public int height;
//    private final ExecutorService decoderThread;
//    private final FFmpegFrameGrabber grab;
//    private final Java2DFrameConverter converter = new Java2DFrameConverter();
//
//    public static Map<Identifier, NativeImage[]> TOTAL_CACHE = new HashMap<>();
//    public volatile NativeImage[] CACHE;
//    public double progression = 0;
//    public double frameRate;
//    public double videoLength;
//    public int totalFrames;
//    private long lastTimeNano = -1L;
//    boolean isDoneCashing = false;
//    public String name;
//    public String namespace;
//    SoundEvent soundEvent;
//
//    public static int ID = 0;
//
//    public VideoTexture(Identifier videoId) {
//        this.id = Identifier.of(videoId.getNamespace(), "videos/" + videoId.getPath() + ".mp4");
//        this.name = videoId.getPath();
//        this.namespace = videoId.getNamespace();
//        try{
//            this.grab = MediaPlayerUtil.fromIdentifier(videoId);
//            this.width = grab.getImageWidth();
//            this.height = grab.getImageHeight();
//            this.frameRate = grab.getFrameRate();
//            this.totalFrames = grab.getLengthInFrames();
//            this.videoLength = grab.getLengthInTime() / 1_000_000.0; // microseconds → seconds
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        this.texture = new NativeImageBackedTexture("Video " + ID, this.width, this.height, false);
//        MinecraftClient.getInstance().getTextureManager().registerTexture(id, texture);
//
//        this.decoderThread = Executors.newSingleThreadExecutor();
//        setupCache();
//        ID++;
//    }
//
//    void setupCache() {
//        if(true) return;
//        decoderThread.submit(() -> {
//            try {
//                VideoAudioToSoundEvent.createSoundEventFromVideo(Identifier.of(this.namespace, this.name), this.name);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            try {
//                if(TOTAL_CACHE.containsKey(this.id)) {
//                    CACHE = TOTAL_CACHE.get(this.id);
//                }
//                else {
//
//                    List<NativeImage> cache = new ArrayList<>();
//                    Frame frame;
//                    while ((frame = grab.grabImage()) != null) {
//                        BufferedImage bimg = converter.convert(frame);
//                        if (bimg == null) continue;
//                        cache.add(MediaPlayerUtil.toNativeImage(bimg));
//                        bimg.flush();
//                    }
//                    CACHE = cache.toArray(new NativeImage[0]);
//                    TOTAL_CACHE.put(this.id, CACHE);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//
//                isDoneCashing = true;
//
//                try { grab.stop(); } catch (Exception ignored) {}
//                try { grab.release(); } catch (Exception ignored) {}
//                try { grab.close(); } catch (Exception ignored) {}
//
//                decoderThread.shutdown();
//            }
//        });
//    }
//
//    public Identifier getTextureId() {
//        return id;
//    }
//
//    public boolean isDoneCashing(){
//        return isDoneCashing;
//    }
//
//    public void update() {
//        if (CACHE == null || CACHE.length == 0) return;
//
//        long now = System.nanoTime();
//        if (lastTimeNano < 0) lastTimeNano = now;
//        double elapsedSeconds = (now - lastTimeNano) / 1_000_000_000.0;
//        lastTimeNano = now;
//
//        progression += elapsedSeconds;
//        int frameNumber = (int) Math.floor(progression * frameRate);
//
//        if (frameNumber >= CACHE.length) {
//            progression = 0;
//            frameNumber = 0;
//            if(soundEvent != null){
//                VideoAudioToSoundEvent.playSoundEvent(soundEvent, 0, 0, 0, false);
//            }
//        }
//
//        NativeImage img = CACHE[frameNumber];
//        if (img != null) {
//            texture.getImage().copyFrom(img);
//            texture.upload();
//        }
//    }
//
//    void close(){
//        decoderThread.shutdownNow();
//        decoderThread.close();
//        converter.close();
//        MinecraftClient.getInstance().getTextureManager().destroyTexture(id);
//
//        try { grab.stop(); } catch (Exception ignored) {}
//        try { grab.release(); } catch (Exception ignored) {}
//        try { grab.close(); } catch (Exception ignored) {}
//
//        if (CACHE != null) for (NativeImage img : CACHE) img.close();
//        clearGlobalCache();
//    }
//    public static void clearGlobalCache() {
//        for (NativeImage[] arr : TOTAL_CACHE.values()) {
//            for (NativeImage img : arr) {
//                if (img != null) img.close();
//            }
//        }
//        TOTAL_CACHE.clear();
//    }

}