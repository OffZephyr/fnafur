package net.zephyr.fnafur.blocks.tile_doors;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.zephyr.fnafur.util.ItemNbtUtil;

public class TileDoorItem extends BlockItem {
    public TileDoorItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        NbtCompound nbt = ItemNbtUtil.getNbt(context.getStack());

        if(!nbt.contains("pos1")){
            nbt.putLong("pos1", context.getBlockPos().offset(context.getSide()).asLong());
            ItemNbtUtil.setNbt(context.getStack(), nbt.copy());
            return ActionResult.SUCCESS;
        }
        else{
            nbt.putLong("pos2", context.getBlockPos().offset(context.getSide()).asLong());
            ItemNbtUtil.setNbt(context.getStack(), nbt.copy());
            return super.useOnBlock(context);
        }
    }
}
