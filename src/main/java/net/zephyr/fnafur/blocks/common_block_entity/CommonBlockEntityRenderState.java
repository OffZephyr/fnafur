package net.zephyr.fnafur.blocks.common_block_entity;

import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;

public class CommonBlockEntityRenderState extends BlockEntityRenderState {
    public NbtCompound nbt;
    public Direction facing;
}
