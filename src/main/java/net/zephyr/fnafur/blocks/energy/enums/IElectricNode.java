package net.zephyr.fnafur.blocks.energy.enums;

import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/// Using a node-base system, for dispatching the power
public interface IElectricNode {
    ActionResult addNode(World world, BlockPos pos, BlockPos toAdd, BlockHitResult hit);
    ActionResult remNode(World world, BlockPos pos, BlockPos toRem, BlockHitResult hit);
    boolean isPowered(BlockView world, BlockPos pos);
    boolean canBeParent();
}
