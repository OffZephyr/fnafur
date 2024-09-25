package net.zephyr.fnafur.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.zephyr.fnafur.blocks.computer.ComputerData;
import net.zephyr.fnafur.util.ItemNbtUtil;

import java.util.List;

public class FloppyDiskItem extends ItemWithDescription {
    public FloppyDiskItem(Settings settings, int... tools) {
        super(settings, ItemWithDescription.COMPUTER);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) throws NumberFormatException {
        String animatronic = ItemNbtUtil.getNbt(stack).getString("entity");
        if (!animatronic.isEmpty() && ComputerData.getAIAnimatronic(animatronic) instanceof ComputerData.Initializer.AnimatronicAI ai) {
            Text before = Text.translatable("item.fnafur.floppy_disk.entity_name", "§7" + ai.entityType().getName().getString());
            Text text = Text.literal(before.getString());
            tooltip.add(text);
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
}
