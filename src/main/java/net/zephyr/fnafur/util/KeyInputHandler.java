package net.zephyr.fnafur.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.CameraType;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.networking.entity.player.UpdateMaskStateC2SPayload;
import net.zephyr.fnafur.networking.nbt_updates.UpdateCrawlingC2SPayload;
import net.zephyr.fnafur.util.mixinAccessing.IUniversePlayer;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final KeyMapping.Category FNAF_UR =  KeyMapping.Category.register(Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, "base_category"));
    public static final String KEY_CRAWL = "key.fnafur.crawl";

    public static final String KEY_MASK = "key.fnafur.mask";

    public static KeyMapping crawlKey;
    public static KeyMapping maskKey;
    static boolean crawling = false;
    static boolean maskKeyPressed = false;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Player p = Minecraft.getInstance().player;
            if(p != null) {

                ((IUniversePlayer) p).setCrawling(crawlKey.isDown());
                if(crawling != crawlKey.isDown()) {
                    ClientPlayNetworking.send(new UpdateCrawlingC2SPayload(crawlKey.isDown()));
                    crawling = crawlKey.isDown();
                }
                boolean bl = ((IUniversePlayer) p).getMaskDelta() == 0 || ((IUniversePlayer) p).getMaskDelta() == 1.5f;
                ((IUniversePlayer) p).setCanAnimateMask(!bl);

                if(!maskKeyPressed && maskKey.isDown() && bl){
                    Minecraft.getInstance().options.setCameraType(CameraType.FIRST_PERSON);
                    ClientPlayNetworking.send(new UpdateMaskStateC2SPayload(!((IUniversePlayer) Minecraft.getInstance().player).hasVanniMaskOn()));
                }
                maskKeyPressed = maskKey.isDown();
            }
        });
    }

    public static void register() {
        crawlKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(KEY_CRAWL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, FNAF_UR));
        maskKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(KEY_MASK, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, FNAF_UR));

        registerKeyInputs();
    }
}
