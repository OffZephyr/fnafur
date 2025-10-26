package net.zephyr.fnafur.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.networking.entity.player.UpdateMaskStateC2SPayload;
import net.zephyr.fnafur.networking.nbt_updates.UpdateCrawlingC2SPayload;
import net.zephyr.fnafur.util.mixinAccessing.IUniversePlayer;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final KeyBinding.Category FNAF_UR =  KeyBinding.Category.create(Identifier.of(FnafUniverseRebuilt.MOD_ID, "base_category"));
    public static final String KEY_CRAWL = "key.fnafur.crawl";

    public static final String KEY_MASK = "key.fnafur.mask";

    public static KeyBinding crawlKey;
    public static KeyBinding maskKey;
    static boolean crawling = false;
    static boolean maskKeyPressed = false;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            PlayerEntity p = MinecraftClient.getInstance().player;
            if(p != null) {

                ((IUniversePlayer) p).setCrawling(crawlKey.isPressed());
                if(crawling != crawlKey.isPressed()) {
                    ClientPlayNetworking.send(new UpdateCrawlingC2SPayload(crawlKey.isPressed()));
                    crawling = crawlKey.isPressed();
                }
                boolean bl = ((IUniversePlayer) p).getMaskDelta() == 0 || ((IUniversePlayer) p).getMaskDelta() == 1.5f;
                ((IUniversePlayer) p).setCanAnimateMask(!bl);

                if(!maskKeyPressed && maskKey.isPressed() && bl){
                    MinecraftClient.getInstance().options.setPerspective(Perspective.FIRST_PERSON);
                    ClientPlayNetworking.send(new UpdateMaskStateC2SPayload(!((IUniversePlayer)MinecraftClient.getInstance().player).hasVanniMaskOn()));
                }
                maskKeyPressed = maskKey.isPressed();
            }
        });
    }

    public static void register() {
        crawlKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(KEY_CRAWL, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, FNAF_UR));
        maskKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(KEY_MASK, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, FNAF_UR));

        registerKeyInputs();
    }
}
