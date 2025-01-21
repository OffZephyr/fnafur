package net.zephyr.fnafur.blocks.energy.blocks.generators;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class CreativeGeneratorBlock extends BaseGeneratorBlock {

    public CreativeGeneratorBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isPowered(BlockView world, BlockPos pos) {
        return true;
    }
}
