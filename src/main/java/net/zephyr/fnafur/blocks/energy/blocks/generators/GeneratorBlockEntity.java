package net.zephyr.fnafur.blocks.energy.blocks.generators;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.linking.EnergyTarget;
import net.zephyr.fnafur.blocks.linking.links.energy.EnergySourcePropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class GeneratorBlockEntity extends EnergySourcePropBlockEntity {
    public GeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.GENERATOR, pos, state);
    }

    @Override
    public boolean canLink(IEntityDataSaver link) {
        return link instanceof EnergyTarget;
    }

    @Override
    public void tick(Level world, BlockPos blockPos, BlockState state, EnergySourcePropBlockEntity entity) {
        super.tick(world, blockPos, state, entity);
    }

    @Override
    public boolean isSendingPower(IEntityDataSaver target) {
        return true;
    }
}
