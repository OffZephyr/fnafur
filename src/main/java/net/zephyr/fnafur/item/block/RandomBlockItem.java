package net.zephyr.fnafur.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

import net.minecraft.util.math.random.Random;
import net.zephyr.fnafur.init.block_init.BlockInit;

import java.util.ArrayList;
import java.util.List;

public class RandomBlockItem extends BlockItem {

    Random random = Random.create();
    public RandomBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public Block getBlock() {
        if(BlockInit.randomize) {
            if (BlockInit.RANDOM_LIST.containsKey(super.getBlock())) {
                List<Block> list = BlockInit.RANDOM_LIST.get(super.getBlock());
                if (!list.isEmpty()) {
                    return list.get(random.nextBetweenExclusive(0, list.size()));
                }
            }
        }
        return super.getBlock();
    }
}
