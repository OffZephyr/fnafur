package net.zephyr.fnafur.client.gui.screens.creative_menu;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.impl.itemgroup.FabricItemGroupBuilderImpl;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;

import java.util.ArrayList;
import java.util.Arrays;
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
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(FnafUniverseRebuilt.MOD_ID, ID),
                FabricItemGroup.builder()
                        .title(Component.translatable(FnafUniverseRebuilt.MOD_ID + "." + ID))
                        .hideTitle()
                        .icon(() -> new ItemStack(Blocks.STONE))
                        .displayItems((displayContext, entries) -> {
                            ITEM_ENTRIES.forEach((entry) -> {
                                entries.acceptAll(entry.items(), CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
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

        public static Entry create(ItemLike icon, ItemLike[]... items) {
            List<ItemLike> list = new ArrayList<>();

            for(ItemLike[] itemCollection : items) {
                list.addAll(Arrays.asList(itemCollection));
            }

            ItemLike[] array = new ItemLike[list.size()];
            array = list.toArray(array);
            return create(new ItemStack(icon), array);
        }

        public static Entry create(ItemLike icon, ItemLike... items) {
            return create(new ItemStack(icon), items);
        }

        public static Entry create(ItemStack icon, ItemLike... items) {
            ItemStack[] itemStacks = new ItemStack[items.length];
            for (int i = 0; i < items.length; i++) {
                itemStacks[i] = new ItemStack(items[i]);
            }
            return create(icon, itemStacks);
        }

        public static Entry create(ItemLike icon, ItemStack... items) {
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
