package net.zephyr.fnafur.blocks.battery.blocks.base;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.blocks.battery.IElectricNode;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;

public class BaseSwitchBlockEntity extends BaseElectricBlockEntity {

    public BaseSwitchBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.REDSTONE_SWITCH, pos, state);
    }

    @Override
    public boolean isPowered() {
        if(this.nodes == null) return false;
        return this.nodes.stream().anyMatch(e -> {
            BlockEntity ent = getWorld().getBlockEntity(e);
            if (!(ent instanceof IElectricNode)) {
                this.nodes.remove(e);
                return false;
            }
            else return ((IElectricNode) ent).isPowered();
        });
    }
}
