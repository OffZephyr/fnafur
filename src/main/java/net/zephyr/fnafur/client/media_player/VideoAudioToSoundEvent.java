package net.zephyr.fnafur.client.media_player;

public class VideoAudioToSoundEvent {
//
//    public static void createSoundEventFromVideo(Identifier videoId, String name) {
//        try {
//            FFmpegFrameGrabber grabber = MediaPlayerUtil.fromIdentifier(videoId);
//
//            int channels = grabber.getAudioChannels();
//            int sampleRate = grabber.getSampleRate();
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            AudioFormat format = new AudioFormat(sampleRate, 16, channels, true, false);
//            try (AudioInputStream ais = new AudioInputStream(new FFmpegAudioInputStream(grabber), format,
//                    grabber.getLengthInAudioFrames())) {
//                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, baos);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//            grabber.stop();
//
//            try {
//
//                File tempFile = File.createTempFile(name, ".wav");
//                tempFile.deleteOnExit();
//                try (var fos = new FileOutputStream(tempFile)) {
//                    baos.writeTo(fos);
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static class FFmpegAudioInputStream extends java.io.InputStream {
//        private final FFmpegFrameGrabber grabber;
//        private ByteBuffer buffer = null;
//
//        public FFmpegAudioInputStream(FFmpegFrameGrabber grabber) {
//            this.grabber = grabber;
//        }
//
//        @Override
//        public int read() {
//            throw new UnsupportedOperationException("Use read(byte[], int, int)");
//        }
//
//        @Override
//        public int read(byte[] b, int off, int len) throws IOException {
//            try {
//                if (buffer == null || !buffer.hasRemaining()) {
//                    Frame frame = grabber.grabSamples();
//                    if (frame == null || frame.samples == null) return -1;
//
//                    ShortBuffer sb = (ShortBuffer) frame.samples[0];
//                    buffer = ByteBuffer.allocate(sb.remaining() * 2).order(ByteOrder.LITTLE_ENDIAN);
//                    while (sb.hasRemaining()) buffer.putShort(sb.get());
//                    buffer.flip();
//                }
//
//                int toRead = Math.min(len, buffer.remaining());
//                buffer.get(b, off, toRead);
//                return toRead;
//            } catch (Exception e) {
//                throw new IOException(e);
//            }
//        }
//    }
//
//    public static void playSoundEvent(SoundEvent event, double x, double y, double z, boolean spatial) {
//        SoundManager soundManager = Minecraft.getInstance().getSoundManager();
//        if (spatial) {
//            soundManager.play(new PositionedSoundInstance(event, SoundCategory.MASTER, 1.0f, 1.0f, Random.create(), x, y, z));
//        } else {
//            soundManager.play(PositionedSoundInstance.master(event, 1.0f));
//        }
//    }
}