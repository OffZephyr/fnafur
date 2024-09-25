package net.zephyr.fnafur.blocks.props.base;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;

public class PropBlockEntity extends BlockEntity {
    public PropBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.PROPS, pos, state);
    }
}
