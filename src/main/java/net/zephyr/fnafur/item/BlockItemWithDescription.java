package net.zephyr.fnafur.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class BlockItemWithDescription extends BlockItem {
    List<Text> tools = new ArrayList<>();
    public BlockItemWithDescription(Block block, Item.Settings settings) {
        super(block, settings);
    }
    public BlockItemWithDescription(Block block, Item.Settings settings, int... tools) {
        this(block, settings);
        this.tools = ItemWithDescription.getTools(tools);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.addAll(ItemWithDescription.getDescription(this.tools, this.asItem()));
    }
}
