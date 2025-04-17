package net.zephyr.fnafur.init.item_init;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.init.DecalInit;
import net.zephyr.fnafur.item.CPUItem;
import net.zephyr.fnafur.item.DeathCoin;
import net.zephyr.fnafur.item.IllusionDisc;
import net.zephyr.fnafur.item.energy.JerryCanItem;
import net.zephyr.fnafur.item.tablet.TabletItem;
import net.zephyr.fnafur.item.tools.DecalBookItem;
import net.zephyr.fnafur.item.tools.PaintbrushItem;
import net.zephyr.fnafur.item.tools.TapeMesurerItem;
import net.zephyr.fnafur.item.tools.WrenchItem;

import java.util.List;
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
            WrenchItem::new,
            new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.COMMON)
                    .component(DataComponentTypes.LORE, new LoreComponent(List.of(
                            Text.translatable("item.fnafur.wrench.description"),
                            Text.translatable("item.fnafur.wrench.description2")
                    )))

    );
    public static final Item PAINTBRUSH = registerItem(
            "paintbrush",
            PaintbrushItem::new,
            new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.COMMON)
                    .component(DataComponentTypes.LORE, new LoreComponent(List.of(
                            Text.translatable("item.fnafur.paintbrush.description"),
                            Text.translatable("item.fnafur.paintbrush.description2")
                    )))
    );
    public static final Item SCRAPER = registerItem(
            "scraper",
            Item::new,
            new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.COMMON)
                    .component(DataComponentTypes.LORE, new LoreComponent(List.of(
                            Text.translatable("item.fnafur.scraper.description"),
                            Text.translatable("item.fnafur.scraper.description2")
                    )))
    );
    public static final Item DECAL_BOOK = registerItem(
            "decal_book",
            DecalBookItem::new,
            new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.COMMON)
                    .component(DataComponentTypes.LORE, new LoreComponent(List.of(
                            Text.translatable("item.fnafur.decal_book.description"),
                            Text.translatable("item.fnafur.decal_book.description2"),
                            Text.translatable("item.fnafur.decal_book.description3")
                    )))
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
        final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(FnafUniverseRebuilt.MOD_ID, path));
        return Items.register(registryKey, factory, settings);
    }

    public static void registerItems() {
        DecalInit.registerStickers();
        SpawnItemInit.registerSpawnItems();
        FnafUniverseRebuilt.LOGGER.info("Registering Items for " + FnafUniverseRebuilt.MOD_ID.toUpperCase());
    }

    public static void clientRegisterItem(){
        FnafUniverseRebuilt.LOGGER.info("Registering Items on Client for " + FnafUniverseRebuilt.MOD_ID.toUpperCase());
    }
}
