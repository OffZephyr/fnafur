package net.zephyr.fnafur.init.item_init;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Rarity;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.init.decal_init.DecalInit;
import net.zephyr.fnafur.item.animatronic.CPUItem;
import net.zephyr.fnafur.item.DeathCoin;
import net.zephyr.fnafur.item.IllusionDisc;
import net.zephyr.fnafur.item.animatronic.suit.SuitItem;
import net.zephyr.fnafur.item.energy.JerryCanItem;
import net.zephyr.fnafur.item.masks.VanniMaskItem;
import net.zephyr.fnafur.item.tools.*;

import java.util.List;
import java.util.function.Function;

public class ItemInit {
    public static final Item MOD_LOGO = registerItem(
            "fnafur",
            Item::new,
            new Item.Properties()
                    .stacksTo(0)
                    .rarity(Rarity.EPIC)
    );
    public static final Item ANIMATRONIC_SUIT = registerItem(
            "suit",
            SuitItem::new,
            new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.COMMON)
    );
    public static final Item VANNI_MASK = registerItem(
            "vanni_mask",
            VanniMaskItem::new,
            new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.COMMON)
    );
    public static final Item PIPE_WRENCH = registerItem(
            "pipe_wrench",
            WrenchItem::new,
            new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.COMMON)
                    .component(DataComponents.LORE, new ItemLore(List.of(
                            Component.translatable("item.fnafur.wrench.description"),
                            Component.translatable("item.fnafur.wrench.description2")
                    )))

    );
    public static final Item PAINTBRUSH = registerItem(
            "paintbrush",
            PaintbrushItem::new,
            new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.COMMON)
                    .component(DataComponents.LORE, new ItemLore(List.of(
                            Component.translatable("item.fnafur.paintbrush.description"),
                            Component.translatable("item.fnafur.paintbrush.description2")
                    )))
    );
    public static final Item SCRAPER = registerItem(
            "scraper",
            ScraperItem::new,
            new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.COMMON)
                    .component(DataComponents.LORE, new ItemLore(List.of(
                            Component.translatable("item.fnafur.scraper.description"),
                            Component.translatable("item.fnafur.scraper.description2")
                    )))
    );
    public static final Item DECAL_BOOK = registerItem(
            "decal_book",
            DecalBookItem::new,
            new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.COMMON)
                    .component(DataComponents.LORE, new ItemLore(List.of(
                            Component.translatable("item.fnafur.decal_book.description"),
                            Component.translatable("item.fnafur.decal_book.description2"),
                            Component.translatable("item.fnafur.decal_book.description3")
                    )))
    );
    public static final Item DEATHCOIN = registerItem(
            "deathcoin",
            DeathCoin::new,
            new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.EPIC)
    );
    public static final Item CPU = registerItem(
            "cpu",
            CPUItem::new,
            new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.COMMON)
    );
    public static final Item ILLUSIONDISC = registerItem(
            "illusion_disc",
            IllusionDisc::new,
            new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.UNCOMMON)
                    .component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.CHEST).setSwappable(false).build())
    );

    public static final Item JERRYCAN = registerItem(
            "jerrycan",
            JerryCanItem::new,
            new Item.Properties().stacksTo(1).rarity(Rarity.COMMON)
    );

    public static final Item FLASHLIGHT = registerItem(
            "flashlight",
            FlashlightItem::new,
            new Item.Properties().stacksTo(1)
    );

    public static Item registerItem(String path, Function<Item.Properties, Item> factory, Item.Properties settings) {
        final ResourceKey<Item> registryKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, path));
        return Items.registerItem(registryKey, factory, settings);
    }

    public static void registerItems() {
        DecalInit.registerStickers();
        FnafUniverseRebuilt.LOGGER.info("Registering Items for " + FnafUniverseRebuilt.MOD_ID.toUpperCase());
    }

    public static void clientRegisterItem(){
        FnafUniverseRebuilt.LOGGER.info("Registering Items on Client for " + FnafUniverseRebuilt.MOD_ID.toUpperCase());
    }
}
