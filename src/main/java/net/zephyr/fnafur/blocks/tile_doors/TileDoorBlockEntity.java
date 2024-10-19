package net.zephyr.fnafur.blocks.tile_doors;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;

public class TileDoorBlockEntity extends BlockEntity {
    BlockState state;
    public double delta = 0;
    public TileDoorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.TILE_DOOR, pos, state);
        this.state = state;
    }

    public BlockState getDefaultState(){
        return this.state.getBlock().getDefaultState();
    }
}
