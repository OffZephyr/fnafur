package net.zephyr.fnafur.client.gui.screens.creative_menu;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.init.block_init.BlockInit;

import java.util.HashMap;
import java.util.Map;

public class ItemCategoriesManager {
    public static final Map<String, ItemCategory> ITEM_CATEGORIES = new HashMap<>();

    public static void createSearchItems() {
        for (ItemCategory itemCategory : ITEM_CATEGORIES.values()) {

        }
    }
}
