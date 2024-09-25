package net.zephyr.fnafur.util.compat;

import net.fabricmc.loader.api.FabricLoader;

public interface Iris {
    static boolean isInstalled() {
        return FabricLoader.getInstance().getModContainer("iris").isPresent();
    }
}
