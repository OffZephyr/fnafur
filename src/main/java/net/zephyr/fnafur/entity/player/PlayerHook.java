package net.zephyr.fnafur.entity.player;

import net.minecraft.entity.player.PlayerEntity;
import net.zephyr.fnafur.rendering.lighting.ILightHolder;
import net.zephyr.fnafur.rendering.lighting.ILightItem;

public class PlayerHook {



    public static void playerTick(PlayerEntity player) {

        if (player.getEntityWorld().isClient()) {
            if (player instanceof ILightHolder holder) {
                if (holder.isLightOn(player)) {
                    holder.addToWorldIfUnique();
                    holder.updateLightInstance();
                } else {
                    holder.removeFromWorld();
                }
            }
        }
    }
}
