package net.zephyr.fnafur.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

public class ItemUtil {
    public static CompoundTag getNbt(ItemStack stack){
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }
    public static ItemStack setNbt(ItemStack stack, CompoundTag nbt){
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
        return stack;
    }

    public static ItemStack setLore(ItemStack stack, Component... lore){
        stack.set(DataComponents.LORE,new ItemLore(Arrays.asList(lore)));
        return stack;
    }

    /*@Environment(EnvType.CLIENT)
    public static void syncNbt(String slotName) {
        EquipmentSlot slot = EquipmentSlot.byName(slotName);

        entity.getEquippedStack(slot);
        slot.getName()
    }*/
}
