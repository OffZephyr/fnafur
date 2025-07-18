package net.zephyr.fnafur.blocks.linking.presets;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.blocks.energy.blocks.switches.office_buttons.OfficeButtons;
import net.zephyr.fnafur.blocks.linking.EnergyTarget;
import net.zephyr.fnafur.blocks.linking.links.energy.EnergySourceTargetPropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class SimpleEnergySwitchPropBlockEntity extends EnergySourceTargetPropBlockEntity {
    public SimpleEnergySwitchPropBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.SIMPLE_PROP_ENERGY_SWITCH, pos, state);
    }

    @Override
    public boolean isSendingPower(IEntityDataSaver target) {
        BlockState state = getWorld().getBlockState(getPos());
        if(state.contains(Properties.POWERED)){
            return state.get(Properties.POWERED) && isReceivingPower();
        }
        return false;
    }
    @Override
    public boolean canLink(IEntityDataSaver link) {
        return link instanceof EnergyTarget;
    }
}
