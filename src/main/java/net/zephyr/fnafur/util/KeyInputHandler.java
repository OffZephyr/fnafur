package net.zephyr.fnafur.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.zephyr.fnafur.networking.nbt_updates.UpdateCrawlingC2SPayload;
import net.zephyr.fnafur.util.mixinAccessing.IPlayerCustomModel;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CRAWL = "key.fnafur.crawl";

    public static KeyBinding crawlKey;
    static boolean crawling = false;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(MinecraftClient.getInstance().player != null) {
                ((IPlayerCustomModel) MinecraftClient.getInstance().player).setCrawling(crawlKey.isPressed());
                if(crawling != crawlKey.isPressed()) {
                    ClientPlayNetworking.send(new UpdateCrawlingC2SPayload(crawlKey.isPressed()));
                    crawling = crawlKey.isPressed();
                }
            }
        });
    }

    public static void register() {
        crawlKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(KEY_CRAWL, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, KeyBinding.MOVEMENT_CATEGORY));

        registerKeyInputs();
    }
}
