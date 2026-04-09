package net.zephyr.fnafur.blocks.linking.presets;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
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
        BlockState state = getLevel().getBlockState(getBlockPos());
        if(state.hasProperty(BlockStateProperties.POWERED)){
            return state.getValue(BlockStateProperties.POWERED) && isReceivingPower();
        }
        return false;
    }
    @Override
    public boolean canLink(IEntityDataSaver link) {
        boolean bl1 = link instanceof EnergyTarget;
        return bl1;
    }
}
