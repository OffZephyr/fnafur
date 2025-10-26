package net.zephyr.fnafur.util.hooks;

import net.minecraft.client.world.ClientWorld;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.linking.LinkTarget;

public class JoinHook {

    public static void joinHook(ClientWorld world){
        LinkSource.allSources.clear();
        LinkTarget.allTargets.clear();
    }
}
