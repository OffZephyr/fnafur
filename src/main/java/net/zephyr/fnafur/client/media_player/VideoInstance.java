package net.zephyr.fnafur.client.media_player;

import net.minecraft.client.texture.NativeImage;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoInstance implements AutoCloseable{
    VideoEntry entry;
    VideoTexture texture;
    public double progression = 0;
    private long lastTimeNano = -1L;
    MediaPlayerAudio audio;
    boolean isDone = false;
    boolean repeats;
    public static List<VideoInstance> instances = new ArrayList<>();

    public VideoInstance(VideoEntry entry, boolean repeats){
        this.entry = entry;
        this.repeats = repeats;
        instances.add(this);
    }

    public void update() {
        if(!getTexture().isDoneCashing() || isDone) {
            NativeImage img = new NativeImage(getImageWidth(), getImageHeight(), false);
            for (int x = 0; x < getImageWidth(); x++) {

                for (int y = 0; y < getImageHeight(); y++) {
                    img.setColor(x, y, 0xFF000000);
                }
            }
            getTexture().texture.getImage().copyFrom(img);
            getTexture().texture.upload();
            return;
        };
        if (getTexture().CACHE == null || getTexture().CACHE.length == 0) return;

        long now = System.nanoTime();
        if (lastTimeNano < 0) lastTimeNano = now;
        double elapsedSeconds = (now - lastTimeNano) / 1_000_000_000.0;
        lastTimeNano = now;

        if(progression == 0 && getSoundEvent() != null){
            getMediaPlayer().play();
        }

        progression += elapsedSeconds;
        int frameNumber = (int) Math.floor(progression * getTexture().frameRate);

        isDone = frameNumber >= getTexture().CACHE.length;
        if (isDone) {
            if(repeats){
                restartVideo();
                update();
            }
            return;
        }

        NativeImage img = getTexture().CACHE[frameNumber];
        if (img != null) {
            getTexture().texture.getImage().copyFrom(img);
            getTexture().texture.upload();
        }
    }

    public void restartVideo(){
        progression = 0;
        getMediaPlayer().stop();
        audio = null;
        isDone = false;
        lastTimeNano = -1L;
    }

    public SoundEvent getSoundEvent(){
        return entry.getSoundEvent();
    }

    public MediaPlayerAudio getMediaPlayer(){
        if (audio != null) return audio;
        audio = makeMediaPlayer();
        return audio;
    }

    MediaPlayerAudio makeMediaPlayer(){
        try{
            return new MediaPlayerAudio(MediaPlayerUtil.fromIdentifier(entry.getID()), false, 0, 0, 0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public VideoTexture getTexture(){
        if (texture != null) return texture;
        texture = makeTexture();
        return texture;
    }

    VideoTexture makeTexture(){
        return new VideoTexture(entry.getID());
    }

    public int getImageWidth(){
        return getTexture().width;
    }
    public int getImageHeight(){
        return getTexture().height;
    }
    public Identifier getTextureId(){
        return getTexture().getTextureId();
    }

    public void close(){
        texture.close();
        audio.close();
        instances.remove(this);
    }
    public static void closeAllInstances() {
        for (VideoInstance inst : new ArrayList<>(instances)) {
            inst.close();
        }
        instances.clear();
    }
}
