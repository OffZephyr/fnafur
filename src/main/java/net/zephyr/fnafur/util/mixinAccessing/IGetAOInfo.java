package net.zephyr.fnafur.util.mixinAccessing;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.BitSet;

public interface IGetAOInfo {
    float[] getBrightness();
    int[] getLight();

    public void applyAO(BlockRenderView world, BlockState state, BlockPos pos, Direction direction, float[] box, BitSet flags, boolean shaded);
}
