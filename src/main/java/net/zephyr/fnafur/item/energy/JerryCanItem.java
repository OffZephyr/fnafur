package net.zephyr.fnafur.item.energy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.blocks.energy.blocks.generators.FuelGeneratorBlock;

public class JerryCanItem extends Item {

    static int JERRYCAN_MAX_CAPACITY = 25;
    int currentCapacity = JERRYCAN_MAX_CAPACITY;

    public JerryCanItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos pos = context.getBlockPos();
        Block block  = context.getWorld().getBlockState(pos).getBlock();
        if(!(block instanceof FuelGeneratorBlock)) return ActionResult.FAIL;
        ((FuelGeneratorBlock)block).AddFuel(context.getWorld(),pos, 100);

        //ActionResult.SUCCESS makes the arm swing
        return ActionResult.SUCCESS;
    }
}
