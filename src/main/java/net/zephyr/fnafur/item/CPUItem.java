package net.zephyr.fnafur.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.zephyr.fnafur.blocks.utility_blocks.computer.ComputerData;
import net.zephyr.fnafur.util.ItemNbtUtil;

public class CPUItem extends Item {
    public CPUItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        String animatronic = ItemNbtUtil.getNbt(stack).getString("entity");
        if (!animatronic.isEmpty() && ComputerData.getAIAnimatronic(animatronic) instanceof ComputerData.Initializer.AnimatronicAI ai) {
            return Text.translatable(this.getTranslationKey(), ai.entityType().getName().getString());
        }
        return Text.translatable(this.getTranslationKey(), "Empty");
    }

    public static float getEntityID(String name){
        return switch(name){
            default -> 0;
            case "fnafur.cl_fred" -> 1;
            case "fnafur.cl_bon" -> 2;
            case "fnafur.cl_chica" -> 3;
            case "fnafur.cl_foxy" -> 4;
            case "fnafur.cl_spar" -> 5;
        };
    }
}
