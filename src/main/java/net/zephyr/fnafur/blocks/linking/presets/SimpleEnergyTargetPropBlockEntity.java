package net.zephyr.fnafur.blocks.linking.presets;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.linking.links.energy.EnergyTargetPropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class SimpleEnergyTargetPropBlockEntity extends EnergyTargetPropBlockEntity {
    public SimpleEnergyTargetPropBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.SIMPLE_PROP_ENERGY_TARGET, pos, state);
    }

    @Override
    public void updateStatus(Level world, BlockPos sourcePos, IEntityDataSaver source) {
        if(world.getBlockState(worldPosition).hasProperty(BlockStateProperties.POWERED)){
            BlockState state = world.getBlockState(worldPosition).setValue(BlockStateProperties.POWERED, isReceivingPower());
            world.setBlockAndUpdate(worldPosition, state);
        }
        super.updateStatus(world, sourcePos, source);
    }
}
