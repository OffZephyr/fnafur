package net.zephyr.fnafur.blocks.energy.blocks.switches;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class RedstoneConverterBlock extends BaseSwitchBlock{

    public RedstoneConverterBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return isPowered(world, pos) ? 16 : 0;
    }

}
