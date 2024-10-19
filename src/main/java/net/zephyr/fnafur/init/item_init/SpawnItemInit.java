package net.zephyr.fnafur.init.item_init;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.item.ZephyrSpawn;
import net.zephyr.fnafur.item.spawn_items.classic.cl_bon_SpawnItem;
import net.zephyr.fnafur.item.spawn_items.classic.cl_fred_SpawnItem;

public class SpawnItemInit {
    public static final Item ZEPHYRSPAWN = registerItem("zephyr_spawn",
            new ZephyrSpawn(new Item.Settings().maxCount(1).rarity(Rarity.COMMON)));
    public static final Item CL_FRED_SPAWN = registerItem("cl_fred_spawn",
            new cl_fred_SpawnItem(new Item.Settings().maxCount(1).rarity(Rarity.COMMON)));
    public static final Item CL_BON_SPAWN = registerItem("cl_bon_spawn",
            new cl_bon_SpawnItem(new Item.Settings().maxCount(1).rarity(Rarity.COMMON)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(FnafUniverseResuited.MOD_ID, name), item);
    }
    public static void registerSpawnItems() {
        FnafUniverseResuited.LOGGER.info("Registering Spawn Items for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }
}
