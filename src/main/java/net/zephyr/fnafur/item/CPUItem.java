package net.zephyr.fnafur.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.zephyr.fnafur.util.ItemUtil;

public class CPUItem extends Item {
    public CPUItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {

        String extra = ItemUtil.getNbt(stack).isEmpty() ? "Empty " : "";

        return Text.translatable(this.getTranslationKey(), extra);

    }


    public static CPU getCPU(ItemStack stack){
        CPU cpu = new CPU();
        return cpu;
    }

    public static class CPU {

    }
}
