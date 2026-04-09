package net.zephyr.fnafur.blocks.energy.blocks.generators;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class CreativeGeneratorBlock extends BaseGeneratorBlock {

    public CreativeGeneratorBlock(Properties settings) {
        super(settings);
    }

    @Override
    public boolean isPowered(BlockGetter world, BlockPos pos) {
        return true;
    }
}
