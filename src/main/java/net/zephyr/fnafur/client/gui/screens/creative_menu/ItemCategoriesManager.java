package net.zephyr.fnafur.client.gui.screens.creative_menu;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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
