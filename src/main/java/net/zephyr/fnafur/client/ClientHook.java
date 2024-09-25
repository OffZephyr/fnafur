package net.zephyr.fnafur.client;

import net.minecraft.nbt.NbtCompound;

public class ClientHook {
    public static void openScreen(String index, NbtCompound nbt, long l){
        /*if (ScreenUtils.getScreens().containsKey(index)) {
            Screen screen = ScreenUtils.getScreens().get(index).create(Text.translatable("screen." + index + ".title"), nbt, l);
            if(MinecraftClient.getInstance().currentScreen == null || MinecraftClient.getInstance().currentScreen.getClass() != screen.getClass()) {
                MinecraftClient.getInstance().setScreen(screen);
            }
        }*/
    }

}
