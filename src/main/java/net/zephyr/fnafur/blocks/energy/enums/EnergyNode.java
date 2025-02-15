package net.zephyr.fnafur.blocks.energy.enums;

import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/// Using a node-base system, for dispatching the power
public interface EnergyNode {
    ActionResult addNode(World world, BlockPos pos, BlockPos toAdd, Vec3d hit);
    ActionResult remNode(World world, BlockPos pos, BlockPos toRem, Vec3d hit);
    boolean isPowered(BlockView world, BlockPos pos);
    default boolean typeOf(EnergyNodeType type){
        return  this.nodeType() == type;
    }
    EnergyNodeType nodeType();
}
