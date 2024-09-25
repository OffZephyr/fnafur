package net.zephyr.fnafur.blocks.arcademachine;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.blocks.GoopyBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;

public class ArcademachineBlockEntity extends GoopyBlockEntity {
    public ArcademachineBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.ARCADE_MACHINE, pos, state);
    }

}
