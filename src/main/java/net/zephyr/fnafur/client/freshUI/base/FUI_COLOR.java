package net.zephyr.fnafur.client.freshUI.base;

public class FUI_COLOR {

    public static final int WHITE   = 0xFFFFFFFF; // A-RGB
    public static final int BLACK   = 0xFF000000;
    public static final int RED     = 0xFFFF0000;
    public static final int BLUE    = 0xFF0000FF;
    public static final int CYAN    = 0xFF00FFFF;

    /// Return the int color, of the rgb value (encoded in hex)
    public static int FromRGB(int red, int green, int blue){
        return Integer.decode("0xFF00FFFF");
    }

}
