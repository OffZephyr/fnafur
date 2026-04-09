package net.zephyr.fnafur.blocks.dynamic.illusion_block;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class MimicFrames2x2 extends MimicFrames {
    public MimicFrames2x2(Properties settings) {
        super(settings);
    }
    @Override
    public int getMatrixSize(){
        return 2;
    }
}
