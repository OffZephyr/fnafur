package net.zephyr.fnafur.blocks.battery.blocks.base;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;

public class BaseGeneratorBlockEntity extends BaseElectricBlockEntity {

    public BaseGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.FUEL_GENERATOR, pos, state);
    }

    @Override
    public boolean isPowered() { return true; }
    public void interact(){}
    public void tick() {}

}
