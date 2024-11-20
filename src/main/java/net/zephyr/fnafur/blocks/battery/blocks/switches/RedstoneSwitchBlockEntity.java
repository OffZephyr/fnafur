package net.zephyr.fnafur.blocks.battery.blocks.switches;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.blocks.battery.blocks.base.BaseSwitchBlockEntity;

public class RedstoneSwitchBlockEntity extends BaseSwitchBlockEntity {

    boolean enabled;

    public RedstoneSwitchBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public void cycle(){
        System.out.println("switch now go -> "+enabled);
        enabled = !enabled;
    }

}
