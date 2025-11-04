package net.zephyr.fnafur.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

import java.util.Arrays;

public class ItemUtil {
    public static NbtCompound getNbt(ItemStack stack){
        return stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
    }
    public static ItemStack setNbt(ItemStack stack, NbtCompound nbt){
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
        return stack;
    }

    public static ItemStack setLore(ItemStack stack, Text... lore){
        stack.set(DataComponentTypes.LORE,new LoreComponent(Arrays.asList(lore)));
        return stack;
    }

    /*@Environment(EnvType.CLIENT)
    public static void syncNbt(String slotName) {
        EquipmentSlot slot = EquipmentSlot.byName(slotName);

        entity.getEquippedStack(slot);
        slot.getName()
    }*/
}
