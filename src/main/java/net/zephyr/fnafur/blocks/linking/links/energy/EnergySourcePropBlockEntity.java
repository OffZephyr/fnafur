package net.zephyr.fnafur.blocks.linking.links.energy;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.linking.EnergySource;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.props.base.PropBlockEntity;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

import java.util.ArrayList;
import java.util.List;

public abstract class EnergySourcePropBlockEntity extends PropBlockEntity implements EnergySource {

    public List<IEntityDataSaver> targets = new ArrayList<>();
    public List<BlockPos> posQueue = new ArrayList<>();
    public List<Integer> idQueue = new ArrayList<>();
    int sourceAmountSync = 0;

    public EnergySourcePropBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        LinkSource.allSources.add((IEntityDataSaver) this);
    }

    public void tick(World world, BlockPos blockPos, BlockState state, EnergySourcePropBlockEntity entity) {

        if(getSourceAmountSync() != getTargets().size()){
            updateSources(world, blockPos);
        }
        this.tick(world, blockPos, state, ((PropBlockEntity) this));
    }

    @Override
    public void markRemoved() {
        super.markRemoved();
        LinkSource.allSources.remove((IEntityDataSaver) this);
    }

    @Override
    public List<IEntityDataSaver> getTargets() {
        return targets;
    }

    @Override
    public List<BlockPos> blockTargetQueue() {
        return posQueue;
    }

    @Override
    public List<Integer> entityTargetQueue() {
        return idQueue;
    }

    @Override
    public int getSourceAmountSync() {
        return sourceAmountSync;
    }
    @Override
    public void setSourceAmountSync(int amount) {
        sourceAmountSync = amount;
    }
}
