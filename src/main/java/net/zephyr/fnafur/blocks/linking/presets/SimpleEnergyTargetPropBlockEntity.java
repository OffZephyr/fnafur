package net.zephyr.fnafur.blocks.linking.presets;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.linking.links.energy.EnergyTargetPropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class SimpleEnergyTargetPropBlockEntity extends EnergyTargetPropBlockEntity {
    public SimpleEnergyTargetPropBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.SIMPLE_PROP_ENERGY_TARGET, pos, state);
    }

    @Override
    public void updateStatus(World world, BlockPos sourcePos, IEntityDataSaver source) {
        if(world.getBlockState(pos).contains(Properties.POWERED)){
            BlockState state = world.getBlockState(pos).with(Properties.POWERED, isReceivingPower());
            world.setBlockState(pos, state);
        }
        super.updateStatus(world, sourcePos, source);
    }
}
