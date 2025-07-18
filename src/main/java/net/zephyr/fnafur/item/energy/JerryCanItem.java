package net.zephyr.fnafur.item.energy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.blocks.energy.blocks.generators.BaseGeneratorBlock;
import net.zephyr.fnafur.blocks.energy.blocks.generators.FuelGeneratorBlock;

public class JerryCanItem extends Item {

    public JerryCanItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(context.getWorld().getBlockState(context.getBlockPos()).getBlock() instanceof BaseGeneratorBlock){
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
