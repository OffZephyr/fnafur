package net.zephyr.fnafur.init;

import net.zephyr.fnafur.networking.nbt_updates.NbtPayloads;
import net.zephyr.fnafur.networking.screens.ScreenPayloads;

public class NetworkingInit {
    public static void registerClientReceivers(){
        ScreenPayloads.registerClientReceivers();
        NbtPayloads.registerClientReceivers();
    }
    public static void registerServerReceivers(){
        ScreenPayloads.registerServerReceivers();
        NbtPayloads.registerServerReceivers();
    }
    public static void registerPayloads(){
        ScreenPayloads.registerPayloads();
        NbtPayloads.registerPayloads();
    }
}
