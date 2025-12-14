package net.zephyr.fnafur.client.media_player;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacv.FFmpegLogCallback;

public class FFmpegSilencer extends FFmpegLogCallback {

    @Override
    public void call(int level, BytePointer msg) {

    }
}