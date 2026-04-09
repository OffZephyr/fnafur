package net.zephyr.fnafur.blocks.energy.enums;

import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

/// Using a node-base system, for dispatching the power
public interface EnergyNode {
    InteractionResult addNode(Level world, BlockPos pos, BlockPos toAdd, Vec3 hit);
    InteractionResult remNode(Level world, BlockPos pos, BlockPos toRem, Vec3 hit);
    boolean isPowered(BlockGetter world, BlockPos pos);
    default boolean typeOf(EnergyNodeType type){
        return  this.nodeType() == type;
    }
    EnergyNodeType nodeType();
}
