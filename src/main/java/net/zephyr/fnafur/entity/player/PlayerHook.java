package net.zephyr.fnafur.entity.player;

import net.minecraft.world.entity.player.Player;
import net.zephyr.fnafur.rendering.lighting.ILightHolder;
import net.zephyr.fnafur.rendering.lighting.ILightItem;

public class PlayerHook {



    public static void playerTick(Player player) {

        if (player.level().isClientSide()) {
            if (player instanceof ILightHolder holder) {
                if (holder.isLightOn(player)) {
                    holder.addToWorldIfUnique();
                    //holder.updateLightInstance();
                } else {
                    holder.removeFromWorld();
                }
            }
        }
    }
}
