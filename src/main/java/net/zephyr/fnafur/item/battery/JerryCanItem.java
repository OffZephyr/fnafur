package net.zephyr.fnafur.item.battery;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.zephyr.fnafur.blocks.battery.blocks.generators.FuelGeneratorBlock;
import net.zephyr.fnafur.blocks.battery.blocks.generators.FuelGeneratorBlockEntity;

public class JerryCanItem extends Item {

    static int JERRYCAN_MAX_CAPACITY = 25;
    int currentCapacity = JERRYCAN_MAX_CAPACITY;

    public JerryCanItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        BlockEntity entity = context.getWorld().getBlockEntity(context.getBlockPos());
        if(!(entity instanceof FuelGeneratorBlockEntity)) return super.useOnBlock(context);

        ((FuelGeneratorBlockEntity)entity).addFuel(currentCapacity);
        entity.markDirty();
        System.out.println("Bloup bloup..");

        //currentCapacity = 0;

        //ActionResult.SUCCESS makes the arm swing
        return ActionResult.SUCCESS;
    }
}
