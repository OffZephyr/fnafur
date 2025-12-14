package net.zephyr.fnafur.client.gui.screens.creative_menu;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.impl.itemgroup.FabricItemGroupBuilderImpl;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemCategory {
    public String ID;
    public Collection<Entry> ITEM_ENTRIES;

    public ItemCategory(String ID, Collection<Entry> entries) {
        this.ID = ID;
        ItemCategoriesManager.ITEM_CATEGORIES.putIfAbsent(ID, this);
        this.ITEM_ENTRIES = entries;
        createSearchItems();
    }

    void createSearchItems() {
        Registry.register(Registries.ITEM_GROUP, Identifier.of(FnafUniverseRebuilt.MOD_ID, ID),
                FabricItemGroup.builder()
                        .displayName(Text.translatable(FnafUniverseRebuilt.MOD_ID + "." + ID))
                        .noRenderedName()
                        .icon(() -> new ItemStack(Blocks.STONE))
                        .entries((displayContext, entries) -> {
                            ITEM_ENTRIES.forEach((entry) -> {
                                entries.addAll(entry.items(), ItemGroup.StackVisibility.SEARCH_TAB_ONLY);
                            });
                        }).build());
    }

    public static class ItemCategoryBuilder {
        private String ID;
        private final Collection<Entry> ENTRIES = new ArrayList<>();

        public ItemCategoryBuilder setID(String ID) {
            this.ID = ID;
            return this;
        }

        public ItemCategoryBuilder addItems(Entry... entries) {
            ENTRIES.addAll(List.of(entries));
            return this;
        }

        public ItemCategory build() {
            return new ItemCategory(ID, ENTRIES);
        }
    }

    public record Entry(ItemStack icon, Collection<ItemStack> items) {

        public static Entry create(ItemConvertible icon, ItemConvertible... items) {
            return create(new ItemStack(icon), items);
        }

        public static Entry create(ItemStack icon, ItemConvertible... items) {
            ItemStack[] itemStacks = new ItemStack[items.length];
            for (int i = 0; i < items.length; i++) {
                itemStacks[i] = new ItemStack(items[i]);
            }
            return create(icon, itemStacks);
        }

        public static Entry create(ItemConvertible icon, ItemStack... items) {
            return create(new ItemStack(icon), items);
        }
        public static Entry create(ItemStack icon, ItemStack... items) {
            return new Entry(icon, List.of(items));
        }
    }

    public static ItemCategory create(ItemCategory category) {
        return category;
    }

    public static ItemCategoryBuilder builder() {
        return new ItemCategoryBuilder();
    }
}
