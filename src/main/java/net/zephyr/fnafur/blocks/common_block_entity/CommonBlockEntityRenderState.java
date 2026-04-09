package net.zephyr.fnafur.blocks.common_block_entity;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;

public class CommonBlockEntityRenderState extends BlockEntityRenderState {
    public CompoundTag nbt;
    public Direction facing;
}
