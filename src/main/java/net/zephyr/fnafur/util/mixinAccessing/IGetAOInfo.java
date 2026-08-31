package net.zephyr.fnafur.util.mixinAccessing;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.BitSet;

public interface IGetAOInfo {
    float[] getBrightness();
    int[] getLight();

    public void applyAO(BlockAndTintGetter world, BlockState state, BlockPos pos, Direction direction, float[] box, BitSet flags, boolean shaded);
}
