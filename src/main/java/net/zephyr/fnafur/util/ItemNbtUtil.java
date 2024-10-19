package net.zephyr.fnafur.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;

public class ItemNbtUtil {
    public static NbtCompound getNbt(ItemStack stack){
        return stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
    }
    public static ItemStack setNbt(ItemStack stack, NbtCompound nbt){
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
        return stack;
    }
    public static NbtCompound getBlockData(ItemStack stack){
        return stack.getOrDefault(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.DEFAULT).copyNbt();
    }
    public static void setBlockSata(ItemStack stack, NbtCompound nbt){
        stack.set(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.of(nbt));
    }
    /*@Environment(EnvType.CLIENT)
    public static void syncNbt(String slotName) {
        EquipmentSlot slot = EquipmentSlot.byName(slotName);

        entity.getEquippedStack(slot);
        slot.getName()
    }*/
}
