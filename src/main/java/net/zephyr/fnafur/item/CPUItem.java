package net.zephyr.fnafur.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.zephyr.fnafur.util.ItemNbtUtil;

public class CPUItem extends Item {
    public CPUItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {

        String extra = ItemNbtUtil.getNbt(stack).isEmpty() ? "Empty " : "";

        return Text.translatable(this.getTranslationKey(), extra);

    }
}
