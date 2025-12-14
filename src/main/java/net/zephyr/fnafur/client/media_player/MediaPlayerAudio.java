package net.zephyr.fnafur.client.media_player;


import io.netty.util.concurrent.SingleThreadEventExecutor;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.lwjgl.openal.AL10;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Safe OpenAL video audio player for Minecraft Fabric 1.21.
 * Streams PCM from FFmpegFrameGrabber in a separate thread.
 */
public class MediaPlayerAudio {

    private final FFmpegFrameGrabber grabber;
    private boolean spatial;
    private final float posX, posY, posZ;

    private int sourceId;
    private volatile boolean running = false;
    private ExecutorService decodeThread;

    public MediaPlayerAudio(FFmpegFrameGrabber grabber, boolean spatial, float x, float y, float z) {
        this.grabber = grabber;
        //this.grabber.start();

        this.spatial = spatial;
        this.posX = x;
        this.posY = y;
        this.posZ = z;

        // Generate OpenAL source (Minecraft already has a valid context)
        sourceId = AL10.alGenSources();
        AL10.alSourcef(sourceId, AL10.AL_GAIN, 1.0f);
        AL10.alSourcef(sourceId, AL10.AL_PITCH, 1.0f);
        AL10.alSourcei(sourceId, AL10.AL_LOOPING, AL10.AL_FALSE);

        if (spatial) {
            AL10.alSourcei(sourceId, AL10.AL_SOURCE_RELATIVE, AL10.AL_FALSE);
            AL10.alSource3f(sourceId, AL10.AL_POSITION, posX, posY, posZ);
        } else {
            AL10.alSourcei(sourceId, AL10.AL_SOURCE_RELATIVE, AL10.AL_TRUE);
        }

        decodeThread  = Executors.newSingleThreadExecutor();
    }

    /** Start decoding and playing audio in a separate thread */
    public void play() {
        if (running) return;
        running = true;

        decodeThread.submit(() -> {
            try {
                Frame frame;
                while (running && (frame = grabber.grabSamples()) != null) {
                    if (frame.samples != null && frame.samples.length > 0) {
                        ShortBuffer sbuf = (ShortBuffer) frame.samples[0];

                        // Use DIRECT buffer for OpenAL safety
                        ByteBuffer bb = ByteBuffer.allocateDirect(sbuf.remaining() * 2)
                                .order(ByteOrder.LITTLE_ENDIAN);
                        while (sbuf.hasRemaining()) bb.putShort(sbuf.get());
                        bb.flip();

                        int format = (grabber.getAudioChannels() == 1) ?
                                AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16;

                        // Unqueue processed buffers to avoid overflow
                        int processed = AL10.alGetSourcei(sourceId, AL10.AL_BUFFERS_PROCESSED);
                        for (int i = 0; i < processed; i++) {
                            int buf = AL10.alSourceUnqueueBuffers(sourceId);
                            AL10.alDeleteBuffers(buf);
                        }

                        int bufferId = AL10.alGenBuffers();
                        AL10.alBufferData(bufferId, format, bb, grabber.getSampleRate());
                        AL10.alSourceQueueBuffers(sourceId, bufferId);

                        // Start playing if not already
                        int state = AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE);
                        if (state != AL10.AL_PLAYING) AL10.alSourcePlay(sourceId);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "Video-Audio-Thread");

    }

    public void playFrom(double seconds) {
        stop(); // stop any previous playback
        running = true;

        decodeThread.submit(() -> {
            try {
                // Restart grabber cleanly
                grabber.stop(); // just in case
                grabber.start();

                // Seek to timestamp (in microseconds)
                grabber.setTimestamp((long) (seconds * 1_000_000));

                Frame frame;
                while (running && (frame = grabber.grabSamples()) != null) {
                    if (frame.samples != null && frame.samples.length > 0) {
                        ShortBuffer sbuf = (ShortBuffer) frame.samples[0];
                        ByteBuffer bb = ByteBuffer.allocateDirect(sbuf.remaining() * 2)
                                .order(ByteOrder.LITTLE_ENDIAN);
                        while (sbuf.hasRemaining()) bb.putShort(sbuf.get());
                        bb.flip();

                        int format = (grabber.getAudioChannels() == 1)
                                ? AL10.AL_FORMAT_MONO16
                                : AL10.AL_FORMAT_STEREO16;

                        // Clean old buffers
                        int processed = AL10.alGetSourcei(sourceId, AL10.AL_BUFFERS_PROCESSED);
                        for (int i = 0; i < processed; i++) {
                            int buf = AL10.alSourceUnqueueBuffers(sourceId);
                            AL10.alDeleteBuffers(buf);
                        }

                        int bufferId = AL10.alGenBuffers();
                        AL10.alBufferData(bufferId, format, bb, grabber.getSampleRate());
                        AL10.alSourceQueueBuffers(sourceId, bufferId);

                        // Begin playback if not already
                        int state = AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE);
                        if (state != AL10.AL_PLAYING)
                            AL10.alSourcePlay(sourceId);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    grabber.stop();
                } catch (Exception ignored) {}
            }
        }, "Video-Audio-Thread");
    }

    /** Stop playback and clean up OpenAL buffers */
    public void stop() {
        running = false;
        if (decodeThread != null) decodeThread.shutdownNow();

        AL10.alSourceStop(sourceId);

        // Unqueue and delete any remaining buffers
        int processed = AL10.alGetSourcei(sourceId, AL10.AL_BUFFERS_PROCESSED);
        for (int i = 0; i < processed; i++) {
            int buf = AL10.alSourceUnqueueBuffers(sourceId);
            AL10.alDeleteBuffers(buf);
        }

        AL10.alDeleteSources(sourceId);

        try {
            grabber.stop();
            grabber.close();
        } catch (Exception ignored) {}
    }

    public void setSpatial(boolean spatial){
        this.spatial = spatial;
    }
    /** Update spatial position at runtime */
    public void setPosition(float x, float y, float z) {
        if (!spatial) return;
        AL10.alSource3f(sourceId, AL10.AL_POSITION, x, y, z);
    }

    /** Adjust volume (0–1) */
    public void setVolume(float volume) {
        AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
    }

    /** Adjust pitch (1.0 = normal) */
    public void setPitch(float pitch) {
        AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
    }

    public void close() {
        stop();
        if(grabber != null){
            try { grabber.stop(); } catch (Exception ignored) {}
            try { grabber.release(); } catch (Exception ignored) {}
            try { grabber.close(); } catch (Exception ignored) {}
        }
        decodeThread.shutdownNow();
        decodeThread.close();
    }
}