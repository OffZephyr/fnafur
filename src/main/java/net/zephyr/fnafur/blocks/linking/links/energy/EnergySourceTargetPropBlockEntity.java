package net.zephyr.fnafur.blocks.linking.links.energy;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.blocks.linking.EnergySource;
import net.zephyr.fnafur.blocks.linking.EnergyTarget;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.linking.LinkTarget;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

import java.util.ArrayList;
import java.util.List;

public abstract class EnergySourceTargetPropBlockEntity<T extends BlockEntityType<?>> extends EnergySourcePropBlockEntity implements EnergyTarget {
    int updateDepth = 0;
    public List<IEntityDataSaver> sources = new ArrayList<>();
    boolean wasUpdated = false;

    public EnergySourceTargetPropBlockEntity(T type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        LinkTarget.allTargets.add((IEntityDataSaver) this);
    }
    @Override
    public void markRemoved() {
        super.markRemoved();
        LinkSource.allSources.remove((IEntityDataSaver) this);
        cleanSources();
    }

    @Override
    public List<IEntityDataSaver> getSources() {
        return sources;
    }

    @Override
    public boolean isReceivingPower() {
        for(IEntityDataSaver ent : getSources()){
            if(ent instanceof EnergySource s){
                if(s.isSendingPower((IEntityDataSaver) this)) return true;
            }
        }
        return getSources().isEmpty();
    }
    @Override
    public int getUpdateDepth() {
        return updateDepth;
    }

    @Override
    public void setUpdateDepth(int updateDepth) {
        this.updateDepth = updateDepth;
    }
}
