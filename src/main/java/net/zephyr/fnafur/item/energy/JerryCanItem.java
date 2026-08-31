package net.zephyr.fnafur.item.energy;

import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.zephyr.fnafur.blocks.energy.blocks.generators.BaseGeneratorBlock;
import net.zephyr.fnafur.blocks.energy.blocks.generators.FuelGeneratorBlock;

public class JerryCanItem extends Item {

    public JerryCanItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getLevel().getBlockState(context.getClickedPos()).getBlock() instanceof BaseGeneratorBlock){
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
