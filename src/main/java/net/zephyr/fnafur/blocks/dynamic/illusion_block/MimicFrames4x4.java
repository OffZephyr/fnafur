package net.zephyr.fnafur.blocks.dynamic.illusion_block;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class MimicFrames4x4 extends MimicFrames {
    public MimicFrames4x4(Properties settings) {
        super(settings);
    }
    @Override
    public int getMatrixSize(){
        return 4;
    }
}
