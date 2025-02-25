package net.zephyr.fnafur.init.item_init;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.zephyr.fnafur.FnafUniverseResuited;
import net.zephyr.fnafur.item.CPUItem;
import net.zephyr.fnafur.item.DeathCoin;
import net.zephyr.fnafur.item.IllusionDisc;
import net.zephyr.fnafur.item.energy.JerryCanItem;
import net.zephyr.fnafur.item.tablet.TabletItem;
import net.zephyr.fnafur.item.tools.PaintbrushItem;
import net.zephyr.fnafur.item.tools.PipeWrenchItem;
import net.zephyr.fnafur.item.tools.TapeMesurerItem;
import net.zephyr.fnafur.item.tools.WrenchItem;

import java.util.function.Function;

public class ItemInit {
    public static final Item MOD_LOGO = registerItem(
            "fnafur",
            Item::new,
            new Item.Settings()
                    .maxCount(0)
                    .rarity(Rarity.EPIC)
    );
    public static final Item PIPE_WRENCH = registerItem(
            "pipe_wrench",
            PipeWrenchItem::new,
            new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.COMMON)
    );
    public static final Item WRENCH = registerItem(
            "wrench",
            WrenchItem::new,
            new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.COMMON)
    );
    public static final Item PAINTBRUSH = registerItem(
            "paintbrush",
            PaintbrushItem::new,
            new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.COMMON)
    );
    public static final Item TAPEMEASURE = registerItem(
            "tapemeasure",
            TapeMesurerItem::new,
            new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.COMMON)
    );
    public static final Item TABLET = registerItem(
            "tablet",
            TabletItem::new,
            new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.COMMON)
    );
    public static final Item DEATHCOIN = registerItem(
            "deathcoin",
            DeathCoin::new,
            new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.EPIC)
    );
    public static final Item CPU = registerItem(
            "cpu",
            CPUItem::new,
            new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.COMMON)
    );
    public static final Item ILLUSIONDISC = registerItem(
            "illusion_disc",
            IllusionDisc::new,
            new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.UNCOMMON)
                    .component(DataComponentTypes.EQUIPPABLE, EquippableComponent.builder(EquipmentSlot.CHEST).swappable(false).build())
    );

    public static final Item JERRYCAN = registerItem(
            "jerrycan",
            JerryCanItem::new,
            new Item.Settings().maxCount(1).rarity(Rarity.COMMON)
    );

    public static Item registerItem(String path, Function<Item.Settings, Item> factory, Item.Settings settings) {
        final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(FnafUniverseResuited.MOD_ID, path));
        return Items.register(registryKey, factory, settings);
    }

    public static void registerItems() {
        StickerInit.registerStickers();
        SpawnItemInit.registerSpawnItems();
        FnafUniverseResuited.LOGGER.info("Registering Items for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }

    public static void clientRegisterItem(){
        FnafUniverseResuited.LOGGER.info("Registering Items on Client for " + FnafUniverseResuited.MOD_ID.toUpperCase());
    }
}
