package net.zephyr.fnafur.init.item_init;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.init.entity_init.ClassicInit;
import net.zephyr.fnafur.init.entity_init.EntityInit;
import net.zephyr.fnafur.item.EntitySpawnItem;

import java.util.function.Function;

public class SpawnItemInit {
    public static final Item ZEPHYRSPAWN = registerItem(
            "zephyr_spawn",
            EntitySpawnItem::new,
            new Item.Settings().maxCount(1).rarity(Rarity.COMMON),
            EntityInit.ZEPHYR
    );
    public static final Item CL_FRED_SPAWN = registerItem(
            "cl_fred_spawn",
            EntitySpawnItem::new,
            new Item.Settings().maxCount(1).rarity(Rarity.COMMON),
            ClassicInit.CL_FRED
    );
    public static final Item CL_BON_SPAWN = registerItem(
            "cl_bon_spawn",
            EntitySpawnItem::new,
            new Item.Settings().maxCount(1).rarity(Rarity.COMMON),
            ClassicInit.CL_BON
    );
    public static final Item CL_CHICA_SPAWN = registerItem(
            "cl_chica_spawn",
            EntitySpawnItem::new,
            new Item.Settings().maxCount(1).rarity(Rarity.COMMON),
            ClassicInit.CL_CHICA
    );
    public static final Item CL_FOXY_SPAWN = registerItem(
            "cl_foxy_spawn",
            EntitySpawnItem::new,
            new Item.Settings().maxCount(1).rarity(Rarity.COMMON),
            ClassicInit.CL_FOXY
    );


    public static Item registerItem(String path, Function<Item.Settings, Item> factory, Item.Settings settings, EntityType<? extends LivingEntity> type) {
        final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(FnafUniverseResuited.MOD_ID, path));
        return ((EntitySpawnItem)Items.register(registryKey, factory, settings)).setEntity(type);
    }
    public static void registerSpawnItems() {
        FnafUniverseResuited.LOGGER.info("Registering Spawn Items for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }
}
