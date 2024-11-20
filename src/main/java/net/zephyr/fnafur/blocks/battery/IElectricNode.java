package net.zephyr.fnafur.blocks.battery;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/// Using a node-base system, for dispatching the power
public interface IElectricNode {

    void addNode(BlockPos newNode);
    void remNode(BlockPos node);
    boolean isPowered();
    
}
